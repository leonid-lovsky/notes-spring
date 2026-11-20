# 29 · Spring Cache Abstraction

Level: middle. Status: not started.

## Change

Frequent reads — a note and its access rights — are cached through the cache abstraction of Spring, in the memory of the application, and evicted on change.

## Why

In 28 the metrics of Actuator show that every read of a note and its access rights goes to the database.

## Tools

- `@Cacheable` and `@CacheEvict` with the simple in-memory cache that Spring Boot configures by default.

## Proof

- The acceptance tests pass.
- The cache metrics of Actuator show hits, and the number of database queries drops.
