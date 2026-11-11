# 03 · Event-driven

Level: middle+. Status: not started.

## Change

Notifications leave the process and become a separate service with its own database; module events are published to Kafka.

## Why

Notifications have a different load profile and release cycle than the rest of the application.

## Tools

Spring for Apache Kafka, Spring Modulith event externalization (`spring-modulith-events-kafka`), Micrometer Tracing with OpenTelemetry, Grafana LGTM in Docker Compose. CI: GitLab CI in addition to GitHub Actions.

## Proof

With the consumer stopped for a minute, no events are lost or duplicated after it comes back; the whole request path is visible in a single trace.
