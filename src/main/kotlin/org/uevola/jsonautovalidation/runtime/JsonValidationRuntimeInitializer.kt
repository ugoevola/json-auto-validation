package org.uevola.jsonautovalidation.runtime

import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.EnvironmentAware
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import org.uevola.jsonautovalidation.aot.config.JacksonConfiguration
import org.uevola.jsonautovalidation.aot.config.JsonValidationConfig
import org.uevola.jsonautovalidation.aot.generators.SchemasGenerator
import org.uevola.jsonautovalidation.runtime.generator.ValidatorBeansGenerator

@Component
@ConditionalOnProperty(
    name = ["json-validation.runtime-generation"],
    havingValue = "true",
    matchIfMissing = false
)
internal class JsonValidationRuntimeInitializer : BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private lateinit var env: Environment

    override fun setEnvironment(environment: Environment) {
        this.env = environment
    }

    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {
        JsonValidationConfig.init(env)
        JacksonConfiguration.init(env)
        SchemasGenerator.generateJsonSchemaFiles()
        ValidatorBeansGenerator.generateDtoValidator(registry)
    }
}