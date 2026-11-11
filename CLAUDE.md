# notes-spring

This repository shows the evolution of one application from a classic monolith to a distributed, configurable, resilient system. Its purpose is to master Spring technologies and to demonstrate them to employers, following the path from junior to senior and architect with standard tools and without exotic choices.

The application is the same in every stage: users, notes, note sharing with roles, notifications with a live feed over Server-Sent Events, and full-text search of notes. Its functionality and HTTP API are fixed in the first stage; stage 11 changes the authentication method once, from HTTP Basic to OAuth 2.0 / OpenID Connect, and the contract does not change afterwards. Later stages change only the internal structure and the infrastructure around the same contract.

The code and the documentation of the previous attempt of the project are kept under the tag `legacy-2026-11` and serve as a reference only.

## Stages

Every stage is a standalone application in its own directory. The first stage is always `01-monolith`; every following stage starts as a copy of the previous one and adds one key technology, motivated by a problem of the previous stage. A directory is named after the key technology of its stage, in lower case with hyphens (`github-actions`, `spring-cloud-gateway`, `micrometer-tracing`); the only exceptions are `monolith`, the baseline, and `hexagonal`, an architectural style. When several technologies matter, the stage is split, one key technology per stage. Patterns and supporting practices — sagas, contract tests, autoscaling, failure tests, page tests — belong to the stage of the technology that implements them. The number of stages is kept even. Each stage is described in its `README.md`; the reasoning behind the whole sequence is in `docs/adr/0001-evolution-in-stages.md`.

| Directory | Change | Key tools | Level |
|---|---|---|---|
| `01-monolith` | minimum viable product: REST API | Gradle, Spring MVC, Spring Data JPA, PostgreSQL with `schema.sql`, Spring Security (HTTP Basic), Bean Validation, `ProblemDetail`, Testcontainers | junior |
| `02-flyway` | versioned schema migrations | Flyway | junior |
| `03-spring-rest-docs` | API documentation from tests | Spring REST Docs, Asciidoctor | junior |
| `04-openapi` | machine-readable API description | springdoc-openapi, Swagger UI | junior |
| `05-actuator` | health, info and metrics endpoints | Spring Boot Actuator | junior |
| `06-thymeleaf` | browser interface | Thymeleaf, form login, CSRF, `EventSource` | junior |
| `07-docker` | container image | `bootBuildImage` (Cloud Native Buildpacks) | junior |
| `08-compose` | the whole stage from one Compose file | Docker Compose, TLS reverse proxy for EC2 | junior |
| `09-github-actions` | build and test pipeline | GitHub Actions; deployment step to AWS (OIDC, ECR, EC2) written and switched off | junior |
| `10-redis` | cache | Redis, Spring Cache | junior |
| `11-oauth2` | OAuth2 instead of passwords | OAuth2 Resource Server and Client, `auth-server` on Spring Authorization Server | middle |
| `12-keycloak` | Keycloak instead of `auth-server` | Keycloak realm as code, unchanged Spring Security | middle |
| `13-modulith` | module boundaries, in-process events | Spring Modulith, event publication registry | middle |
| `14-prometheus` | metrics collection and alerts | Prometheus, Micrometer | middle |
| `15-grafana` | dashboards | Grafana | middle |
| `16-elasticsearch` | search engine | Elasticsearch, search module fed by events | middle |
| `17-spring-batch` | rebuild of the search index by a batch job | Spring Batch | middle |
| `18-jenkins` | second equal pipeline | Jenkins, Configuration as Code | middle |
| `19-rabbitmq` | notifications as a separate service | Spring AMQP, RabbitMQ, Spring Modulith event externalization | middle |
| `20-kafka` | events as a replayable log | Kafka, Spring Modulith event externalization | middle |
| `21-micrometer-tracing` | tracing and logs | Micrometer Tracing with OpenTelemetry, Grafana LGTM | senior |
| `22-spring-cloud-gateway` | single entry point, seam for extracting services | Spring Cloud Gateway, token relay, session in Redis | senior |
| `23-http-service-clients` | every module a service | Spring HTTP service clients, client credentials, Spring Cloud Contract, sagas over Kafka, `web` service | senior |
| `24-graphql` | combined reads across services | Spring for GraphQL next to the unchanged REST API | senior |
| `25-spring-cloud-config` | central configuration and encrypted secrets | Spring Cloud Config | senior |
| `26-eureka` | service discovery | Eureka, Spring Cloud LoadBalancer | senior |
| `27-resilience4j` | resilient calls | Resilience4j through Spring Cloud Circuit Breaker, rate limiting | senior |
| `28-spring-grpc` | hot internal call over gRPC | Spring gRPC, Protocol Buffers | senior |
| `29-kubernetes` | the platform replaces Spring Cloud infrastructure, autoscaling | k3s (local, ready for EC2), manifests, Spring Boot Kubernetes support, Horizontal Pod Autoscaler | senior |
| `30-graalvm` | native images | Spring AOT, GraalVM Native Image | senior |
| `31-hexagonal` | storage port, database chosen by profile | port and contract test; PostgreSQL, MySQL, H2 | architect |
| `32-mongodb` | second storage adapter | Spring Data MongoDB | architect |
| `33-spring-ai` | semantic search | Spring AI, Ollama, Elasticsearch vector store | architect |
| `34-webflux` | second web stack, choice by measurement | WebFlux, reactive MongoDB, Server-Sent Events | architect |

