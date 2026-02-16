## Description

<p align="justify"> 
Based on the automatic generation of json schemas and validation beans using annotations,
its aim is to validate json data before deserialization,
thereby minimizing deserialization errors and lightening the workload
involved in verifying data after deserialization.
</p>

## Good to know

The library operates at two key moments in the application lifecycle:
1. AOT Compilation > Scanning of annotated controller + DTOs, and automatic generation of JSON schemas + validation beans
2. Runtime > Interception of incoming requests and validation via the appropriate schema before deserialization

JSON data validation is based on [networknt/json-schema-validator](https://github.com/networknt/json-schema-validator).

## Spring Boot compatibility

Beans and schemas are generated during the AOT (Ahead-Of-Time) phase.

> [!WARNING]
> This version is not compatible with Spring Boot versions lower than 3.0.

| Library version | Minimum version of Spring Boot | maintenance  |
|-----------------|--------------------------------| ------------ |
| `≥ 1.*.*`       | 3.0+ (AOT mandatory)           |    🟢       |   
| `< 1.*.*`       | 2.x                            |    🔴       |

- 🟢 Active Maintenance & Development
- 🟡 Bug Fix Only / Limited Maintenance
- 🔴 No Longer Maintained / Deprecated


## Installation

```xml
<dependency>
    <groupId>io.github.ugoevola</groupId>
    <artifactId>json-auto-validation</artifactId>
    <version>1.1.1</version>
</dependency>
```

```kts
implementation("io.github.ugoevola:json-auto-validation:1.1.1")
```

## How does it work?

1. You declare your DTOs using the `@JsonValidation` annotations and your endpoints to be validated with `@Validate`.
2. AOT compilation phase → the library scans the annotated DTOs and automatically generates the corresponding JSON schemas.
3. AOT compilation phase → The library then identifies the controllers to be validated and generates Spring components associated with each DTO referenced in the relevant endpoints.
4. Runtime → During execution, incoming requests are intercepted before accessing the controller by `JsonSchemaValidationInterceptor`, which:
    - Identifies the validator corresponding to the expected schema
    - Validates data of the request
    - Prevents deserialization in case of invalid data

## Example
  Let's take an example. Consider an end point on our API as it is:

  ```kotlin
  @RestController
  @RequestMapping("cars")
  class CarController(
    private val carService: CarService
  ) {

    @GetMapping
    fun getCarByFilter(
      @Validate filterDto: FilterDto
    ) = carService.getCarsByFilter(filterDto)

    @GetMapping("/{id}")
    fun getCarById(
      @Validate @IsUuid @PathVariable id: String
    ) = cartService.getCarById(id)

}
```

The FilterDto class:
```kotlin
@JsonValidation
class FilterDto(
  @field:Required
  @field:IsEnum
  val brand: BrandEnum,
  
  @field:Required
  @field:IsInteger(minimum = 3, maximum=5)
  val nbDoors: Int,
  
  @field:IsBool
  val gps: Boolean?,
    
  val groupId: String  
)
```

The BrandEnum class:
```kotlin
enum class BrandEnum {
    Audi,
    Ford,
    Mercedes,
    Volkswagen
}
```

Therefore, tells json-auto-validation to intercept and verify
requests before they are processed by the controller handler.

non-exhaustive list of requests that will be in error:
- `GET /api/v1/cars?brand=Ford`
- `GET /api/v1/cars?brand=Audi&nbDoors=one`
- `GET /api/v1/cars?brand=Audi&nbDoors=42`
- `GET /api/v1/cars?brand=Peugeot&nbDoors=5`
- `GET /api/v1/car/84`
- `GET /api/v1/car/7856486875`

non-exhaustive list of requests that will succeed:
- `GET /api/v1/cars?brand=Ford&nbDoors=5`
- `GET /api/v1/car/4d9199af-decf-42d4-8f14-d9e6fc94729c`

