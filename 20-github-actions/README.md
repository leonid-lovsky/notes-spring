# 20 · GitHub Actions

Level: junior. Status: not started.

## Change

Every change is built, tested and packaged by a pipeline, which starts the stage and runs the acceptance tests against it.

## Why

In 19 building, testing and packaging depend on a developer running the right commands.

## Tools

- GitHub Actions workflow per stage, triggered by changes in the stage directory, calling Gradle tasks.

## Proof

- The acceptance tests pass in the pipeline.
