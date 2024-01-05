package org.uevola.jsonautovalidation.common.exceptions

import com.networknt.schema.ValidationMessage

class KeywordValidationException(
    val validationMessage: ValidationMessage? = null
) : RuntimeException()