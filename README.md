# notes-spring

One application — notes in Markdown with users, sharing, notifications and search — evolves from a classic monolith into a distributed, configurable, resilient system. Each stage is a finished, tested application in its own directory that adds one step to the previous stage, so the sequence shows the development of the application, the mastering of Spring technologies and the path from junior to architect.

The REST API appears in 14 and is fixed by the acceptance tests in 16; from then on it grows only by addition, and only the authentication method changes once, in 39. From 28 every stage is ready to be deployed on Amazon Web Services, with the deployment switched off until it is needed.

Status: the plan is complete; work on 01 is in progress.

| # | Project | Change | Level |
|---|---|---|---|
| 01 | [monolith](01-monolith) | notes in the browser: Gradle, Spring MVC, Thymeleaf, Spring Data JPA on Hibernate, PostgreSQL in a Docker container with the schema from `schema-postgresql.sql`, `@Transactional`; tests on PostgreSQL with Testcontainers | junior |
| 02 | [jakarta-validation](02-jakarta-validation) | form validation | junior |
| 03 | [markdown](03-markdown) | notes in Markdown | junior |
| 04 | [pagination](04-pagination) | paginated and sorted list of notes | junior |
| 05 | [command-query-separation](05-command-query-separation) | commands and queries separated in the services | junior |
| 06 | [spring-security](06-spring-security) | form login, session, users in the database with one role | junior |
| 07 | [user-registration](07-user-registration) | sign-up | junior |
| 08 | [spring-boot-sql-initialization](08-spring-boot-sql-initialization) | schema from `schema.sql`, entities validated against it | junior |
| 09 | [flyway](09-flyway) | versioned schema migrations | junior |
| 10 | [note-ownership](10-note-ownership) | notes owned by users | junior |
| 11 | [method-security](11-method-security) | note sharing with roles | junior |
| 12 | [in-app-notifications](12-in-app-notifications) | notifications from Spring application events | junior |
| 13 | [server-sent-events](13-server-sent-events) | live notification feed | junior |
| 14 | [rest-api](14-rest-api) | JSON REST API with HTTP Basic next to the pages | junior |
| 15 | [problem-details](15-problem-details) | errors of the API as `ProblemDetail` (RFC 9457) | junior |
| 16 | [rest-assured](16-rest-assured) | black-box acceptance tests; the contract of the API is fixed | junior |
| 17 | [spring-rest-docs](17-spring-rest-docs) | API documentation from tests | junior |
| 18 | [openapi](18-openapi) | springdoc-openapi, Swagger UI | junior |
| 19 | [spring-boot-actuator](19-spring-boot-actuator) | health, info and metrics endpoints | junior |
| 20 | [structured-logging](20-structured-logging) | structured JSON logs | junior |
| 21 | [docker](21-docker) | container image with `bootBuildImage` | junior |
| 22 | [docker-compose](22-docker-compose) | one Compose file: PostgreSQL for `bootRun`, the whole stage for `docker compose up` | junior |
| 23 | [mysql](23-mysql) | MySQL or PostgreSQL by profile, migrations per vendor | junior |
| 24 | [twelve-factor-app](24-twelve-factor-app) | configuration from the environment, stateless processes, one image for every environment | junior |
| 25 | [nginx](25-nginx) | TLS reverse proxy in front of the application | junior |
| 26 | [github-actions](26-github-actions) | build and test pipeline | junior |
| 27 | [jenkins](27-jenkins) | second equal pipeline, Configuration as Code | junior |
| 28 | [amazon-web-services](28-amazon-web-services) | deployment step to Amazon Web Services (OIDC, ECR, EC2), switched off | junior |
| 29 | [spring-cache-abstraction](29-spring-cache-abstraction) | cache of frequent reads in memory | middle |
| 30 | [redis-cache](30-redis-cache) | the same cache in Redis | middle |
| 31 | [spring-session](31-spring-session) | session in Redis, two instances | middle |
| 32 | [redis-pub-sub](32-redis-pub-sub) | live feed across instances | middle |
| 33 | [prometheus](33-prometheus) | metrics collection and alerts | middle |
| 34 | [grafana](34-grafana) | dashboards | middle |
| 35 | [grafana-loki](35-grafana-loki) | logs collected in Grafana Loki | middle |
| 36 | [spring-modulith](36-spring-modulith) | module boundaries, event publication registry | middle |
| 37 | [elasticsearch](37-elasticsearch) | full-text search in a separate read model | middle |
| 38 | [spring-batch](38-spring-batch) | rebuild of the search index by a batch job | middle |
| 39 | [spring-authorization-server](39-spring-authorization-server) | OAuth2 instead of passwords: `auth-server`, Resource Server and Client | middle |
| 40 | [email-verification](40-email-verification) | email confirmation in `auth-server` | middle |
| 41 | [phone-verification](41-phone-verification) | phone confirmation in `auth-server` through Amazon SNS | middle |
| 42 | [multi-factor-authentication](42-multi-factor-authentication) | second factor with Spring Security | middle |
| 43 | [keycloak](43-keycloak) | Keycloak instead of `auth-server`, realm as code | middle |
| 44 | [google-docs-export](44-google-docs-export) | export of a note to Google Docs | middle |
| 45 | [spring-cloud-gateway](45-spring-cloud-gateway) | single entry point that hides how the system is split | senior |
| 46 | [token-relay](46-token-relay) | browser sign-in at the gateway, token relay | senior |
| 47 | [react](47-react) | single-page application behind the gateway | senior |
| 48 | [progressive-web-app](48-progressive-web-app) | installable application that works offline | senior |
| 49 | [react-native](49-react-native) | mobile client, public OAuth2 client with PKCE | senior |
| 50 | [rabbitmq](50-rabbitmq) | notifications as the first separate service | senior |
| 51 | [apache-kafka](51-apache-kafka) | Apache Kafka as the alternative broker selected by configuration | senior |
| 52 | [micrometer-tracing](52-micrometer-tracing) | trace context across processes, trace identifiers in the logs | senior |
| 53 | [grafana-tempo](53-grafana-tempo) | traces stored and shown in Grafana | senior |
| 54 | [spring-cloud-openfeign](54-spring-cloud-openfeign) | every module a service, calls through OpenFeign as an intermediate step | senior |
| 55 | [http-service-clients](55-http-service-clients) | Spring HTTP service clients instead of OpenFeign | senior |
| 56 | [spring-cloud-contract](56-spring-cloud-contract) | consumer-driven contract tests | senior |
| 57 | [saga](57-saga) | sagas over broker events with compensation | senior |
| 58 | [spring-cloud-config](58-spring-cloud-config) | central configuration and encrypted secrets | senior |
| 59 | [eureka](59-eureka) | service discovery, Spring Cloud LoadBalancer | senior |
| 60 | [spring-framework-resilience-features](60-spring-framework-resilience-features) | timeouts and retries with Spring Framework | senior |
| 61 | [resilience4j](61-resilience4j) | circuit breakers and bulkheads through Spring Cloud Circuit Breaker | senior |
| 62 | [gateway-rate-limiting](62-gateway-rate-limiting) | request rate limiter of the gateway | senior |
| 63 | [graphql](63-graphql) | combined reads across services next to the unchanged REST API | senior |
| 64 | [grpc](64-grpc) | hot internal call over gRPC | senior |
| 65 | [kubernetes](65-kubernetes) | the same services on k3s (local, ready for EC2), manifests, probes | senior |
| 66 | [kubernetes-service-discovery-and-configuration](66-kubernetes-service-discovery-and-configuration) | Kubernetes Services, ConfigMaps and Secrets instead of Eureka and Spring Cloud Config | senior |
| 67 | [horizontal-pod-autoscaling](67-horizontal-pod-autoscaling) | services scale with load | senior |
| 68 | [graalvm](68-graalvm) | native images with Spring AOT | senior |
| 69 | [domain-driven-design](69-domain-driven-design) | aggregates, value objects, domain events | architect |
| 70 | [hexagonal-architecture](70-hexagonal-architecture) | storage port and contract test; PostgreSQL and MySQL | architect |
| 71 | [mongodb](71-mongodb) | second storage adapter on Spring Data MongoDB | architect |
| 72 | [command-query-responsibility-segregation](72-command-query-responsibility-segregation) | separate read and write models of the notes service | architect |
| 73 | [event-sourcing](73-event-sourcing) | notes stored as a sequence of events | architect |
| 74 | [spring-ai](74-spring-ai) | semantic search with Spring AI, Ollama, Elasticsearch vector store | architect |
| 75 | [spring-webflux](75-spring-webflux) | second web stack, choice by measurement | architect |

The black-box [acceptance tests](acceptance-tests) run against every stage from 16. Architecture decisions are recorded in [docs/adr](docs/adr); the previous attempt of the project is kept under the tag `legacy-2026-11`.
