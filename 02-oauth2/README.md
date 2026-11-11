# 02 · OAuth2

Level: junior. Status: not started.

## Change

Authentication moves from passwords checked by the application to OAuth 2.0 / OpenID Connect. Tokens are issued by `auth-server`, a separate Spring Boot application on Spring Authorization Server, which takes over the user credentials; the application becomes a Resource Server and keeps only the user profiles. This is the only change of the HTTP contract after 01.

## Why

In 01 every request carries the user's password, and the application both stores credentials and serves notes. A dedicated authorization server separates these concerns and lets other clients, such as the browser interface of 03, sign in.

## Tools

- Spring Security OAuth2 Resource Server (JWT).
- Spring Authorization Server, configured through properties.
- Tests: Spring Security test support for slice tests; the acceptance tests obtain a token through standard OpenID Connect discovery.

## Proof

- The acceptance tests pass with token authentication.
- A request without a token gets 401, a request without the needed role gets 403, both as `ProblemDetail`.
