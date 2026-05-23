# Architecture

Collaborative notes application with role-based access control.
Built on Spring Boot 4, Gradle composite builds, microservices monorepo.

---

## How to read this document

This is a **design specification**, not a project status report.

- It describes the intended architecture, conventions, and technology choices for the complete application.
- The actual implementation may be at any stage — some modules may not exist yet, some choices may have been revised for valid reasons.
- **If you find a deviation** between this document and the code: decide which is correct, then synchronize both. The document and the code must always agree.
- **If a deliberate alternative was chosen** (a different adapter, library, or approach) during implementation: update this document to reflect that choice. The document always describes what was actually built, not what was originally planned.

This document is sufficient to reconstruct the project from scratch — by a human or a machine.

---

## Tech Stack

| Concern           | Technology                                          |
|-------------------|-----------------------------------------------------|
| Language          | Java                                                |
| Framework         | Spring Boot 4.0.6                                   |
| Build             | Gradle 9.5.1 — composite builds                     |
| Security          | Spring Authorization Server — OAuth2 / OIDC         |
| API gateway       | Spring Cloud Gateway MVC                            |
| Service discovery | Spring Cloud Netflix Eureka                         |
| Configuration     | Spring Cloud Config Server                          |
| HTTP client       | Spring Cloud OpenFeign + Resilience4j               |
| Persistence       | Spring Data JPA                                     |
| Schema migration  | Liquibase or Flyway                                 |
| Caching           | Spring Data Redis                                   |
| Search            | Spring Data Elasticsearch                           |
| Messaging         | Spring AMQP (RabbitMQ) or Spring Kafka              |
| Real-time         | Spring WebSocket + STOMP                            |
| RPC               | gRPC + Protobuf                                     |
| API docs          | springdoc-openapi                                   |
| Metrics           | Micrometer + Prometheus + Grafana                   |
| Tracing           | OpenTelemetry + Jaeger / Grafana Tempo              |
| Logs              | Elastic Stack (ELK)                                 |
| Secrets           | HashiCorp Vault — Spring Cloud Vault                |
| Testing           | Testcontainers                                      |
| Packaging         | Docker — Spring Boot Buildpacks                     |
| Orchestration     | Kubernetes + Helm                                   |

> Spring Cloud BOM (`org.springframework.cloud:spring-cloud-dependencies:2025.1.1`) is imported
> inside `spring-boot-application-conventions.gradle` via `dependencyManagement`.
> No changes to `build-logic/build.gradle` are needed.

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

Rules that follow from these principles:

- Business model lives in `domain/` only — never in adapters or `application/`.
- Adapters depend on `domain/` only — never on `application/`.
- Full elimination or explicit duplication — no partial abstractions.

---

## Project Structure

```
notes-spring/
├── build-logic/           Gradle convention plugins
│
├── crud/                  Shared CRUD library — no application/
│   ├── domain/
│   ├── webmvc/
│   └── data-jpa/
│
├── auth/                  Spring Authorization Server
│   ├── domain/
│   ├── application/
│   ├── webmvc/
│   └── data-jpa/
│
├── user/                  User profiles
│   ├── domain/
│   ├── application/
│   ├── webmvc/
│   └── data-jpa/
│
├── note/                  Notes
│   ├── domain/
│   ├── application/
│   ├── webmvc/
│   └── data-jpa/
│
├── user-note/             Access control — calls user/ and note/ via feign/
│   ├── domain/
│   ├── application/
│   ├── webmvc/
│   ├── data-jpa/
│   └── feign/
│
├── gateway/               Spring Cloud Gateway — single entry point
│   └── application/
│
├── registry/              Eureka Server — service discovery
│   └── application/
│
└── config/                Spring Cloud Config Server — centralized configuration
    └── application/
```

**Business services** — `domain/` + `application/` + adapters: `auth` · `user` · `note` · `user-note`

**Infrastructure services** — single `application/` each: `gateway` · `registry` · `config`

**Shared library** — adapters only, no `application/`: `crud`

---

## Hexagonal Architecture (Ports & Adapters)

Every business service has the same internal structure: a pure-Java core (`domain/`) surrounded by swappable adapters. `domain/` defines **what** the service does. Adapters define **how** it is done.

