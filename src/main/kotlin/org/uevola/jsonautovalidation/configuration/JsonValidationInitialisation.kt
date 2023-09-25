package org.uevola.jsonautovalidation.configuration

import mu.KLogging
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.context.EnvironmentAware
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.uevola.jsonautovalidation.utils.generators.JsonValidationBeanGenerator
import org.uevola.jsonautovalidation.utils.generators.JsonValidationSchemaGenerator
import kotlin.time.ExperimentalTime

@ExperimentalTime
@Configuration
open class JsonValidationInitialisation:
    BeanDefinitionRegistryPostProcessor,
    EnvironmentAware {
    companion object : KLogging()

    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {}

    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {
        JsonValidationBeanGenerator.generateBeans(registry)
    }

    override fun setEnvironment(environment: Environment) {
        JsonValidationConfiguration.init(environment)
    }
}