# 16 · In-App Notifications

Level: junior. Status: not started.

## Change

A user is notified when a note was shared with them or their role changed; the API and the pages list the notifications.

## Why

In 15 a user learns about a shared note only by finding it in the list.

## Tools

- Spring application events: the sharing service publishes an event, a `@TransactionalEventListener` stores the notification after the sharing is committed.
- Tests: the acceptance tests of the capability notifications, polling the API.

## Proof

- The acceptance tests pass.
- A failed notification does not roll back the sharing; a notification lost this way is the known gap that 36 closes.
