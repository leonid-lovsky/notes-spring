# 02 · Flyway

Level: junior. Status: not started.

## Change

The database schema is managed by versioned migrations instead of `schema.sql`.

## Why

In 01 `schema.sql` can only create a schema from scratch; it cannot change the schema of a database that already holds data.

## Tools

- Flyway through Spring Boot auto-configuration; the first migration reproduces the schema of 01.

## Proof

- The acceptance tests pass.
- A new migration changes an existing database without losing data.
