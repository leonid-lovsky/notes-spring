# 14 · REST API

Level: junior. Status: not started.

## Change

Every function of the pages, the live feed included, is also available through a JSON REST API with HTTP Basic.

## Why

In 13 only a browser can use the application.

## Tools

`@RestController` with records, a second `SecurityFilterChain` for `/api/**`.

## Proof

- The page tests pass.
- Every function of the pages works through the API.
