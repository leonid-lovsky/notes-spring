# 03 · Event-driven

Level: middle+. Status: not started.

## Change

Notifications leave the process and become a separate service with its own database. Module events reach it through Kafka.

## Why

Notifications have a different load profile and release cycle than the rest of the application.

## Tools

- Messaging: Spring for Apache Kafka, Spring Modulith event externalization (`spring-modulith-events-kafka`).
- Observability: Micrometer Tracing with OpenTelemetry, Grafana LGTM in Docker Compose.
- CI: GitLab CI joins GitHub Actions and Jenkins.

## Proof

- The acceptance tests pass.
- With the notifications service stopped for a minute, no events are lost or duplicated after it comes back.
- The whole request path is visible in a single trace.
