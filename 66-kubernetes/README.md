# 66 · Kubernetes

Level: senior. Status: not started.

## Change

The services run on Kubernetes instead of Docker Compose.

## Why

In 65 running many services with Compose does not scale.

## Tools

k3s with plain manifests and Actuator probes.

## Proof

- The acceptance tests pass.
- A deleted pod comes back on its own.
