# CLAUDE.md — notes-spring

> Единственный источник истины для этого проекта. Читается автоматически в начале каждой сессии.  
> Последнее обновление: 2026-06-19T11:32Z

---

## Правила

- **Язык** — общение на русском; код, идентификаторы, комментарии — на английском
- **Файлы** — не изменять и не создавать без явного «измени X в файле Y»
- **Коммиты** — не коммитить и не пушить без явного запроса
- **CI** — не трогать `.github/workflows/` без явного запроса
- **Приоритет** — всегда называть текущую задачу (раздел Задачи); отложенное не предлагать
- **Решение проблем** — сначала: актуальна ли проблема? кто выигрывает? не упускаем ли что-то?  
  затем: варианты с плюсами/минусами + склонение → ждать выбора → обосновать  
  не предлагать реализационные задачи, пока открыты архитектурные вопросы
- **Этот файл** — приоритет над всем; обновлять при изменении проекта или принципов;  
  всё, что пользователь просит запомнить — отражать здесь
- **Перед коммитом** — обновить дату: `date -u +"%Y-%m-%dT%H:%MZ"`

---

## Задачи

```
ГОТОВО      user/ · note/ · user-note/  — domain · service · webmvc · data-jpa
ТЕКУЩИЙ     auth/  — Spring Authorization Server + AuthUser + OIDC
НЕ СОЗДАНО  bff/ · thymeleaf/ · auth/webmvc/ · auth/data-jpa/ · user-note/feign/ · crud/
```

### Чистка перед `auth/`

1. Убрать `password` из `User` — `AuthUser ≠ User` уже принято; `user/` — профиль, не IdP
2. Доменные исключения — заменить `NoSuchElementException` как сигнал 404
3. `@ControllerAdvice` + `ProblemDetail` (RFC 9457) — убрать дублирующий `@ExceptionHandler`
4. `UserNoteService.update()` → `existsByUserIdAndNoteId` — лишний read

### `auth/` ← **ТЕКУЩИЙ ПРИОРИТЕТ**

1. `auth/domain/` — `AuthUser` record + `AuthUserUseCase` + `AuthUserOutputPort`
2. `auth/data-jpa/` — JPA adapter + Flyway schema
3. Spring Authorization Server — OIDC endpoint + JWT issuer

### После `auth/`

1. Resource Server в одном бизнес-сервисе — smoke test: auth/ выдаёт токен, сервис принимает
2. Resource Server во всех бизнес-сервисах; убрать `userId` из `UserNoteRequest` (берётся из JWT `sub`)
3. `bff/` — OAuth2 Client + Spring Session + Token Exchange
4. `thymeleaf/` — server-rendered BFF
5. Banking Phase 2 — MFA, token rotation, audit log
6. `user-note/feign/` — OpenFeign
7. `crud/` — shared library
8. Широкий стек — после выбора стратегии активации; цель: показать, что домен не зависит от протокола и хранилища

---

## Открытые решения

> Откладываются при взаимозависимости или преждевременности. Не реализовывать без явного решения.

### После Resource Server

- **Регистрация** — координация `auth/` ↔ `user/`; решить до реализации:
  - **Lazy** — `user/` создаёт профиль при первом запросе; нет email при регистрации; нет coupling
  - **Sync** — `auth/` → `user/` через RestClient; нарушает direction of dependencies (infra → business)
  - **Events** — Kafka/RabbitMQ; единственный вариант без нарушения SoC/SRP; требует брокера
