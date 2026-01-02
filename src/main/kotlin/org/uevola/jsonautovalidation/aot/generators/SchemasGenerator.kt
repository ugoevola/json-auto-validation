package org.uevola.jsonautovalidation.aot.generators

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.oshai.kotlinlogging.KotlinLogging
import org.uevola.jsonautovalidation.annotations.jsonValidationAnnotation.IsJsonValidation
import org.uevola.jsonautovalidation.aot.StrategyFactory
import org.uevola.jsonautovalidation.common.extensions.*
import org.uevola.jsonautovalidation.common.schemas.jsonSchemaBaseTemplate
import org.uevola.jsonautovalidation.common.utils.ClassPathUtils.getDtoClassesToValidate
import org.uevola.jsonautovalidation.common.utils.JsonUtils.newObjectNode
import org.uevola.jsonautovalidation.common.utils.ResourceUtils
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.time.Duration
import kotlin.time.measureTime

internal object SchemasGenerator {
    private val logger = KotlinLogging.logger {}

    private var getJsonSchema: (clazz: KClass<*>) -> ObjectNode?
    private val strategyFactory = StrategyFactory

    init {
        getJsonSchema = getJsonSchema()
    }

    fun generateJsonSchemaFiles() {
        logger.info { "Json schemas generation..." }
        val elapsed: Duration = measureTime {
            val annotatedClasses = getDtoClassesToValidate()
            for (clazz in annotatedClasses) {
                ResourceUtils.addSchemaResource(
                    clazz.simpleName,
                    getJsonSchema(clazz.kotlin)?.toString()
                )
            }
        }
        logger.info { "Json schemas generation completed in ${elapsed.inWholeMilliseconds}ms" }
    }

    private fun getJsonSchema() = { clazz: KClass<*> ->
        val properties = clazz.memberProperties
            .map { getJsonForProperty(it) }
            .merge()
        val requiredProperties = clazz.getRequiredJsonPropertiesNames()
        val values = mapOf(
            "title" to clazz.qualifiedName,
            "required" to requiredProperties,
            "properties" to properties
        )
        jsonSchemaBaseTemplate.resolveTemplate(values)
    }

    private fun getJsonForProperty(property: KProperty1<out Any, *>): ObjectNode? {
        val value = property
            .getAnnotations()
            .filter { it.annotationClass.hasAnnotation<IsJsonValidation>() }
            .map { strategyFactory.generateSchemaFor(it, property, getJsonSchema) }
            .merge()
        if (value.isEmpty) return null
        val json = newObjectNode()
        json.set<JsonNode>(property.getJsonPropertyName(), value)
        return json
    }
}