# 0001. Evolution in stages

Status: accepted (2026-11-11)

## Context

The goal is to learn Spring technologies and to demonstrate them to employers on one application, from a classic monolith to a distributed, configurable, resilient system. Technologies that Java/Spring job postings require almost always must appear in the plan, each in the stage where it solves a real problem. The previous attempt built every variant at once (web stack × data access × database vendor as Gradle modules) and never reached a running application.

## Decision

Thirty-four independent projects in one repository, built strictly one after another. The first is always the monolith; each following project starts as a copy of the previous one and adds one key technology, motivated by a problem of the previous stage. Functionality and HTTP API — users, notes, sharing with roles, notifications with a live feed over Server-Sent Events, full-text search — are fixed in the first project, which already authenticates users with HTTP Basic; stage 11 replaces the authentication method with OAuth 2.0 / OpenID Connect, and the contract does not change afterwards. GraphQL in stage 24 adds a second API for combined reads without changing the REST API. One black-box acceptance test suite (`acceptance-tests/`) runs against every stage over HTTP only and knows nothing about the implementation; it proves that the behaviour of the REST API does not change.

The project is limited to the current Spring portfolio and the features of Spring Boot, with auto-configuration preferred over custom configuration. When two technologies are alternatives, the current one is chosen; outdated or feature-complete technologies are not used. A technology outside the Spring portfolio is used only when matching job postings critically requires it, and only after the Spring way of solving the same task, as a separate stage. The set aims to be the complete toolkit of a Spring developer with the widest current outlook. The choice stays open to alternatives: every technology is used through a standard abstraction or protocol where one exists — Spring Data, Spring Cache, Spring Modulith event externalization, Micrometer, OAuth 2.0 / OpenID Connect — so replacing it is a change of dependencies and settings rather than of code.

The project grows iteratively, smoothly and as evenly as possible. Every milestone is a working application, a separate project and a separate directory. The sequence of directories shows three lines at once: the development of the project, the mastering of technologies, and the growth from junior to senior and architect. A stage is named after its key technology; the only exceptions are monolith, the baseline, and hexagonal, an architectural style. When several technologies matter, the stage is split, one key technology per stage. Patterns and supporting practices — sagas, contract tests, autoscaling, failure tests, page tests — belong to the stage of the technology that implements them. The number of stages is kept even.

The order follows the roadmap of a Spring developer: ten stages of a junior, ten of a middle, ten of a senior and the final stages of an architect, each technology at the point where a developer usually meets it.

- 01–10, junior: a REST API with Spring Data JPA and Spring Security (monolith); schema migrations (flyway); API documentation from tests and as an OpenAPI description (spring-rest-docs, openapi); operational endpoints (actuator); a server-rendered browser interface with form login (thymeleaf); a container image and the whole stage in one Compose file (docker, compose); a pipeline (github-actions); a cache (redis).
- 11–20, middle: OAuth2 with Spring Authorization Server and Keycloak replacing it without a code change (oauth2, keycloak); module boundaries while restructuring is still cheap (modulith); monitoring (prometheus, grafana); a search engine and a batch job that rebuilds its index (elasticsearch, spring-batch); a second pipeline (jenkins); the first service out of the process, first through a queue and then through a replayable log (rabbitmq, kafka). Delivery and monitoring precede distribution because a system needs them before it is split (Martin Fowler, "MicroservicePrerequisites").
- 21–30, senior: tracing across processes (micrometer-tracing); a gateway as the single entry point and the seam for extracting services (spring-cloud-gateway); services calling each other through Spring HTTP service clients, protected by contract tests and kept consistent by sagas (http-service-clients); combined reads (graphql); configuration with encrypted secrets and discovery (spring-cloud-config, eureka); resilience (resilience4j); the hottest internal call over gRPC (spring-grpc); a platform that replaces Spring Cloud infrastructure and scales with load (kubernetes); native images that start and scale fast (graalvm).
- 31–34, architect: decisions on trade-offs and measurements — where a storage abstraction pays off (hexagonal, mongodb), what semantic search is worth against the cost of a model (spring-ai), and which web stack the measurements justify (webflux).

Technologies outside the Spring portfolio, kept because job postings critically require them, are placed as follows:

