# 20 · Docker

Level: junior. Status: not started.

## Change

The application is packaged as a container image without a hand-written `Dockerfile`.

## Why

In 19 the application runs only from source code.

## Tools

`bootBuildImage` with Cloud Native Buildpacks.

## Proof

- The acceptance tests pass against the image started with `docker run`.
