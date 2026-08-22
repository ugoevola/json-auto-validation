package org.uevola.jsonautovalidation.common.strategies.schemas

import org.uevola.jsonautovalidation.api.enums.JsonTypeEnum
import tools.jackson.databind.node.ObjectNode
import java.lang.reflect.Parameter
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties

internal interface JsonSchemaGeneratorStrategy {

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

    fun hasGlobalErrorMessage(annotation: Annotation) =
        annotation.annotationClass.declaredMemberProperties
            .find { it.name == "errorMessage" }
            ?.call(annotation)
            .toString()
            .isNotEmpty()

    fun annotationEntries(annotation: Annotation) =
        annotation.annotationClass.declaredMemberProperties.associate { property ->
            property.name to jsonSchemaValue(property.call(annotation))
        }

    /**
     * Names of the annotation attributes left to their default value.
     *
     * A constraint that was not declared has no reason to appear in the schema:
     * writing the default bounds would forbid, for instance, any decimal value
     * (multipleOf) or any negative value (exclusiveMinimum).
     */
    fun defaultValuedAttributes(annotation: Annotation): Set<String> =
        annotation.annotationClass.java.declaredMethods
            .filter { method -> method.defaultValue != null }
            .filter { method -> areEqual(method.invoke(annotation), method.defaultValue) }
            .map { method -> method.name }
            .toSet()
}

/**
 * A JsonTypeEnum carries the JSON Schema type in its value: the name of the
 * constant is not a valid type. A single expected type is written as a plain
 * string, several ones as an array of strings.
 */
private fun jsonSchemaValue(value: Any?): Any? = when {
    value is JsonTypeEnum -> value.value
    value is Array<*> && value.isNotEmpty() && value.all { it is JsonTypeEnum } ->
        if (value.size == 1) (value[0] as JsonTypeEnum).value
        else value.map { (it as JsonTypeEnum).value }.toTypedArray()

    else -> value
}

private fun areEqual(value: Any?, other: Any?): Boolean =
    if (value is Array<*> && other is Array<*>) value.contentDeepEquals(other)
    else value == other