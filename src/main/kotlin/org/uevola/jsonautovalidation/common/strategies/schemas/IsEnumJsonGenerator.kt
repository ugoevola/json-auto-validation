package org.uevola.jsonautovalidation.common.strategies.schemas

import org.uevola.jsonautovalidation.annotations.jsonValidationAnnotation.IsEnum
import org.uevola.jsonautovalidation.aot.schemas.jsonSchemas
import org.uevola.jsonautovalidation.common.extensions.resolveTemplate
import org.uevola.jsonautovalidation.common.utils.JsonUtils
import org.uevola.jsonautovalidation.common.utils.JsonUtils.readValue
import org.uevola.jsonautovalidation.common.utils.JsonUtils.writeValueAsString
import tools.jackson.databind.node.ObjectNode
import java.lang.reflect.Parameter
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.javaType

internal object IsEnumJsonGenerator : JsonSchemaGeneratorStrategy {

    override fun getOrdered() = 0

    override fun resolve(annotation: Annotation) = annotation is IsEnum

    override fun generate(
        annotation: Annotation,
        property: KProperty1<out Any, *>,
        generateSchema: (clazz: KClass<*>) -> ObjectNode?
    ): ObjectNode? {
        val enumClass = getEnumClass(property) ?: return null
        return generate(annotation, enumClass, property.name)
    }

    override fun generate(annotation: Annotation, parameter: Parameter): ObjectNode? {
        val enumClass = getEnumClass(parameter) ?: return null
        return generate(annotation, enumClass, parameter.name)
    }

    private fun generate(
        annotation: Annotation,
        enumClass: KClass<out Any>,
        fieldName: String
    ): ObjectNode {
        var enumValues = getEnumValues(enumClass)
        enumValues = enumValues + ""
        val values = mapOf("enum" to enumValues) + annotationEntries(annotation)
        val jsonString = jsonSchemas[IsEnum::class]
        val objectNode = JsonUtils.objectNodeFromString(jsonString)
        return objectNode.resolveTemplate(values, fieldName, hasGlobalErrorMessage(annotation))
    }

    private fun getEnumClass(property: KProperty1<out Any, *>): KClass<out Any>? {
        val returnType = property.returnType

        if (returnType.classifier is KClass<*>) {
            val kClass = returnType.classifier as KClass<*>

            if (kClass.isSubclassOf(Enum::class)) {
                return kClass
            }
        } else if (returnType.javaType is Class<*>) {
            val javaClass = returnType.javaType as Class<*>

            if (javaClass.isEnum) {
                return javaClass.enumConstants[0].javaClass.kotlin
            }
        }

        return null
    }

    private fun getEnumClass(parameter: Parameter) =
        if (parameter.javaClass.isEnum) parameter.javaClass.kotlin else null

    private fun getEnumValues(enumClass: KClass<out Any>): List<*> {
        val json = writeValueAsString(enumClass.java.enumConstants)
        return readValue(json, List::class.java)
    }
}