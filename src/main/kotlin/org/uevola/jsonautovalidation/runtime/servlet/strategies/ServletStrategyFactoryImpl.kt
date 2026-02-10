package org.uevola.jsonautovalidation.runtime.servlet.strategies

import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import org.uevola.jsonautovalidation.runtime.common.config.JsonAutoValidationProperties
import org.uevola.jsonautovalidation.runtime.common.strategies.AbstractStrategyFactory
import org.uevola.jsonautovalidation.runtime.common.strategies.validators.ValidatorStrategy
import org.uevola.jsonautovalidation.runtime.common.utils.CacheableProxy
import org.uevola.jsonautovalidation.runtime.servlet.strategies.readers.ServletRequestReaderStrategy
import java.lang.reflect.Parameter

@Component
@ConditionalOnProperty(
    name = ["json-validation.web-stack"],
    havingValue = "servlet",
    matchIfMissing = true
)
internal class ServletStrategyFactoryImpl(
    validators: Set<ValidatorStrategy>,
    private val requestReaders: Set<ServletRequestReaderStrategy>,
    cacheableProxy: CacheableProxy,
    properties: JsonAutoValidationProperties
) : AbstractStrategyFactory(validators, cacheableProxy, properties), ServletStrategyFactory {

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