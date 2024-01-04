package org.uevola.jsonautovalidation.common.utils

import org.json.JSONObject

object JsonUtil {

    /**
     * used to resolve json-auto-validation template
     * each property value that need to be replaced follows that template : {{property_name}}
     * and its value is linked to it's name in the values attribute map
     *
     * @param template the template that contains 0 or more properties to be replaced
     * @param values the map that bind each property name to its value
     */
    fun resolveTemplate(template: JSONObject?, values: Map<String, Any?>): JSONObject {
        val result = JSONObject(template.toString())
        result.keys().forEach { key ->
            val value = result.get(key)
            if (value is JSONObject) result.put(key, resolveTemplate(value, values))
            if (value !is String) return@forEach
            val regex = Regex("^@\\{(.+)}$")
            val match = regex.find(value) ?: return@forEach
            val capturedValue = match.groupValues[1]
            result.put(key, values[capturedValue])
        }
        return result
    }

    fun mergeJSONObject(json1: JSONObject, json2: JSONObject?): JSONObject {
        if (json2 == null) return json1
        JSONObject.getNames(json2)?.forEach { json1.put(it, json2.get(it)) }
        return json1
    }
}