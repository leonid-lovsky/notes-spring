# 62 · gRPC

Level: senior. Status: not started.

## Change

The hottest internal call, the access check, moves to gRPC.

## Why

In 61 JSON over HTTP adds latency to every request.

## Tools

Spring gRPC.

## Proof

- The acceptance tests pass.
- The latency of the access check drops.
