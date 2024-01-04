package org.uevola.jsonautovalidation.common.annotations.jsonValidationAnnotation

import org.springframework.core.annotation.AliasFor

@IsJsonValidation
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class IsUri(
    @get:AliasFor(annotation = IsJsonValidation::class, attribute = "message")
    val message: String = ""
)
