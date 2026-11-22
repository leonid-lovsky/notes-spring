# 12 · In-App Notifications

Level: junior. Status: not started.

## Change

A user is notified when a note is shared with them or their role changes.

## Why

In 11 a user learns about a shared note only by finding it.

## Tools

Spring application events with `@TransactionalEventListener`.

## Proof

- The page tests pass.
- A failed notification does not roll back the sharing; the lost notification is the gap that 37 closes.
