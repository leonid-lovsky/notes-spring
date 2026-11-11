# 20 · Kafka

Level: middle. Status: not started.

## Change

Domain events move from RabbitMQ to Kafka, which keeps them as an ordered, replayable log. An audit consumer added at this stage reads the whole history from the beginning.

## Why

In 19 RabbitMQ removes a message once it is consumed: a consumer added later cannot read the events that happened before it, and the sagas of 23 will need events ordered per note.

## Tools

- Spring for Apache Kafka, Spring Modulith event externalization (`spring-modulith-events-kafka`), partitioning by note, idempotent consumers; Kafka in Docker Compose and in Testcontainers.

## Proof

- The acceptance tests pass.
- An audit consumer started after the events happened reads all of them in order.
- With the notifications service stopped for a minute, no events are lost or duplicated after it comes back.
