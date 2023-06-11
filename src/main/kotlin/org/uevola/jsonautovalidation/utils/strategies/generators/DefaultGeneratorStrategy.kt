package org.uevola.jsonautovalidation.utils.strategies.generators

import org.json.JSONObject
import org.springframework.stereotype.Component
import org.uevola.jsonautovalidation.utils.Utils
import org.uevola.jsonautovalidation.utils.annotations.jsonValidationAnnotation.IsRequired
import java.lang.reflect.Parameter
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties

@Component
class DefaultGeneratorStrategy(private val utils: Utils) : JsonSchemaGeneratorStrategy {

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
        return utils.resolveTemplate(
            JSONObject(utils
                .getSchemaResource(schemaName)?.inputStream
                ?.bufferedReader().use { it?.readText() }), annotationEntries(annotation)
        )
    }

    private fun annotationEntries(annotation: Annotation) =
        annotation.annotationClass.declaredMemberProperties.associate { property ->
            property.name to property.call(annotation)
        }
}