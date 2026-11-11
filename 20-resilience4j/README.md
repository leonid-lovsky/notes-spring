# 20 · Resilience4j

Level: senior. Status: not started.

## Change

Calls between services have timeouts, retries, circuit breakers and bulkheads; the gateway limits the request rate.

## Why

In 19 a slow or failed service blocks its callers.

## Tools

- Resilience4j through Spring Cloud Circuit Breaker, the gateway rate limiter with Redis.
- Failure tests with Testcontainers and Toxiproxy.

## Proof

- The acceptance tests pass.
- A failed or slow service does not take the system down; its callers degrade predictably, and the dashboard shows the circuit breaker states.
