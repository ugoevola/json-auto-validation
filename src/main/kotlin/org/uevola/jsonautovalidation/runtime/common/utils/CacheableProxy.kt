package org.uevola.jsonautovalidation.runtime.common.utils

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import org.uevola.jsonautovalidation.api.annotations.rules.IsJsonValidation
import org.uevola.jsonautovalidation.common.extensions.merge
import org.uevola.jsonautovalidation.common.strategies.schemas.*
import org.uevola.jsonautovalidation.common.utils.JsonUtils
import tools.jackson.databind.node.ObjectNode
import java.lang.reflect.Parameter

@Component
class CacheableProxy {

    private val schemaGenerators: Set<JsonSchemaGeneratorStrategy> = setOf(
        DefaultJsonGenerator,
        IsEnumJsonGenerator,
        IsJsonSchemaJsonGenerator,
        IsNestedJsonGenerator
    )

    @Cacheable(
        cacheNames = ["parameterSchema"],
        key = "#parameter.name + '::' + #parameter.type.name"
    )
    fun generateSchemaForParameter(
        parameter: Parameter
    ): ObjectNode? {
        val value = parameter.annotations
            .filter { annotation -> annotation.annotationClass.annotations.any { it is IsJsonValidation } }
            .map { annotation ->
                schemaGenerators
                    .sortedBy { it.getOrdered() }
                    .find { it.resolve(annotation) }!!
                    .generate(annotation, parameter)
            }
            .merge()
        if (value.isEmpty) return null
        val json = JsonUtils.newObjectNode()
        json.set(parameter.name, value)
        return json
    }

}