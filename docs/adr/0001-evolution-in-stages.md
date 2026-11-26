# 0001. Evolution in stages

Status: accepted (2026-11-11)

## Context

The goal is to learn Spring technologies and to demonstrate them to employers on one application that grows from a classic monolith into a distributed, configurable, resilient system. The technologies that Java/Spring job postings almost always require must appear, each in the stage where it solves a real problem. The previous attempt built every variant at once — web stack × data access × database vendor as Gradle modules — and never reached a running application.

## Decision

The project is a sequence of independent applications in one repository, built strictly one after another. The first is an elementary monolith with server-rendered pages; each following stage starts as a copy of the previous one and adds one atomic step — a technology, a pattern or a function — motivated by a problem of the previous stage. Every requirement of the application is a visible stage of its own.

The REST API appears in 14, gets its error format in 15 and is fixed by the acceptance tests in 16. From then on it grows only by addition; only the authentication method changes once, in 40, and GraphQL in 64 adds a second API without changing the REST API. One black-box suite in `acceptance-tests/` runs against every stage from 16 over HTTP only and knows nothing about the implementation.

The sequence follows these rules:

- each stage adds the smallest step that still gives a working result, and a stage that does two things is split;
- simple steps come before advanced ones, and a step comes after the step it builds on — Docker before Docker Compose;
- the Spring way comes before an external technology for the same task — `schema.sql` before Flyway, Spring REST Docs before OpenAPI, Spring Authorization Server before Keycloak;
- alternatives stand next to each other — PostgreSQL and MySQL, a cache in memory and in Redis, GitHub Actions and Jenkins, RabbitMQ and Apache Kafka, Spring Cloud OpenFeign and HTTP service clients;
- a step that needs several processes comes after the system is distributed, and delivery and monitoring come before distribution (Martin Fowler, "MicroservicePrerequisites");
- a stage is named after the full official name of its key technology, pattern or function, without abbreviations; only `monolith` names the baseline.

The levels follow the roadmap of a Spring developer and have different numbers of stages: junior 01–29 grows the monolith and its delivery, middle 30–45 adds caching, sessions across instances, observability, modules, search and OAuth2, senior 46–69 distributes the system, and architect 70–76 decides on trade-offs and measurements.

The project uses the current Spring portfolio and the features of Spring Boot, with auto-configuration preferred over custom configuration. An outdated or feature-complete technology appears only as an intermediate stage that the next stage replaces. A technology outside the portfolio is used only when job postings critically require it or a function of the application cannot work without it, and always through a standard abstraction or protocol where one exists — Spring Data, Spring Cache Abstraction, Spring Modulith event externalization, Micrometer, OpenTelemetry, OAuth 2.0 / OpenID Connect — so that replacing it is a change of dependencies and settings rather than of code.

Open questions, answered when their stage starts:

- how Keycloak confirms a phone number by SMS (44);
- how the second factor works with Spring WebFlux (76), since Spring Security does not support multi-factor authentication in reactive applications; the plan keeps the second factor in the identity provider.

Considered and not included, because they are outside the current Spring portfolio and not critical for job postings, or duplicate a tool already in the plan:

- MapStruct and Lombok — records with hand-written mapping cover the application
- Spring Java Format — it was tried and imposes the fixed style of Spring Framework, which does not allow the code style of the project
- Liquibase — Flyway covers migrations
- SonarQube and JaCoCo — static analysis and coverage are not a Spring concern
- Gatling — load is generated in the stages that need a measurement
- Terraform, Argo CD and Helm — the deployment is postponed, and plain Kubernetes manifests cover one environment
- HashiCorp Vault and AWS Secrets Manager — Spring Cloud Config encryption covers secrets without tying the project to one vendor
- Toxiproxy — failure tests stop or slow down containers through Testcontainers
- Spring Cloud Stream — Spring Modulith event externalization already decouples the code from the broker
- WebSocket — Server-Sent Events cover the one-way live feed
- the ELK stack — Grafana with Grafana Loki covers logs
- Maven and GitLab CI — Gradle and two pipelines cover the build
- a hand-written `Dockerfile` — `bootBuildImage` builds layered images without one
- an event sourcing framework — the event store of 74 is written for the stage on the existing storage
- managed services that cost money without load — Amazon RDS, Amazon MSK, Amazon EKS — are named as production alternatives but not used

## Consequences

Benefits:

- every stage is a finished application, ready to be run, operated and shown to an employer
- the difference between two neighbouring directories shows one step of the evolution, and the directories read as a path from junior to architect
- every critically required technology is backed by working code, so the job search can start after the junior stages
- every stage is small enough to be finished without a long break

Costs:

- code is duplicated between stages on purpose, so the repository is large, and frozen stages still need dependency updates
- some stages change only the build or the infrastructure
- the acceptance tests start only in 16; stages 01–15 are checked by page tests
- the prices of Amazon Web Services are checked when the deployment starts

The project starts from what Spring Initializr generates and keeps the default behaviour of its tools. The known compromises, each with the point at which it is revisited:

- the group `com.example` and the package `com.example.notes` are the placeholders of Spring Initializr; they are replaced in all stages at once before the project is published
- the tests run with the default warnings of Mockito and the JVM; a `-javaagent` for Mockito and `-Xshare:off` would remove them, but both are custom build configuration and the warnings do not affect the results
- until the first release the database holds only local test data, so no migrations are used: the schema script always describes the current schema, a change is made in the script and the entities, and the table is dropped by hand to be created again at the next start; migrations come with the first database whose data must be kept, and the script becomes the first migration
- in 01–21 the URL and the credentials of the local database are written in `application.properties`, and in 22–24 in the Compose file; they leave the repository in 25
- the application runs against its real database and the tests against the database that Testcontainers starts, so the image of a backing service is declared twice — in 01–21 in the `docker run` command of the stage `README.md` and in the Testcontainers configuration, from 22 in the Compose file and in the Testcontainers configuration — and kept equal by hand until a synchronisation that keeps automatic dependency updates is found; Dependabot does not see an image name in the Java code
- the repository ignores the whole `.claude/` directory; the rule is narrowed to `.claude/settings.local.json` when a file is to be shared with Claude Code
