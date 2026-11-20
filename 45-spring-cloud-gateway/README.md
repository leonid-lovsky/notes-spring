# 45 · Spring Cloud Gateway

Level: senior. Status: not started.

## Change

A gateway becomes the single entry point in front of the application: it routes the requests of the pages and of the API. The gateway is also the seam along which the next stages extract services one by one (strangler fig pattern).

## Why

In 44 clients address the application directly, and the next stages are about to add processes that clients would have to know.

## Tools

- Spring Cloud Gateway with routes in properties, behind NGINX.

## Proof

- The acceptance tests pass.
- Clients use one address for the whole system.
