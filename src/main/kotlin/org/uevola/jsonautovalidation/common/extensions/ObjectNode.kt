package org.uevola.jsonautovalidation.common.extensions

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.uevola.jsonautovalidation.common.utils.JsonUtil



/**
 * used to resolve json-auto-validation template
 * each property value that need to be replaced follows that template : {{property_name}}
 * and its value is linked to it's name in the values attribute map
 *
 * @param values the map that bind each property name to its value
 */
fun ObjectNode.resolveTemplate(
    values: Map<String, Any?>
): ObjectNode {
    val result = this.deepCopy()
    this.fields().forEach { (key, value) ->
        when {
            value.isObject -> result.set<JsonNode>(key, (value as ObjectNode).resolveTemplate(values))
            value.isTextual -> {
                val regex = Regex("^@\\{(.+)}$")
                val match = regex.find(value.textValue())
                if (match != null) {
                    val capturedValue = match.groupValues[1]
                    result.put(key, values[capturedValue]?.toString())
                }
            }
        }
    }
    return result
}

fun ObjectNode.mergeWith(
    other: ObjectNode?
): ObjectNode {
    if (other == null) return this
    other.fields().forEach { (key, value) -> this.set<JsonNode>(key, value) }
    return this
}

fun List<ObjectNode?>.merge(): ObjectNode {
    return this.fold(JsonUtil.newObjectNode()) { acc, jsonObject ->
        acc.mergeWith(jsonObject)
        acc
    }
}