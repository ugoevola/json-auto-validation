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
) : BaseKeywordValidator(
    keyword, schemaNode, schemaLocation, parentSchema, schemaContext
) {
    private val maximumValue = schemaNode.get(MAXIMUM)?.asDouble()
    private val minimumValue = schemaNode.get(MINIMUM)?.asDouble()
    private val exclusiveMinimumValue = schemaNode.get(EXCLUSIVE_MINIMUM)?.asDouble()
    private val exclusiveMaximumValue = schemaNode.get(EXCLUSIVE_MAXIMUM)?.asDouble()
    private val multipleOfValue = schemaNode.get(MULTIPLE_OF)?.asDouble()

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
        } catch (exception: KeywordValidationException) {
            executionContext.addError(
                error()
                    .message(errorMessage(exception.keyword))
                    .instanceLocation(instanceLocation)
                    .instanceNode(instanceNode)
                    .evaluationPath(executionContext.getEvaluationPath())
                    .build()
            )
        }
    }

    /**
     * The error messages of the keyword are carried by the schema itself, under
     * the errorMessage property, one message per constraint.
     */
    private fun errorMessage(keyword: String) =
        schemaNode.get(ERROR_MESSAGE_KEYWORD)
            ?.get(keyword)
            ?.asString()
            ?: DEFAULT_ERROR_MESSAGE

    private fun verifyIfEmpty(
        node: JsonNode,
    ) {
        if (node.isNull || (node.isString && node.asString().isBlank())) {
            throw KeywordValidationException(TYPE)
        }
    }

    private fun verifyMaximum(value: Double) {
        if (maximumValue != null && value > maximumValue) {
            throw KeywordValidationException(MAXIMUM)
        }
    }

    private fun verifyMinimum(value: Double) {
        if (minimumValue != null && value < minimumValue) {
            throw KeywordValidationException(MINIMUM)
        }
    }

    private fun verifyExclusiveMinimum(value: Double) {
        if (exclusiveMinimumValue != null && value <= exclusiveMinimumValue) {
            throw KeywordValidationException(EXCLUSIVE_MINIMUM)
        }
    }

    private fun verifyExclusiveMaximum(value: Double) {
        if (exclusiveMaximumValue != null && value >= exclusiveMaximumValue) {
            throw KeywordValidationException(EXCLUSIVE_MAXIMUM)
        }
    }

    private fun verifyMultipleOf(value: Double) {
        if (multipleOfValue != null && value.mod(multipleOfValue) != 0.0) {
            throw KeywordValidationException(MULTIPLE_OF)
        }
    }

    companion object {
        const val TYPE = "type"
        const val MAXIMUM = "maximum"
        const val MINIMUM = "minimum"
        const val EXCLUSIVE_MINIMUM = "exclusiveMinimum"
        const val EXCLUSIVE_MAXIMUM = "exclusiveMaximum"
        const val MULTIPLE_OF = "multipleOf"
        const val DEFAULT_ERROR_MESSAGE = "The value is invalid."
    }

}
