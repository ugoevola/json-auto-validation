package org.uevola.jsonautovalidation.runtime.reactive.strategies.readers

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.server.ServerWebExchange
import org.uevola.jsonautovalidation.common.enums.ContentTypeEnum
import org.uevola.jsonautovalidation.common.enums.HttpRequestPartEnum
import org.uevola.jsonautovalidation.common.utils.JsonUtils.jsonNodeFromString
import org.uevola.jsonautovalidation.runtime.common.config.JsonAutoValidationProperties
import org.uevola.jsonautovalidation.runtime.common.utils.ExceptionUtils
import reactor.core.publisher.Mono
import tools.jackson.databind.JsonNode
import java.lang.reflect.Parameter

@Component
@ConditionalOnProperty(
    name = ["json-validation.web-stack"],
    havingValue = "webflux",
    matchIfMissing = false
)
internal class ReactiveJsonBodyReader(
    private val properties: JsonAutoValidationProperties
) : ReactiveRequestReaderStrategy {

    override val requestPart = HttpRequestPartEnum.REQUEST_BODY

    override fun getOrdered() = 1

    override fun resolve(
        parameter: Parameter,
        exchange: ServerWebExchange
    ) = parameter.annotations.any { it is RequestBody } &&
            exchange.request.headers.contentType?.toString()
                ?.startsWith(ContentTypeEnum.APPLICATION_JSON.value) ?: false

    override fun read(
        exchange: ServerWebExchange
    ): Mono<JsonNode> {
        val cachedBody = exchange.getAttribute<ByteArray>("CACHED_JSON_BODY")
        if (cachedBody != null) {
            return Mono.just(jsonNodeFromString(cachedBody.toString(Charsets.UTF_8)))
        }

        var accumulatedSize = 0L

        return exchange.request.body
            .map { dataBuffer ->
                val size = dataBuffer.readableByteCount()
                accumulatedSize += size
                if (accumulatedSize > properties.getMaxJsonSizeInBytes()) {
                    throw ExceptionUtils.httpClientErrorException(
                        "Payload Too Large: JSON body exceeds maximum size of ${properties.getMaxJsonSizeInBytes()} bytes",
                        HttpStatus.PAYLOAD_TOO_LARGE
                    )
                }
                val bytes = dataBuffer.asInputStream().readAllBytes()
                bytes
            }
            .collectList()
            .map { list -> list.reduce { acc, bytes -> acc + bytes } }
            .map { bytes ->
                exchange.attributes["CACHED_JSON_BODY"] = bytes
                jsonNodeFromString(bytes.toString(Charsets.UTF_8))
            }
    }
}
