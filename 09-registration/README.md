# 09 · Registration

Level: junior. Status: not started.

## Change

A visitor creates an account through a sign-up form and signs in with it.

## Why

In 08 users exist only as test data; the application cannot get new users.

## Tools

- A sign-up form with Bean Validation, a unique user name enforced by the database, the password hashed with the `PasswordEncoder` of 07.

## Proof

- The page tests pass.
- A new account signs in, and a taken user name is rejected with a message.
