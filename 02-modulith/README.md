# 02 · Modulith

Level: middle. Status: not started.

## Change

The same application split into application modules — users, notes, sharing, notifications. Modules talk to each other only through their public API and application events; tables belong to one module, no foreign keys across modules.

## Why

In 01 the packages are entangled, and a failure while sending a notification rolls back the sharing of a note.

## Tools

Spring Modulith: `ApplicationModules.verify()`, `@ApplicationModuleTest`, `@ApplicationModuleListener`, event publication registry.

## Proof

The module structure test passes; a notification that failed is delivered again after restart while the sharing stays committed.
