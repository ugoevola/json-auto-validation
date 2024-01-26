package org.uevola.jsonautovalidation.strategies.validators

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.uevola.jsonautovalidation.common.utils.JsonUtil.objectNodeFromString
import org.uevola.jsonautovalidation.common.utils.ResourcesUtil
import java.lang.reflect.Parameter
import kotlin.reflect.KClass

class JsonSchemaValidator<T: Any> @PublishedApi internal constructor(
    private val type: KClass<out T>
): ValidatorStrategy<T>, AbstractValidator() {
    companion object{
        inline operator fun <reified T: Any> invoke() = JsonSchemaValidator(T::class)
    }
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
        val content = ResourcesUtil.getResourceSchemaAsString(type.simpleName!!)
        validate(json, objectNodeFromString(content), getCustomMessage(parameter))
    }

}