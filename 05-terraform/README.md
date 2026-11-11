# 05 · Terraform

Level: middle. Status: not started.

## Change

The AWS environment of the stage is described as code. The application code does not change.

## Why

In 04 the AWS resources would have to be created by hand. A system needs rapid provisioning before it is distributed (Martin Fowler, "MicroservicePrerequisites").

## Tools

- Terraform describes the AWS resources — EC2, ECR, security group, IAM role for GitHub Actions (OIDC).
- GitHub Actions runs `terraform fmt`, `validate` and `plan`.

## Proof

- The acceptance tests pass.
- `terraform validate` and `terraform plan` succeed; `terraform apply` is the only step left to create the AWS environment.
