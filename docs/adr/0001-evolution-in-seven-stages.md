# 0001. Evolution in seven stages

Status: accepted (2026-11-11)

## Context

The goal is to learn Spring technologies and to demonstrate them to employers on one application, from a classic monolith to a distributed, configurable, resilient system. Technologies that Java/Spring job postings require almost always must appear in the plan, each in the stage where it solves a real problem. The previous attempt built every variant at once (web stack × data access × database vendor as Gradle modules) and never reached a running application.

## Decision

Seven independent projects in one repository, built strictly one after another: monolith, modulith, event-driven, microservices, kubernetes, hexagonal, reactive. Each project starts as a copy of the previous one and adds exactly one architectural change, motivated by a problem of the previous stage. Functionality and HTTP API — users, notes, sharing with roles, notifications with a live feed over Server-Sent Events, full-text search — are fixed in the first project. One black-box acceptance test suite (`acceptance-tests/`) runs against every stage over HTTP only and knows nothing about the implementation; it proves that the behaviour does not change. Standard Spring Boot starters with auto-configuration are preferred over custom configuration.

Technologies required by the job market are placed as follows:

- **AWS** hosts every stage. 01: Amazon EC2 with Docker Compose, images in Amazon ECR, GitHub Actions authenticates through OIDC with a least-privilege IAM role. 04: secrets in AWS Secrets Manager through Spring Cloud AWS. 05: Terraform describes the whole AWS infrastructure; k3s runs on EC2.
- **Docker Compose** runs stages 01–04 locally and on EC2; **Kubernetes** replaces it from 05.
- **GitHub Actions** builds every stage; from 02 **Jenkins** is an equal second pipeline with the same steps. Both call the same Gradle tasks, so the build logic is not duplicated. GitLab CI comes in 03, Argo CD in 05.
- **OAuth 2.0 / OpenID Connect** from 01: tokens are issued by `auth-server`, a separate Spring Boot application on Spring Authorization Server, configured through properties. The application is an OAuth2 Client and Resource Server; in 04 the gateway takes over login and relays the token to the services.
- **Elasticsearch** comes in 02: the search module indexes notes from note events, and the event publication registry guarantees the index catches up after an outage. In 01 search is a plain PostgreSQL query.
- **Kafka** comes in 03 and carries the saga in 04.
- **Redis** comes in 04, where several gateway instances must share the session (Spring Session) and services benefit from caching (Spring Cache).
- **Gradle**, **MapStruct**, **OpenAPI** (springdoc-openapi, Swagger UI), **Actuator** and **SonarQube Cloud** start in 01.

Managed services that cost money even without load — Amazon RDS, Amazon MSK, Amazon EKS — are named as production alternatives in the READMEs of their stages but not used.

Considered and not included in the plan, each can be added later as a separate decision:

- RabbitMQ — Kafka covers messaging
- the ELK stack as a log platform — Grafana with Loki covers logs
- Liquibase — Flyway covers migrations
- Maven — Gradle is the build tool
- Lombok — records cover request and response bodies
- Keycloak — Spring Authorization Server stays within the Spring stack and its auto-configuration
- GraphQL (Spring for GraphQL) — the REST API covers the functionality; the natural candidate place is the gateway in 04, where a client needs data aggregated from several services

## Consequences

Benefits:

- every stage is a finished, deployable application and a portfolio item
- the difference between two neighbouring directories shows one architectural step
- every technology from typical job postings is backed by working code in a finished stage, AWS already in the first one

Costs:

- code is duplicated between stages on purpose: each directory is a snapshot
- frozen stages still need dependency updates
- AWS resources cost money from the first stage; prices and free tier conditions are checked before 01 is deployed
