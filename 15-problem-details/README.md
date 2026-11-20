# 15 · Problem Details

Level: junior. Status: not started.

## Change

Errors of the API are returned in the standard format of RFC 9457, Problem Details for HTTP APIs.

## Why

In 14 the API returns the error body of Spring Boot, which has no standard format and carries no field errors of validation.

## Tools

- `ProblemDetail` responses through the Spring MVC support for RFC 9457, enabled by a Spring Boot property; validation errors with their fields.

## Proof

- The page tests pass.
- Validation, authorization and not-found errors of the API are `ProblemDetail` responses.
