# 01 · Monolith

Level: junior. Status: not started.

## Change

The baseline and the minimum viable product: a classic layered Spring Boot application with a REST API, one PostgreSQL database and one transaction per request. Its functionality and HTTP API are fixed here — users, notes, note sharing with roles, notifications with a live feed over Server-Sent Events, and full-text search of notes. Only the authentication method changes once, in 11; later stages change the internal structure and the infrastructure around the same contract.

## Why

Every later stage is measured against a working, tested, conventional Spring application. The stage contains only what such an application cannot work without.

## Tools

- Application: Gradle (Kotlin DSL), Spring Boot starters with auto-configuration, Spring MVC, Bean Validation, error responses as `ProblemDetail` (RFC 9457), paginated lists, the live feed on `SseEmitter`; request and response bodies are records mapped by hand.
- Data: Spring Data JPA, PostgreSQL; the schema is created by Spring Boot SQL initialization from `schema.sql`. Search is a PostgreSQL full-text query.
- Security: Spring Security with users and BCrypt password hashes in the database, HTTP Basic for the API, role checks for note sharing.
- Tests: JUnit, Mockito, slice tests, Testcontainers with `@ServiceConnection`.
- Run: `./gradlew bootRun` starts the application and its PostgreSQL through Spring Boot Docker Compose support.

## Proof

- The [acceptance tests](../acceptance-tests) pass against the running application.
- One command starts the application with its database.
