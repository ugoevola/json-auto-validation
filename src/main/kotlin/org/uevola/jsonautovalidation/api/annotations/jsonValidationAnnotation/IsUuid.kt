package org.uevola.jsonautovalidation.api.annotations.jsonValidationAnnotation

import org.springframework.core.annotation.AliasFor

@IsJsonValidation
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class IsUuid(
    @get:AliasFor(annotation = IsJsonValidation::class, attribute = "message")
    val errorMessage: String = "The field @{fieldName} must be a valid UUID."
)
