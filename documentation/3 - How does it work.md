# How does it work

## AOT Compilation

Ahead-of-Time (AOT) compilation in `json-auto-validation` allows the library to **generate JSON schemas and Spring validator beans during the compile phase**, so that all request validation is prepared before runtime, improving performance and reliability.

> [!NOTE]
> Utilisation sur `BeanFactoryInitializationAotProcessor` de [Spring](https://docs.spring.io/spring-framework/reference/core/aot.html) pour la génération des schémas et validateurs.

### Schemas & Validators

#### What is a JSON Schema?
A JSON Schema defines a data model for your JSON input, including property types, formats, lengths, and other constraints that the JSON must comply with.

> citation from [official web site](https://json-schema.org/):
> "JSON Schema is the vocabulary that enables JSON data consistency, validity, and interoperability at scale."

> [!NOTE]
> json-auto-validation uses [Draft 2020-12](https://json-schema.org/draft/2020-12) of the JSON Schema specification (release notes) as a base.

#### How are schemas generated?
`json-auto-validation` generates schemas from your DTO objects annotated with `@JsonValidation` in your API.

- Typically, there is one schema per DTO.
- The library uses [networknt/json-schema-validator](https://github.com/networknt/json-schema-validator) to perform validation

**Example**
```kts
@JsonValidation
class ExampleDto()
```
Each property name defined in the schema is calculated using the `JsonMapper` of [Jackson 3](https://github.com/FasterXML/jackson/wiki/Jackson-Release-3.0) and is based on the property naming strategy configured in the API properties.

**example**
```properties
spring.jackson.property-naming-strategy=SNAKE_CASE
```

> [!NOTE]
> After compiling your application, you can find the generated schemas under the resources folder: `/generated-schemas/`

#### What is a Validator?

A Validator is a Spring Bean created by `json-auto-validation` that validates incoming data for a specific DTO.

- There is one validator per DTO/schema.
- At runtime, each validator uses `networknt/json-schema-validator` to enforce the schema rules.
- Validators are generated based on controller definitions, giving you control over which data to validate.

To validate data, use the `@Validate` annotation on:
- Controller class: validates all data for any method in the controller
- Controller method: validates all data for that method
- Controller parameter: validates only the specific parameter

**Example**
```kts
// on class
@Validate // <--
@RestController
@RequestMapping("example")
class ExampleController() {
  //...
}

// on method
@RestController
@RequestMapping("example")
class ExempleController() {

  @Validate // <--
  @GetMapping
  fun getExamples() {
    //...
  }

}

// on parameter
@RestController
@RequestMapping("example")
class ExempleController() {

  @PostMapping()
  fun createExemples(
    @validate // <--
    @RequestBody
    exampleDto: ExampleDto
  ) {
    //...
  }

}

```


## Runtime

During execution, incoming requests are intercepted before accessing the controller by `JsonSchemaValidationInterceptor`, which:
  1. Identifies the validator corresponding to the expected schema
  2. Validates data of the request by using **jsonschema** and `networknt/json-schema-validator`
  3. Prevents deserialization in case of invalid data

> [!TIP]
> For data that does not have an associated DTO but still requires validation, a default validator `DefaultJsonSchemaValidator` will be used based on the annotations placed directly on the parameter in the controller.

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="../assets/diagram-1-dark.png">
  <source media="(prefers-color-scheme: light)" srcset="../assets/diagram-1-light.png">
</picture>

If validation fails, a `HttpClientErrorException` exception will be thrown with a `statusText` equals to "REQUEST_VALIDATION_KO"

If a technical isue occurs during the validation, a `HttpServerErrorException` will be thrown.
