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
| Persistence       | Spring Data JPA                                 |
| API docs          | springdoc-openapi (planned)                     |
| Observability     | Prometheus + Grafana · Zipkin · Elastic Stack   |

> Spring Cloud version must be compatible with Spring Boot 4.0.6.
> Add the Spring Cloud BOM to `build-logic/build.gradle` before using
> gateway, registry, config, or feign modules.

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
// current
rootProject.name = 'notes-spring'

includeBuild 'build-logic'
includeBuild 'application'   // placeholder — will become gateway/
includeBuild 'auth'
includeBuild 'note'
includeBuild 'user'
includeBuild 'user-note'
includeBuild 'crud'
```

```groovy
// target — after infrastructure services are added
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

### Service `settings.gradle` pattern

```groovy
pluginManagement {
    includeBuild '../build-logic'
}
rootProject.name = 'auth'     // change per service
include ':domain'             // add when domain/ module is created
include ':application'
include ':webmvc'
include ':data-jpa'
```

### Service `build.gradle` pattern

```groovy
tasks.register('clean') { dependsOn subprojects.collect { ":${it.name}:clean" } }
tasks.register('test')  { dependsOn subprojects.collect { ":${it.name}:test"  } }
tasks.register('check') { dependsOn subprojects.collect { ":${it.name}:check" } }
tasks.register('build') { dependsOn subprojects.collect { ":${it.name}:build" } }
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
    // add Spring Cloud Gradle plugin here when needed
}
```

### Convention Plugins

Located in `build-logic/src/main/groovy/`.

#### `spring-boot-application-conventions.gradle` ✓

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

#### `spring-webmvc-adapter-conventions.gradle` ✓

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
    testImplementation 'org.springframework.boot:spring-boot-starter-webmvc-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
tasks.named('test') {
    useJUnitPlatform()
}
```

#### `spring-data-jpa-adapter-conventions.gradle` ✓

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

#### `spring-h2-database-conventions.gradle` ✓ (mixin — no standalone module)

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-h2console'
    runtimeOnly 'com.h2database:h2'
}
```

#### `spring-domain-conventions.gradle` ○ (planned)

```groovy
plugins {
    id 'java-library'
}
repositories {
    mavenCentral()
}
```

#### `spring-openfeign-adapter-conventions.gradle` ○ (planned)

```groovy
plugins {
    id 'java-library'
    id 'io.spring.dependency-management'
}
dependencyManagement {
    imports {
        mavenBom org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES
        // add Spring Cloud BOM here
    }
}
repositories {
    mavenCentral()
}
dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-openfeign'
    implementation 'org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
tasks.named('test') {
    useJUnitPlatform()
}
```

---

## Implementation Order

```
1. build-logic/        — convention plugins (done)
2. crud/               — domain/ · webmvc/ · data-jpa/
3. user/               — domain/ · data-jpa/ · webmvc/ · application/
4. note/               — domain/ · data-jpa/ · webmvc/ · application/
5. auth/               — domain/ · data-jpa/ · webmvc/ · application/
6. user-note/          — domain/ · data-jpa/ · feign/ · webmvc/ · application/
7. registry/           — application/ (Eureka Server)
8. config/             — application/ (Config Server)
9. gateway/            — application/ (replace placeholder application/)
```

Within each service, build modules in this order: `domain/` → `data-jpa/` → `webmvc/` → `application/`.
This follows the dependency graph — each module depends only on what was built before it.

---

## Project Structure

### Current (branch `auth`)

```
notes-spring/
├── build-logic/           ✓ 4 convention plugins
├── application/           ✓ placeholder, will become gateway/
├── auth/                  ✓ application/ · webmvc/ · data-jpa/
├── note/                  ✓ application/ · webmvc/ · data-jpa/
├── user/                  ✓ application/ · webmvc/ · data-jpa/
├── user-note/             ✓ application/ · webmvc/ · data-jpa/
└── crud/                  ✓ webmvc/ · data-jpa/
```

