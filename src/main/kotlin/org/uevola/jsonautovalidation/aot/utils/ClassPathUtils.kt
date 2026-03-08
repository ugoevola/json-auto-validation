package org.uevola.jsonautovalidation.aot.utils

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.uevola.jsonautovalidation.aot.config.JsonValidationConfig
import org.uevola.jsonautovalidation.api.annotations.JsonValidation

internal object ClassPathUtils {

    private val scanner = ClassPathScanningCandidateComponentProvider(false)

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