package org.uevola.jsonautovalidation.configuration

import mu.KLogging
import org.springframework.core.env.Environment
import org.uevola.jsonautovalidation.utils.Util

object JsonValidationConfig: KLogging() {
    private const val JSON_VALIDATION_KEY_PROPERTY = "json-validation"

    lateinit var dtoPackageName: String
    lateinit var resourcesPath: String
    lateinit var controllersPackageName: String

    fun init(environment: Environment) {
        initDtoPackageName(environment)
        initResourcesPath(environment)
        initControllersPackageName(environment)
    }

    private fun initDtoPackageName(env: Environment) {
        dtoPackageName = env.getProperty(
            "$JSON_VALIDATION_KEY_PROPERTY.dto-package-name"
        ) ?: Util.rootPackage
        logger.info { "Dto package for auto validation: $dtoPackageName" }
    }

    private fun initResourcesPath(env: Environment) {
        resourcesPath = env.getProperty(
            "$JSON_VALIDATION_KEY_PROPERTY.resources-path"
        ) ?: ""
        logger.info { "Resources path for json auto validation: $resourcesPath" }
    }

    private fun initControllersPackageName(env: Environment) {
        controllersPackageName = env.getProperty(
            "$JSON_VALIDATION_KEY_PROPERTY.controllers-package-name"
        ) ?: Util.rootPackage
        logger.info { "Controllers package for auto validation: $controllersPackageName" }
    }
}