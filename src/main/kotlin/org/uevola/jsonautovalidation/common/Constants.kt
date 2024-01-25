package org.uevola.jsonautovalidation.common

import org.uevola.jsonautovalidation.common.annotations.jsonValidationAnnotation.IsStringBool
import org.uevola.jsonautovalidation.common.annotations.jsonValidationAnnotation.IsStringInteger
import org.uevola.jsonautovalidation.common.annotations.jsonValidationAnnotation.IsStringNumber

object Constants {
    const val SCHEMA_JSON_EXT = ".schema.json"
    const val GENERATED_JSON_PATH = "/generated-schemas"
    const val STRING_INTEGER_KEYWORD = "string-integer"
    const val STRING_NUMBER_KEYWORD = "string-number"

    val ANNOTATIONS_THAT_OVERRIDE_INFERRED_ANNOTATIONS: List<Annotation> =
        listOf(
            IsStringInteger(),
            IsStringNumber(),
            IsStringBool()
        )
}