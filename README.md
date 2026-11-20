# notes-spring

The evolution of one application — notes with users, sharing, notifications and search — from a classic monolith to a distributed, configurable, resilient system. The sequence of projects shows three lines at once: the development of the application, the mastering of technologies, and the growth from junior to senior and architect.

Every project, once finished, is a tested application in its own directory, ready to be run, operated and shown to an employer, and from stage 28 ready to be deployed on Amazon Web Services; the actual deployment is postponed until it is needed. The first project is always an elementary monolith with server-rendered pages; the next project starts as a copy of the previous one and adds one atomic step — a technology, a pattern or a function — so the application grows linearly from simple and elementary steps to complex and advanced ones. The REST API appears in stage 11, its contract is fixed by the acceptance tests in 13 and from then on grows only by addition; stage 39 changes the authentication method once, from HTTP Basic to OAuth 2.0 / OpenID Connect.

Status: the plan is complete; work on 01 is in progress.

| # | Project | Change | Level |
|---|---|---|---|
| 01 | [monolith](01-monolith) | notes in the browser: Gradle, Spring MVC, Thymeleaf, Spring Data JPA on Hibernate, PostgreSQL with `schema.sql`, `@Transactional`; tests on H2 | junior |
| 02 | [jakarta-validation](02-jakarta-validation) | form validation | junior |
| 03 | [pagination](03-pagination) | paginated and sorted list of notes | junior |
| 04 | [command-query-separation](04-command-query-separation) | commands and queries separated in the services | junior |
| 05 | [markdown](05-markdown) | notes in Markdown | junior |
| 06 | [flyway](06-flyway) | versioned schema migrations | junior |
| 07 | [spring-security](07-spring-security) | form login, session, users in the database with one role | junior |
| 08 | [note-ownership](08-note-ownership) | notes owned by users | junior |
| 09 | [user-registration](09-user-registration) | sign-up | junior |
| 10 | [method-security](10-method-security) | note sharing with roles | junior |
| 11 | [rest-api](11-rest-api) | JSON REST API with HTTP Basic next to the pages | junior |
| 12 | [problem-details](12-problem-details) | errors of the API as `ProblemDetail` (RFC 9457) | junior |
| 13 | [rest-assured](13-rest-assured) | black-box acceptance tests; the contract of the API is fixed | junior |
| 14 | [spring-rest-docs](14-spring-rest-docs) | API documentation from tests | junior |
| 15 | [openapi](15-openapi) | springdoc-openapi, Swagger UI | junior |
| 16 | [in-app-notifications](16-in-app-notifications) | notifications from Spring application events | junior |
| 17 | [server-sent-events](17-server-sent-events) | live notification feed | junior |
| 18 | [spring-boot-actuator](18-spring-boot-actuator) | health, info and metrics endpoints | junior |
| 19 | [structured-logging](19-structured-logging) | structured JSON logs | junior |
| 20 | [docker](20-docker) | container image with `bootBuildImage` | junior |
| 21 | [docker-compose](21-docker-compose) | one Compose file: PostgreSQL for `bootRun`, the whole stage for `docker compose up` | junior |
| 22 | [testcontainers](22-testcontainers) | tests on PostgreSQL in a container instead of H2 | junior |
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
| 45 | [spring-cloud-gateway](45-spring-cloud-gateway) | single entry point, seam for extracting services | senior |
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
| 57 | [spring-cloud-config](57-spring-cloud-config) | central configuration and encrypted secrets | senior |
| 58 | [eureka](58-eureka) | service discovery, Spring Cloud LoadBalancer | senior |
| 59 | [spring-framework-resilience-features](59-spring-framework-resilience-features) | timeouts and retries with Spring Framework | senior |
| 60 | [resilience4j](60-resilience4j) | circuit breakers and bulkheads through Spring Cloud Circuit Breaker | senior |
| 61 | [gateway-rate-limiting](61-gateway-rate-limiting) | request rate limiter of the gateway | senior |
| 62 | [graphql](62-graphql) | combined reads across services next to the unchanged REST API | senior |
| 63 | [saga](63-saga) | sagas over broker events with compensation | senior |
| 64 | [grpc](64-grpc) | hot internal call over gRPC | senior |
| 65 | [kubernetes](65-kubernetes) | k3s (local, ready for EC2), manifests, Spring Boot Kubernetes support | senior |
| 66 | [horizontal-pod-autoscaling](66-horizontal-pod-autoscaling) | services scale with load | senior |
| 67 | [graalvm](67-graalvm) | native images with Spring AOT | senior |
| 68 | [domain-driven-design](68-domain-driven-design) | aggregates, value objects, domain events | architect |
| 69 | [hexagonal-architecture](69-hexagonal-architecture) | storage port and contract test; PostgreSQL, MySQL, H2 | architect |
| 70 | [mongodb](70-mongodb) | second storage adapter on Spring Data MongoDB | architect |
| 71 | [command-query-responsibility-segregation](71-command-query-responsibility-segregation) | separate read and write models of the notes service | architect |
| 72 | [event-sourcing](72-event-sourcing) | notes stored as a sequence of events | architect |
| 73 | [spring-ai](73-spring-ai) | semantic search with Spring AI, Ollama, Elasticsearch vector store | architect |
| 74 | [spring-webflux](74-spring-webflux) | second web stack, choice by measurement | architect |

The same black-box [acceptance tests](acceptance-tests) run against every stage from 13 and prove that behaviour defined in an earlier stage does not change.

Architecture decisions are recorded in [docs/adr](docs/adr). The previous attempt of the project is kept under the tag `legacy-2026-11`.
