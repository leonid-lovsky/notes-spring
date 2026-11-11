# 05 · Actuator

Level: junior. Status: not started.

## Change

The application exposes its health, information and metrics through Actuator endpoints.

## Why

In 04 nothing outside the application can tell whether it is alive, ready to serve requests or which version is running.

## Tools

- Spring Boot Actuator: health with liveness and readiness groups, info with build and Git data, metrics; endpoints on a separate management port.

## Proof

- The acceptance tests pass.
- Health, info and metrics endpoints answer, and the health turns down when the database is unavailable.
