# 53 · Spring Cloud OpenFeign

Level: senior. Status: not started.

## Change

Every module becomes a service, and services call each other through OpenFeign as an intermediate step.

## Why

Since 49 only notifications are independent.

## Tools

Spring Cloud OpenFeign, OAuth2 client credentials.

## Proof

- The acceptance tests pass; operations across services are not yet atomic, which 56 closes.
