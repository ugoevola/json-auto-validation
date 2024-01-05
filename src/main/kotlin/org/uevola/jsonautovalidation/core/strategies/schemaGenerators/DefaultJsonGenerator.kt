package org.uevola.jsonautovalidation.core.strategies.schemaGenerators

import org.json.JSONObject
import org.springframework.stereotype.Component
import org.uevola.jsonautovalidation.common.annotations.jsonValidationAnnotation.IsRequired
import org.uevola.jsonautovalidation.common.utils.JsonUtil
import org.uevola.jsonautovalidation.common.utils.ResourcesUtil
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
        generateSchema: (clazz: KClass<*>) -> JSONObject?
    ) = generate(annotation)

    override fun generate(annotation: Annotation, parameter: Parameter) = generate(annotation)

    private fun generate(annotation: Annotation): JSONObject? {
        val schemaName = annotation.annotationClass.simpleName ?: return null
        return JsonUtil.resolveTemplate(
            JSONObject(
                ResourcesUtil
                    .getResourceSchema(schemaName)?.inputStream
                    ?.bufferedReader().use { it?.readText() }), annotationEntries(annotation)
        )
    }

    private fun annotationEntries(annotation: Annotation) =
        annotation.annotationClass.declaredMemberProperties.associate { property ->
            property.name to property.call(annotation)
        }
}