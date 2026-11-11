# 19 · RabbitMQ

Level: middle. Status: not started.

## Change

Notifications leave the process and become a separate service with its own database. Module events reach it through RabbitMQ.

## Why

Notifications have a different load profile and release cycle than the rest of the application. A message broker is the next step after the in-process events of 13.

## Tools

- Spring AMQP, Spring Modulith event externalization (`spring-modulith-events-amqp`), durable queues with a dead-letter queue; RabbitMQ in Docker Compose and in Testcontainers.

## Proof

- The acceptance tests pass.
- With the notifications service stopped for a minute, no notification is lost after it comes back; a message that keeps failing ends in the dead-letter queue.
