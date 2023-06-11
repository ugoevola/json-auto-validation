package org.uevola.jsonautovalidation.utils.strategies.validators

import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.ListableBeanFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ResolvableType
import org.springframework.web.client.HttpClientErrorException
import org.uevola.jsonautovalidation.utils.Utils
import org.uevola.jsonautovalidation.utils.enums.HttpRequestPartEnum
import org.uevola.jsonautovalidation.utils.validators.DefaultJsonSchemaValidator
import org.uevola.jsonautovalidation.utils.validators.JsonSchemaValidator
import java.lang.reflect.Parameter

abstract class AbstractValidatorStrategy {

    @Autowired
    protected lateinit var utils: Utils

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
            throw utils.httpClientErrorException("Error in ${httpRequestPartEnum.name}: ${e.message}", e.statusCode)
        }
    }

    protected fun validate(parameter: Parameter, json: String, httpRequestPartEnum: HttpRequestPartEnum) {
        try {
            defaultJsonSchemaValidator.validate(parameter, json)
        } catch (e: HttpClientErrorException) {
            throw utils.httpClientErrorException("Error in ${httpRequestPartEnum.name}: ${e.message}", e.statusCode)
        }
    }

    private fun getBeanValidator(target: Class<*>): JsonSchemaValidator<*>? {
        val names = beanFactory.getBeanNamesForType(
            ResolvableType.forClassWithGenerics(
                JsonSchemaValidator::class.java,
                target
            )
        )
        return if (names.isNotEmpty()) beanFactory.getBean(names[0], JsonSchemaValidator::class.java) else null
    }
}