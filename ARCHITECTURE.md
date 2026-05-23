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
| Security          | Spring Authorization Server (OAuth2 / OIDC)     |
| API gateway       | Spring Cloud Gateway (MVC)                      |
| Service discovery | Spring Cloud Netflix Eureka                     |
| Configuration     | Spring Cloud Config Server                      |
| HTTP client       | Spring Cloud OpenFeign + Resilience4j           |
| Persistence       | Spring Data JPA + H2 (dev)                      |
| Schema migration  | Liquibase or Flyway                             |
| Caching           | Spring Data Redis                               |
| Search            | Spring Data Elasticsearch                       |
| Messaging         | Spring AMQP (RabbitMQ) or Spring Kafka          |
| Real-time         | Spring WebSocket + STOMP                        |
| RPC               | gRPC + Protobuf                                 |
| API docs          | springdoc-openapi                               |
| Metrics           | Micrometer + Prometheus + Grafana               |
| Tracing           | OpenTelemetry + Jaeger / Grafana Tempo          |
| Logs              | Elastic Stack (ELK)                             |
| Secrets           | HashiCorp Vault (Spring Cloud Vault)            |
| Testing           | Testcontainers                                  |
| Deployment        | Docker · Docker Compose · Kubernetes · Helm     |

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
| Liquibase | add `org.springframework.boot:spring-boot-starter-liquibase` to `spring-data-jpa-adapter-conventions` |
| Flyway    | add `org.springframework.boot:spring-boot-starter-flyway` to `spring-data-jpa-adapter-conventions`    |

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

## Service Discovery (Eureka)

`registry/` runs a Eureka Server. Every other service is a Eureka client — it registers itself on startup and discovers other services by name, not by hostname or port.

### `registry/application/build.gradle`

```groovy
plugins {
    id 'spring-boot-application-conventions'
}
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

### Eureka client (every business and infrastructure service)

Add to `spring-boot-application-conventions.gradle`:

```groovy
dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
    // ...existing deps
}
```

`application.properties` per service:

```properties
spring.application.name=auth    # Eureka registers the service under this name
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

No `@EnableDiscoveryClient` annotation needed — auto-configuration activates the client when the dependency is on the classpath.

### Health check integration

Eureka uses Actuator's `/actuator/health` to mark instances UP or DOWN.
Requires `spring-boot-starter-actuator` (already in `spring-boot-application-conventions`).

```properties
eureka.instance.health-check-url-path=/actuator/health
eureka.instance.status-page-url-path=/actuator/info
```

### Gateway routing via Eureka

`gateway/` resolves service names from Eureka automatically:

```properties
spring.cloud.gateway.discovery.locator.enabled=true
spring.cloud.gateway.discovery.locator.lower-case-service-id=true
```

A request to `/auth/**` is routed to the `auth` instance registered in Eureka — no static URLs needed.

### Kubernetes alternative

On Kubernetes, Eureka is disabled entirely. K8s `Service` objects handle discovery.

```properties
# application-k8s.properties
eureka.client.enabled=false
```

---

## API Gateway

`gateway/` is a Spring Cloud Gateway application — the single entry point for all client traffic.
It routes requests to backend services, applies cross-cutting filters, and enforces authentication.

### `gateway/application/build.gradle`

```groovy
plugins {
    id 'spring-boot-application-conventions'
}
dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-gateway-server-webmvc'
    implementation 'org.springframework.boot:spring-boot-starter-security-oauth2-client'
    implementation 'org.springframework.boot:spring-boot-starter-security-oauth2-resource-server'
}
```

### Routing

Routes are defined in `application.properties` (or `application.yml`):

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: auth
          uri: lb://auth          # lb:// means LoadBalancer resolves via Eureka
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
```

### Filters

Apply cross-cutting concerns via gateway filters:

```yaml
spring:
  cloud:
    gateway:
      default-filters:
        - TokenRelay          # forward OAuth2 token to downstream services
        - CircuitBreaker=...  # wrap all routes with a circuit breaker
```

See [Security](#security) for OAuth2 configuration.

---

## OpenFeign

`feign/` in `user-note/` calls `user/` and `note/` services declaratively.
OpenFeign resolves service names via Eureka + LoadBalancer.

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

Enable Feign in `user-note/application/`:

```java
@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.usernote.feign")
public class UserNoteApplication {}
```

### Feign client interfaces (in `user-note/feign/`)

These implement the port interfaces defined in `user-note/domain/`:

```java
@FeignClient(name = "user")        // "user" is the Eureka service name
public interface UserFeignClient extends UserClient {
    @GetMapping("/users/{id}")
    User findById(@PathVariable UUID id);
}

@FeignClient(name = "note")
public interface NoteFeignClient extends NoteClient {
    @GetMapping("/notes/{id}")
    Note findById(@PathVariable UUID id);
}
```

---

## Load Balancing

Spring Cloud LoadBalancer is included with `spring-cloud-starter-openfeign` and `spring-cloud-starter-gateway-server-webmvc`.
It resolves `lb://service-name` URIs and `@FeignClient(name = "service-name")` to live instances from Eureka.

