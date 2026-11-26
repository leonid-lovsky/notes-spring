# 60 · Resilience4j

Level: senior. Status: not started.

## Change

Calls between services get circuit breakers and bulkheads.

## Why

In 59 retries make a failing service worse.

## Tools

Resilience4j through Spring Cloud Circuit Breaker.

## Proof

- The acceptance tests pass.
- A failed or slow service does not take the system down.
