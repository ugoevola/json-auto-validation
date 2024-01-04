package org.uevola.jsonautovalidation.common.annotations.jsonValidationAnnotation

@Retention(AnnotationRetention.RUNTIME)
annotation class IsJsonValidation(
    val message: String = ""
)
