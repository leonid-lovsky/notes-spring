# 45 · Kafka

Level: senior. Status: not started.

## Change

Kafka becomes the second broker, selected by configuration instead of RabbitMQ; the code of the services does not change. Kafka keeps the events as an ordered log, so a consumer added later can read the history.

## Why

In 44 the system is bound to one broker, while Kafka and RabbitMQ are alternatives that companies choose between.

## Tools

- Spring for Apache Kafka, Spring Modulith event externalization, partitioning by note, idempotent consumers; Kafka in Docker Compose and in Testcontainers.

## Proof

- The acceptance tests pass with either broker.
- A consumer started after the events happened reads all of them in order.
