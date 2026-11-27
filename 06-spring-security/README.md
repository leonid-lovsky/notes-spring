# 06 · Spring Security

Level: junior. Status: not started.

## Change

Pages require sign-in through a form; the users of 01 get one role and a BCrypt password hash in the database.

## Why

In 05 everyone can read and change every note.

## Tools

Spring Security form login, a `UserDetailsService` over Spring Data JPA.

## Proof

- The page tests pass.
- A page without a session redirects to the sign-in form.
