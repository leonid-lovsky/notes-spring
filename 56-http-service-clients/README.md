# 56 · HTTP Service Clients

Level: senior. Status: not started.

## Change

The OpenFeign clients are replaced by declarative HTTP service clients of Spring Framework. The services do not change otherwise.

## Why

Spring Cloud OpenFeign is feature-complete, and Spring recommends HTTP service clients instead.

## Tools

- Spring HTTP service clients (`@HttpExchange` interfaces on `RestClient`).

## Proof

- The acceptance tests pass.
- No OpenFeign dependency remains.
