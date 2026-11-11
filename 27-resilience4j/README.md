# 27 · Resilience4j

Level: senior. Status: not started.

## Change

Calls between services have timeouts, retries, circuit breakers and bulkheads; the gateway limits the request rate.

## Why

In 26 a slow or failed service blocks its callers.

## Tools

- Resilience4j through Spring Cloud Circuit Breaker for circuit breakers and bulkheads; retries and concurrency limits through the resilience features of Spring Framework where they suffice; the gateway rate limiter with Redis.
- Failure tests that stop or slow down a service container in Testcontainers.

## Proof

- The acceptance tests pass.
- A failed or slow service does not take the system down; its callers degrade predictably, and the dashboard shows the circuit breaker states.
