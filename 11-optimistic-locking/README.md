# 11 · Optimistic Locking

Level: junior. Status: not started.

## Change

A note that another user has changed since it was opened cannot be saved over that change: the save is refused, and the editor sees the current text.

## Why

In 10 two editors of a shared note overwrite each other's changes without notice, and the change saved first is lost.

## Tools

A version attribute with `@Version` of Jakarta Persistence, a new column added by a Flyway migration, the version as a hidden field of the edit form, and `ObjectOptimisticLockingFailureException` shown as a message on the form.

## Proof

- The page tests pass.
- Of two edits started from the same version, the second save is refused and the first change survives, against every DBMS.
