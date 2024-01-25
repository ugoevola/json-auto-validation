package org.uevola.jsonautovalidation.strategies.reader

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import jakarta.servlet.http.HttpServletRequest
import org.uevola.jsonautovalidation.common.enums.HttpRequestPartEnum
import org.uevola.jsonautovalidation.common.utils.JsonUtil.writeValueAsString
import java.lang.reflect.Parameter

class RequestParamReader: RequestReaderStrategy {

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