package org.uevola.jsonautovalidation.runtime.servlet.web

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.uevola.jsonautovalidation.api.resolvers.JsonValidationAware
import org.uevola.jsonautovalidation.common.extensions.getParamsToValidate
import org.uevola.jsonautovalidation.runtime.servlet.strategies.ServletStrategyFactory
import java.lang.reflect.Method
import java.lang.reflect.Parameter

@Component
@ConditionalOnProperty(
    name = ["json-validation.web-stack"],
    havingValue = "servlet",
    matchIfMissing = true
)
class JsonAutoValidationInterceptor(
    private val strategyFactory: ServletStrategyFactory,
    private val jsonValidationAwares: List<JsonValidationAware>
) : HandlerInterceptor {

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        if (handler !is HandlerMethod) return true
        handler.method.getParamsToValidate(handler.beanType)
            .forEach { parameter ->
                val effectiveClass = resolveEffectiveClass(handler.method, parameter)
                strategyFactory.validate(request, parameter, effectiveClass)
            }
        return true
    }

    private fun resolveEffectiveClass(method: Method, parameter: Parameter): Class<*>? {
        val index = method.parameters.indexOf(parameter)
        val methodParameter = MethodParameter(method, index)
        return jsonValidationAwares
            .find { it.supportsParameter(methodParameter) }?.getRequestDtoType()
    }

}