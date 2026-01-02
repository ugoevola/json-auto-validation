package org.uevola.jsonautovalidation.aot.enums

import kotlin.reflect.KClass

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
internal annotation class EnvPropertyName(
    val key: String,
    val type: KClass<*> = Nothing::class
)