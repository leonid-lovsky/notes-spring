# notes-spring

The evolution of one application — notes with users, sharing, notifications and search — from a classic monolith to a distributed, configurable, resilient system. The sequence of projects shows three lines at once: the development of the application, the mastering of technologies, and the growth from junior to senior and architect.

Every project is a finished, tested application in its own directory, ready to be deployed on AWS, operated and shown to an employer; the actual deployment is postponed until it is needed. The next project starts as a copy of the previous one and adds one change of comparable size, so the application grows iteratively and evenly. Functionality and HTTP API are fixed in the first project; the second changes the authentication method once, from HTTP Basic to OAuth 2.0 / OpenID Connect, and the contract does not change afterwards.

| # | Project | Change | Level |
|---|---|---|---|
| 01 | [monolith](01-monolith) | minimum viable product: REST API, PostgreSQL, Spring Security, Docker Compose, GitHub Actions | junior |
| 02 | [oauth2](02-oauth2) | OAuth2 with an own authorization server | junior |
| 03 | [thymeleaf](03-thymeleaf) | browser interface with OAuth2 sign-in | junior |
| 04 | [modulith](04-modulith) | module boundaries, in-process events, outbox | junior+ |
| 05 | [terraform](05-terraform) | AWS environment as code | middle |
| 06 | [jenkins](06-jenkins) | second equal pipeline | middle |
| 07 | [gitlab-ci](07-gitlab-ci) | third equal pipeline | middle |
| 08 | [sonarqube](08-sonarqube) | quality gate in every pipeline | middle |
| 09 | [prometheus](09-prometheus) | metrics collection and alerts | middle |
| 10 | [grafana](10-grafana) | dashboards | middle |
| 11 | [elasticsearch](11-elasticsearch) | search engine | middle |
| 12 | [redis](12-redis) | cache | middle |
| 13 | [kafka](13-kafka) | notifications as a separate service | middle+ |
| 14 | [opentelemetry](14-opentelemetry) | tracing and logs | middle+ |
| 15 | [microservices](15-microservices) | every module a service, contract tests | senior |
| 16 | [spring-cloud-gateway](16-spring-cloud-gateway) | single entry point, shared session | senior |
| 17 | [spring-cloud-config](17-spring-cloud-config) | central configuration | senior |
| 18 | [eureka](18-eureka) | service discovery | senior |
| 19 | [secrets-manager](19-secrets-manager) | secrets out of configuration | senior |
| 20 | [resilience4j](20-resilience4j) | timeouts, retries, circuit breakers, rate limiting | senior |
| 21 | [saga](21-saga) | consistency across services | senior |
| 22 | [kubernetes](22-kubernetes) | the platform replaces Spring Cloud infrastructure | senior |
| 23 | [helm](23-helm) | packages and environments | senior |
| 24 | [argocd](24-argocd) | GitOps | senior |
| 25 | [autoscaling](25-autoscaling) | scaling under load, service level objectives | senior |
| 26 | [hexagonal](26-hexagonal) | storage port, database chosen by profile | architect |
| 27 | [mongodb](27-mongodb) | second storage adapter | architect |
| 28 | [webflux](28-webflux) | second web stack, choice by measurement | architect |

The same black-box [acceptance tests](acceptance-tests) run against every stage and prove that the behaviour of the API does not change.

Architecture decisions are recorded in [docs/adr](docs/adr). The previous attempt of the project is kept in [docs/archive](docs/archive) and under the tag `legacy-2026-11`.
