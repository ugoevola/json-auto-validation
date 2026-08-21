# JsonValidationAware

## Context

By default, `json-auto-validation` infers the validation schema from the **type of the controller parameter**. This works well in the common case where the controller parameter directly reflects the shape of the incoming HTTP request.

However, when a `HandlerMethodArgumentResolver` is involved, this assumption breaks down. The resolver intercepts the request **before** it reaches the controller and transforms the data into a different type. The controller parameter type no longer matches the actual shape of the request.

```
HTTP Request (shape: RequestDto)
        │
        ▼
HandlerMethodArgumentResolver   ← transforms to SomeOtherDto
        │
        ▼
Controller expects: SomeOtherDto   ← library infers schema from this
```

Without any indication, the library would:
- attempt to find a schema for `SomeOtherDto` — which likely does not exist
- never validate against the `RequestDto` schema — even though it was generated

## Solution

The `JsonValidationAware` interface allows a `HandlerMethodArgumentResolver` to **declare the DTO type it actually consumes from the HTTP request**, so the library can resolve the correct schema.

```kotlin
interface JsonValidationAware {
    fun getRequestDtoType(): KClass<*>
}
```

## Usage

Implement `JsonValidationAware` alongside `HandlerMethodArgumentResolver` on your resolver and override `getRequestDtoType()` to return the DTO type consumed from the request.

```kotlin
@Component
class SomeOtherDtoResolver(
    private val buildContextUseCase: BuildSomeOtherDtoUseCase,
) : HandlerMethodArgumentResolver, JsonValidationAware {

    override fun getRequestDtoType() = RequestDto::class

    override fun supportsParameter(parameter: MethodParameter) =
        parameter.parameterType == SomeOtherDto::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): SomeOtherDto = buildContextUseCase()
}
```

The controller remains completely unchanged:

```kotlin
@RequestMapping
fun requestForSomething(
    @Validate context: SomeOtherDto
) {
    // your business logic
}
```

At runtime, the library will:
1. Detect that a `JsonValidationAware` resolver handles the `SomeOtherDto` parameter
2. Use `RequestDto` as the validation target instead
3. Validate the raw request data against the `RequestDto` schema

> [!IMPORTANT]
> The DTO declared in `getRequestDtoType()` must be annotated with `@JsonValidation` so that its schema and validator bean are generated at compile time.

```kotlin
@JsonValidation
class RequestDto(
    // ...
)
```

> [!NOTE]
> The responsibility of declaring the consumed type sits in the resolver itself — the only component that knows about the transformation. This keeps the controller clean and free of any validation infrastructure concerns.