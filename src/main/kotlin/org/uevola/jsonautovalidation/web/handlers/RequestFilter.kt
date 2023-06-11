package org.uevola.jsonautovalidation.web.handlers

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component
import org.uevola.jsonautovalidation.utils.wrappers.CustomHttpServletRequestWrapper

@Component
class RequestFilter : Filter {
    override fun doFilter(
        servletRequest: ServletRequest,
        servletResponse: ServletResponse,
        filterChain: FilterChain,
    ) {
        val wrappedRequest = CustomHttpServletRequestWrapper(servletRequest as HttpServletRequest)
        filterChain.doFilter(wrappedRequest, servletResponse)
    }

}