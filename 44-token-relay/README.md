# 44 · Token Relay

Level: senior. Status: not started.

## Change

The gateway signs the browser in, keeps the session in Redis and relays the access token; the application only accepts tokens.

## Why

In 43 every process facing a browser would have to handle sign-in.

## Tools

The token relay of Spring Cloud Gateway with Spring Session.

## Proof

- The acceptance tests pass.
- The application receives a token, never a browser session.
