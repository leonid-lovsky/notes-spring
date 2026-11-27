# notes-spring

This repository shows the evolution of one application from a classic monolith to a distributed, configurable, resilient system. Its purpose is to master Spring technologies and to demonstrate them to employers, following the path from junior to senior and architect with standard tools and without exotic choices.

The application is the same in every stage: notes written in Markdown, users with registration, note sharing with roles, notifications with a live feed over Server-Sent Events, and full-text search of notes. The first stage is an elementary monolith with server-rendered pages; every following stage adds one step — a technology, a pattern or a function — so the functionality grows stage by stage. The HTTP API appears in stage 13, its contract is fixed by the acceptance tests in 15, and from then on it grows only by addition: behaviour defined in an earlier stage does not change. Stage 37 changes the authentication method once, from HTTP Basic to OAuth 2.0 / OpenID Connect.

The code and the documentation of the previous attempt of the project are kept under the tag `legacy-2026-11` and serve as a reference only.

## Stages

Every stage is a standalone application in its own directory. The first stage is always `01-monolith`; every following stage starts as a copy of the previous one and adds one key step, motivated by a problem of the previous stage. A directory is named after the full official name of the key technology of its stage, in lower case with hyphens and without abbreviations (`docker-compose`, `spring-boot-actuator`, `spring-cloud-gateway`); a stage whose step is a pattern or a function is named after the full name of that pattern or function (`command-query-separation`, `saga`, `note-ownership`, `email-verification`); only `monolith` names the baseline. Patterns and supporting practices — contract tests, failure tests, page tests — belong to the stage of the step that introduces them. The number of stages is not limited, and the levels may have different numbers of stages. The order of the stages that are not yet built is flexible: an argument about a stage is weighed on its merits, and a better order changes the plan rather than being rejected because of the current one. Each stage is described in its `README.md`; the reasoning behind the whole sequence is in `docs/adr/0001-evolution-in-stages.md`.

