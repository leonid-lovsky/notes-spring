# 0001. Evolution in stages

Status: accepted (2026-11-11)

## Context

The goal is to learn Spring technologies and to demonstrate them to employers on one application, from a classic monolith to a distributed, configurable, resilient system. Technologies that Java/Spring job postings require almost always must appear in the plan, each in the stage where it solves a real problem. The previous attempt built every variant at once (web stack × data access × database vendor as Gradle modules) and never reached a running application.

## Decision

Independent projects in one repository, built strictly one after another; their number is not limited. The first is always the monolith; each following project starts as a copy of the previous one and adds one atomic step — a technology, a pattern or a function — motivated by a problem of the previous stage. The first stage is a monolith with server-rendered pages; the functionality — Markdown notes, users with registration, sharing with roles, notifications with a live feed over Server-Sent Events, full-text search, export to Google Docs — arrives step by step, and every requirement is a visible stage of its own. The REST API appears in 11 and from then on grows only by addition: behaviour defined in an earlier stage does not change, and only the authentication method changes once, in 33. GraphQL in 54 adds a second API for combined reads without changing the REST API. One black-box acceptance test suite (`acceptance-tests/`) runs against every stage from 11 over HTTP only, grouped by capability, and knows nothing about the implementation.

The project is limited to the current Spring portfolio and the features of Spring Boot, with auto-configuration preferred over custom configuration. When two technologies are alternatives, the current one is chosen; an outdated or feature-complete technology appears only as an intermediate stage that the next stage replaces. A technology outside the Spring portfolio is used only when matching job postings critically require it or a function of the application cannot work without it, and only after the Spring way of solving the same task, as a separate stage. The choice stays open to alternatives: every technology is used through a standard abstraction or protocol where one exists — Spring Data, Spring Cache, Spring Modulith event externalization, Micrometer, OpenTelemetry, OAuth 2.0 / OpenID Connect — so replacing it is a change of dependencies and settings rather than of code.

The sequence follows four rules. Each stage adds the smallest step that still gives a working result, and a stage that does two things is split. Simple and elementary technologies and patterns come first, complex and advanced ones last, so the sequence is linear in difficulty. Within that order a step comes after the step it builds on, and alternatives come next to each other — the first one, then the second one selected by configuration or replacing the first. A step that needs several processes — a message broker, tracing, contract tests — comes after the system is distributed. A stage is named after its key technology, or after its step when the step is a function or a pattern; `monolith` and `hexagonal` name the baseline and the architectural style.

The order follows the roadmap of a Spring developer; the levels have different numbers of stages.

- 01–22, junior: the monolith grows one elementary step at a time — validation, command–query separation, Markdown, migrations and a second database vendor (bean-validation, cqs, markdown, flyway, mysql); sign-in, ownership of notes, registration and sharing with method security (spring-security, multi-user, registration, method-security); the REST API with HTTP Basic and `ProblemDetail`, after which the acceptance tests guard every later stage, and its documentation (rest-api, spring-rest-docs, openapi); notifications from Spring application events and their live feed (notifications, server-sent-events); operational endpoints, a container image, the whole stage in one Compose file and the Twelve-Factor App (actuator, docker, compose, twelve-factor); two equal pipelines and a deployment step (github-actions, jenkins, aws).
- 23–38, middle: a cache, a shared session and a live feed across two instances (redis, spring-session, redis-pub-sub); logs, metrics and dashboards of one process (structured-logging, prometheus, grafana, loki); module boundaries with an event publication registry that no longer loses notifications (modulith); a search engine as a separate read model and a batch job that rebuilds its index (elasticsearch, spring-batch); OAuth2 with Spring Authorization Server, which takes over the users together with registration, email and phone confirmation and the second factor, and then Keycloak replacing it without a code change (authorization-server, mail, sms, mfa, keycloak); the export to Google Docs through an OAuth2 Client (google-docs). The API and the pages switch to tokens in the same stage, because once the credentials leave the application neither of them can check a password.
- 39–60, senior: a gateway as the single entry point and the seam for extracting services, then browser sign-in at the gateway with token relay (spring-cloud-gateway, token-relay); clients on the API, which no longer gains capabilities — a single-page application, a Progressive Web App and a mobile client (react, pwa, react-native); the first service out of the process through a broker and the second broker as its alternative (rabbitmq, kafka); tracing across processes (micrometer-tracing); every module a service, first through OpenFeign and then through Spring HTTP service clients (openfeign, http-service-clients); configuration with encrypted secrets and discovery (spring-cloud-config, eureka); timeouts and retries with Spring Framework, then circuit breakers, then rate limiting (retry, resilience4j, rate-limiting); combined reads, contract tests and sagas (graphql, spring-cloud-contract, sagas); the hottest internal call over gRPC (spring-grpc); a platform that replaces Spring Cloud infrastructure and scales with load (kubernetes, autoscaling); native images (graalvm). Delivery and monitoring precede distribution because a system needs them before it is split (Martin Fowler, "MicroservicePrerequisites").
- 61–67, architect: the most advanced patterns and decisions on trade-offs and measurements — a domain model with aggregates and domain events (ddd); where a storage abstraction pays off (hexagonal, mongodb); separate read and write models and a full history of changes (cqrs, event-sourcing); what semantic search is worth against the cost of a model (spring-ai); and which web stack the measurements justify (webflux).

