package org.uevola.jsonautovalidation.utils.annotations.jsonValidationAnnotation

@IsJsonValidation
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class IsNotEmpty(val message: String = "")