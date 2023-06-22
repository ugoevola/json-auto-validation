# @IsEnum

The annotation will automatically detect the enum type of the property;
if it's not an enum, nothing will happen.
You can also use the com.fasterxml.jackson.annotation.JsonValue annotation to bind the value.

**Example**

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