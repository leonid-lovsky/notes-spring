# 05 · Kubernetes

Level: senior. Status: not started.

## Change

The same code on Kubernetes. Platform features replace Spring Cloud infrastructure: Services instead of Eureka, ConfigMaps and Secrets instead of the Config Server.

## Why

Starting, configuring and scaling many services by hand does not scale.

## Tools

k3s, Helm, Argo CD (GitOps), Horizontal Pod Autoscaler, Gatling. Terraform for provisioning is optional.

## Proof

A deleted pod comes back on its own; the load test report stays within the defined service level objectives.
