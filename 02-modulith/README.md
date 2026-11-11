# 02 · Modulith

Level: middle. Status: not started.

## Change

The same application split into application modules — users, notes, sharing, notifications and search. Modules talk to each other only through their public API and application events. Each table belongs to one module; there are no foreign keys across modules.

## Why

In 01 the packages are entangled, a failure while sending a notification rolls back the sharing of a note, and search in PostgreSQL has neither relevance ranking nor typo tolerance.

## Tools

- Modules: Spring Modulith — `ApplicationModules.verify()`, `@ApplicationModuleTest`, `@ApplicationModuleListener`, event publication registry.
- Search: the search module indexes notes into Elasticsearch (Spring Data Elasticsearch) from note events. Elasticsearch runs in Docker Compose and in Testcontainers.
- CI: Jenkins joins GitHub Actions as an equal pipeline — declarative `Jenkinsfile`, Jenkins on Amazon EC2 configured with Configuration as Code. Both pipelines run the same Gradle tasks: build, test, image to Amazon ECR, deployment to Amazon EC2, acceptance tests.

## Proof

- The acceptance tests pass.
- The module structure test passes.
- A notification that failed is delivered again after a restart, while the sharing stays committed.
- After Elasticsearch was down, the index catches up with all changes.
