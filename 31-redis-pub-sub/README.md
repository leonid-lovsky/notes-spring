# 31 · Redis Pub/Sub

Level: middle. Status: not started.

## Change

The live feed works across instances.

## Why

In 30 a notification reaches only the feeds held by its own instance.

## Tools

Redis publish/subscribe through Spring Data Redis.

## Proof

- The acceptance tests pass.
- A notification from one instance appears in a feed opened on the other.
