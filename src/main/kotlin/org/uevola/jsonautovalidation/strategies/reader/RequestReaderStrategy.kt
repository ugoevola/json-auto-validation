package org.uevola.jsonautovalidation.strategies.reader

import com.fasterxml.jackson.databind.JsonNode
import jakarta.servlet.http.HttpServletRequest
import org.uevola.jsonautovalidation.common.enums.HttpRequestPartEnum
import java.lang.reflect.Parameter

interface RequestReaderStrategy {

    val requestPart: HttpRequestPartEnum

    fun getOrdered(): Int

    fun resolve(parameter: Parameter, request: HttpServletRequest): Boolean

    fun read(request: HttpServletRequest): JsonNode

}