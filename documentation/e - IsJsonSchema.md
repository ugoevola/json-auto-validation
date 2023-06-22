# @IsJsonSchema

This annotation can be used to refer to a customized validation diagram you've created yourself.
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