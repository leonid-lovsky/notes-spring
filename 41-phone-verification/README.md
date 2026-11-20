# 41 · Phone Verification

Level: middle. Status: not started.

## Change

`auth-server` confirms the phone number of an account with a code sent by SMS.

## Why

In 40 the only confirmed channel of a user is email.

## Tools

- Amazon SNS sends the messages; the client library is chosen when the stage starts. Local runs and tests replace the SMS gateway with a stub.

## Proof

- The acceptance tests pass.
- A phone number is confirmed only with the code that was sent to it.
