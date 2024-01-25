package org.uevola.jsonautovalidation.strategies.schemaGenerators

import com.fasterxml.jackson.databind.node.ObjectNode
import java.lang.reflect.Parameter
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

interface JsonSchemaGeneratorStrategy {

    fun getOrdered(): Int

    fun resolve(annotation: Annotation): Boolean

    fun generate(
        annotation: Annotation,
        property: KProperty1<out Any, *>,
        generateSchema: (clazz: KClass<*>) -> ObjectNode?,
    ): ObjectNode?

    fun generate(
        annotation: Annotation,
        parameter: Parameter
    ): ObjectNode?
}