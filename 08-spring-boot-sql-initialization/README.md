# 08 · Spring Boot SQL Initialization

Level: junior. Status: not started.

## Change

The schema is created from `schema.sql`, and Hibernate only validates the entities against it.

## Why

Since 01 Hibernate generates a schema that is written down nowhere and drifts from the code as it grows.

## Tools

Spring Boot SQL initialization, `spring.jpa.hibernate.ddl-auto=validate`.

## Proof

- The page tests pass.
- An entity that does not match the schema stops the application at startup.