- **PostgreSQL**, **Testcontainers** and **Gradle** start in 01; the schema comes from Spring Boot SQL initialization (`schema.sql`) and moves to **Flyway** in 02. **OpenAPI** (springdoc-openapi, Swagger UI) comes in 04 after Spring REST Docs in 03.
- **Docker** comes in 07 with an image built by `bootBuildImage`, **Docker Compose** in 08 with the whole stage in one file. Compose runs stages 08–27, **Kubernetes** (k3s) replaces it from 29. Both run locally and in CI until the deployment starts.
- **AWS** is the target platform from 09: Amazon EC2, images in Amazon ECR, GitHub Actions authenticates through OIDC with a least-privilege IAM role. Each stage is ready for deployment, but the deployment step of the pipelines stays switched off until the job search starts.
- **GitHub Actions** comes in 09; **Jenkins** joins in 18 as an equal pipeline with the same steps, both calling the same Gradle tasks, so the build logic is not duplicated.
- **Keycloak** replaces Spring Authorization Server in 12, changing only the issuer settings of the application. The browser interface of 06 uses form login until 11, where it becomes an OAuth2 Client.
- **Prometheus** comes in 14 and **Grafana** in 15; traces and logs join them in 21.
- **Redis** comes in 10 as a cache (Spring Cache), its effect visible in the cache metrics of Actuator; in 22 it also keeps the session shared by several gateway instances (Spring Session) and in 27 backs the rate limiter of the gateway.
- **Elasticsearch** comes in 16 through Spring Data Elasticsearch and replaces the PostgreSQL full-text query; the event publication registry of Spring Modulith guarantees that the index catches up after an outage.
- **RabbitMQ** comes in 19 through Spring AMQP and Spring Modulith event externalization as the first broker, when notifications leave the process. **Kafka** replaces it for domain events in 20, when events must be kept and replayed by consumers added later, and carries the sagas in 23.
- **GraalVM** comes in 30 through Spring AOT and `bootBuildImage`.
- **Ollama** runs the embedding model of Spring AI locally in 33; the vector store is the Elasticsearch already in the plan.
- **Resilience4j** comes in 27 through Spring Cloud Circuit Breaker; retries and concurrency limits use the resilience features of Spring Framework where they suffice.
- **MongoDB** comes in 32 through Spring Data MongoDB.

Considered and not included, because they are outside the current Spring portfolio and not critical for job postings, or duplicate a tool already in the plan:

- MapStruct — records with hand-written mapping cover the application
- Liquibase — Flyway covers migrations
- SonarQube and JaCoCo — static analysis and coverage are not a Spring concern
- Gatling — load is generated in the stages that need a measurement
- Terraform and Argo CD — the actual deployment is postponed; the pipelines carry the deployment step
- HashiCorp Vault — Spring Cloud Config encryption covers secrets
- Helm — plain Kubernetes manifests cover one environment
- Toxiproxy — failure tests stop or slow down containers through Testcontainers
- Spring Cloud OpenFeign — feature-complete since Spring Cloud 2022.0; Spring recommends HTTP service clients instead
- Spring Cloud Stream — Spring Modulith event externalization already decouples the code from the broker
- WebSocket — Server-Sent Events cover the one-way live feed
- the ELK stack — Grafana covers logs
- Maven — Gradle is the build tool
- GitLab CI — a third pipeline with the same steps teaches nothing new
- Lombok — records cover request and response bodies
- AWS Secrets Manager — Spring Cloud Config encryption covers secrets without tying the project to one cloud
- a hand-written `Dockerfile` — `bootBuildImage` builds layered images without one

Managed services that cost money even without load — Amazon RDS, Amazon MSK, Amazon EKS — are named as production alternatives in the READMEs of their stages but not used.

## Consequences

Benefits:

- every stage is a finished application, ready for deployment, operation and a demonstration to an employer
- the directories read as a path from junior to architect
- the difference between two neighbouring directories shows one step of the evolution
- every technology the job postings critically require is backed by working code in a finished stage; the job search can start after the junior stages and is well covered after the middle stages

Costs:

- code is duplicated between stages on purpose: each directory is a snapshot, and thirty-four snapshots make the repository large
- some stages change only the build or the infrastructure, not the application code
- frozen stages still need dependency updates
- AWS costs nothing until the deployment starts; prices and free tier conditions are checked at that moment

The project starts from what Spring Initializr generates and keeps the default behaviour of its tools; custom configuration is added only when a task cannot be solved without it. The known consequences, each with the point at which it is revisited:

- the group `com.example` and the package `com.example.notes` are the placeholders of Spring Initializr; they stay identical in all stages and are replaced in all of them at once before the project is published
- the tests run with the warnings that Mockito and the JVM emit by default: Mockito attaches itself to the running JVM as an agent, and the JVM reports the dynamic agent loading and limits class data sharing to boot loader classes because Mockito appends to the bootstrap classpath; passing the Mockito jar to the test JVM as `-javaagent`, as the Mockito documentation recommends for Java 21 and later, removes the first three, and `-Xshare:off` removes the last one by switching class data sharing off; neither is configured, because both are custom build configuration and the warnings do not affect the results of the tests
- the image of a backing service is declared twice, in the Compose file used by `bootRun` and in the Testcontainers configuration used by the tests, because neither tool reads the configuration of the other; the two declarations are kept equal by hand, and a way to synchronise them that keeps automatic dependency updates replaces this compromise as soon as one is found
- the Compose file of the development-time services keeps the defaults of Spring Initializr — the image tag `latest`, so the database version changes whenever a newer image is pulled, the database port mapped to an ephemeral host port, and placeholder credentials written in the file — which suit local development only; they must not reach a deployed system, so when the Compose file describes the whole stage and becomes deployable in 08, the credentials leave the public repository, the database port stops being published to the host, and the floating image tag is revisited
