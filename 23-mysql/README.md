# 23 · MySQL

Level: junior. Status: not started.

## Change

The application runs on PostgreSQL or MySQL, selected by profile, with migrations per vendor.

## Why

In 22 the application is bound to one vendor.

## Tools

Spring Data JPA and vendor-specific Flyway locations.

## Proof

- The acceptance tests pass on both vendors.
- The tests of the stage and the build run against each supported database, and a database-specific failure fails the build.
