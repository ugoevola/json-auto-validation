package org.uevola.jsonautovalidation.runtime.strategies.readers

import jakarta.servlet.http.HttpServletRequest
import org.uevola.jsonautovalidation.common.enums.HttpRequestPartEnum
import tools.jackson.databind.JsonNode
import java.lang.reflect.Parameter

internal interface RequestReaderStrategy {

    val requestPart: HttpRequestPartEnum

    fun getOrdered(): Int

    fun resolve(parameter: Parameter, request: HttpServletRequest): Boolean

    fun read(request: HttpServletRequest): JsonNode

}