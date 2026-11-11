# notes-spring

The evolution of one application — notes with users, sharing and notifications — from a classic monolith to a distributed, configurable, resilient system. Each project is a finished, tested and deployed application; the next one starts as a copy of the previous one and adds exactly one architectural change. Functionality and HTTP API stay the same throughout.

| # | Project | Change | Level |
|---|---|---|---|
| 01 | [monolith](01-monolith) | baseline: layers, one database, one transaction | junior |
| 02 | [modulith](02-modulith) | module boundaries, in-process events | middle |
| 03 | [event-driven](03-event-driven) | notifications as a separate service, Kafka | middle+ |
| 04 | [microservices](04-microservices) | every module a service, Spring Cloud | senior |
| 05 | [kubernetes](05-kubernetes) | the platform replaces Spring Cloud infrastructure | senior |
| 06 | [hexagonal](06-hexagonal) | storage port with swappable database adapters | senior |
| 07 | [reactive](07-reactive) | WebFlux next to Spring MVC for one service | senior |

Architecture decisions: [docs/adr](docs/adr). History of the previous attempt: [docs/archive](docs/archive) and tag `legacy-2026-11`.
