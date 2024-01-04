package org.uevola.jsonautovalidation.common.annotations.jsonValidationAnnotation

import org.springframework.core.annotation.AliasFor
import org.uevola.jsonautovalidation.common.enums.JsonTypeEnum

@IsJsonValidation
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class IsArray(
    @get:AliasFor(annotation = IsJsonValidation::class, attribute = "message")
    val message: String = "",
    val type: Array<JsonTypeEnum> = [
        JsonTypeEnum.ARRAY,
        JsonTypeEnum.BOOLEAN,
        JsonTypeEnum.INTEGER,
        JsonTypeEnum.NULL,
        JsonTypeEnum.NUMBER,
        JsonTypeEnum.OBJECT,
        JsonTypeEnum.STRING],
    val minItems: Int = 0,
    val maxItems: Int = Int.MAX_VALUE,
    val uniqueItems: Boolean = false,
)
