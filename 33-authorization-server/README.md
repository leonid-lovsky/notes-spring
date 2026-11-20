# 33 · Authorization Server

Level: middle. Status: not started.

## Change

Authentication moves from passwords checked by the application to OAuth 2.0 / OpenID Connect. Tokens are issued by `auth-server`, a separate Spring Boot application on Spring Authorization Server, which takes over the users, their credentials and the sign-up form; the application keeps only a profile of each user — the subject and the name from the token. The REST API becomes a Resource Server, and the pages sign in as an OAuth2 Client. Both clients switch in the same stage, because once the credentials leave the application neither of them can check a password. This is the only change of the authentication method in the project.

## Why

In 32 the application stores credentials and checks passwords itself, and every API request carries the user's password.

## Tools

- Spring Security OAuth2 Resource Server (JWT) for the API, OAuth2 Client with the authorization code flow for the pages.
- Spring Authorization Server as a second service in the Compose file, with the users and the sign-up form of 09.
- Tests: Spring Security test support for slice tests; the acceptance tests obtain a token through standard OpenID Connect discovery.

## Proof

- The acceptance tests pass with token authentication.
- A request without a token gets 401, a request without the needed role gets 403, both as `ProblemDetail`.
- The pages sign in through `auth-server`.
