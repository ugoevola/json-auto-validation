package org.uevola.jsonautovalidation.annotations

import org.springframework.context.annotation.Import
import org.uevola.jsonautovalidation.runtime.JsonValidationComponentScan

@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(JsonValidationComponentScan::class)
annotation class EnableJsonAutoValidation
