# 11 · OAuth2

Level: middle. Status: not started.

## Change

Authentication moves from passwords checked by the application to OAuth 2.0 / OpenID Connect. Tokens are issued by `auth-server`, a separate Spring Boot application on Spring Authorization Server, which takes over the user credentials. The REST API becomes a Resource Server, and the browser interface of 06 signs in as an OAuth2 Client. This is the only change of the HTTP contract after 01.

## Why

In 10 the application stores credentials and checks passwords itself, and every API request carries the user's password. A dedicated authorization server separates these concerns and gives every client — the API, the browser interface and later the services — one way to sign in.

## Tools

- Spring Security OAuth2 Resource Server (JWT) for the API, OAuth2 Client with the authorization code flow for the pages.
- Spring Authorization Server, configured through properties, as a second service in the Compose file.
- Tests: Spring Security test support for slice tests; the acceptance tests obtain a token through standard OpenID Connect discovery.

## Proof

- The acceptance tests pass with token authentication.
- A request without a token gets 401, a request without the needed role gets 403, both as `ProblemDetail`.
- The browser interface signs in through `auth-server`.
