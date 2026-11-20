# 19 · Structured Logging

Level: junior. Status: not started.

## Change

The application writes its logs to standard output as structured JSON events.

## Why

In 18 the logs are lines of text that a tool cannot filter by field.

## Tools

- Spring Boot structured logging, configured through properties.

## Proof

- The acceptance tests pass.
- Every log event is a JSON object with the fields of the chosen format.
