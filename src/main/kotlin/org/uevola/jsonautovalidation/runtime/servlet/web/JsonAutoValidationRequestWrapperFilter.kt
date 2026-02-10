package org.uevola.jsonautovalidation.runtime.servlet.web

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import org.uevola.jsonautovalidation.common.enums.ContentTypeEnum
import org.uevola.jsonautovalidation.runtime.common.config.JsonAutoValidationProperties

@Component
@ConditionalOnProperty(
    name = ["json-validation.web-stack"],
    havingValue = "servlet",
    matchIfMissing = true
)
internal class JsonAutoValidationRequestWrapperFilter(
    private val properties: JsonAutoValidationProperties
) : Filter {
    override fun doFilter(
        servletRequest: ServletRequest,
        servletResponse: ServletResponse,
        filterChain: FilterChain,
    ) {
        val httpServletRequest = servletRequest as HttpServletRequest
        val contentType = httpServletRequest.contentType

        if (contentType != null && contentType.startsWith(ContentTypeEnum.APPLICATION_JSON.value)) {
            val wrappedRequest = JsonAutoValidationRequestWrapper(
                httpServletRequest,
                properties.getMaxJsonSizeInBytes()
            )
            filterChain.doFilter(wrappedRequest, servletResponse)
        } else {
            filterChain.doFilter(servletRequest, servletResponse)
        }
    }

}