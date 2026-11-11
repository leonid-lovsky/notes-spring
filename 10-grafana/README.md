# 10 · Grafana

Level: middle. Status: not started.

## Change

The collected metrics are shown on a dashboard.

## Why

In 09 the metrics can only be queried; nobody sees the state of the system at a glance, and the next stages need a dashboard to show their effect.

## Tools

- Grafana in Docker Compose with Prometheus as the data source; the dashboard is stored as code next to the stage.

## Proof

- The acceptance tests pass.
- The dashboard shows request rate, latency and errors of the running stage.
