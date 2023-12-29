package org.uevola.jsonautovalidation.core.strategies.validators

import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestBody
import org.uevola.jsonautovalidation.utils.enums.HttpRequestPartEnum
import java.lang.reflect.Parameter

@Component
class RequestBodyStrategy : ValidatorStrategy, AbstractValidatorStrategy() {
    override fun getOrdered() = 0

    override fun resolve(parameter: Parameter) =
        parameter.annotations.any { it is RequestBody }

    override fun validate(request: HttpServletRequest, parameter: Parameter) {
        val json = extractJsonBodyFromRequest(request)
        validate(parameter.type, json, HttpRequestPartEnum.REQUEST_BODY, getCustomMessage(parameter))
    }

    private fun extractJsonBodyFromRequest(request: HttpServletRequest) = try {
        request.reader.readText()
    } catch (e: Exception) {
        ""
    }
}