package org.uevola.jsonautovalidation.common.utils

import tools.jackson.databind.JsonNode
import tools.jackson.databind.json.JsonMapper
import tools.jackson.databind.node.ObjectNode

internal object JsonUtils {

    private val jsonMapper = JsonMapper.builder().build()

    fun objectNodeFromString(str: String?): ObjectNode = jsonMapper.readTree(str) as ObjectNode

    fun jsonNodeFromString(str: String?): JsonNode = jsonMapper.readTree(str)

    fun newObjectNode(): ObjectNode = jsonMapper.createObjectNode()

    fun writeValueAsString(obj: Any): String = jsonMapper.writeValueAsString(obj)

    fun <T> readValue(content: String, valueType: Class<T>): T = jsonMapper.readValue(content, valueType)

    fun objectNodeFromRequestParams(params: Map<String, *>): ObjectNode {
        val jsonObject = newObjectNode()
        params
            .forEach { (name, values) ->
                val list = when (values) {
                    is Array<*> -> values.toList()
                    is List<*> -> values
                    else -> return@forEach
                }

                if (list.isEmpty()) return@forEach

                if (list.size == 1) {
                    jsonObject.put(name, list[0]?.toString())
                } else {
                    val arrayNode = jsonObject.putArray(name)
                    list.forEach { arrayNode.add(it?.toString()) }
                }
            }
        return jsonObject
    }

}