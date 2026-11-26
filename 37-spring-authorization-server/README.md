# 37 · Spring Authorization Server

Level: middle. Status: not started.

## Change

Authentication moves to OAuth 2.0 / OpenID Connect: `auth-server` takes over the users and the sign-up form, the API becomes a Resource Server and the pages an OAuth2 Client. This is the only change of the authentication method in the project.

## Why

In 36 the application checks passwords itself, and every API request carries one.

## Tools

Spring Authorization Server, Spring Security OAuth2 Resource Server and Client.

## Proof

- The acceptance tests pass with tokens, and the pages sign in through `auth-server`.
