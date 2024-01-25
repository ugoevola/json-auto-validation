package org.uevola.jsonautovalidation.common.extensions

import org.uevola.jsonautovalidation.common.annotations.jsonValidationAnnotation.*
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.isSubclassOf

fun KType.getCorrespondedJsonValidationAnnotation(): Annotation? {
    val returnType = this.classifier as? KClass<*> ?: return null

    return when {
        returnType.isSubclassOf(String::class) -> IsString()
        returnType.isSubclassOf(Enum::class) -> IsEnum()
        returnType.isSubclassOf(Int::class) -> IsInteger()
        returnType.isSubclassOf(Number::class) -> IsNumber()
        returnType.isSubclassOf(Boolean::class) -> IsBool()
        else -> null
    }
}