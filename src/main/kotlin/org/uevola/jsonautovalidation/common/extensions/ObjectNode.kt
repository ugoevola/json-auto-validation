package org.uevola.jsonautovalidation.common.extensions

import org.uevola.jsonautovalidation.common.Constants.ERROR_MESSAGE_KEYWORD
import org.uevola.jsonautovalidation.common.utils.JsonUtils
import tools.jackson.databind.node.ObjectNode
import java.math.BigDecimal
import java.math.BigInteger

/**
 * used to resolve json-auto-validation template
 * each property value that needs to be replaced follows that template: @{property_name},
 * and its value is linked to it's name in the values attribute map
 *
 * @param values the map that binds each property name to its value
 */
internal fun <T> ObjectNode.resolveTemplate(
    values: Map<String, T>,
    fieldName: T,
): ObjectNode {
    val result = this.deepCopy()
    this.properties().forEach { (key, value) ->
        when {
            value.isObject -> result.set(key, (value as ObjectNode).resolveTemplate(values, fieldName))
            value.isString -> {
                val regex = Regex("^@\\{(.+)}$")
                val match = regex.find(value.asString())
                if (match != null) {
                    val capturedValue = match.groupValues[1]
                    if (capturedValue == ERROR_MESSAGE_KEYWORD) {
                        val newValues = values.toMutableMap()
                        newValues["fieldName"] = fieldName
                        val newValue = values[capturedValue].toString().populate(newValues)
                        result.put(key, newValue)
                    } else {
                        result.putAny(key, values[capturedValue])
                    }
                }
            }
        }
    }
    return result
}

private fun ObjectNode.putAny(key: String, value: Any?) {
    when (value) {
        is String -> this.put(key, value)
        is Int -> this.put(key, value)
        is Boolean -> this.put(key, value)
        is Long -> this.put(key, value)
        is Double -> this.put(key, value)
        is Float -> this.put(key, value)
        is BigDecimal -> this.put(key, value)
        is BigInteger -> this.put(key, value)
        is Short -> this.put(key, value)
        is ByteArray -> this.put(key, value)
        else -> this.putPOJO(key, value)
    }
}

internal fun ObjectNode.mergeWith(
    other: ObjectNode?
): ObjectNode {
    if (other == null) return this
    other.properties().forEach { (key, value) -> this.set(key, value) }
    return this
}

internal fun List<ObjectNode?>.merge(): ObjectNode {
    return this.fold(JsonUtils.newObjectNode()) { acc, jsonObject ->
        acc.mergeWith(jsonObject)
        acc
    }
}