package org.uevola.jsonautovalidation.runtime.servlet.strategies.readers

import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestBody
import org.uevola.jsonautovalidation.common.enums.ContentTypeEnum
import org.uevola.jsonautovalidation.common.enums.HttpRequestPartEnum
import org.uevola.jsonautovalidation.common.utils.JsonUtils.jsonNodeFromString
import java.lang.reflect.Parameter

@Component
internal class JsonBodyReader: RequestReaderStrategy {

    override val requestPart = HttpRequestPartEnum.REQUEST_BODY

    override fun getOrdered() = 1

    override fun resolve(
        parameter: Parameter,
        request: HttpServletRequest
    ) = parameter.annotations.any { it is RequestBody } &&
            request.getHeader("Content-Type")  == ContentTypeEnum.APPLICATION_JSON.value

    override fun read(
        request: HttpServletRequest
    ) = jsonNodeFromString(request.reader.readText())
}