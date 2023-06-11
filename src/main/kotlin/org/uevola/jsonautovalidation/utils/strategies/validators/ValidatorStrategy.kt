package org.uevola.jsonautovalidation.utils.strategies.validators

import jakarta.servlet.http.HttpServletRequest
import java.lang.reflect.Parameter

interface ValidatorStrategy {
    fun getOrdered(): Int

    fun resolve(parameter: Parameter): Boolean

    fun validate(request: HttpServletRequest, parameter: Parameter)
}