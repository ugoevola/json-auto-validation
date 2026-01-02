package org.uevola.jsonautovalidation.runtime.strategies.validators

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.uevola.jsonautovalidation.common.utils.JsonUtils.objectNodeFromString
import org.uevola.jsonautovalidation.common.utils.ResourceUtils
import java.lang.reflect.Parameter

/**
 * Base class for each DTO validator, used in bean generation or to override one in the parent API.
 */
open class JsonSchemaValidator(
    private val type: Class<out Any>
) : ValidatorStrategy, AbstractValidator() {
    override fun getOrdered() = 999

    override fun resolve(parameterType: Class<*>) =
        parameterType.simpleName == type.simpleName

    override fun validate(
        json: JsonNode,
        parameter: Parameter,
        generateSchema: (Parameter) -> ObjectNode?
    ) {
        validate(json, parameter)
    }

    override fun validate(
        json: JsonNode,
        parameter: Parameter
    ) {
        val content = ResourceUtils.getResourceSchemaAsString(type.simpleName)
        validate(json, objectNodeFromString(content), getCustomMessage(parameter))
    }
}
