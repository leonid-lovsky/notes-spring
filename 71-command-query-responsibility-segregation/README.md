# 71 · Command Query Responsibility Segregation

Level: architect. Status: not started.

## Change

Reads and writes of notes are separated (CQRS): the commands keep the normalised model, and the lists of notes are read from a denormalised read model — a note with its role for the user and the name of its owner in one row — which a projection keeps up to date from note and sharing events.

## Why

Since 04 commands and queries are separate methods, but both still work on the same model: every list of notes joins notes and access rights on every request, while lists are read far more often than notes change. The search index of 37 is already a separate read model for one query; this stage separates the models of the notes service itself.

## Tools

- A read model in the notes service, a projection fed by note and sharing events through the broker; idempotent projection updates.

## Proof

- The acceptance tests pass.
- A list of notes is served by one query without joins.
- After a failed projection the read model catches up with all changes.
