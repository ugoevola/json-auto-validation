package org.uevola.jsonautovalidation.common.keywords

import com.networknt.schema.ExecutionContext
import com.networknt.schema.Schema
import com.networknt.schema.SchemaContext
import com.networknt.schema.SchemaLocation
import com.networknt.schema.keyword.BaseKeywordValidator
import com.networknt.schema.keyword.Keyword
import com.networknt.schema.path.NodePath
import org.uevola.jsonautovalidation.common.Constants.ERROR_MESSAGE_KEYWORD
import org.uevola.jsonautovalidation.common.exceptions.KeywordValidationException
import tools.jackson.databind.JsonNode

internal class NumberKeywordValidator(
    keyword: Keyword,
    schemaNode: JsonNode,
    schemaLocation: SchemaLocation,
    parentSchema: Schema,
    schemaContext: SchemaContext,
    val verifyIfGoodType: (JsonNode) -> Double
): BaseKeywordValidator(
    keyword, schemaNode, schemaLocation, parentSchema, schemaContext
) {
    private val maximumValue = schemaNode.get(MAXIMUM).asDouble()
    private val minimumValue = schemaNode.get(MINIMUM).asDouble()
    private val exclusiveMinimumValue = schemaNode.get(EXCLUSIVE_MINIMUM).asDouble()
    private val exclusiveMaximumValue = schemaNode.get(EXCLUSIVE_MAXIMUM).asDouble()
    private val multipleOfValue = schemaNode.get(MULTIPLE_OF).asDouble()

    override fun validate(
        executionContext: ExecutionContext,
        instanceNode: JsonNode,
        instance: JsonNode,
        instanceLocation: NodePath
    ) {
        try {
            verifyIfEmpty(instanceNode)
            val castValue = verifyIfGoodType(instanceNode)
            verifyMaximum(castValue)
            verifyMinimum(castValue)
            verifyExclusiveMinimum(castValue)
            verifyExclusiveMaximum(castValue)
            verifyMultipleOf(castValue)
        } catch (_: KeywordValidationException) {
            executionContext.addError(error()
                .message(instanceNode.get(ERROR_MESSAGE_KEYWORD).toString())
                .arguments(instanceNode.toString())
                .instanceLocation(instanceLocation)
                .instanceNode(instanceNode)
                .evaluationPath(executionContext.getEvaluationPath())
                .build()
            )
        }
    }

    private fun verifyIfEmpty(
        node: JsonNode,
    ) {
        if (node.isEmpty) throw KeywordValidationException()
    }

    private fun verifyMaximum(value: Double) {
        if (schemaNode.has(MAXIMUM) && value >= maximumValue) {
            throw KeywordValidationException()
        }
    }

    private fun verifyMinimum(value: Double) {
        if (schemaNode.has(MINIMUM) && value <= minimumValue) {
            throw KeywordValidationException()
        }
    }

    private fun verifyExclusiveMinimum(value: Double) {
        if (schemaNode.has(EXCLUSIVE_MINIMUM) && value < exclusiveMinimumValue) {
            throw KeywordValidationException()
        }
    }

    private fun verifyExclusiveMaximum(value: Double) {
        if (schemaNode.has(EXCLUSIVE_MAXIMUM) && value > exclusiveMaximumValue) {
            throw KeywordValidationException()
        }
    }

    private fun verifyMultipleOf(value: Double) {
        if (schemaNode.has(MULTIPLE_OF) && value.mod(multipleOfValue) != 0.0) {
            throw KeywordValidationException()
        }
    }

    companion object {
        const val MAXIMUM = "maximum"
        const val MINIMUM = "minimum"
        const val EXCLUSIVE_MINIMUM = "exclusiveMinimum"
        const val EXCLUSIVE_MAXIMUM = "exclusiveMaximum"
        const val MULTIPLE_OF = "multipleOf"
    }

}