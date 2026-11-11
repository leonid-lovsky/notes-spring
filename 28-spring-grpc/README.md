# 28 · Spring gRPC

Level: senior. Status: not started.

## Change

The most frequent internal call — the check of a user's access rights to a note, made by several services on every request — moves from HTTP to gRPC with a shared Protocol Buffers schema. The external REST API does not change.

## Why

In 27 the access check is the hottest internal call; JSON over HTTP adds latency, and nothing but the contract tests keeps both sides of the call compatible.

## Tools

- Spring gRPC server and client, Protocol Buffers schema shared through a Gradle source set, OAuth2 client credentials on gRPC calls.

## Proof

- The acceptance tests pass.
- The dashboard shows a lower latency of the access check.
- An incompatible change of the schema breaks the build of its clients.
