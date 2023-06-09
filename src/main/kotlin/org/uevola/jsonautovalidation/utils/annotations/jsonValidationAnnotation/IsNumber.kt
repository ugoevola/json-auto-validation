package org.uevola.jsonautovalidation.utils.annotations.jsonValidationAnnotation

@IsJsonValidation
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class IsNumber(
    val message: String = "",
    val minimum: Float = Float.MIN_VALUE,
    val maximum: Float = Float.MAX_VALUE,
    val exclusiveMinimum: Float = Float.MIN_VALUE,
    val exclusiveMaximum: Float = Float.MAX_VALUE,
    val multipleOf: Int = 1,
)
