package org.uevola.jsonautovalidation.aot

import com.fasterxml.jackson.databind.node.ObjectNode
import org.uevola.jsonautovalidation.common.strategies.schemas.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

internal object StrategyFactory {

    private val schemaGenerators: Set<JsonSchemaGeneratorStrategy> = setOf(
        DefaultJsonGenerator,
        IsEnumJsonGenerator,
        IsJsonSchemaJsonGenerator,
        IsNestedJsonGenerator
    )

    fun generateSchemaFor(
        annotation: Annotation,
        property: KProperty1<out Any, *>,
        getJsonSchema: (clazz: KClass<*>) -> ObjectNode?
    ): ObjectNode? {
        return schemaGenerators
            .sortedBy { it.getOrdered() }
            .find { it.resolve(annotation) }
            ?.generate(annotation, property, getJsonSchema)
    }

}