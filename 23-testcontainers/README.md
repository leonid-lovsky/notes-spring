# 23 · Testcontainers

Level: junior. Status: not started.

## Change

The tests run against PostgreSQL in a container instead of the embedded H2; H2 leaves the build.

## Why

In 22 the tests run on H2, which differs from PostgreSQL in types, SQL and locking, so a test can pass while the application fails on PostgreSQL.

## Tools

- Testcontainers with `@ServiceConnection` through Spring Boot auto-configuration.
- H2 and the H2 console module, added by Spring Initializr in 01, leave the build.
- From this stage the image of PostgreSQL is declared twice, in the Compose file and in the Testcontainers configuration, and the two are kept equal by hand; the compromise is recorded in ADR 0001.

## Proof

- The acceptance tests pass.
- A test that passed on H2 now catches a behaviour specific to PostgreSQL.
