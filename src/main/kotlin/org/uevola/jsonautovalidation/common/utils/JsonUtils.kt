package org.uevola.jsonautovalidation.common.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode

internal object JsonUtils {

    private val objectMapper = ObjectMapper()

    fun objectNodeFromString(str: String?) = objectMapper.readTree(str) as ObjectNode
    fun jsonNodeFromString(str: String?): JsonNode = objectMapper.readTree(str)

    fun newObjectNode(): ObjectNode = objectMapper.createObjectNode()

    fun writeValueAsString(obj: Any): String = objectMapper.writeValueAsString(obj)
    fun <T> readValue(content: String, valueType: Class<T>): T = objectMapper.readValue(content, valueType)

}