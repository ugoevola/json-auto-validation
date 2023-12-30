package org.uevola.jsonautovalidation.utils.exceptions

import org.springframework.core.NestedRuntimeException

class JsonValidationGenerationException(
    message: String,
    throwable: Throwable
): NestedRuntimeException(
    message,
    throwable
)