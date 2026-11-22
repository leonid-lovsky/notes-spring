# 11 · Method Security

Level: junior. Status: not started.

## Change

A note is shared with other users in a role — owner, editor or viewer — and the last owner cannot be removed.

## Why

In 10 users cannot work on a note together.

## Tools

Spring Security method security with `@PreAuthorize`.

## Proof

- The page tests pass.
- Every role can do exactly what it allows.
