package org.uevola.jsonautovalidation.api.annotations.rules

@Retention(AnnotationRetention.RUNTIME)
annotation class IsJsonValidation(
    val errorMessage: String = "The field @{fieldName} is invalid.",
)
