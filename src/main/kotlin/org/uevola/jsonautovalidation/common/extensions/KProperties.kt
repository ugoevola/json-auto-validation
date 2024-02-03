package org.uevola.jsonautovalidation.common.extensions

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import org.uevola.jsonautovalidation.common.Constants
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.javaField

fun KProperty1<out Any, *>.getJsonPropertyName(
    objectMapper: ObjectMapper
): String {
    val jsonPropertyAnnotation = this.javaField?.getDeclaredAnnotation(JsonProperty::class.java)
    return jsonPropertyAnnotation?.value
        ?: objectMapper
            .deserializationConfig
            ?.propertyNamingStrategy
            ?.nameForField(objectMapper.deserializationConfig, null, this.name)
        ?: this.name
}

/**
 * Retrieves annotations linked to the property
 * and inferred annotations linked to the property type.
 * If an annotation that override inferred annotation is already present on the property,
 * the inferred process wil be ignored.
 */
fun KProperty1<out Any, *>.getAnnotations(): Array<Annotation> {
    var annotations = this
        .javaField
        ?.declaredAnnotations
        ?: emptyArray<Annotation>()
    val inferredAnnotation = this.returnType.getInferredAnnotation()
    val inferredAnnotationCanBeAdded = inferredAnnotation != null
            && annotations.none { Constants.ANNOTATIONS_THAT_OVERRIDE_INFERRED_ANNOTATIONS.contains(it) }
            && annotations.none { it.annotationClass == inferredAnnotation.annotationClass }
    if (inferredAnnotationCanBeAdded) {
        annotations = annotations.plus(inferredAnnotation)
    }
    return annotations
}