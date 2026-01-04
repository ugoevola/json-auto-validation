# Validators

## Overriding a Validator


Basically, when a request linked to a DTO is being validated, it passes through a validator that simply checks whether the DTO complies with its generated schema.

However,`json-auto-validation` gives you the ability to create your own custom validator for a specific DTO to perform additional or more advanced validation logic.

To do so, create a Spring Bean that extends the abstract class JsonSchemaValidator, passing the target DTO class to its constructor. This tells the library that your validator is associated with that specific DTO.

> [!IMPORTANT]
> There are now be multiple validator beans registered for the same DTO.
To ensure that your custom validator takes priority, you must inform the `json-auto-validation` factory that yours takes priority (the component responsible for managing validation order).

Override the getOrdered() method in your validator class and return a value lower than `999` to give it higher priority.

**Example**
```kotlin
@Component
class UserDtoValidator(
    private val groupController: GroupController
): JsonSchemaValidator(UserDto::class.java) {

    override fun getOrdered() = 0

    override fun validate(json: JsonNode) {   
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
