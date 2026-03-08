package org.uevola.jsonautovalidation.api.resolvers

import org.springframework.core.MethodParameter

interface JsonValidationAware {
    fun getRequestDtoType(): Class<*>
    fun supportsParameter(parameter: MethodParameter): Boolean
}