Not yet created: `domain/` modules · `feign/` in user-note · `gateway/` · `registry/` · `config/`

### Target

```
notes-spring/
├── build-logic/

├── crud/
│   ├── domain/
│   ├── webmvc/
│   └── data-jpa/

├── auth/
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

### Module `build.gradle` per layer

```groovy
// domain/build.gradle
plugins { id 'spring-domain-conventions' }
```

```groovy
// webmvc/build.gradle
plugins { id 'spring-webmvc-adapter-conventions' }
```

```groovy
// data-jpa/build.gradle
plugins { id 'spring-data-jpa-adapter-conventions' }
```

```groovy
// feign/build.gradle  (user-note only)
plugins { id 'spring-openfeign-adapter-conventions' }
```

```groovy
// application/build.gradle
plugins {
    id 'spring-boot-application-conventions'
    id 'spring-h2-database-conventions'  // dev only; remove for prod or move to profile
}
dependencies {
    implementation project(':domain')
    implementation project(':webmvc')
    implementation project(':data-jpa')
    // implementation project(':feign')  // user-note only
}
```

### Module dependency table

| Module         | Depends on                               |
|----------------|------------------------------------------|
| `domain/`      | nothing — pure Java                      |
| `webmvc/`      | `domain/`                                |
| `data-jpa/`    | `domain/`                                |
| `feign/`       | `domain/` *(user-note only)*             |
| `application/` | `domain/` + all adapters of this service |

---

## Domain Models

```
auth/domain/     AuthUser   { UUID id, String username, String passwordHash, String refreshToken }
user/domain/     User       { UUID id, String username, String email }
note/domain/     Note       { UUID id, String content }
user-note/domain/ UserNote  { UUID id, UUID userId, UUID noteId, UserRole role }
                 UserRole   CREATOR | OWNER | EDITOR | VIEWER
crud/domain/     CrudRepository<T, ID>
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
  UserClient          findById(userId)          → implemented in feign/
  NoteClient          findById(noteId)          → implemented in feign/
```

---

## API Contracts

| Service      | Method | Path                          | Description               |
|--------------|--------|-------------------------------|---------------------------|
| `auth`       | POST   | /auth/register                | register new user         |
| `auth`       | POST   | /auth/login                   | obtain tokens             |
| `auth`       | POST   | /auth/logout                  | revoke refresh token      |
| `auth`       | POST   | /auth/refresh-token           | rotate refresh token      |
| `user`       | GET    | /users/{id}                   | get user profile          |
| `user`       | PUT    | /users/{id}                   | update user profile       |
| `note`       | GET    | /notes/{id}                   | get note                  |
| `note`       | POST   | /notes                        | create note               |
| `note`       | PUT    | /notes/{id}                   | update note               |
| `note`       | DELETE | /notes/{id}                   | delete note               |
| `user-note`  | GET    | /user-notes                   | list access entries       |
| `user-note`  | POST   | /user-notes                   | grant access              |
| `user-note`  | DELETE | /user-notes/{id}              | revoke access             |
| `user-note`  | PUT    | /user-notes/{id}/transfer     | transfer ownership        |

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

## Key Configuration

### Business service (`application.properties`)

```properties
spring.application.name=auth      # change per service
server.port=8081                  # unique per service
spring.datasource.url=jdbc:h2:mem:authdb
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
spring.config.import=configserver:http://localhost:8888
```

### Suggested ports

| Service    | Port |
|------------|------|
| gateway    | 8080 |
| auth       | 8081 |
| user       | 8082 |
| note       | 8083 |
| user-note  | 8084 |
| registry   | 8761 |
| config     | 8888 |

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

| Item           | Why                                                                       |
|----------------|---------------------------------------------------------------------------|
| **Kubernetes** | container orchestration; Eureka disabled — K8s handles service discovery  |
| **AWS**        | target platform for production deployment                                 |
