# 66 · Horizontal Pod Autoscaling

Level: senior. Status: not started.

## Change

The services scale out under load and scale back in when it ends.

## Why

In 65 the number of instances is fixed in the manifests.

## Tools

- Horizontal Pod Autoscaler on metrics from Prometheus; a load test.

## Proof

- The acceptance tests pass.
- Under load the services scale out and scale back in when it ends.
