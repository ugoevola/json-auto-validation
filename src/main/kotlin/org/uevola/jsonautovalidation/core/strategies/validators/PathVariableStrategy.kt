package org.uevola.jsonautovalidation.core.strategies.validators

import com.fasterxml.jackson.core.JsonParseException
import jakarta.servlet.http.HttpServletRequest
import org.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PathVariable
import org.uevola.jsonautovalidation.utils.ExceptionUtil
import org.uevola.jsonautovalidation.utils.enums.HttpRequestPartEnum
import java.lang.reflect.Parameter

@Component
class PathVariableStrategy : ValidatorStrategy, AbstractValidatorStrategy() {
    override fun getOrdered() = 0

    override fun resolve(parameter: Parameter) =
        parameter.annotations.any { it is PathVariable }

    override fun validate(request: HttpServletRequest, parameter: Parameter) {
        try {
            val json = getPathVariables(request)
            validate(parameter, json, HttpRequestPartEnum.PATH_VARIABLES)
        } catch (e: JsonParseException) {
            throw ExceptionUtil.httpClientErrorException(
                "Error in ${HttpRequestPartEnum.PATH_VARIABLES}: ${e.message}",
                HttpStatus.BAD_REQUEST
            )
        }
    }

    private fun getPathVariables(request: HttpServletRequest): String {
        val json = JSONObject()

        val attributeNames = request.attributeNames
        while (attributeNames.hasMoreElements()) {
            val attributeName = attributeNames.nextElement()
            if (attributeName.startsWith("org.springframework.web.servlet.HandlerMapping.uriTemplateVariables")) {
                val pathVariableName = attributeName.substringAfterLast('.')
                val pathVariableValue = request.getAttribute(attributeName) as? String
                if (pathVariableValue != null) {
                    json.put(pathVariableName, pathVariableValue)
                }
            }
        }

        return json.toString()
    }
}