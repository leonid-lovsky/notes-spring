# 12 · Keycloak

Level: middle. Status: not started.

## Change

Keycloak replaces `auth-server` as the OpenID Connect provider. The application changes only its issuer settings; users, clients and roles move into a Keycloak realm stored as code.

## Why

Spring Authorization Server of 11 covers the protocol, but many companies run Keycloak as their identity provider. Because the application relies only on OpenID Connect, the provider can be replaced without touching the code.

## Tools

- Keycloak in Docker Compose with a realm imported from a file; the Keycloak Testcontainers module for tests.
- Spring Security OAuth2 Resource Server and Client, unchanged.

## Proof

- The acceptance tests pass with only the issuer URL changed.
- The browser interface signs in through Keycloak.
