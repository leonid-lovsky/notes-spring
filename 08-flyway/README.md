# 08 · Flyway

Level: junior. Status: not started.

## Change

The schema is managed by versioned migrations, one set for every DBMS.

## Why

Since 01 the schema scripts cannot change a database that already holds data, and the next stage must add an owner to every note.

## Tools

Flyway through Spring Boot auto-configuration, with a migration location for every DBMS.

## Proof

- The page tests pass.
- A migration changes an existing database without losing data, in every DBMS.
