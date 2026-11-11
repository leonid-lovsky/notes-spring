# 19 · AWS Secrets Manager

Level: senior. Status: not started.

## Change

Passwords and keys leave the configuration and come from AWS Secrets Manager.

## Why

In 18 secrets live next to ordinary settings in the configuration repository.

## Tools

- Spring Cloud AWS with AWS Secrets Manager; LocalStack locally and in tests; Terraform from 05 describes the secrets.

## Proof

- The acceptance tests pass.
- No secret is stored in the repository or in the configuration server.
