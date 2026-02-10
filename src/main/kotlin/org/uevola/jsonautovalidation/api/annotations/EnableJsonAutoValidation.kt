package org.uevola.jsonautovalidation.api.annotations

import org.springframework.context.annotation.Import
import org.uevola.jsonautovalidation.runtime.JsonAutoValidationComponentScan

@Suppress("unused")
@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(JsonAutoValidationComponentScan::class)
annotation class EnableJsonAutoValidation
