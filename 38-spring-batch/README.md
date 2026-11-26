# 38 · Spring Batch

Level: middle. Status: not started.

## Change

A batch job rebuilds the search index from the database and restarts from the last committed chunk.

## Why

In 37 a lost index cannot be rebuilt.

## Tools

Spring Batch.

## Proof

- The acceptance tests pass.
- The job rebuilds an empty index and continues after an interruption.
