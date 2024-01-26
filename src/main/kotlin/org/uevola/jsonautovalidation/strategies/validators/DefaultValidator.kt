package org.uevola.jsonautovalidation.strategies.validators

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.stereotype.Component
import java.lang.reflect.Parameter

@Component
class DefaultValidator: ValidatorStrategy<Any>, AbstractValidator() {

    override fun getOrdered() = Int.MAX_VALUE

    override fun resolve(parameterType: Class<*>) = true

    override fun validate(
        json: JsonNode,
        parameter: Parameter,
        generateSchema: (Parameter) -> ObjectNode?
    ) {
        val schema = generateSchema(parameter)
        if (schema != null) {
            validate(json, schema, emptyMap())
        }
    }

    override fun validate(
        json: JsonNode,
        parameter: Parameter
    ) { }
}