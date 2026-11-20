# 35 · Grafana Loki

Level: middle. Status: not started.

## Change

The structured logs of all instances are collected in one place and shown in Grafana next to the metrics.

## Why

In 34 the logs of 19 are machine-readable but stay in the containers, and a restarted container loses them.

## Tools

- Grafana Loki in the Compose file as a data source of Grafana; the way the logs reach Loki is chosen when the stage starts.

## Proof

- The acceptance tests pass.
- The logs of both instances can be searched in Grafana after a restart of their containers.
