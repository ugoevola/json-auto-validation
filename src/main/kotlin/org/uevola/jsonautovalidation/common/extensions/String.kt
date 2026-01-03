package org.uevola.jsonautovalidation.common.extensions

internal fun String.populate(values: Map<String, Any?>): String {
    val regex = Regex("@\\{(\\w+)}")
    return regex.replace(this) { matchResult ->
        val key = matchResult.groupValues[1]
        val value = values[key] ?: return@replace matchResult.value

        val stringValue = when (value) {
            is Array<*> -> value.contentToString()
            is IntArray -> value.contentToString()
            is LongArray -> value.contentToString()
            is DoubleArray -> value.contentToString()
            is FloatArray -> value.contentToString()
            is BooleanArray -> value.contentToString()
            is CharArray -> value.contentToString()
            is ShortArray -> value.contentToString()
            is ByteArray -> value.contentToString()
            else -> value.toString()
        }

        stringValue
    }
}