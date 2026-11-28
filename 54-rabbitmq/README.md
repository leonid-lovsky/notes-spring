# 54 · RabbitMQ

Level: senior. Status: not started.

## Change

Notifications become the first separate service and receive module events through a broker.

## Why

In 53 notifications share the release cycle and load of the whole application.

## Tools

RabbitMQ through Spring AMQP and Spring Modulith event externalization.

## Proof

- The acceptance tests pass.
- No notification is lost while the service is stopped.
