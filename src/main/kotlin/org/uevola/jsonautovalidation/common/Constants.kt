package org.uevola.jsonautovalidation.common

import org.uevola.jsonautovalidation.annotations.jsonValidationAnnotation.IsStringBool
import org.uevola.jsonautovalidation.annotations.jsonValidationAnnotation.IsStringInteger
import org.uevola.jsonautovalidation.annotations.jsonValidationAnnotation.IsStringNumber

internal object Constants {
    const val VALIDATORS_PACKAGE_NAME = "org.uevola.jsonautovalidation.generated.validators"

    const val SCHEMA_JSON_EXT = ".schema.json"
    const val GENERATED_JSON_PATH = "/generated-schemas"
    const val STRING_INTEGER_KEYWORD = "string-integer"
    const val STRING_NUMBER_KEYWORD = "string-number"
    const val ERROR_MESSAGE_KEYWORD = "errorMessage"

    val ANNOTATIONS_THAT_OVERRIDE_INFERRED_ANNOTATIONS: List<Annotation> =
        listOf(
            IsStringInteger(),
            IsStringNumber(),
            IsStringBool()
        )
}