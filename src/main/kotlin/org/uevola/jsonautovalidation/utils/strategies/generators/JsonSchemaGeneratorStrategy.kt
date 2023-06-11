package org.uevola.jsonautovalidation.utils.strategies.generators

import org.json.JSONObject
import java.lang.reflect.Parameter
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

interface JsonSchemaGeneratorStrategy {

    fun getOrdered(): Int

    fun resolve(annotation: Annotation): Boolean

    fun generate(
        annotation: Annotation,
        property: KProperty1<out Any, *>,
        generateSchema: (clazz: KClass<*>) -> JSONObject?,
    ): JSONObject?

    fun generate(
        annotation: Annotation,
        parameter: Parameter
    ): JSONObject?
}