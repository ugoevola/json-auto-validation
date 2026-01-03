package org.uevola.jsonautovalidation.annotations.jsonValidationAnnotation

import org.springframework.core.annotation.AliasFor
import org.uevola.jsonautovalidation.enums.JsonTypeEnum

@IsJsonValidation
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class IsArray(
    @get:AliasFor(annotation = IsJsonValidation::class, attribute = "errorMessage")
    val errorMessage: String = "The field @{fieldName} must be an array and respect constraints: minItems=@{minItems}, maxItems=@{maxItems}, uniqueItems@={uniqueItems}",
    val type: Array<JsonTypeEnum> = [
        JsonTypeEnum.ARRAY,
        JsonTypeEnum.BOOLEAN,
        JsonTypeEnum.INTEGER,
        JsonTypeEnum.NULL,
        JsonTypeEnum.NUMBER,
        JsonTypeEnum.OBJECT,
        JsonTypeEnum.STRING],
    @Suppress("unused") val minItems: Int = 0,
    @Suppress("unused") val maxItems: Int = Int.MAX_VALUE,
    @Suppress("unused") val uniqueItems: Boolean = false,
)
