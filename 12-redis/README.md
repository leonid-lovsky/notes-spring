# 12 · Redis

Level: middle. Status: not started.

## Change

Frequent reads are cached in Redis.

## Why

In 11 the dashboard shows that every read of a note and its access rights goes to the database.

## Tools

- Redis through Spring Cache; cache invalidation on change.

## Proof

- The acceptance tests pass.
- The dashboard shows cache hits and fewer database queries.
