# 24 · Argo CD

Level: senior. Status: not started.

## Change

The state of the cluster is described in Git and applied automatically.

## Why

In 23 the cluster is changed by hand with Helm commands.

## Tools

- Argo CD deploys the images that the three pipelines build.
- Terraform from 05 grows to describe the whole AWS infrastructure of the cluster — VPC, EC2 nodes, ECR, IAM.

## Proof

- The acceptance tests pass.
- A change merged into Git reaches the cluster without manual steps.
