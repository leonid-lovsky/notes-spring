# 30 · Spring Cache Abstraction

Level: middle. Status: not started.

## Change

Frequent reads of notes and access rights are cached in memory and evicted on change.

## Why

In 29 every read goes to the database.

## Tools

Spring Cache Abstraction with the default in-memory cache.

## Proof

- The acceptance tests pass.
- The cache metrics show hits.
