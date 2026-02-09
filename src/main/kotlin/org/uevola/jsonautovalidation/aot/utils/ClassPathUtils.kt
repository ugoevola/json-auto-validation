package org.uevola.jsonautovalidation.aot.utils

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RestController
import org.uevola.jsonautovalidation.aot.config.JsonValidationConfig
import org.uevola.jsonautovalidation.api.annotations.JsonValidation

internal object ClassPathUtils {

    private val scanner = ClassPathScanningCandidateComponentProvider(false)
    fun getControllersToValidate(): Set<Class<*>> {
        scanner.addIncludeFilter(AnnotationTypeFilter(Controller::class.java))
        scanner.addIncludeFilter(AnnotationTypeFilter(RestController::class.java))
        val result = scanner.findCandidateComponents(JsonValidationConfig.controllersPackageName)
            .map { Class.forName(it.beanClassName) }
            .filter {
                try { it.declaredMethods; true }
                catch (_: NoClassDefFoundError) { false }
            }
            .toSet()
        scanner.clearCache()
        scanner.resetFilters(false)
        return result
    }

    fun getDtoClassesToValidate(): Set<Class<*>> {
        scanner.addIncludeFilter(AnnotationTypeFilter(JsonValidation::class.java))
        val result = scanner.findCandidateComponents(JsonValidationConfig.dtoPackageName)
            .map { Class.forName(it.beanClassName) }
            .toSet()
        scanner.clearCache()
        scanner.resetFilters(false)
        return result
    }

}