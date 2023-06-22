# DTO

To validate a DTO, you must indicate to the library that it is a
DTO to be validated using the **@JsonValidation** annotation.
Then you can then use all IsJsonValidation annotations on
the various fields of your DTO.

One allows the library to retrieve the DTOs to be validated
and the other to generate the json schemas.

**Example**

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