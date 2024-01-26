package org.uevola.jsonautovalidation.web

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.uevola.jsonautovalidation.common.extensions.getParamsToValidate
import org.uevola.jsonautovalidation.strategies.StrategyFactory

@Component
class JsonSchemaValidationInterceptor(
    private val strategyFactory: StrategyFactory
) : HandlerInterceptor {

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        if (handler !is HandlerMethod) return true
        handler.method
            .getParamsToValidate(handler.beanType)
            .forEach { strategyFactory.validate(request, it) }
        return true
    }

}