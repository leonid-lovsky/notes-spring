# notes-spring

The evolution of one application — notes with users and sharing — from a classic monolith to a distributed, configurable, resilient system. Each project is a finished, tested and deployed application built on top of the previous one.

| # | Project | How the building blocks communicate |
|---|---|---|
| 01 | [monolith](01-monolith) | direct method calls, one transaction, one database |
| 02 | [modulith](02-modulith) | module APIs and in-process events |
| 03 | [event-driven](03-event-driven) | events through Kafka, first extracted service |
| 04 | [microservices](04-microservices) | HTTP behind a gateway, events, saga |
| 05 | [kubernetes](05-kubernetes) | the same, managed by the platform |
| 06 | [hexagonal](06-hexagonal) | ports and swappable adapters |

Architecture decisions: [docs/adr](docs/adr). History of the previous attempt: [docs/archive](docs/archive) and tag `legacy-2026-11`.
