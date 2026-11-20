# 06 · Flyway

Level: junior. Status: not started.

## Change

The database schema is managed by versioned migrations instead of `schema.sql`.

## Why

In 05 `schema.sql` can only create a schema from scratch; it cannot change the schema of a database that already holds data.

## Tools

- Flyway through Spring Boot auto-configuration; the first migration reproduces the schema of 05; the migrations run on PostgreSQL and on H2 of the tests.

## Proof

- The page tests pass.
- A new migration changes an existing database without losing data.
