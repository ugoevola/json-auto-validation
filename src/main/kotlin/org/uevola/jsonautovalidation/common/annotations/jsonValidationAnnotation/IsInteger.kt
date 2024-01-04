package org.uevola.jsonautovalidation.common.annotations.jsonValidationAnnotation

import org.springframework.core.annotation.AliasFor

@IsJsonValidation
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class IsInteger(
    @get:AliasFor(annotation = IsJsonValidation::class, attribute = "message")
    val message: String = "",
    val minimum: Int = Int.MIN_VALUE,
    val maximum: Int = Int.MAX_VALUE,
    val exclusiveMinimum: Int = Int.MIN_VALUE,
    val exclusiveMaximum: Int = Int.MAX_VALUE,
    val multipleOf: Int = 1,
)
