package org.uevola.jsonautovalidation.common

import org.uevola.jsonautovalidation.api.annotations.jsonValidationAnnotation.IsStringBool
import org.uevola.jsonautovalidation.api.annotations.jsonValidationAnnotation.IsStringInteger
import org.uevola.jsonautovalidation.api.annotations.jsonValidationAnnotation.IsStringNumber

internal object Constants {
    const val VALIDATORS_PACKAGE_NAME = "org.uevola.jsonautovalidation.generated.validators"

    const val SCHEMA_JSON_EXT = ".schema.json"
    const val GENERATED_JSON_PATH = "/generated-schemas"
    const val STRING_INTEGER_KEYWORD = "string-integer"
    const val STRING_NUMBER_KEYWORD = "string-number"
    const val ERROR_MESSAGE_KEYWORD = "errorMessage"
    const val TYPE_ERROR_MESSAGE_PLACEHOLDER = "typeErrorMessage"
    const val MIN_ITEMS_ERROR_MESSAGE_PLACEHOLDER = "minItemsErrorMessage"
    const val MAX_ITEMS_ERROR_MESSAGE_PLACEHOLDER = "maxItemsErrorMessage"
    const val UNIQUE_ITEMS_ERROR_MESSAGE_PLACEHOLDER = "uniqueItemsErrorMessage"
    const val MINIMUM_ERROR_MESSAGE_PLACEHOLDER = "minimumErrorMessage"
    const val MAXIMUM_ERROR_MESSAGE_PLACEHOLDER = "maximumErrorMessage"
    const val EXCLUSIVE_MINIMUM_ERROR_MESSAGE_PLACEHOLDER = "exclusiveMinimumErrorMessage"
    const val EXCLUSIVE_MAXIMUM_ERROR_MESSAGE_PLACEHOLDER = "exclusiveMaximumErrorMessage"
    const val MULTIPLE_OF_ERROR_MESSAGE_PLACEHOLDER = "multipleOfErrorMessage"
    const val MIN_LENGTH_ERROR_MESSAGE_KEYWORD = "minLengthErrorMessage"
    const val MAX_LENGTH_ERROR_MESSAGE_KEYWORD = "maxLengthErrorMessage"
    const val PATTERN_ERROR_MESSAGE_KEYWORD = "patternErrorMessage"
    const val FORMAT_ERROR_MESSAGE_KEYWORD = "formatErrorMessage"
    const val REQUIRED_ERROR_MESSAGE_KEYWORD = "requiredErrorMessage"

    val ERROR_MESSAGE_PLACEHOLDERS = setOf(
        ERROR_MESSAGE_KEYWORD,
        TYPE_ERROR_MESSAGE_PLACEHOLDER,
        MIN_ITEMS_ERROR_MESSAGE_PLACEHOLDER,
        MAX_ITEMS_ERROR_MESSAGE_PLACEHOLDER,
        UNIQUE_ITEMS_ERROR_MESSAGE_PLACEHOLDER,
        MINIMUM_ERROR_MESSAGE_PLACEHOLDER,
        MAXIMUM_ERROR_MESSAGE_PLACEHOLDER,
        EXCLUSIVE_MINIMUM_ERROR_MESSAGE_PLACEHOLDER,
        EXCLUSIVE_MAXIMUM_ERROR_MESSAGE_PLACEHOLDER,
        MULTIPLE_OF_ERROR_MESSAGE_PLACEHOLDER,
        MIN_LENGTH_ERROR_MESSAGE_KEYWORD,
        MAX_LENGTH_ERROR_MESSAGE_KEYWORD,
        PATTERN_ERROR_MESSAGE_KEYWORD,
        FORMAT_ERROR_MESSAGE_KEYWORD
    )

    val ANNOTATIONS_THAT_OVERRIDE_INFERRED_ANNOTATIONS: List<Annotation> =
        listOf(
            IsStringInteger(),
            IsStringNumber(),
            IsStringBool()
        )
}