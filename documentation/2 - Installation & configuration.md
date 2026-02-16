# Installation & configuration

## Installation
### Maven

```xml
<dependency>
    <groupId>io.github.ugoevola</groupId>
    <artifactId>json-auto-validation</artifactId>
    <version>1.1.1</version>
</dependency>
```

### Gradle
```kts
implementation("io.github.ugoevola:json-auto-validation:1.1.1")
```

## Configuration

### Enabling
To enable `json-auto-validation`, you have to annotate a Bean class with `@EnableJsonAutoValidation` annotation as follows:

```kotlin
@SpringBootApplication
@EnableJsonAutoValidation
class MainApplication

fun main(args: Array<String>) {
	runApplication<ZymAuthServerApplication>(args)
}
```

### Set up

#### Improving compilation times

To avoid long loading times during compilation, the library uses class paths to fetch the information it needs,
thus avoiding having to search the entire application.

- The JSON schemas are generated from the dto, and by default it fetches them from `"${java.base.package}.dto"`.
- Control beans are generated from controllers. By default, it reads controllers from `"${java.base.package}"`.

You can change this behavior in your application properties as follows:
```properties
json-validation.dto-package-name=com.exemple.utils.dto
json-validation.controllers-package-name=com.exemple.web
```
#### Application web stack

The librairie is supported by both servlet and reactive webflux applications.
Depending on your API, reactive webflux, or servlet, set this property.
If it is not specified, `json-auto-validation` will be configured to work on a servlet API.

```properties
json-validation.web-stack=servlet # or webflux
```

#### Security

To prevent DoS attacks, the library limits the maximum size of a JSON body to 2 MB. You can change this threshold using this property.

```properties
json-validation.max-json-size=2 # in MB
```
