# Architecture

A collaborative notes application with role-based access control.
Spring Boot 4 · Gradle composite builds · microservices monorepo.

---

## Tech Stack

| Layer             | Technology                                      |
|-------------------|-------------------------------------------------|
| Language          | Java                                            |
| Framework         | Spring Boot 4.0.6                               |
| Build             | Gradle 9.5.1, composite builds                  |
| Authorization     | Spring Authorization Server (OAuth2 / OIDC)     |
| API gateway       | Spring Cloud Gateway                            |
| Service discovery | Spring Cloud Netflix Eureka                     |
| Configuration     | Spring Cloud Config Server                      |
| HTTP client       | Spring Cloud OpenFeign + Spring Circuit Breaker |
| Persistence       | Spring Data JPA + H2 (dev)                      |
| Schema migration  | Liquibase or Flyway                             |
| Caching           | Redis (Spring Data Redis)                       |
| Search            | Elasticsearch (Spring Data Elasticsearch)       |
| Messaging         | RabbitMQ (Spring AMQP) or Apache Kafka          |
| API docs          | springdoc-openapi                               |
| Observability     | Prometheus + Grafana · Zipkin · Elastic Stack   |

> Spring Cloud version must be compatible with Spring Boot 4.0.6.
> Add the Spring Cloud BOM to `build-logic/build.gradle` before using
> gateway, registry, config, or feign.

---

## Design Principles

| Principle                    | In practice                                               |
|------------------------------|-----------------------------------------------------------|
| DRY                          | every piece of knowledge lives in exactly one place       |
| KISS                         | simple solution over clever one                           |
| YAGNI                        | don't add what isn't needed right now                     |
| SLAP                         | one level of abstraction per method or module             |
| SOLID                        | single responsibility, depend on abstractions             |
| GRASP                        | assign responsibilities through established patterns      |
| Separation of Concerns       | HTTP, persistence, domain — separate modules              |
| Composition over Inheritance | implement interfaces, don't extend implementations        |
| Fail Fast                    | fail immediately, never silently swallow errors           |
| Single Source of Truth       | `domain/` is the only place for business model           |
| Zen of Python                | explicit > implicit, simple > complex, readability counts |

Key consequences:
- Business model lives only in `domain/` — nowhere else.
- Adapters depend only on `domain/` and know nothing about `application/`.
- Explicit duplication is better than an abstraction that only reduces lines without solving the problem.

---

## Project Structure

```
notes-spring/
├── build-logic/

├── crud/                  shared CRUD library (no application/)
│   ├── domain/
│   ├── webmvc/
│   └── data-jpa/

├── auth/                  Spring Authorization Server
│   ├── domain/
│   ├── application/
│   ├── webmvc/
│   └── data-jpa/

├── user/
│   ├── domain/
│   ├── application/
│   ├── webmvc/
│   └── data-jpa/

├── note/
│   ├── domain/
│   ├── application/
│   ├── webmvc/
│   └── data-jpa/

├── user-note/
│   ├── domain/
│   ├── application/
│   ├── webmvc/
│   ├── data-jpa/
│   └── feign/

├── gateway/
│   └── application/

├── registry/
│   └── application/

└── config/
    └── application/
```

**Business services** (`domain/` + `application/` + adapters):
`auth` · `user` · `note` · `user-note`

**Infrastructure services** (single `application/` each):
`gateway` · `registry` · `config`

**Shared library** (no `application/`):
`crud`

---

## Hexagonal Architecture (Ports & Adapters)

Every business service has the same structure: a pure Java core surrounded by swappable adapters.
`domain/` defines **what** the service does. Adapters define **how** it does it.

```
┌─────────────────────────────────────────────────────┐
│                    application/                     │
│         Spring Boot · use cases · wiring            │
│                                                     │
│   ┌──────────┐  ┌───────────┐  ┌────────────────┐  │
│   │ webmvc/  │  │ data-jpa/ │  │    feign/      │  │
│   │  HTTP in │  │persistence│  │ (user-note/)   │  │
│   └────┬─────┘  └─────┬─────┘  └───────┬────────┘  │
│        └──────────────┼────────────────┘            │
│                       ↓                             │
│              ┌─────────────────┐                    │
│              │    domain/      │                    │
│              │ entities        │                    │
│              │ port interfaces │                    │
│              │ (pure Java)     │                    │
│              └─────────────────┘                    │
└─────────────────────────────────────────────────────┘
```

