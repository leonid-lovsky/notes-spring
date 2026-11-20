# 27 · GitHub Actions

Level: junior. Status: not started.

## Change

Every change is built, tested and packaged by a pipeline, which starts the stage and runs the acceptance tests against it.

## Why

In 26 building, testing and packaging depend on a developer running the right commands.

## Tools

- GitHub Actions workflow per stage, triggered by changes in the stage directory, calling Gradle tasks.
- A toolchain repository in `settings.gradle`, so that a clean runner can provision the Java toolchain; this resolves the first known warning of 01. The way Gradle recommends is checked when the stage starts.

## Proof

- The acceptance tests pass in the pipeline.
