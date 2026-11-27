# 10 · Method Security

Level: junior. Status: not started.

## Change

A note is shared with other users in a role — owner, editor or viewer; a user has at most one role on a note, and the last owner cannot be demoted or removed.

## Why

In 09 users cannot work on a note together.

## Tools

Spring Security method security with `@PreAuthorize`.

## Proof

- The page tests pass.
- Every role can do exactly what it allows.
