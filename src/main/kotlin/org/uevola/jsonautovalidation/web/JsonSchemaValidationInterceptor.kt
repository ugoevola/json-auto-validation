package org.uevola.jsonautovalidation.web

import com.fasterxml.jackson.databind.JsonNode
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.uevola.jsonautovalidation.common.enums.HttpRequestPartEnum
import org.uevola.jsonautovalidation.common.utils.ExceptionUtil
import org.uevola.jsonautovalidation.core.BeansGenerator
import org.uevola.jsonautovalidation.strategies.reader.RequestReaderStrategy
import org.uevola.jsonautovalidation.strategies.validators.ValidatorStrategy
import java.lang.reflect.Parameter

@Component
class JsonSchemaValidationInterceptor(
    private val validators: List<ValidatorStrategy<*>>,
    private val requestReaders: List<RequestReaderStrategy>
) : HandlerInterceptor {

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        if (handler !is HandlerMethod) return true
        BeansGenerator
            .getParamsToValidate(handler.beanType, handler.method)
            .forEach { parameter ->
                val requestReader = requestReaders
                    .sortedBy { it.getOrdered() }
                    .find { it.resolve(parameter, request) }!!
                validate(
                    requestReader.requestPart,
                    requestReader.read(request),
                    parameter
                )
            }
        return true
    }

    private fun validate(
        requestPart: HttpRequestPartEnum,
        json: JsonNode,
        parameter: Parameter
    ) {
        try {
            validators
                .sortedBy { it.getOrdered() }
                .find { it.resolve(parameter.type) }!!
                .validate(json, parameter)
        } catch (e: HttpClientErrorException) {
            throw ExceptionUtil
                .httpClientErrorException("Error in ${requestPart.name}: ${e.message}", e.statusCode)
        }
    }
}