### How a stage is built

The project grows iteratively, smoothly and as evenly as possible. Every milestone is a working application, a separate project and a separate directory; there are no milestones inside a directory. The sequence of directories shows three lines at once: the development of the project, the mastering of technologies, and the roadmap of a developer — ten stages of a junior, ten of a middle, ten of a senior and the final stages of an architect, in the order in which a Spring developer usually learns the technologies. Each stage can be learned and brought to a working result without a long break; a stage noticeably larger than its neighbours is split. The last stages call for an architectural decision based on trade-offs and measurements rather than for one more technology.

Stages are built strictly one after another. Work on a stage starts with a commit that copies the previous directory; every change after that is a separate commit, so the history reads as a step-by-step transition.

Every stage is ready to be tested, operated and shown to an employer, and reflects one pragmatic step in skills and technology stack — no more than the step needs. A stage is finished when the whole system starts with one command (in 01–06 the application with its database through `bootRun`, in 07 the application image with `docker run`, from 08 the whole stage from the Compose file), the acceptance tests and the tests of the stage pass, the `README.md` is complete and the key decisions are recorded in ADRs. A finished stage is frozen: only bugs and vulnerabilities are fixed in it, and Dependabot keeps its dependencies up to date.

Every stage is a standalone Gradle build with its own `settings.gradle.kts` and wrapper; there is no root build and no shared `build-logic`. CI follows the same layout: one pipeline per stage, triggered only by changes in the stage directory. The pipeline appears in 09 (GitHub Actions); Jenkins joins in 18 as an equal pipeline with the same steps. Both pipelines call the same Gradle tasks, so the build logic is not duplicated.

Stages 08–28 run with Docker Compose, stages from 29 on Kubernetes (k3s), both locally and in CI. From 09 every stage is ready for deployment on AWS: the Compose files or Kubernetes manifests and the deployment step of the pipelines are in place, but the deployment step stays switched off. It is switched on when the job search starts, because a link to a running system is worth more than a link to code.

### Repository history

The public repository is a portfolio, so its history is clean, tidy and linear. The work happens on the `auth` branch until `01-monolith` is implemented and has been tried on a server. At that point `main` starts a new history from the state of that moment, and the repository gets a `LICENSE`; the previous history, the `auth` branch and the `legacy-2026-11` tag move to a private archive repository and are removed from the public one. This rewrite of public history happens once, only on an explicit request of the user and after the archive has been checked.

From then on the history stays linear: no merge commits, every change is rebased onto `main`, and the repository is configured to require a linear history. Commits are small and self-contained, each builds and passes the tests, and their messages follow Conventional Commits in English (`feat:`, `fix:`, `docs:`, `build:`, `ci:`, `test:`, `refactor:`). A stage starts with a commit that copies the previous stage and continues with one commit per change, so the history reads as the evolution itself.

### Acceptance tests

