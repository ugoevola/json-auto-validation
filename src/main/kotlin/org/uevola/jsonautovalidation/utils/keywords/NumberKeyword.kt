package org.uevola.jsonautovalidation.utils.keywords

import com.fasterxml.jackson.databind.JsonNode
import com.networknt.schema.AbstractJsonValidator
import com.networknt.schema.ValidationMessage
import org.uevola.jsonautovalidation.utils.exceptions.KeywordValidationException
import java.text.MessageFormat

abstract class NumberKeyword: AbstractJsonValidator() {

    protected fun verifyMaximum(
        schemaNode: JsonNode,
        node: JsonNode,
        at: String
    ) {
        if (schemaNode.has(MAXIMUM) && node.asText().toInt() >= schemaNode.get(MAXIMUM).asText().toInt()) {
            val message = ValidationMessage.Builder()
                .path(at)
                .arguments(schemaNode.get(MAXIMUM).asText())
                .format(MessageFormat("{0}: the maximum authorized value is {1}"))
                .build()
            throw KeywordValidationException(message)
        }
    }

    protected fun verifyMinimum(
        schemaNode: JsonNode,
        node: JsonNode,
        at: String
    ) {
        if (schemaNode.has(MINIMUM) && node.asText().toInt() <= schemaNode.get(MINIMUM).asText().toInt()) {
            val message = ValidationMessage.Builder()
                .path(at)
                .arguments(schemaNode.get(MINIMUM).asText())
                .format(MessageFormat("{0}: the minimum authorized value is {1}"))
                .build()
            throw KeywordValidationException(message)
        }
    }

    protected fun verifyExclusiveMinimum(
        schemaNode: JsonNode,
        node: JsonNode,
        at: String
    ) {
        if (schemaNode.has(EXCLUSIVE_MINIMUM) && node.asText().toInt() < schemaNode.get(EXCLUSIVE_MINIMUM)
                .asText().toInt()
        ) {
            val message = ValidationMessage.Builder()
                .path(at)
                .arguments((schemaNode.get(EXCLUSIVE_MINIMUM).asText().toInt() + 1).toString())
                .format(MessageFormat("{0}: the minimum authorized value is {1}"))
                .build()
            throw KeywordValidationException(message)
        }
    }

    protected fun verifyExclusiveMaximum(
        schemaNode: JsonNode,
        node: JsonNode,
        at: String
    ) {
        if (schemaNode.has(EXCLUSIVE_MAXIMUM) && node.asText().toInt() > schemaNode.get(EXCLUSIVE_MAXIMUM)
                .asText().toInt()
        ) {
            val message = ValidationMessage.Builder()
                .path(at)
                .arguments((schemaNode.get(EXCLUSIVE_MAXIMUM).asText().toInt() - 1).toString())
                .format(MessageFormat("{0}: the maximum authorized value is {1}"))
                .build()
            throw KeywordValidationException(message)
        }
    }

    protected fun verifyMultipleOf(
        schemaNode: JsonNode,
        node: JsonNode,
        at: String
    ) {
        if (schemaNode.has(MULTIPLE_OF) && node.asText().toInt()
                .mod(schemaNode.get(MULTIPLE_OF).asText().toInt()) != 0
        ) {
            val message = ValidationMessage.Builder()
                .path(at)
                .arguments(schemaNode.get(MULTIPLE_OF).asText())
                .format(MessageFormat("{0}: the value lust be a multiple of {1}"))
                .build()
            throw KeywordValidationException(message)
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