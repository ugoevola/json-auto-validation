package org.uevola.jsonautovalidation.annotations.jsonValidationAnnotation

import org.springframework.core.annotation.AliasFor

@IsJsonValidation
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class IsIpv4(
    @get:AliasFor(annotation = IsJsonValidation::class, attribute = "errorMessage")
    val errorMessage: String = "The field @{fieldName} must be a valid IPv4 address",
)
