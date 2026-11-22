# 05 · Command–Query Separation

Level: junior. Status: not started.

## Change

A service method either changes state or returns data, never both.

## Why

In 04 a signature does not tell whether a call has side effects.

## Tools

Command and query methods in the services, read-only transactions for queries.

## Proof

- The page tests pass.
- No query method changes state.
