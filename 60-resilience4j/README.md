# 60 · Resilience4j

Level: senior. Status: not started.

## Change

Calls between services have circuit breakers and bulkheads: a failing service is no longer called until it recovers, and one slow dependency cannot exhaust the threads of its caller.

## Why

In 59 retries make a failing service worse: its callers keep calling it, and their threads wait for it.

## Tools

- Resilience4j through Spring Cloud Circuit Breaker.
- Failure tests that stop or slow down a service container in Testcontainers.

## Proof

- The acceptance tests pass.
- A failed or slow service does not take the system down; its callers degrade predictably, and the dashboard shows the circuit breaker states.
