package org.uevola.jsonautovalidation.common.extensions

import org.uevola.jsonautovalidation.api.annotations.rules.IsRequired
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

internal fun Class<*>.isIgnoredType(): Boolean {
    return this.name.startsWith("kotlin.") ||
            this.name.startsWith("java.") ||
            this.name.startsWith("javax.") ||
            this.name.startsWith("jakarta.")
}

internal fun KClass<*>.getRequiredJsonPropertiesNames(): List<String> {
    return this.memberProperties
        .filter { property ->
            property
                .javaField
                ?.declaredAnnotations
                ?.any { it.annotationClass.java == IsRequired::class.java }
                ?: false
        }
        .map { it.getJsonPropertyName() }
}