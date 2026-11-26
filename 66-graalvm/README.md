# 66 · GraalVM

Level: senior. Status: not started.

## Change

The services are built as native images.

## Why

In 65 slow startup delays scaling.

## Tools

GraalVM Native Image through Spring AOT and `bootBuildImage`.

## Proof

- The acceptance tests pass.
- A native service starts faster and uses less memory.
