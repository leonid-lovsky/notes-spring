# 74 · Hexagonal Architecture

Level: architect. Status: not started.

## Change

A storage port separates the domain from the persistence technology.

## Why

Delivery to foreign infrastructure may require a store that JPA does not cover.

## Tools

A storage port with one contract test.

## Proof

- The acceptance tests pass.
- The contract test passes on PostgreSQL and MySQL.
