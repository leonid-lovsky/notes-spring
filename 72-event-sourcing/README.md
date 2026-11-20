# 72 · Event Sourcing

Level: architect. Status: not started.

## Change

The notes service stores the changes of a note as a sequence of events and derives the current state from them; the read models of 71 become projections of these events. The REST API does not change.

## Why

In 71 the service keeps only the current state: the history of a note is lost, and the state at an earlier moment cannot be restored. The stage is about weighing a full history against the complexity of an event store.

## Tools

- An event store written for the stage on the existing storage, projections for reads; no framework outside the Spring portfolio.

## Proof

- The acceptance tests pass.
- The state of a note at any earlier moment is restored from its events.
