# 01 · Monolith

Level: junior. Status: not started.

## Change

The baseline and the minimum viable product: a classic layered application with a REST API, one database and one transaction per request. Its functionality and HTTP API are fixed here — users, notes, note sharing with roles, notifications with a live feed over Server-Sent Events, and full-text search of notes. Only the authentication method changes once, in 02; later stages change the internal structure and the infrastructure around the same contract.

## Why

Every later stage is measured against a working, conventional Spring application that is ready for deployment. The stage is kept small on purpose so that it reaches this point quickly.

## Tools

- Application: Gradle (Kotlin DSL), Spring Boot starters with auto-configuration, Spring MVC, Bean Validation, error responses as `ProblemDetail` (RFC 9457), paginated lists, MapStruct for mapping between entities and request/response bodies, springdoc-openapi (Swagger UI), Actuator. The live feed uses `SseEmitter`.
- Data: Spring Data JPA, PostgreSQL, Flyway. Search is a PostgreSQL full-text query.
- Security: Spring Security with users and BCrypt password hashes in the application database, HTTP Basic for the API, role checks for note sharing.
- Tests: JUnit, Mockito, slice tests, Testcontainers with `@ServiceConnection`, coverage reports with JaCoCo.
- Run: Docker Compose — Spring Boot Docker Compose support for development, a Compose file with the application image for running the whole stage. The same Compose file with TLS is meant for Amazon EC2.
- CI/CD: GitHub Actions builds and tests, builds the image, starts the stage in Docker Compose on the runner and runs the acceptance tests against it. The deployment job — authentication to AWS through OIDC, image to Amazon ECR, deployment to Amazon EC2 — is written but stays switched off until the job search starts.

## Proof

- The [acceptance tests](../acceptance-tests) pass against the stage running in Docker Compose.
- One command starts the whole stage locally; the deployment job needs only to be switched on.
