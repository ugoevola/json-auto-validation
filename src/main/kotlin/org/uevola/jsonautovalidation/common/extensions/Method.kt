package org.uevola.jsonautovalidation.common.extensions

import org.uevola.jsonautovalidation.common.annotations.Validate
import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.lang.reflect.ParameterizedType

fun Method.getParamsToValidate(
    controller: Class<*>,
): List<Parameter> =
    if (controller.annotations.any { it is Validate } || this.annotations.any { it is Validate })
        this.parameters
            .toList()
            .filter { parameter -> parameter.parameterizedType !is ParameterizedType }
    else
        this.parameters
            .filter { parameter ->
                parameter.annotations.any { it is Validate } && parameter.parameterizedType !is ParameterizedType
            }