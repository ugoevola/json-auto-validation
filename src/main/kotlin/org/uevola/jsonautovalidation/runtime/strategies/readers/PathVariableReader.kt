package org.uevola.jsonautovalidation.runtime.strategies.readers

import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PathVariable
import org.uevola.jsonautovalidation.common.enums.HttpRequestPartEnum
import tools.jackson.databind.JsonNode
import tools.jackson.databind.node.JsonNodeFactory
import java.lang.reflect.Parameter

@Component
internal class PathVariableReader: RequestReaderStrategy {

    override val requestPart = HttpRequestPartEnum.PATH_VARIABLES
    override fun getOrdered() = 0

    override fun resolve(
        parameter: Parameter,
        request: HttpServletRequest
    ) = parameter.annotations.any { it is PathVariable }

    override fun read(request: HttpServletRequest): JsonNode {
        val jsonObject = JsonNodeFactory.instance.objectNode()
        for (attributeName in request.attributeNames) {
            if (attributeName.startsWith("org.springframework.web.servlet.HandlerMapping.uriTemplateVariables")) {
                val pathVariableName = attributeName.substringAfterLast('.')
                val pathVariableValue = request.getAttribute(attributeName) as? String
                if (pathVariableValue != null) {
                    jsonObject.put(pathVariableName, pathVariableValue)
                }
            }
        }
        return jsonObject
    }
}