package org.uevola.jsonautovalidation.common.extensions

import org.springframework.web.bind.annotation.RequestMapping
import org.uevola.jsonautovalidation.api.annotations.Validate
import org.uevola.jsonautovalidation.api.annotations.jsonValidationAnnotation.IsRequired
import java.lang.reflect.Method
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

internal fun Class<*>.isIgnoredType(): Boolean {
    return this.name.startsWith("kotlin.") ||
            this.name.startsWith("java.") ||
            this.name.startsWith("javax.") ||
            this.name.startsWith("jakarta.")
}

internal fun Class<*>.getMethodsToValidate(): List<Method> =
    if (this.annotations.any { it is Validate })
        this.declaredMethods.toList()
    else
        this.declaredMethods.filter { method ->
            method.annotations.any { annotation ->
                annotation is RequestMapping || annotation.annotationClass.annotations.any { it is RequestMapping }
            }
        }

internal fun KClass<*>.getRequiredJsonPropertiesNames(): List<String> {
    return this.memberProperties
        .filter { property -> property
            .javaField
            ?.declaredAnnotations
            ?.any { it.annotationClass.java == IsRequired::class.java }
            ?: false
        }
        .map { it.getJsonPropertyName() }
}