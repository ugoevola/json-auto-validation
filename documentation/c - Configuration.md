# Configuration

## Set up
To avoid long loading times, the library uses class paths to fetch the information it needs,
thus avoiding having to search the entire application.

- The json schemas are generated from the dto, and by default it fetches them from `"${java.base.package}.dto"`.
- Control beans are generated from controllers, by default it reads controllers from `"${java.base.package}"`.
- Finally, it reads and/or generates the json schemas in the default resources in the `"json-schemas/"` folder.

You can change this behavior in your application properties as follows:
```properties
json-validation.dto-package-name=com.exemple.utils.dto
json-validation.controllers-package-name=com.exemple.web
json-validation.resources-path=validation/js-schema
```

## Process the validation
Once the validation prerequisites have been defined, you can request automatic
data validation using the **@Validate** annotation, either
- on a controller class: **json-auto-validation** will try to validate all the parameters
  of all the controller's handler methods
- on a controller's handler method: tries to validate all handler method parameters
- on a handler method parameter: to validate a specific parameter :

**Example**

```kotlin
// For every parameter of every UserController methods
@Validate
@RestController
@RequestMapping("users")
class UserController(
  private val userService: UserService
) {

  @GetMapping
  fun getUserByFilter(
    filterDto: FilterDto
  ) = userService.getCarsByFilter(filterDto)

  @GetMapping("/{id}")
  fun getUserById(
    @IsUuid @PathVariable id: String,
    @IsInteger maxAge: Int
  ) = userService.getCarById(id)
}
```
```kotlin
@RestController
@RequestMapping("users")
class UserController(
  private val userService: UserService
) {
  @GetMapping
  fun getUserByFilter(
    filterDto: FilterDto
  ) = userService.getCarsByFilter(filterDto)


  // For getUserById validation only  
  @Validate
  @GetMapping("/{id}")
  fun getUserById(
    @IsUuid @PathVariable id: String,
    @IsInteger maxAge: Int
  ) = userService.getCarById(id)
}
```

```kotlin
@RestController
@RequestMapping("users")
class UserController(
  private val userService: UserService
) {
  
  @GetMapping
  fun getUserByFilter(
    filterDto: FilterDto
  ) = userService.getCarsByFilter(filterDto)

  @GetMapping("/{id}")
  fun getUserById(
    @PathVariable id: String,
    @Validate @IsInteger maxAge: Int // For maxAge validation only  
  ) = userService.getCarById(id)
}
```