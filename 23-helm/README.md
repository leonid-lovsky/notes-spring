# 23 · Helm

Level: senior. Status: not started.

## Change

The manifests become Helm charts with values per environment; PostgreSQL, Kafka, Elasticsearch and Redis come from existing charts.

## Why

In 22 the manifests repeat the same structure for every service and every environment.

## Tools

- Helm charts for the services, existing charts for the infrastructure, values files per environment.

## Proof

- The acceptance tests pass.
- The whole system is installed and upgraded with Helm commands.
