package org.uevola.jsonautovalidation.runtime.common.strategies

import org.springframework.web.client.HttpClientErrorException
import org.uevola.jsonautovalidation.common.enums.HttpRequestPartEnum
import org.uevola.jsonautovalidation.runtime.common.config.JsonValidationProperties
import org.uevola.jsonautovalidation.runtime.common.strategies.validators.ValidatorStrategy
import org.uevola.jsonautovalidation.runtime.common.utils.CacheableProxy
import org.uevola.jsonautovalidation.runtime.common.utils.ExceptionUtils
import tools.jackson.databind.JsonNode
import java.lang.reflect.Parameter

internal abstract class AbstractStrategyFactory(
    private val validators: Set<ValidatorStrategy>,
    private val cacheableProxy: CacheableProxy,
    protected val properties: JsonValidationProperties
) {

    protected val generateSchemaForParameterLambda = { parameter: Parameter ->
        cacheableProxy.generateSchemaForParameter(parameter)
    }

    protected fun validate(
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