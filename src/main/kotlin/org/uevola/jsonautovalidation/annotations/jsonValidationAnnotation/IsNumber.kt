package org.uevola.jsonautovalidation.annotations.jsonValidationAnnotation

import org.springframework.core.annotation.AliasFor

@IsJsonValidation
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class IsNumber(
    @get:AliasFor(annotation = IsJsonValidation::class, attribute = "errorMessage")
    val errorMessage: String = "The field @{fieldName} must be a number and respect constraints: minimum=@{minimum}, maximum=@{maximum}, exclusiveMinimum=@{exclusiveMinimum}, exclusiveMaximum=@{exclusiveMaximum}, multipleOf=@{multipleOf}",
    @Suppress("unused") val minimum: Float = Float.MIN_VALUE,
    @Suppress("unused") val maximum: Float = Float.MAX_VALUE,
    @Suppress("unused") val exclusiveMinimum: Float = Float.MIN_VALUE,
    @Suppress("unused") val exclusiveMaximum: Float = Float.MAX_VALUE,
    @Suppress("unused") val multipleOf: Int = 1,
)
