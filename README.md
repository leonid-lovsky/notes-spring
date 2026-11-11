# notes-spring

The evolution of one application — notes with users, sharing, notifications and search — from a classic monolith to a distributed, configurable, resilient system. The sequence of projects shows three lines at once: the development of the application, the mastering of technologies, and the growth from junior to senior and architect.

Every project, once finished, is a tested application in its own directory, ready to be run, operated and shown to an employer, and from stage 09 ready to be deployed on AWS; the actual deployment is postponed until it is needed. The first project is always the monolith; the next project starts as a copy of the previous one and adds one key technology, so the application grows iteratively and evenly. Functionality and HTTP API are fixed in the first project; stage 11 changes the authentication method once, from HTTP Basic to OAuth 2.0 / OpenID Connect, and the contract does not change afterwards.

Status: the plan is complete; no stage is implemented yet. Work starts with 01.

| # | Project | Change | Level |
|---|---|---|---|
| 01 | [monolith](01-monolith) | minimum viable product: REST API, PostgreSQL, Spring Security | junior |
| 02 | [flyway](02-flyway) | schema migrations | junior |
| 03 | [spring-rest-docs](03-spring-rest-docs) | API documentation from tests | junior |
| 04 | [openapi](04-openapi) | OpenAPI description and Swagger UI | junior |
| 05 | [actuator](05-actuator) | health, info and metrics | junior |
| 06 | [thymeleaf](06-thymeleaf) | browser interface with form login | junior |
| 07 | [docker](07-docker) | container image | junior |
| 08 | [compose](08-compose) | the whole stage from one Compose file | junior |
| 09 | [github-actions](09-github-actions) | pipeline with acceptance tests, deployment step ready | junior |
| 10 | [redis](10-redis) | cache | junior |
| 11 | [oauth2](11-oauth2) | OAuth2 with Spring Authorization Server | middle |
| 12 | [keycloak](12-keycloak) | Keycloak instead of Spring Authorization Server | middle |
| 13 | [modulith](13-modulith) | module boundaries, in-process events, outbox | middle |
| 14 | [prometheus](14-prometheus) | metrics collection and alerts | middle |
| 15 | [grafana](15-grafana) | dashboards | middle |
| 16 | [elasticsearch](16-elasticsearch) | search engine | middle |
| 17 | [spring-batch](17-spring-batch) | search index rebuilt by a batch job | middle |
| 18 | [jenkins](18-jenkins) | second equal pipeline | middle |
| 19 | [rabbitmq](19-rabbitmq) | notifications as a separate service | middle |
| 20 | [kafka](20-kafka) | events as a replayable log | middle |
| 21 | [micrometer-tracing](21-micrometer-tracing) | tracing and logs | senior |
| 22 | [spring-cloud-gateway](22-spring-cloud-gateway) | single entry point, shared session | senior |
| 23 | [http-service-clients](23-http-service-clients) | every module a service, contract tests, sagas | senior |
| 24 | [graphql](24-graphql) | combined reads across services | senior |
| 25 | [spring-cloud-config](25-spring-cloud-config) | central configuration and encrypted secrets | senior |
| 26 | [eureka](26-eureka) | service discovery | senior |
| 27 | [resilience4j](27-resilience4j) | timeouts, retries, circuit breakers, rate limiting | senior |
| 28 | [spring-grpc](28-spring-grpc) | hot internal call over gRPC | senior |
| 29 | [kubernetes](29-kubernetes) | the platform replaces Spring Cloud infrastructure, autoscaling | senior |
| 30 | [graalvm](30-graalvm) | native images | senior |
| 31 | [hexagonal](31-hexagonal) | storage port, database chosen by profile | architect |
| 32 | [mongodb](32-mongodb) | second storage adapter | architect |
| 33 | [spring-ai](33-spring-ai) | semantic search | architect |
| 34 | [webflux](34-webflux) | second web stack, choice by measurement | architect |

The same black-box [acceptance tests](acceptance-tests) run against every stage and prove that the behaviour of the API does not change.

Architecture decisions are recorded in [docs/adr](docs/adr). The previous attempt of the project is kept under the tag `legacy-2026-11`.
