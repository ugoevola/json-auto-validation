# Installation

## Maven
> In coming...

## Gradle
> In coming...

## Enabling
To enable **Json-auto-validation**, you just have to annotate a Bean class with **@EnableJsonAutoValidation** annotation as follows:

```kotlin
import org.uevola.jsonautovalidation.utils.annotations.EnableJsonAutoValidation

@SpringBootApplication
@EnableJsonAutoValidation
class MainApplication

fun main(args: Array<String>) {
	runApplication<ZymAuthServerApplication>(args)
}
```

And add the interceptor to the web configurations:
```kotlin
import org.uevola.jsonautovalidation.web.handlers.JsonSchemaValidationInterceptor

@Configuration
class WebConfig(private val jsonSchemaValidatorHandler: JsonSchemaValidationInterceptor) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(jsonSchemaValidatorHandler)
    }
}
```