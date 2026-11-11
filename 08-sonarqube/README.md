# 08 · SonarQube

Level: middle. Status: not started.

## Change

All three pipelines pass through one quality gate. The application code does not change.

## Why

In 07 code quality and test coverage are not checked by any pipeline.

## Tools

- SonarQube Cloud analyses the code and the JaCoCo coverage; a failed quality gate stops the pipeline.

## Proof

- The acceptance tests pass.
- Every pipeline stops on a failed quality gate.
