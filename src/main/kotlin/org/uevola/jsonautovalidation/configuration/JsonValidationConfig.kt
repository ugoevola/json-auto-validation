package org.uevola.jsonautovalidation.configuration

import mu.KLogging
import org.springframework.core.env.Environment

object JsonValidationConfig : KLogging() {
    private const val JSON_VALIDATION_KEY_PROPERTY = "json-validation"

    lateinit var dtoPackageName: String
    lateinit var controllersPackageName: String

    fun init(environment: Environment) {
        initDtoPackageName(environment)
        initControllersPackageName(environment)
    }

    private fun initDtoPackageName(env: Environment) {
        dtoPackageName = env.getProperty(
            "$JSON_VALIDATION_KEY_PROPERTY.dto-package-name"
        ) ?: ""
        logger.info { "Dto package for auto validation: $dtoPackageName" }
    }

    private fun initControllersPackageName(env: Environment) {
        controllersPackageName = env.getProperty(
            "$JSON_VALIDATION_KEY_PROPERTY.controllers-package-name"
        ) ?: ""
        logger.info { "Controllers package for auto validation: $controllersPackageName" }
    }
}