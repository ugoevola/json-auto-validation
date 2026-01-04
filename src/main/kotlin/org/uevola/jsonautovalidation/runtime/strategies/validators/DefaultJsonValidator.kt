package org.uevola.jsonautovalidation.runtime.strategies.validators

import org.springframework.stereotype.Component
import tools.jackson.databind.JsonNode
import tools.jackson.databind.node.ObjectNode
import java.lang.reflect.Parameter

@Component
internal class DefaultJsonValidator : ValidatorStrategy, AbstractValidator() {

    override fun getOrdered() = Int.MAX_VALUE

    override fun resolve(parameterType: Class<*>) = true

    override fun validate(
        json: JsonNode,
        parameter: Parameter,
        generateSchema: (Parameter) -> ObjectNode?
    ) {
        val schema = generateSchema(parameter)
        if (schema != null) {
            super.baseValidate(json, schema)
        }
    }
}