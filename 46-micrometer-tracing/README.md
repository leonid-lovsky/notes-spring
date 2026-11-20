# 46 · Micrometer Tracing

Level: senior. Status: not started.

## Change

Requests and events are traced across processes, and every log event carries the identifier of its trace.

## Why

Since 44 a request crosses several processes and a broker, and metrics and logs alone no longer show where time is spent or where a failure happened.

## Tools

- Micrometer Tracing with the OpenTelemetry exporter through Spring Boot auto-configuration; Grafana gains traces next to the metrics of 28 and the logs of 29.

## Proof

- The acceptance tests pass.
- The whole path of a request, through the broker into the notifications service, is visible in a single trace and links to its logs.
