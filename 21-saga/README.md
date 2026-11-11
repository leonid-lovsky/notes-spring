# 21 · Saga

Level: senior. Status: not started.

## Change

A business operation that spans several services — creating a note together with its owner access — becomes a choreographed saga over Kafka events with compensating actions.

## Why

In 20 such an operation can leave one service done and the other not, because there is no transaction across services.

## Tools

- A choreographed saga over Kafka events, idempotent consumers, compensating actions.

## Proof

- The acceptance tests pass.
- A failure in the middle of the saga is compensated, and no note is left without an owner.