- **Mapping** — где маппить между слоями; ручной / MapStruct; отдельные DTO/VO на каждом слое
- **PATCH** — только в `service/` (полный объект → `replace`) / в output port / не поддерживать
- **Возврат из service/** — доменный объект / `void` для мутирующих; CQS / CQRS на уровне input port

### Отложено (фундаментальное)

- **NoteVisibility** — в `note/domain/` или `user-note/domain/` (требует ясности по ACL)
- **Reactive/sync impedance** — `Optional<T>` несовместим с реактивными адаптерами (`.block()` = deadlock):
  - **Параллельные порты** _(склонение)_ — `NoteRepository` + `ReactiveNoteRepository` в `domain/`
  - **Sync обёртка** — `Mono.fromCallable().subscribeOn(boundedElastic())` — не настоящий reactive
  - **Отдельные реактивные сервисы** — дублирование домена
- **Стратегия активации адаптеров**:
  - `@Profile("jpa")` — просто, грубо
  - Отдельные `application-jpa/` · `application-r2dbc/` — чисто; дублирует composition root
- **Название output adapter при нескольких реализациях** — `NoteOutputAdapter` / `NoteJpaOutputAdapter`

---

## Стек

> Spring Boot 4 monorepo — banking-grade

| Инструмент                   | Версия   |
|------------------------------|----------|
| Java                         | 17       |
| Gradle                       | 9.6.0    |
| Spring Boot                  | 4.1.0    |
| Spring Cloud                 | 2025.1.2 |
| Spring Dependency Management | 1.1.7    |

> **Проект сейчас:** Boot 4.0.6 · Cloud 2025.1.1 · Gradle 9.5.1 —  
> обновить `buildSrc/build.gradle` и `gradle/wrapper/gradle-wrapper.properties`.

---

## Сервисы

```
EDGE          gateway/     Spring Cloud Gateway — stateless, routing + rate limiting
PRESENTATION  bff/         OAuth2 Client + Spring Session + Token Exchange (SPA)
              thymeleaf/   Thymeleaf + OAuth2 Client — self-contained BFF
IDENTITY      auth/        Spring Authorization Server — OIDC-compliant, JWT issuer
BUSINESS      user/        Resource Server  (User: id, username, email)
              note/        Resource Server  (Note: id, content)
              user-note/   Resource Server  (UserNote: userId, noteId, role)
INFRA         config/      Spring Cloud Config Server
              registry/    Eureka Server
EXTERNAL      Redis        Spring Session backing (bff/ + thymeleaf/ при масштабировании)
              PostgreSQL×N по одной БД на: auth, user, note, user-note
              Kafka/MQ     только при событийной регистрации (решение не принято)
```

> **MVP** — та же архитектура; H2 вместо PostgreSQL, `MapSessionRepository` вместо Redis.  
> Backing services подключаются при реальной потребности, не как архитектурный шаг.

---

## Архитектура

**Hexagonal Architecture (Ports & Adapters)** — главный принцип; все решения проверяются на соответствие:

```
application/  Spring Boot app — composition root; знает все модули
domain/       entities + port interfaces (input + output) — чистая Java, без Spring
service/      use case implementations (@Service, @Transactional) — зависит только от domain/
webmvc/       driving adapter   →  domain/
data-jpa/     driven adapter    →  domain/
feign/        driven outbound   →  domain/  (только user-note/)
```

**Dependency direction** — `application/` знает всё; адаптеры знают только `domain/`; `domain/` — ничего снаружи  
**Database per service** — cross-service JOIN запрещён  
**Stateless** — `gateway/` · `config/` · `registry/` · `user/` · `note/` · `user-note/`  
**Stateful** — `bff/` · `thymeleaf/` → Spring Session; `auth/` → OAuth2Authorization в PostgreSQL

### Целевая модель портов

| Модель                     | Driving                               | Driven                                 |
|----------------------------|---------------------------------------|----------------------------------------|
| Sync + Virtual Threads     | `webmvc/`, `shell/`, `batch/`         | `data-jpa/`, `data-jdbc/`, `jooq/`     |
| Reactive (Project Reactor) | `webflux/`, `rsocket/`                | `data-r2dbc/`, `data-mongodb-rx/`      |
| Async messaging            | `kafka/` consumer, `amqp/` consumer   | `kafka/` producer, `amqp/` producer    |

WebFlux + Virtual Threads несовместимы → один `application/` собирает одну модель.

### Семантика output port

Output port говорит на языке домена, не хранилища. Коллекционная семантика (Evans):

```java
boolean existsById(UUID id);       // containsKey
Optional<Note> findById(UUID id);  // get
List<Note> findAll();              // values
void add(Note note);               // put
void replace(Note note);           // replace (full)
void remove(UUID id);              // remove
```

`void` для мутирующих — UUID генерируется в `service/` до вызова порта.  
`replace` — полная замена; PATCH решается в `webmvc/` + `service/`.

### Принятые решения

- **Gateway ≠ BFF** · **BFF — один на UX** (Sam Newman)
- **Token Exchange (RFC 8693)** — BFF обменивает access token на internal JWT с `aud` микросервиса
- **Spring Authorization Server — постоянный IdP**; Keycloak/Auth0 только после детальной оценки
- **OpenFeign** — `spring-cloud-starter-openfeign` для `user-note/feign/`
- **Input port — интерфейс в `domain/`** — `webmvc/` зависит от интерфейса, не от `service/`
- **Domain objects — Java records** — `withXxx()` для изменённой копии; JPA entities — обычные классы
- **`existsById` в output port** — валидный паттерн; не заменять на `findById`
- **`*OutputPort`** / **`*OutputAdapter`** — без фреймворк-/технологических коннотаций
- **`ResponseEntity<T>` везде** — статусы явно через `HttpStatus`
- **`AuthUser` (`auth/`) ≠ `User` (`user/`)**

---

## Безопасность

> `identity ≠ profile ≠ business ≠ permissions` — смешивание нарушает SoC и усложняет смену IdP

**Zero Trust** — каждый слой валидирует JWT самостоятельно  
**JWT claims** — только стандартные: `sub` · `iss` · `aud` · `exp` · `jti` · `acr` · `amr` · `scope`  
**Браузер не видит JWT** — только HttpOnly session cookie в `bff/` · `thymeleaf/` (защита от XSS)

| Слой            | Модуль                | Ответственность                              |
|-----------------|-----------------------|----------------------------------------------|
| Edge            | `gateway/`            | Routing, rate limiting, TLS                  |
| BFF             | `bff/` · `thymeleaf/` | OAuth2 login flow, session, Token Exchange   |
| IdP             | `auth/`               | JWT issuing, credentials, OIDC               |
| Resource Server | `*/webmvc/`           | JWT validation per request                   |
| ACL             | `user-note/`          | `UserNote { userId, noteId, role }`          |

**UserNoteRole:** `OWNER` · `EDITOR` · `COMMENTER` _(не MVP)_ · `VIEWER`  
**NoteVisibility:** `RESTRICTED` · `ANYONE_WITH_LINK` + role · `PUBLIC` + role

### Клиенты

| Клиент               | Grant Type          | Хранит токен         | BFF          |
|----------------------|---------------------|----------------------|--------------|
| Browser / Thymeleaf  | Authorization Code  | Серверная сессия     | Сам себе BFF |
| Browser / SPA        | Authorization Code  | Серверная сессия BFF | `bff/`       |
| Mobile (Android/iOS) | AuthCode + PKCE     | Keychain / Keystore  | Нет          |
| B2B / CLI            | Client Credentials  | Не хранит            | Нет          |

### Аутентификация

**Все методы → единый JWT** с `acr` (1 = single factor, 2 = MFA) и `amr` (pwd / google / passkey / totp)  
**Social Login** — `auth/` сам OAuth2 Client к Google/GitHub; их токен никогда не покидает `auth/`  
**MFA** — `@EnableMultiFactorAuthentication` (Spring Security 7) + `FactorGrantedAuthority`  
**Step-up auth** — `/oauth2/authorize?acr_values=2` → MFA challenge → новый токен с `acr=2`

### Токены

**Flow:** User → `auth/` → access_token → BFF Token Exchange (RFC 8693) → internal JWT (`aud`=сервис) → Microservice  
**access_token** 15 мин · **refresh_token** 30–90 дней; хранится в сессии BFF или Keychain/Keystore; в микросервисы не отправляется  
**Rotation** — каждый refresh → новый refresh_token; повторное использование старого → revoke вся семья → re-login  
**JTI Blocklist** — Redis `SET jti:{jti} "revoked" EX ttl`; ~0.5 ms на запрос; logout < 1 с (banking-grade)  
**Back-channel logout** — `auth/` → POST `bff/logout/connect/back-channel`; при горизонтальном масштабировании BFF требует Redis Session

### Banking-grade фазы

| Фаза         | Содержание                                                                                  |
|--------------|---------------------------------------------------------------------------------------------|
| 1 — основа   | PKCE · `aud`/`scope`/`jti` claims · refresh rotation · rate limiting · TLS 1.3 · stateless |
| 2 — MFA      | TOTP/Passkey · Social Login · JTI Blocklist · Step-up auth · device tracking · audit log   |
| 3 — максимум | DPoP · mTLS · Certificate pinning · App attestation                                        |

---

## Принципы

- **Hexagonal Architecture** — главный принцип; все решения проверяются на соответствие
- **SoC / SRP** — на всех уровнях; **видимость** — `default` или минимально необходимая
- **No partial abstractions** — полное устранение или явное дублирование
- **Twelve-Factor:** III Config · VI Stateless · IX Graceful shutdown · X Testcontainers · XI stdout · XII Flyway
- **Остальные** — SOLID · KISS · DRY · SSOT · Law of Demeter · Fail Fast

---

## Техническая база

### Gradle

**`buildSrc`** + convention plugins (Groovy DSL) — единственный механизм; flat, без вложенности  
**Нет:** root `build.gradle` · `buildSrc/settings.gradle` · `libs.versions.toml`  
**Порядок блоков:** `plugins` → `java` → `repositories` → [`ext`] → `dependencies` → `dependencyManagement` → `test`  
**Порядок зависимостей:** `domain` → `service` → `webmvc` → `data-jpa`  
**`settings.gradle`:** `gateway` → `config` → `registry` → `auth` → `user` → `note` → `user-note`; внутри: `application` → `domain` → `service` → `webmvc` → `data-jpa`  
**Convention plugins:** ✅ `application` · `domain` · `webmvc` · `data-jpa` · `h2-database` | ❌ планируется ≈16  
**`domain`-plugin** — только `java`; без Spring BOM; JSpecify и JUnit с явными версиями

### Spring Boot 4

- `starter-web` → `starter-webmvc`
- `starter-test` — JUnit/Mockito/AssertJ; слайсы (`@WebMvcTest`, `@DataJpaTest` и др.) выведены в отдельные `*-test` стартеры (в Boot 3 входили в `starter-test`)
- Jackson 3: `com.fasterxml.jackson` → `tools.jackson`
- RestTemplate deprecated → `RestClient`
- Flyway + PostgreSQL: нужен `runtimeOnly 'org.flywaydb:flyway-database-postgresql'`
- Обработка ошибок: `ResponseEntityExceptionHandler` + `ProblemDetail` (RFC 9457)
- Lombok: `compileOnly` + `annotationProcessor`; `@Data`/`@EqualsAndHashCode` запрещены на entities — нарушают Hibernate lifecycle; `equals()`/`hashCode()` по ID вручную: `hashCode() { return getClass().hashCode(); }`

### Spring Security

**Цепочка:** `SecurityFilterChain` → `AuthenticationManager` → `ProviderManager` → `DaoAuthenticationProvider` → `UserDetailsService` + `PasswordEncoder`  
**Resource Server** — `JwtDecoder` + `JwtAuthenticationConverter` → `GrantedAuthority`; минимум: property `spring.security.oauth2.resourceserver.jwt.issuer-uri`  
**Auth Server** — `OAuth2AuthorizationServerConfigurer` · `RegisteredClientRepository`; 4 обязательных бина: `SecurityFilterChain` × 2, `UserDetailsService`, `JWKSource`

Стартеры — см. раздел **Верифицированные координаты** ниже.

### Null Safety и стиль

**`@NullMarked`** (JSpecify) через `package-info.java` — non-null по умолчанию; не иерархично: каждый пакет требует своего `package-info.java`; `org.springframework.lang` deprecated  
**`@Nullable`** — `@Target(TYPE_USE)`: `private @Nullable String field`; массивы: `Object @Nullable []` (nullable ссылка), `@Nullable Object[]` (nullable элементы)  
**Spring Cloud 2026.0** — ещё не null-safe (registry/, config/, gateway/); при нужде: `@NullUnmarked`  
**Импорты** — `com.example.*` + `org.*` / `jakarta.*`, затем `java.*`; wildcard при 3+  
**Имена полей** — camelCase от типа: `noteOutputPort`, `noteJpaRepository`; не `repository`, не `port`  
**Промежуточная переменная** — перед `return` всегда извлекать результат (`response`, `notes`); не inline в `.body()`  
**Пустое тело** — одна пустая строка · **EOF** — один `\n`

---

## Верифицированные координаты (Boot 4.x / Cloud 2025.1.x)

> Источник правды — `start.spring.io`. Maven Central не является авторитетом для Boot 4 стартеров.

### Spring Boot 4 — `implementation` (`org.springframework.boot`)

```
spring-boot-h2console
spring-boot-starter-actuator
spring-boot-starter-amqp
spring-boot-starter-batch
spring-boot-starter-batch-jdbc
spring-boot-starter-cache
spring-boot-starter-data-jdbc
spring-boot-starter-data-jpa
spring-boot-starter-data-mongodb
spring-boot-starter-data-mongodb-reactive
spring-boot-starter-data-r2dbc
spring-boot-starter-data-redis
spring-boot-starter-data-redis-reactive
spring-boot-starter-data-rest
spring-boot-starter-elasticsearch
spring-boot-starter-flyway
spring-boot-starter-graphql
spring-boot-starter-hateoas
spring-boot-starter-integration
spring-boot-starter-jdbc
spring-boot-starter-kafka
spring-boot-starter-liquibase
spring-boot-starter-mail
spring-boot-starter-opentelemetry
spring-boot-starter-pulsar
spring-boot-starter-quartz
spring-boot-starter-r2dbc
spring-boot-starter-restclient
spring-boot-starter-rsocket
spring-boot-starter-security
spring-boot-starter-security-oauth2-authorization-server
spring-boot-starter-security-oauth2-client
spring-boot-starter-security-oauth2-resource-server
spring-boot-starter-session-data-redis
spring-boot-starter-session-jdbc
spring-boot-starter-thymeleaf
spring-boot-starter-validation
spring-boot-starter-webclient
spring-boot-starter-webflux
spring-boot-starter-webmvc
spring-boot-starter-websocket
```

### Spring Boot 4 — `testImplementation`

```
spring-boot-starter-test
spring-boot-starter-data-jpa-test
spring-boot-starter-security-test
spring-boot-starter-security-oauth2-authorization-server-test
spring-boot-starter-security-oauth2-client-test
spring-boot-starter-security-oauth2-resource-server-test
spring-boot-starter-webmvc-test
spring-boot-testcontainers
```

### Spring Cloud 2025.1.x — `org.springframework.cloud`

```
spring-cloud-config-server
spring-cloud-starter-config
spring-cloud-starter-gateway-server-webmvc
spring-cloud-starter-gateway-server-webflux
spring-cloud-starter-loadbalancer
spring-cloud-starter-netflix-eureka-client
spring-cloud-starter-netflix-eureka-server
spring-cloud-starter-openfeign
spring-cloud-stream
spring-cloud-stream-binder-kafka
spring-cloud-stream-binder-rabbit
```

### `runtimeOnly`

```
# Database drivers
org.postgresql:postgresql
org.postgresql:r2dbc-postgresql
com.h2database:h2

# Flyway — ОБЯЗАТЕЛЬНО при использовании spring-boot-starter-flyway с PostgreSQL
org.flywaydb:flyway-database-postgresql
org.flywaydb:flyway-mysql
```

### Testcontainers — `testImplementation` (версия управляется Spring Boot BOM)

```
org.testcontainers:testcontainers-junit-jupiter
org.testcontainers:postgresql
org.testcontainers:kafka
org.testcontainers:mongodb
# Redis: нет официального TC модуля → GenericContainer("redis:latest")
```

### `testRuntimeOnly`

```
org.junit.platform:junit-platform-launcher
```

### `developmentOnly`

```
org.springframework.boot:spring-boot-devtools
org.springframework.boot:spring-boot-docker-compose
```

### Прочее

```
org.thymeleaf.extras:thymeleaf-extras-springsecurity6   ← имя "6", даже с Security 7
org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.2 ← явная версия (third-party)
```
