# 25 · Autoscaling

Level: senior. Status: not started.

## Change

The services scale with load, and the system has measured service level objectives.

## Why

In 24 the number of instances is fixed, and nobody knows how much load the system can take.

## Tools

- Horizontal Pod Autoscaler on metrics from Prometheus.
- Gatling load tests against defined service level objectives.

## Proof

- The acceptance tests pass.
- Under load the services scale out, and the load test report stays within the service level objectives.
