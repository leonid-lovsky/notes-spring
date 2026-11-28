# 53 · Mobile Push Notifications

Level: senior. Status: not started.

## Change

Notifications reach the mobile client as push notifications.

## Why

In 52 the mobile client learns about a notification only while it is open.

## Tools

Amazon SNS mobile push, which delivers through Firebase Cloud Messaging and the Apple Push Notification service; replaced by a stub in development and tests, as in 42.

## Proof

- The acceptance tests pass.
- A notification about a shared note reaches a registered device.
