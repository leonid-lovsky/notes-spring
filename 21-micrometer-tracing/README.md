# 21 · Micrometer Tracing

Level: senior. Status: not started.

## Change

Requests and events are traced across both processes, and logs are collected next to the metrics.

## Why

Since 19 a request crosses several processes and a broker, and metrics alone no longer show where time is spent or where a failure happened.

## Tools

- Micrometer Tracing with the OpenTelemetry exporter through Spring Boot auto-configuration; Grafana from 15 gains traces and logs (Grafana LGTM in Docker Compose).

## Proof

- The acceptance tests pass.
- The whole path of a request, through Kafka into the notifications and audit consumers, is visible in a single trace.
