# 28 · WebFlux

Level: architect. Status: not started.

## Change

A second web stack for the notifications service. The live notification feed over Server-Sent Events, built on `SseEmitter` since 01, is reimplemented on Spring WebFlux with reactive storage. The Spring MVC and WebFlux versions are two applications; a Helm value selects which one is deployed.

## Why

A live feed keeps thousands of connections open. The stage is about choosing a technology on measured trade-offs rather than on fashion.

## Tools

- Spring WebFlux, Spring Data R2DBC or reactive MongoDB, `WebTestClient`.
- Gatling compares both versions under the same load.

## Proof

- Both versions pass the same acceptance tests.
- A load test compares them, and an ADR records the choice.
