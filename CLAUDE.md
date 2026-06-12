# CLAUDE.md — notes-spring

> Читается автоматически в начале каждой сессии.
> Детали, история, справочники — в `~/.claude/projects/…/memory/`.
> Последнее обновление: 2026-06-12 (2)

---

## Правила

- **Язык** — общение на русском; код, идентификаторы, комментарии — на английском
- **Файлы** — не изменять и не создавать без явного «измени X в файле Y»
- **Коммиты** — не коммитить и не пушить без явного запроса
- **CI** — не трогать `.github/workflows/` без явного запроса
- **Подход** — читать память перед ответом; взвешивать варианты, проверять соответствие принципам
- **Решение проблем** — формулировать проблему → предлагать варианты с плюсами/минусами + склонение → ждать выбора → обосновать принятое решение; не предлагать реализационные задачи пока открыты архитектурные вопросы
- **CLAUDE.md** — в приоритете над памятью; обновлять при каждом изменении проекта или принципов; всё, что пользователь просит запомнить — отражать здесь
- **Перед коммитом** — рефакторинг CLAUDE.md → синхронизация памяти → обновление даты
- **≤ 400 строк** — при превышении удалять второстепенное (детали реализации в первую очередь)

---

## Стек

> Spring Boot 4 monorepo — banking-grade

| Инструмент                   | Версия               |
|------------------------------|----------------------|
| Java                         | 17 (`.java-version`) |
| Gradle                       | 9.5.1                |
| Spring Boot                  | 4.0.6                |
| Spring Cloud                 | 2025.1.1             |
| Spring Dependency Management | 1.1.7                |

---

## Сервисы

```
EDGE          gateway/     Spring Cloud Gateway — stateless, routing + rate limiting
PRESENTATION  bff/         OAuth2 Client + Spring Session + Token Exchange (SPA)
              thymeleaf/   Thymeleaf + OAuth2 Client — self-contained BFF
IDENTITY      auth/        Spring Authorization Server — OIDC-compliant, JWT issuer
BUSINESS      user/        Resource Server  (User: id, username, email, password)
              note/        Resource Server  (Note: id, content)
              user-note/   Resource Server  (UserNote: userId, noteId, role)
INFRA         config/      Spring Cloud Config Server
              registry/    Eureka Server
EXTERNAL      Redis        Spring Session backing (bff/ + thymeleaf/ при масштабировании)
              PostgreSQL×N по одной на: auth, user, note, user-note
              Kafka/MQ     только при событийной регистрации (решение не принято)
```

> **Ещё не создано:** `bff/` · `thymeleaf/` · `auth/webmvc/` · `auth/data-jpa/` · `user-note/feign/` · `crud/`  
> **Не реализовано:** `service/` во всех бизнес-сервисах · `user/webmvc` · `user/data-jpa` · `user-note/webmvc` · `user-note/data-jpa`

---

## Открытые архитектурные вопросы

Решение этих вопросов необходимо до реализации зависящих от них частей.
Откладываются при взаимозависимости или преждевременности — решаются когда проект доходит до реализации зависящих частей.

### Блокеры

**Семантика портов** — дизайн output port интерфейсов; касается всех бизнес-сервисов: ← **ТЕКУЩИЙ ПРИОРИТЕТ; блокирует `service/`**
- `save` (upsert) vs раздельные `create` / `update`
- `update` (partial / PATCH) vs `replace` (full / PUT)
- `void delete` vs `T delete` — возвращать ли удалённый объект

**Регистрация** — не выбрана стратегия координации между `auth/` и `user/`: ← **отложено: сложная и неоднозначная; ни один вариант не очевиден**
- Lazy: `user/` создаёт профиль при первом запросе; нет email до явного обновления
- Sync: `auth/` → `user/` через RestClient; нарушает direction of dependencies
- Events: Kafka/RabbitMQ; архитектурно чисто; требует брокера

### Ожидают решения

**NoteVisibility** — где хранить: в `note/domain/` или `user-note/domain/`; `note/domain/` создан без неё ← **отложено: требует ясности по ACL-дизайну**

