# 11 · Method Security

Level: junior. Status: not started.

## Change

A note can be shared with other users in a role — owner, editor or viewer; the role decides what a user may do with the note, and the last owner cannot be removed.

## Why

In 10 a note is visible only to its author; users cannot work on a note together.

## Tools

- Spring Security method security: `@PreAuthorize` on service methods with an authorization bean that checks the role of the current user on the note.

## Proof

- The page tests pass.
- Every role can do exactly what it allows, and the last owner cannot be demoted or removed.
