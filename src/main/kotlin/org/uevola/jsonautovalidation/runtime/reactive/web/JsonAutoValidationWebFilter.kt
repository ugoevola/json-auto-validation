package org.uevola.jsonautovalidation.runtime.reactive.web

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.MethodParameter
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import org.uevola.jsonautovalidation.api.resolvers.JsonValidationAware
import org.uevola.jsonautovalidation.common.extensions.getParamsToValidate
import org.uevola.jsonautovalidation.runtime.reactive.strategies.ReactiveStrategyFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.lang.reflect.Method
import java.lang.reflect.Parameter

@Component
@ConditionalOnProperty(
    name = ["json-validation.web-stack"],
    havingValue = "webflux",
    matchIfMissing = false
)
class JsonAutoValidationWebFilter(
    private val reactiveJsonValidationExecutor: ReactiveStrategyFactory,
    private val jsonValidationAwares: List<JsonValidationAware>
) : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val handler = exchange.getAttribute<Any>(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE)

        if (handler !is HandlerMethod) {
            return chain.filter(exchange)
        }

        val parameters = handler.method.getParamsToValidate(handler.beanType)

        return Flux.fromIterable(parameters)
            .flatMap { parameter ->
                val effectiveClass = resolveEffectiveClass(handler.method, parameter)
                reactiveJsonValidationExecutor.validate(exchange, parameter, effectiveClass)
            }
            .then(Mono.defer { decorateAndFilter(exchange, chain) })
    }

    private fun decorateAndFilter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val cachedBody = exchange.getAttribute<ByteArray>("CACHED_JSON_BODY")
            ?: return chain.filter(exchange)

        val mutatedExchange = exchange.mutate()
            .request(createCachedBodyDecorator(exchange, cachedBody))
            .build()

        return chain.filter(mutatedExchange)
    }

    private fun createCachedBodyDecorator(
        exchange: ServerWebExchange,
        cachedBody: ByteArray
    ): ServerHttpRequestDecorator {
        return object : ServerHttpRequestDecorator(exchange.request) {
            override fun getBody(): Flux<DataBuffer> {
                val buffer = exchange.response.bufferFactory().wrap(cachedBody)
                return Flux.just(buffer)
            }
        }
    }

    private fun resolveEffectiveClass(method: Method, parameter: Parameter): Class<*>? {
        val index = method.parameters.indexOf(parameter)
        val methodParameter = MethodParameter(method, index)
        return jsonValidationAwares
            .find { it.supportsParameter(methodParameter) }?.getRequestDtoType()
    }
}