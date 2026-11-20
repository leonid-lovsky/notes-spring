# 62 · Hexagonal

Level: architect. Status: not started.

## Change

A storage port is introduced in the notes service, with the existing Spring Data JPA adapter behind it; the domain no longer depends on the persistence technology (Clean Architecture).

## Why

Delivery to infrastructure owned by someone else may require a store that JPA does not cover. The stage is about designing for replaceability and deciding where an abstraction pays off.

## Tools

- A storage port and one contract test of it, run against PostgreSQL, MySQL and H2.

## Proof

- The acceptance tests pass.
- The same contract test passes on PostgreSQL, MySQL and H2.
