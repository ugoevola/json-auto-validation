package org.uevola.jsonautovalidation.configuration

import mu.KLogging
import net.bytebuddy.ByteBuddy
import net.bytebuddy.description.modifier.Visibility
import net.bytebuddy.description.type.TypeDescription
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.RootBeanDefinition
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.core.ResolvableType
import org.uevola.jsonautovalidation.utils.Utils
import org.uevola.jsonautovalidation.utils.generators.JsonGenerator
import org.uevola.jsonautovalidation.utils.validators.JsonSchemaValidator
import java.lang.reflect.Parameter
import java.lang.reflect.ParameterizedType
import java.util.*

@Configuration
open class JsonValidationInitializer(
    private val utils: Utils,
    jsonGenerator: JsonGenerator,
) : ApplicationListener<ContextRefreshedEvent> {
    companion object : KLogging()

    init {
        jsonGenerator.generateJsonSchemaFiles()
    }

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        if (event.applicationContext !is BeanDefinitionRegistry) return
        logger.info { "Generation of Json Bean Validators" }
        utils.getControllersToValidate().forEach { controller ->
            utils.getMethodsToValidate(controller).forEach { method ->
                utils.getParamsToValidate(controller, method)
                    .filter { parameter -> parameter.parameterizedType !is ParameterizedType }
                    .forEach { parameter -> generateBeanValidator(event, parameter) }
            }
        }
    }

    private fun generateBeanValidator(event: ContextRefreshedEvent, parameter: Parameter) {
        val beanDefinitionRegistry = event.applicationContext as BeanDefinitionRegistry
        val beanName =
            parameter.type.simpleName.replaceFirstChar { it.lowercase(Locale.getDefault()) }.plus("Validator")
        if (beanDefinitionRegistry.containsBeanDefinition(beanName)) return
        val resolvableType = ResolvableType.forClassWithGenerics(JsonSchemaValidator::class.java, parameter.type)
        val beanDefinition =
            RootBeanDefinition(JsonSchemaValidator::class.java) { getJsonSchemaValidatorFor(parameter.type) }
        beanDefinition.setTargetType(resolvableType)
        beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinition)
    }

    private fun getJsonSchemaValidatorFor(dtoClass: Class<*>?): JsonSchemaValidator<*> {
        val generic =
            TypeDescription.Generic.Builder.parameterizedType(JsonSchemaValidator::class.java, dtoClass).build()
        return ByteBuddy()
            .subclass(generic)
            .defineField("names", generic, Visibility.PRIVATE)
            .make()
            .load(JsonSchemaValidator::class.java.classLoader, ClassLoadingStrategy.Default.WRAPPER)
            .loaded
            .getDeclaredConstructor()
            .apply { isAccessible = true }
            .newInstance() as JsonSchemaValidator<*>
    }
}