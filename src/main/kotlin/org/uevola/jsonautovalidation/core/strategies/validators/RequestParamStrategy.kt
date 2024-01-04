package org.uevola.jsonautovalidation.core.strategies.validators

import com.fasterxml.jackson.core.JsonParseException
import jakarta.servlet.http.HttpServletRequest
import org.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.uevola.jsonautovalidation.common.utils.ClassesUtil
import org.uevola.jsonautovalidation.common.utils.ExceptionUtil
import org.uevola.jsonautovalidation.common.enums.HttpRequestPartEnum
import org.uevola.jsonautovalidation.common.utils.isJavaOrKotlin
import java.lang.reflect.Parameter

@Component
class RequestParamStrategy : ValidatorStrategy, AbstractValidatorStrategy() {
    override fun getOrdered() = Int.MAX_VALUE

    override fun resolve(parameter: Parameter) = true

    override fun validate(request: HttpServletRequest, parameter: Parameter) {
        try {
            val json = extractJsonRequestParamsFromRequest(request.parameterMap).toString()
            if (parameter.type.isJavaOrKotlin()) {
                validate(parameter, json, HttpRequestPartEnum.REQUEST_PARAMS)
            } else {
                validate(parameter.type, json, HttpRequestPartEnum.REQUEST_PARAMS, getCustomMessage(parameter))
            }
        } catch (e: JsonParseException) {
            throw ExceptionUtil.httpClientErrorException(
                "Error in ${HttpRequestPartEnum.REQUEST_PARAMS}: ${e.message}",
                HttpStatus.BAD_REQUEST
            )
        }
    }

    private fun extractJsonRequestParamsFromRequest(parameterMap: Map<String, Array<String>>): JSONObject {
        val jsonObject = JSONObject()
        parameterMap.forEach { (key, value) ->
            if (value.isNotEmpty()) {
                val finalValue = if(value.size > 1) value else value[0]
                jsonObject.put(key, finalValue)
            }
        }
        return jsonObject
    }

}