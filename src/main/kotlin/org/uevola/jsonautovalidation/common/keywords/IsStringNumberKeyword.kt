package org.uevola.jsonautovalidation.common.keywords

import com.networknt.schema.Schema
import com.networknt.schema.SchemaContext
import com.networknt.schema.SchemaException
import com.networknt.schema.SchemaLocation
import com.networknt.schema.keyword.AbstractKeyword
import org.uevola.jsonautovalidation.common.Constants.STRING_NUMBER_KEYWORD
import org.uevola.jsonautovalidation.common.exceptions.KeywordValidationException
import tools.jackson.databind.JsonNode

internal class IsStringNumberKeyword : AbstractKeyword(STRING_NUMBER_KEYWORD) {

    @Throws(SchemaException::class, Exception::class)
    override fun newValidator(
        schemaLocation: SchemaLocation,
        schemaNode: JsonNode,
        parentSchema: Schema,
        validationContext: SchemaContext
    ) = NumberKeywordValidator(
        this,
        schemaNode,
        schemaLocation,
        parentSchema,
        validationContext,
        ::verifyIfGoodType
    )

    fun verifyIfGoodType(node: JsonNode): Int {
        val textValue = node.asString()
        if (!(node.isNumber || node.isString && NUMBER_REGEX matches textValue)) {
            throw KeywordValidationException()
        }
        return node.asString().toInt()
    }

    companion object {
        private val NUMBER_REGEX = "^(-)?\\d+([.]\\d+)?$".toRegex()
    }
}