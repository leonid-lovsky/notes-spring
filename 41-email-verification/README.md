# 41 · Email Verification

Level: middle. Status: not started.

## Change

`auth-server` confirms the email address of a new account.

## Why

In 40 anyone can register with an address they do not own.

## Tools

Spring Boot mail support and Mailpit of 15, now in `auth-server`.

## Proof

- The acceptance tests pass.
- An account signs in only after its address is confirmed.
