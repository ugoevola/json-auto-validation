package org.uevola.jsonautovalidation.strategies.schemaGenerators

import org.json.JSONObject
import org.springframework.stereotype.Component
import org.uevola.jsonautovalidation.common.annotations.jsonValidationAnnotation.IsNested
import java.lang.reflect.Parameter
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.javaType

@Component
class IsNestedJsonGenerator : JsonSchemaGeneratorStrategy {

    override fun getOrdered() = 0

    override fun resolve(annotation: Annotation) = annotation is IsNested

    override fun generate(
        annotation: Annotation,
        property: KProperty1<out Any, *>,
        generateSchema: (clazz: KClass<*>) -> JSONObject?
    ): JSONObject? {
        val clazz = getNestedClass(property) ?: return null
        return generateSchema(clazz)
    }

    override fun generate(annotation: Annotation, parameter: Parameter) = null

    private fun getNestedClass(property: KProperty1<out Any, *>) =
        if (property.returnType.classifier is KClass<*>)
            property.returnType.classifier as KClass<*>
        else if (property.returnType.javaType is Class<*>)
            (property.returnType.javaType as Class<*>).kotlin
        else null
}