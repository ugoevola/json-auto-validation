package org.uevola.jsonautovalidation.core.strategies.schemaGenerators

import com.fasterxml.jackson.databind.ObjectMapper
import org.json.JSONObject
import org.springframework.stereotype.Component
import org.uevola.jsonautovalidation.common.utils.ResourcesUtil
import org.uevola.jsonautovalidation.common.annotations.jsonValidationAnnotation.IsEnum
import org.uevola.jsonautovalidation.common.utils.JsonUtil
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
        generateSchema: (clazz: KClass<*>) -> JSONObject?
    ): JSONObject? {
        val enumClass = getEnumClass(property) ?: return null
        return generate(enumClass)
    }

    override fun generate(annotation: Annotation, parameter: Parameter): JSONObject? {
        val enumClass = getEnumClass(parameter) ?: return null
        return generate(enumClass)
    }

    private fun generate(enumClass: KClass<out Any>): JSONObject? {
        var enumValues = getEnumValues(enumClass)
        enumValues = enumValues + ""
        val values = mapOf("enum" to enumValues)
        val schemaName = IsEnum::class.simpleName ?: return null
        return JsonUtil.resolveTemplate(
            JSONObject(
                ResourcesUtil
                .getSchemaResource(schemaName)?.inputStream
                ?.bufferedReader().use { it?.readText() }), values
        )
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
        val objectMapper = ObjectMapper()
        val json = objectMapper.writeValueAsString(enumClass.java.enumConstants)
        return objectMapper.readValue(json, List::class.java)
    }
}