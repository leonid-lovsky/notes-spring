# 32 · MongoDB

Level: architect. Status: not started.

## Change

A second adapter of the storage port, on Spring Data MongoDB, proves that the port does not depend on SQL.

## Why

In 31 every adapter is relational, so the port has not yet been tested against a different data model.

## Tools

- Spring Data MongoDB, MongoDB in Testcontainers and in the cluster.

## Proof

- The acceptance tests pass.
- The contract test of 31 passes on MongoDB without changes.
