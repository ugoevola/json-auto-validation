package org.uevola.jsonautovalidation.common.strategies.schemas

import org.uevola.jsonautovalidation.api.annotations.jsonValidationAnnotation.IsRequired
import org.uevola.jsonautovalidation.aot.schemas.jsonSchemas
import org.uevola.jsonautovalidation.common.extensions.resolveTemplate
import org.uevola.jsonautovalidation.common.utils.JsonUtils.objectNodeFromString
import tools.jackson.databind.node.ObjectNode
import java.lang.reflect.Parameter
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

internal object DefaultJsonGenerator : JsonSchemaGeneratorStrategy {

    override fun getOrdered() = 1

    override fun resolve(annotation: Annotation) = annotation !is IsRequired

    override fun generate(
        annotation: Annotation,
        property: KProperty1<out Any, *>,
        generateSchema: (clazz: KClass<*>) -> ObjectNode?
    ) = generate(annotation, property.name)

    override fun generate(annotation: Annotation, parameter: Parameter) = generate(annotation, parameter.name)

    private fun generate(annotation: Annotation, fieldName: String): ObjectNode {
        val jsonString = jsonSchemas[annotation.annotationClass]
        val objectNode = objectNodeFromString(jsonString)
        return objectNode.resolveTemplate(annotationEntries(annotation), fieldName, hasGlobalErrorMessage(annotation))
    }
}