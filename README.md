# notes-spring

The evolution of one application — notes with users, sharing, notifications and search — from a classic monolith to a distributed, configurable, resilient system. The sequence of projects shows three lines at once: the development of the application, the mastering of technologies, and the growth from junior to senior and architect.

Every project, once finished, is a tested application in its own directory, ready to be run, operated and shown to an employer, and from stage 22 ready to be deployed on AWS; the actual deployment is postponed until it is needed. The first project is always the monolith with server-rendered pages; the next project starts as a copy of the previous one and adds one atomic step — a technology, a pattern or a function — so the application grows linearly from simple and elementary steps to complex and advanced ones. The REST API appears in stage 11 and from then on grows only by addition; stage 33 changes the authentication method once, from HTTP Basic to OAuth 2.0 / OpenID Connect.

Status: the plan is complete; work on 01 is in progress.

| # | Project | Change | Level |
|---|---|---|---|
| 01 | [monolith](01-monolith) | notes in the browser: Gradle, Spring MVC, Thymeleaf, Spring Data JPA on Hibernate, PostgreSQL with `schema.sql`, `@Transactional`, Testcontainers | junior |
| 02 | [bean-validation](02-bean-validation) | form validation | junior |
| 03 | [cqs](03-cqs) | command–query separation in the services | junior |
| 04 | [markdown](04-markdown) | notes in Markdown | junior |
| 05 | [flyway](05-flyway) | versioned schema migrations | junior |
| 06 | [mysql](06-mysql) | MySQL or PostgreSQL by profile, migrations per vendor | junior |
| 07 | [spring-security](07-spring-security) | form login, session, users in the database with one role | junior |
| 08 | [multi-user](08-multi-user) | notes owned by users | junior |
| 09 | [registration](09-registration) | sign-up | junior |
| 10 | [method-security](10-method-security) | note sharing with roles | junior |
| 11 | [rest-api](11-rest-api) | REST API with HTTP Basic and `ProblemDetail`; the acceptance tests start | junior |
| 12 | [spring-rest-docs](12-spring-rest-docs) | API documentation from tests | junior |
| 13 | [openapi](13-openapi) | springdoc-openapi, Swagger UI | junior |
| 14 | [notifications](14-notifications) | notifications from Spring application events | junior |
| 15 | [server-sent-events](15-server-sent-events) | live notification feed | junior |
| 16 | [actuator](16-actuator) | health, info and metrics endpoints | junior |
| 17 | [docker](17-docker) | container image with `bootBuildImage` | junior |
| 18 | [compose](18-compose) | the whole stage from one Compose file, TLS reverse proxy for EC2 | junior |
| 19 | [twelve-factor](19-twelve-factor) | configuration from the environment, stateless processes, one image for every environment | junior |
| 20 | [github-actions](20-github-actions) | build and test pipeline | junior |
| 21 | [jenkins](21-jenkins) | second equal pipeline, Configuration as Code | junior |
| 22 | [aws](22-aws) | deployment step to AWS (OIDC, ECR, EC2), switched off | junior |
| 23 | [redis](23-redis) | cache through Spring Cache | middle |
| 24 | [spring-session](24-spring-session) | session in Redis, two instances | middle |
| 25 | [redis-pub-sub](25-redis-pub-sub) | live feed across instances | middle |
| 26 | [structured-logging](26-structured-logging) | structured JSON logs | middle |
| 27 | [prometheus](27-prometheus) | metrics collection and alerts | middle |
| 28 | [grafana](28-grafana) | dashboards | middle |
| 29 | [loki](29-loki) | logs collected in Grafana Loki | middle |
| 30 | [modulith](30-modulith) | module boundaries, event publication registry | middle |
| 31 | [elasticsearch](31-elasticsearch) | full-text search in a separate read model | middle |
| 32 | [spring-batch](32-spring-batch) | rebuild of the search index by a batch job | middle |
| 33 | [authorization-server](33-authorization-server) | OAuth2 instead of passwords: `auth-server` on Spring Authorization Server, Resource Server and Client | middle |
| 34 | [mail](34-mail) | email confirmation in `auth-server` | middle |
| 35 | [sms](35-sms) | phone confirmation in `auth-server` through Amazon SNS | middle |
| 36 | [mfa](36-mfa) | second factor with Spring Security multi-factor authentication | middle |
| 37 | [keycloak](37-keycloak) | Keycloak instead of `auth-server`, realm as code | middle |
| 38 | [google-docs](38-google-docs) | export of a note to Google Docs | middle |
| 39 | [spring-cloud-gateway](39-spring-cloud-gateway) | single entry point, seam for extracting services | senior |
| 40 | [token-relay](40-token-relay) | browser sign-in at the gateway, token relay, session at the gateway | senior |
| 41 | [react](41-react) | single-page application behind the gateway | senior |
| 42 | [pwa](42-pwa) | installable application that works offline | senior |
| 43 | [react-native](43-react-native) | mobile client, public OAuth2 client with PKCE | senior |
| 44 | [rabbitmq](44-rabbitmq) | notifications as the first separate service: Spring AMQP, event externalization | senior |
| 45 | [kafka](45-kafka) | Kafka as the alternative broker selected by configuration | senior |
| 46 | [micrometer-tracing](46-micrometer-tracing) | tracing across processes with OpenTelemetry | senior |
| 47 | [openfeign](47-openfeign) | every module a service, calls through OpenFeign as an intermediate step | senior |
| 48 | [http-service-clients](48-http-service-clients) | Spring HTTP service clients instead of OpenFeign | senior |
| 49 | [spring-cloud-config](49-spring-cloud-config) | central configuration and encrypted secrets | senior |
| 50 | [eureka](50-eureka) | service discovery, Spring Cloud LoadBalancer | senior |
| 51 | [retry](51-retry) | timeouts and retries with Spring Framework | senior |
| 52 | [resilience4j](52-resilience4j) | circuit breakers and bulkheads through Spring Cloud Circuit Breaker | senior |
| 53 | [rate-limiting](53-rate-limiting) | request rate limiter of the gateway | senior |
| 54 | [graphql](54-graphql) | combined reads across services next to the unchanged REST API | senior |
| 55 | [spring-cloud-contract](55-spring-cloud-contract) | consumer-driven contract tests | senior |
| 56 | [sagas](56-sagas) | sagas over broker events with compensation | senior |
| 57 | [spring-grpc](57-spring-grpc) | hot internal call over gRPC | senior |
| 58 | [kubernetes](58-kubernetes) | k3s (local, ready for EC2), manifests, Spring Boot Kubernetes support | senior |
| 59 | [autoscaling](59-autoscaling) | Horizontal Pod Autoscaler | senior |
| 60 | [graalvm](60-graalvm) | native images with Spring AOT | senior |
| 61 | [ddd](61-ddd) | aggregates, value objects, domain events | architect |
| 62 | [hexagonal](62-hexagonal) | storage port and contract test, Clean Architecture; PostgreSQL, MySQL, H2 | architect |
| 63 | [mongodb](63-mongodb) | second storage adapter on Spring Data MongoDB | architect |
| 64 | [cqrs](64-cqrs) | separate read and write models of the notes service | architect |
| 65 | [event-sourcing](65-event-sourcing) | notes stored as a sequence of events | architect |
| 66 | [spring-ai](66-spring-ai) | semantic search with Spring AI, Ollama, Elasticsearch vector store | architect |
| 67 | [webflux](67-webflux) | second web stack, choice by measurement | architect |

The same black-box [acceptance tests](acceptance-tests) run against every stage from 11 and prove that behaviour defined in an earlier stage does not change.

Architecture decisions are recorded in [docs/adr](docs/adr). The previous attempt of the project is kept under the tag `legacy-2026-11`.
