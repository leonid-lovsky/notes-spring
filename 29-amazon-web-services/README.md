# 29 · Amazon Web Services

Level: junior. Status: not started.

## Change

Both pipelines get a deployment step: the image goes to Amazon ECR and the stage is deployed to Amazon EC2 behind NGINX. The step is written but stays switched off until the job search starts.

## Why

In 28 the pipelines prove that the stage works but cannot put it in front of anyone.

## Tools

- GitHub Actions authenticates to Amazon Web Services through OIDC with a least-privilege IAM role; Jenkins uses the same role; Amazon ECR and Amazon EC2.

## Proof

- The acceptance tests pass.
- The deployment step needs only to be switched on.
