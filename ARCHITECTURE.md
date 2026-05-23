# Architecture

Collaborative notes application with role-based access control.
Built on Spring Boot 4, Gradle composite builds, microservices monorepo.

---

## How to read this document

This is a **design specification** — not a status report. The implementation may be at any stage and may differ from what is described here for valid reasons. When the code and this document disagree, decide which is correct and update both to match.

---

## Tech Stack

| Concern           | Technology                                     |
|-------------------|------------------------------------------------|
| Language          | Java                                           |
| Framework         | Spring Boot 4.0.6                              |
| Build             | Gradle 9.5.1 — composite builds               |
| Security          | Spring Authorization Server (OAuth2 / OIDC)   |
| API gateway       | Spring Cloud Gateway MVC                       |
| Service discovery | Spring Cloud Netflix Eureka                    |
| Configuration     | Spring Cloud Config Server                     |
| HTTP client       | Spring Cloud OpenFeign + Resilience4j          |
| Persistence       | Spring Data JPA                                |
| Schema migration  | Liquibase or Flyway                            |
| Caching           | Spring Data Redis                              |
| Search            | Spring Data Elasticsearch                      |
| Messaging         | Spring AMQP (RabbitMQ) or Spring Kafka         |
| Real-time         | Spring WebSocket + STOMP                       |
| RPC               | gRPC + Protobuf                                |
| API docs          | springdoc-openapi 3.0.2                        |
| Metrics           | Micrometer + Prometheus + Grafana              |
| Tracing           | OpenTelemetry + Jaeger / Grafana Tempo         |
| Logs              | Elastic Stack (ELK)                            |
| Secrets           | HashiCorp Vault — Spring Cloud Vault           |
| Testing           | Testcontainers                                 |
| Packaging         | Docker — Spring Boot Buildpacks                |
| Orchestration     | Kubernetes + Helm                              |

---

## Design Principles

| Principle                    | In practice                                               |
|------------------------------|-----------------------------------------------------------|
| DRY                          | every piece of knowledge lives in exactly one place       |
| KISS                         | simplest solution that works                              |
| YAGNI                        | add nothing that is not needed right now                  |
| SLAP                         | one level of abstraction per method or module             |
| SOLID                        | single responsibility; depend on abstractions             |
| GRASP                        | assign responsibilities through established patterns      |
| Separation of Concerns       | HTTP, persistence, domain — separate modules              |
| Composition over Inheritance | implement interfaces; do not extend implementations       |
| Fail Fast                    | fail immediately; never silently swallow errors           |
| Single Source of Truth       | `domain/` is the only home for the business model        |
| Zen of Python                | explicit > implicit; simple > complex; readability counts |

Key rules:

- Business model lives in `domain/` only — never in adapters or `application/`.
- Adapters depend on `domain/` only — never on `application/`.
- Full elimination or explicit duplication — no partial abstractions.

---

## Project Structure

```
notes-spring/
├── build-logic/           convention plugins
├── crud/                  shared CRUD library — no application/
│   ├── domain/
│   ├── webmvc/
│   └── data-jpa/
├── auth/                  Spring Authorization Server
│   ├── domain/
│   ├── application/
│   ├── webmvc/
│   └── data-jpa/
├── user/                  user profiles
│   ├── domain/
│   ├── application/
│   ├── webmvc/
│   └── data-jpa/
├── note/                  notes
│   ├── domain/
│   ├── application/
│   ├── webmvc/
│   └── data-jpa/
├── user-note/             access control — calls user/ and note/ via feign/
│   ├── domain/
│   ├── application/
│   ├── webmvc/
│   ├── data-jpa/
│   └── feign/
├── gateway/               single entry point
│   └── application/
├── registry/              Eureka Server
│   └── application/
└── config/                Spring Cloud Config Server
    └── application/
```

**Business services** (`domain/` + `application/` + adapters): `auth` · `user` · `note` · `user-note`

**Infrastructure services** (single `application/` each): `gateway` · `registry` · `config`

**Shared library** (adapters only, no `application/`): `crud`

---

## Hexagonal Architecture (Ports & Adapters)

Every business service has the same internal structure: a pure-Java core (`domain/`) surrounded by swappable adapters.

