# 14 · OpenTelemetry

Level: middle+. Status: not started.

## Change

Requests and events are traced across both processes, and logs are collected next to the metrics.

## Why

Since 13 a request crosses two processes and Kafka, and metrics alone no longer show where time is spent or where a failure happened.

## Tools

- Micrometer Tracing with OpenTelemetry; Grafana from 10 gains traces and logs (Grafana LGTM in Docker Compose).

## Proof

- The acceptance tests pass.
- The whole path of a request, through Kafka into the notifications service, is visible in a single trace.