```
┌─────────────────────────────────────────────────────┐
│                    application/                     │
│         Spring Boot · use cases · wiring            │
│                                                     │
│   ┌──────────┐  ┌───────────┐  ┌────────────────┐   │
│   │ webmvc/  │  │ data-jpa/ │  │    feign/      │   │
│   │  HTTP in │  │persistence│  │ (user-note/)   │   │
│   └────┬─────┘  └─────┬─────┘  └───────┬────────┘   │
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

**Dependency rule:** adapters depend on `domain/` only. Importing `application/` pulls `spring-boot-starter` into a library module and breaks isolation.

### Module dependencies

| Module         | Depends on                               |
|----------------|------------------------------------------|
| `domain/`      | nothing — pure Java, no frameworks       |
| `webmvc/`      | `domain/`                                |
| `data-jpa/`    | `domain/`                                |
| `feign/`       | `domain/` — user-note only               |
| `application/` | `domain/` + all adapters of this service |

### Adapter catalogue

Each adapter type has a dedicated convention plugin in `build-logic/`.
To swap an adapter: (1) create the new module, (2) implement the same port interfaces from `domain/`, (3) add the convention plugin, (4) replace the dependency in `application/build.gradle` — `domain/` stays untouched.

**Incoming HTTP**

| Module     | Technology         | Style    | Convention plugin                    |
|------------|--------------------|----------|--------------------------------------|
| `webmvc/`  | Spring MVC         | sync     | `spring-webmvc-adapter-conventions`  |
| `webflux/` | Spring WebFlux     | reactive | `spring-webflux-adapter-conventions` |
| `graphql/` | Spring for GraphQL | sync/rx  | `spring-graphql-adapter-conventions` |

**Persistence**

| Module      | Technology          | Style         | Convention plugin                       |
|-------------|---------------------|---------------|-----------------------------------------|
| `data-jpa/` | Spring Data JPA     | sync, SQL     | `spring-data-jpa-adapter-conventions`   |
| `r2dbc/`    | Spring Data R2DBC   | reactive, SQL | `spring-data-r2dbc-adapter-conventions` |
| `mongo/`    | Spring Data MongoDB | sync/rx       | `spring-data-mongo-adapter-conventions` |

**Database driver** — add inside the persistence adapter convention plugin, not a separate module:

| Driver     | Usage                  | Artifact                          |
|------------|------------------------|-----------------------------------|
| H2         | development, in-memory | `spring-h2-database-conventions` mixin |
| PostgreSQL | production             | `org.postgresql:postgresql`       |
| MySQL      | production             | `com.mysql:mysql-connector-j`     |

**Schema migration** — add to the persistence adapter convention plugin:

| Tool      | Artifact                                            |
|-----------|-----------------------------------------------------|
| Liquibase | `org.springframework.boot:spring-boot-starter-liquibase` |
| Flyway    | `org.springframework.boot:spring-boot-starter-flyway`    |

Migration files: `src/main/resources/db/changelog/` (Liquibase) · `src/main/resources/db/migration/` (Flyway).

**Outgoing HTTP**

| Module         | Technology             | Style             | Convention plugin                        |
|----------------|------------------------|-------------------|------------------------------------------|
| `feign/`       | Spring Cloud OpenFeign | declarative, sync | `spring-openfeign-adapter-conventions`   |
| `rest-client/` | Spring RestClient      | imperative, sync  | `spring-rest-client-adapter-conventions` |
| `web-client/`  | Spring WebClient       | reactive          | `spring-web-client-adapter-conventions`  |

**Caching**

| Module   | Technology        | Convention plugin                       |
|----------|-------------------|-----------------------------------------|
| `cache/` | Spring Data Redis | `spring-data-redis-adapter-conventions` |

**Search**

| Module    | Technology                | Convention plugin                          |
|-----------|---------------------------|--------------------------------------------|
| `search/` | Spring Data Elasticsearch | `spring-elasticsearch-adapter-conventions` |

**Messaging** — a messaging adapter is both consumer (incoming) and producer (outgoing):

| Module      | Technology  | Style       | Convention plugin                     |
|-------------|-------------|-------------|---------------------------------------|
| `rabbitmq/` | Spring AMQP | push, queue | `spring-rabbitmq-adapter-conventions` |
| `kafka/`    | Spring Kafka | stream     | `spring-kafka-adapter-conventions`    |

**WebSocket**

| Module       | Technology             | Convention plugin                      |
|--------------|------------------------|----------------------------------------|
| `websocket/` | Spring WebSocket+STOMP | `spring-websocket-adapter-conventions` |

**gRPC** — one module handles both server and client stubs; `.proto` files go in `src/main/proto/`:

| Module  | Technology                     | Convention plugin                 |
|---------|--------------------------------|-----------------------------------|
| `grpc/` | gRPC-Java + Protobuf           | `spring-grpc-adapter-conventions` |

**Validation** — Bean Validation applies across layers, not a separate module:

| Layer        | How                                                           |
|--------------|---------------------------------------------------------------|
| `webmvc/`    | `@Valid` on controller parameters — validates the request body |
| `domain/`    | `@NotNull`, `@Size`, etc. on entity fields                    |
| Any adapter  | add `spring-boot-starter-validation` to the convention plugin  |

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

Defined in `domain/`, implemented in adapters.

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

| Service     | Method | Path                      | Description         |
|-------------|--------|---------------------------|---------------------|
| `auth`      | POST   | /auth/register            | register new user   |
| `auth`      | POST   | /auth/login               | obtain tokens       |
| `auth`      | POST   | /auth/logout              | revoke refresh token |
| `auth`      | POST   | /auth/refresh-token       | rotate refresh token |
| `user`      | GET    | /users/{id}               | get user profile    |
| `user`      | PUT    | /users/{id}               | update profile      |
| `note`      | GET    | /notes/{id}               | get note            |
| `note`      | POST   | /notes                    | create note         |
| `note`      | PUT    | /notes/{id}               | update note         |
| `note`      | DELETE | /notes/{id}               | delete note         |
| `user-note` | GET    | /user-notes               | list access entries |
| `user-note` | POST   | /user-notes               | grant access        |
| `user-note` | DELETE | /user-notes/{id}          | revoke access       |
| `user-note` | PUT    | /user-notes/{id}/transfer | transfer ownership  |

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

### OAuth2 Authorization Server — `auth/`

`auth/application/build.gradle`:

```groovy
plugins { id 'spring-boot-application-conventions' }

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-security-oauth2-authorization-server'
    implementation project(':webmvc')
    implementation project(':data-jpa')
    implementation project(':domain')
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