```
┌──────────────────────────────────────────────────────┐
│                    application/                      │
│          Spring Boot · use cases · wiring            │
│                                                      │
│   ┌──────────┐   ┌───────────┐   ┌────────────────┐  │
│   │ webmvc/  │   │ data-jpa/ │   │    feign/      │  │
│   │  HTTP in │   │persistence│   │ (user-note/)   │  │
│   └────┬─────┘   └─────┬─────┘   └───────┬────────┘  │
│        └───────────────┼─────────────────┘           │
│                        ↓                             │
│               ┌─────────────────┐                    │
│               │    domain/      │                    │
│               │ entities        │                    │
│               │ port interfaces │                    │
│               │ (pure Java)     │                    │
│               └─────────────────┘                    │
└──────────────────────────────────────────────────────┘
```

**Rule:** `domain/` defines what the service does; adapters define how. Adapters depend on `domain/` only — importing `application/` pulls `spring-boot-starter` into a library module and breaks isolation.

### Module dependencies

| Module         | Depends on                                |
|----------------|-------------------------------------------|
| `domain/`      | nothing — pure Java, no frameworks        |
| `webmvc/`      | `domain/`                                 |
| `data-jpa/`    | `domain/`                                 |
| `feign/`       | `domain/` (user-note only)                |
| `application/` | `domain/` + all adapters of this service  |

### Adapter catalogue

Each adapter has a dedicated convention plugin in `build-logic/`. To swap an adapter: create the new module, implement the same port interfaces from `domain/`, and replace the dependency in `application/build.gradle` — `domain/` is untouched.

| Type          | Module         | Technology                    | Convention plugin                          |
|---------------|----------------|-------------------------------|--------------------------------------------|
| HTTP incoming | `webmvc/`      | Spring MVC (sync)             | `spring-webmvc-adapter-conventions`        |
| HTTP incoming | `webflux/`     | Spring WebFlux (reactive)     | `spring-webflux-adapter-conventions`       |
| HTTP incoming | `graphql/`     | Spring for GraphQL            | `spring-graphql-adapter-conventions`       |
| Persistence   | `data-jpa/`    | Spring Data JPA               | `spring-data-jpa-adapter-conventions`      |
| Persistence   | `r2dbc/`       | Spring Data R2DBC (reactive)  | `spring-data-r2dbc-adapter-conventions`    |
| Persistence   | `mongo/`       | Spring Data MongoDB           | `spring-data-mongo-adapter-conventions`    |
| HTTP outgoing | `feign/`       | Spring Cloud OpenFeign        | `spring-openfeign-adapter-conventions`     |
| HTTP outgoing | `rest-client/` | Spring RestClient             | `spring-rest-client-adapter-conventions`   |
| HTTP outgoing | `web-client/`  | Spring WebClient (reactive)   | `spring-web-client-adapter-conventions`    |
| Caching       | `cache/`       | Spring Data Redis             | `spring-data-redis-adapter-conventions`    |
| Search        | `search/`      | Spring Data Elasticsearch     | `spring-elasticsearch-adapter-conventions` |
| Messaging     | `rabbitmq/`    | Spring AMQP (RabbitMQ)        | `spring-rabbitmq-adapter-conventions`      |
| Messaging     | `kafka/`       | Spring Kafka                  | `spring-kafka-adapter-conventions`         |
| Real-time     | `websocket/`   | Spring WebSocket + STOMP      | `spring-websocket-adapter-conventions`     |
| RPC           | `grpc/`        | gRPC-Java + Protobuf          | `spring-grpc-adapter-conventions`          |

**Database driver** — add to the persistence adapter plugin, not a separate module:

| Driver     | Artifact                              | Usage                  |
|------------|---------------------------------------|------------------------|
| H2         | `spring-h2-database-conventions` mixin | development, in-memory |
| PostgreSQL | `org.postgresql:postgresql`           | production             |
| MySQL      | `com.mysql:mysql-connector-j`         | production             |

**Schema migration** — add to the persistence adapter plugin:

| Tool      | Artifact                                               |
|-----------|--------------------------------------------------------|
| Liquibase | `org.springframework.boot:spring-boot-starter-liquibase` |
| Flyway    | `org.springframework.boot:spring-boot-starter-flyway`    |

Migration files go in `src/main/resources/db/changelog/` (Liquibase) or `src/main/resources/db/migration/` (Flyway).

**Validation** — not a module; applies across layers via `spring-boot-starter-validation`:

- `webmvc/` — `@Valid` on controller parameters validates the request body
- `domain/` — `@NotNull`, `@Size`, etc. on entity fields

**gRPC** — `.proto` files go in `src/main/proto/`; the Protobuf Gradle plugin generates Java stubs at build time.

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

