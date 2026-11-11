# 01 · Monolith

Level: junior. Status: not started.

## Change

The baseline: a classic layered application with one database and one transaction per request. Its functionality and HTTP API are fixed here and stay the same in every later stage — users, notes, note sharing with roles, notifications with a live feed over Server-Sent Events, and full-text search of notes. Later stages change only the internal structure.

## Why

Every later stage is measured against a working, deployed, conventional Spring application.

## Tools

- Application: Gradle (Kotlin DSL), Spring Boot starters with auto-configuration, Spring MVC, Thymeleaf, Bean Validation, MapStruct for mapping between entities and request/response bodies, springdoc-openapi (Swagger UI), Actuator. The live feed uses `SseEmitter`.
- Data: Spring Data JPA, PostgreSQL, Flyway. Search is a plain PostgreSQL query.
- Security: the application is an OAuth2 Client for login and a Resource Server for the API. Tokens are issued by `auth-server`, a separate Spring Boot application on Spring Authorization Server.
- Tests: JUnit, Mockito, slice tests, Testcontainers with `@ServiceConnection`.
- Run and delivery: Spring Boot Docker Compose support locally. GitHub Actions authenticates to AWS through OIDC, pushes the images to Amazon ECR and deploys them to Amazon EC2 with Docker Compose and TLS. SonarQube Cloud checks quality.

## Proof

- The [acceptance tests](../acceptance-tests) pass against the deployed stage.
- The live instance on AWS is reachable over HTTPS.
