package org.uevola.jsonautovalidation.core

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KLogging
import org.json.JSONObject
import org.springframework.context.annotation.Configuration
import org.uevola.jsonautovalidation.common.annotations.JsonValidation
import org.uevola.jsonautovalidation.common.annotations.jsonValidationAnnotation.IsJsonValidation
import org.uevola.jsonautovalidation.common.annotations.jsonValidationAnnotation.IsRequired
import org.uevola.jsonautovalidation.common.utils.*
import org.uevola.jsonautovalidation.configuration.JsonValidationConfig
import org.uevola.jsonautovalidation.core.strategies.schemaGenerators.JsonSchemaGeneratorStrategy
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@ExperimentalTime
@Configuration
open class SchemasGenerator(
    private val objectMapper: ObjectMapper,
    private val jsonGenerationStrategies: Set<JsonSchemaGeneratorStrategy>
) {

    private var getJsonSchema: (clazz: KClass<*>) -> JSONObject?

    companion object : KLogging() {
        private val JSON_SCHEMA_BASE_TEMPLATE =
            JSONObject(
                ResourcesUtil
                    .getResourceSchema("JsonSchemaBaseTemplate")
                    ?.inputStream?.bufferedReader().use { it?.readText() })
    }

    init {
        getJsonSchema = getJsonSchema()
        generateJsonSchemaFiles()
    }

    private fun generateJsonSchemaFiles() {
        logger.info { "Json schemas generation..." }
        val elapsed: Duration = measureTime {
            val annotatedClasses =
                ClassesUtil.getAnnotatedClassesIn(JsonValidationConfig.dtoPackageName, JsonValidation::class.java)
            for (clazz in annotatedClasses) {
                ResourcesUtil.addSchemaResource(clazz.simpleName, getJsonSchema(clazz.kotlin)?.toString())
            }
        }
        logger.info { "Json schemas generation completed in ${elapsed.inWholeMilliseconds}ms" }
    }

    private fun getJsonSchema() = { clazz: KClass<*> ->
        val properties = clazz.memberProperties
            .map { getJsonForProperty(it) }
            .fold(JSONObject()) { acc, jsonObject ->
                JsonUtil.mergeJSONObject(acc, jsonObject)
                acc
            }
        val requiredProperties = clazz.memberProperties.filter { property ->
            property.javaField?.declaredAnnotations?.any { it.annotationClass.java == IsRequired::class.java } ?: false
        }.map { getJsonPropertyName(it) }
        val values = mapOf(
            "title" to clazz.qualifiedName,
            "required" to requiredProperties,
            "properties" to properties
        )
        JsonUtil.resolveTemplate(JSON_SCHEMA_BASE_TEMPLATE, values)
    }

    private fun getJsonForProperty(property: KProperty1<out Any, *>): JSONObject? {
        var annotations = property
            .javaField
            ?.declaredAnnotations
            ?: emptyArray<Annotation>()
        val correspondedAnnotation = property.returnType.getCorrespondedJsonValidationAnnotation()
        if (
            annotations.none { AnnotationsUtil.overriderAutomaticAnnotations().contains(it) }
            && correspondedAnnotation != null
            && annotations.none { it.annotationClass == correspondedAnnotation.annotationClass }
        ) {
            annotations = annotations.plus(correspondedAnnotation)
        }
        val value = annotations
            .filter { it.annotationClass.hasAnnotation<IsJsonValidation>() }
            .map { annotation ->
                jsonGenerationStrategies
                    .sortedBy { it.getOrdered() }
                    .find { it.resolve(annotation) }
                    ?.generate(annotation, property, getJsonSchema)
            }.fold(JSONObject()) { acc, jsonObject ->
                JsonUtil.mergeJSONObject(acc, jsonObject)
                acc
            }
        if (value.isEmpty) return null
        val json = JSONObject()
        json.put(getJsonPropertyName(property), value)
        return json
    }

    private fun getJsonPropertyName(property: KProperty1<out Any, *>): String {
        val jsonPropertyAnnotation = property.javaField?.getDeclaredAnnotation(JsonProperty::class.java)
        return jsonPropertyAnnotation?.value
            ?: objectMapper
                .deserializationConfig
                .propertyNamingStrategy
                .nameForField(objectMapper.deserializationConfig, null, property.name)
    }
}

