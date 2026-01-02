package org.uevola.jsonautovalidation.runtime.strategies.readers

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component
import org.uevola.jsonautovalidation.common.enums.HttpRequestPartEnum
import org.uevola.jsonautovalidation.common.utils.JsonUtils.writeValueAsString
import java.lang.reflect.Parameter

@Component
internal class RequestParamReader: RequestReaderStrategy {

    override val requestPart = HttpRequestPartEnum.REQUEST_PARAMS
    override fun getOrdered() = Int.MAX_VALUE

    override fun resolve(
        parameter: Parameter,
        request: HttpServletRequest
    ) = true

    override fun read(request: HttpServletRequest): JsonNode {
        val jsonObject = JsonNodeFactory.instance.objectNode()
        request.parameterMap
            .filter { it.value.isNotEmpty() }
            .forEach { jsonObject.put(it.key, writeValueAsString(it.value)) }
        return jsonObject
    }
}