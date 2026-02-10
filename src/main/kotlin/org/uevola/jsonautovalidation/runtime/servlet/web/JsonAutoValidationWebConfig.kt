package org.uevola.jsonautovalidation.runtime.servlet.web

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@ConditionalOnProperty(
    name = ["json-validation.web-stack"],
    havingValue = "servlet",
    matchIfMissing = true
)
class JsonAutoValidationWebConfig(
    private val interceptor: JsonAutoValidationInterceptor
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(interceptor)
    }
}