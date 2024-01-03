package org.uevola.jsonautovalidation.utils.exceptions

import com.networknt.schema.ValidationMessage

class KeywordValidationException(
    val validationMessage: ValidationMessage? = null
): RuntimeException()