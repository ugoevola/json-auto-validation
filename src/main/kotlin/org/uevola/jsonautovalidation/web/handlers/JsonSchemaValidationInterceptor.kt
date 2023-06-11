package org.uevola.jsonautovalidation.web.handlers

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.ListableBeanFactory
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.uevola.jsonautovalidation.utils.Utils
import org.uevola.jsonautovalidation.utils.annotations.Validate
import org.uevola.jsonautovalidation.utils.strategies.validators.ValidatorStrategy

@Component
class JsonSchemaValidationInterceptor(
    private val utils: Utils,
    private val strategies: List<ValidatorStrategy>,
) : HandlerInterceptor {

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        if (handler is HandlerMethod) {
            for (parameter in utils.getParamsToValidate(handler.beanType ,handler.method)) {
                strategies.sortedBy { it.getOrdered() }
                    .find { it.resolve(parameter) }
                    ?.validate(request, parameter)
            }
        }
        return true
    }
}