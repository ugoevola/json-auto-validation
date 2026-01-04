package org.uevola.jsonautovalidation.aot.config

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.core.env.Environment
import org.uevola.jsonautovalidation.aot.enums.EnvPropertyName
import tools.jackson.databind.PropertyNamingStrategy
import tools.jackson.databind.json.JsonMapper

internal object JacksonConfiguration: Configuration() {
    private val logger = KotlinLogging.logger {}

    @EnvPropertyName(
        "spring.jackson.property-naming-strategy",
        PropertyNamingStrategy::class
    )
    private lateinit var namingStrategy: PropertyNamingStrategy
    lateinit var jsonMapper: JsonMapper

    fun init(environment: Environment) {
        initConfig(JsonValidationConfig, environment, logger)
        jsonMapper = JsonMapper.builder()
            .propertyNamingStrategy(namingStrategy)
            .build()
    }

}