**Rule:** adapters (`webmvc/`, `data-jpa/`, `feign/`) depend **only** on `domain/`.
Importing from `application/` pulls in `spring-boot-starter` and breaks isolation.

### Module dependency table

| Module         | Depends on                               |
|----------------|------------------------------------------|
| `domain/`      | nothing — pure Java                      |
| `webmvc/`      | `domain/`                                |
| `data-jpa/`    | `domain/`                                |
| `feign/`       | `domain/` *(user-note only)*             |
| `application/` | `domain/` + all adapters of this service |

### Adapter Alternatives

Because adapters implement port interfaces from `domain/`, any adapter can be replaced
without touching business logic. Each adapter type has its own convention plugin in `build-logic/`.

**Incoming HTTP** — how clients reach this service:

| Module      | Technology              | Style    | Convention plugin                    |
|-------------|-------------------------|----------|--------------------------------------|
| `webmvc/`   | Spring MVC              | sync     | `spring-webmvc-adapter-conventions`  |
| `webflux/`  | Spring WebFlux          | reactive | `spring-webflux-adapter-conventions` |
| `graphql/`  | Spring for GraphQL      | sync/rx  | `spring-graphql-adapter-conventions` |

**Persistence** — how the service stores data:

| Module       | Technology          | Style         | Convention plugin                       |
|--------------|---------------------|---------------|-----------------------------------------|
| `data-jpa/`  | Spring Data JPA     | sync, SQL     | `spring-data-jpa-adapter-conventions`   |
| `r2dbc/`     | Spring Data R2DBC   | reactive, SQL | `spring-data-r2dbc-adapter-conventions` |
| `mongo/`     | Spring Data MongoDB | sync/rx       | `spring-data-mongo-adapter-conventions` |

**Database driver** — configured inside the persistence adapter, not a separate module:

| Driver     | Usage                  | How to add                                       |
|------------|------------------------|--------------------------------------------------|
| H2         | development, in-memory | `spring-h2-database-conventions` mixin           |
| PostgreSQL | production, SQL        | add `org.postgresql:postgresql` driver to plugin  |
| MySQL      | production, SQL        | add `com.mysql:mysql-connector-j` to plugin       |

**Schema migration** — applied inside the persistence adapter alongside the driver:

| Tool      | How to add                                                        |
|-----------|-------------------------------------------------------------------|
| Liquibase | add `org.liquibase:liquibase-core` to `spring-data-jpa-adapter-conventions` |
| Flyway    | add `org.flywaydb:flyway-core` to `spring-data-jpa-adapter-conventions`     |

Place migration files in `src/main/resources/db/changelog/` (Liquibase) or `src/main/resources/db/migration/` (Flyway).

**Caching** — how the service stores frequently read data:

| Module    | Technology             | Convention plugin                       |
|-----------|------------------------|-----------------------------------------|
| `cache/`  | Spring Data Redis      | `spring-data-redis-adapter-conventions` |

**Search** — how the service queries full-text or complex search indexes:

| Module      | Technology                      | Convention plugin                             |
|-------------|---------------------------------|-----------------------------------------------|
| `search/`   | Spring Data Elasticsearch       | `spring-elasticsearch-adapter-conventions`    |

**Messaging** — how services communicate asynchronously:

| Module        | Technology             | Style       | Convention plugin                          |
|---------------|------------------------|-------------|--------------------------------------------|
| `rabbitmq/`   | Spring AMQP            | push, queue | `spring-rabbitmq-adapter-conventions`      |
| `kafka/`      | Spring Kafka           | stream      | `spring-kafka-adapter-conventions`         |

A messaging adapter can be both incoming (consumer) and outgoing (producer), implementing port interfaces from `domain/`.

**WebSocket** — how the service pushes events to clients in real time:

| Module        | Technology                     | Convention plugin                          |
|---------------|--------------------------------|--------------------------------------------|
| `websocket/`  | Spring WebSocket + STOMP       | `spring-websocket-adapter-conventions`     |

**gRPC** — high-performance RPC between services (alternative to REST for inter-service calls):

| Module    | Role             | Technology                          | Convention plugin                   |
|-----------|------------------|-------------------------------------|-------------------------------------|
| `grpc/`   | incoming + outgoing | gRPC-Java + `grpc-spring-boot-starter` | `spring-grpc-adapter-conventions` |

Both server (incoming) and client (outgoing) stubs live in the same `grpc/` module.
`.proto` files are placed in `src/main/proto/`. The Protobuf Gradle plugin generates Java stubs at build time.