No explicit configuration is required — LoadBalancer activates automatically when a Eureka client is on the classpath.

Default strategy: **round-robin**. To switch to random:

```properties
spring.cloud.loadbalancer.configurations=random
```

Custom strategy: implement `ServiceInstanceListSupplier` and register it as a `@Bean`.

---

## Resilience4j

Resilience4j protects inter-service calls with circuit breakers, retries, and rate limiting.
It integrates with OpenFeign and Spring Cloud Gateway via `spring-cloud-starter-circuitbreaker-resilience4j`.

### Circuit breaker

Wraps a Feign client or a gateway route. Opens after a threshold of failures; half-opens after a wait duration to probe recovery.

`application.properties`:

```properties
resilience4j.circuitbreaker.instances.user.failure-rate-threshold=50
resilience4j.circuitbreaker.instances.user.wait-duration-in-open-state=10s
resilience4j.circuitbreaker.instances.user.sliding-window-size=10
```

Fallback on a Feign client:

```java
@FeignClient(name = "user", fallback = UserFeignClientFallback.class)
public interface UserFeignClient extends UserClient {}

@Component
class UserFeignClientFallback implements UserFeignClient {
    public User findById(UUID id) {
        return User.unknown(id);   // safe default
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

### Actuator integration

Resilience4j exposes circuit breaker state via Actuator:

```properties
management.endpoints.web.exposure.include=health,circuitbreakers,retries
management.endpoint.health.show-details=always
```

---

## Security (OAuth2)

The security model is built on OAuth2 / OIDC. `auth/` is the Authorization Server. `gateway/` is the OAuth2 Client and Resource Server. Backend services (`user/`, `note/`, `user-note/`) are Resource Servers.

### Token flow

```
Browser / mobile app
        │  1. Authorization Code request
        ▼
    gateway/  ────────────────────▶  auth/
    (OAuth2 Client)                  (Authorization Server)
        │  2. JWT access token
        │
        │  3. forward request + token (TokenRelay filter)
        ▼
    user/ · note/ · user-note/
    (OAuth2 Resource Servers — validate JWT)
```

### OAuth2 Authorization Server (`auth/`)

`auth/` issues JWT access tokens, refresh tokens, and OIDC ID tokens using Spring Authorization Server.

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

Security config in `auth/application/`:

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

Token endpoints exposed by the Authorization Server:

| Endpoint                   | Description                    |
|----------------------------|--------------------------------|
| `/oauth2/authorize`        | authorization code request     |
| `/oauth2/token`            | token issuance                 |
| `/oauth2/revoke`           | token revocation               |
| `/oauth2/introspect`       | token introspection            |
| `/.well-known/openid-configuration` | OIDC discovery        |

### OAuth2 Client (`gateway/`)

`gateway/` initiates the OAuth2 authorization code flow on behalf of the browser and forwards the obtained JWT to backend services via the `TokenRelay` filter.

`gateway/application/application.properties`:

```properties
spring.security.oauth2.client.registration.gateway.client-id=gateway
spring.security.oauth2.client.registration.gateway.client-secret=secret
spring.security.oauth2.client.registration.gateway.authorization-grant-type=authorization_code
spring.security.oauth2.client.registration.gateway.scope=openid,read
spring.security.oauth2.client.provider.gateway.issuer-uri=http://localhost:8081

spring.cloud.gateway.default-filters[0]=TokenRelay
```

Security config in `gateway/application/`:

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

### OAuth2 Resource Server (backend services)

`user/`, `note/`, `user-note/` validate the JWT forwarded by the gateway.

Add to each service's `application/build.gradle`:

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-security-oauth2-resource-server'
    // ...existing deps
}
```

`application.properties` per service:

```properties
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8081
```

Security config per service:

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

The JWT is validated against the public key fetched from `auth/`'s JWKS endpoint (`/.well-known/jwks.json`) — no shared secret needed.

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

Applied to every `application/` module. Provides Spring Boot, service discovery, observability, and testing out of the box.

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
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
    implementation 'io.micrometer:micrometer-registry-prometheus'
    implementation 'io.micrometer:micrometer-tracing-bridge-otel'
    implementation 'io.opentelemetry:opentelemetry-exporter-otlp'
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
| `spring-kafka-adapter-conventions`       | `spring-boot-starter-kafka`                                      |
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

### Spring Boot Actuator

Add `spring-boot-starter-actuator` to `spring-boot-application-conventions.gradle`. Every service then exposes:

| Endpoint              | Purpose                                     |
|-----------------------|---------------------------------------------|
| `/actuator/health`    | liveness / readiness probes (Kubernetes)    |
| `/actuator/info`      | service metadata                            |
| `/actuator/metrics`   | application metrics                         |
| `/actuator/prometheus`| Prometheus-format metrics scrape endpoint   |
| `/actuator/loggers`   | runtime log-level management                |

