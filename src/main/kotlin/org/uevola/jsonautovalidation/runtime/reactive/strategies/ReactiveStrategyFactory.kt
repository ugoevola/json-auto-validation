package org.uevola.jsonautovalidation.runtime.reactive.strategies

import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.lang.reflect.Parameter

interface ReactiveStrategyFactory {
    fun validate(exchange: ServerWebExchange, parameter: Parameter): Mono<Void>
}