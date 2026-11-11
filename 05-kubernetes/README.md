# 05 · Kubernetes

Level: senior. Status: not started.

## Change

The same code on Kubernetes instead of Docker Compose. Platform features replace Spring Cloud infrastructure: Services instead of Eureka, ConfigMaps and Secrets instead of the Config Server.

## Why

Starting, configuring and scaling many services by hand does not scale.

## Tools

- Platform: k3s on Amazon EC2, Horizontal Pod Autoscaler. PostgreSQL, Kafka, Elasticsearch and Redis run in the cluster from Helm charts.
- Delivery: Helm charts for the services; Argo CD (GitOps) deploys the images that GitHub Actions and Jenkins build.
- Infrastructure as code: Terraform describes the whole AWS infrastructure — VPC, EC2, ECR, IAM.
- Load: Gatling.

Amazon EKS is the production alternative; it is not used because its control plane is billed even without load.

## Proof

- The acceptance tests pass.
- A deleted pod comes back on its own.
- The load test report stays within the defined service level objectives.
