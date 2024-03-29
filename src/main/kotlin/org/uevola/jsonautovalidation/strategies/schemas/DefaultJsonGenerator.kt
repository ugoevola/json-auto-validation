package org.uevola.jsonautovalidation.strategies.schemas

import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.stereotype.Component
import org.uevola.jsonautovalidation.common.annotations.jsonValidationAnnotation.IsRequired
import org.uevola.jsonautovalidation.common.extensions.resolveTemplate
import org.uevola.jsonautovalidation.common.schemas.jsonSchemas
import org.uevola.jsonautovalidation.common.utils.JsonUtil.objectNodeFromString
import java.lang.reflect.Parameter
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties

@Component
class DefaultJsonGenerator : JsonSchemaGeneratorStrategy {

    override fun getOrdered() = 1

    override fun resolve(annotation: Annotation) = annotation !is IsRequired

    override fun generate(
        annotation: Annotation,
        property: KProperty1<out Any, *>,
        generateSchema: (clazz: KClass<*>) -> ObjectNode?
    ) = generate(annotation)

    override fun generate(annotation: Annotation, parameter: Parameter) = generate(annotation)

    private fun generate(annotation: Annotation): ObjectNode {
        val jsonString = jsonSchemas[annotation.annotationClass]
        val objectNode = objectNodeFromString(jsonString)
        return objectNode.resolveTemplate(annotationEntries(annotation))
    }

    private fun annotationEntries(annotation: Annotation) =
        annotation.annotationClass.declaredMemberProperties.associate { property ->
            property.name to property.call(annotation)
        }
}