# 44 · Keycloak

Level: middle. Status: not started.

## Change

Keycloak replaces `auth-server` as the OpenID Connect provider, together with registration, email confirmation and the second factor. The application changes only its issuer settings; users, clients and roles move into a Keycloak realm stored as code.

## Why

Spring Authorization Server of 40–43 covers the protocol, but many companies run Keycloak as their identity provider. Because the application relies only on OpenID Connect, the provider can be replaced without touching the code.

## Tools

- Keycloak in the Compose file with a realm imported from a file; the Keycloak Testcontainers module for tests.
- Spring Security OAuth2 Resource Server and Client, unchanged.
- Open question, recorded in ADR 0001: how Keycloak confirms a phone number by SMS, as `auth-server` did in 42, is checked against the Keycloak documentation when the stage starts.

## Proof

- The acceptance tests pass with only the issuer URL changed.
- The pages sign in through Keycloak with email confirmation and a second factor.