### Port interfaces

Defined in `domain/`, implemented in adapters:

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
  UserClient          findById(userId)  — implemented in feign/ or rest-client/ or web-client/
  NoteClient          findById(noteId)  — implemented in feign/ or rest-client/ or web-client/
```

---

## API Contracts

| Service     | Method | Path                      | Description          |
|-------------|--------|---------------------------|----------------------|
| `auth`      | POST   | /auth/register            | register new user    |
| `auth`      | POST   | /auth/login               | obtain tokens        |
| `auth`      | POST   | /auth/logout              | revoke refresh token |
| `auth`      | POST   | /auth/refresh-token       | rotate refresh token |
| `user`      | GET    | /users/{id}               | get user profile     |
| `user`      | PUT    | /users/{id}               | update profile       |
| `note`      | GET    | /notes/{id}               | get note             |
| `note`      | POST   | /notes                    | create note          |
| `note`      | PUT    | /notes/{id}               | update note          |
| `note`      | DELETE | /notes/{id}               | delete note          |
| `user-note` | GET    | /user-notes               | list access entries  |
| `user-note` | POST   | /user-notes               | grant access         |
| `user-note` | DELETE | /user-notes/{id}          | revoke access        |
| `user-note` | PUT    | /user-notes/{id}/transfer | transfer ownership   |

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
  All services ──▶ config/     fetch configuration on startup
```

---

## Implementation Order

Build in dependency order within each service: `domain/` → `data-jpa/` → `webmvc/` → `application/`.

```
1. build-logic/    convention plugins
2. crud/           domain/ · webmvc/ · data-jpa/
3. user/           domain/ · data-jpa/ · webmvc/ · application/
4. note/           domain/ · data-jpa/ · webmvc/ · application/
5. auth/           domain/ · data-jpa/ · webmvc/ · application/
6. user-note/      domain/ · data-jpa/ · feign/ · webmvc/ · application/
7. registry/       application/
8. config/         application/
9. gateway/        application/
```

---

## Build System

### Root `gradle.properties`

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

### `settings.gradle` — per service

```groovy
pluginManagement {
    includeBuild '../build-logic'
}

rootProject.name = 'auth'   // change per service

include ':domain'
include ':application'
include ':webmvc'
include ':data-jpa'
// include ':feign'   // user-note only
```

### `build.gradle` — per service

```groovy
tasks.register('clean') { dependsOn subprojects.collect { ":${it.name}:clean" } }
tasks.register('test')  { dependsOn subprojects.collect { ":${it.name}:test"  } }
tasks.register('check') { dependsOn subprojects.collect { ":${it.name}:check" } }
tasks.register('build') { dependsOn subprojects.collect { ":${it.name}:build" } }
```

### `build.gradle` — per module

```groovy
// domain/build.gradle
plugins { id 'spring-domain-conventions' }

// webmvc/build.gradle  (alternatives: webflux/, graphql/)
plugins { id 'spring-webmvc-adapter-conventions' }

// data-jpa/build.gradle  (alternatives: r2dbc/, mongo/)
plugins { id 'spring-data-jpa-adapter-conventions' }

// feign/build.gradle  (alternatives: rest-client/, web-client/)
plugins { id 'spring-openfeign-adapter-conventions' }

// application/build.gradle
plugins {
    id 'spring-boot-application-conventions'
    id 'spring-h2-database-conventions'   // dev only; replace with PostgreSQL in production
}
dependencies {
    implementation project(':domain')
    implementation project(':webmvc')    // swap adapter here
    implementation project(':data-jpa')  // swap adapter here
    // implementation project(':feign')  // user-note only
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
}
```

---

## Convention Plugins

All plugins are `.gradle` files in `build-logic/src/main/groovy/`.

Adapter plugins follow the same pattern: `java-library` + `io.spring.dependency-management` + BOM import + starter dependency.

> **Source of truth for all Spring Boot starters and recommended configuration:**
> `curl https://start.spring.io`
> Maven Central search is NOT authoritative — Spring Boot 4 starters may not appear there.
> Every main starter has a matching `-test` variant (e.g. `spring-boot-starter-data-jpa-test`).

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

Applied to every `application/` module. Provides Spring Boot, Actuator, Eureka client, Prometheus, and OpenTelemetry out of the box. Imports the Spring Cloud BOM so Spring Cloud starters need no explicit version.

