package org.uevola.jsonautovalidation.runtime.servlet.strategies

import jakarta.servlet.http.HttpServletRequest
import java.lang.reflect.Parameter

interface ServletStrategyFactory {
    fun validate(request: HttpServletRequest, parameter: Parameter)
}