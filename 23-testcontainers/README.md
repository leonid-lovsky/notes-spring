# 23 · Testcontainers

Level: junior. Status: not started.

## Change

The tests run on PostgreSQL in a container, and H2 leaves the build.

## Why

In 22 a test can pass on H2 and fail on PostgreSQL.

## Tools

Testcontainers with `@ServiceConnection`. The image is now declared both in the Compose file and in the tests, a compromise recorded in ADR 0001.

## Proof

- The acceptance tests pass.
- A test catches behaviour specific to PostgreSQL.
