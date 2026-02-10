# Json validation annotations

## Principle
Each annotation is based on a combination of [(type, format)](https://json-schema.org/understanding-json-schema/reference/type#built-in-formats) of jsonschema from **draft 2020-12**. The goal is to stay as close as possible to the official JSON Schema specification to make the use of annotations simple and intuitive.

## Decorate data
### Decorate a DTO
To validate a DTO, you must decorate it with the `@JsonValidation` annotation, then place the desired annotations on the DTO properties.

```kotlin
@JsonValidation
class FilterDto(
  @Required
  @IsEnum(errorMessage = "invalid brand")
  val brand: BrandEnum,
  
  @Required
  @IsInteger(minimum = 3, maximum=5)
  val nbDoors: Int,
  
  @IsBool
  val gps: Boolean?,
    
  val groupId: String  
)
```

### Decorate a controller parameter
If you don't want to create a DTO to retrieve data, you can use decorators directly on your controller's handler methods.

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

## Automatic detection on DTO
When a DTO requires validation (annotated with `@JsonValidation`), some types are detected automatically when generating the DTO's JSON schema. There is therefore no need to decorate it.

```kts
@JsonValidation
class FilterDto(
  val brand: BrandEnum,
  val gps: Boolean?,
  val groupId: String  
)
```
is equivalent to

```kts
@JsonValidation
class FilterDto(
  @IsEnum val brand: BrandEnum,
  @IsBool val gps: Boolean?,
  @IsString val groupId: String  
)
```
See the [list of annotations](#annotation-list) for a complete list of automatically detected annotations.

## Special annotations

### IsArray
This annotation is used on collections, and we **must** provide the expected type.

```kotlin
@JsonValidation
class MyDto(
  @field:IsArray(type = JsonTypeEnum.STRING)
  val stringList: List<String>,
  @field:IsArray(type = JsonTypeEnum.INTEGER)
  val integerList: List<String>,
  @field:IsArray(type = JsonTypeEnum.BOOLEAN)
  val integerList: List<String>,
)
```

### IsJsonSchema
This annotation can be used to refer to a customized validation schema you've created yourself.
This schema must be
- be stored in the correct resource directory (see configuration)
- and must only contain data relating to a single property
- the file name must finish by **.schema.json**

**Example**

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

### IsNested
The IsNested annotation can be used to refer to a child class.

**Example**

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

> [!TIP]
> `@JsonValidation` generates a schema for a given DTO. Unless you need this schema for another service, you don't need to annotate the subclasses used in your DTO with the `@JsonValidation` annotation.

### IsStringInteger & IsStringNumber
The annotations `@IsStringInteger` and `@IsStringNumber` are not part of the official JSON Schema **Draft 2020–12** specification. They were added using the networknt/json-schema-validator library to simplify the validation of string values containing numeric data. These annotations behave as if the property were of type `integer` or `number`.


**example**
```kotlin
// DTO
@JsonValidation
class CartDto(
    @IsStringInteger(minimum= 50, maximum = 100)
    val myProp: String,
)
```

> Generated JSON Schema
```json
{
    "title": "package.dto.CartDto",
    "type": "object",
    "required": [],
    "properties": {
        "string-integer": {
            "minimum": 50,
            "maximum": 100,
            ...
        }
    }
}
```
> given object > myProp is to high
```json
{
  "myProp ": "789"
}
```
> given object > myProp is to low
```json
{
  "myProp ": "10"
}
```
> given object > myProp is not a number
```json
{
  "myProp ": "qwerty"
}
```
> given valid object > OK
```json
{
  "myProp ": "75"
}
```

## Annotation list

Below is a complete list of validation annotations that can be placed on a property.

- 🟢 **Support** the automatic detection 
- 🔴 **Does not support** the automatic detection 

|         Annotation         | Automatic<br/>type detection |
|:--------------------------:|:----------------------------:|
|        **@IsArray**        |             🔴              |
|        **@IsBool**         |             🟢              |
|        **@IsDate**         |             🔴              |
|      **@IsDateTime**       |             🔴              |
|        **@IsEmail**        |             🔴              |
|        **@IsEnum**         |             🟢              |
|       **@IsEqualTo**       |             🔴              |
|      **@IsHostName**       |             🔴              |
|      **@IsIdnEmail**       |             🔴              |
|    **@IsIdnHostEmail**     |             🔴              |
|       **@IsInteger**       |             🟢              |
|        **@IsIpv4**         |             🔴              |
|        **@IsIpv6**         |             🔴              |
|         **@IsIri**         |             🔴              |
|     **@IsJsonPointer**     |             🔴              |
|     **@IsJsonSchema**      |             🔴              |
|       **@IsNested**        |             🔴              |
|       **@IsNotNull**       |             🔴              |
|      **@IsNotEmpty**       |             🔴              |
|       **@IsNumber**        |             🟢              |
|     **@IsPhoneNumber**     |             🔴              |
|        **@IsRegex**        |             🔴              |
| **@IsRelativeJsonPointer** |             🔴              |
|      **@IsRequired**       |             🔴              |
|       **@IsString**        |             🟢              |
|     **@IsStringBool**      |             🔴              |
|    **@IsStringInteger**    |             🔴              |
|    **@IsStringNumber**     |             🔴              |
|        **@IsTime**         |             🔴              |
|         **@IsUri**         |             🔴              |
|    **@IsUriReference**     |             🔴              |
|     **@IsUriTemplate**     |             🔴              |
|        **@IsUuid**         |             🔴              |
|       **@IsValues**        |             🔴              |
