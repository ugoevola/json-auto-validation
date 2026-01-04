package org.uevola.jsonautovalidation.runtime.strategies.validators

import org.uevola.jsonautovalidation.common.utils.JsonUtils.objectNodeFromString
import org.uevola.jsonautovalidation.common.utils.ResourceUtils
import tools.jackson.databind.JsonNode
import tools.jackson.databind.node.ObjectNode
import java.lang.reflect.Parameter

/**
 * Base class for each DTO validator, used in bean generation or to override one in the parent API.
 */
abstract class JsonSchemaValidator(
    private val type: Class<out Any>
) : ValidatorStrategy, AbstractValidator() {
    override fun getOrdered() = 999

    override fun resolve(parameterType: Class<*>) =
        parameterType.simpleName == type.simpleName

    override fun validate(
        json: JsonNode,
        parameter: Parameter,
        generateSchema: (Parameter) -> ObjectNode?
    ) = validate(json)

    open fun validate(json: JsonNode) {
        val content = ResourceUtils.getResourceSchemaAsString(type.simpleName)
        super.baseValidate(json, objectNodeFromString(content))
    }

}
