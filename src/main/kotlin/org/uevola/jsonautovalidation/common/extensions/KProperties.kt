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
            .propertyNamingStrategy
            .nameForField(objectMapper.deserializationConfig, null, this.name)
}

/**
 * Retrieves annotations linked to the property
 * and annotations linked to the property type, which are processed automatically.
 * If an annotation already present on the property overrides
 * an annotation that can be placed automatically, the automatic annotation will not be returned.
 */
fun KProperty1<out Any, *>.getAnnotations(): Array<Annotation> {
    var annotations = this
        .javaField
        ?.declaredAnnotations
        ?: emptyArray<Annotation>()
    val automaticAnnotation = this.returnType.getCorrespondedJsonValidationAnnotation()
    val automaticAnnotationCanBeAdded = automaticAnnotation != null
            && annotations.none { Constants.ANNOTATIONS_THAT_OVERRIDE_AUTOMATIC_ANNOTATION.contains(it) }
            && annotations.none { it.annotationClass == automaticAnnotation.annotationClass }
    if (automaticAnnotationCanBeAdded) {
        annotations = annotations.plus(automaticAnnotation)
    }
    return annotations
}