| Directory | Change | Level |
|---|---|---|
| `01-monolith` | notes in the browser and the user model with its invariants: Gradle, Spring MVC, Thymeleaf, Spring Data JPA on Hibernate, PostgreSQL or MySQL selected by profile, its server in a Docker container, the schema from `schema-postgresql.sql` or `schema-mysql.sql`, `@Transactional`; tests against every DBMS with Testcontainers | junior |
| `02-jakarta-validation` | form validation | junior |
| `03-markdown` | notes in Markdown | junior |
| `04-pagination` | paginated and sorted list of notes | junior |
| `05-command-query-separation` | commands and queries separated in the services | junior |
| `06-spring-security` | form login, session, users in the database with one role | junior |
| `07-user-registration` | sign-up | junior |
| `08-flyway` | versioned schema migrations for every DBMS | junior |
| `09-note-ownership` | notes owned by users | junior |
| `10-method-security` | note sharing with roles | junior |
| `11-in-app-notifications` | notifications from Spring application events | junior |
| `12-server-sent-events` | live notification feed | junior |
| `13-rest-api` | JSON REST API with HTTP Basic next to the pages | junior |
| `14-problem-details` | errors of the API as `ProblemDetail` (RFC 9457) | junior |
| `15-rest-assured` | black-box acceptance tests; the contract of the API is fixed | junior |
| `16-spring-rest-docs` | API documentation from tests | junior |
| `17-openapi` | springdoc-openapi, Swagger UI | junior |
| `18-spring-boot-actuator` | health, info and metrics endpoints | junior |
| `19-structured-logging` | structured JSON logs | junior |
| `20-docker` | container image with `bootBuildImage` | junior |
| `21-docker-compose` | one Compose file: the DBMS servers for `bootRun`, the whole stage for `docker compose up` | junior |
| `22-twelve-factor-app` | configuration from the environment, stateless processes, one image for every environment | junior |
| `23-nginx` | TLS reverse proxy in front of the application | junior |
| `24-github-actions` | build and test pipeline | junior |
| `25-jenkins` | second equal pipeline, Configuration as Code | junior |
| `26-amazon-web-services` | deployment step to Amazon Web Services (OIDC, ECR, EC2), switched off | junior |
| `27-spring-cache-abstraction` | cache of frequent reads in memory | middle |
| `28-redis-cache` | the same cache in Redis | middle |
| `29-spring-session` | session in Redis, two instances | middle |
| `30-redis-pub-sub` | live feed across instances | middle |
| `31-prometheus` | metrics collection and alerts | middle |
| `32-grafana` | dashboards | middle |
| `33-grafana-loki` | logs collected in Grafana Loki | middle |
| `34-spring-modulith` | module boundaries, event publication registry | middle |
| `35-elasticsearch` | full-text search in a separate read model | middle |
| `36-spring-batch` | rebuild of the search index by a batch job | middle |
| `37-spring-authorization-server` | OAuth2 instead of passwords: `auth-server`, Resource Server and Client | middle |
| `38-email-verification` | email confirmation in `auth-server` | middle |
| `39-phone-verification` | phone confirmation in `auth-server` through Amazon SNS | middle |
| `40-multi-factor-authentication` | second factor with Spring Security | middle |
| `41-keycloak` | Keycloak instead of `auth-server`, realm as code | middle |
| `42-google-docs-export` | export of a note to Google Docs | middle |
| `43-spring-cloud-gateway` | single entry point that hides how the system is split | senior |
| `44-token-relay` | browser sign-in at the gateway, token relay | senior |
| `45-react` | single-page application behind the gateway | senior |
| `46-progressive-web-app` | installable application that works offline | senior |
| `47-react-native` | mobile client, public OAuth2 client with PKCE | senior |
| `48-rabbitmq` | notifications as the first separate service | senior |
| `49-apache-kafka` | Apache Kafka as the alternative broker selected by configuration | senior |
| `50-micrometer-tracing` | trace context across processes, trace identifiers in the logs | senior |
| `51-grafana-tempo` | traces stored and shown in Grafana | senior |
| `52-spring-cloud-openfeign` | every module a service, calls through OpenFeign as an intermediate step | senior |
| `53-http-service-clients` | Spring HTTP service clients instead of OpenFeign | senior |
| `54-spring-cloud-contract` | consumer-driven contract tests | senior |
| `55-saga` | sagas over broker events with compensation | senior |
| `56-spring-cloud-config` | central configuration and encrypted secrets | senior |
| `57-eureka` | service discovery, Spring Cloud LoadBalancer | senior |
| `58-spring-framework-resilience-features` | timeouts and retries with Spring Framework | senior |
| `59-resilience4j` | circuit breakers and bulkheads through Spring Cloud Circuit Breaker | senior |
| `60-gateway-rate-limiting` | request rate limiter of the gateway | senior |
| `61-graphql` | combined reads across services next to the unchanged REST API | senior |
| `62-grpc` | hot internal call over gRPC | senior |
| `63-kubernetes` | the same services on k3s (local, ready for EC2), manifests, probes | senior |
| `64-kubernetes-service-discovery-and-configuration` | Kubernetes Services, ConfigMaps and Secrets instead of Eureka and Spring Cloud Config | senior |
| `65-horizontal-pod-autoscaling` | services scale with load | senior |
| `66-graalvm` | native images with Spring AOT | senior |
| `67-domain-driven-design` | aggregates, value objects, domain events | architect |
| `68-hexagonal-architecture` | storage port and contract test; PostgreSQL and MySQL | architect |
| `69-mongodb` | second storage adapter on Spring Data MongoDB | architect |
| `70-command-query-responsibility-segregation` | separate read and write models of the notes service | architect |
| `71-event-sourcing` | notes stored as a sequence of events | architect |
| `72-spring-ai` | semantic search with Spring AI, Ollama, Elasticsearch vector store | architect |
| `73-spring-webflux` | second web stack, choice by measurement | architect |

### How a stage is built

