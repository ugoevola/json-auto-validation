package org.uevola.jsonautovalidation.strategies.schemaGenerators

import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.stereotype.Component
import org.uevola.jsonautovalidation.common.annotations.jsonValidationAnnotation.IsJsonSchema
import org.uevola.jsonautovalidation.common.utils.JsonUtil
import org.uevola.jsonautovalidation.common.utils.ResourcesUtil
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
        val jsonString = ResourcesUtil
            .getResourceSchema(annotation.jsonSchemaName)?.inputStream
            ?.bufferedReader().use { it?.readText() }
        return JsonUtil.objectNodeFromString(jsonString)
    }
}