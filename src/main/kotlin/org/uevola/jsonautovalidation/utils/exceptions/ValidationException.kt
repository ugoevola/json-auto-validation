package org.uevola.jsonautovalidation.utils.exceptions

import com.networknt.schema.ValidationMessage
import org.springframework.http.HttpStatusCode
import org.springframework.web.client.HttpClientErrorException

class ValidationException(
    message: String,
    statusCode: HttpStatusCode,
    val validationMessages: Set<ValidationMessage>
): HttpClientErrorException(
    message,
    statusCode,
    "REQUEST_VALIDATION_KO",
    null,
    null,
    null
)