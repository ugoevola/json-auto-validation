package org.uevola.jsonautovalidation.utils.annotations

import org.springframework.context.annotation.Import
import org.uevola.jsonautovalidation.configuration.JsonValidationComponentScan

@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(JsonValidationComponentScan::class)
annotation class EnableJsonAutoValidation
