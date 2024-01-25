package org.uevola.jsonautovalidation.strategies.validators

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.cache.annotation.Cacheable
import org.uevola.jsonautovalidation.common.annotations.jsonValidationAnnotation.IsJsonValidation
import org.uevola.jsonautovalidation.common.utils.ResourcesUtil
import java.lang.reflect.Field
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
        parameter: Parameter
    ) {
        val objectMapper = ObjectMapper()
        val content = ResourcesUtil.getResourceSchemaAsString(type.simpleName!!)
        validate(json, objectMapper.readTree(content), getCustomMessage(parameter))
    }

    @Cacheable
    private fun getCustomMessage(parameter: Parameter) = parameter.type.declaredFields
        .associate { it.name to getFieldMessage(it) }
        .filter { it.value.isNotEmpty() }

    private fun getFieldMessage(field: Field) = field.annotations
        .filter { annotation ->
            annotation.annotationClass.annotations.any { it is IsJsonValidation }
        }
        .map { it.annotationClass.java.getMethod("message").invoke(it) as String }
        .filter { it.isNotEmpty() }
        .joinToString(", ")

}