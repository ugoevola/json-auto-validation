package org.uevola.jsonautovalidation.strategies.validators

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import java.lang.reflect.Parameter

interface ValidatorStrategy<T> {

    fun getOrdered(): Int

    fun resolve(parameterType: Class<*>): Boolean

    fun validate(
        json: JsonNode,
        parameter: Parameter,
        generateSchema: (Parameter) -> ObjectNode?
    )

    fun validate(
        json: JsonNode,
        parameter: Parameter
    )
}