```groovy
plugins {
    id 'java-library'
    id 'org.springframework.boot'
    id 'io.spring.dependency-management'
}

dependencyManagement {
    imports {
        mavenBom 'org.springframework.cloud:spring-cloud-dependencies:2025.1.1'
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
    implementation 'org.springframework.boot:spring-boot-starter-opentelemetry'
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
    runtimeOnly 'io.micrometer:micrometer-registry-prometheus'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-actuator-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-opentelemetry-test'
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
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.2'
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

### `spring-h2-database-conventions.gradle`

Mixin — applied alongside another plugin, not standalone.

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-h2console'
    runtimeOnly 'com.h2database:h2'
}
```

### Additional adapter plugins

Follow the same pattern as `spring-webmvc-adapter-conventions.gradle`. Spring Cloud starters also need the Spring Cloud BOM import.

| Plugin                                   | Key dependency                                                   |
|------------------------------------------|------------------------------------------------------------------|
| `spring-rest-client-adapter-conventions` | `spring-boot-starter-web`                                        |
| `spring-web-client-adapter-conventions`  | `spring-boot-starter-webflux`                                    |
| `spring-webflux-adapter-conventions`     | `spring-boot-starter-webflux`                                    |
| `spring-graphql-adapter-conventions`     | `spring-boot-starter-graphql`                                    |
| `spring-data-r2dbc-adapter-conventions`  | `spring-boot-starter-data-r2dbc`                                 |
| `spring-data-mongo-adapter-conventions`  | `spring-boot-starter-data-mongodb`                               |
| `spring-data-redis-adapter-conventions`  | `spring-boot-starter-data-redis`                                 |
| `spring-elasticsearch-adapter-conventions` | `spring-boot-starter-data-elasticsearch`                       |
| `spring-rabbitmq-adapter-conventions`    | `spring-boot-starter-amqp`                                       |
| `spring-kafka-adapter-conventions`       | `spring-boot-starter-kafka`                                      |
| `spring-websocket-adapter-conventions`   | `spring-boot-starter-websocket`                                  |
| `spring-grpc-adapter-conventions`        | `grpc-spring-boot-starter` + `protobuf-gradle-plugin`            |

---

## Security (OAuth2 / OIDC)

`auth/` is the Authorization Server. `gateway/` is the OAuth2 Client and Resource Server. Backend services (`user/`, `note/`, `user-note/`) are Resource Servers.

### Token flow

```
Browser / mobile app
        │  1. Authorization Code request
        ▼
    gateway/  ──────────────────▶  auth/
    OAuth2 Client                  Authorization Server — issues JWT
        │
        │  2. forward request + JWT (TokenRelay filter)
        ▼
    user/ · note/ · user-note/
    OAuth2 Resource Servers — validate JWT via JWKS
```

### Authorization Server — `auth/`

`auth/application/build.gradle`:

```groovy
plugins {
    id 'spring-boot-application-conventions'
    id 'spring-h2-database-conventions'
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-security-oauth2-authorization-server'
    implementation 'org.springframework.cloud:spring-cloud-starter-config'
    testImplementation 'org.springframework.boot:spring-boot-starter-security-oauth2-authorization-server-test'
    implementation project(':domain')
    implementation project(':webmvc')
    implementation project(':data-jpa')
}
```

`auth/application/src/main/java/.../AuthorizationServerConfig.java`:

```java
@Configuration
public class AuthorizationServerConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
            .oidc(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("gateway")
            .clientSecret("{noop}secret")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .redirectUri("http://localhost:8080/login/oauth2/code/gateway")
            .scope(OidcScopes.OPENID)
            .scope("read")
            .build();
        return new InMemoryRegisteredClientRepository(client);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = Jwks.generateRsa();
        return new ImmutableJWKSet<>(new JWKSet(rsaKey));
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
            .issuer("http://localhost:8081")
            .build();
    }
}
```

Standard endpoints:

| Endpoint                            | Description                    |
|-------------------------------------|--------------------------------|
| `/oauth2/authorize`                 | authorization code flow        |
| `/oauth2/token`                     | token issuance                 |
| `/oauth2/revoke`                    | token revocation               |
| `/oauth2/introspect`                | token introspection            |
| `/.well-known/openid-configuration` | OIDC discovery document        |
| `/.well-known/jwks.json`            | public keys for JWT validation |

### OAuth2 Client — `gateway/`

`gateway/application/build.gradle`:

