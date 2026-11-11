# 22 · Kubernetes

Level: senior. Status: not started.

## Change

The same code runs on Kubernetes instead of Docker Compose. Platform features replace Spring Cloud infrastructure: Services instead of Eureka, ConfigMaps and Secrets instead of the Config Server.

## Why

Starting, configuring and scaling many services by hand with Docker Compose does not scale.

## Tools

- A local k3s cluster, the same one that is meant for Amazon EC2; plain Kubernetes manifests; readiness and liveness probes from Actuator.

## Proof

- The acceptance tests pass.
- A deleted pod comes back on its own.
