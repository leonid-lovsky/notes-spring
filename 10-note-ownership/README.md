# 10 · Note Ownership

Level: junior. Status: not started.

## Change

Every note belongs to the user who created it, and a user sees and changes only their own notes.

## Why

In 09 every signed-in user still sees the notes of all users.

## Tools

- A Flyway migration adds the owner column and assigns the existing notes to an owner; the owner of a new note is taken from the authenticated principal; repository queries are restricted to the current user.

## Proof

- The page tests pass.
- A user cannot open, change or delete another user's note, even by its identifier.