**Reactive/sync impedance** — синхронный порт (`Optional<Note> findById(UUID)`) несовместим с реактивными адаптерами (`.block()` = deadlock): ← **отложено: фундаментальное решение, влияет на `domain/`**
- Вариант 1 — параллельные порты в `domain/` (склонение): `NoteRepository` (sync) + `ReactiveNoteRepository` (Mono/Flux); `domain/` принимает Reactor
- Вариант 2 — sync `domain/`, обёртка в адаптере: `Mono.fromCallable().subscribeOn(boundedElastic())`; не настоящий reactive
- Вариант 3 — отдельные реактивные сервисы: `note/` sync, реактивный — новый сервис с нуля; дублирование домена

**Стратегия активации адаптеров** — как выбрать активный адаптер в runtime: ← **отложено: зависит от Reactive/sync impedance**
- Spring Profiles (`@Profile("jpa")`) — просто, но грубо
- Отдельные `application-jpa/` / `application-r2dbc/` модули — чисто, но дублирует composition root

### Порядок реализации

1. Семантика портов ← **ТЕКУЩИЙ ПРИОРИТЕТ**
2. `service/` + `domain/` input ports во всех бизнес-сервисах
3. Регистрация — lazy / sync / events
4. `auth/` — Authorization Server + AuthUser + OIDC
5. Resource Server в `user/` · `note/` · `user-note/`
6. `bff/` — OAuth2 Client + Spring Session + Token Exchange
7. `thymeleaf/` — server-rendered BFF
8. Banking Phase 2 — MFA, token rotation, audit log
9. `user-note/feign/` — OpenFeign
10. `crud/` — shared library
11. Широкий стек — после выбора стратегии активации

---

## Архитектура

**Hexagonal Architecture (Ports & Adapters)** — структура каждого бизнес-сервиса:

```
application/  Spring Boot app — composition root
domain/       entities + port interfaces (input + output) — чистая Java, без Spring
service/      use case implementations (@Service, @Transactional) — зависит только от domain/
webmvc/       driving HTTP adapter  →  domain/
data-jpa/     driven persistence adapter  →  domain/
feign/        driven outbound HTTP adapter  →  domain/  (только user-note/)
```

**Dependency direction** — `application/` знает всё; адаптеры знают только `domain/`; `domain/` не знает ничего снаружи  
**Database per service** — каждый сервис хранит данные в своей БД; cross-service JOIN запрещён  
**Stateless** — `gateway/` · `config/` · `registry/` · `user/` · `note/` · `user-note/`  
**Stateful** — `bff/` · `thymeleaf/` → Spring Session; `auth/` → OAuth2Authorization в PostgreSQL

### Целевая модель портов

Цель — один `domain/` работает с адаптерами принципиально разных execution-моделей:

| Модель | Driving | Driven |
|--------|---------|--------|
| Sync + Virtual Threads | `webmvc/`, `shell/`, `batch/` | `data-jpa/`, `data-jdbc/`, `jooq/` |
| Reactive (Project Reactor) | `webflux/`, `rsocket/` | `data-r2dbc/`, `data-mongodb-rx/`, `data-redis-rx/` |
| Async messaging | `kafka/` consumer, `amqp/` consumer | `kafka/` producer, `amqp/` producer |

WebFlux + Virtual Threads несовместимы → один `application/` собирает одну модель.

### Типы адаптеров

**Driving (primary)** — вызывают domain: HTTP, GraphQL, gRPC, CLI, Kafka consumer, Batch  
**Driven (secondary)** — domain вызывает их: JPA, MongoDB, Redis, Kafka producer, Mail, Search

**Driving:**

| Модуль       | Стартер                                 |
|--------------|-----------------------------------------|
| `webmvc/`    | `spring-boot-starter-webmvc`            |
| `webflux/`   | `spring-boot-starter-webflux`           |
| `graphql/`   | `spring-boot-starter-graphql`           |
| `grpc/`      | `spring-grpc-server`                    |
| `websocket/` | `spring-boot-starter-websocket`         |
| `rsocket/`   | `spring-boot-starter-rsocket`           |
| `shell/`     | `spring-shell-starter`                  |
| `batch/`     | `spring-boot-starter-batch`             |

