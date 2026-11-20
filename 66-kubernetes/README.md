# 66 · Kubernetes

Level: senior. Status: not started.

## Change

The same services run on Kubernetes instead of Docker Compose. Eureka and the Config Server run in the cluster like any other service, so the code does not change.

## Why

In 65 starting, configuring and restarting many services by hand with Docker Compose does not scale.

## Tools

- A local k3s cluster, the same one that is meant for Amazon EC2; plain Kubernetes manifests.
- Spring Boot support for Kubernetes: readiness and liveness probes from Actuator, graceful shutdown.
- Both pipelines apply the manifests; the deployment to a cluster on Amazon EC2 stays switched off.

## Proof

- The acceptance tests pass.
- A deleted pod comes back on its own.
