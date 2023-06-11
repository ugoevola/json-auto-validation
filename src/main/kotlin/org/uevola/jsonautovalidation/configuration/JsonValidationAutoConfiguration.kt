package org.uevola.jsonautovalidation.configuration

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@EnableCaching
@Configuration
@ConditionalOnClass(JsonValidationConfiguration::class)
@Import(JsonValidationImportSelector::class)
open class JsonValidationAutoConfiguration