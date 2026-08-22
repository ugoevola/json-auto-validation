package org.uevola.jsonautovalidation.common.strategies.schemas

import org.uevola.jsonautovalidation.api.annotations.rules.IsJsonSchema
import org.uevola.jsonautovalidation.common.Constants.SCHEMA_JSON_EXT
import org.uevola.jsonautovalidation.common.utils.JsonUtils
import org.uevola.jsonautovalidation.common.utils.ResourceUtils
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

    /**
     * The schema is not built from the annotation but written by the application
     * itself: it is read from the resources, in the file named after the
     * annotation, suffixed with .schema.json.
     */
    private fun generate(annotation: Annotation): ObjectNode? {
        if (annotation !is IsJsonSchema) return null
        val jsonString = ResourceUtils.getResourceSchemaAsString(annotation.jsonSchemaName)
            ?: throw IllegalArgumentException(
                "No validation schema named ${annotation.jsonSchemaName} found: " +
                        "a resource file ${annotation.jsonSchemaName}$SCHEMA_JSON_EXT is expected in the classpath."
            )
        return JsonUtils.objectNodeFromString(jsonString)
    }
}
