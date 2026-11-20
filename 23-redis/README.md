# 23 · Redis

Level: middle. Status: not started.

## Change

Frequent reads are cached in Redis.

## Why

In 22 the cache metrics of Actuator show that every read of a note and its access rights goes to the database.

## Tools

- Redis through Spring Cache; cache invalidation on change.

## Proof

- The acceptance tests pass.
- The cache metrics of Actuator show hits, and the number of database queries drops.
