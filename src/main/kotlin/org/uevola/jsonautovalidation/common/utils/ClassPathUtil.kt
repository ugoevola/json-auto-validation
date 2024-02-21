package org.uevola.jsonautovalidation.common.utils

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RestController
import org.uevola.jsonautovalidation.common.annotations.JsonValidation
import org.uevola.jsonautovalidation.configuration.JsonValidationConfig.controllersPackageName
import org.uevola.jsonautovalidation.configuration.JsonValidationConfig.dtoPackageName

object ClassPathUtil {

    private val scanner = ClassPathScanningCandidateComponentProvider(false)
    fun getControllersToValidate(): Set<Class<*>> {
        scanner.addIncludeFilter(AnnotationTypeFilter(Controller::class.java))
        scanner.addIncludeFilter(AnnotationTypeFilter(RestController::class.java))
        val result = scanner.findCandidateComponents(controllersPackageName)
            .map { Class.forName(it.beanClassName) }
            .filter {
                try { it.declaredMethods; true }
                catch (e: NoClassDefFoundError) { false }
            }
            .toSet()
        scanner.clearCache()
        scanner.resetFilters(false)
        return result
    }

    fun getDtoClassesToValidate(): Set<Class<*>> {
        scanner.addIncludeFilter(AnnotationTypeFilter(JsonValidation::class.java))
        val result = scanner.findCandidateComponents(dtoPackageName)
            .map { Class.forName(it.beanClassName) }
            .toSet()
        scanner.clearCache()
        scanner.resetFilters(false)
        return result
    }

}