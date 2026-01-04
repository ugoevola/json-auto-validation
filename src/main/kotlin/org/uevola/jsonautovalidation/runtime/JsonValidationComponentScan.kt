package org.uevola.jsonautovalidation.runtime

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.uevola.jsonautovalidation.runtime.config.JsonValidationProperties

@Configuration
@ComponentScan(
    basePackages = ["org.uevola.jsonautovalidation"]
)
@EnableConfigurationProperties(JsonValidationProperties::class)
class JsonValidationComponentScan