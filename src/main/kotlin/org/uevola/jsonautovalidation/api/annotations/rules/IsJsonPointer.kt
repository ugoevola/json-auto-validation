package org.uevola.jsonautovalidation.api.annotations.rules

import org.springframework.core.annotation.AliasFor

@IsJsonValidation
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class IsJsonPointer(
    @get:AliasFor(annotation = IsJsonValidation::class, attribute = "errorMessage")
    val errorMessage: String = "The field @{fieldName} must be a valid JSON pointer.",
)
