# 07 · Docker

Level: junior. Status: not started.

## Change

The application is packaged as a container image. The application code does not change.

## Why

In 06 the application runs only from the source code on a developer machine; it cannot be shipped to a server.

## Tools

- Container image built by the Spring Boot Gradle plugin (`bootBuildImage` with Cloud Native Buildpacks), without a hand-written `Dockerfile`; configuration through environment variables.

## Proof

- The acceptance tests pass against the application started from the image with `docker run`.
- The same image runs locally and is ready for Amazon EC2.
