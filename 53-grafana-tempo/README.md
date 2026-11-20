# 53 · Grafana Tempo

Level: senior. Status: not started.

## Change

The traces are exported to Grafana Tempo and shown in Grafana next to the metrics of 34 and the logs of 35; a trace links to its logs.

## Why

In 52 the trace identifiers link the logs, but the timing of each step of a request is not recorded anywhere, so it is still unknown where the time is spent.

## Tools

- The OpenTelemetry exporter of Spring Boot, Grafana Tempo in the Compose file as a data source of Grafana.

## Proof

- The acceptance tests pass.
- The whole path of a request, through the broker into the notifications service, is visible in a single trace with the duration of every step.
