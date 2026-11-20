# 66 · Kubernetes

Level: senior. Status: not started.

## Change

The same code runs on Kubernetes instead of Docker Compose. Platform features replace Spring Cloud infrastructure: Kubernetes Services instead of Eureka, ConfigMaps and Secrets instead of the Config Server.

## Why

In 65 starting, configuring and restarting many services by hand with Docker Compose does not scale.

## Tools

- A local k3s cluster, the same one that is meant for Amazon EC2; plain Kubernetes manifests.
- Spring Boot support for Kubernetes: ConfigMaps and Secrets mounted as files and read through `spring.config.import=configtree:`, readiness and liveness probes from Actuator, graceful shutdown.
- Both pipelines apply the manifests; the deployment to a cluster on Amazon EC2 stays switched off.

## Proof

- The acceptance tests pass.
- A deleted pod comes back on its own.
