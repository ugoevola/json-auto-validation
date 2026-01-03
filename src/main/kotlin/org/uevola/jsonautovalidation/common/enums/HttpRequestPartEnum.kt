package org.uevola.jsonautovalidation.common.enums

internal enum class HttpRequestPartEnum(val value: String) {
    REQUEST_BODY("request body"),
    REQUEST_PARAMS("request param"),
    PATH_VARIABLES("path variables")
}