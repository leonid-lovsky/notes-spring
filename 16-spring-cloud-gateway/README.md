# 16 · Spring Cloud Gateway

Level: senior. Status: not started.

## Change

A gateway becomes the single entry point: it routes requests to the services, takes over browser sign-in, keeps the session in Redis and relays the token issued by `auth-server`.

## Why

In 15 clients must know the address of every service, and every service that faces a browser handles sign-in itself.

## Tools

- Spring Cloud Gateway, OAuth2 login with token relay, Spring Session with Redis — Redis, used for caching since 12, also keeps the sessions.

## Proof

- The acceptance tests pass.
- Several gateway instances serve one user session.
- Clients use one address for the whole system.
