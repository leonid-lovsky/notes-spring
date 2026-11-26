# 71 · Command Query Responsibility Segregation

Level: architect. Status: not started.

## Change

Lists of notes are read from a denormalised read model kept up to date by a projection.

## Why

Since 05 commands and queries are separate methods but share one model.

## Tools

A projection fed by note and sharing events.

## Proof

- The acceptance tests pass.
- A list is served by one query without joins.
