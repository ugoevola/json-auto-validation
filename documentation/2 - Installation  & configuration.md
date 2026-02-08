# Installation & configuration

## Installation
### Maven

```xml
<dependency>
    <groupId>io.github.ugoevola</groupId>
    <artifactId>json-auto-validation</artifactId>
    <version>1.0.1</version>
</dependency>
```

### Gradle
```kts
implementation("io.github.ugoevola:json-auto-validation:1.0.1")
```

## Configuration

### Set up

To avoid long loading times during compilation, the library uses class paths to fetch the information it needs,
thus avoiding having to search the entire application.

- The JSON schemas are generated from the dto, and by default it fetches them from `"${java.base.package}.dto"`.
- Control beans are generated from controllers, by default, it reads controllers from `"${java.base.package}"`.

You can change this behavior in your application properties as follows:
```properties
json-validation.dto-package-name=com.exemple.utils.dto
json-validation.controllers-package-name=com.exemple.web
```


### Enabling
To enable `json-auto-validation`, you have to annotate a Bean class with `@EnableJsonAutoValidation` annotation as follows:

```kotlin
import org.uevola.jsonautovalidation.annotations.EnableJsonAutoValidation

@SpringBootApplication
@EnableJsonAutoValidation
class MainApplication

fun main(args: Array<String>) {
	runApplication<ZymAuthServerApplication>(args)
}
```

And add the interceptor to the web configurations:
```kotlin
import org.uevola.jsonautovalidation.runtime.web.JsonSchemaValidationInterceptor

@Configuration
class WebConfig(private val jsonSchemaValidatorHandler: JsonSchemaValidationInterceptor) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(jsonSchemaValidatorHandler)
    }
}
```