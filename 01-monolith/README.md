# 01 · Monolith

Level: junior. Status: in progress.

## Change

The baseline and the minimum viable product: a classic layered Spring Boot application with a REST API, one PostgreSQL database and one transaction per request. Its functionality and HTTP API are fixed here — users, notes, note sharing with roles, notifications with a live feed over Server-Sent Events, and full-text search of notes. Only the authentication method changes once, in 11; later stages change the internal structure and the infrastructure around the same contract.

## Why

Every later stage is measured against a working, tested, conventional Spring application. The stage contains only what such an application cannot work without.

## Tools

- Application: Gradle (Groovy DSL), Spring Boot starters with auto-configuration, Spring MVC, Bean Validation, error responses as `ProblemDetail` (RFC 9457), paginated lists, the live feed on `SseEmitter`; request and response bodies are records mapped by hand.
- Data: Spring Data JPA, PostgreSQL; the schema is created by Spring Boot SQL initialization from `schema.sql`. Search is a PostgreSQL full-text query.
- Open Session in View: `spring.jpa.open-in-view` is set explicitly, because Spring Boot logs a warning at startup while the property keeps its default. With `true`, the default, the JPA `EntityManager` stays bound to the thread for the whole request: lazy associations are loaded outside the service transaction — in the controller and while the response is serialised — and a database connection, once taken, is held until the response is written. With `false`, the `EntityManager` lives only inside a transaction, and a lazy association accessed outside it throws `LazyInitializationException`. The stage uses `false`: services load everything a response needs inside their transactions and map it to records, so no query runs outside the service layer.
- Security: Spring Security with users and BCrypt password hashes in the database, HTTP Basic for the API, role checks for note sharing.
- Tests: JUnit, Mockito, slice tests, Testcontainers with `@ServiceConnection`.
- Run: `./gradlew bootRun` starts the application and its PostgreSQL through Spring Boot Docker Compose support. Spring Boot looks for `compose.yaml` in the working directory, so the application is started with the stage directory as its working directory, also from an IDE.

## Known warnings

The build, the tests and the running application emit the warnings below, ordered by how critical they are. They are resolved in that order as the application is developed; a resolved warning is removed from the list.

1. Gradle, on every build with `--warning-mode all`: the Java 25 toolchain was installed by auto-provisioning, but the build declares no toolchain repository. Gradle has deprecated this behaviour and fails with an error from Gradle 10; on a clean machine, such as a CI runner, the toolchain is not found.
2. Spring Boot, at startup and in the tests: `UserDetailsServiceAutoConfiguration` logs a generated password, because the application has no user configuration of its own yet. The warning disappears with the security configuration of the stage — users and BCrypt password hashes in the database.
3. Mockito and the JVM, in the tests: Mockito attaches itself as an agent to the running JVM, the JVM reports the dynamic agent loading, and class data sharing is limited to boot loader classes. These warnings come from the default behaviour of the tools and are accepted; the reason and the configuration that would remove them are recorded in [ADR 0001](../docs/adr/0001-evolution-in-stages.md).

## Proof

- The [acceptance tests](../acceptance-tests) pass against the running application.
- One command starts the application with its database.
