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
    val verifyIfGoodType: (JsonNode) -> Int
): BaseKeywordValidator(
    keyword, schemaNode, schemaLocation, parentSchema, schemaContext
) {
    private val value = schemaNode.asString()
    private val maximumValue = schemaNode.get(MAXIMUM).asString().toInt()
    private val minimumValue = schemaNode.get(MINIMUM).asString().toInt()
    private val exclusiveMinimumValue = schemaNode.get(EXCLUSIVE_MINIMUM).asString().toInt()
    private val exclusiveMaximumValue = schemaNode.get(EXCLUSIVE_MAXIMUM).asString().toInt()
    private val multipleOfValue = schemaNode.get(MULTIPLE_OF).asString().toInt()

    override fun validate(
        executionContext: ExecutionContext,
        instanceNode: JsonNode,
        instance: JsonNode,
        instanceLocation: NodePath
    ) {
        try {
            verifyIfEmpty(instanceNode)
            val value = verifyIfGoodType(instanceNode)
            verifyMaximum(value)
            verifyMinimum(value)
            verifyExclusiveMinimum(value)
            verifyExclusiveMaximum(value)
            verifyMultipleOf(value)
        } catch (_: KeywordValidationException) {
            executionContext.addError(error()
                .message(instanceNode.get(ERROR_MESSAGE_KEYWORD).toString())
                .arguments(value)
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
        if (node.asString().isEmpty()) throw KeywordValidationException()
    }

    private fun verifyMaximum(value: Int) {
        if (schemaNode.has(MAXIMUM) && value >= maximumValue) {
            throw KeywordValidationException()
        }
    }

    private fun verifyMinimum(value: Int) {
        if (schemaNode.has(MINIMUM) && value <= minimumValue) {
            throw KeywordValidationException()
        }
    }

    private fun verifyExclusiveMinimum(value: Int) {
        if (schemaNode.has(EXCLUSIVE_MINIMUM) && value < exclusiveMinimumValue) {
            throw KeywordValidationException()
        }
    }

    private fun verifyExclusiveMaximum(value: Int) {
        if (schemaNode.has(EXCLUSIVE_MAXIMUM) && value > exclusiveMaximumValue) {
            throw KeywordValidationException()
        }
    }

    private fun verifyMultipleOf(value: Int) {
        if (schemaNode.has(MULTIPLE_OF) && value.mod(multipleOfValue) != 0) {
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