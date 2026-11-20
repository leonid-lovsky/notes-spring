# 57 · Spring Cloud Contract

Level: senior. Status: not started.

## Change

Every client–provider pair of services is protected by consumer-driven contract tests.

## Why

In 56 a provider can change its API and break a client that only the acceptance tests of the whole system would notice.

## Tools

- Spring Cloud Contract: contracts in the provider, generated provider tests, stubs used by the client tests.

## Proof

- The acceptance tests pass.
- An incompatible change of a provider fails its build.
