package org.uevola.jsonautovalidation.utils.annotations.jsonValidationAnnotation

import org.springframework.core.annotation.AliasFor

@IsJsonValidation
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class IsString(
    @get:AliasFor(annotation = IsJsonValidation::class, attribute = "message")
    val message: String = "",
    val minLength: Int = Int.MIN_VALUE,
    val maxLength: Int = Int.MAX_VALUE,
    val pattern: String = ".*",
    val format: String = "*",
)
