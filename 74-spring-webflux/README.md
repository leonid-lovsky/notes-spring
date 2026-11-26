# 74 · Spring WebFlux

Level: architect. Status: not started.

## Change

The live feed of the notifications service is reimplemented on a reactive web stack, and a measurement decides which version is deployed.

## Why

A live feed holds thousands of open connections, and the web stack is chosen by measurement rather than by fashion.

## Tools

Spring WebFlux with Spring Data R2DBC; how the second factor works in a reactive application is an open question recorded in ADR 0001.

## Proof

- The acceptance tests pass.
- Both versions pass the acceptance tests, and an ADR records the choice from a load test.
