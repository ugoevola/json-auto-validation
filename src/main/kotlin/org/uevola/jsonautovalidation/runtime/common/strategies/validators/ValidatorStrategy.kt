package org.uevola.jsonautovalidation.runtime.common.strategies.validators

import tools.jackson.databind.JsonNode
import tools.jackson.databind.node.ObjectNode
import java.lang.reflect.Parameter

interface ValidatorStrategy {

    fun getOrdered(): Int

    fun resolve(parameterType: Class<*>): Boolean

    fun validate(
        json: JsonNode,
        parameter: Parameter,
        generateSchema: (Parameter) -> ObjectNode?
    )
}