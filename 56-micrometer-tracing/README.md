# 56 · Micrometer Tracing

Level: senior. Status: not started.

## Change

A trace context crosses processes and the broker, and every log event carries its trace identifier.

## Why

Since 54 the logs of one request are scattered over several processes.

## Tools

Micrometer Tracing with the OpenTelemetry bridge.

## Proof

- The acceptance tests pass.
- The logs of one request are found together by its trace identifier.
