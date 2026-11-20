# notes-spring

This repository shows the evolution of one application from a classic monolith to a distributed, configurable, resilient system. Its purpose is to master Spring technologies and to demonstrate them to employers, following the path from junior to senior and architect with standard tools and without exotic choices.

The application is the same in every stage: notes written in Markdown, users with registration, note sharing with roles, notifications with a live feed over Server-Sent Events, and full-text search of notes. The first stage is a monolith with server-rendered pages; every following stage adds one step — a technology, a pattern or a function — so the functionality grows stage by stage. The HTTP API appears in stage 11 and from then on grows only by addition: behaviour defined in an earlier stage does not change. Stage 33 changes the authentication method once, from HTTP Basic to OAuth 2.0 / OpenID Connect.

The code and the documentation of the previous attempt of the project are kept under the tag `legacy-2026-11` and serve as a reference only.

## Stages

Every stage is a standalone application in its own directory. The first stage is always `01-monolith`; every following stage starts as a copy of the previous one and adds one key step, motivated by a problem of the previous stage. A directory is named after the key technology of its stage, in lower case with hyphens (`github-actions`, `spring-cloud-gateway`, `micrometer-tracing`); a stage whose step is a function or a pattern rather than a technology is named after that step (`multi-user`, `rest-api`, `cqs`, `sagas`, `autoscaling`), and `monolith` and `hexagonal` name the baseline and the architectural style. Patterns and supporting practices — contract tests, sagas, autoscaling, failure tests, page tests — belong to the stage of the step that introduces them. The number of stages is not limited, and the levels may have different numbers of stages. Each stage is described in its `README.md`; the reasoning behind the whole sequence is in `docs/adr/0001-evolution-in-stages.md`.

