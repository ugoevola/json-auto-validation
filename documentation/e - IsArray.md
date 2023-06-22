# @IsArray

This annotation is used on collections. It does not yet support nested collections.

**Example**

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