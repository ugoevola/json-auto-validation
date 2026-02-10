package org.uevola.jsonautovalidation.runtime.reactive.strategies.readers

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.uevola.jsonautovalidation.common.enums.ContentTypeEnum
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
internal class ReactiveFormDataReader : ReactiveRequestReaderStrategy {

    override val requestPart = HttpRequestPartEnum.FORM_DATA

    override fun getOrdered() = 1

    override fun resolve(
        parameter: Parameter,
        exchange: ServerWebExchange
    ): Boolean {
        val contentType = exchange.request.headers.contentType?.toString() ?: ""
        return contentType.startsWith(ContentTypeEnum.APPLICATION_FORM_URLENCODED.value) ||
                contentType.startsWith(ContentTypeEnum.MULTIPART_FORM_DATA.value)
    }

    override fun read(
        exchange: ServerWebExchange
    ): Mono<JsonNode> = Mono.just(objectNodeFromRequestParams(exchange.request.queryParams))
}
