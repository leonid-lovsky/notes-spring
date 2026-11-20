# 24 · MySQL

Level: junior. Status: not started.

## Change

The application runs on PostgreSQL or MySQL, selected by profile. Hibernate chooses the dialect, so the services and repositories do not change; each vendor has its own chain of migrations with the same version numbers.

## Why

In 23 the application is bound to PostgreSQL, while many companies run MySQL.

## Tools

- Spring Data JPA on both vendors; vendor-specific Flyway locations through the `{vendor}` placeholder of `spring.flyway.locations`, checked against the current Spring Boot reference when the stage starts.
- Tests: the integration tests run on both vendors through Testcontainers.

## Proof

- The acceptance tests pass. The suite runs on both vendors.
- Every migration is applied on both vendors by a test.
