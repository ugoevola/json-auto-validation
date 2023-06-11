package org.uevola.jsonautovalidation.utils.generators

import mu.KLogging
import org.json.JSONObject
import org.reflections.Reflections
import org.springframework.stereotype.Component
import org.uevola.jsonautovalidation.configuration.JsonValidationConfiguration
import org.uevola.jsonautovalidation.utils.Utils
import org.uevola.jsonautovalidation.utils.annotations.JsonValidation
import org.uevola.jsonautovalidation.utils.annotations.jsonValidationAnnotation.IsRequired
import org.uevola.jsonautovalidation.utils.strategies.generators.JsonSchemaGeneratorStrategy
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

@Component
open class JsonGenerator(
    private val jsonAutoValidatorConfiguration: JsonValidationConfiguration,
    private val utils: Utils,
    private val strategies: List<JsonSchemaGeneratorStrategy>,
) {

    private var jsonSchemaBaseTemplate: JSONObject
    private var getJsonSchema: (clazz: KClass<*>) -> JSONObject?

    companion object : KLogging()

    init {
        logger.info { "Json schemas génération" }
        jsonSchemaBaseTemplate = jsonSchemaBaseTemplate()
        getJsonSchema = getJsonSchema()
    }

    fun generateJsonSchemaFiles() {
        val reflections = Reflections(jsonAutoValidatorConfiguration.dtoPackageName)
        val annotatedClasses = reflections.getTypesAnnotatedWith(JsonValidation::class.java)
        for (clazz in annotatedClasses) {
            utils.addSchemaResource(clazz.simpleName, getJsonSchema(clazz.kotlin)?.toString())
        }
    }

    private fun jsonSchemaBaseTemplate() =
        JSONObject(utils
            .getSchemaResource("JsonSchemaBaseTemplate")
            ?.inputStream?.bufferedReader().use { it?.readText() })

    private fun getJsonSchema() = { clazz: KClass<*> ->
        val properties = clazz.memberProperties
            .map { getJsonForProperty(it) }
            .fold(JSONObject()) { acc, jsonObject ->
                utils.mergeJSONObject(acc, jsonObject)
                acc
            }
        val requiredProperties = clazz.memberProperties.filter { property ->
            property.javaField?.declaredAnnotations?.any { it.annotationClass.java == IsRequired::class.java } ?: false
        }.map { it.name }
        val values = mapOf(
            "title" to clazz.qualifiedName,
            "required" to requiredProperties,
            "properties" to properties
        )
        utils.resolveTemplate(jsonSchemaBaseTemplate, values)
    }

    private fun getJsonForProperty(property: KProperty1<out Any, *>): JSONObject? {
        var annotations = property.javaField?.declaredAnnotations ?: emptyArray<Annotation>()
        val basicAnnotation = utils.getAnnotationForBaseClass(property.returnType)
        if (basicAnnotation != null && !annotations.any { it.annotationClass == basicAnnotation.annotationClass }) {
            annotations = annotations.plus(basicAnnotation)
        }
        val value = annotations.map { annotation ->
            strategies.sortedBy { it.getOrdered() }.find { it.resolve(annotation) }
                ?.generate(annotation, property, getJsonSchema)
        }.fold(JSONObject()) { acc, jsonObject ->
            utils.mergeJSONObject(acc, jsonObject)
            acc
        }
        if (value.isEmpty) return null
        val json = JSONObject()
        json.put(property.name, value)
        return json
    }
}

