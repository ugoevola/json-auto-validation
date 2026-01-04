package org.uevola.jsonautovalidation.common.extensions

import com.fasterxml.jackson.annotation.JsonProperty
import org.uevola.jsonautovalidation.aot.config.JacksonConfiguration
import org.uevola.jsonautovalidation.common.Constants
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.javaField

internal fun KProperty1<out Any, *>.getJsonPropertyName(): String {
    val jsonPropertyAnnotation = this.javaField?.getDeclaredAnnotation(JsonProperty::class.java)
    if (jsonPropertyAnnotation != null && jsonPropertyAnnotation.value.isNotBlank()) {
        return jsonPropertyAnnotation.value
    }

    val mapper = JacksonConfiguration.jsonMapper
    val config = mapper.deserializationConfig()
    val strategy = config.propertyNamingStrategy

    return strategy.nameForField(config, null, this.name)
}

/**
 * Retrieves annotations linked to the property
 * and inferred annotations linked to the property type.
 * If an annotation that overrides the inferred annotation is already present on the property,
 * the inferred process will be ignored.
 */
internal fun KProperty1<out Any, *>.getAnnotations(): Array<Annotation> {
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