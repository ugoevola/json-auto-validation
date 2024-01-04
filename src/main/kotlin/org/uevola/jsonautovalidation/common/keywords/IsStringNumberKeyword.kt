package org.uevola.jsonautovalidation.common.keywords

import com.fasterxml.jackson.databind.JsonNode
import com.networknt.schema.*
import org.uevola.jsonautovalidation.common.Constants.STRING_NUMBER_KEYWORD
import org.uevola.jsonautovalidation.common.exceptions.KeywordValidationException
import java.text.MessageFormat

class IsStringNumberKeyword: AbstractKeyword(STRING_NUMBER_KEYWORD) {
    @Throws(JsonSchemaException::class, Exception::class)
    override fun newValidator(
        schemaPath: String,
        schemaNode: JsonNode,
        parentSchema: JsonSchema,
        validationContext: ValidationContext
    ) = object: NumberKeyword() {
        override fun validate(
            executionContext: ExecutionContext?,
            node: JsonNode,
            rootNode: JsonNode,
            at: String
        ): Set<ValidationMessage> {
            try {
                verifyIfEmpty(node)
                verifyIfNumber(node, at)
                verifyMaximum(schemaNode, node, at)
                verifyMinimum(schemaNode, node, at)
                verifyExclusiveMinimum(schemaNode, node, at)
                verifyExclusiveMaximum(schemaNode, node, at)
                verifyMultipleOf(schemaNode, node, at)
            } catch(exception: KeywordValidationException) {
                return if (exception.validationMessage == null) emptySet()
                else setOf(exception.validationMessage)
            }
            return emptySet()
        }

        private fun verifyIfNumber(
            node: JsonNode,
            at: String
        ) {
            val regex = "^(-)?\\d+([.]\\d+)?\$".toRegex()
            if (!(node.isNumber || node.isTextual && regex matches node.asText())) {
                val message = ValidationMessage.Builder()
                    .path(at)
                    .format(MessageFormat("{0}: the value must be an number or a string containing an number"))
                    .build()
                throw KeywordValidationException(message)
            }
        }

    }
}