package org.uevola.jsonautovalidation.runtime.servlet.web

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import org.uevola.jsonautovalidation.runtime.common.config.JsonValidationProperties

@Component
@ConditionalOnProperty(
    name = ["json-validation.web-stack"],
    havingValue = "servlet",
    matchIfMissing = true
)
internal class RequestWrapperFilter(
    private val properties: JsonValidationProperties
) : Filter {
    override fun doFilter(
        servletRequest: ServletRequest,
        servletResponse: ServletResponse,
        filterChain: FilterChain,
    ) {
        val wrappedRequest = CustomHttpServletRequestWrapper(
            servletRequest as HttpServletRequest,
            properties.getMaxJsonSizeInBytes()
        )
        filterChain.doFilter(wrappedRequest, servletResponse)
    }

}