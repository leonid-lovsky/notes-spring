# 15 · Microservices

Level: senior. Status: not started.

## Change

Every module becomes a service with its own database. The browser interface of 03 becomes a `web` service that signs users in and calls the services with their token. Services call each other over HTTP; addresses and settings are plain configuration of each service.

## Why

Modules need independent releases and independent scaling.

## Tools

- HTTP service clients.
- Spring Cloud Contract for every client–provider pair.
- All services run in Docker Compose, ready for Amazon EC2; the three pipelines grow to many services.

## Proof

- The acceptance tests pass.
- Contract tests protect every client–provider pair.
