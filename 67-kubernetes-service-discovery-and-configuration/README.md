# 67 · Kubernetes Service Discovery and Configuration

Level: senior. Status: not started.

## Change

Platform features replace Spring Cloud infrastructure: services find each other through Kubernetes Services instead of Eureka, and their settings come from ConfigMaps and Secrets instead of the Config Server. Eureka and the Config Server leave the system.

## Why

In 66 the cluster runs service discovery and configuration twice: Kubernetes provides both, and Eureka and the Config Server repeat them as services of their own.

## Tools

- Kubernetes Services addressed by their DNS names; ConfigMaps and Secrets mounted as files and read through `spring.config.import=configtree:`; the encrypted values of 59 move to Kubernetes Secrets.

## Proof

- The acceptance tests pass.
- No Eureka server and no Config Server run in the cluster, and a changed ConfigMap reaches a service.
