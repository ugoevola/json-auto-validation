package org.uevola.jsonautovalidation.configuration

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType

@Configuration
@ComponentScan(
    basePackages = ["org.uevola.jsonautovalidation"],
    excludeFilters = [ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        value = [JsonValidationAutoConfiguration::class]
    )]
)
@EnableConfigurationProperties(JsonValidationConfiguration::class)
open class JsonValidationComponentScan