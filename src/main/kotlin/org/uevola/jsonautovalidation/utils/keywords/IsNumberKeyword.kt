package org.uevola.jsonautovalidation.utils.keywords

import com.fasterxml.jackson.databind.JsonNode
import com.networknt.schema.*
import org.uevola.jsonautovalidation.utils.Util.STRING_NUMBER_KEYWORD
import java.text.MessageFormat

class IsNumberKeyword: AbstractKeyword(STRING_NUMBER_KEYWORD) {
    @Throws(JsonSchemaException::class, Exception::class)
    override fun newValidator(
        schemaPath: String,
        schemaNode: JsonNode,
        parentSchema: JsonSchema,
        validationContext: ValidationContext
    ) = object: AbstractJsonValidator() {
        override fun validate(
            executionContext: ExecutionContext?,
            node: JsonNode,
            rootNode: JsonNode,
            at: String
        ): Set<ValidationMessage> {
            var result = emptySet<ValidationMessage>()
            val numberMessage = verifyIfNumber(node, at)
            val numberMaximum = verifyMaximum(node, at)
            val numberMinimum = verifyMinimum(node, at)
            val numberExclusiveMinimum = verifyExclusiveMinimum(node, at)
            val numberExclusiveMaximum = verifyExclusiveMaximum(node, at)
            val multipleOf = verifyMultipleOf(node, at)
            if (numberMessage != null) result = result.plus(numberMessage)
            if (numberMaximum != null) result = result.plus(numberMaximum)
            if (numberMinimum != null) result = result.plus(numberMinimum)
            if (numberExclusiveMinimum != null) result = result.plus(numberExclusiveMinimum)
            if (numberExclusiveMaximum != null) result = result.plus(numberExclusiveMaximum)
            if (multipleOf != null) result = result.plus(multipleOf)
            return result
        }

        private fun verifyIfNumber(
            node: JsonNode,
            at: String
        ): ValidationMessage? {
            val regex = "^(-)?\\d+([.]\\d+)?\$".toRegex()
            if (!(node.isNumber || node.isTextual && regex matches node.asText()))
                return ValidationMessage.Builder()
                    .path(at)
                    .format(MessageFormat("{0}: the value must be an number or a string containing an number"))
                    .build()
            return null
        }

        private fun verifyMaximum(
            node: JsonNode,
            at: String
        ) = if (schemaNode.has(MAXIMUM) && node.asText().toInt() >= schemaNode.get(MAXIMUM).asText().toInt())
            ValidationMessage.Builder()
                .path(at)
                .arguments(schemaNode.get(MAXIMUM).asText())
                .format(MessageFormat("{0}: the maximum authorized value is {1}"))
                .build()
        else null

        private fun verifyMinimum(
            node: JsonNode,
            at: String
        ) = if (schemaNode.has(MINIMUM) && node.asText().toInt() <= schemaNode.get(MINIMUM).asText().toInt())
            ValidationMessage.Builder()
                .path(at)
                .arguments(schemaNode.get(MINIMUM).asText())
                .format(MessageFormat("{0}: the minimum authorized value is {1}"))
                .build()
        else null

        private fun verifyExclusiveMinimum(
            node: JsonNode,
            at: String
        ) = if (schemaNode.has(EXCLUSIVE_MINIMUM) && node.asText().toInt() < schemaNode.get(EXCLUSIVE_MINIMUM)
                    .asText().toInt()
            )
                ValidationMessage.Builder()
                    .path(at)
                    .arguments((schemaNode.get(EXCLUSIVE_MINIMUM).asText().toInt() + 1).toString())
                    .format(MessageFormat("{0}: the minimum authorized value is {1}"))
                    .build()
        else null

        private fun verifyExclusiveMaximum(
            node: JsonNode,
            at: String
        ) = if (schemaNode.has(EXCLUSIVE_MAXIMUM) && node.asText().toInt() > schemaNode.get(EXCLUSIVE_MAXIMUM)
                .asText().toInt()
        )
            ValidationMessage.Builder()
                .path(at)
                .arguments((schemaNode.get(EXCLUSIVE_MAXIMUM).asText().toInt() - 1).toString())
                .format(MessageFormat("{0}: the maximum authorized value is {1}"))
                .build()
        else null

        private fun verifyMultipleOf(
            node: JsonNode,
            at: String
        ) = if (schemaNode.has(MULTIPLE_OF) && node.asText().toInt()
                .mod(schemaNode.get(MULTIPLE_OF).asText().toInt()) != 0
            )
                ValidationMessage.Builder()
                    .path(at)
                    .arguments(schemaNode.get(MULTIPLE_OF).asText())
                    .format(MessageFormat("{0}: the value lust be a multiple of {1}"))
                    .build()
        else null

    }

    companion object {
        const val MAXIMUM = "maximum"
        const val MINIMUM = "minimum"
        const val EXCLUSIVE_MINIMUM = "exclusiveMinimum"
        const val EXCLUSIVE_MAXIMUM = "exclusiveMaximum"
        const val MULTIPLE_OF = "multipleOf"
    }
}