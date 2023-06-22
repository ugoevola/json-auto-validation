# Validators

## Overriding a Validator
**Json-auto-validation** let you the possibility to create your own validator for a specific DTO.
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


That way, the library will add **Json-auto-validation** Beans (Validators & Handlers) to your API spring environment.

After you must add a json validation schema that refer to a DTO class by naming the same way in your json validation schema folder in the resources (`"json-schemas/"` by default, cf. configuration to change it).

And the things will work by themselves.