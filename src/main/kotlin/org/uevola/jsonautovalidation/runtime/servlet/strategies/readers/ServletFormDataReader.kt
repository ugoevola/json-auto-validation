package org.uevola.jsonautovalidation.runtime.servlet.strategies.readers

import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import org.uevola.jsonautovalidation.common.enums.ContentTypeEnum
import org.uevola.jsonautovalidation.common.enums.HttpRequestPartEnum
import org.uevola.jsonautovalidation.common.utils.JsonUtils.objectNodeFromRequestParams
import java.lang.reflect.Parameter

@Component
@ConditionalOnProperty(
    name = ["json-validation.web-stack"],
    havingValue = "servlet",
    matchIfMissing = true
)
internal class ServletFormDataReader : ServletRequestReaderStrategy {

    override val requestPart = HttpRequestPartEnum.FORM_DATA

    override fun getOrdered() = 1

    override fun resolve(
        parameter: Parameter,
        request: HttpServletRequest
    ): Boolean {
        val contentType = request.contentType ?: ""
        return contentType.startsWith(ContentTypeEnum.APPLICATION_FORM_URLENCODED.value) ||
                contentType.startsWith(ContentTypeEnum.MULTIPART_FORM_DATA.value)
    }

    override fun read(
        request: HttpServletRequest
    ) = objectNodeFromRequestParams(request.parameterMap)
}
