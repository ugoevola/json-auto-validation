package org.uevola.jsonautovalidation.runtime.common.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "json-validation")
internal data class JsonAutoValidationProperties(
    val maxJsonSize: Long = 2,
    val dtoPackageName: String = "",
    val controllersPackageName: String = "",
    val reactiveApplication: String = ""
) {
    fun getMaxJsonSizeInBytes(): Long = maxJsonSize * 1024 * 1024
}
