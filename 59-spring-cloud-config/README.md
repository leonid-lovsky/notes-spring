# 59 · Spring Cloud Config

Level: senior. Status: not started.

## Change

Settings come from a central configuration server, with secrets stored encrypted.

## Why

In 58 every service carries its own settings.

## Tools

Spring Cloud Config with encryption.

## Proof

- The acceptance tests pass.
- A changed setting reaches a running service, and no secret is in plain text.
