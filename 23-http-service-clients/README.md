# 23 · HTTP Service Clients

Level: senior. Status: not started.

## Change

The remaining modules are extracted behind the gateway into services with their own databases; the browser interface becomes a `web` service. Services call each other through declarative HTTP service clients of Spring Framework and authenticate to each other with OAuth2 client credentials. Operations that span several services, such as creating a note together with its owner access, become choreographed sagas over Kafka events with compensating actions.

## Why

Modules need independent releases and independent scaling.

## Tools

- Spring HTTP service clients (`@HttpExchange` interfaces on `RestClient`); OAuth2 client credentials through Keycloak.
- Consumer-driven contract tests with Spring Cloud Contract for every client–provider pair.
- Sagas over Kafka events with idempotent consumers.
- All services run in Docker Compose; both pipelines grow to many services.

## Proof

- The acceptance tests pass.
- Contract tests protect every client–provider pair.
- A failure in the middle of a saga is compensated, and no note is left without an owner.
