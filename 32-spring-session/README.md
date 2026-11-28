# 32 · Spring Session

Level: middle. Status: not started.

## Change

Two instances run behind NGINX, and the session is kept in Redis.

## Why

In 31 a session lives in one instance.

## Tools

Spring Session with Redis.

## Proof

- The acceptance tests pass.
- A user stays signed in across both instances and a restart.
