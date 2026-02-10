package org.uevola.jsonautovalidation.runtime.reactive.strategies.readers

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.server.ServerWebExchange
import org.uevola.jsonautovalidation.common.enums.HttpRequestPartEnum
import reactor.core.publisher.Mono
import tools.jackson.databind.JsonNode
import tools.jackson.databind.node.JsonNodeFactory
import java.lang.reflect.Parameter

@Component
@ConditionalOnProperty(
    name = ["json-validation.web-stack"],
    havingValue = "webflux",
    matchIfMissing = false
)
internal class ReactivePathVariableReader : ReactiveRequestReaderStrategy {

    override val requestPart = HttpRequestPartEnum.PATH_VARIABLES

    override fun getOrdered() = 0

    override fun resolve(
        parameter: Parameter,
        exchange: ServerWebExchange
    ) = parameter.annotations.any { it is PathVariable }

    override fun read(exchange: ServerWebExchange): Mono<JsonNode> {
        val jsonObject = JsonNodeFactory.instance.objectNode()
        val pathVariables = exchange.getAttribute<Map<String, String>>(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE)
        pathVariables?.forEach { (name, value) ->
            jsonObject.put(name, value)
        }
        return Mono.just(jsonObject)
    }
}
