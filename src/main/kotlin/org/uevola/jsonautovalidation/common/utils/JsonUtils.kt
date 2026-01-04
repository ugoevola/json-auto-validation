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

}