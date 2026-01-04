package org.uevola.jsonautovalidation.aot.config

import io.github.oshai.kotlinlogging.KLogger
import org.springframework.core.env.Environment
import org.uevola.jsonautovalidation.aot.enums.EnvPropertyName
import tools.jackson.databind.PropertyNamingStrategies
import tools.jackson.databind.PropertyNamingStrategy
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.javaField

internal abstract class Configuration {

    fun initConfig(config: Any, environment: Environment, logger: KLogger) {
        this::class.declaredMemberProperties
            .forEach { kProp ->
                val annotation = kProp.findAnnotation<EnvPropertyName>() ?: return@forEach

                val key = annotation.key
                val type = annotation.type

                val stringValue = environment.getProperty(key) ?: ""
                val value = if (type === PropertyNamingStrategy::class)
                    keyToPropertyNamingStrategy(stringValue)
                else stringValue

                val javaField = kProp.javaField
                if (javaField != null) {
                    javaField.isAccessible = true
                    javaField.set(config, value)
                    logger.debug { "Initialized property '${kProp.name}' from '$key' = '${stringValue}'" }
                }
            }
    }

    fun keyToPropertyNamingStrategy(key: String?): PropertyNamingStrategy? =
        when (key) {
            "SNAKE_CASE" -> PropertyNamingStrategies.SNAKE_CASE
            "LOWER_CAMEL_CASE" -> PropertyNamingStrategies.LOWER_CAMEL_CASE
            "UPPER_CAMEL_CASE" -> PropertyNamingStrategies.UPPER_CAMEL_CASE
            "KEBAB_CASE" -> PropertyNamingStrategies.KEBAB_CASE
            "LOWER_CASE" -> PropertyNamingStrategies.LOWER_CASE
            else -> null
        }

}