Standard endpoints exposed by Spring Authorization Server:

| Endpoint                            | Description              |
|-------------------------------------|--------------------------|
| `/oauth2/authorize`                 | authorization code flow  |
| `/oauth2/token`                     | token issuance           |
| `/oauth2/revoke`                    | token revocation         |
| `/oauth2/introspect`                | token introspection      |
| `/.well-known/openid-configuration` | OIDC discovery document  |
| `/.well-known/jwks.json`            | public keys for JWT validation |

### OAuth2 Client — `gateway/`

`gateway/` initiates the authorization code flow for the browser and forwards the JWT to backend services via the `TokenRelay` filter.

`gateway/application/build.gradle`:

```groovy
plugins { id 'spring-boot-application-conventions' }

dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-gateway-server-webmvc'
    implementation 'org.springframework.boot:spring-boot-starter-security-oauth2-client'
    implementation 'org.springframework.boot:spring-boot-starter-security-oauth2-resource-server'
}
```

`gateway/application/src/main/resources/application.properties`:

```properties
spring.security.oauth2.client.registration.gateway.client-id=gateway
spring.security.oauth2.client.registration.gateway.client-secret=secret
spring.security.oauth2.client.registration.gateway.authorization-grant-type=authorization_code
spring.security.oauth2.client.registration.gateway.scope=openid,read
spring.security.oauth2.client.provider.gateway.issuer-uri=http://localhost:8081

spring.cloud.gateway.default-filters[0]=TokenRelay
```

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

### OAuth2 Resource Server — `user/`, `note/`, `user-note/`

Each service validates the JWT forwarded by the gateway using the public key fetched from `auth/`'s JWKS endpoint — no shared secret required.

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

`registry/` runs a Eureka Server. Every other service registers itself on startup and resolves other services by name — no hardcoded hostnames or ports.

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

### Eureka Client — every business and infrastructure service

`spring-cloud-starter-netflix-eureka-client` is included in `spring-boot-application-conventions` — no explicit configuration needed beyond the service name and zone:

```properties
spring.application.name=auth
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.instance.health-check-url-path=/actuator/health
eureka.instance.status-page-url-path=/actuator/info
```

No `@EnableDiscoveryClient` annotation needed — auto-configuration activates when the dependency is on the classpath.

### Gateway routing via Eureka

```properties
spring.cloud.gateway.discovery.locator.enabled=true
spring.cloud.gateway.discovery.locator.lower-case-service-id=true
```

A request to `/auth/**` is routed to the `auth` instance registered in Eureka — no static URLs.

### Kubernetes alternative

On Kubernetes, Eureka is replaced by native K8s `Service` discovery:

```properties
# application-k8s.properties
eureka.client.enabled=false
```

---

## API Gateway

