# 44 · RabbitMQ

Level: senior. Status: not started.

## Change

Notifications leave the process and become the first separate service, with its own database and the live feed. Module events reach it through RabbitMQ.

## Why

In 43 notifications share the release cycle and the load of the whole application, although nothing else depends on them.

## Tools

- Spring AMQP, Spring Modulith event externalization, durable queues with a dead-letter queue; RabbitMQ in Docker Compose and in Testcontainers.

## Proof

- The acceptance tests pass.
- With the notifications service stopped for a minute, no notification is lost after it comes back; a message that keeps failing ends in the dead-letter queue.
