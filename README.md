# notes-spring

One application — notes in Markdown with users, sharing, collaborative editing, notifications and search — evolves from a classic monolith into a distributed, configurable, resilient system. Each stage is a finished, tested application in its own directory that adds one step to the previous stage, so the sequence shows the development of the application, the mastering of Spring technologies and the path from junior to architect.

The REST API appears in 16 and is fixed by the acceptance tests in 18; from then on it grows only by addition, and only the authentication method changes once, in 40. From 29 every stage is ready to be deployed on Amazon Web Services, with the deployment switched off until it is needed.

Status: the plan is complete; work on 01 is in progress.

| # | Project | Change | Level |
|---|---|---|---|
| 01 | [monolith](01-monolith) | notes in the browser and the user model with its invariants: Gradle, Spring MVC, Thymeleaf, Spring Data JPA on Hibernate, PostgreSQL or MySQL selected by profile, its server in a Docker container, the schema from `schema-postgresql.sql` or `schema-mysql.sql`, `@Transactional`; tests against every DBMS with Testcontainers | junior |
| 02 | [jakarta-validation](02-jakarta-validation) | form validation | junior |
| 03 | [markdown](03-markdown) | notes in Markdown | junior |
| 04 | [pagination](04-pagination) | paginated and sorted list of notes | junior |
| 05 | [command-query-separation](05-command-query-separation) | commands and queries separated in the services | junior |
| 06 | [spring-security](06-spring-security) | form login, session, users in the database with one role | junior |
| 07 | [user-registration](07-user-registration) | sign-up | junior |
| 08 | [flyway](08-flyway) | versioned schema migrations for every DBMS | junior |
| 09 | [note-ownership](09-note-ownership) | notes owned by users | junior |
| 10 | [method-security](10-method-security) | note sharing with roles | junior |
| 11 | [optimistic-locking](11-optimistic-locking) | a save over a newer change of a note is refused | junior |
| 12 | [in-app-notifications](12-in-app-notifications) | notifications on the pages of the application from Spring application events | junior |
| 13 | [server-sent-events](13-server-sent-events) | live notification feed | junior |
| 14 | [websocket](14-websocket) | who has a note open and who edits it, saved changes at once on every open page | junior |
| 15 | [email-notifications](15-email-notifications) | notifications by email | junior |
| 16 | [rest-api](16-rest-api) | JSON REST API with HTTP Basic next to the pages | junior |
| 17 | [problem-details](17-problem-details) | errors of the API as `ProblemDetail` (RFC 9457) | junior |
| 18 | [rest-assured](18-rest-assured) | black-box acceptance tests; the contract of the API is fixed | junior |
| 19 | [spring-rest-docs](19-spring-rest-docs) | API documentation from tests | junior |
| 20 | [openapi](20-openapi) | springdoc-openapi, Swagger UI | junior |
| 21 | [spring-boot-actuator](21-spring-boot-actuator) | health, info and metrics endpoints | junior |
| 22 | [structured-logging](22-structured-logging) | structured JSON logs | junior |
| 23 | [docker](23-docker) | container image with `bootBuildImage` | junior |
| 24 | [docker-compose](24-docker-compose) | one Compose file: the DBMS servers for `bootRun`, the whole stage for `docker compose up` | junior |
| 25 | [twelve-factor-app](25-twelve-factor-app) | configuration from the environment, stateless processes, one image for every environment | junior |
| 26 | [nginx](26-nginx) | TLS reverse proxy in front of the application | junior |
| 27 | [github-actions](27-github-actions) | build and test pipeline | junior |
| 28 | [jenkins](28-jenkins) | second equal pipeline, Configuration as Code | junior |
| 29 | [amazon-web-services](29-amazon-web-services) | deployment step to Amazon Web Services (OIDC, ECR, EC2), switched off | junior |
| 30 | [spring-cache-abstraction](30-spring-cache-abstraction) | cache of frequent reads in memory | middle |
| 31 | [redis-cache](31-redis-cache) | the same cache in Redis | middle |
| 32 | [spring-session](32-spring-session) | session in Redis, two instances | middle |
| 33 | [redis-pub-sub](33-redis-pub-sub) | live feed and note presence across instances | middle |
| 34 | [prometheus](34-prometheus) | metrics collection and alerts | middle |
| 35 | [grafana](35-grafana) | dashboards | middle |
| 36 | [grafana-loki](36-grafana-loki) | logs collected in Grafana Loki | middle |
| 37 | [spring-modulith](37-spring-modulith) | module boundaries, event publication registry | middle |
| 38 | [elasticsearch](38-elasticsearch) | full-text search in a separate read model | middle |
| 39 | [spring-batch](39-spring-batch) | rebuild of the search index by a batch job | middle |
| 40 | [spring-authorization-server](40-spring-authorization-server) | OAuth2 instead of passwords: `auth-server`, Resource Server and Client | middle |
| 41 | [email-verification](41-email-verification) | email confirmation in `auth-server` | middle |
| 42 | [phone-verification](42-phone-verification) | phone confirmation in `auth-server` through Amazon SNS | middle |
| 43 | [multi-factor-authentication](43-multi-factor-authentication) | second factor with Spring Security | middle |
| 44 | [keycloak](44-keycloak) | Keycloak instead of `auth-server`, realm as code | middle |
| 45 | [google-docs-export](45-google-docs-export) | export of a note to Google Docs | middle |
| 46 | [spring-cloud-gateway](46-spring-cloud-gateway) | single entry point that hides how the system is split | senior |
| 47 | [token-relay](47-token-relay) | browser sign-in at the gateway, token relay | senior |
| 48 | [react](48-react) | single-page application behind the gateway | senior |
| 49 | [collaborative-editing](49-collaborative-editing) | several editors type in one note at the same time, merged by a CRDT | senior |
| 50 | [progressive-web-app](50-progressive-web-app) | installable application that works offline | senior |
| 51 | [web-push](51-web-push) | desktop notifications by Web Push, also with the application closed | senior |
| 52 | [react-native](52-react-native) | mobile client, public OAuth2 client with PKCE | senior |
| 53 | [mobile-push-notifications](53-mobile-push-notifications) | push notifications to the mobile client through Amazon SNS | senior |
| 54 | [rabbitmq](54-rabbitmq) | notifications as the first separate service | senior |
| 55 | [apache-kafka](55-apache-kafka) | Apache Kafka as the alternative broker selected by configuration | senior |
| 56 | [micrometer-tracing](56-micrometer-tracing) | trace context across processes, trace identifiers in the logs | senior |
| 57 | [grafana-tempo](57-grafana-tempo) | traces stored and shown in Grafana | senior |
| 58 | [spring-cloud-openfeign](58-spring-cloud-openfeign) | every module a service, calls through OpenFeign as an intermediate step | senior |
| 59 | [http-service-clients](59-http-service-clients) | Spring HTTP service clients instead of OpenFeign | senior |
| 60 | [spring-cloud-contract](60-spring-cloud-contract) | consumer-driven contract tests | senior |
| 61 | [saga](61-saga) | sagas over broker events with compensation | senior |
| 62 | [spring-cloud-config](62-spring-cloud-config) | central configuration and encrypted secrets | senior |
| 63 | [eureka](63-eureka) | service discovery, Spring Cloud LoadBalancer | senior |
| 64 | [spring-framework-resilience-features](64-spring-framework-resilience-features) | timeouts and retries with Spring Framework | senior |
| 65 | [resilience4j](65-resilience4j) | circuit breakers and bulkheads through Spring Cloud Circuit Breaker | senior |
| 66 | [gateway-rate-limiting](66-gateway-rate-limiting) | request rate limiter of the gateway | senior |
| 67 | [graphql](67-graphql) | combined reads across services next to the unchanged REST API | senior |
| 68 | [grpc](68-grpc) | hot internal call over gRPC | senior |
| 69 | [kubernetes](69-kubernetes) | the same services on k3s (local, ready for EC2), manifests, probes | senior |
| 70 | [kubernetes-service-discovery-and-configuration](70-kubernetes-service-discovery-and-configuration) | Kubernetes Services, ConfigMaps and Secrets instead of Eureka and Spring Cloud Config | senior |
| 71 | [horizontal-pod-autoscaling](71-horizontal-pod-autoscaling) | services scale with load | senior |
| 72 | [graalvm](72-graalvm) | native images with Spring AOT | senior |
| 73 | [domain-driven-design](73-domain-driven-design) | aggregates, value objects, domain events | architect |
| 74 | [hexagonal-architecture](74-hexagonal-architecture) | storage port and contract test; PostgreSQL and MySQL | architect |
| 75 | [mongodb](75-mongodb) | second storage adapter on Spring Data MongoDB | architect |
| 76 | [command-query-responsibility-segregation](76-command-query-responsibility-segregation) | separate read and write models of the notes service | architect |
| 77 | [event-sourcing](77-event-sourcing) | notes stored as a sequence of events | architect |
| 78 | [spring-ai](78-spring-ai) | semantic search with Spring AI, Ollama, Elasticsearch vector store | architect |
| 79 | [spring-webflux](79-spring-webflux) | second web stack, choice by measurement | architect |

The black-box [acceptance tests](acceptance-tests) run against every stage from 18. Architecture decisions are recorded in [docs/adr](docs/adr); the previous attempt of the project is kept under the tag `legacy-2026-11`.
