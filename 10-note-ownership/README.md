# 10 · Note Ownership

Level: junior. Status: not started.

## Change

Every note belongs to the user who created it, and a user works only with their own notes.

## Why

In 09 every user sees the notes of all users.

## Tools

A Flyway migration adds the owner; queries are restricted to the current user.

## Proof

- The page tests pass.
- A user cannot open, change or delete another user's note.
