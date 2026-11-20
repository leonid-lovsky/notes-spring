# 25 · Redis Pub/Sub

Level: middle. Status: not started.

## Change

The live feed works across instances: a notification created on one instance reaches open feeds on every instance.

## Why

In 24 the live feed of 15 reaches only the connections held by the instance that handled the change.

## Tools

- Redis publish/subscribe through Spring Data Redis; Redis of 23.

## Proof

- The acceptance tests pass.
- A notification created through one instance appears in a feed opened on the other one.
