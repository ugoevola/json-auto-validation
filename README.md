# Json auto validation
**Json-auto-validation** is a library for automatic validation of incoming
json data in a spring-boot API.

## Documentation

A full documentation is available here : [Documentation](https://github.com/ugoevola/json-auto-validation/blob/main/documentation)


## Description

Based on the automatic generation of json schemas and validation beans using annotations,
its aim is to validate json data before deserialization,
thereby minimizing deserialization errors and lightening the workload
involved in verifying data after deserialization.

## Installation

```xml
<dependency>
    <groupId>io.github.ugoevola</groupId>
    <artifactId>json-auto-validation</artifactId>
    <version>0.2.7</version>
</dependency>
```

```kts
implementation("io.github.ugoevola:json-auto-validation:0.2.7")
```

### Example
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

You can also create a custom Validator that perform more validation treatments before deserialization:
```kotlin
@Component
class FilterDtoValidator(
    private val groupController: GroupController
): JsonSchemaValidator<FilterDto>() {

    override fun validate(json: String) {   
        try {
            super.validate(json)
            val jsonObject = JSONObject(json)
            groupController.getGroupById(jsonObject.get("groupId"))
        } catch (exception: GroupNotFoundException) {
            TODO(deals with GroupNotFoundException)
        } catch (exception: ValidationException) {
            TODO(deals with ValidationException)
        }
    }
}
```

