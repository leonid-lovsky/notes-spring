# 42 · Multi-Factor Authentication

Level: middle. Status: not started.

## Change

`auth-server` asks for a second factor after the password.

## Why

In 41 a stolen password is enough to take over an account.

## Tools

- Multi-factor authentication of Spring Security 7 (`@EnableMultiFactorAuthentication`) with a one-time token as the second factor, delivered through the channels of 40 and 41.

## Proof

- The acceptance tests pass.
- Sign-in without the second factor does not issue a token.
