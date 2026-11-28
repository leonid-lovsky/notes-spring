# 14 · WebSocket

Level: junior. Status: not started.

## Change

Users who have a note open see who else has it open and who is editing it, and a saved change appears on their pages at once.

## Why

In 13 the server can only push to the browser, so an editor learns that someone else is working on the same note only when 11 refuses the save.

## Tools

Spring WebSocket with STOMP messaging: `@MessageMapping`, `SimpMessagingTemplate` and the simple broker in memory, secured by the message security of Spring Security; a STOMP client in the browser. The optimistic locking of 11 stays the guard of the data.

## Proof

- The page tests pass.
- A second user sees that a note is being edited and receives the saved text without a reload.
- A user who closes the page disappears from the users of the note.
