package org.uevola.jsonautovalidation.strategies.schemaGenerators

import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.stereotype.Component
import org.uevola.jsonautovalidation.common.annotations.jsonValidationAnnotation.IsEnum
import org.uevola.jsonautovalidation.common.extensions.resolveTemplate
import org.uevola.jsonautovalidation.common.utils.JsonUtil
import org.uevola.jsonautovalidation.common.utils.JsonUtil.readValue
import org.uevola.jsonautovalidation.common.utils.JsonUtil.writeValueAsString
import org.uevola.jsonautovalidation.common.utils.ResourcesUtil
import java.lang.reflect.Parameter
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.javaType

@Component
class IsEnumJsonGenerator : JsonSchemaGeneratorStrategy {

    override fun getOrdered() = 0

    override fun resolve(annotation: Annotation) = annotation is IsEnum

    override fun generate(
        annotation: Annotation,
        property: KProperty1<out Any, *>,
        generateSchema: (clazz: KClass<*>) -> ObjectNode?
    ): ObjectNode? {
        val enumClass = getEnumClass(property) ?: return null
        return generate(enumClass)
    }

    override fun generate(annotation: Annotation, parameter: Parameter): ObjectNode? {
        val enumClass = getEnumClass(parameter) ?: return null
        return generate(enumClass)
    }

    private fun generate(enumClass: KClass<out Any>): ObjectNode? {
        var enumValues = getEnumValues(enumClass)
        enumValues = enumValues + ""
        val values = mapOf("enum" to enumValues)
        val schemaName = IsEnum::class.simpleName ?: return null
        val jsonString = ResourcesUtil
            .getResourceSchema(schemaName)?.inputStream
            ?.bufferedReader().use { it?.readText() }
        val objectNode = JsonUtil.objectNodeFromString(jsonString)
        return objectNode.resolveTemplate(values)
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