**Outgoing HTTP** — how this service calls other services:

| Module         | Technology             | Style              | Convention plugin                        |
|----------------|------------------------|--------------------|------------------------------------------|
| `feign/`       | Spring Cloud OpenFeign | declarative, sync  | `spring-openfeign-adapter-conventions`   |
| `rest-client/` | Spring RestClient      | imperative, sync   | `spring-rest-client-adapter-conventions` |
| `web-client/`  | Spring WebClient       | reactive           | `spring-web-client-adapter-conventions`  |

**Validation** — Bean Validation (`jakarta.validation`) applies across layers:

| Where                 | What                                                              |
|-----------------------|-------------------------------------------------------------------|
| `webmvc/` (incoming)  | `@Valid` on controller method parameters — validates request body |
| `domain/` (entities)  | `@NotNull`, `@Size`, etc. on entity fields                        |
| Any adapter           | add `spring-boot-starter-validation` to the convention plugin     |

**How to swap an adapter:**
1. Create the new adapter module (e.g., `webflux/` replacing `webmvc/`)
2. Implement the same port interfaces from `domain/`
3. Add the corresponding convention plugin to `build-logic/`
4. In `application/build.gradle`: replace `project(':webmvc')` with `project(':webflux')`
5. Delete the old adapter module — `domain/` is untouched

---

## Domain Models

```
auth/domain/      AuthUser   { UUID id, String username, String passwordHash, String refreshToken }
user/domain/      User       { UUID id, String username, String email }
note/domain/      Note       { UUID id, String content }
user-note/domain/ UserNote   { UUID id, UUID userId, UUID noteId, UserRole role }
                  UserRole   CREATOR | OWNER | EDITOR | VIEWER
crud/domain/      CrudRepository<T, ID>
```

### Port Interfaces (defined in `domain/`, implemented in adapters)

```
auth/domain/
  AuthUserRepository  findByUsername(username) · save(user)
  TokenStore          save(token) · exists(token) · delete(token)

user/domain/
  UserRepository      findById(id) · save(user)

note/domain/
  NoteRepository      findById(id) · save(note) · delete(id)

user-note/domain/
  UserNoteRepository  findByUserIdAndNoteId(userId, noteId) · save(userNote)
  UserClient          findById(userId)    — implemented in feign/ (or rest-client/ or web-client/)
  NoteClient          findById(noteId)    — implemented in feign/ (or rest-client/ or web-client/)
```

---

## API Contracts

| Service      | Method | Path                      | Description            |
|--------------|--------|---------------------------|------------------------|
| `auth`       | POST   | /auth/register            | register new user      |
| `auth`       | POST   | /auth/login               | obtain tokens          |
| `auth`       | POST   | /auth/logout              | revoke refresh token   |
| `auth`       | POST   | /auth/refresh-token       | rotate refresh token   |
| `user`       | GET    | /users/{id}               | get user profile       |
| `user`       | PUT    | /users/{id}               | update user profile    |
| `note`       | GET    | /notes/{id}               | get note               |
| `note`       | POST   | /notes                    | create note            |
| `note`       | PUT    | /notes/{id}               | update note            |
| `note`       | DELETE | /notes/{id}               | delete note            |
| `user-note`  | GET    | /user-notes               | list access entries    |
| `user-note`  | POST   | /user-notes               | grant access           |
| `user-note`  | DELETE | /user-notes/{id}          | revoke access          |
| `user-note`  | PUT    | /user-notes/{id}/transfer | transfer ownership     |

---

## OpenAPI / Swagger

Each business service exposes its API spec at `/v3/api-docs`. `gateway/` aggregates all specs into a single Swagger UI.

### Setup per service (`webmvc/`)

Add `springdoc-openapi` to `spring-webmvc-adapter-conventions.gradle`:

```groovy
dependencies {
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui'
    // ... existing deps
}
```

### Endpoints

| Path              | Description                          |
|-------------------|--------------------------------------|
| `/v3/api-docs`    | OpenAPI JSON spec for this service   |
| `/swagger-ui`     | Swagger UI for this service          |

### Aggregation in `gateway/`

Configure `springdoc-openapi` in `gateway/application/` to collect specs from all services via Eureka:

```properties
springdoc.swagger-ui.urls[0].name=auth
springdoc.swagger-ui.urls[0].url=/auth/v3/api-docs
springdoc.swagger-ui.urls[1].name=user
springdoc.swagger-ui.urls[1].url=/user/v3/api-docs
springdoc.swagger-ui.urls[2].name=note
springdoc.swagger-ui.urls[2].url=/note/v3/api-docs
springdoc.swagger-ui.urls[3].name=user-note
springdoc.swagger-ui.urls[3].url=/user-note/v3/api-docs
```

