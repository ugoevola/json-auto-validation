package org.uevola.jsonautovalidation.common.utils

import org.springframework.http.HttpStatusCode
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException

object ExceptionUtil {

    /**
     * return an HttpClientErrorException with default statusText REQUEST_VALIDATION_KO
     *
     * @param message the exception message
     * @param statusCode the exception code
     */
    fun httpClientErrorException(message: String, statusCode: HttpStatusCode) =
        HttpClientErrorException(message, statusCode, "REQUEST_VALIDATION_KO", null, null, null)


    /**
     * return an HttpServerErrorException with default statusText REQUEST_VALIDATION_KO
     *
     * @param message the exception message
     * @param statusCode the exception code
     */
    fun httpServerErrorException(message: String, statusCode: HttpStatusCode) =
        HttpServerErrorException(message, statusCode, "REQUEST_VALIDATION_KO", null, null, null)
}