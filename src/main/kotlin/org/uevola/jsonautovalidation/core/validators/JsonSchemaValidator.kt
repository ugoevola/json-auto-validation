package org.uevola.jsonautovalidation.core.validators

import org.uevola.jsonautovalidation.common.utils.ResourcesUtil
import java.lang.reflect.ParameterizedType

abstract class JsonSchemaValidator<T> : AbstractValidator(), IJsonSchemaValidator<T> {
    private val jsonSchemaName: String

    init {
        val superClass = javaClass.genericSuperclass
        if (superClass !is ParameterizedType)
            throw IllegalArgumentException("No type argument found")
        val typeArguments = superClass.actualTypeArguments
        if (typeArguments.isEmpty())
            throw IllegalArgumentException("No type argument found")
        val typeArgument = typeArguments[0]
        if (typeArgument !is Class<*>)
            throw IllegalArgumentException("Invalid type argument")
        jsonSchemaName = typeArgument.simpleName
    }

    override fun validate(json: String, customMessages: Map<String, String>): Unit? {
        val content = ResourcesUtil.getResourceSchemaAsString(jsonSchemaName)
        return if (content != null) validate(json, customMessages, content) else null
    }

}