# 41 · Multi-Factor Authentication

Level: middle. Status: not started.

## Change

`auth-server` asks for a second factor after the password.

## Why

In 40 a stolen password is enough.

## Tools

Multi-factor authentication of Spring Security.

## Proof

- The acceptance tests pass.
- Sign-in without the second factor issues no token.
