# 28 · Redis Cache

Level: middle. Status: not started.

## Change

The cache moves to Redis without a change of code.

## Why

In 27 the cache is lost on restart and competes for the heap, and the next stage needs Redis for two instances anyway.

## Tools

Redis as the provider of Spring Cache.

## Proof

- The acceptance tests pass.
- The cache survives a restart of the application.
