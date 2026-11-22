# 42 · Phone Verification

Level: middle. Status: not started.

## Change

`auth-server` confirms the phone number of an account by SMS.

## Why

In 41 email is the only confirmed channel.

## Tools

Amazon SNS, replaced by a stub in development and tests.

## Proof

- The acceptance tests pass.
- A number is confirmed only with the code sent to it.
