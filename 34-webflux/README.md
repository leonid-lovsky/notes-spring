# 34 · WebFlux

Level: architect. Status: not started.

## Change

A second web stack for the notifications service. The live notification feed over Server-Sent Events, built on `SseEmitter` since 01, is reimplemented on Spring WebFlux with reactive MongoDB from 32. The Spring MVC and WebFlux versions are two applications; the cluster manifests select which one is deployed.

## Why

A live feed keeps thousands of connections open. The stage is about choosing a technology on measured trade-offs rather than on fashion.

## Tools

- Spring WebFlux, reactive Spring Data MongoDB, `WebTestClient`.
- A load test of the live feed with many open connections, measured on the Grafana dashboard.

## Proof

- Both versions pass the same acceptance tests.
- A load test compares them, and an ADR records the choice.
