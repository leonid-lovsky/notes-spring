# 08 · Spring Security

Level: junior. Status: not started.

## Change

Pages require sign-in. Users are stored in the database with BCrypt password hashes and one role, sign in through a form and keep a session.

## Why

In 07 everyone who opens the application can read and change every note.

## Tools

- Spring Security form login with an `HttpSession` and CSRF protection, a `UserDetailsService` over Spring Data JPA, `PasswordEncoder` with BCrypt; test users are inserted by the tests.
- Tests: Spring Security test support for MockMvc.

## Proof

- The page tests pass.
- A page without a session redirects to the sign-in form.
