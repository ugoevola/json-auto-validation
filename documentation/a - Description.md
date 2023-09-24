# Description

Based on the automatic generation of [json schemas](https://json-schema.org/draft/2020-12/json-schema-validation.html) and validation beans using annotations,
its aim is to validate json data before deserialization,
thereby minimizing deserialization errors and lightening the workload
involved in verifying data after deserialization.

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
  val brand: BrandEnum,
  
  @field:Required
  @field:IsInteger(minimum = 3, maximum=5)
  val nbDoors: Int,
  
  val gps: Boolean?
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
- `GET /api/v1/cars?gps=12`
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
