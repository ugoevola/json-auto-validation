package org.uevola.jsonautovalidation.common.strategies.schemas

import org.uevola.jsonautovalidation.aot.schemas.jsonSchemas
import org.uevola.jsonautovalidation.api.annotations.rules.IsJsonSchema
import org.uevola.jsonautovalidation.common.utils.JsonUtils
import tools.jackson.databind.node.ObjectNode
import java.lang.reflect.Parameter
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

internal object IsJsonSchemaJsonGenerator : JsonSchemaGeneratorStrategy {

    override fun getOrdered() = 0

    override fun resolve(annotation: Annotation) = annotation is IsJsonSchema

    override fun generate(
        annotation: Annotation,
        property: KProperty1<out Any, *>,
        generateSchema: (clazz: KClass<*>) -> ObjectNode?
    ) = generate(annotation)

    override fun generate(annotation: Annotation, parameter: Parameter) = generate(annotation)

    private fun generate(annotation: Annotation): ObjectNode? {
        if (annotation !is IsJsonSchema) return null
        val jsonString = jsonSchemas[annotation.annotationClass]
        return JsonUtils.objectNodeFromString(jsonString)
    }
}