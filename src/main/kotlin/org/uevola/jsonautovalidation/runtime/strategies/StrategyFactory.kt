package org.uevola.jsonautovalidation.runtime.strategies

import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.uevola.jsonautovalidation.common.enums.HttpRequestPartEnum
import org.uevola.jsonautovalidation.runtime.strategies.readers.RequestReaderStrategy
import org.uevola.jsonautovalidation.runtime.strategies.validators.ValidatorStrategy
import org.uevola.jsonautovalidation.runtime.utils.CacheableProxy
import org.uevola.jsonautovalidation.runtime.utils.ExceptionUtils
import tools.jackson.databind.JsonNode
import java.lang.reflect.Parameter

@Component
internal class StrategyFactory(
    private val validators: Set<ValidatorStrategy>,
    private val requestReaders: Set<RequestReaderStrategy>,
    private val cacheableProxy: CacheableProxy
): JsonValidationExecutor {

    private val generateSchemaForParameterLambda = { parameter: Parameter ->
        cacheableProxy.generateSchemaForParameter(parameter)
    }

    override fun validate(
        request: HttpServletRequest,
        parameter: Parameter
    ) {
        val requestReader = requestReaders
            .sortedBy { it.getOrdered() }
            .find { it.resolve(parameter, request) }!!
        validate(
            requestReader.requestPart,
            requestReader.read(request),
            parameter
        )
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
                .validate(json, parameter, generateSchemaForParameterLambda)
        } catch (e: HttpClientErrorException) {
            throw ExceptionUtils
                .httpClientErrorException("Error in ${requestPart.value}: ${e.message}", e.statusCode)
        }
    }
}