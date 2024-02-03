package org.uevola.jsonautovalidation.strategies.validators

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.uevola.jsonautovalidation.common.utils.JsonUtil.objectNodeFromString
import org.uevola.jsonautovalidation.common.utils.ResourcesUtil
import java.lang.reflect.Parameter

open class JsonSchemaValidator(
    private val type: Class<out Any>
) : ValidatorStrategy, AbstractValidator() {
    override fun getOrdered() = 0

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
        val content = ResourcesUtil.getResourceSchemaAsString(type.simpleName)
        validate(json, objectNodeFromString(content), getCustomMessage(parameter))
    }

}