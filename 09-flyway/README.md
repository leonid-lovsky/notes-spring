# 09 · Flyway

Level: junior. Status: not started.

## Change

The schema is managed by versioned migrations.

## Why

In 08 `schema.sql` cannot change a database that already holds data, and the next stage must add an owner to every note.

## Tools

Flyway through Spring Boot auto-configuration.

## Proof

- The page tests pass.
- A migration changes an existing database without losing data.
