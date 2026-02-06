package org.uevola.jsonautovalidation.api.annotations

import org.springframework.context.annotation.Import
import org.uevola.jsonautovalidation.runtime.JsonValidationComponentScan

@Suppress("unused")
@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(JsonValidationComponentScan::class)
annotation class EnableJsonAutoValidation
