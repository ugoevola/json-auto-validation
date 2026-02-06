package org.uevola.jsonautovalidation.runtime.common.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "json-validation")
internal class JsonValidationProperties(
    var maxJsonSize: Long = 2,
    @Suppress("unused") var dtoPackageName: String = "",
    @Suppress("unused") var controllersPackageName: String = "",
    @Suppress("unused") var reactiveApplication: String = ""
) {
    fun getMaxJsonSizeInBytes(): Long = maxJsonSize * 1024 * 1024
}
