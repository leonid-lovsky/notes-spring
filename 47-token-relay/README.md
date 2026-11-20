# 47 · Token Relay

Level: senior. Status: not started.

## Change

The gateway takes over browser sign-in: it signs the user in through Keycloak, keeps the session in Redis and relays the access token to the application with every request (backend for frontend). The application no longer handles browser sign-in; it only accepts tokens.

## Why

In 46 every process that faces a browser has to handle sign-in and keep a session itself, and a browser client would have to hold tokens.

## Tools

- OAuth2 login and the token relay filter of Spring Cloud Gateway, Spring Session with Redis of 32.

## Proof

- The acceptance tests pass.
- Several gateway instances serve one user session.
- The application receives a token, never a browser session.