`gateway/` is the single entry point for all client traffic. It authenticates requests, routes them to backend services, and applies cross-cutting filters. See [Security](#security-oauth2--oidc) for OAuth2 configuration.

### Routing

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

`lb://service-name` — Spring Cloud LoadBalancer resolves the name to a live instance via Eureka.

---

## OpenFeign

`user-note/feign/` calls `user/` and `note/` declaratively. OpenFeign resolves service names via Eureka + LoadBalancer automatically.

### `spring-openfeign-adapter-conventions.gradle`

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

repositories { mavenCentral() }

dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-openfeign'
    implementation 'org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j'
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

Feign clients implement the port interfaces declared in `user-note/domain/`:

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

## Load Balancing

Spring Cloud LoadBalancer is included transitively with `spring-cloud-starter-openfeign` and `spring-cloud-starter-gateway-server-webmvc`. No explicit configuration is required — it activates automatically when a Eureka client is on the classpath.

Default strategy: **round-robin**.

```properties
# switch to random
spring.cloud.loadbalancer.configurations=random
```

Custom strategy: implement `ServiceInstanceListSupplier` and register it as a `@Bean`.

---

## Resilience4j

Resilience4j protects inter-service calls with circuit breakers, retries, and rate limiters. It is included in `spring-openfeign-adapter-conventions` via `spring-cloud-starter-circuitbreaker-resilience4j`.

### Circuit breaker

Opens after a failure threshold is reached; half-opens after the wait duration to probe recovery.

```properties
resilience4j.circuitbreaker.instances.user.failure-rate-threshold=50
resilience4j.circuitbreaker.instances.user.wait-duration-in-open-state=10s
resilience4j.circuitbreaker.instances.user.sliding-window-size=10
```

Fallback for a Feign client:

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

### Actuator endpoints

```properties
management.endpoints.web.exposure.include=health,circuitbreakers,retries
management.endpoint.health.show-details=always
```

---

## OpenAPI / Swagger

Each business service exposes its API spec at `/v3/api-docs`. `gateway/` aggregates all specs into a single Swagger UI at `http://localhost:8080/swagger-ui`.

`springdoc-openapi` is included in `spring-webmvc-adapter-conventions` — no per-service configuration needed.

| Path           | Description                        |
|----------------|------------------------------------|
| `/v3/api-docs` | OpenAPI JSON spec for this service |
| `/swagger-ui`  | Swagger UI for this service        |

### Aggregation in `gateway/`

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

## Implementation Order

Build modules in dependency order: `domain/` → `data-jpa/` → `webmvc/` → `application/`.

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

### `gradle.properties` — root

```properties
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configureondemand=true
org.gradle.configuration-cache=true
org.gradle.configuration-cache.parallel=true
```

### `settings.gradle` — root

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

### `build.gradle` — root

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
def projects = subprojects.collect { it.name }

tasks.register('clean') { dependsOn projects.collect { ":${it}:clean" } }
tasks.register('test')  { dependsOn projects.collect { ":${it}:test"  } }
tasks.register('check') { dependsOn projects.collect { ":${it}:check" } }
tasks.register('build') { dependsOn projects.collect { ":${it}:build" } }
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
    // add Spring Cloud Gradle plugin when using Gateway, Eureka, Config, or OpenFeign
}
```

---

## Convention Plugins

All plugins are `.gradle` files in `build-logic/src/main/groovy/`.

Adapter plugins share the same structure: `java-library` + `io.spring.dependency-management` + Spring Boot BOM import + starter dependency.

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

Applied to every `application/` module. Includes Spring Boot, Actuator, Eureka client, Prometheus metrics, and distributed tracing.

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
    implementation 'org.springframework.boot:spring-boot-starter'
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
    implementation 'org.springframework.boot:spring-boot-starter-opentelemetry'
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
    implementation 'io.micrometer:micrometer-registry-prometheus'
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

Mixin plugin — applied alongside another plugin, never standalone.

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-h2console'
    runtimeOnly 'com.h2database:h2'
}
```

### Additional adapter plugins

All follow the same structure as `spring-webmvc-adapter-conventions.gradle`.

