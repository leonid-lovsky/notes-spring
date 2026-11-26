# 60 · Spring Framework Resilience Features

Level: senior. Status: not started.

## Change

Calls between services have timeouts and retries.

## Why

In 59 a slow service blocks its callers, and a short failure fails a request.

## Tools

Resilience features of Spring Framework.

## Proof

- The acceptance tests pass.
- A request survives a short outage of a service it calls.
