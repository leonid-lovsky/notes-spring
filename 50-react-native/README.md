# 50 · React Native

Level: senior. Status: not started.

## Change

A mobile client in React Native works with the same REST API.

## Why

In 49 the system has no native mobile client.

## Tools

- React Native; the client signs in to Keycloak as a public OAuth2 client with PKCE and calls the API through the gateway with a bearer token.

## Proof

- The acceptance tests pass.
- The mobile client signs in and works with notes.
