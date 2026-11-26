# 14 · Problem Details

Level: junior. Status: not started.

## Change

Errors of the API follow RFC 9457, Problem Details for HTTP APIs.

## Why

In 13 errors have no standard format and no field errors.

## Tools

`ProblemDetail` support of Spring MVC, enabled by a Spring Boot property.

## Proof

- The page tests pass.
- Validation, authorization and not-found errors are `ProblemDetail` responses.
