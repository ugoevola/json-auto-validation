package org.uevola.jsonautovalidation.api.annotations.jsonValidationAnnotation

import org.springframework.core.annotation.AliasFor

@IsJsonValidation
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class IsInteger(
    @get:AliasFor(annotation = IsJsonValidation::class, attribute = "errorMessage")
    val errorMessage: String = "",
    @Suppress("unused") val typeErrorMessage: String = "The field @{fieldName} must be an integer.",
    @Suppress("unused") val minimumErrorMessage: String = "The field @{fieldName} must be greater than or equal to @{minimum}.",
    @Suppress("unused") val maximumErrorMessage: String = "The field @{fieldName} must be less than or equal to @{maximum}.",
    @Suppress("unused") val exclusiveMinimumErrorMessage: String = "The field @{fieldName} must be greater than @{exclusiveMinimum}.",
    @Suppress("unused") val exclusiveMaximumErrorMessage: String = "The field @{fieldName} must be less than @{exclusiveMaximum}.",
    @Suppress("unused") val multipleOfErrorMessage: String = "The field @{fieldName} must be a multiple of @{multipleOf}.",
    @Suppress("unused") val minimum: Int = Int.MIN_VALUE,
    val maximum: Int = Int.MAX_VALUE,
    @Suppress("unused") val exclusiveMinimum: Int = Int.MIN_VALUE,
    @Suppress("unused") val exclusiveMaximum: Int = Int.MAX_VALUE,
    @Suppress("unused") val multipleOf: Int = 1,
)