Swagger UI aggregated: `http://localhost:8080/swagger-ui`

---

## Service Interaction

```
                  ┌──────────────────────────────────────────┐
Client ─────────▶ │                gateway/                  │
                  └────┬──────┬────────┬────────────────┬────┘
                       │      │        │                │
                       ▼      ▼        ▼                ▼
                      auth/  user/   note/          user-note/
                                                        │
                                               feign/ calls
                                                        │
                                                ┌───────┴───────┐
                                                ▼               ▼
                                              user/           note/

  All services ──▶ registry/   register on startup (Eureka)
  All services ──▶ config/     fetch config on startup
```

---

## Implementation Order

Build modules within each service in this order: `domain/` → `data-jpa/` → `webmvc/` → `application/`.
This follows the dependency graph — each module depends only on what was built before it.

```
1. build-logic/    convention plugins
2. crud/           domain/ · webmvc/ · data-jpa/
3. user/           domain/ · data-jpa/ · webmvc/ · application/
4. note/           domain/ · data-jpa/ · webmvc/ · application/
5. auth/           domain/ · data-jpa/ · webmvc/ · application/
6. user-note/      domain/ · data-jpa/ · feign/ · webmvc/ · application/
7. registry/       application/   (Eureka Server)
8. config/         application/   (Config Server)
9. gateway/        application/   (Spring Cloud Gateway)
```

---

## Build System

### `gradle.properties` (root)

```properties
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configureondemand=true
org.gradle.configuration-cache=true
org.gradle.configuration-cache.parallel=true
```

### Root `settings.gradle`

```groovy
rootProject.name = 'notes-spring'

includeBuild 'build-logic'
includeBuild 'auth'
includeBuild 'note'
includeBuild 'user'
includeBuild 'user-note'
includeBuild 'crud'
includeBuild 'gateway'
includeBuild 'registry'
includeBuild 'config'
```

### Root `build.gradle`

```groovy
def builds = gradle.includedBuilds

tasks.register('clean') { dependsOn builds.collect { it.task(':clean') } }
tasks.register('test')  { dependsOn builds.collect { it.task(':test')  } }
tasks.register('check') { dependsOn builds.collect { it.task(':check') } }
tasks.register('build') { dependsOn builds.collect { it.task(':build') } }
```

### Service `settings.gradle`

```groovy
pluginManagement {
    includeBuild '../build-logic'
}
rootProject.name = 'auth'   // change per service
include ':domain'
include ':application'
include ':webmvc'
include ':data-jpa'
// include ':feign'          // user-note only
```

### Service `build.gradle`

```groovy
tasks.register('clean') { dependsOn subprojects.collect { ":${it.name}:clean" } }
tasks.register('test')  { dependsOn subprojects.collect { ":${it.name}:test"  } }
tasks.register('check') { dependsOn subprojects.collect { ":${it.name}:check" } }
tasks.register('build') { dependsOn subprojects.collect { ":${it.name}:build" } }
```

### Module `build.gradle` per layer

```groovy
// domain/build.gradle
plugins { id 'spring-domain-conventions' }

// webmvc/build.gradle  (or webflux/, graphql/)
plugins { id 'spring-webmvc-adapter-conventions' }

// data-jpa/build.gradle  (or r2dbc/, mongo/)
plugins { id 'spring-data-jpa-adapter-conventions' }

// feign/build.gradle  (or rest-client/, web-client/)
plugins { id 'spring-openfeign-adapter-conventions' }

// application/build.gradle
plugins {
    id 'spring-boot-application-conventions'
    id 'spring-h2-database-conventions'   // dev only
}
dependencies {
    implementation project(':domain')
    implementation project(':webmvc')     // swap adapter here
    implementation project(':data-jpa')   // swap adapter here
    // implementation project(':feign')   // user-note only
}
```

### `build-logic/build.gradle`

```groovy
plugins {
    id 'groovy-gradle-plugin'
}
repositories {
    gradlePluginPortal()
    mavenCentral()
}
dependencies {
    implementation 'org.springframework.boot:spring-boot-gradle-plugin:4.0.6'
    implementation 'io.spring.gradle:dependency-management-plugin:1.1.7'
    // add Spring Cloud Gradle plugin when using gateway / registry / config / feign
}
```