| Directory | Change | Level |
|---|---|---|
| `01-monolith` | notes in the browser: Gradle, Spring MVC, Thymeleaf, Spring Data JPA on Hibernate, PostgreSQL with `schema.sql`, `@Transactional`, Testcontainers | junior |
| `02-bean-validation` | form validation | junior |
| `03-cqs` | command–query separation in the services | junior |
| `04-markdown` | notes in Markdown | junior |
| `05-flyway` | versioned schema migrations | junior |
| `06-mysql` | MySQL or PostgreSQL by profile, migrations per vendor | junior |
| `07-spring-security` | form login, session, users in the database with one role | junior |
| `08-multi-user` | notes owned by users | junior |
| `09-registration` | sign-up | junior |
| `10-method-security` | note sharing with roles | junior |
| `11-rest-api` | REST API with HTTP Basic and `ProblemDetail`; the acceptance tests start | junior |
| `12-spring-rest-docs` | API documentation from tests | junior |
| `13-openapi` | springdoc-openapi, Swagger UI | junior |
| `14-notifications` | notifications from Spring application events | junior |
| `15-server-sent-events` | live notification feed | junior |
| `16-actuator` | health, info and metrics endpoints | junior |
| `17-docker` | container image with `bootBuildImage` | junior |
| `18-compose` | the whole stage from one Compose file, TLS reverse proxy for EC2 | junior |
| `19-twelve-factor` | configuration from the environment, stateless processes, one image for every environment | junior |
| `20-github-actions` | build and test pipeline | junior |
| `21-jenkins` | second equal pipeline, Configuration as Code | junior |
| `22-aws` | deployment step to AWS (OIDC, ECR, EC2), switched off | junior |
| `23-redis` | cache through Spring Cache | middle |
| `24-spring-session` | session in Redis, two instances | middle |
| `25-redis-pub-sub` | live feed across instances | middle |
| `26-structured-logging` | structured JSON logs | middle |
| `27-prometheus` | metrics collection and alerts | middle |
| `28-grafana` | dashboards | middle |
| `29-loki` | logs collected in Grafana Loki | middle |
| `30-modulith` | module boundaries, event publication registry | middle |
| `31-elasticsearch` | full-text search in a separate read model | middle |
| `32-spring-batch` | rebuild of the search index by a batch job | middle |
| `33-authorization-server` | OAuth2 instead of passwords: `auth-server` on Spring Authorization Server, Resource Server and Client | middle |
| `34-mail` | email confirmation in `auth-server` | middle |
| `35-sms` | phone confirmation in `auth-server` through Amazon SNS | middle |
| `36-mfa` | second factor with Spring Security multi-factor authentication | middle |
| `37-keycloak` | Keycloak instead of `auth-server`, realm as code | middle |
| `38-google-docs` | export of a note to Google Docs | middle |
| `39-spring-cloud-gateway` | single entry point, seam for extracting services | senior |
| `40-token-relay` | browser sign-in at the gateway, token relay, session at the gateway | senior |
| `41-react` | single-page application behind the gateway | senior |
| `42-pwa` | installable application that works offline | senior |
| `43-react-native` | mobile client, public OAuth2 client with PKCE | senior |
| `44-rabbitmq` | notifications as the first separate service: Spring AMQP, event externalization | senior |
| `45-kafka` | Kafka as the alternative broker selected by configuration | senior |
| `46-micrometer-tracing` | tracing across processes with OpenTelemetry | senior |
| `47-openfeign` | every module a service, calls through OpenFeign as an intermediate step | senior |
| `48-http-service-clients` | Spring HTTP service clients instead of OpenFeign | senior |
| `49-spring-cloud-config` | central configuration and encrypted secrets | senior |
| `50-eureka` | service discovery, Spring Cloud LoadBalancer | senior |
| `51-retry` | timeouts and retries with Spring Framework | senior |
| `52-resilience4j` | circuit breakers and bulkheads through Spring Cloud Circuit Breaker | senior |
| `53-rate-limiting` | request rate limiter of the gateway | senior |
| `54-graphql` | combined reads across services next to the unchanged REST API | senior |
| `55-spring-cloud-contract` | consumer-driven contract tests | senior |
| `56-sagas` | sagas over broker events with compensation | senior |
| `57-spring-grpc` | hot internal call over gRPC | senior |
| `58-kubernetes` | k3s (local, ready for EC2), manifests, Spring Boot Kubernetes support | senior |
| `59-autoscaling` | Horizontal Pod Autoscaler | senior |
| `60-graalvm` | native images with Spring AOT | senior |
| `61-ddd` | aggregates, value objects, domain events | architect |
| `62-hexagonal` | storage port and contract test, Clean Architecture; PostgreSQL, MySQL, H2 | architect |
| `63-mongodb` | second storage adapter on Spring Data MongoDB | architect |
| `64-cqrs` | separate read and write models of the notes service | architect |
| `65-event-sourcing` | notes stored as a sequence of events | architect |
| `66-spring-ai` | semantic search with Spring AI, Ollama, Elasticsearch vector store | architect |
| `67-webflux` | second web stack, choice by measurement | architect |

### How a stage is built

The project grows iteratively, smoothly, linearly and atomically. Every milestone is a working application, a separate project and a separate directory; there are no milestones inside a directory. Each stage adds the smallest step that still gives a working result, and a stage that does two things is split. Simple and elementary technologies and patterns come first, complex and advanced ones last; within that order a step comes after the step it builds on, alternatives come next to each other — the first one, then the second one selected by configuration or replacing the first (PostgreSQL and MySQL, GitHub Actions and Jenkins, `auth-server` and Keycloak, RabbitMQ and Kafka, OpenFeign and HTTP service clients) — and a step that needs several processes comes after the system is distributed. Every requirement of the application is a visible stage of its own rather than a detail of another stage. The sequence of directories shows three lines at once: the development of the project, the mastering of technologies, and the roadmap of a developer — junior, middle, senior and architect stages in the order in which a Spring developer usually learns the technologies. The last stages call for an architectural decision based on trade-offs and measurements rather than for one more technology.

