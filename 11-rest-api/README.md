# 11 · REST API

Level: junior. Status: not started.

## Change

Every function of the pages is also available through a JSON REST API with HTTP Basic authentication; lists are paginated as on the pages.

## Why

In 10 the application can be used only through a browser; no other client can work with it over HTTP.

## Tools

- `@RestController` with records as request and response bodies, mapped by hand.
- Two `SecurityFilterChain` beans, as the Spring Security reference shows for an application with an API and pages: `/api/**` with HTTP Basic, without a session and without CSRF; the pages keep form login.
- Tests: `RestTestClient` slice tests.

## Proof

- The page tests pass.
- Every function of the pages works through the API in the slice tests.
