# 56 · Sagas

Level: senior. Status: not started.

## Change

Operations that span several services, such as creating a note together with its owner access, become choreographed sagas over broker events with compensating actions.

## Why

In 55 a failure in the middle of such an operation leaves the services inconsistent, for example a note without an owner.

## Tools

- Events through Spring Modulith event externalization, idempotent consumers, compensating actions.

## Proof

- The acceptance tests pass.
- A failure in the middle of a saga is compensated, and no note is left without an owner.
