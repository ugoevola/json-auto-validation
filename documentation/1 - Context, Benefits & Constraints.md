# Context, Benefits & Constraints

## Context
### Typically
In a Springboot REST API with a layered architecture, the controller layer is responsible for validating incoming data.  The developer fills in the various entry points accessible in the API with their associated http method, as well as the types of data read in the request.

The deserialization process, which is transparent to the developer, transforms the request data into a java object that can be used in the code. A large part of the data validation is done at this point: if the data is not of the expected type, deserialization fails, and the request is rejected with a `400 BadRequest` return code.

We can then add further checks on the data, for example, to check that a character string respects a certain regular expression. To do this, a common practice is to use the [spring-boot-starter-validation]() starter, proposed by Spring and based on the use of annotations.

### Problem
The fact that data validation takes place in two stages (deserialization, then bean validation) can in some circumstances be challenging to supervise.

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="../assets/explain-bean-val-dark.png">
  <source media="(prefers-color-scheme: light)" srcset="../assets/explain-bean-val-light.png">
  <img alt="explain-bean-validation" />
</picture>

Let's suppose we have an API in which we want to have an error specifically linked to the validation of a certain piece of data. The idea would be to create an exception handler, which would catch exceptions linked to deserialization and those linked to validation. We would then need to check whether the exception in question is linked to the data in question and generate the desired error.

In SpringBoot, the default JSON deserializer is Jackson. When a deserialization fails, Jackson can return various types of Exceptions. Given that not all the data received is JSON, you can imagine the number of exceptions you have to handle in your exception handler. It can quickly become tedious.

### Solution
The aim of this library is to carry out a thorough check of all types of data before deserialization in record time and to return only one type of exception. As a result, there will be no more deserialization errors and no need to check the beans.

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="../assets/explain-json-val-dark.png">
  <source media="(prefers-color-scheme: light)" srcset="../assets/explain-json-val-light.png">
  <img alt="explain-json-validation" />
</picture>

## Benefits

### Early data validation
Data is validated before deserialization, preventing Jackson mapping exceptions and other runtime errors. This ensures that controllers always receive pre-validated objects, improving reliability and consistency.

### Reduction in manual validation code
This approach removes the need for manual validation logic in controllers or services. It reduces code complexity, eases maintenance, and minimizes the amount of business validation code to manage.

### Faster error detection
By catching validation errors early —before deserialization— responses to clients are faster and more consistent. This improves debugging and monitoring by producing standardized, predictable validation error messages.


### Performance thanks to AOT generation
Schemas and beans are generated at compile time (AOT), eliminating runtime overhead. This makes the solution highly efficient and perfectly optimized for Spring Boot 3 and GraalVM environments.

## Constraints

### Compatibility limited to Spring Boot 3+
AOT generation makes the library incompatible with Spring Boot 2.x. This restricts its use in older projects and legacy environments.

### Partial validation of the HTTP model
At this stage, the library only validates request JSON bodies and URL parameters (`@PathVariable`, `@RequestParam`). Other elements such as headers, multipart/form-data, and complex query types are not yet supported.

### Less flexible than manual validations
When validation depends on dynamic contexts—such as database checks or conditional business logic—it must still be handled manually. Validations remain static and are defined at compile time.

### External dependency on networknt/json-schema-validator
Validation quality and performance partly rely on this third-party library. This can introduce subtle behavioral differences between versions.

### Increased complexity during the build phase
Integrating with the AOT cycle adds an extra build step. Misconfiguration can lead to compile-time errors that are often harder to diagnose and fix.