The project grows iteratively, smoothly, linearly and atomically. Every milestone is a working application, a separate project and a separate directory; there are no milestones inside a directory. Each stage adds the smallest step that still gives a working result, and a stage that does two things is split. Simple and elementary technologies and patterns come first, complex and advanced ones last; the first stage contains only what a junior uses in a first application, and a tool that needs knowledge from a later stage — Docker Compose needs the Docker images of 20 — comes after that stage. Within that order a step comes after the step it builds on, the Spring way comes before an external technology for the same task, alternatives come next to each other — the first one, then the second one selected by configuration or replacing the first (Spring Cache Abstraction in memory and Redis, GitHub Actions and Jenkins, Spring Authorization Server and Keycloak, RabbitMQ and Apache Kafka, Spring Cloud OpenFeign and HTTP service clients) — and a step that needs several processes comes after the system is distributed. Every requirement of the application is a visible stage of its own rather than a detail of another stage. The sequence of directories shows three lines at once: the development of the project, the mastering of technologies, and the roadmap of a developer — junior, middle, senior and architect stages in the order in which a Spring developer usually learns the technologies. The last stages call for an architectural decision based on trade-offs and measurements rather than for one more technology.

Stages are built strictly one after another. Work on a stage starts with a commit that copies the previous directory, so the history shows what the stage changed.

Inside a stage the work goes in vertical slices: each function is built through every layer at once, from the database to the user interface, so that something working can be tried in the browser as early as possible. The user interface of a function is written together with the backend that serves it, never after the whole backend.

Every stage is ready to be tested, operated and shown to an employer, and reflects one pragmatic step in skills and technology stack — no more than the step needs. A stage is finished when the system starts with one command — in 01–19 the application through `bootRun` against the server of its DBMS started by hand in a Docker container with `docker run`, in 20 the application image with `docker run`, from 21 `bootRun` with the DBMS server started by Spring Boot Docker Compose support and the whole stage from the Compose file; the application always runs against a real DBMS server, and the tests against the server that Testcontainers starts — the acceptance tests (from 15) and the tests of the stage pass, the `README.md` is complete and the key decisions are recorded in ADRs. A finished stage is frozen: only bugs and vulnerabilities are fixed in it, and Dependabot keeps its dependencies up to date.

Every stage is a standalone Gradle build with its own `settings.gradle` and wrapper, in the Groovy DSL that Spring Initializr generates; there is no root build and no shared `build-logic`. CI follows the same layout: one pipeline per stage, triggered only by changes in the stage directory. The pipeline appears in 24 (GitHub Actions); Jenkins joins in 25 as an equal pipeline with the same steps. Both pipelines call the same Gradle tasks, so the build logic is not duplicated.

Stages 21–62 run with Docker Compose, stages from 63 on Kubernetes (k3s), both locally and in CI. From 26 every stage is ready for deployment on Amazon Web Services: the Compose files or Kubernetes manifests and the deployment step of the pipelines are in place, but the deployment step stays switched off. It is switched on when the job search starts, because a link to a running system is worth more than a link to code.

### Repository history

The public repository is a portfolio, so its history is clean, tidy and linear. The work happens on the `auth` branch until `01-monolith` is implemented and has been tried on a server. At that point `main` starts a new history from the state of that moment, and the repository gets a `LICENSE`; the previous history, the `auth` branch and the `legacy-2026-11` tag move to a private archive repository and are removed from the public one. This rewrite of public history happens once, only on an explicit request of the user and after the archive has been checked.

From then on the history stays linear: no merge commits, every change is rebased onto `main`, and the repository is configured to require a linear history. Every commit builds and passes the tests, and its message follows Conventional Commits in English (`feat:`, `fix:`, `docs:`, `build:`, `ci:`, `test:`, `refactor:`).

### Acceptance tests

The `acceptance-tests/` directory contains one black-box test suite that runs against every stage from 15 over HTTP only and proves that behaviour defined in an earlier stage does not change; only the authentication method changes once, in 37. It is grouped by capability — notes, sharing, notifications, live feed, search — and knows only the public API and its parameters: the base URL, the credentials of a test user, the issuer URL for a system that accepts tokens and the capabilities to run. It knows nothing about the implementation, creates and verifies test data only through the API and verifies asynchronous behaviour by polling. The browser interface and the technologies of a stage are checked by the unit, slice, page and integration tests inside the stage. Details are in `acceptance-tests/README.md`.