```groovy
plugins { id 'spring-boot-application-conventions' }

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-security-oauth2-client'
    implementation 'org.springframework.boot:spring-boot-starter-security-oauth2-resource-server'
    implementation 'org.springframework.cloud:spring-cloud-starter-config'
    implementation 'org.springframework.cloud:spring-cloud-starter-gateway-server-webmvc'
    testImplementation 'org.springframework.boot:spring-boot-starter-security-oauth2-client-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-security-oauth2-resource-server-test'
}
```

`gateway/application/src/main/resources/application.properties`:

```properties
spring.security.oauth2.client.registration.gateway.client-id=gateway
spring.security.oauth2.client.registration.gateway.client-secret=secret
spring.security.oauth2.client.registration.gateway.authorization-grant-type=authorization_code
spring.security.oauth2.client.registration.gateway.scope=openid,read
spring.security.oauth2.client.provider.gateway.authorization-uri=http://localhost:8081/oauth2/authorize
spring.security.oauth2.client.provider.gateway.token-uri=http://localhost:8081/oauth2/token
spring.security.oauth2.client.provider.gateway.jwk-set-uri=http://localhost:8081/oauth2/jwks
spring.security.oauth2.client.provider.gateway.user-info-uri=http://localhost:8081/userinfo
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8081/oauth2/jwks

spring.cloud.gateway.default-filters[0]=TokenRelay
```

> **Note:** `issuer-uri` triggers eager OIDC discovery at startup — the app will fail if the Auth Server is unreachable. Use explicit provider URIs (`authorization-uri`, `token-uri`, `jwk-set-uri`, `user-info-uri`) which are lazy-resolved at first request. This is also required for test isolation.

`gateway/application/src/main/java/.../GatewaySecurityConfig.java`:

```java
@Bean
public SecurityFilterChain gatewayFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(a -> a
            .requestMatchers("/auth/register", "/auth/login").permitAll()
            .anyRequest().authenticated())
        .oauth2Login(Customizer.withDefaults())
        .oauth2ResourceServer(r -> r.jwt(Customizer.withDefaults()));
    return http.build();
}
```

### Resource Server — `user/`, `note/`, `user-note/`

JWT is validated against `auth/`'s public key via JWKS — no shared secret required.

Add to each service's `application/build.gradle`:

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-security-oauth2-resource-server'
}
```

`application/src/main/resources/application.properties`:

```properties
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8081
```

`application/src/main/java/.../SecurityConfig.java`:

```java
@Bean
public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(a -> a
            .requestMatchers("/actuator/health").permitAll()
            .anyRequest().authenticated())
        .oauth2ResourceServer(r -> r.jwt(Customizer.withDefaults()));
    return http.build();
}
```

---

## Service Discovery (Eureka)

`registry/` runs a Eureka Server. Every other service registers on startup and resolves other services by name — no hardcoded hostnames or ports.

### Eureka Server — `registry/`

`registry/application/build.gradle`:

```groovy
plugins { id 'spring-boot-application-conventions' }

dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-server'
}
```

`registry/application/src/main/java/.../RegistryApplication.java`:

```java
@SpringBootApplication
@EnableEurekaServer
public class RegistryApplication {}
```

`registry/application/src/main/resources/application.properties`:

```properties
spring.application.name=registry
server.port=8761
eureka.instance.hostname=localhost
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

### Eureka Client — all other services

`spring-cloud-starter-netflix-eureka-client` is included in `spring-boot-application-conventions`. Each service needs only:

```properties
spring.application.name=auth
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.instance.health-check-url-path=/actuator/health
eureka.instance.status-page-url-path=/actuator/info
```

No `@EnableDiscoveryClient` annotation — auto-configuration activates when the dependency is on the classpath.

### Kubernetes alternative

On Kubernetes, Eureka is replaced by native K8s `Service` discovery. Add to `application-k8s.properties`:

```properties
eureka.client.enabled=false
```

---

## Centralized Configuration (Config Server)

`config/` serves configuration files to all services at startup. Each service fetches its `application.properties` (and profile-specific overrides) from the Config Server before starting.

### Config Server — `config/`

`config/application/build.gradle`:

```groovy
plugins { id 'spring-boot-application-conventions' }

dependencies {
    implementation 'org.springframework.cloud:spring-cloud-config-server'
}
```

`config/application/src/main/java/.../ConfigApplication.java`:

```java
@SpringBootApplication
@EnableConfigServer
public class ConfigApplication {}
```

`config/application/src/main/resources/application.properties`:

