package org.uevola.jsonautovalidation.annotations.jsonValidationAnnotation

import org.springframework.core.annotation.AliasFor

@IsJsonValidation
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class IsString(
    @get:AliasFor(annotation = IsJsonValidation::class, attribute = "errorMessage")
    val errorMessage: String = "The field @{fieldName} must be a string and respect constraints: minLength=@{minLength}, maxLength=@{maxLength}, pattern=@{pattern}, format=@{format}",
    @Suppress("unused") val minLength: Int = Int.MIN_VALUE,
    @Suppress("unused") val maxLength: Int = Int.MAX_VALUE,
    @Suppress("unused") val pattern: String = ".*",
    val format: String = "*",
)
