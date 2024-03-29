package org.uevola.jsonautovalidation.strategies.schemas

import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.stereotype.Component
import org.uevola.jsonautovalidation.common.annotations.jsonValidationAnnotation.IsJsonSchema
import org.uevola.jsonautovalidation.common.schemas.jsonSchemas
import org.uevola.jsonautovalidation.common.utils.JsonUtil
import java.lang.reflect.Parameter
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

@Component
class IsJsonSchemaJsonGenerator : JsonSchemaGeneratorStrategy {

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
        return JsonUtil.objectNodeFromString(jsonString)
    }
}