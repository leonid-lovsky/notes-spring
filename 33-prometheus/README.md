# 33 · Prometheus

Level: middle. Status: not started.

## Change

Prometheus collects the metrics of all instances and raises alerts.

## Why

In 32 nobody collects the metrics, and a system needs monitoring before it is distributed.

## Tools

Micrometer with the Prometheus registry.

## Proof

- The acceptance tests pass.
- An alert fires when the error rate exceeds its threshold.
