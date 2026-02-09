package org.uevola.jsonautovalidation.aot.generators

import com.networknt.schema.dialect.DialectId.DRAFT_2020_12
import io.github.oshai.kotlinlogging.KotlinLogging
import org.uevola.jsonautovalidation.aot.StrategyFactory
import org.uevola.jsonautovalidation.aot.schemas.jsonSchemaBaseTemplate
import org.uevola.jsonautovalidation.aot.utils.ClassPathUtils.getDtoClassesToValidate
import org.uevola.jsonautovalidation.api.annotations.jsonValidationAnnotation.IsJsonValidation
import org.uevola.jsonautovalidation.common.Constants.REQUIRED_ERROR_MESSAGE_KEYWORD
import org.uevola.jsonautovalidation.common.extensions.*
import org.uevola.jsonautovalidation.common.utils.JsonUtils.newObjectNode
import org.uevola.jsonautovalidation.common.utils.ResourceUtils
import tools.jackson.databind.node.ObjectNode
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.time.Duration
import kotlin.time.measureTime

internal object SchemasGenerator {
    private val logger = KotlinLogging.logger {}
    private var nbGeneratedSchemas = 0
    private val strategyFactory = StrategyFactory

    fun generateJsonSchemaFiles() {
        logger.info { "Json schemas generation..." }
        val elapsed: Duration = measureTime {
            val annotatedClasses = getDtoClassesToValidate()
            for (clazz in annotatedClasses) {
                nbGeneratedSchemas++
                ResourceUtils.addSchemaResource(
                    clazz.simpleName,
                    getRootJsonSchema(clazz.kotlin).toString()
                )
            }
        }
        logger.info { "Json schemas generation completed in ${elapsed.inWholeMilliseconds}ms" }
        logger.info { "Number of Json schemas generated: $nbGeneratedSchemas" }
    }

    private fun getRootJsonSchema(clazz: KClass<*>): ObjectNode {
        val rootNode = getJsonSchema(clazz)
        rootNode.put($$"$schema", DRAFT_2020_12)
        return rootNode
    }

    private fun getJsonSchema(clazz: KClass<*>): ObjectNode {
        val properties = clazz.memberProperties
            .map { getJsonForProperty(it) }
            .merge()
        val requiredProperties = clazz.getRequiredJsonPropertiesNames()
        val requiredErrorMessages = getRequiredErrorMessagesForProperties(requiredProperties)
        val values = mapOf(
            "title" to clazz.qualifiedName,
            "required" to requiredProperties,
            "properties" to properties,
            REQUIRED_ERROR_MESSAGE_KEYWORD to requiredErrorMessages
        )
        return jsonSchemaBaseTemplate.resolveTemplate(values, "", false)
    }

    private fun getRequiredErrorMessagesForProperties(
        requiredProperties: List<String>
    ): ObjectNode {
        val res = newObjectNode()
        val requiredMessages = newObjectNode()
        requiredProperties.forEach { requiredMessages.put(it, "The field $it is required.") }
        res.set("required", requiredMessages)
        return res
    }

    private fun getJsonForProperty(property: KProperty1<out Any, *>): ObjectNode? {
        val value = property
            .getAnnotations()
            .filter { it.annotationClass.hasAnnotation<IsJsonValidation>() }
            .map { strategyFactory.generateSchemaFor(it, property, ::getJsonSchema) }
            .merge()
        if (value.isEmpty) return null
        val json = newObjectNode()
        json.set(property.getJsonPropertyName(), value)
        return json
    }
}