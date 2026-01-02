package org.uevola.jsonautovalidation.annotations.jsonValidationAnnotation

@Retention(AnnotationRetention.RUNTIME)
annotation class IsJsonValidation(
    val message: String = ""
)