Stages are built strictly one after another. Work on a stage starts with a commit that copies the previous directory, so the history shows what the stage changed.

Every stage is ready to be tested, operated and shown to an employer, and reflects one pragmatic step in skills and technology stack — no more than the step needs. A stage is finished when the whole system starts with one command (in 01–16 the application with its database through `bootRun`, in 17 the application image with `docker run`, from 18 the whole stage from the Compose file), the acceptance tests (from 11) and the tests of the stage pass, the `README.md` is complete and the key decisions are recorded in ADRs. A finished stage is frozen: only bugs and vulnerabilities are fixed in it, and Dependabot keeps its dependencies up to date.

Every stage is a standalone Gradle build with its own `settings.gradle` and wrapper, in the Groovy DSL that Spring Initializr generates; there is no root build and no shared `build-logic`. CI follows the same layout: one pipeline per stage, triggered only by changes in the stage directory. The pipeline appears in 20 (GitHub Actions); Jenkins joins in 21 as an equal pipeline with the same steps. Both pipelines call the same Gradle tasks, so the build logic is not duplicated.

Stages 18–57 run with Docker Compose, stages from 58 on Kubernetes (k3s), both locally and in CI. From 22 every stage is ready for deployment on AWS: the Compose files or Kubernetes manifests and the deployment step of the pipelines are in place, but the deployment step stays switched off. It is switched on when the job search starts, because a link to a running system is worth more than a link to code.

### Repository history

The public repository is a portfolio, so its history is clean, tidy and linear. The work happens on the `auth` branch until `01-monolith` is implemented and has been tried on a server. At that point `main` starts a new history from the state of that moment, and the repository gets a `LICENSE`; the previous history, the `auth` branch and the `legacy-2026-11` tag move to a private archive repository and are removed from the public one. This rewrite of public history happens once, only on an explicit request of the user and after the archive has been checked.

From then on the history stays linear: no merge commits, every change is rebased onto `main`, and the repository is configured to require a linear history. Every commit builds and passes the tests, and its message follows Conventional Commits in English (`feat:`, `fix:`, `docs:`, `build:`, `ci:`, `test:`, `refactor:`).

### Acceptance tests

The `acceptance-tests/` directory contains one black-box test suite for every stage from 11, where the REST API appears. It talks to a running system over HTTP only and proves that behaviour defined in an earlier stage does not change while new behaviour is added; only the authentication method changes once, in 33. The tests are grouped by capability — notes, sharing, notifications, live feed, search — and a stage runs the capabilities its API contains. The suite does not check the browser interface — page tests inside the stages do that. It does not know the technologies of the implementation: the framework, the databases, the message broker, the authorization server product, the structure of the system or the number of the stage. It knows only the base URL of the system, the credentials of a test user, the issuer URL for a system that accepts tokens, the capabilities to run, and the public API — HTTP, JSON, Server-Sent Events, OAuth 2.0 / OpenID Connect. Test data is created and verified only through the API; asynchronous behaviour is verified by polling the API. The tests inside a stage — unit, slice, page and integration tests — stay in the stage and check its technologies. Details are in `acceptance-tests/README.md`.

## Decision priorities

Technical decisions follow these priorities:

