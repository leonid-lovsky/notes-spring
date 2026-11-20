# 61 · DDD

Level: architect. Status: not started.

## Change

The domain model follows domain-driven design: a note with its access rights is one aggregate that guards its own invariants, such as the last owner that cannot be removed; roles and identifiers are value objects; the aggregate registers domain events when it changes, and Spring Data publishes them when the aggregate is saved.

## Why

In 60 the rules of the domain are spread over service classes and security expressions, and entities are bags of fields that any service can put into an invalid state.

## Tools

- Spring Data JPA aggregate roots with `AbstractAggregateRoot` and domain events published on save; value objects as records; the invariants tested without Spring.

## Proof

- The acceptance tests pass.
- The invariants of the aggregate hold for every way of changing it, checked by plain unit tests.
