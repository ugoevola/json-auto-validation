package org.uevola.jsonautovalidation.strategies.reader

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.RequestBody
import org.uevola.jsonautovalidation.common.enums.ContentTypeEnum
import org.uevola.jsonautovalidation.common.enums.HttpRequestPartEnum
import java.lang.reflect.Parameter

class JsonBodyReader: RequestReaderStrategy {

    override val requestPart = HttpRequestPartEnum.REQUEST_BODY

    override fun getOrdered() = 1

    override fun resolve(
        parameter: Parameter,
        request: HttpServletRequest
    ) = parameter.annotations.any { it is RequestBody } &&
            request.getHeader("Content-Type")  == ContentTypeEnum.APPLICATION_JSON.value

    override fun read(request: HttpServletRequest): JsonNode {
        val objectMapper = ObjectMapper()
        return try {
            objectMapper.readTree(request.reader.readText())
        } catch (e: Exception) {
            objectMapper.readTree("")
        }
    }
}