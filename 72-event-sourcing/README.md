# 72 · Event Sourcing

Level: architect. Status: not started.

## Change

Notes are stored as a sequence of events, and the current state is derived from them.

## Why

In 71 the history of a note is lost.

## Tools

An event store written for the stage on the existing storage.

## Proof

- The acceptance tests pass.
- The state of a note at any earlier moment is restored.
