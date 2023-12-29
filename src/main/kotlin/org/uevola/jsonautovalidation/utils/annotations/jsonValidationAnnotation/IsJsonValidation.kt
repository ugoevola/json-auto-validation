package org.uevola.jsonautovalidation.utils.annotations.jsonValidationAnnotation

@Retention(AnnotationRetention.RUNTIME)
annotation class IsJsonValidation(
    val message: String = ""
)
