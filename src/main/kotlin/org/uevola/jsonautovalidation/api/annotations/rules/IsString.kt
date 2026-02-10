package org.uevola.jsonautovalidation.api.annotations.rules

import org.springframework.core.annotation.AliasFor

@IsJsonValidation
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class IsString(
    @get:AliasFor(annotation = IsJsonValidation::class, attribute = "errorMessage")
    val errorMessage: String = "",
    @Suppress("unused") val typeErrorMessage: String = "The field @{fieldName} must be a string.",
    @Suppress("unused") val minLengthErrorMessage: String = "The field @{fieldName} must have at least @{minLength} characters.",
    @Suppress("unused") val maxLengthErrorMessage: String = "The field @{fieldName} must have at most @{maxLength} characters.",
    @Suppress("unused") val patternErrorMessage: String = "The field @{fieldName} must match the pattern /@{pattern}/.",
    @Suppress("unused") val formatErrorMessage: String = "The field @{fieldName} must respect the format '@{format}'.",
    @Suppress("unused") val minLength: Int = Int.MIN_VALUE,
    @Suppress("unused") val maxLength: Int = Int.MAX_VALUE,
    @Suppress("unused") val pattern: String = ".*",
    val format: String = "*",
)
