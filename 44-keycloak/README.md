# 44 · Keycloak

Level: middle. Status: not started.

## Change

Keycloak replaces `auth-server`; the application changes only its issuer settings.

## Why

Many companies run Keycloak, and the application relies only on OpenID Connect.

## Tools

Keycloak with a realm stored as code; how it confirms a phone number is an open question recorded in ADR 0001.

## Proof

- The acceptance tests pass with only the issuer URL changed.
