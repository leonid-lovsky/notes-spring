# 70 · Kubernetes Service Discovery and Configuration

Level: senior. Status: not started.

## Change

Kubernetes Services, ConfigMaps and Secrets replace Eureka and Spring Cloud Config.

## Why

In 69 the cluster provides discovery and configuration twice.

## Tools

Kubernetes Services and `configtree:` imports.

## Proof

- The acceptance tests pass.
- No Eureka and no Config Server run in the cluster.
