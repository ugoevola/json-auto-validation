# Customize an error message

## Default
The library allows you to customize the error messages returned when validation fails.

By default, a predefined error message exists for each type of validation.
Examples:

- Type error → `The field token must be (a/an) @{type}.`
- Maximum value error → `The field token must be greater than or equal to @{maximum}.`

You can also define a global error message that overrides all other validation messages and becomes the only message returned in case of validation failure.

To override a specific error message, specify a custom message when decorating the property with a validation annotation.

> [!TIP]
> It is possible to reference other values defined in the annotation or the name of the field itself by using the pattern `@{key}`.

> [!CAUTION]
> Currently, it's not possible to access at the property value.

```kotlin
@JsonValidation
class CredentialDto(
  // override specific error message
  @Required
  @IsEnum(formatErrorMessage = "The login must not contain any numbers.")
  val login: String,
  
  // override generic error message
  @Required
  @IsInteger(errorMessage = "The password is invalid.")
  val password: String,
)
```