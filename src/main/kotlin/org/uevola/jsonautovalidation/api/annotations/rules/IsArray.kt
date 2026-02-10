package org.uevola.jsonautovalidation.api.annotations.rules

import org.springframework.core.annotation.AliasFor
import org.uevola.jsonautovalidation.api.enums.JsonTypeEnum

@IsJsonValidation
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class IsArray(
    @get:AliasFor(annotation = IsJsonValidation::class, attribute = "errorMessage")
    val errorMessage: String = "",
    @Suppress("unused") val typeErrorMessage: String = "The field @{fieldName} must be an array of type @{type}.",
    @Suppress("unused") val minItemsErrorMessage: String = "The field @{fieldName} must have at least @{minItems} items.",
    @Suppress("unused") val maxItemsErrorMessage: String = "The field @{fieldName} must have at most @{maxItems} items.",
    @Suppress("unused") val uniqueItemsErrorMessage: String = "The field @{fieldName} must have unique items.",
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