Technologies outside the Spring portfolio are placed as follows:

- **PostgreSQL**, **Testcontainers** and **Gradle** start in 01; the schema comes from Spring Boot SQL initialization (`schema.sql`) and moves to **Flyway** in 05. **MySQL** comes in 06 as the second vendor under JPA, selected by profile, with migrations per vendor in vendor-specific Flyway locations. **OpenAPI** (springdoc-openapi, Swagger UI) comes in 13 after Spring REST Docs in 12.
- A **CommonMark** library renders Markdown in 04; it is chosen when the stage starts.
- **Docker** comes in 17 with an image built by `bootBuildImage`, **Docker Compose** in 18 with the whole stage in one file. Compose runs stages 18–57, **Kubernetes** (k3s) replaces it from 58. Both run locally and in CI until the deployment starts.
- **GitHub Actions** comes in 20; **Jenkins** joins in 21 as an equal pipeline with the same steps, both calling the same Gradle tasks, so the build logic is not duplicated.
- **AWS** is the target platform from 22: Amazon EC2, images in Amazon ECR, GitHub Actions authenticates through OIDC with a least-privilege IAM role. Each stage is ready for deployment, but the deployment step of the pipelines stays switched off until the job search starts. **Amazon SNS** sends the SMS of 35.
- **Redis** comes in 23 as a cache (Spring Cache), in 24 it keeps the session of two instances (Spring Session), in 25 it carries the live feed between them, in 40 it keeps the session of the gateway and in 53 the counters of its rate limiter.
- **Prometheus** comes in 27, **Grafana** in 28 and **Loki** in 29 after structured logs in 26; traces join them in 46.
- **Elasticsearch** comes in 31 through Spring Data Elasticsearch as a separate read model fed by note events; the event publication registry of Spring Modulith guarantees that the index catches up after an outage. A full-text query inside the database would have to be written separately for PostgreSQL and MySQL.
- **Keycloak** replaces Spring Authorization Server in 37, changing only the issuer settings of the application.
- **Google Docs** receives exported notes from 38 through its API.
- **React** comes in 41 behind the gateway, the Progressive Web App in 42, **React Native** in 43 as a public OAuth2 client with PKCE.
- **RabbitMQ** comes in 44 through Spring AMQP and Spring Modulith event externalization, when notifications leave the process. **Kafka** comes in 45 as the alternative broker selected by configuration; it keeps events as a replayable log. Both are brokers of a distributed system and solve the same task in this project, so they stand next to each other.
- **Spring Cloud OpenFeign** is used in 47 as an intermediate step and replaced in 48 by Spring HTTP service clients, which Spring recommends since OpenFeign became feature-complete.
- **Resilience4j** comes in 52 through Spring Cloud Circuit Breaker, after timeouts and retries with the resilience features of Spring Framework in 51.
- **GraalVM** comes in 60 through Spring AOT and `bootBuildImage`.
- **MongoDB** comes in 63 through Spring Data MongoDB.
- **Ollama** runs the embedding model of Spring AI locally in 66; the vector store is the Elasticsearch already in the plan.

