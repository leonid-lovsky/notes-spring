# 06 · Hexagonal

Level: senior. Status: not started.

## Change

A storage port is introduced in one or two services — only where a second implementation appears — with two adapters: Spring Data JDBC (PostgreSQL, MySQL or H2, selected by profile) and Spring Data MongoDB. The system keeps running on Kubernetes.

## Why

Delivery to infrastructure owned by someone else requires a different database.

## Tools

- Persistence: Spring Data JDBC, Spring Data MongoDB.
- Tests: one contract test of the port, run against every adapter; a GitHub Actions matrix over databases.

## Proof

- The acceptance tests pass.
- The same contract test passes on every adapter and database.
