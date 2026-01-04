package org.uevola.jsonautovalidation.runtime.strategies

import jakarta.servlet.http.HttpServletRequest
import java.lang.reflect.Parameter

interface JsonValidationExecutor {
    fun validate(request: HttpServletRequest, parameter: Parameter)
}