## Decision priorities

Technical decisions follow these priorities:

1. **Spring Boot auto-configuration.** Standard starters and properties are used. Custom bean configuration is allowed only when a task cannot be solved without it, and the reason is stated.
2. **KISS and DRY.** When the principles conflict, the option that keeps auto-configuration and still avoids duplication wins. When both options keep auto-configuration, the simpler one wins.
3. **Current Spring technologies only.** The project is limited to the current Spring portfolio and the features of Spring Boot — Spring Boot SQL initialization, Spring Boot Docker Compose support, Spring Cache Abstraction, Spring Security multi-factor authentication, Spring Session, Spring REST Docs, Spring Authorization Server, Spring HTTP service clients, Spring Cloud Contract, Spring gRPC, Spring Batch, Spring AMQP, Spring AI, Spring Cloud Config encryption, Spring Cloud Circuit Breaker, Spring AOT, `bootBuildImage`, Spring Boot Kubernetes support. When two technologies are alternatives, the current one is chosen; an outdated or feature-complete technology appears only as an intermediate stage that the next stage replaces, such as Spring Cloud OpenFeign before Spring HTTP service clients. A technology outside the Spring portfolio is used only when matching job postings critically require it or a function of the application cannot work without it: PostgreSQL, MySQL, MongoDB, Testcontainers, Flyway, OpenAPI, REST Assured, Docker and Docker Compose, NGINX, GitHub Actions, Jenkins, Keycloak, Prometheus, Grafana, Grafana Loki and Grafana Tempo, Redis, Elasticsearch, Apache Kafka, RabbitMQ, Kubernetes, GraalVM, Amazon Web Services, React and React Native, a CommonMark library for Markdown and the Google Docs API for export. Such a technology is introduced after the Spring way of solving the same task, as a separate stage — Flyway after Spring Boot SQL initialization, OpenAPI after Spring REST Docs, Keycloak after Spring Authorization Server. The set aims to be the complete toolkit of a Spring developer with the widest current outlook.

The plan covers the technologies that Java/Spring job postings require almost always. The set of technologies is chosen pragmatically, but the project stays ready for an alternative choice: every technology is used through a standard abstraction or protocol where one exists — Spring Data, Spring Cache, Spring Modulith event externalization, Micrometer, OpenTelemetry, OAuth 2.0 / OpenID Connect — so replacing it with an alternative is a change of dependencies and settings rather than of code. Technologies that were considered and not included are listed with their reasons in ADR 0001.

The database is designed so that three later moves stay easy: from one DBMS to another, from the application's own users to Keycloak or a similar identity provider, and from passwords to OAuth 2.0 / OpenID Connect. Tables and columns use types that every DBMS of the project accepts and no feature of a single DBMS; the tests against every DBMS prove that the schemas behave alike. Rules about the content of the data — normalization such as the case of a username, and validation — belong to the domain and are enforced in the application by the model itself, which keeps its own invariants so that no service, controller or other implementation can bypass them, never left to a setting of a DBMS such as its collation; the constraints of the schema, such as `not null` and `unique`, stay as the last guard of integrity. Names are in lowercase and are always quoted, so a reserved word such as `user` is a valid name: Hibernate quotes every name it generates (`hibernate.globally_quoted_identifiers`) and also has keyword quoting switched on (`hibernate.auto_quote_keyword`), which is redundant while every name is quoted but keeps reserved words safe if global quoting is ever switched off, and hand-written SQL quotes every name with the quotes of its DBMS — double quotes in PostgreSQL, backticks in MySQL. Identity — sign-in data such as the username and the password — is kept apart from the data of the application: other tables refer to a user only by the application's own key, never by a username or an email, so that moving sign-in to an identity provider replaces the sign-in columns of the user with the provider's subject identifier and leaves every reference unchanged. Roles on a note belong to the application; global roles move to the identity provider. The user schema that Spring Security ships for `JdbcUserDetailsManager` (`users.ddl` in `spring-security-core`) and the implementations of Spring Security are the reference for users and their roles, and every deviation from them is recorded with its reason. Where no standard defines a rule of the domain, such as the characters of a username, the rule follows widely known systems such as GitHub and Linux wherever possible, and where they differ it is the rule that all of them accept at once.

