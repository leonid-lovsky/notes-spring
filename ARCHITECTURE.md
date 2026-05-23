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
| Service discovery | Spring Cloud Netflix Eureka                     |
| API gateway       | Spring Cloud Gateway                            |
| Configuration     | Spring Cloud Config Server                      |
| Authorization     | Spring Authorization Server (OAuth2 / OIDC)     |
| HTTP client       | Spring Cloud OpenFeign + Spring Circuit Breaker |
| Persistence       | Spring Data JPA                                 |
| API docs          | springdoc-openapi (planned)                     |
| Observability     | Prometheus + Grafana · Zipkin · Elastic Stack   |

> Spring Cloud version: must be compatible with Spring Boot 4.0.6. Add the Spring Cloud BOM
> to `build-logic/build.gradle` before using gateway, registry, config, or feign.

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

## Build System

The root project is a Gradle composite build. Each service is an independent included build.

**Root `settings.gradle` (current):**
```groovy
rootProject.name = 'notes-spring'

includeBuild 'build-logic'
includeBuild 'application'   // placeholder — will be replaced by gateway/
includeBuild 'auth'
includeBuild 'note'
includeBuild 'user'
includeBuild 'user-note'
includeBuild 'crud'
```

**Root `settings.gradle` (target — after infrastructure services are added):**
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

**Root `build.gradle`** — delegates lifecycle tasks to all included builds:
```groovy
def builds = gradle.includedBuilds

tasks.register('clean') { dependsOn builds.collect { it.task(':clean') } }
tasks.register('test')  { dependsOn builds.collect { it.task(':test')  } }
tasks.register('check') { dependsOn builds.collect { it.task(':check') } }
tasks.register('build') { dependsOn builds.collect { it.task(':build') } }
```

**Each service `settings.gradle` (current — no `domain/` yet):**
```groovy
pluginManagement {
    includeBuild '../build-logic'
}
rootProject.name = 'auth'
include ':application'
include ':webmvc'
include ':data-jpa'
```

**Each service `settings.gradle` (target — after `domain/` module is added):**
```groovy
pluginManagement {
    includeBuild '../build-logic'
}
rootProject.name = 'auth'
include ':domain'
include ':application'
include ':webmvc'
include ':data-jpa'
```

**Each service `build.gradle`** — delegates to subprojects:
```groovy
tasks.register('clean') { dependsOn subprojects.collect { ":${it.name}:clean" } }
tasks.register('test')  { dependsOn subprojects.collect { ":${it.name}:test"  } }
tasks.register('check') { dependsOn subprojects.collect { ":${it.name}:check" } }
tasks.register('build') { dependsOn subprojects.collect { ":${it.name}:build" } }
```

**`build-logic/build.gradle`:**
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
    // add Spring Cloud plugin dependency when adding gateway / registry / config / feign
}
```

### Convention Plugins

Located in `build-logic/src/main/groovy/`.

| Plugin                                | Used in              | Provides                                  | Status   |
|---------------------------------------|----------------------|-------------------------------------------|----------|
| `spring-domain-conventions`           | `domain/`            | `java-library` only, no frameworks        | planned  |
| `spring-boot-application-conventions` | `application/`       | Spring Boot app, test setup               | exists   |
| `spring-webmvc-adapter-conventions`   | `webmvc/`            | Spring MVC, BOM import, test setup        | exists   |
| `spring-data-jpa-adapter-conventions` | `data-jpa/`          | Spring Data JPA, BOM import, test setup   | exists   |
| `spring-h2-database-conventions`      | `application/` (dev) | H2 console + driver (mixin, dev only)     | exists   |
| `spring-openfeign-adapter-conventions`| `feign/`             | OpenFeign + Circuit Breaker               | planned  |

All existing convention plugins include `repositories { mavenCentral() }` — services have no repository config.

---

## Project Structure

### Target

```
notes-spring/
├── build-logic/           convention plugins (not a service)

├── crud/                  shared CRUD library — not a standalone service
│   ├── domain/            generic CrudRepository<T, ID>
│   ├── webmvc/            generic CRUD controllers
│   └── data-jpa/          generic JPA implementation

├── auth/                  authentication & authorization
│   ├── domain/            AuthUser { id, username, passwordHash, refreshToken }
│   ├── application/       Spring Boot + Spring Authorization Server
│   ├── webmvc/            POST /register  /login  /logout  /refresh-token
│   └── data-jpa/

├── user/                  user profiles
│   ├── domain/            User { id, username, email }
│   ├── application/
│   ├── webmvc/            GET /users/{id}
│   └── data-jpa/

├── note/                  notes content
│   ├── domain/            Note { id, content }
│   ├── application/
│   ├── webmvc/            GET/POST/PUT/DELETE /notes/{id}
│   └── data-jpa/

├── user-note/             access control and ownership
│   ├── domain/            UserNote { id, userId, noteId, role }
│   │                      Role: CREATOR | OWNER | EDITOR | VIEWER
│   ├── application/       use cases: access check, ownership transfer
│   ├── webmvc/            /user-notes
│   ├── data-jpa/
│   └── feign/             UserClient, NoteClient → user/, note/

├── gateway/               single entry point
│   └── application/       Spring Cloud Gateway — routing + auth filter

├── registry/              service registry
│   └── application/       Eureka Server

└── config/                centralized configuration
    └── application/       Spring Cloud Config Server
```

### Current (branch: `auth`)

```
notes-spring/
├── build-logic/           ✓ exists
├── application/           ✓ exists — placeholder, will become gateway/
├── auth/                  ✓ exists — application/ · webmvc/ · data-jpa/ (no domain/ yet)
├── note/                  ✓ exists — application/ · webmvc/ · data-jpa/ (no domain/ yet)
├── user/                  ✓ exists — application/ · webmvc/ · data-jpa/ (no domain/ yet)
├── user-note/             ✓ exists — application/ · webmvc/ · data-jpa/ (no domain/, no feign/)
└── crud/                  ✓ exists — webmvc/ · data-jpa/ (no domain/ yet)
```

Not yet created: `domain/` modules · `feign/` · `gateway/` · `registry/` · `config/`

---

## Hexagonal Architecture (Ports & Adapters)

Every business service has the same internal structure.

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

**Module dependencies:**

| Module         | Depends on                               |
|----------------|------------------------------------------|
| `domain/`      | nothing — pure Java                      |
| `webmvc/`      | `domain/`                                |
| `data-jpa/`    | `domain/`                                |
| `feign/`       | `domain/` *(user-note only)*             |
| `application/` | `domain/` + all adapters of this service |

**Swapping an adapter:** change one dependency in `application/build.gradle` — `domain/` stays untouched.

**Adding inter-service communication:** define a port interface in `domain/`, implement it in a new adapter module.

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

## Observability

| Tool                 | Purpose             |
|----------------------|---------------------|
| Prometheus + Grafana | metrics             |
| Zipkin               | distributed tracing |
| Elastic Stack (ELK)  | centralized logs    |

---

## Roadmap

| Item           | Why                                                                       |
|----------------|---------------------------------------------------------------------------|
| **Kubernetes** | container orchestration; Eureka disabled — K8s handles service discovery  |
| **AWS**        | target platform for production deployment                                 |
