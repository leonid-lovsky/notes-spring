# 55 · Spring Cloud OpenFeign

Level: senior. Status: not started.

## Change

The remaining modules are extracted behind the gateway into services with their own databases; the pages become a `web` service. Services call each other through Spring Cloud OpenFeign clients and authenticate to each other with OAuth2 client credentials.

## Why

Since 51 only notifications are independent; the other modules still share releases and scaling.

## Tools

- Spring Cloud OpenFeign, used deliberately as the long-established way that the next stage replaces; OAuth2 client credentials through Keycloak; all services in the Compose file; both pipelines grow to many services.

## Proof

- The acceptance tests pass.
- Operations that span services are not yet atomic; 58 closes this gap.
