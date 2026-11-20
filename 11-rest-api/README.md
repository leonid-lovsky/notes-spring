# 11 · REST API

Level: junior. Status: not started.

## Change

Every function of the pages is also available through a JSON REST API with HTTP Basic authentication; errors are returned as `ProblemDetail` (RFC 9457) and lists are paginated. From this stage the black-box acceptance tests run against the system.

## Why

In 10 the application can be used only through a browser; no other client and no automated test can work with it over HTTP.

## Tools

- `@RestController` with records as request and response bodies, mapped by hand; `ProblemDetail` responses.
- Two `SecurityFilterChain` beans, as the Spring Security reference shows for an application with an API and pages: `/api/**` with HTTP Basic, without a session and without CSRF; the pages keep form login.
- Tests: `RestTestClient` slice tests; the acceptance tests of the capabilities notes and sharing.

## Proof

- The acceptance tests pass.
- Validation and authorization errors of the API are `ProblemDetail` responses.
