package org.uevola.jsonautovalidation.common.utils

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.uevola.jsonautovalidation.common.exceptions.JsonValidationGenerationException

fun Class<*>.isIgnoredType(): Boolean {
    return this.name.startsWith("kotlin.") ||
            this.name.startsWith("java.") ||
            this.name.startsWith("javax.") ||
            this.name.startsWith("jakarta.")
}

object ClassesUtil {

    var rootPackage: String

    init {
        rootPackage = resolveRootPackage()
    }

    fun getAnnotatedClassesIn(basePackage: String, annotationType: Class<out Annotation>): Set<Class<*>> {
        try {
            val scanner = ClassPathScanningCandidateComponentProvider(false)
            scanner.addIncludeFilter(AnnotationTypeFilter(annotationType))
            return scanner.findCandidateComponents(basePackage)
                .map { Class.forName(it.beanClassName) }
                .toSet()
        } catch (exception: Exception) {
            throw JsonValidationGenerationException(
                "Error while scanning $basePackage for annotation ${annotationType.name}",
                exception
            )
        }
    }


    /**
     * Retrieve the package of a packaged class name
     * example: gives "com.exemple.test", then the result will be "com.exemple"
     * return empty string if no package
     */
    private fun getRootPackage(classPackage: String): String {
        val lastDotIndex = classPackage.lastIndexOf('.')
        return if (lastDotIndex != -1) classPackage.substring(0, lastDotIndex) else ""
    }

    /**
     * Calculates the package in which the Class annotated with @SpringBootApplication is located
     * Considered as the root package
     */
    private fun resolveRootPackage(): String {
        val scanner = ClassPathScanningCandidateComponentProvider(false)
        scanner.addIncludeFilter(AnnotationTypeFilter(SpringBootApplication::class.java))
        val candidates = scanner.findCandidateComponents("")
        if (candidates.isNotEmpty()) {
            val candidate = candidates.iterator().next()
            val candidateClassName = candidate.beanClassName
            if (candidateClassName != null) {
                return getRootPackage(candidateClassName)
            }
        }
        throw IllegalStateException("Unable to resolve the root package.")
    }
}