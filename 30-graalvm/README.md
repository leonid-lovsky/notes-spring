# 30 · GraalVM

Level: senior. Status: not started.

## Change

The services are built as native images.

## Why

In 29 a service starts in seconds and needs much memory, so the cluster reacts slowly when it has to scale out under a sudden load.

## Tools

- Spring AOT and GraalVM Native Image through `bootBuildImage`; native tests in the pipelines; reflection hints where a library needs them.

## Proof

- The acceptance tests pass.
- A native service starts in a fraction of a second and uses less memory, and the cluster scales out faster on the dashboard.
