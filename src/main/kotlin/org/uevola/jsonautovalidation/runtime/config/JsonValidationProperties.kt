package org.uevola.jsonautovalidation.runtime.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "json-validation")
data class JsonValidationProperties(
    var dtoPackageName: String = "",
    var controllersPackageName: String = ""
)