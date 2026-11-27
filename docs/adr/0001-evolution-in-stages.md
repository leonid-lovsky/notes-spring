# 0001. Evolution in stages

Status: accepted (2026-11-11)

## Context

The goal is to learn Spring technologies and to demonstrate them to employers on one application that grows from a classic monolith into a distributed, configurable, resilient system. The technologies that Java/Spring job postings almost always require must appear, each in the stage where it solves a real problem. The previous attempt built every variant at once — web stack × data access × DBMS as Gradle modules — and never reached a running application.

## Decision

The project is a sequence of independent applications in one repository, built strictly one after another. The first is an elementary monolith with server-rendered pages; each following stage starts as a copy of the previous one and adds one atomic step — a technology, a pattern or a function — motivated by a problem of the previous stage. Every requirement of the application is a visible stage of its own.

The REST API appears in 13, gets its error format in 14 and is fixed by the acceptance tests in 15. From then on it grows only by addition; only the authentication method changes once, in 37, and GraphQL in 61 adds a second API without changing the REST API. One black-box suite in `acceptance-tests/` runs against every stage from 15 over HTTP only and knows nothing about the implementation.

The sequence follows these rules:

- each stage adds the smallest step that still gives a working result, and a stage that does two things is split;
- simple steps come before advanced ones, and a step comes after the step it builds on — Docker before Docker Compose;
- the Spring way comes before an external technology for the same task — Spring Boot SQL initialization before Flyway, Spring REST Docs before OpenAPI, Spring Authorization Server before Keycloak;
- alternatives stand next to each other — a cache in memory and in Redis, GitHub Actions and Jenkins, RabbitMQ and Apache Kafka, Spring Cloud OpenFeign and HTTP service clients;
- a step that needs several processes comes after the system is distributed, and delivery and monitoring come before distribution (Martin Fowler, "MicroservicePrerequisites");
- a stage is named after the full official name of its key technology, pattern or function, without abbreviations; only `monolith` names the baseline.

The levels follow the roadmap of a Spring developer and have different numbers of stages: junior 01–26 grows the monolith and its delivery, middle 27–42 adds caching, sessions across instances, observability, modules, search and OAuth2, senior 43–66 distributes the system, and architect 67–73 decides on trade-offs and measurements.

The project uses the current Spring portfolio and the features of Spring Boot, with auto-configuration preferred over custom configuration. An outdated or feature-complete technology appears only as an intermediate stage that the next stage replaces. A technology outside the portfolio is used only when job postings critically require it or a function of the application cannot work without it, and always through a standard abstraction or protocol where one exists — Spring Data, Spring Cache Abstraction, Spring Modulith event externalization, Micrometer, OpenTelemetry, OAuth 2.0 / OpenID Connect — so that replacing it is a change of dependencies and settings rather than of code.

The database is designed so that a move to another DBMS, to an identity provider such as Keycloak and to OAuth 2.0 / OpenID Connect stays easy. Names are in lowercase and always quoted — by Hibernate through `hibernate.globally_quoted_identifiers`, backed by `hibernate.auto_quote_keyword`, and in hand-written SQL with the quotes of each DBMS — so that a reserved word such as `user` is a valid name and the table names stay singular; the cost is the quotes in every hand-written statement. Other tables refer to a user only by the application's own key, so that sign-in data can move to an identity provider without changing any reference. Rules about the content of the data are domain rules: the model keeps them in its constructors instead of leaving them to a service or to a setting of a DBMS such as its collation, and the constraints of the schema stay as the last guard. The user schema of Spring Security for `JdbcUserDetailsManager` is the reference for users, and every deviation is recorded with its reason; a rule that no standard defines, such as the characters of a username, is the one that GitHub and Linux both accept.

Open questions, answered when their stage starts:

- how Keycloak confirms a phone number by SMS (41);
- how the second factor works with Spring WebFlux (73), since Spring Security does not support multi-factor authentication in reactive applications; the plan keeps the second factor in the identity provider.

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
- an event sourcing framework — the event store of 71 is written for the stage on the existing storage
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
- the acceptance tests start only in 15; stages 01–14 are checked by page tests
- the prices of Amazon Web Services are checked when the deployment starts

The project starts from what Spring Initializr generates and keeps the default behaviour of its tools. The known compromises, each with the point at which it is revisited:

- the group `com.example` and the package `com.example.notes` are the placeholders of Spring Initializr; they are replaced in all stages at once before the project is published
- the tests run with the default warnings of Mockito and the JVM; a `-javaagent` for Mockito and `-Xshare:off` would remove them, but both are custom build configuration and the warnings do not affect the results
- until the first release the database holds only local test data, so no migrations are used: the schema script always describes the current schema, a change is made in the script and the entities, and the table is dropped by hand to be created again at the next start; migrations come with the first database whose data must be kept, and the script becomes the first migration
- in 01–20 the URLs and the credentials of the local databases are written in the profile file of each DBMS, and in 21 in the Compose file; they leave the repository in 22
- the application runs against a real DBMS server and the tests against the server that Testcontainers starts, so the image of a backing service is declared twice — in 01–20 in the `docker run` command of the stage `README.md` and in the Testcontainers configuration, from 21 in the Compose file and in the Testcontainers configuration — and kept equal by hand until a synchronisation that keeps automatic dependency updates is found; Dependabot does not see an image name in the Java code
- the build reads `spring.profiles.default` from `application.properties`, so that `./gradlew check` runs the tests once against every DBMS while the standard `test` task keeps its default behaviour; reading the application settings from the build is not a standard Gradle practice, and the usual way, one DBMS per run selected by a parameter in a CI matrix, is reconsidered in 24, when the pipeline appears
- the repository ignores the whole `.claude/` directory; the rule is narrowed to `.claude/settings.local.json` when a file is to be shared with Claude Code
