# Json auto validation
**Json-auto-validation** is a library for automatic validation of incoming
json data in a spring-boot API.

## Table of contents

- [Installation](#installation)
- [Description](#description)
- [Preventive measures](#preventive-measures)
  - [Request reader](#request-reader)
  - [Automatic resource generation](#automatic-resource-generation)
  - [Usage of cache](#usage-of-cache) 
- [Usage](#usage)
  - [Enabling](#enabling)
  - [Json Validation Annotations](#json-validation-annotations)
    - [On dto class](#on-dto-class)
    - [On method handler](#on-method-handler)
    - [Non-standard annotations](#non-standard-annotations)
  - [Validators](#validators)
     - [Overriding a Validator](#overriding-a-Validator)
  - [Configuration](#configuration)

## Installation

> In coming...

## Description

Based on the automatic generation of json schemas and validation beans using annotations,
its aim is to validate json data before deserialization,
thereby minimizing deserialization errors and lightening the workload
involved in verifying data after deserialization.

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

will produce an error (non-exhaustive):
- `GET /api/v1/cars?brand=Ford`
- `GET /api/v1/cars?brand=Audi&nbDoors=one`
- `GET /api/v1/cars?brand=Audi&nbDoors=42`
- `GET /api/v1/cars?brand=Peugeot&nbDoors=5`
- `GET /api/v1/car/84`
- `GET /api/v1/car/7856486875`

will succeed (non-exhaustive):<br>
- `GET /api/v1/cars?brand=Ford&nbDoors=5`
- `GET /api/v1/car/4d9199af-decf-42d4-8f14-d9e6fc94729c`

## Preventive Measures

Before installing a bookstore, it's always a good idea to understand
what it can do for your project. As far as **Json-auto-validation** here are a few things
you need to know before setting out to install it.

### Request reader
In order to validate the request before it is deserialized,
it is necessary to encapsulate the Body in an HttpServletRequestWrapper
used in a RequestFilter.
Therefore, the body can be read multiple times, at least one time for the validation,
and one time for the deserialization.

### Automatic resource generation

This library is based on automatically generated Beans and schema files:
- dto json schemas (at application launch)
- validation Beans for each of these dto (at application launch)

### Usage of cache
To limit the time it takes to validate a request, certain information can be cached.
When you want to validate basic parameters directly on your endpoints,
**Json-auto-validation** will automatically generate the corresponding schematics.
Since these schemas will not change from one call to the next,
they will be stored in cache.
To avoid excessive cache usage, use DTO classes as much as possible for json validation.
Schematics will be generated automatically when the application is launched.

The cache is used from the spring cache library and has been activated
with the org.springframework.cache.annotation.EnableCaching annotation.

## Usage
### Enabling
To enable **Json-auto-validator**, you just have to annotate a Bean class with @EnableJsonAutoValidation annotation as follows:

```kotlin
@SpringBootApplication
@EnableJsonAutoValidation
class MainApplication

fun main(args: Array<String>) {
	runApplication<ZymAuthServerApplication>(*args)
}
```
### Json Validation Annotations
#### On dto class

To validate a DTO, you must indicate to the library that it is a
DTO to be validated using the @JsonValidation annotation.
Then you can then use all @IsJsonValidation annotations on
the various fields of your DTO.

One allows the library to retrieve the DTOs to be validated
and the other to generate the json schemas.

example : 

```kotlin
@JsonValidation
class UserDto(
    @field:IsUudi
    @field:IsRequired
    val id: String,
    
    @field:IsEmail
    @field:IsRequired
    val email: String,

    @field:IsUudi
    @field:IsRequired
    val groupId: String
)
```

Here is an exhaustive list of existing annotations with their appropriate json schema :
> In coming...

#### On method handler


#### Non-standard annotations

- **IsEnum**

The annotation will automatically detect the enum type of the property;
if it's not an enum, nothing will happen.
You can also use the com.fasterxml.jackson.annotation.JsonValue annotation to bind the value.

```kotlin
enum class GenderEnum(@get:JsonValue val value: String) {
    MR("mr"),
    MS("ms")
}

@JsonValidation
class UserDto(
  @field:IsEnum
  val friend: GenderEnum,
)
```

- **@IsNested**

The IsNested annotation can be used to refer to a child class.

example :

```kotlin
@JsonValidation
class UserDto(
    @field:IsNested
    @field:IsRequired
    val friend: FriendDto,
)

class FriendDto(
  @field:IsEmail
  @field:IsRequired
  val email: String,
  
  @field:IsString(minLength = 3, maxLenght = 50)
  @field:IsRequired
  val fullName: String,
)
```

> **Note**: @JsonValidation generates a schema for a given DTO.
> Unless you need this schema for another service,
> you don't need to annotate the subclasses used in your DTO with the
> @JsonValidation annotation.


- **@IsJsonSchema**

This annotation can be used to refer to a customized validation diagram you've created yourself.
This schema must be
- be stored in the correct resource directory (see configuration)
- and must only contain data relating to a single property
- the file name must finish by **.schema.json**

Example:

`/resources/json-schemas/GivenName.schema.json`
```json
{
    "description": "User's first name",
    "type": "string",
    "maxLength": 64,
    "optional": true
}
```

```kotlin
@JsonValidation
class UserDto(
    @field:IsJsonSchema("GivenName")
    val givenName: FriendDto,
)
```

### Process the validation
Once the validation prerequisites have been defined, you can request automatic
data validation using the @Validate annotation, either
- on a controller class: ... will try to validate all the parameters
of all the controller's handler methods
- on a controller's handler method: tries to validate all handler method parameters
- on a handler method parameter: to validate a specific parameter : 

example : 
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

### Validators

#### Overriding a Validator
**Json-auto-validator** let you the possibility to create your own validator for a specific DTO.
As a result, there will be no automatically generated validator for this DTO, and yours will be used:

```kotlin
@Component
class UserDtoValidator(
    private val groupController: GroupController
): JsonSchemaValidator<UserDto>() {

    override fun validate(json: String) {   
        try {
            super.validate(json)
            val jsonObject = JSONObject(json)
            groupController.getGroupById(jsonObject.get("groupId"))
        } catch (exception: GroupNotFoundException) {
            TODO(deals with GroupNotFoundException)
        } catch (exception: HttpClientErrorException) {
            TODO(deals with HttpClientErrorException)
        }
    }
}
```


That way, the library will add **Json-auto-validator** Beans (Validators & Handlers) to your API spring environment.

After you must add a json validation schema that refer to a DTO class by naming the same way in your json validation schema folder in the resources (`"json-schemas/"` by default, cf. configuration to change it).

And the things will work by themselves.

### Configuration
To avoid long loading times, **json-auto-validation** will focus on
one package for DTO validation, by default `"${java.package}.dto"`.
It will also fetch and store the json validation schemas
in the `"json-schemas/"` folder in the resources.

You can change this behavior in your application properties as follows:
```properties
json-validation.dto-package-name=${java.package}.utils.dto
json-validation.resources-path=validation/js-schema
```



