# 29 · Loki

Level: middle. Status: not started.

## Change

The logs of the stage are collected in one place and shown in Grafana next to the metrics.

## Why

In 28 the logs are machine-readable but stay in the containers, and a restarted container loses them.

## Tools

- Grafana Loki in Docker Compose as a data source of Grafana; the way the logs reach Loki is chosen when the stage starts.

## Proof

- The acceptance tests pass.
- The logs of the application can be searched in Grafana after a restart of its container.
