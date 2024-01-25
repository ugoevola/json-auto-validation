package org.uevola.jsonautovalidation.web

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component

@Component
class RequestWrapperFilter : Filter {
    override fun doFilter(
        servletRequest: ServletRequest,
        servletResponse: ServletResponse,
        filterChain: FilterChain,
    ) {
        val wrappedRequest = CustomHttpServletRequestWrapper(servletRequest as HttpServletRequest)
        filterChain.doFilter(wrappedRequest, servletResponse)
    }

}