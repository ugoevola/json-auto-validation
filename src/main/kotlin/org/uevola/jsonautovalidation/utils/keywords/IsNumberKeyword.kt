package org.uevola.jsonautovalidation.utils.keywords

import com.fasterxml.jackson.databind.JsonNode
import com.networknt.schema.*
import org.uevola.jsonautovalidation.utils.Utils.Companion.STRING_NUMBER_KEYWORD
import java.text.MessageFormat

class IsNumberKeyword : AbstractKeyword(STRING_NUMBER_KEYWORD) {
    @Throws(JsonSchemaException::class, Exception::class)
    override fun newValidator(
        schemaPath: String,
        schemaNode: JsonNode,
        parentSchema: JsonSchema,
        validationContext: ValidationContext
    ): AbstractJsonValidator {
        return object : AbstractJsonValidator() {
            override fun validate(node: JsonNode, rootNode: JsonNode, at: String): Set<ValidationMessage> {

                val regex = "^(-)?\\d+([.]\\d+)?\$".toRegex()
                var result = emptySet<ValidationMessage>()

                if (!(node.isNumber || node.isTextual && regex matches node.asText()))
                    result = result.plus(
                        ValidationMessage.Builder()
                            .path(at)
                            .format(MessageFormat("{0}: the value must be an number or a string containing an number"))
                            .build()
                    )

                if (schemaNode.has(MAXIMUM) && node.asText().toInt() >= schemaNode.get(MAXIMUM).asText().toInt())
                    result = result.plus(
                        ValidationMessage.Builder()
                            .path(at)
                            .arguments(schemaNode.get(MAXIMUM).asText())
                            .format(MessageFormat("{0}: the maximum authorized value is {1}"))
                            .build()
                    )

                if (schemaNode.has(MINIMUM) && node.asText().toInt() <= schemaNode.get(MINIMUM).asText().toInt())
                    result = result.plus(
                        ValidationMessage.Builder()
                            .path(at)
                            .arguments(schemaNode.get(MINIMUM).asText())
                            .format(MessageFormat("{0}: the maximum authorized value is {1}"))
                            .build()
                    )

                if (schemaNode.has(EXCLUSIVE_MINIMUM) && node.asText().toInt() < schemaNode.get(EXCLUSIVE_MINIMUM)
                        .asText().toInt()
                )
                    result = result.plus(
                        ValidationMessage.Builder()
                            .path(at)
                            .arguments((schemaNode.get(EXCLUSIVE_MINIMUM).asText().toInt() + 1).toString())
                            .format(MessageFormat("{0}: the minimum authorized value is {1}"))
                            .build()
                    )

                if (schemaNode.has(EXCLUSIVE_MAXIMUM) && node.asText().toInt() > schemaNode.get(EXCLUSIVE_MAXIMUM)
                        .asText().toInt()
                )
                    result = result.plus(
                        ValidationMessage.Builder()
                            .path(at)
                            .arguments((schemaNode.get(EXCLUSIVE_MAXIMUM).asText().toInt() - 1).toString())
                            .format(MessageFormat("{0}: the maximum authorized value is {1}"))
                            .build()
                    )

                if (schemaNode.has(MULTIPLE_OF) && node.asText().toInt()
                        .mod(schemaNode.get(MULTIPLE_OF).asText().toInt()) != 0
                )
                    result = result.plus(
                        ValidationMessage.Builder()
                            .path(at)
                            .arguments(schemaNode.get(MULTIPLE_OF).asText())
                            .format(MessageFormat("{0}: the value lust be a multiple of {1}"))
                            .build()
                    )

                return result
            }
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