package org.uevola.jsonautovalidation.common.exceptions

import org.springframework.core.NestedRuntimeException

class JsonValidationGenerationException(
    message: String,
    throwable: Throwable
): NestedRuntimeException(
    message,
    throwable
)