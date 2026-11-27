# notes-spring

One application — notes in Markdown with users, sharing, notifications and search — evolves from a classic monolith into a distributed, configurable, resilient system. Each stage is a finished, tested application in its own directory that adds one step to the previous stage, so the sequence shows the development of the application, the mastering of Spring technologies and the path from junior to architect.

The REST API appears in 13 and is fixed by the acceptance tests in 15; from then on it grows only by addition, and only the authentication method changes once, in 37. From 26 every stage is ready to be deployed on Amazon Web Services, with the deployment switched off until it is needed.

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
| 11 | [in-app-notifications](11-in-app-notifications) | notifications from Spring application events | junior |
| 12 | [server-sent-events](12-server-sent-events) | live notification feed | junior |
| 13 | [rest-api](13-rest-api) | JSON REST API with HTTP Basic next to the pages | junior |
| 14 | [problem-details](14-problem-details) | errors of the API as `ProblemDetail` (RFC 9457) | junior |
| 15 | [rest-assured](15-rest-assured) | black-box acceptance tests; the contract of the API is fixed | junior |
| 16 | [spring-rest-docs](16-spring-rest-docs) | API documentation from tests | junior |
| 17 | [openapi](17-openapi) | springdoc-openapi, Swagger UI | junior |
| 18 | [spring-boot-actuator](18-spring-boot-actuator) | health, info and metrics endpoints | junior |
| 19 | [structured-logging](19-structured-logging) | structured JSON logs | junior |
| 20 | [docker](20-docker) | container image with `bootBuildImage` | junior |
| 21 | [docker-compose](21-docker-compose) | one Compose file: the DBMS servers for `bootRun`, the whole stage for `docker compose up` | junior |
| 22 | [twelve-factor-app](22-twelve-factor-app) | configuration from the environment, stateless processes, one image for every environment | junior |
| 23 | [nginx](23-nginx) | TLS reverse proxy in front of the application | junior |
| 24 | [github-actions](24-github-actions) | build and test pipeline | junior |
| 25 | [jenkins](25-jenkins) | second equal pipeline, Configuration as Code | junior |
| 26 | [amazon-web-services](26-amazon-web-services) | deployment step to Amazon Web Services (OIDC, ECR, EC2), switched off | junior |
| 27 | [spring-cache-abstraction](27-spring-cache-abstraction) | cache of frequent reads in memory | middle |
| 28 | [redis-cache](28-redis-cache) | the same cache in Redis | middle |
| 29 | [spring-session](29-spring-session) | session in Redis, two instances | middle |
| 30 | [redis-pub-sub](30-redis-pub-sub) | live feed across instances | middle |
| 31 | [prometheus](31-prometheus) | metrics collection and alerts | middle |
| 32 | [grafana](32-grafana) | dashboards | middle |
| 33 | [grafana-loki](33-grafana-loki) | logs collected in Grafana Loki | middle |
| 34 | [spring-modulith](34-spring-modulith) | module boundaries, event publication registry | middle |
| 35 | [elasticsearch](35-elasticsearch) | full-text search in a separate read model | middle |
| 36 | [spring-batch](36-spring-batch) | rebuild of the search index by a batch job | middle |
| 37 | [spring-authorization-server](37-spring-authorization-server) | OAuth2 instead of passwords: `auth-server`, Resource Server and Client | middle |
| 38 | [email-verification](38-email-verification) | email confirmation in `auth-server` | middle |
| 39 | [phone-verification](39-phone-verification) | phone confirmation in `auth-server` through Amazon SNS | middle |
| 40 | [multi-factor-authentication](40-multi-factor-authentication) | second factor with Spring Security | middle |
| 41 | [keycloak](41-keycloak) | Keycloak instead of `auth-server`, realm as code | middle |
| 42 | [google-docs-export](42-google-docs-export) | export of a note to Google Docs | middle |
| 43 | [spring-cloud-gateway](43-spring-cloud-gateway) | single entry point that hides how the system is split | senior |
| 44 | [token-relay](44-token-relay) | browser sign-in at the gateway, token relay | senior |
| 45 | [react](45-react) | single-page application behind the gateway | senior |
| 46 | [progressive-web-app](46-progressive-web-app) | installable application that works offline | senior |
| 47 | [react-native](47-react-native) | mobile client, public OAuth2 client with PKCE | senior |
| 48 | [rabbitmq](48-rabbitmq) | notifications as the first separate service | senior |
| 49 | [apache-kafka](49-apache-kafka) | Apache Kafka as the alternative broker selected by configuration | senior |
| 50 | [micrometer-tracing](50-micrometer-tracing) | trace context across processes, trace identifiers in the logs | senior |
| 51 | [grafana-tempo](51-grafana-tempo) | traces stored and shown in Grafana | senior |
| 52 | [spring-cloud-openfeign](52-spring-cloud-openfeign) | every module a service, calls through OpenFeign as an intermediate step | senior |
| 53 | [http-service-clients](53-http-service-clients) | Spring HTTP service clients instead of OpenFeign | senior |
| 54 | [spring-cloud-contract](54-spring-cloud-contract) | consumer-driven contract tests | senior |
| 55 | [saga](55-saga) | sagas over broker events with compensation | senior |
| 56 | [spring-cloud-config](56-spring-cloud-config) | central configuration and encrypted secrets | senior |
| 57 | [eureka](57-eureka) | service discovery, Spring Cloud LoadBalancer | senior |
| 58 | [spring-framework-resilience-features](58-spring-framework-resilience-features) | timeouts and retries with Spring Framework | senior |
| 59 | [resilience4j](59-resilience4j) | circuit breakers and bulkheads through Spring Cloud Circuit Breaker | senior |
| 60 | [gateway-rate-limiting](60-gateway-rate-limiting) | request rate limiter of the gateway | senior |
| 61 | [graphql](61-graphql) | combined reads across services next to the unchanged REST API | senior |
| 62 | [grpc](62-grpc) | hot internal call over gRPC | senior |
| 63 | [kubernetes](63-kubernetes) | the same services on k3s (local, ready for EC2), manifests, probes | senior |
| 64 | [kubernetes-service-discovery-and-configuration](64-kubernetes-service-discovery-and-configuration) | Kubernetes Services, ConfigMaps and Secrets instead of Eureka and Spring Cloud Config | senior |
| 65 | [horizontal-pod-autoscaling](65-horizontal-pod-autoscaling) | services scale with load | senior |
| 66 | [graalvm](66-graalvm) | native images with Spring AOT | senior |
| 67 | [domain-driven-design](67-domain-driven-design) | aggregates, value objects, domain events | architect |
| 68 | [hexagonal-architecture](68-hexagonal-architecture) | storage port and contract test; PostgreSQL and MySQL | architect |
| 69 | [mongodb](69-mongodb) | second storage adapter on Spring Data MongoDB | architect |
| 70 | [command-query-responsibility-segregation](70-command-query-responsibility-segregation) | separate read and write models of the notes service | architect |
| 71 | [event-sourcing](71-event-sourcing) | notes stored as a sequence of events | architect |
| 72 | [spring-ai](72-spring-ai) | semantic search with Spring AI, Ollama, Elasticsearch vector store | architect |
| 73 | [spring-webflux](73-spring-webflux) | second web stack, choice by measurement | architect |

The black-box [acceptance tests](acceptance-tests) run against every stage from 15. Architecture decisions are recorded in [docs/adr](docs/adr); the previous attempt of the project is kept under the tag `legacy-2026-11`.
