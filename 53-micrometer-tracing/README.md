# 53 · Micrometer Tracing

Level: senior. Status: not started.

## Change

Requests and events carry a trace context across processes and the broker, and every log event carries the identifiers of its trace and span, so the logs of one request can be found together in Grafana Loki.

## Why

Since 51 a request crosses several processes and a broker, and the logs of one request are scattered over several services with nothing that links them.

## Tools

- Micrometer Tracing with the OpenTelemetry bridge through Spring Boot auto-configuration; trace identifiers in the structured logs of 20.

## Proof

- The acceptance tests pass.
- The logs of one request in all services are found in Grafana Loki by its trace identifier.
