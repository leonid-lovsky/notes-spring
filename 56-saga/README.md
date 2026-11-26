# 56 · Saga

Level: senior. Status: not started.

## Change

Operations across services become choreographed sagas with compensation.

## Why

Since 53 a failure in the middle of such an operation leaves the services inconsistent.

## Tools

Broker events, idempotent consumers, compensating actions.

## Proof

- The acceptance tests pass.
- A failed saga is compensated, and no note is left without an owner.
