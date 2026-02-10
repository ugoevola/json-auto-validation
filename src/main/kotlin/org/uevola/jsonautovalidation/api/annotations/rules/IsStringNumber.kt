package org.uevola.jsonautovalidation.api.annotations.rules

import org.springframework.core.annotation.AliasFor

@IsJsonValidation
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class IsStringNumber(
    @get:AliasFor(annotation = IsJsonValidation::class, attribute = "message")
    val errorMessage: String = "",
    @Suppress("unused") val typeErrorMessage: String = "The field @{fieldName} must be a number.",
    @Suppress("unused") val minimumErrorMessage: String = "The field @{fieldName} must be greater than or equal to @{minimum}.",
    @Suppress("unused") val maximumErrorMessage: String = "The field @{fieldName} must be less than or equal to @{maximum}.",
    @Suppress("unused") val exclusiveMinimumErrorMessage: String = "The field @{fieldName} must be greater than @{exclusiveMinimum}.",
    @Suppress("unused") val exclusiveMaximumErrorMessage: String = "The field @{fieldName} must be less than @{exclusiveMaximum}.",
    @Suppress("unused") val multipleOfErrorMessage: String = "The field @{fieldName} must be a multiple of @{multipleOf}.",
    @Suppress("unused") val minimum: Float = Float.MIN_VALUE,
    val maximum: Float = Float.MAX_VALUE,
    @Suppress("unused") val exclusiveMinimum: Float = Float.MIN_VALUE,
    @Suppress("unused") val exclusiveMaximum: Float = Float.MAX_VALUE,
    @Suppress("unused") val multipleOf: Int = 1,
)
