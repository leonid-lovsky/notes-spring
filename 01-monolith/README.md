# 01 · Monolith

Level: junior. Status: not started.

## Change

The baseline. Functionality and HTTP API are fixed here and stay the same in every later stage: users, notes, note sharing with roles, notifications. Only the internal structure changes afterwards.

## Tools

Spring Boot starters with auto-configuration: Spring MVC, Spring Data JPA, PostgreSQL, Flyway, Bean Validation, Spring Security with OAuth2 Client and Resource Server (Keycloak as the OIDC provider), Thymeleaf, Actuator. Tests: JUnit, slice tests, Testcontainers with `@ServiceConnection`. Local run: Spring Boot Docker Compose support. Delivery: GitHub Actions, container image, VPS with TLS.

## Proof

End-to-end scenarios pass against the running application; the live instance is reachable over HTTPS.
