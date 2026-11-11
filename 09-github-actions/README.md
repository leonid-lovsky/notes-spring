# 09 · GitHub Actions

Level: junior. Status: not started.

## Change

Every change is built, tested and packaged by a pipeline, which starts the stage and runs the acceptance tests against it. The deployment job — authentication to AWS through OIDC, image to Amazon ECR, deployment to Amazon EC2 — is written but stays switched off until the job search starts.

## Why

In 08 building, testing and packaging depend on a developer running the right commands.

## Tools

- GitHub Actions workflow per stage, triggered by changes in the stage directory, calling Gradle tasks; the deployment job authenticates to AWS through OIDC.

## Proof

- The acceptance tests pass in the pipeline.
- The deployment job needs only to be switched on.
