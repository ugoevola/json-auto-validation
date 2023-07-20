package org.uevola.jsonautovalidation.utils

import mu.KLogging
import org.json.JSONObject
import org.reflections.Reflections
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.core.io.support.ResourcePatternUtils
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.uevola.jsonautovalidation.configuration.JsonValidationConfiguration
import org.uevola.jsonautovalidation.utils.annotations.Validate
import org.uevola.jsonautovalidation.utils.annotations.jsonValidationAnnotation.*
import java.io.File
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.nio.charset.StandardCharsets
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.isSubclassOf

@Component
class Utils(
    private val jsonAutoValidatorConfiguration: JsonValidationConfiguration,
    private val resourceLoader: ResourceLoader,
) {
    /**
     * return an HttpClientErrorException with default statusText REQUEST_VALIDATION_KO
     *
     * @param message the exception message
     * @param statusCode the exception code
     */
    fun httpClientErrorException(message: String, statusCode: HttpStatusCode) =
        HttpClientErrorException(message, statusCode, "REQUEST_VALIDATION_KO", null, null, null)


    /**
     * return an HttpServerErrorException with default statusText REQUEST_VALIDATION_KO
     *
     * @param message the exception message
     * @param statusCode the exception code
     */
    fun httpServerErrorException(message: String, statusCode: HttpStatusCode) =
        HttpServerErrorException(message, statusCode, "REQUEST_VALIDATION_KO", null, null, null)

    /**
     * retrieve the resource corresponding to the schema name
     * recovery is based on the property "json-validation.resources-path"
     *
     * @param schemaName the name of the resource schema
     */
    fun getSchemaResource(schemaName: String): Resource? {
        val resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
        return resourcePatternResolver.getResources(
            "classpath*:"
                .plus(jsonAutoValidatorConfiguration.resourcesPath)
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
        try {
            mkDir("", jsonAutoValidatorConfiguration.resourcesPath)
            mkDir(jsonAutoValidatorConfiguration.resourcesPath, GENERATED_JSON_PATH)
            val resourcePath = ClassPathResource(jsonAutoValidatorConfiguration.resourcesPath + GENERATED_JSON_PATH)
            val file = File(resourcePath.file, schemaName + SCHEMA_JSON_EXT)
            if (!file.exists()) {
                file.createNewFile()
            }
            file.writeText(content, StandardCharsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * create directory in the targeted resource path
     *
     * @param resourcePath
     * @param dirName
     */
    private fun mkDir(resourcePath: String, dirName: String) {
        val resource = ClassPathResource(resourcePath)
        val directory = File(resource.file, dirName)
        if (!directory.exists()) {
            directory.mkdir()
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

    fun getControllersToValidate(): Set<Class<*>> {
        val reflections = Reflections(resolveRootPackage())
        val controllers = reflections.getTypesAnnotatedWith(Controller::class.java)
        controllers.addAll(reflections.getTypesAnnotatedWith(RestController::class.java))
        return controllers
    }

    fun getMethodsToValidate(controller: Class<*>) =
        if (controller.annotations.any { it is Validate })
            controller.declaredMethods.toList()
        else
            controller.declaredMethods.filter { method ->
                method.annotations.any { annotation ->
                    annotation is RequestMapping || annotation.annotationClass.annotations.any { it is RequestMapping }
                }
            }

    fun getParamsToValidate(controller: Class<*>, method: Method): List<Parameter> =
        if (controller.annotations.any { it is Validate } || method.annotations.any { it is Validate })
            method.parameters.toList()
        else
            method.parameters.filter { parameter -> parameter.annotations.any { it is Validate } }

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

    fun getCustomMessage(parameter: Parameter) = parameter.type.declaredFields
        .associate { it.name to getFieldMessage(it) }
        .filter { it.value.isNotEmpty() }

    private fun getFieldMessage(field: Field) = field.annotations
        .filter { annotation ->
            annotation.annotationClass.annotations.any { it is IsJsonValidation }
        }
        .map { it.annotationClass.java.getMethod("message").invoke(it) as String }
        .filter { it.isNotEmpty() }
        .joinToString(", ")

    companion object : KLogging() {
        private const val SCHEMA_JSON_EXT = ".schema.json"

        const val GENERATED_JSON_PATH = "/generated-schemas"
        const val STRING_INTEGER_KEYWORD = "string-integer"
        const val STRING_NUMBER_KEYWORD = "string-number"

        fun resolveRootPackage(): String {
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

        private fun getRootPackage(className: String): String {
            val lastDotIndex = className.lastIndexOf('.')
            return if (lastDotIndex != -1) className.substring(0, lastDotIndex) else ""
        }
    }
}

