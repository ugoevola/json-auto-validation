package org.uevola.jsonautovalidation.configuration

import mu.KLogging
import org.springframework.core.env.Environment
import org.uevola.jsonautovalidation.utils.Util

object JsonValidationConfiguration: KLogging() {
    private const val JSON_VALIDATION_KEY_PROPERTY = "json-validation"

    lateinit var dtoPackageName: String
    lateinit var resourcesPath: String

    fun init(environment: Environment) {
        initDtoPackageName(environment)
        initResourcesPath(environment)
    }

    private fun initDtoPackageName(env: Environment) {
        dtoPackageName = env.getProperty(
            "$JSON_VALIDATION_KEY_PROPERTY.dto-package-name"
        ) ?: Util.resolveRootPackage()
        logger.info { "Resources path for json auto validation: $dtoPackageName" }
    }

    private fun initResourcesPath(env: Environment) {
        resourcesPath = env.getProperty(
            "$JSON_VALIDATION_KEY_PROPERTY.resources-path"
        ) ?: ""
        logger.info { "Dto package for auto validation: $resourcesPath" }
    }
}