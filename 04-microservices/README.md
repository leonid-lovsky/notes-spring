# 04 · Microservices

Level: senior. Status: not started.

## Change

Every module becomes a service. The Thymeleaf UI becomes a `web` service behind the gateway; the gateway handles login and relays the token to the services.

## Why

Modules need independent releases, and calls between them now go over the network.

## Tools

Spring Cloud Gateway, Spring Cloud Config, Eureka, Resilience4j (Spring Cloud Circuit Breaker), HTTP service clients, Spring Cloud Contract, a choreographed saga over Kafka events. CI: Jenkins in addition to GitHub Actions.

## Proof

A failed service does not take the system down; a failure in the middle of the saga is compensated; contract tests protect every client–provider pair.
