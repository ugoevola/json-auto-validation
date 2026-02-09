package org.uevola.jsonautovalidation.runtime.reactive.strategies.readers

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.server.ServerWebExchange
import org.uevola.jsonautovalidation.common.enums.HttpRequestPartEnum
import org.uevola.jsonautovalidation.common.utils.JsonUtils.objectNodeFromRequestParams
import reactor.core.publisher.Mono
import tools.jackson.databind.JsonNode
import java.lang.reflect.Parameter

@Component
@ConditionalOnProperty(
    name = ["json-validation.web-stack"],
    havingValue = "webflux",
    matchIfMissing = false
)
internal class ReactiveRequestParamReader : ReactiveRequestReaderStrategy {

    override val requestPart = HttpRequestPartEnum.REQUEST_PARAMS

    override fun getOrdered() = Int.MAX_VALUE

    override fun resolve(
        parameter: Parameter,
        exchange: ServerWebExchange
    ) = parameter.annotations.any { it is RequestParam } || parameter.annotations.isEmpty()

    override fun read(
        exchange: ServerWebExchange
    ): Mono<JsonNode> = Mono.just(objectNodeFromRequestParams(exchange.request.queryParams))
}
