package org.uevola.jsonautovalidation.utils.keywords

import com.fasterxml.jackson.databind.JsonNode
import com.networknt.schema.*
import org.uevola.jsonautovalidation.utils.Util.STRING_INTEGER_KEYWORD
import org.uevola.jsonautovalidation.utils.exceptions.KeywordValidationException
import java.text.MessageFormat

class IsStringIntegerKeyword: AbstractKeyword(STRING_INTEGER_KEYWORD) {
    @Throws(JsonSchemaException::class, Exception::class)
    override fun newValidator(
        schemaPath: String,
        schemaNode: JsonNode,
        parentSchema: JsonSchema,
        validationContext: ValidationContext
    ): AbstractJsonValidator {
        return object : NumberKeyword() {
            override fun validate(
                executionContext: ExecutionContext?,
                node: JsonNode,
                rootNode: JsonNode,
                at: String
            ): Set<ValidationMessage> {
                try {
                    verifyIfEmpty(node)
                    verifyIfInteger(node, at)
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

            private fun verifyIfInteger(
                node: JsonNode,
                at: String
            ) {
                val regex = "^(-)?\\d+\$".toRegex()
                if (!(node.isInt || node.isTextual && regex matches node.asText())) {
                    val message = ValidationMessage.Builder()
                        .path(at)
                        .format(MessageFormat("{0}: the value must be an integer or a string containing an integer"))
                        .build()
                    throw KeywordValidationException(message)
                }
            }
        }
    }
}