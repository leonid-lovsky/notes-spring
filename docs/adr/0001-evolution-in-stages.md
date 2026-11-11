# 0001. Evolution in stages

Status: accepted (2026-11-11)

## Context

The goal is to learn Spring technologies and to demonstrate them to employers on one application, from a classic monolith to a distributed, configurable, resilient system. Technologies that Java/Spring job postings require almost always must appear in the plan, each in the stage where it solves a real problem. The previous attempt built every variant at once (web stack × data access × database vendor as Gradle modules) and never reached a running application.

## Decision

Twenty-eight independent projects in one repository, built strictly one after another. Each project starts as a copy of the previous one and adds one change, motivated by a problem of the previous stage. Functionality and HTTP API — users, notes, sharing with roles, notifications with a live feed over Server-Sent Events, full-text search — are fixed in the first project, which already authenticates users with HTTP Basic; the second replaces the authentication method with OAuth 2.0 / OpenID Connect, and the contract does not change afterwards. One black-box acceptance test suite (`acceptance-tests/`) runs against every stage over HTTP only and knows nothing about the implementation; it proves that the behaviour of the API does not change. Standard Spring Boot starters with auto-configuration are preferred over custom configuration.

The project grows iteratively, smoothly and as evenly as possible. Every milestone is a working application, a separate project and a separate directory. The sequence of directories shows three lines at once: the development of the project, the mastering of technologies, and the growth from junior to senior and architect. A stage is named after the technology that defines it and its architecture; when several technologies matter, the stage is split into several stages, one per technology. Only stages whose change is an architectural style or technique are named after it: monolith, microservices, saga, autoscaling, hexagonal.

The stages form groups:

- 01–04, junior: a REST API with Spring Security (monolith), OAuth2 with an own authorization server (oauth2), a browser interface (thymeleaf), module boundaries restructured while it is still cheap (modulith).
- 05–12, middle: the modular monolith becomes ready for production — Terraform, Jenkins, GitLab CI, SonarQube, Prometheus, Grafana, Elasticsearch, Redis. Provisioning, delivery and monitoring precede distribution because a system needs them before it is split (Martin Fowler, "MicroservicePrerequisites").
- 13–14, middle+: the first service leaves the process (kafka) and is traced end to end (opentelemetry).
- 15–21, senior: the system is distributed (microservices, spring-cloud-gateway), made configurable (spring-cloud-config, eureka, secrets-manager) and resilient (resilience4j, saga).
- 22–25, senior: the system moves to a platform (kubernetes, helm, argocd, autoscaling).
- 26–28, architect: decisions on trade-offs and measurements — where a storage abstraction pays off (hexagonal, mongodb) and which web stack the measurements justify (webflux).

Technologies required by the job market are placed as follows:

- **AWS** is the target platform of every stage; each stage is ready for deployment, but the actual deployment is postponed and the deployment step of the pipelines stays switched off until the job search starts. 01: Amazon EC2 with Docker Compose, images in Amazon ECR, GitHub Actions authenticates through OIDC with a least-privilege IAM role. 05: Terraform describes the AWS resources. 19: secrets in AWS Secrets Manager through Spring Cloud AWS, with LocalStack locally and in tests. 24: Terraform grows to the whole cluster infrastructure; k3s runs on EC2.
- **Docker Compose** runs stages 01–21; **Kubernetes** (k3s) replaces it from 22. Both run locally and in CI until the deployment starts.
- **GitHub Actions** builds every stage; **Jenkins** joins in 06 and **GitLab CI** in 07 as equal pipelines with the same steps, all calling the same Gradle tasks, so the build logic is not duplicated. **SonarQube Cloud** is the quality gate of all pipelines from 08, **Argo CD** deploys to the cluster from 24.
- **Spring Security** from 01: users and BCrypt password hashes in the application database, HTTP Basic. **OAuth 2.0 / OpenID Connect** comes in 02: tokens are issued by `auth-server`, a separate Spring Boot application on Spring Authorization Server, configured through properties, and the application becomes a Resource Server. In 03 the browser interface signs in as an OAuth2 Client; in 16 the gateway takes over browser sign-in and relays the token to the services.
- **Thymeleaf** comes in 03 as a server-rendered browser interface next to the unchanged REST API.
- **Prometheus** comes in 09, **Grafana** in 10; distributed tracing and logs join them in 14, when a second process appears.
- **Elasticsearch** comes in 11 and replaces the PostgreSQL full-text query. The search module indexes notes from Spring Modulith events, so the event publication registry guarantees that the index catches up after an outage.
- **Redis** comes in 12 as a cache (Spring Cache); in 16 it also keeps the session shared by several gateway instances (Spring Session).
- **Kafka** comes in 13 and carries the saga in 21.
- **Gradle**, **Actuator**, **MapStruct**, **OpenAPI** (springdoc-openapi, Swagger UI), `ProblemDetail` error responses (RFC 9457) and **JaCoCo** start in 01.

Managed services that cost money even without load — Amazon RDS, Amazon MSK, Amazon EKS — are named as production alternatives in the READMEs of their stages but not used.

The choice of technologies is pragmatic, but the project stays ready for an alternative choice: every technology is used through a standard abstraction or protocol where one exists — Spring Data, Spring Cache, Spring Modulith event externalization, Micrometer, OpenTelemetry, OAuth 2.0 / OpenID Connect — so replacing it with an alternative is a change of dependencies and settings rather than of code.

Considered and not included in the plan, each can be added later as a separate decision:

- RabbitMQ — Kafka covers messaging; Spring Modulith externalizes events to AMQP as well
- the ELK stack as a log platform — Grafana with Loki covers logs
- Liquibase — Flyway covers migrations
- Maven — Gradle is the build tool
- Lombok — records cover request and response bodies
- Keycloak — Spring Authorization Server stays within the Spring stack and its auto-configuration; the application relies only on OpenID Connect, so any compliant provider fits
- GraphQL (Spring for GraphQL) — the REST API covers the functionality; the natural candidate place is the gateway in 16, where a client needs data aggregated from several services

## Consequences

Benefits:

- every stage is a finished application, ready for deployment, operation and a demonstration to an employer
- the directories read as a path from junior to architect
- the difference between two neighbouring directories shows one step of the evolution
- every technology from typical job postings is backed by working code in a finished stage; the job search can start after 03 and is well covered after 12

Costs:

- code is duplicated between stages on purpose: each directory is a snapshot, and twenty-eight snapshots make the repository large, and some stages change only the build or the infrastructure
- frozen stages still need dependency updates
- AWS costs nothing until the deployment starts; prices and free tier conditions are checked at that moment
