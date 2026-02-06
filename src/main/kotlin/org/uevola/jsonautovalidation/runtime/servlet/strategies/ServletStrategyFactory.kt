package org.uevola.jsonautovalidation.runtime.servlet.strategies

import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component
import org.uevola.jsonautovalidation.runtime.common.config.JsonValidationProperties
import org.uevola.jsonautovalidation.runtime.common.strategies.AbstractStrategyFactory
import org.uevola.jsonautovalidation.runtime.servlet.strategies.readers.RequestReaderStrategy
import org.uevola.jsonautovalidation.runtime.common.strategies.validators.ValidatorStrategy
import org.uevola.jsonautovalidation.runtime.common.utils.CacheableProxy
import java.lang.reflect.Parameter

@Component
internal class ServletStrategyFactory(
    validators: Set<ValidatorStrategy>,
    private val requestReaders: Set<RequestReaderStrategy>,
    cacheableProxy: CacheableProxy,
    properties: JsonValidationProperties
): AbstractStrategyFactory(validators, cacheableProxy, properties), JsonValidationExecutor {

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
}