Recommended `application.properties`:

```properties
management.endpoints.web.exposure.include=health,info,metrics,prometheus,loggers
management.endpoint.health.show-details=always
```

### Prometheus

Prometheus scrapes metrics from every service's `/actuator/prometheus` endpoint.
Requires Micrometer Prometheus registry — add to `spring-boot-application-conventions.gradle`:

```groovy
dependencies {
    implementation 'io.micrometer:micrometer-registry-prometheus'
    // ...existing deps
}
```

`prometheus.yml` scrape config (one entry per service):

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
```

Grafana connects to Prometheus as a data source and visualizes dashboards.

### OpenTelemetry

OpenTelemetry provides distributed tracing across all services via Micrometer Tracing.
Add to `spring-boot-application-conventions.gradle`:

```groovy
dependencies {
    implementation 'io.micrometer:micrometer-tracing-bridge-otel'
    implementation 'io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter'
    // export to OTLP collector (Jaeger / Tempo / Zipkin):
    implementation 'io.opentelemetry:opentelemetry-exporter-otlp'
    // ...existing deps
}
```

`application.properties` per service:

```properties
management.tracing.sampling.probability=1.0
otel.exporter.otlp.endpoint=http://localhost:4318
```

Trace data flows: service → OpenTelemetry Collector → Jaeger (or Grafana Tempo, or Zipkin).
Every incoming HTTP request, outgoing Feign call, and database query is automatically traced.

### Monitoring stack

| Tool                 | Purpose                                         |
|----------------------|-------------------------------------------------|
| Prometheus           | metrics collection (scrapes `/actuator/prometheus`) |
| Grafana              | metrics dashboards and alerts                   |
| OpenTelemetry        | distributed tracing instrumentation             |
| Jaeger / Grafana Tempo | trace storage and visualization               |
| Elastic Stack (ELK)  | centralized log aggregation                     |

---

## Deployment

### Docker

Each service is packaged as a Docker image using Spring Boot's built-in Buildpacks support.
`bootBuildImage` is available automatically because `org.springframework.boot` is applied by `spring-boot-application-conventions`.

Build image for a service:

```
./gradlew :application:bootBuildImage --imageName=notes-spring/auth:latest
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

On Kubernetes, Eureka is replaced by native K8s service discovery.
Each service is deployed as a `Deployment` + `Service`. `ConfigMap` replaces the Config Server.

Disable Eureka in `application.properties` for the K8s profile:

```properties
eureka.client.enabled=false
spring.cloud.config.enabled=false
```

Liveness and readiness probes use Actuator endpoints:

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

Enable liveness/readiness in `application.properties`:

```properties
management.endpoint.health.probes.enabled=true
```

### Helm

Each service has a `helm/` directory with a Helm chart. A root `Chart.yaml` with subcharts assembles the full stack:

```
notes-spring/
├── helm/
│   ├── Chart.yaml        # umbrella chart
│   ├── values.yaml       # shared defaults
│   └── charts/           # per-service subchart tarballs
│
├── auth/helm/
├── user/helm/
├── note/helm/
└── user-note/helm/
```

Deploy to a cluster:

```
helm upgrade --install notes-spring ./helm --namespace notes-spring --create-namespace
```

---

## Secrets Management

### HashiCorp Vault

Vault stores secrets (database passwords, API keys, JWT signing keys) and delivers them to services at startup via Spring Cloud Vault.

Add to `spring-boot-application-conventions.gradle`:

```groovy
dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-vault-config'
    // ...existing deps
}
```

`application.properties` per service:

```properties
spring.cloud.vault.uri=http://localhost:8200
spring.cloud.vault.authentication=token
spring.cloud.vault.token=${VAULT_TOKEN}
spring.cloud.vault.kv.enabled=true
spring.cloud.vault.kv.backend=secret
spring.cloud.vault.kv.default-context=auth   # change per service
spring.config.import=vault://
```

Secrets stored at `secret/auth` in Vault are automatically injected as Spring properties.
In production, replace token authentication with Kubernetes auth or AWS IAM.

---

## Testing

### Testcontainers

Testcontainers starts real infrastructure (database, Redis, Kafka, etc.) inside Docker during tests.
Use it in `data-jpa/`, `cache/`, `rabbitmq/`, `kafka/` adapter tests to avoid mocking persistence.

Add to the relevant adapter convention plugin (e.g., `spring-data-jpa-adapter-conventions.gradle`):

```groovy
dependencies {
    testImplementation 'org.springframework.boot:spring-boot-testcontainers'
    testImplementation 'org.testcontainers:postgresql'   // or :kafka, :redis, etc.
    // ...existing deps
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
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", postgres::getJdbcUrl);
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);
    }
}
```

---

## Production Platform

The target runtime platform is AWS. Kubernetes handles container orchestration and service discovery (Eureka disabled). Helm manages parameterized deployments across environments. See [Deployment](#deployment) for full configuration.
