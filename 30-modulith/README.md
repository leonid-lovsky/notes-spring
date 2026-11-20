# 30 · Modulith

Level: middle. Status: not started.

## Change

The same application split into application modules — users, notes, sharing and notifications. Modules talk to each other only through their public API and application events; an event is stored in the same transaction as the change that caused it and is delivered again until its listener succeeds. Each table belongs to one module; there are no foreign keys across modules.

## Why

In 29 the packages are entangled, and a notification is lost when its listener fails after the sharing was committed.

## Tools

- Spring Modulith: `ApplicationModules.verify()`, `@ApplicationModuleTest`, `@ApplicationModuleListener`, event publication registry, generated module documentation.

## Proof

- The acceptance tests pass.
- The module structure test passes.
- A notification that failed is delivered again after a restart, while the sharing stays committed.
