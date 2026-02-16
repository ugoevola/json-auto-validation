package org.uevola.jsonautovalidation.runtime

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.uevola.jsonautovalidation.runtime.common.config.JsonAutoValidationProperties

@Configuration
@ComponentScan(
    basePackages = ["org.uevola.jsonautovalidation.runtime", "org.uevola.jsonautovalidation.api"]
)
@EnableConfigurationProperties(JsonAutoValidationProperties::class)
class JsonAutoValidationComponentScan