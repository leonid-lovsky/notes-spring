# 31 · Redis Cache

Level: middle. Status: not started.

## Change

The cache moves from the memory of the application to Redis; the code of 30 does not change.

## Why

In 30 every instance keeps its own cache, which is lost on restart and grows with the heap.

## Tools

- Redis as the cache provider of Spring Cache through Spring Boot auto-configuration; Redis in the Compose file and in Testcontainers.

## Proof

- The acceptance tests pass.
- The cache survives a restart of the application, and the code of the services is unchanged.
