# 69 · Domain-Driven Design

Level: architect. Status: not started.

## Change

A note with its access rights becomes an aggregate that guards its invariants and registers domain events.

## Why

In 68 the rules of the domain are spread over services.

## Tools

`AbstractAggregateRoot` of Spring Data, value objects as records.

## Proof

- The acceptance tests pass.
- Plain unit tests prove the invariants of the aggregate.
