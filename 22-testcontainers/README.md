# 22 · Testcontainers

Level: junior. Status: not started.

## Change

The tests run against PostgreSQL in a container instead of the embedded H2; H2 leaves the build.

## Why

In 21 the tests run on H2, which differs from PostgreSQL in types, SQL and locking, so a test can pass while the application fails on PostgreSQL.

## Tools

- Testcontainers with `@ServiceConnection` through Spring Boot auto-configuration.

## Proof

- The acceptance tests pass.
- A test that passed on H2 now catches a behaviour specific to PostgreSQL.