---

## Convention Plugins

Located in `build-logic/src/main/groovy/`. Each plugin is a `.gradle` file.
All adapter plugins follow the same structure: `java-library` + `dependency-management` + BOM import + starter dependency.

### `spring-domain-conventions.gradle`

```groovy
plugins {
    id 'java-library'
}
repositories {
    mavenCentral()
}
```

### `spring-boot-application-conventions.gradle`

```groovy
plugins {
    id 'java-library'
    id 'org.springframework.boot'
    id 'io.spring.dependency-management'
}
repositories {
    mavenCentral()
}
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
tasks.named('test') {
    useJUnitPlatform()
}
```

### `spring-webmvc-adapter-conventions.gradle`

```groovy
plugins {
    id 'java-library'
    id 'io.spring.dependency-management'
}
dependencyManagement {
    imports {
        mavenBom org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES
    }
}
repositories {
    mavenCentral()
}
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-webmvc'
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui'
    testImplementation 'org.springframework.boot:spring-boot-starter-webmvc-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
tasks.named('test') {
    useJUnitPlatform()
}
```

### `spring-data-jpa-adapter-conventions.gradle`

```groovy
plugins {
    id 'java-library'
    id 'io.spring.dependency-management'
}
dependencyManagement {
    imports {
        mavenBom org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES
    }
}
repositories {
    mavenCentral()
}
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    testImplementation 'org.springframework.boot:spring-boot-starter-data-jpa-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
tasks.named('test') {
    useJUnitPlatform()
}
```

### `spring-h2-database-conventions.gradle` (mixin)

Applied alongside another plugin, not standalone.

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-h2console'
    runtimeOnly 'com.h2database:h2'
}
```

### Additional adapter plugins (follow the same pattern)

| Plugin                                   | Key dependency                                                   |
|------------------------------------------|------------------------------------------------------------------|
| `spring-openfeign-adapter-conventions`   | `spring-cloud-starter-openfeign` + `circuitbreaker-resilience4j` |
| `spring-rest-client-adapter-conventions` | `spring-boot-starter-web` (RestClient is included)               |
| `spring-web-client-adapter-conventions`  | `spring-boot-starter-webflux`                                    |
| `spring-webflux-adapter-conventions`     | `spring-boot-starter-webflux`                                    |
| `spring-graphql-adapter-conventions`     | `spring-boot-starter-graphql`                                    |
| `spring-data-r2dbc-adapter-conventions`  | `spring-boot-starter-data-r2dbc`                                 |
| `spring-data-mongo-adapter-conventions`  | `spring-boot-starter-data-mongodb`                               |
| `spring-data-redis-adapter-conventions`  | `spring-boot-starter-data-redis`                                 |
| `spring-elasticsearch-adapter-conventions` | `spring-boot-starter-data-elasticsearch`                       |
| `spring-rabbitmq-adapter-conventions`    | `spring-boot-starter-amqp`                                       |
| `spring-kafka-adapter-conventions`       | `spring-kafka`                                                   |
| `spring-websocket-adapter-conventions`   | `spring-boot-starter-websocket`                                  |
| `spring-grpc-adapter-conventions`        | `grpc-spring-boot-starter` + `protobuf-gradle-plugin`            |

---

## Key Configuration

### Business service `application.properties`

```properties
spring.application.name=auth         # change per service
server.port=8081                     # see port table below
spring.datasource.url=jdbc:h2:mem:authdb
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
spring.config.import=configserver:http://localhost:8888
```

### Suggested ports

| Service   | Port |
|-----------|------|
| gateway   | 8080 |
| auth      | 8081 |
| user      | 8082 |
| note      | 8083 |
| user-note | 8084 |
| registry  | 8761 |
| config    | 8888 |

### `registry/application.properties`

```properties
spring.application.name=registry
server.port=8761
eureka.instance.hostname=localhost
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

### `gateway/application.properties`

```properties
spring.application.name=gateway
server.port=8080
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
spring.cloud.gateway.discovery.locator.enabled=true
```

---

## Observability

| Tool                 | Purpose             |
|----------------------|---------------------|
| Prometheus + Grafana | metrics             |
| Zipkin               | distributed tracing |
| Elastic Stack (ELK)  | centralized logs    |

---

## Roadmap

| Item           | Why                                                                      |
|----------------|--------------------------------------------------------------------------|
| **Kubernetes** | container orchestration; Eureka disabled — K8s handles service discovery |
| **AWS**        | target platform for production deployment                                |
