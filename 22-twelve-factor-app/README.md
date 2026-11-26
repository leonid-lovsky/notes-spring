# 22 · Twelve-Factor App

Level: junior. Status: not started.

## Change

Configuration and credentials come from the environment, and one image runs in every environment.

## Why

In 21 the credentials of the databases are in the repository.

## Tools

Spring Boot externalized configuration and graceful shutdown; this closes the compromise recorded in ADR 0001.

## Proof

- The acceptance tests pass.
- No credential is in the repository, and one image runs with two configurations.
