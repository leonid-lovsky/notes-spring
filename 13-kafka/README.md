# 13 · Kafka

Level: middle+. Status: not started.

## Change

Notifications leave the process and become a separate service with its own database. Module events reach it through Kafka.

## Why

Notifications have a different load profile and release cycle than the rest of the application.

## Tools

- Spring for Apache Kafka, Spring Modulith event externalization (`spring-modulith-events-kafka`), Kafka in Docker Compose and in Testcontainers.

## Proof

- The acceptance tests pass.
- With the notifications service stopped for a minute, no events are lost or duplicated after it comes back.
