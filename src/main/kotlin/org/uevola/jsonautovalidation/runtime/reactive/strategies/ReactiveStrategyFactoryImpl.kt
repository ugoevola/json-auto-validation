package org.uevola.jsonautovalidation.runtime.reactive.strategies

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.uevola.jsonautovalidation.runtime.common.config.JsonAutoValidationProperties
import org.uevola.jsonautovalidation.runtime.common.strategies.AbstractStrategyFactory
import org.uevola.jsonautovalidation.runtime.common.strategies.validators.ValidatorStrategy
import org.uevola.jsonautovalidation.runtime.common.utils.CacheableProxy
import org.uevola.jsonautovalidation.runtime.reactive.strategies.readers.ReactiveRequestReaderStrategy
import reactor.core.publisher.Mono
import java.lang.reflect.Parameter

@Component
@ConditionalOnProperty(
    name = ["json-validation.web-stack"],
    havingValue = "webflux",
    matchIfMissing = false
)
internal class ReactiveStrategyFactoryImpl(
    validators: Set<ValidatorStrategy>,
    private val requestReaders: Set<ReactiveRequestReaderStrategy>,
    cacheableProxy: CacheableProxy,
    properties: JsonAutoValidationProperties
) : AbstractStrategyFactory(validators, cacheableProxy, properties), ReactiveStrategyFactory {

    override fun validate(
        exchange: ServerWebExchange,
        parameter: Parameter
    ): Mono<Void> {
        val requestReader = requestReaders
            .sortedBy { it.getOrdered() }
            .find { it.resolve(parameter, exchange) }
            ?: return Mono.empty()

        return requestReader.read(exchange)
            .flatMap { json ->
                validate(
                    requestReader.requestPart,
                    json,
                    parameter
                )
                Mono.empty()
            }
    }
}