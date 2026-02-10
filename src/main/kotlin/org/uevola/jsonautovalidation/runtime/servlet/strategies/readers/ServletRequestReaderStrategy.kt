package org.uevola.jsonautovalidation.runtime.servlet.strategies.readers

import jakarta.servlet.http.HttpServletRequest
import org.uevola.jsonautovalidation.common.enums.HttpRequestPartEnum
import tools.jackson.databind.JsonNode
import java.lang.reflect.Parameter

internal interface ServletRequestReaderStrategy {

    val requestPart: HttpRequestPartEnum

    fun getOrdered(): Int

    fun resolve(parameter: Parameter, request: HttpServletRequest): Boolean

    fun read(request: HttpServletRequest): JsonNode

}