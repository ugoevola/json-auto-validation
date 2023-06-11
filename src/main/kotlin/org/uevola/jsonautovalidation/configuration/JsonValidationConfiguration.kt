package org.uevola.jsonautovalidation.configuration

import mu.KLogging
import org.springframework.boot.context.properties.ConfigurationProperties
import org.uevola.jsonautovalidation.utils.Utils

@ConfigurationProperties(prefix = "json-validation")
data class JsonValidationConfiguration(
    var resourcesPath: String = "json-schemas/",
    var dtoPackageName: String? = null,
) {

    companion object : KLogging()

    init {
        if (dtoPackageName == null) {
            dtoPackageName = "${Utils.resolveRootPackage()}.dto"
        }
        logger.info { "Resources path for json auto validation : $resourcesPath" }
        logger.info { "Dto package for auto validation : $dtoPackageName" }
    }
}