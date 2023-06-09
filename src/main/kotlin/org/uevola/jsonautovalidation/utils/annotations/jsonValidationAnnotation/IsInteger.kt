package org.uevola.jsonautovalidation.utils.annotations.jsonValidationAnnotation

@IsJsonValidation
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class IsInteger(
    val message: String = "",
    val minimum: Int = Int.MIN_VALUE,
    val maximum: Int = Int.MAX_VALUE,
    val exclusiveMinimum: Int = Int.MIN_VALUE,
    val exclusiveMaximum: Int = Int.MAX_VALUE,
    val multipleOf: Int = 1,
)