```properties
spring.application.name=config
server.port=8888
spring.cloud.config.server.git.uri=file://${user.home}/notes-spring-config
spring.cloud.config.server.git.default-label=main
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
management.endpoints.web.exposure.include=health,info,metrics,prometheus,loggers
management.endpoint.health.show-details=always
management.endpoint.health.probes.enabled=true
management.tracing.sampling.probability=1.0
otel.exporter.otlp.endpoint=http://localhost:4318
```

### Config Client — all business services

`spring-cloud-starter-config` is added to each service's `application/build.gradle`. Each service requires:

```properties
spring.config.import=optional:configserver:http://localhost:8888
```

The `optional:` prefix means the service starts normally if the Config Server is unreachable (useful in tests and local development without the full stack).

### Startup order

Config Server depends on Eureka for registration, but services fetch config before Eureka registration. Run services in this order locally:

```
registry → config → auth → user · note · user-note → gateway
```

---

## API Gateway

`gateway/` is the single entry point for all client traffic. It routes requests, enforces authentication, and forwards JWT tokens to backend services. See [Security](#security-oauth2--oidc) for full OAuth2 configuration.

### Routing and load balancing

`gateway/application/src/main/resources/application.yml`:

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: auth
          uri: lb://auth
          predicates:
            - Path=/auth/**
        - id: user
          uri: lb://user
          predicates:
            - Path=/users/**
        - id: note
          uri: lb://note
          predicates:
            - Path=/notes/**
        - id: user-note
          uri: lb://user-note
          predicates:
            - Path=/user-notes/**
      default-filters:
        - TokenRelay
```

`lb://service-name` — Spring Cloud LoadBalancer resolves the name to a live Eureka instance. Default strategy is round-robin; set `spring.cloud.loadbalancer.configurations=random` to switch to random.

---

## OpenFeign

`user-note/feign/` calls `user/` and `note/` declaratively. OpenFeign resolves service names via Eureka + Spring Cloud LoadBalancer automatically.

### `spring-openfeign-adapter-conventions.gradle`

```groovy
plugins {
    id 'java-library'
    id 'io.spring.dependency-management'
}

dependencyManagement {
    imports {
        mavenBom org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES
        mavenBom 'org.springframework.cloud:spring-cloud-dependencies:2025.1.1'
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-openfeign'
    implementation 'org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

tasks.named('test') { useJUnitPlatform() }
```

Enable Feign clients in `user-note/application/src/main/java/.../UserNoteApplication.java`:

```java
@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.usernote.feign")
public class UserNoteApplication {}
```

### Client interfaces — `user-note/feign/`

Feign clients implement the port interfaces from `user-note/domain/`:

```java
@FeignClient(name = "user", fallback = UserFeignClientFallback.class)
public interface UserFeignClient extends UserClient {
    @GetMapping("/users/{id}")
    User findById(@PathVariable UUID id);
}

@FeignClient(name = "note", fallback = NoteFeignClientFallback.class)
public interface NoteFeignClient extends NoteClient {
    @GetMapping("/notes/{id}")
    Note findById(@PathVariable UUID id);
}
```

---

## Resilience4j

Resilience4j is included in `spring-openfeign-adapter-conventions` via `spring-cloud-starter-circuitbreaker-resilience4j`.

### Circuit breaker

Opens after a failure threshold; half-opens after the wait duration to probe recovery.

```properties
resilience4j.circuitbreaker.instances.user.failure-rate-threshold=50
resilience4j.circuitbreaker.instances.user.wait-duration-in-open-state=10s
resilience4j.circuitbreaker.instances.user.sliding-window-size=10
```

Fallback on a Feign client:

```java
@Component
class UserFeignClientFallback implements UserFeignClient {
    public User findById(UUID id) {
        return User.unknown(id);
    }
}
```

### Retry

```properties
resilience4j.retry.instances.user.max-attempts=3
resilience4j.retry.instances.user.wait-duration=500ms
```

### Rate limiter

```properties
resilience4j.ratelimiter.instances.note.limit-for-period=100
resilience4j.ratelimiter.instances.note.limit-refresh-period=1s
resilience4j.ratelimiter.instances.note.timeout-duration=0
```

---

## OpenAPI / Swagger

`springdoc-openapi` is included in `spring-webmvc-adapter-conventions` — no per-service setup needed.

Each business service exposes:

| Path           | Description                        |
|----------------|------------------------------------|
| `/v3/api-docs` | OpenAPI JSON spec for this service |
| `/swagger-ui`  | Swagger UI for this service        |

`gateway/` aggregates all specs into a single Swagger UI at `http://localhost:8080/swagger-ui`.

`gateway/application/src/main/resources/application.properties`:

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

---

## Key Configuration

### Service ports

| Service   | Port |
|-----------|------|
| gateway   | 8080 |
| auth      | 8081 |
| user      | 8082 |
| note      | 8083 |
| user-note | 8084 |
| registry  | 8761 |
| config    | 8888 |

### Business service `application.properties`

Example for `auth/` (ports 8081–8084 per the port table above):

```properties
spring.application.name=auth-service
server.port=8081
spring.config.import=optional:configserver:http://localhost:8888
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
management.endpoints.web.exposure.include=health,info,metrics,prometheus,loggers
management.endpoint.health.show-details=always
management.endpoint.health.probes.enabled=true
management.tracing.sampling.probability=1.0
otel.exporter.otlp.endpoint=http://localhost:4318
```

Resource Server services (`user/`, `note/`, `user-note/`) add JWT validation via JWKS (lazy — no startup dependency on auth):

```properties
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8081
```

### `registry/application.properties`

```properties
spring.application.name=registry
server.port=8761
eureka.instance.hostname=localhost
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
management.endpoints.web.exposure.include=health,info,metrics,prometheus,loggers
management.endpoint.health.show-details=always
management.endpoint.health.probes.enabled=true
management.tracing.sampling.probability=1.0
otel.exporter.otlp.endpoint=http://localhost:4318
```

### `gateway/application.properties`

```properties
spring.application.name=gateway
server.port=8080
spring.config.import=optional:configserver:http://localhost:8888
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
spring.cloud.gateway.discovery.locator.enabled=true
spring.cloud.gateway.discovery.locator.lower-case-service-id=true
spring.cloud.gateway.default-filters[0]=TokenRelay
spring.security.oauth2.client.registration.gateway.client-id=gateway
spring.security.oauth2.client.registration.gateway.client-secret=secret
spring.security.oauth2.client.registration.gateway.authorization-grant-type=authorization_code
spring.security.oauth2.client.registration.gateway.scope=openid,read
spring.security.oauth2.client.provider.gateway.authorization-uri=http://localhost:8081/oauth2/authorize
spring.security.oauth2.client.provider.gateway.token-uri=http://localhost:8081/oauth2/token
spring.security.oauth2.client.provider.gateway.jwk-set-uri=http://localhost:8081/oauth2/jwks
spring.security.oauth2.client.provider.gateway.user-info-uri=http://localhost:8081/userinfo
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8081/oauth2/jwks
management.endpoints.web.exposure.include=health,info,metrics,prometheus,loggers
management.endpoint.health.show-details=always
management.endpoint.health.probes.enabled=true
management.tracing.sampling.probability=1.0
otel.exporter.otlp.endpoint=http://localhost:4318
```

`gateway/src/test/resources/application.properties` (test isolation — excludes OAuth2 auto-config to avoid eager OIDC discovery at test startup):

```properties
spring.config.import=optional:configserver:
spring.autoconfigure.exclude[0]=org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration
spring.autoconfigure.exclude[1]=org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration
```

### `application-k8s.properties` (profile — overrides base config)

```properties
eureka.client.enabled=false
spring.cloud.config.enabled=false
```

---

## Observability

### Spring Boot Actuator

Included in `spring-boot-application-conventions`. Endpoints exposed on every service:

| Endpoint               | Purpose                       |
|------------------------|-------------------------------|
| `/actuator/health`     | liveness and readiness probes |
| `/actuator/info`       | service metadata              |
| `/actuator/metrics`    | application metrics           |
| `/actuator/prometheus` | Prometheus scrape endpoint    |
| `/actuator/loggers`    | runtime log-level management  |

### Prometheus

`micrometer-registry-prometheus` is included in `spring-boot-application-conventions`. Prometheus scrapes `/actuator/prometheus` on each service.

`prometheus.yml`:

```yaml
scrape_configs:
  - job_name: auth
    metrics_path: /actuator/prometheus
    static_configs:
      - targets: ['localhost:8081']
  - job_name: user
    metrics_path: /actuator/prometheus
    static_configs:
      - targets: ['localhost:8082']
  - job_name: note
    metrics_path: /actuator/prometheus
    static_configs:
      - targets: ['localhost:8083']
  - job_name: user-note
    metrics_path: /actuator/prometheus
    static_configs:
      - targets: ['localhost:8084']
```

### OpenTelemetry

`spring-boot-starter-opentelemetry` is included in `spring-boot-application-conventions`. Every HTTP request, Feign call, and database query is traced automatically.

Trace pipeline: service → OpenTelemetry Collector → Jaeger or Grafana Tempo.

### Monitoring stack

| Tool                    | Purpose                              |
|-------------------------|--------------------------------------|
| Prometheus              | metrics collection                   |
| Grafana                 | dashboards and alerts                |
| OpenTelemetry Collector | trace aggregation and routing        |
| Jaeger / Grafana Tempo  | trace storage and visualization      |
| Elastic Stack (ELK)     | centralized log aggregation          |

---

## Deployment

Target runtime is **AWS** — Kubernetes for orchestration (Eureka disabled in `k8s` profile), Helm for parameterized multi-environment deploys.

### Docker

`bootBuildImage` is available on every `application/` module — `org.springframework.boot` is applied by `spring-boot-application-conventions`.

```shell
./gradlew :auth:application:bootBuildImage --imageName=notes-spring/auth:latest
```

### Docker Compose

`docker-compose.yml` at the repository root:

```yaml
services:
  registry:
    image: notes-spring/registry:latest
    ports: ["8761:8761"]

  config:
    image: notes-spring/config:latest
    ports: ["8888:8888"]
    depends_on: [registry]

  auth:
    image: notes-spring/auth:latest
    ports: ["8081:8081"]
    depends_on: [registry, config]

  user:
    image: notes-spring/user:latest
    ports: ["8082:8082"]
    depends_on: [registry, config]

  note:
    image: notes-spring/note:latest
    ports: ["8083:8083"]
    depends_on: [registry, config]

  user-note:
    image: notes-spring/user-note:latest
    ports: ["8084:8084"]
    depends_on: [registry, config, user, note]

  gateway:
    image: notes-spring/gateway:latest
    ports: ["8080:8080"]
    depends_on: [registry, auth, user, note, user-note]

  prometheus:
    image: prom/prometheus:latest
    ports: ["9090:9090"]

  grafana:
    image: grafana/grafana:latest
    ports: ["3000:3000"]
```

### Kubernetes

On Kubernetes, Eureka is replaced by K8s `Service` discovery; `ConfigMap` replaces the Config Server. Activate via the `k8s` Spring profile (`application-k8s.properties`).

Liveness and readiness probes:

```yaml
livenessProbe:
  httpGet:
    path: /actuator/health/liveness
    port: 8081
readinessProbe:
  httpGet:
    path: /actuator/health/readiness
    port: 8081
```

### Helm

Each service has a `helm/` directory. A root umbrella chart assembles the full stack:

```
notes-spring/
├── helm/
│   ├── Chart.yaml     umbrella chart
│   ├── values.yaml    shared defaults
│   └── charts/        per-service subchart tarballs
├── auth/helm/
├── user/helm/
├── note/helm/
└── user-note/helm/
```

```shell
helm upgrade --install notes-spring ./helm \
  --namespace notes-spring \
  --create-namespace
```

---

## Secrets Management

Vault stores secrets (database passwords, JWT signing keys, API credentials) and injects them as Spring properties at startup via Spring Cloud Vault.

Add to `spring-boot-application-conventions.gradle`:

```groovy
dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-vault-config'
}
```

`application.properties`:

```properties
spring.cloud.vault.uri=http://localhost:8200
spring.cloud.vault.authentication=token
spring.cloud.vault.token=${VAULT_TOKEN}
spring.cloud.vault.kv.enabled=true
spring.cloud.vault.kv.backend=secret
spring.cloud.vault.kv.default-context=auth   # change per service
spring.config.import=vault://
```

Secrets at `secret/auth` in Vault are injected automatically. In production, replace token auth with Kubernetes auth or AWS IAM.

---

## Testing

Testcontainers starts real infrastructure (database, Redis, Kafka, etc.) inside Docker during tests. Use it in adapter modules (`data-jpa/`, `cache/`, `rabbitmq/`, `kafka/`) to avoid mocking persistence.

Add to the relevant adapter convention plugin:

```groovy
dependencies {
    testImplementation 'org.springframework.boot:spring-boot-testcontainers'
    testImplementation 'org.testcontainers:testcontainers-postgresql'   // or :testcontainers-kafka, :testcontainers-vault, etc.
}
```

Example integration test:

```java
@SpringBootTest
@Testcontainers
class UserRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
}
```

