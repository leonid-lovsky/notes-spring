# notes-spring

One application — notes in Markdown with users, sharing, notifications and search — evolves from a classic monolith into a distributed, configurable, resilient system. Each stage is a finished, tested application in its own directory that adds one step to the previous stage, so the sequence shows the development of the application, the mastering of Spring technologies and the path from junior to architect.

The REST API appears in 14 and is fixed by the acceptance tests in 16; from then on it grows only by addition, and only the authentication method changes once, in 38. From 27 every stage is ready to be deployed on Amazon Web Services, with the deployment switched off until it is needed.

Status: the plan is complete; work on 01 is in progress.

| # | Project | Change | Level |
|---|---|---|---|
| 01 | [monolith](01-monolith) | notes in the browser: Gradle, Spring MVC, Thymeleaf, Spring Data JPA on Hibernate, PostgreSQL or MySQL selected by profile, its server in a Docker container, the schema from `schema-postgresql.sql` or `schema-mysql.sql`, `@Transactional`; tests against every DBMS with Testcontainers | junior |
| 02 | [jakarta-validation](02-jakarta-validation) | form validation | junior |
| 03 | [markdown](03-markdown) | notes in Markdown | junior |
| 04 | [pagination](04-pagination) | paginated and sorted list of notes | junior |
| 05 | [command-query-separation](05-command-query-separation) | commands and queries separated in the services | junior |
| 06 | [spring-security](06-spring-security) | form login, session, users in the database with one role | junior |
| 07 | [user-registration](07-user-registration) | sign-up | junior |
| 08 | [spring-boot-sql-initialization](08-spring-boot-sql-initialization) | schema from `schema.sql`, entities validated against it | junior |
| 09 | [flyway](09-flyway) | versioned schema migrations for every DBMS | junior |
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
| 22 | [docker-compose](22-docker-compose) | one Compose file: the DBMS servers for `bootRun`, the whole stage for `docker compose up` | junior |
| 23 | [twelve-factor-app](23-twelve-factor-app) | configuration from the environment, stateless processes, one image for every environment | junior |
| 24 | [nginx](24-nginx) | TLS reverse proxy in front of the application | junior |
| 25 | [github-actions](25-github-actions) | build and test pipeline | junior |
| 26 | [jenkins](26-jenkins) | second equal pipeline, Configuration as Code | junior |
| 27 | [amazon-web-services](27-amazon-web-services) | deployment step to Amazon Web Services (OIDC, ECR, EC2), switched off | junior |
| 28 | [spring-cache-abstraction](28-spring-cache-abstraction) | cache of frequent reads in memory | middle |
| 29 | [redis-cache](29-redis-cache) | the same cache in Redis | middle |
| 30 | [spring-session](30-spring-session) | session in Redis, two instances | middle |
| 31 | [redis-pub-sub](31-redis-pub-sub) | live feed across instances | middle |
| 32 | [prometheus](32-prometheus) | metrics collection and alerts | middle |
| 33 | [grafana](33-grafana) | dashboards | middle |
| 34 | [grafana-loki](34-grafana-loki) | logs collected in Grafana Loki | middle |
| 35 | [spring-modulith](35-spring-modulith) | module boundaries, event publication registry | middle |
| 36 | [elasticsearch](36-elasticsearch) | full-text search in a separate read model | middle |
| 37 | [spring-batch](37-spring-batch) | rebuild of the search index by a batch job | middle |
| 38 | [spring-authorization-server](38-spring-authorization-server) | OAuth2 instead of passwords: `auth-server`, Resource Server and Client | middle |
| 39 | [email-verification](39-email-verification) | email confirmation in `auth-server` | middle |
| 40 | [phone-verification](40-phone-verification) | phone confirmation in `auth-server` through Amazon SNS | middle |
| 41 | [multi-factor-authentication](41-multi-factor-authentication) | second factor with Spring Security | middle |
| 42 | [keycloak](42-keycloak) | Keycloak instead of `auth-server`, realm as code | middle |
| 43 | [google-docs-export](43-google-docs-export) | export of a note to Google Docs | middle |
| 44 | [spring-cloud-gateway](44-spring-cloud-gateway) | single entry point that hides how the system is split | senior |
| 45 | [token-relay](45-token-relay) | browser sign-in at the gateway, token relay | senior |
| 46 | [react](46-react) | single-page application behind the gateway | senior |
| 47 | [progressive-web-app](47-progressive-web-app) | installable application that works offline | senior |
| 48 | [react-native](48-react-native) | mobile client, public OAuth2 client with PKCE | senior |
| 49 | [rabbitmq](49-rabbitmq) | notifications as the first separate service | senior |
| 50 | [apache-kafka](50-apache-kafka) | Apache Kafka as the alternative broker selected by configuration | senior |
| 51 | [micrometer-tracing](51-micrometer-tracing) | trace context across processes, trace identifiers in the logs | senior |
| 52 | [grafana-tempo](52-grafana-tempo) | traces stored and shown in Grafana | senior |
| 53 | [spring-cloud-openfeign](53-spring-cloud-openfeign) | every module a service, calls through OpenFeign as an intermediate step | senior |
| 54 | [http-service-clients](54-http-service-clients) | Spring HTTP service clients instead of OpenFeign | senior |
| 55 | [spring-cloud-contract](55-spring-cloud-contract) | consumer-driven contract tests | senior |
| 56 | [saga](56-saga) | sagas over broker events with compensation | senior |
| 57 | [spring-cloud-config](57-spring-cloud-config) | central configuration and encrypted secrets | senior |
| 58 | [eureka](58-eureka) | service discovery, Spring Cloud LoadBalancer | senior |
| 59 | [spring-framework-resilience-features](59-spring-framework-resilience-features) | timeouts and retries with Spring Framework | senior |
| 60 | [resilience4j](60-resilience4j) | circuit breakers and bulkheads through Spring Cloud Circuit Breaker | senior |
| 61 | [gateway-rate-limiting](61-gateway-rate-limiting) | request rate limiter of the gateway | senior |
| 62 | [graphql](62-graphql) | combined reads across services next to the unchanged REST API | senior |
| 63 | [grpc](63-grpc) | hot internal call over gRPC | senior |
| 64 | [kubernetes](64-kubernetes) | the same services on k3s (local, ready for EC2), manifests, probes | senior |
| 65 | [kubernetes-service-discovery-and-configuration](65-kubernetes-service-discovery-and-configuration) | Kubernetes Services, ConfigMaps and Secrets instead of Eureka and Spring Cloud Config | senior |
| 66 | [horizontal-pod-autoscaling](66-horizontal-pod-autoscaling) | services scale with load | senior |
| 67 | [graalvm](67-graalvm) | native images with Spring AOT | senior |
| 68 | [domain-driven-design](68-domain-driven-design) | aggregates, value objects, domain events | architect |
| 69 | [hexagonal-architecture](69-hexagonal-architecture) | storage port and contract test; PostgreSQL and MySQL | architect |
| 70 | [mongodb](70-mongodb) | second storage adapter on Spring Data MongoDB | architect |
| 71 | [command-query-responsibility-segregation](71-command-query-responsibility-segregation) | separate read and write models of the notes service | architect |
| 72 | [event-sourcing](72-event-sourcing) | notes stored as a sequence of events | architect |
| 73 | [spring-ai](73-spring-ai) | semantic search with Spring AI, Ollama, Elasticsearch vector store | architect |
| 74 | [spring-webflux](74-spring-webflux) | second web stack, choice by measurement | architect |

The black-box [acceptance tests](acceptance-tests) run against every stage from 16. Architecture decisions are recorded in [docs/adr](docs/adr); the previous attempt of the project is kept under the tag `legacy-2026-11`.
