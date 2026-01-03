package org.uevola.jsonautovalidation.annotations.jsonValidationAnnotation

@Retention(AnnotationRetention.RUNTIME)
annotation class IsJsonValidation(
    val errorMessage: String = "The field @{fieldName} is invalid",
)