**Driven:**

| Модуль               | Стартер                                         |
|----------------------|-------------------------------------------------|
| `data-jpa/`          | `spring-boot-starter-data-jpa`                  |
| `data-jdbc/`         | `spring-boot-starter-data-jdbc`                 |
| `jooq/`              | `spring-boot-starter-jooq`                      |
| `data-r2dbc/`        | `spring-boot-starter-data-r2dbc`                |
| `data-mongodb/`      | `spring-boot-starter-data-mongodb`              |
| `data-mongodb-rx/`   | `spring-boot-starter-data-mongodb-reactive`     |
| `data-redis/`        | `spring-boot-starter-data-redis`                |
| `data-redis-rx/`     | `spring-boot-starter-data-redis-reactive`       |
| `data-cassandra/`    | `spring-boot-starter-data-cassandra`            |
| `data-cassandra-rx/` | `spring-boot-starter-data-cassandra-reactive`   |
| `data-neo4j/`        | `spring-boot-starter-data-neo4j`                |
| `elasticsearch/`     | `spring-boot-starter-data-elasticsearch`        |
| `cache/`             | `spring-boot-starter-cache` (+ redis/hazelcast) |
| `http/`              | `spring-boot-starter-restclient` (outbound)     |
| `feign/`             | `spring-cloud-starter-openfeign` (outbound)     |
| `mail/`              | `spring-boot-starter-mail`                      |

**Messaging:**

| Модуль    | Стартер                      |
|-----------|------------------------------|
| `kafka/`  | `spring-boot-starter-kafka`  |
| `amqp/`   | `spring-boot-starter-amqp`   |
| `pulsar/` | `spring-boot-starter-pulsar` |

### Принятые решения

- **Gateway ≠ BFF** — `gateway/` stateless edge; `bff/` и `thymeleaf/` — отдельные stateful-сервисы
- **BFF — один на UX** (Sam Newman, Phil Calçado) — один BFF на тип устройства, не общий
- **Thymeleaf = self-contained BFF** — ведёт OAuth2 login flow самостоятельно, без отдельного `bff/`
- **Token Exchange (RFC 8693), не TokenRelay** — BFF обменивает access token на internal JWT с `aud` конкретного микросервиса
- **Spring Authorization Server — постоянный IdP** — OIDC-совместимый; Keycloak/Auth0 только после детальной оценки
- **OpenFeign для `user-note/feign/`** — `spring-cloud-starter-openfeign`; `@ImportHttpServices` не рассматривается
- **`@GeneratedValue` не используется** — UUID генерируется в `service/` (`UUID.randomUUID()`); JPA-entities без `@GeneratedValue`
- **`service/` модуль** — use case implementations (`@Service`, `@Transactional`); зависит только от `domain/`; `application/` остаётся чистым composition root
- **Domain objects — Java records** — `Note`, `User`, `UserNote` и все будущие domain objects — `record`; `withXxx()` для изменённой копии; JPA entities — обычные классы
- **Контроллеры: `ResponseEntity<T>` везде** — все методы возвращают `ResponseEntity`; статусы явно через `HttpStatus`; `Location` не добавляется
- **`existsById` в output port** — валидный паттерн; не заменять на `findById`; для delete-операций эффективнее (`SELECT 1` vs `SELECT *`)
- **Input port — интерфейс в `domain/`** — `NoteUseCase` / `UserUseCase` / `UserNoteUseCase`; `service/` реализует; `webmvc/` зависит от интерфейса, не от `service/`
- **`service/` — после семантики портов** — не реализовывать до принятия решения по семантике output port методов

---

## Безопасность

> `identity ≠ profile ≠ business ≠ permissions`