A project starts from what its generator produces — Spring Initializr for a stage, `gradle init` for a plain Gradle project — and keeps the default behaviour of its tools. Every known issue of a default is recorded in the documentation — a compromise in ADR 0001, a warning in the "Known warnings" section of the stage `README.md` — and is resolved when it starts to matter.

Copying code between stages is a deliberate exception to DRY: every directory is a snapshot of its stage. Inside a stage DRY applies fully.

Standards — RFCs, specifications, the documentation on docs.spring.io — outweigh tutorials and the user's own proposals; a deviation from a standard is allowed only with a stated reason. Architectural decisions are made by the user: the assistant describes the options and gives a recommendation.

Facts about Spring — whether a starter exists, its name, its version, its status — are checked against Spring Initializr, docs.spring.io and GitHub spring-projects when a stage starts, never from memory and never through the Maven Central search, whose index lags behind.

## Documentation

All documentation, comments and project content are written in correct technical English. Conversation with the user is in Russian.

Every place of the project — code, configuration and documentation — uses the most precise term and keeps related concepts apart according to their context: for example, the DBMS (PostgreSQL, MySQL), selected by a profile; its server, a Docker or Testcontainers container; and the `notes` database on that server.

Documentation is written for its reader: coherent text organised in sections with an introduction, recording the outcome of a decision and its reason, without a chronicle of the discussion, quotations or notes such as "as requested by the user".

Between commits existing records are only edited in place; new records are not added next to them, and a new record appears only after a commit. Until the first stage has working code, decisions are written into the existing records even after a commit, because the plan is still being shaped. For ADRs this means that a decision made after the last commit goes into an existing record, and a new ADR is created only for a decision made after a commit. ADRs live in `docs/adr/`, one decision per file `NNNN-title.md`.

## Code style

The rules below apply to every file of the project, whatever its format or language — Java, Gradle, properties or any other — including the files that a generator such as Spring Initializr produces, which are brought in line with them after generation.

- Indentation is four spaces; tabs are not used.
- Two blank lines never stand in a row.
- A file ends with exactly one line break after its last line, with no blank lines at its end.
- Exactly one blank line follows the line that opens a class.
- No blank line precedes a closing brace, except in an empty class or method body, which holds exactly one blank line between its braces.
- Intermediate results go into local variables with clear names instead of nested calls.
- Abbreviations in identifiers are written in capitals, except `Id`.

For now the rules are kept with the tools at hand — `.editorconfig` and the formatter of the editor — and with discipline. A later stage of its own, not yet placed in the sequence, chooses, adds and tries one flexible tool that both checks the formatting in the build and formats the code, and that integrates equally with every development environment, preferably highlighting violations while the code is edited rather than only in the build, so that no editor is the source of truth.

## Code and build

The repository is named `notes-spring`; the application is named `notes` in every stage — the Gradle project, `spring.application.name` and the package `com.example.notes`.

Builds and tests pass without errors and warnings caused by the project's own code and configuration; warnings that a tool emits in its default behaviour are accepted and not suppressed by custom configuration.

When a step is finished — the build passes and the documentation is synchronised — the assistant proposes a commit and push with the planned commit messages before moving on. Commits and pushes are made only on an explicit request of the user; requests such as "do it", "apply" or "go" are not requests to commit, and a question such as "should we commit?" is answered, not carried out. The Russian command `Зафиксируй` ("commit it") means: synchronise the documentation, commit and push. Every request to commit and push starts with a review of the repository: the assistant first reports what needs attention, if anything, and only after the user has considered those points records in the documentation what the commit requires, commits and pushes. Dates in the documentation are taken from the `date` command.
