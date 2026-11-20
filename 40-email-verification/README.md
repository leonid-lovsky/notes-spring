# 40 · Email Verification

Level: middle. Status: not started.

## Change

`auth-server` confirms the email address of a new account with a link sent by email; an unconfirmed account cannot sign in.

## Why

In 39 anyone can register with an address they do not own.

## Tools

- Spring Boot mail support in `auth-server`; Mailpit catches the mail in the Compose file and in the tests.

## Proof

- The acceptance tests pass.
- An account signs in only after the link from the email was opened.
