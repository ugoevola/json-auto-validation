package org.uevola.jsonautovalidation.aot.config

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.core.env.Environment
import org.uevola.jsonautovalidation.aot.enums.EnvPropertyName

internal object JsonValidationConfig: Configuration() {
    private val logger = KotlinLogging.logger {}

    @EnvPropertyName("json-validation.dto-package-name")
    lateinit var dtoPackageName: String

    @EnvPropertyName("json-validation.controllers-package-name")
    lateinit var controllersPackageName: String

    fun init(environment: Environment) {
        initConfig(JsonValidationConfig, environment, logger)
    }
}