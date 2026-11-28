# 33 · Redis Pub/Sub

Level: middle. Status: not started.

## Change

The live feed and the presence of users on a note of 14 work across instances.

## Why

In 32 a notification or a saved change reaches only the pages connected to its own instance.

## Tools

Redis publish/subscribe through Spring Data Redis, both for the feeds of Server-Sent Events and for the STOMP messages of the simple broker.

## Proof

- The acceptance tests pass.
- A notification from one instance appears in a feed opened on the other.
- A change saved on one instance appears on a page of the note opened on the other.