1. **Spring Boot auto-configuration.** Standard starters and properties are used. Custom bean configuration is allowed only when a task cannot be solved without it, and the reason is stated.
2. **KISS and DRY.** When the principles conflict, the option that keeps auto-configuration and still avoids duplication wins. When both options keep auto-configuration, the simpler one wins.
3. **Current Spring technologies only.** The project is limited to the current Spring portfolio and the features of Spring Boot — Spring Boot SQL initialization, Spring Security multi-factor authentication, Spring Session, Spring REST Docs, Spring Authorization Server, Spring HTTP service clients, Spring Cloud Contract, Spring gRPC, Spring Batch, Spring AMQP, Spring AI, Spring Cloud Config encryption, Spring Cloud Circuit Breaker, Spring AOT, `bootBuildImage`, Spring Boot Kubernetes support. When two technologies are alternatives, the current one is chosen; an outdated or feature-complete technology appears only as an intermediate stage that the next stage replaces, such as Spring Cloud OpenFeign before Spring HTTP service clients. A technology outside the Spring portfolio is used only when matching job postings critically require it or a function of the application cannot work without it: PostgreSQL, MySQL, MongoDB, Testcontainers, Flyway, OpenAPI, Docker and Docker Compose, GitHub Actions, Jenkins, Keycloak, Prometheus, Grafana and Loki, Redis, Elasticsearch, Kafka, RabbitMQ, Kubernetes, GraalVM, AWS, React and React Native, a CommonMark library for Markdown and the Google Docs API for export. Such a technology is introduced after the Spring way of solving the same task, as a separate stage — Flyway after `schema.sql`, OpenAPI after Spring REST Docs, Keycloak after Spring Authorization Server. The set aims to be the complete toolkit of a Spring developer with the widest current outlook.

The plan covers the technologies that Java/Spring job postings require almost always. The set of technologies is chosen pragmatically, but the project stays ready for an alternative choice: every technology is used through a standard abstraction or protocol where one exists — Spring Data, Spring Cache, Spring Modulith event externalization, Micrometer, OpenTelemetry, OAuth 2.0 / OpenID Connect — so replacing it with an alternative is a change of dependencies and settings rather than of code. Technologies that were considered and not included are listed with their reasons in ADR 0001.

A project starts from what its generator produces — Spring Initializr for a stage, `gradle init` for a plain Gradle project — and keeps the default behaviour of its tools. Every known issue of a default is recorded in the documentation — a compromise in ADR 0001, a warning in the "Known warnings" section of the stage `README.md` — and is resolved when it starts to matter.

Copying code between stages is a deliberate exception to DRY: every directory is a snapshot of its stage. Inside a stage DRY applies fully.

Standards — RFCs, specifications, the documentation on docs.spring.io — outweigh tutorials and the user's own proposals; a deviation from a standard is allowed only with a stated reason. Architectural decisions are made by the user: the assistant describes the options and gives a recommendation.

Facts about Spring — whether a starter exists, its name, its version, its status — are checked against Spring Initializr, docs.spring.io and GitHub spring-projects when a stage starts, never from memory and never through the Maven Central search, whose index lags behind.

## Documentation

All documentation, comments and project content are written in correct technical English. Conversation with the user is in Russian.

Documentation is written for its reader: coherent text organised in sections with an introduction, recording the outcome of a decision and its reason, without a chronicle of the discussion, quotations or notes such as "as requested by the user".

Between commits existing records are only edited in place; new records are not added next to them, and a new record appears only after a commit. Until the first stage has working code, decisions are written into the existing records even after a commit, because the plan is still being shaped. For ADRs this means that a decision made after the last commit goes into an existing record, and a new ADR is created only for a decision made after a commit. ADRs live in `docs/adr/`, one decision per file `NNNN-title.md`.

## Code and build

Builds and tests pass without errors and warnings caused by the project's own code and configuration; warnings that a tool emits in its default behaviour are accepted and not suppressed by custom configuration. Indentation is four spaces. Intermediate results go into local variables with clear names instead of nested calls. Abbreviations in identifiers are written in capitals, except `Id`.

Commits and pushes are made only on an explicit request of the user; requests such as "do it", "apply" or "go" are not requests to commit. The Russian command `Зафиксируй` ("commit it") means: synchronise the documentation, commit and push. Every request to commit and push starts with a review of the repository: the assistant first reports what needs attention, if anything, and only after the user has considered those points records in the documentation what the commit requires, commits and pushes. Dates in the documentation are taken from the `date` command.