**Zero Trust** — каждый слой валидирует JWT самостоятельно; Gateway не является точкой доверия  
**JWT claims** — только стандартные: `sub` · `iss` · `aud` · `exp` · `jti` · `acr` · `amr` · `scope`  
**Banking-grade** — фаза 1: основа · фаза 2: MFA + token rotation · фаза 3: DPoP + mTLS

| Слой            | Модуль                | Ответственность                                   |
|-----------------|-----------------------|---------------------------------------------------|
| Edge            | `gateway/`            | Routing, rate limiting, TLS, security headers     |
| BFF             | `bff/` · `thymeleaf/` | OAuth2 login flow, session cookie, Token Exchange |
| IdP             | `auth/`               | JWT issuing, credentials (AuthUser), OIDC         |
| Resource Server | `*/webmvc/`           | JWT validation per request                        |
| ACL             | `user-note/`          | `UserNote { userId, noteId, role }`               |

### UserNoteRole

- `OWNER` — редактирование · комментарии · управление доступом · удаление · передача владения
- `EDITOR` — редактирование, комментарии, управление доступом (если не ограничено Owner)
- `COMMENTER` — комментарии и предложения, без правки контента _(опционально, не MVP)_
- `VIEWER` — только чтение, без комментариев

### NoteVisibility

- `RESTRICTED` — только люди с явным доступом
- `ANYONE_WITH_LINK` + роль (`VIEWER` / `COMMENTER` / `EDITOR`) — любой с ссылкой, без входа
- `PUBLIC` + роль (`VIEWER` / `COMMENTER` / `EDITOR`) — индексируется поиском, без входа

### Клиенты

| Клиент              | Auth flow          | Токен хранит                | BFF                     |
|---------------------|--------------------|-----------------------------|-------------------------|
| Browser (Thymeleaf) | Authorization Code | Spring Session в thymeleaf/ | thymeleaf/ сам себе BFF |
| Browser (React/Vue) | Authorization Code | Spring Session в bff/       | bff/                    |
| Android / iOS       | Auth Code + PKCE   | Keychain / Keystore         | нет                     |
| B2B / CLI           | Client Credentials | не хранит                   | нет                     |

---

## Принципы

**Hexagonal Architecture + SRP** — главный архитектурный принцип проекта; все решения проверяются на соответствие; варианты, нарушающие эти принципы, отмечаются явно  
**SoC / SRP** — на всех уровнях: сервисы, модули, классы, методы  
**Видимость** — `default` (package-private) или минимально необходимая; `public` только для реального публичного API  
**No partial abstractions** — полное устранение или явное дублирование; незавершённые абстракции под запретом

**Twelve-Factor App** — обязателен для каждого сервиса:

| Фактор | Правило                                          |
|--------|--------------------------------------------------|
| III    | Config в env vars / Config Server                |
| VI     | Stateless; Spring Session вместо in-memory state |
| IX     | `server.shutdown=graceful`                       |
| X      | Testcontainers, не H2 вместо PostgreSQL в тестах |
| XI     | Только stdout                                    |
| XII    | Flyway / Liquibase                               |

**Остальные** — SOLID · KISS · YAGNI · DRY · SSOT · Law of Demeter · Fail Fast

---

## Gradle

**`buildSrc`** + convention plugins (Groovy DSL) — единственный механизм; flat, без вложенности  
**Нет:** root `build.gradle` · `buildSrc/settings.gradle` · `libs.versions.toml` · version catalogs  
**Версии плагинов** — только в `buildSrc/build.gradle`  
**Порядок блоков** — `plugins` → `java` → `repositories` → [`ext`] → `dependencies` → `dependencyManagement` → `test`  
**`ext {}`** — только для версий сторонних BOM (Spring Cloud, Modulith); в buildSrc не нужен  
**Порядок зависимостей** — `domain` → `service` → `webmvc` → `data-jpa`

**Порядок в `settings.gradle`:**
- сервисы: `gateway` → `config` → `registry` → `auth` → `user` → `note` → `user-note`
- внутри сервиса: `application` → `domain` → `service` → `webmvc` → `data-jpa`

