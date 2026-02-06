package org.uevola.jsonautovalidation.runtime.reactive.strategies.readers

import org.springframework.web.server.ServerWebExchange
import org.uevola.jsonautovalidation.common.enums.HttpRequestPartEnum
import reactor.core.publisher.Mono
import tools.jackson.databind.JsonNode
import java.lang.reflect.Parameter

internal interface ReactiveRequestReaderStrategy {

    val requestPart: HttpRequestPartEnum

    fun getOrdered(): Int

    fun resolve(parameter: Parameter, exchange: ServerWebExchange): Boolean

    fun read(exchange: ServerWebExchange): Mono<JsonNode>

}