Open questions, answered when their stage starts: how Keycloak confirms a phone number by SMS (37), and how the second factor works with WebFlux (67), since Spring Security does not support multi-factor authentication in reactive applications; the plan keeps the second factor in the identity provider, outside the services.

Considered and not included, because they are outside the current Spring portfolio and not critical for job postings, or duplicate a tool already in the plan:

- MapStruct — records with hand-written mapping cover the application
- Liquibase — Flyway covers migrations
- SonarQube and JaCoCo — static analysis and coverage are not a Spring concern
- Gatling — load is generated in the stages that need a measurement
- Terraform and Argo CD — the actual deployment is postponed; the pipelines carry the deployment step
- HashiCorp Vault — Spring Cloud Config encryption covers secrets
- Helm — plain Kubernetes manifests cover one environment
- Toxiproxy — failure tests stop or slow down containers through Testcontainers
- Spring Cloud Stream — Spring Modulith event externalization already decouples the code from the broker
- WebSocket — Server-Sent Events cover the one-way live feed
- the ELK stack — Grafana with Loki covers logs
- Maven — Gradle is the build tool
- GitLab CI — a third pipeline with the same steps teaches nothing new
- Lombok — records cover request and response bodies
- AWS Secrets Manager — Spring Cloud Config encryption covers secrets without tying the project to one cloud
- a hand-written `Dockerfile` — `bootBuildImage` builds layered images without one
- an event sourcing framework — the event store of 65 is written for the stage on the existing storage

Managed services that cost money even without load — Amazon RDS, Amazon MSK, Amazon EKS — are named as production alternatives in the READMEs of their stages but not used.

## Consequences

Benefits:

- every stage is a finished application, ready for deployment, operation and a demonstration to an employer
- the directories read as a path from junior to architect
- the difference between two neighbouring directories shows one step of the evolution
- every technology the job postings critically require is backed by working code in a finished stage; the job search can start after the junior stages and is well covered after the middle stages
- every stage is small enough to be learned and finished without a long break

Costs:

- code is duplicated between stages on purpose: each directory is a snapshot, and many snapshots make the repository large
- some stages change only the build or the infrastructure, not the application code
- the acceptance tests start only in 11; stages 01–10 are checked by page tests inside the stages
- frozen stages still need dependency updates
- AWS costs nothing until the deployment starts; prices and free tier conditions are checked at that moment

The project starts from what Spring Initializr generates and keeps the default behaviour of its tools; custom configuration is added only when a task cannot be solved without it. The known consequences, each with the point at which it is revisited:

- the group `com.example` and the package `com.example.notes` are the placeholders of Spring Initializr; they stay identical in all stages and are replaced in all of them at once before the project is published
- the tests run with the warnings that Mockito and the JVM emit by default: Mockito attaches itself to the running JVM as an agent, and the JVM reports the dynamic agent loading and limits class data sharing to boot loader classes because Mockito appends to the bootstrap classpath; passing the Mockito jar to the test JVM as `-javaagent`, as the Mockito documentation recommends for Java 21 and later, removes the first three, and `-Xshare:off` removes the last one by switching class data sharing off; neither is configured, because both are custom build configuration and the warnings do not affect the results of the tests
- the image of a backing service is declared twice, in the Compose file used by `bootRun` and in the Testcontainers configuration used by the tests, because neither tool reads the configuration of the other; the two declarations are kept equal by hand, and a way to synchronise them that keeps automatic dependency updates replaces this compromise as soon as one is found
- the repository ignores the whole `.claude/` directory, although Claude Code treats only `.claude/settings.local.json` as personal and expects `.claude/settings.json`, agents, commands, skills and rules to be committed; nothing is shared with Claude Code yet, and the rule is narrowed to `.claude/settings.local.json` when a shared file is needed
- the Compose file of the development-time services keeps the defaults of Spring Initializr — the image tag `latest`, so the database version changes whenever a newer image is pulled, the database port mapped to an ephemeral host port, and placeholder credentials written in the file — which suit local development only; they must not reach a deployed system, so when the Compose file describes the whole stage and becomes deployable in 18, the credentials leave the public repository, the database port stops being published to the host, and the floating image tag is revisited
