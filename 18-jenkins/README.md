# 18 · Jenkins

Level: middle. Status: not started.

## Change

Jenkins joins GitHub Actions as an equal pipeline with the same steps. The application code does not change.

## Why

In 17 the whole delivery depends on GitHub Actions, while many companies build with Jenkins.

## Tools

- Declarative `Jenkinsfile`; Jenkins in a container (locally for now, on Amazon EC2 once deployment starts), configured with Configuration as Code.
- Both pipelines call the same Gradle tasks, so the build logic is not duplicated.

## Proof

- The acceptance tests pass.
- Both pipelines build, test and run the same application with the same steps.
