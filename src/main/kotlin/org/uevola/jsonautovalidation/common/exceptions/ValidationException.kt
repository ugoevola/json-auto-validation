package org.uevola.jsonautovalidation.common.exceptions

import org.springframework.http.HttpStatusCode
import org.springframework.web.client.HttpClientErrorException

class ValidationException(
    message: String,
    statusCode: HttpStatusCode
) : HttpClientErrorException(
    message,
    statusCode,
    "REQUEST_VALIDATION_KO",
    null,
    null,
    null
)