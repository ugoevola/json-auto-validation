package org.uevola.jsonautovalidation.core.strategies.schemaGenerators

import org.json.JSONObject
import org.springframework.stereotype.Component
import org.uevola.jsonautovalidation.utils.Util
import org.uevola.jsonautovalidation.utils.annotations.jsonValidationAnnotation.IsJsonSchema
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
        generateSchema: (clazz: KClass<*>) -> JSONObject?
    ) = generate(annotation)

    override fun generate(annotation: Annotation, parameter: Parameter) = generate(annotation)

    private fun generate(annotation: Annotation): JSONObject? {
        if (annotation !is IsJsonSchema) return null
        return JSONObject(Util
            .getSchemaResource(annotation.jsonSchemaName)
            ?.inputStream?.bufferedReader().use { it?.readText() })
    }
}