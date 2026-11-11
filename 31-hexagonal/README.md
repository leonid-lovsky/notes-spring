# 31 · Hexagonal

Level: architect. Status: not started.

## Change

A storage port is introduced in one service, with the existing Spring Data JPA adapter behind it, and the database vendor becomes a choice: PostgreSQL, MySQL or H2, selected by profile.

## Why

Delivery to infrastructure owned by someone else requires a different database. The stage is about designing for replaceability and deciding where an abstraction pays off.

## Tools

- A storage port and one contract test of it, run against every vendor in a GitHub Actions matrix.

## Proof

- The acceptance tests pass.
- The same contract test passes on PostgreSQL, MySQL and H2.
