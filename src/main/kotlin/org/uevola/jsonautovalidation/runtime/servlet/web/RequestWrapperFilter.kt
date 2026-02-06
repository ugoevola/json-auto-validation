package org.uevola.jsonautovalidation.runtime.servlet.web

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component
import org.uevola.jsonautovalidation.runtime.common.config.JsonValidationProperties

@Component
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
            properties.maxJsonSize
        )
        filterChain.doFilter(wrappedRequest, servletResponse)
    }

}