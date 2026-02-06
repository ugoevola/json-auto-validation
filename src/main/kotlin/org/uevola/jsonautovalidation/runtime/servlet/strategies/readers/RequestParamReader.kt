package org.uevola.jsonautovalidation.runtime.servlet.strategies.readers

import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component
import org.uevola.jsonautovalidation.common.enums.HttpRequestPartEnum
import tools.jackson.databind.JsonNode
import tools.jackson.databind.node.JsonNodeFactory
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
            .forEach { (name, values)  ->
                val value = if (values.size == 1) values[0]
                else {
                    val arrayNode = jsonObject.putArray(name)
                    values.forEach { arrayNode.add(it) }
                    return arrayNode
                }
                jsonObject.put(name, value)
            }
        return jsonObject
    }
}