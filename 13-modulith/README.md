# 13 · Modulith

Level: middle. Status: not started.

## Change

The same application split into application modules — users, notes, sharing, notifications and search. Modules talk to each other only through their public API and application events; an event is stored in the same transaction as the change that caused it and is delivered again until its listener succeeds. Each table belongs to one module; there are no foreign keys across modules. Search still uses the PostgreSQL full-text query, now inside its own module.

## Why

In 12 the packages are entangled, and a failure while sending a notification rolls back the sharing of a note. Restructuring the code is cheapest now, before the following stages add integrations that would have to be moved between modules later.

## Tools

- Spring Modulith: `ApplicationModules.verify()`, `@ApplicationModuleTest`, `@ApplicationModuleListener`, event publication registry, generated module documentation.

## Proof

- The acceptance tests pass.
- The module structure test passes.
- A notification that failed is delivered again after a restart, while the sharing stays committed.