| Plugin                                   | Key dependency                                                            |
|------------------------------------------|---------------------------------------------------------------------------|
| `spring-openfeign-adapter-conventions`   | `spring-cloud-starter-openfeign` + `circuitbreaker-resilience4j`          |
| `spring-rest-client-adapter-conventions` | `spring-boot-starter-web`                                                 |
| `spring-web-client-adapter-conventions`  | `spring-boot-starter-webflux`                                             |
| `spring-webflux-adapter-conventions`     | `spring-boot-starter-webflux`                                             |
| `spring-graphql-adapter-conventions`     | `spring-boot-starter-graphql`                                             |
| `spring-data-r2dbc-adapter-conventions`  | `spring-boot-starter-data-r2dbc`                                          |
| `spring-data-mongo-adapter-conventions`  | `spring-boot-starter-data-mongodb`                                        |
| `spring-data-redis-adapter-conventions`  | `spring-boot-starter-data-redis`                                          |
| `spring-elasticsearch-adapter-conventions` | `spring-boot-starter-data-elasticsearch`                                |
| `spring-rabbitmq-adapter-conventions`    | `spring-boot-starter-amqp`                                                |
| `spring-kafka-adapter-conventions`       | `spring-boot-starter-kafka`                                               |
| `spring-websocket-adapter-conventions`   | `spring-boot-starter-websocket`                                           |
| `spring-grpc-adapter-conventions`        | `grpc-spring-boot-starter` + `protobuf-gradle-plugin`                     |

---

## Key Configuration

### Business service — `application.properties`

```properties
spring.application.name=auth
server.port=8081

spring.datasource.url=jdbc:h2:mem:authdb

eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
spring.config.import=configserver:http://localhost:8888

management.endpoints.web.exposure.include=health,info,metrics,prometheus,loggers
management.endpoint.health.show-details=always
management.endpoint.health.probes.enabled=true

management.tracing.sampling.probability=1.0
otel.exporter.otlp.endpoint=http://localhost:4318

spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8081
```

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
spring.cloud.gateway.discovery.locator.lower-case-service-id=true
```

---

## Observability

### Spring Boot Actuator

Included in `spring-boot-application-conventions`. Exposes the following endpoints on every service:

| Endpoint               | Purpose                                   |
|------------------------|-------------------------------------------|
| `/actuator/health`     | liveness and readiness probes             |
| `/actuator/info`       | service metadata                          |
| `/actuator/metrics`    | application metrics                       |
| `/actuator/prometheus` | Prometheus scrape endpoint                |
| `/actuator/loggers`    | runtime log-level management              |

### Prometheus

Prometheus scrapes `/actuator/prometheus` on every service. `micrometer-registry-prometheus` is included in `spring-boot-application-conventions`.

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

Grafana connects to Prometheus as a data source and visualizes dashboards.

### OpenTelemetry

`spring-boot-starter-opentelemetry` is included in `spring-boot-application-conventions`. It provides the OTel SDK, Micrometer tracing bridge, and OTLP exporter — every incoming HTTP request, outgoing Feign call, and database query is automatically traced.

Trace pipeline: service → OpenTelemetry Collector → Jaeger or Grafana Tempo.

### Monitoring stack

| Tool                     | Purpose                                  |
|--------------------------|------------------------------------------|
| Prometheus               | metrics collection                       |
| Grafana                  | dashboards and alerts                    |
| OpenTelemetry Collector  | trace aggregation and routing            |
| Jaeger / Grafana Tempo   | trace storage and visualization          |
| Elastic Stack (ELK)      | centralized log aggregation              |

---

## Deployment

### Docker

`bootBuildImage` is available on every `application/` module because `org.springframework.boot` is applied by `spring-boot-application-conventions`.

Build an image:

```shell
./gradlew :auth:application:bootBuildImage --imageName=notes-spring/auth:latest
```

### Docker Compose

`docker-compose.yml` at the repository root starts the full stack locally:

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

On Kubernetes, Eureka is disabled — K8s `Service` objects handle discovery. `ConfigMap` replaces the Config Server.

`application-k8s.properties`:

```properties
eureka.client.enabled=false
spring.cloud.config.enabled=false
management.endpoint.health.probes.enabled=true
```

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

Deploy to a cluster:

```shell
helm upgrade --install notes-spring ./helm \
  --namespace notes-spring \
  --create-namespace
```

---

## Secrets Management

### HashiCorp Vault

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

Secrets at `secret/auth` in Vault are injected automatically. In production, replace token authentication with Kubernetes auth or AWS IAM.

---

## Testing

### Testcontainers

Testcontainers starts real infrastructure (database, Redis, Kafka, etc.) inside Docker during tests. Use it in adapter modules (`data-jpa/`, `cache/`, `rabbitmq/`, `kafka/`) to test against real systems without mocking.

Add to the relevant adapter convention plugin:

```groovy
dependencies {
    testImplementation 'org.springframework.boot:spring-boot-testcontainers'
    testImplementation 'org.testcontainers:postgresql'   // or :kafka, :redis, etc.
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

---

## Production Platform

The target runtime is **AWS**. Kubernetes manages container orchestration and service discovery (Eureka disabled). Helm handles parameterized, reproducible deployments across environments. See [Deployment](#deployment) for full configuration.
