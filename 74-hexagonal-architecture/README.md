# 74 · Hexagonal Architecture

Level: architect. Status: not started.

## Change

A storage port separates the domain from the persistence technology.

## Why

Delivery to foreign infrastructure may require a store that JPA does not cover. Until this stage the entities of Jakarta Persistence are the model of the domain, so the domain carries the mapping and a row read from the database bypasses the constructors that guard its invariants.

## Tools

A storage port with one contract test.

## Proof

- The acceptance tests pass.
- The contract test passes on PostgreSQL and MySQL.
