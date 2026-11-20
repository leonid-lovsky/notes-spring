# 26 · Structured Logging

Level: middle. Status: not started.

## Change

The application writes its logs to standard output as structured JSON events.

## Why

In 25 the logs are lines of text: a tool cannot filter them by field, and a container keeps them only while it lives.

## Tools

- Spring Boot structured logging, configured through properties.

## Proof

- The acceptance tests pass.
- Every log event is a JSON object with the fields of the chosen format.
