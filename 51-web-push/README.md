# 51 · Web Push

Level: senior. Status: not started.

## Change

Notifications reach the desktop of a user as system notifications, also when the application is closed.

## Why

In 50 a notification appears on the screen only while a page of the application is open.

## Tools

The Push API and the Notifications API in the service worker of 50; the server sends messages by the Web Push protocol (RFC 8030) with VAPID (RFC 8292) and message encryption (RFC 8291) through a library outside the Spring portfolio, because Spring has no Web Push support.

## Proof

- The acceptance tests pass.
- A subscribed browser shows a system notification about a shared note while the application is closed.
