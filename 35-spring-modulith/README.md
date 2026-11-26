# 35 · Spring Modulith

Level: middle. Status: not started.

## Change

The application is split into modules that talk through their APIs and events, and an event is delivered again until its listener succeeds.

## Why

In 34 the packages are entangled, and a failed notification is lost.

## Tools

Spring Modulith with the event publication registry.

## Proof

- The acceptance tests pass.
- The module structure is verified, and a failed notification is delivered after a restart.
