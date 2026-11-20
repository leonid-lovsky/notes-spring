# 13 · Server-Sent Events

Level: junior. Status: not started.

## Change

Notifications reach the user at once through a live feed: the server streams them over Server-Sent Events, and the pages receive them through `EventSource`.

## Why

In 12 a user sees a new notification only after reloading the list.

## Tools

- `SseEmitter` of Spring MVC; the browser `EventSource` API on the pages.

## Proof

- The page tests pass.
- A notification appears in an open feed without a reload.
