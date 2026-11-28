# 16 · REST API

Level: junior. Status: not started.

## Change

Every function of the pages, the live feed included, is also available through a JSON REST API with HTTP Basic.

## Why

In 15 only a browser can use the application.

## Tools

`@RestController` with records, a second `SecurityFilterChain` for `/api/**`. The API has controllers of its own next to those of the pages, over the same services: `NoteController` keeps serving the pages under `/notes`, and `NoteRESTController` serves the API under `/api/notes`, so the bean names and the mappings do not collide. The version of a note from 11 is its `ETag`, and a change requires `If-Match` (RFC 9110), so a change over a newer one is refused with `412 Precondition Failed`.

## Proof

- The page tests pass.
- Every function of the pages works through the API.
