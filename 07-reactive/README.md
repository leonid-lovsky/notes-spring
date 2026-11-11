# 07 · Reactive

Level: senior. Status: not started.

## Change

A second web stack for the notifications service: a live notification feed over Server-Sent Events on Spring WebFlux with reactive storage. The Spring MVC and WebFlux versions are two applications; a Helm value selects which one is deployed.

## Why

A live feed keeps thousands of connections open.

## Tools

Spring WebFlux, Spring Data R2DBC or reactive MongoDB, `WebTestClient`, Gatling.

## Proof

Both versions pass the same end-to-end scenarios; a load test compares them.
