# 49 · Collaborative Editing

Level: senior. Status: not started.

## Change

Several editors type in one note at the same time, and their changes merge character by character without conflicts.

## Why

In 48 a note is edited by one user at a time, and a save made in parallel is refused.

## Tools

Yjs, a conflict-free replicated data type (CRDT), in the editor of the single-page application; the server relays the updates over the WebSocket endpoint of 14 through the gateway and stores the merged text of the note.

## Proof

- The acceptance tests pass.
- Two editors who type at the same time see the same text.
- The note keeps the merged text after both editors close it.