**Convention plugins:**
- ✅ Есть: `application` · `domain` · `webmvc` · `data-jpa` · `h2-database`
- ❌ Планируется: `service` · `resource-server` · `auth-server` · `oauth2-bff` · `openfeign` · `webflux` · `graphql` · `grpc` · `data-r2dbc` · `data-mongodb` · `data-cassandra` · `kafka` · `amqp` · `pulsar` (≈16 плагинов)

**`domain`-plugin** — только `java`; никакого Spring BOM; JSpecify и JUnit с явными версиями

---

## Spring Boot 4

### Изменения относительно Boot 3

- `spring-boot-starter-web` → `spring-boot-starter-webmvc`
- `org.springframework.session:spring-session-data-redis` → `spring-boot-starter-session-data-redis`
- `org.springframework.kafka:spring-kafka` → `spring-boot-starter-kafka`
- `org.flywaydb:flyway-core` → `spring-boot-starter-flyway` + `org.flywaydb:flyway-database-postgresql`
- `org.testcontainers:postgresql` → `org.testcontainers:testcontainers-postgresql`
- `spring-boot-starter-test` (один на все) → индивидуальные `*-test` стартеры на каждый модуль
- Плагин `org.springframework.boot` не применяет `io.spring.dependency-management` автоматически
- **Jackson 3** — `com.fasterxml.jackson` → `tools.jackson`; `Jackson2ObjectMapperBuilder` → `JsonMapper.Builder`
- **RestTemplate** deprecated → `RestClient` (sync) или `WebClient` (reactive)

### Паттерны

- `start.spring.io` — источник правды для координат (не Maven Central)
- **Flyway + PostgreSQL** — стартер не включает драйвер БД; нужен `runtimeOnly 'org.flywaydb:flyway-database-postgresql'`
- **Обработка ошибок** — `ResponseEntityExceptionHandler` + `ProblemDetail` (RFC 9457)
- **Lombok** — `compileOnly` + `annotationProcessor` (не `implementation`); то же для тестов
- **Lombok + JPA** — `@Data` / `@EqualsAndHashCode` запрещены на entities; безопасны `@Getter` · `@Setter` · `@Builder`

---

## Spring Security

- **SecurityFilterChain** — бин `HttpSecurity`; несколько цепочек с `securityMatcher`; `SecurityContextHolder` = ThreadLocal
- **Authentication** — до верификации: `principal` = String, `credentials` = password; после: `principal` = `UserDetails`, `credentials` очищается
- **AuthenticationManager** → `ProviderManager` → `AuthenticationProvider` → `DaoAuthenticationProvider` → `UserDetailsService` + `PasswordEncoder`
- **UserDetails** — `username` · `password` · `authorities` + флаги; в проекте: `AuthUser` (`auth/`) ≠ `User` (`user/`)
- **AuthorizationManager** — allow/deny; `@PreAuthorize` / `@PostAuthorize`
- **Resource Server** — `JwtDecoder` + `JwtAuthenticationConverter` → `GrantedAuthority`
- **Auth Server** — `OAuth2AuthorizationServerConfigurer` · `RegisteredClientRepository` · `OAuth2AuthorizationService`

---

## Null Safety

**`@NullMarked`** (JSpecify) через `package-info.java` в каждом пакете — non-null по умолчанию  
`org.springframework.lang` — deprecated с Framework 7, не использовать  
**Enforcement** — IntelliJ 2025.3+ (Java 17); NullAway требует JDK 21.0.8+

---

## Стиль кода Java

**Импорты** — `com.example.*` + `org.*` / `jakarta.*` вместе, затем пустая строка, затем `java.*`; wildcard `.*` при 3+ классах из одного пакета  
**Имена полей** — полные описательные: `noteRepository`, `noteJpaRepository`; не `repository`, `jpa`  
**Пустое тело** — одна пустая строка внутри `{ }` пустых методов и конструкторов  
**EOF** — ровно один `\n` в конце файла (Unix EOF)
