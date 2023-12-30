package org.uevola.jsonautovalidation.utils

import mu.KLogging
import org.json.JSONObject
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.uevola.jsonautovalidation.configuration.JsonValidationConfig
import org.uevola.jsonautovalidation.utils.annotations.Validate
import org.uevola.jsonautovalidation.utils.annotations.jsonValidationAnnotation.*
import org.uevola.jsonautovalidation.utils.exceptions.JsonValidationGenerationException
import java.io.File
import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.nio.charset.StandardCharsets
import java.nio.file.FileSystems
import java.nio.file.Files
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.isSubclassOf


object Util: KLogging() {

    private const val SCHEMA_JSON_EXT = ".schema.json"
    private const val GENERATED_JSON_PATH = "/generated-schemas"
    const val STRING_INTEGER_KEYWORD = "string-integer"
    const val STRING_NUMBER_KEYWORD = "string-number"

    var rootPackage: String

    init {
        rootPackage = resolveRootPackage()
    }

    private fun resolveRootPackage(): String {
        val scanner = ClassPathScanningCandidateComponentProvider(false)
        scanner.addIncludeFilter(AnnotationTypeFilter(SpringBootApplication::class.java))
        val candidates = scanner.findCandidateComponents("")
        if (candidates.isNotEmpty()) {
            val candidate = candidates.iterator().next()
            val candidateClassName = candidate.beanClassName
            if (candidateClassName != null) {
                return getRootPackage(candidateClassName)
            }
        }
        throw IllegalStateException("Unable to resolve the root package.")
    }

    fun findClassesByAnnotation(basePackage: String, annotationType: Class<out Annotation>): Set<Class<*>> {
        try {
            val scanner = ClassPathScanningCandidateComponentProvider(false)
            scanner.addIncludeFilter(AnnotationTypeFilter(annotationType))
            return scanner.findCandidateComponents(basePackage)
                .map { Class.forName(it.beanClassName) }
                .toSet()
        } catch (exception: Exception) {
            throw JsonValidationGenerationException("Error while scanning $basePackage for annotation ${annotationType.name}", exception)
        }
    }

    /**
     * retrieve the resource corresponding to the schema name
     * recovery is based on the property "json-validation.resources-path"
     *
     * @param schemaName the name of the resource schema
     */
    fun getSchemaResource(schemaName: String): Resource? {
        val resolver = PathMatchingResourcePatternResolver()
        return resolver.getResources(
            "classpath*:"
                .plus(JsonValidationConfig.resourcesPath)
                .plus("**/")
                .plus(schemaName)
                .plus(SCHEMA_JSON_EXT)
        ).find { schemaName.plus(SCHEMA_JSON_EXT) == it.filename }
    }

    /**
     * add a resource schema in the resource path corresponding to the property "json-validation.resources-path"
     *
     * @param schemaName the name of the resource schema
     * @param content the content of the resource
     */
    fun addSchemaResource(schemaName: String, content: String?) {
        if (content == null) return
        val pathResource = ClassPathResource("")
        val uri = pathResource.uri
        if (uri.scheme == "file") {
            createDirectories(JsonValidationConfig.resourcesPath)
            createFile(
                JsonValidationConfig.resourcesPath,
                schemaName + SCHEMA_JSON_EXT,
                content
            )
        } else {
            createFileJar(
                JsonValidationConfig.resourcesPath,
                schemaName + SCHEMA_JSON_EXT,
                content
            )
        }
    }

    private fun createDirectories(basePath: String) {
        val pathResource = ClassPathResource(basePath)
        val directory = File(pathResource.uri.path, GENERATED_JSON_PATH)
        if (!directory.exists()) {
            directory.mkdirs()
        }
    }

    private fun createFile(basePath: String, name: String, content: String) {
        val pathResource = ClassPathResource(basePath)
        val file = File(pathResource.uri.path, name)
        if (!file.exists()) {
            file.createNewFile()
        }
        file.writeText(content, StandardCharsets.UTF_8)
    }

    private fun createFileJar(basePath: String, filename: String, content: String) {
        val pathResource = ClassPathResource(basePath)
        val fileSystem = FileSystems.newFileSystem(pathResource.uri, emptyMap<String, Any>())
        val filePath = fileSystem.getPath(basePath, GENERATED_JSON_PATH, filename)
        val parentPath = filePath.parent
        if (!Files.isDirectory(parentPath)) {
            Files.createDirectories(parentPath)
        }
        if (!Files.exists(filePath)) {
            Files.createFile(filePath)
            Files.newBufferedWriter(filePath, StandardCharsets.UTF_8).use { writer ->
                writer.write(content)
            }
        }
        fileSystem.close()
    }

    fun isJavaOrKotlinClass(clazz: Class<*>) = clazz.name.startsWith("kotlin.") ||
            clazz.name.startsWith("java.")

    fun getAnnotationForBaseClass(type: KType): Annotation? {
        val returnType = type.classifier as? KClass<*> ?: return null

        return when {
            returnType.isSubclassOf(String::class) -> IsString()
            returnType.isSubclassOf(Enum::class) -> IsEnum()
            returnType.isSubclassOf(Int::class) -> IsInteger()
            returnType.isSubclassOf(Number::class) -> IsNumber()
            returnType.isSubclassOf(Boolean::class) -> IsBool()
            else -> null
        }
    }


    /**
     * used to resolve json-auto-validation template
     * each property value that need to be replaced follows that template : {{property_name}}
     * and its value is linked to it's name in the values attribute map
     *
     * @param template the template that contains 0 or more properties to be replaced
     * @param values the map that bind each property name to its value
     */
    fun resolveTemplate(template: JSONObject?, values: Map<String, Any?>): JSONObject {
        val result = JSONObject(template.toString())
        result.keys().forEach { key ->
            val value = result.get(key)
            if (value is JSONObject) result.put(key, resolveTemplate(value, values))
            if (value !is String) return@forEach
            val regex = Regex("^@\\{(.+)}$")
            val match = regex.find(value) ?: return@forEach
            val capturedValue = match.groupValues[1]
            result.put(key, values[capturedValue])
        }
        return result
    }

    fun mergeJSONObject(json1: JSONObject, json2: JSONObject?): JSONObject {
        if (json2 == null) return json1
        JSONObject.getNames(json2)?.forEach { json1.put(it, json2.get(it)) }
        return json1
    }

    private fun getRootPackage(className: String): String {
        val lastDotIndex = className.lastIndexOf('.')
        return if (lastDotIndex != -1) className.substring(0, lastDotIndex) else ""
    }

    fun getParamsToValidate(controller: Class<*>, method: Method): List<Parameter> =
        if (controller.annotations.any { it is Validate } || method.annotations.any { it is Validate })
            method.parameters.toList()
        else
            method.parameters.filter { parameter -> parameter.annotations.any { it is Validate } }

}
