# @IsNested

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

> **Note**: @JsonValidation generates a schema for a given DTO.
> Unless you need this schema for another service,
> you don't need to annotate the subclasses used in your DTO with the
> @JsonValidation annotation.