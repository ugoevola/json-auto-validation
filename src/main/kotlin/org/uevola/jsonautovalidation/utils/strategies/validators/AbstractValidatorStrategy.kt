package org.uevola.jsonautovalidation.utils.strategies.validators

import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.ListableBeanFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ResolvableType
import org.springframework.web.client.HttpClientErrorException
import org.uevola.jsonautovalidation.utils.ExceptionUtil
import org.uevola.jsonautovalidation.utils.annotations.jsonValidationAnnotation.IsJsonValidation
import org.uevola.jsonautovalidation.utils.enums.HttpRequestPartEnum
import org.uevola.jsonautovalidation.utils.validators.DefaultJsonSchemaValidator
import org.uevola.jsonautovalidation.utils.validators.JsonSchemaValidator
import java.lang.reflect.Field
import java.lang.reflect.Parameter

abstract class AbstractValidatorStrategy {

    @Autowired
    private lateinit var beanFactory: ListableBeanFactory

    @Autowired
    private lateinit var defaultJsonSchemaValidator: DefaultJsonSchemaValidator

    abstract fun getOrdered(): Int

    abstract fun resolve(parameter: Parameter): Boolean

    abstract fun validate(request: HttpServletRequest, parameter: Parameter)

    protected fun validate(
        target: Class<*>,
        json: String,
        httpRequestPartEnum: HttpRequestPartEnum,
        customMessages: Map<String, String>
    ) {
        try {
            getBeanValidator(target)?.validate(json, customMessages)
        } catch (e: HttpClientErrorException) {
            throw ExceptionUtil
                .httpClientErrorException("Error in ${httpRequestPartEnum.name}: ${e.message}", e.statusCode)
        }
    }

    protected fun validate(parameter: Parameter, json: String, httpRequestPartEnum: HttpRequestPartEnum) {
        try {
            defaultJsonSchemaValidator.validate(parameter, json)
        } catch (e: HttpClientErrorException) {
            throw ExceptionUtil
                .httpClientErrorException("Error in ${httpRequestPartEnum.name}: ${e.message}", e.statusCode)
        }
    }

    protected fun getCustomMessage(parameter: Parameter) = parameter.type.declaredFields
        .associate { it.name to getFieldMessage(it) }
        .filter { it.value.isNotEmpty() }

    private fun getBeanValidator(target: Class<*>): JsonSchemaValidator<*>? {
        val names = beanFactory.getBeanNamesForType(
            ResolvableType.forClassWithGenerics(
                JsonSchemaValidator::class.java,
                target
            )
        )
        return if (names.isNotEmpty()) beanFactory.getBean(names[0], JsonSchemaValidator::class.java) else null
    }

    private fun getFieldMessage(field: Field) = field.annotations
        .filter { annotation ->
            annotation.annotationClass.annotations.any { it is IsJsonValidation }
        }
        .map { it.annotationClass.java.getMethod("message").invoke(it) as String }
        .filter { it.isNotEmpty() }
        .joinToString(", ")
}