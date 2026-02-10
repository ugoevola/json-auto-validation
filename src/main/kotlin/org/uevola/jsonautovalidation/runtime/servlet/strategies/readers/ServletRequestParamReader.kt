package org.uevola.jsonautovalidation.runtime.servlet.strategies.readers

import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import org.uevola.jsonautovalidation.common.enums.HttpRequestPartEnum
import org.uevola.jsonautovalidation.common.utils.JsonUtils.objectNodeFromRequestParams
import java.lang.reflect.Parameter

@Component
@ConditionalOnProperty(
    name = ["json-validation.web-stack"],
    havingValue = "servlet",
    matchIfMissing = true
)
internal class ServletRequestParamReader : ServletRequestReaderStrategy {

    override val requestPart = HttpRequestPartEnum.REQUEST_PARAMS
    override fun getOrdered() = Int.MAX_VALUE

    override fun resolve(
        parameter: Parameter,
        request: HttpServletRequest
    ) = true

    override fun read(
        request: HttpServletRequest
    ) = objectNodeFromRequestParams(request.parameterMap)
}