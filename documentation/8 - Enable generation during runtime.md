# Enable Generation During Runtime

By default, `json-auto-validation` generates JSON schemas and validator beans during **AOT compilation** (Ahead-of-Time), which is the recommended and most performant approach for production applications.

However, there are specific cases where AOT compilation is not available and schema generation must happen at **application startup**, at runtime.

---

## When to use it

### Integration tests

When running tests (e.g. with Cucumber, Spring Boot Test), AOT compilation is not triggered by default. Launching tests directly from an IDE like IntelliJ will bypass Gradle entirely, meaning schemas are never generated and validator beans are never registered.

Enabling runtime generation is the recommended solution for this use case.

### Spring Boot APIs without AOT support

Some Spring Boot setups do not support or enable AOT compilation — for example, projects that do not target GraalVM native image and have not configured the `processAot` task. In these cases, runtime generation allows the library to function correctly without requiring any build configuration changes.

---

## How to enable it

Add the following property to your configuration (e.g. `application.properties` or `application-test.properties`):

```properties
json-validation.runtime-generation=true
```

When this property is set to `true`:

- The AOT processor is **skipped** entirely during compilation
- Schemas are generated at **application startup** via an `ApplicationListener`
- Validator beans are registered dynamically into the Spring context via `BeanDefinitionRegistry`

---

## ⚠️ Why it is discouraged in production

Runtime generation should be considered a **fallback**, not the default. Here is why:

- **Startup overhead** — schema generation and bean registration happen at startup, increasing application boot time
- **No compile-time guarantees** — errors that would be caught during AOT compilation (missing schemas, invalid configurations) are deferred to runtime
- **Not compatible with GraalVM native image** — native executables require all beans and resources to be known at compile time; runtime generation cannot fulfill this requirement
- **AOT is the intended execution path** — the library is designed and optimized around AOT compilation; runtime mode is a convenience feature, not a first-class execution model

For any production deployment, especially native image builds, always rely on AOT compilation and ensure `processAot` runs before packaging.