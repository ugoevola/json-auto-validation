package org.uevola.jsonautovalidation.utils.annotations.jsonValidationAnnotation

import org.uevola.jsonautovalidation.utils.enums.JsonTypeEnum

@IsJsonValidation
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class IsArray(
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
