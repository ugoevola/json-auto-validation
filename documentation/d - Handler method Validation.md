# Handler Method Validation

If you don't want to create a DTO to retrieve your arguments or validate your pathParam,
you can use decorators directly on your controller's handler methods like this:

```kotlin
@Validate
@RestController
@RequestMapping("cars")
class CarController(
    private val carService: CarService
) {

    @GetMapping("/{id}")
    fun getCarById(
        @IsUuid @PathVariable id: String,
        @IsInteger(maximum = 5) @QueryParam nbDoors: Integer
    ) = cartService.getCarById(id)

}
```