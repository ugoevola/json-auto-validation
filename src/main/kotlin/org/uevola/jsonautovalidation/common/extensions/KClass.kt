package org.uevola.jsonautovalidation.common.extensions

import com.fasterxml.jackson.databind.ObjectMapper
import org.uevola.jsonautovalidation.common.annotations.jsonValidationAnnotation.IsRequired
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

fun Class<*>.isIgnoredType(): Boolean {
    return this.name.startsWith("kotlin.") ||
            this.name.startsWith("java.") ||
            this.name.startsWith("javax.") ||
            this.name.startsWith("jakarta.")
}

fun KClass<*>.getRequiredJsonPropertiesNames(
    objectMapper: ObjectMapper
): List<String> {
    return this.memberProperties
        .filter { property -> property
            .javaField
            ?.declaredAnnotations
            ?.any { it.annotationClass.java == IsRequired::class.java }
            ?: false
        }
        .map { it.getJsonPropertyName(objectMapper) }
}