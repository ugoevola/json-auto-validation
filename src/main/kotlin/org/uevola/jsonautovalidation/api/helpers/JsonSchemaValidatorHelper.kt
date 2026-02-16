package org.uevola.jsonautovalidation.api.helpers

import org.springframework.stereotype.Component
import org.uevola.jsonautovalidation.runtime.common.strategies.validators.AbstractJsonSchemaValidator
import tools.jackson.databind.JsonNode

@Component
final class JsonSchemaValidatorHelper : AbstractJsonSchemaValidator() {

    fun validate(
        json: JsonNode,
        schema: JsonNode
    ) {
        super.baseValidate(json, schema)
    }

}