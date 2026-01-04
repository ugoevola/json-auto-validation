package org.uevola.jsonautovalidation.aot

import org.springframework.beans.factory.aot.BeanFactoryInitializationAotContribution
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.getBean
import org.springframework.core.env.Environment
import org.uevola.jsonautovalidation.aot.config.JacksonConfiguration
import org.uevola.jsonautovalidation.aot.config.JsonValidationConfig
import org.uevola.jsonautovalidation.aot.generators.SchemasGenerator
import org.uevola.jsonautovalidation.aot.generators.ValidatorBeansGenerator

internal open class JsonValidationBeanFactoryInitializationAotProcessor :
    BeanFactoryInitializationAotProcessor {

    override fun processAheadOfTime(
        beanFactory: ConfigurableListableBeanFactory
    ): BeanFactoryInitializationAotContribution? {
        val env: Environment = beanFactory.getBean<Environment>()
        JsonValidationConfig.init(env)
        JacksonConfiguration.init(env)
        SchemasGenerator.generateJsonSchemaFiles()
        return BeanFactoryInitializationAotContribution { generationContext , _ ->
            ValidatorBeansGenerator.generateDtoValidator(generationContext)
        }
    }
}