The `acceptance-tests/` directory contains one black-box test suite for every stage. It talks to a running system over HTTP only and proves that the behaviour defined in 01 does not change; only the authentication method changes once, in 11. The suite does not check the browser interface of 06 — page tests inside the stages do that. It does not know the technologies of the implementation: the framework, the databases, the message broker, the authorization server product, the structure of the system or the number of the stage. It knows only the base URL of the system, the credentials of a test user, the issuer URL for a system that accepts tokens, and the public API — HTTP, JSON, Server-Sent Events, OAuth 2.0 / OpenID Connect. Test data is created and verified only through the API; asynchronous behaviour is verified by polling the API. The tests inside a stage — unit, slice and integration tests — stay in the stage and check its technologies. Details are in `acceptance-tests/README.md`.

## Decision priorities

Technical decisions follow these priorities:

1. **Spring Boot auto-configuration.** Standard starters and properties are used. Custom bean configuration is allowed only when a task cannot be solved without it, and the reason is stated.
2. **KISS and DRY.** When the principles conflict, the option that keeps auto-configuration and still avoids duplication wins. When both options keep auto-configuration, the simpler one wins.
3. **Current Spring technologies only.** The project is limited to the current Spring portfolio and the features of Spring Boot — Spring Boot SQL initialization, Spring REST Docs, Spring Authorization Server, Spring HTTP service clients, Spring gRPC, Spring Batch, Spring AMQP, Spring AI, Spring Cloud Config encryption, Spring Cloud Circuit Breaker, Spring AOT, `bootBuildImage`, Spring Boot Kubernetes support. When two technologies are alternatives, the current one is chosen; outdated or feature-complete technologies are not used, such as Spring Cloud OpenFeign, which Spring itself replaces with HTTP service clients. A technology outside the Spring portfolio is used only when matching job postings critically requires it: PostgreSQL, MongoDB, Testcontainers, Flyway, OpenAPI, Docker and Docker Compose, GitHub Actions, Jenkins, Keycloak, Prometheus, Grafana, Redis, Elasticsearch, Kafka, RabbitMQ, Kubernetes, GraalVM and AWS. Such a technology is introduced after the Spring way of solving the same task, as a separate stage — Flyway after `schema.sql`, OpenAPI after Spring REST Docs, Keycloak after Spring Authorization Server. The set aims to be the complete toolkit of a Spring developer with the widest current outlook.

The plan covers the technologies that Java/Spring job postings require almost always. The set of technologies is chosen pragmatically, but the project stays ready for an alternative choice: every technology is used through a standard abstraction or protocol where one exists — Spring Data, Spring Cache, Spring Modulith event externalization, Micrometer, OpenTelemetry, OAuth 2.0 / OpenID Connect — so replacing it with an alternative is a change of dependencies and settings rather than of code. Technologies that were considered and not included are listed with their reasons in ADR 0001.

Copying code between stages is a deliberate exception to DRY: every directory is a snapshot of its stage. Inside a stage DRY applies fully.

Standards — RFCs, specifications, the documentation on docs.spring.io — outweigh tutorials and the user's own proposals; a deviation from a standard is allowed only with a stated reason. Architectural decisions are made by the user: the assistant describes the options and gives a recommendation.

Facts about Spring — whether a starter exists, its name, its version, its status — are checked against Spring Initializr, docs.spring.io and GitHub spring-projects when a stage starts, never from memory and never through the Maven Central search, whose index lags behind.

## Documentation

All documentation, comments and project content are written in correct technical English. Conversation with the user is in Russian.

Documentation is written for its reader: coherent text organised in sections with an introduction, recording the outcome of a decision and its reason, without a chronicle of the discussion, quotations or notes such as "as requested by the user".

Between commits existing records are only edited in place; new records are not added next to them, and a new record appears only after a commit. Until the first stage has working code, decisions are written into the existing records even after a commit, because the plan is still being shaped. For ADRs this means that a decision made after the last commit goes into an existing record, and a new ADR is created only for a decision made after a commit. ADRs live in `docs/adr/`, one decision per file `NNNN-title.md`.

## Code and build

Builds and tests pass without errors and warnings. Indentation is four spaces. Intermediate results go into local variables with clear names instead of nested calls. Abbreviations in identifiers are written in capitals, except `Id`.

Commits and pushes are made only on an explicit request of the user; requests such as "do it", "apply" or "go" are not requests to commit. The Russian command `Зафиксируй` ("commit it") means: synchronise the documentation, commit and push. Dates in the documentation are taken from the `date` command.
