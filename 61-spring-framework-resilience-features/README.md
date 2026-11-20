# 61 · Spring Framework Resilience Features

Level: senior. Status: not started.

## Change

Calls between services have timeouts, and calls that may fail for a moment are retried.

## Why

In 60 a slow service blocks its callers without limit, and a short network failure fails a whole request.

## Tools

- Timeouts of the HTTP clients and the resilience features of Spring Framework (`@Retryable`, `@ConcurrencyLimit`), checked against the Spring Framework reference when the stage starts.

## Proof

- The acceptance tests pass.
- A request survives a short outage of a service it calls.
