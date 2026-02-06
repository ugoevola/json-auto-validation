package org.uevola.jsonautovalidation.common.enums

internal enum class ContentTypeEnum(val value: String) {
    APPLICATION_JSON("application/json"),
    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded"),
    MULTIPART_FORM_DATA("multipart/form-data")
}