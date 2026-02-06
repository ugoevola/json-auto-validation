package org.uevola.jsonautovalidation.common.strategies.schemas

import org.uevola.jsonautovalidation.api.annotations.jsonValidationAnnotation.IsNested
import tools.jackson.databind.node.ObjectNode
import java.lang.reflect.Parameter
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.javaType

internal object IsNestedJsonGenerator : JsonSchemaGeneratorStrategy {

    override fun getOrdered() = 0

    override fun resolve(annotation: Annotation) = annotation is IsNested

    override fun generate(
        annotation: Annotation,
        property: KProperty1<out Any, *>,
        generateSchema: (clazz: KClass<*>) -> ObjectNode?
    ): ObjectNode? {
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