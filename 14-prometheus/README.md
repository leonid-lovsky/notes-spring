# 14 · Prometheus

Level: middle. Status: not started.

## Change

The metrics that Actuator already exposes are collected by Prometheus. The application code changes only by the dependency that exports metrics in Prometheus format.

## Why

In 13 the metrics exist but nobody collects them. A system needs basic monitoring before it is distributed (Martin Fowler, "MicroservicePrerequisites").

## Tools

- Micrometer with the Prometheus registry on the Actuator endpoint of 05.
- Prometheus in Docker Compose with scrape configuration and alerting rules.

## Proof

- The acceptance tests pass.
- Prometheus scrapes the running stage, and an alert fires when the error rate exceeds its threshold.
