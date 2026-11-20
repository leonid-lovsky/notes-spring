# 61 · Gateway Rate Limiting

Level: senior. Status: not started.

## Change

The gateway limits the request rate of every user.

## Why

In 60 the services protect themselves from each other, but one client can still flood the whole system.

## Tools

- The request rate limiter of Spring Cloud Gateway with Redis.

## Proof

- The acceptance tests pass.
- Requests above the limit get 429, and other users are not affected.
