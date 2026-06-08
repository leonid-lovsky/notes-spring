# CLAUDE.md — notes-spring

> Читается автоматически в начале каждой сессии.
> Детали, история, справочники — в `~/.claude/projects/…/memory/`.
> Последнее обновление: 2026-06-09

---

## Правила

- **Язык** — общение на русском; код, идентификаторы, комментарии — на английском
- **Файлы** — не изменять и не создавать без явного «измени X в файле Y»
- **Коммиты** — не коммитить и не пушить без явного запроса
- **CI** — не трогать `.github/workflows/` без явного запроса
- **Подход** — читать память перед ответом; взвешивать варианты, проверять соответствие принципам
- **CLAUDE.md** — в приоритете над памятью; обновлять при каждом изменении проекта или принципов
- **Перед коммитом** — рефакторинг CLAUDE.md → синхронизация памяти → обновление даты
- **≤ 300 строк** — при превышении удалять второстепенное (детали реализации в первую очередь)

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

---

## Архитектура

**Hexagonal Architecture (Ports & Adapters)** — структура каждого бизнес-сервиса:

```
application/  Spring Boot app — composition root
domain/       entities + port interfaces — чистая Java, без Spring
webmvc/       incoming HTTP adapter  →  domain/
data-jpa/     persistence adapter    →  domain/
feign/        outbound HTTP adapter  →  domain/  (только user-note/)
```

**Dependency direction** — `application/` знает всё; адаптеры знают только `domain/`; `domain/` не знает ничего снаружи  
**Database per service** — каждый сервис хранит данные в своей БД; cross-service JOIN запрещён  
**Stateless** — `gateway/` · `config/` · `registry/` · `user/` · `note/` · `user-note/`  
**Stateful** — `bff/` · `thymeleaf/` → Spring Session; `auth/` → OAuth2Authorization в PostgreSQL

### Принятые решения

- **Gateway ≠ BFF** — `gateway/` stateless edge; `bff/` и `thymeleaf/` — отдельные stateful-сервисы
- **BFF — один на UX** (Sam Newman, Phil Calçado) — один BFF на тип устройства, не общий
- **Thymeleaf = self-contained BFF** — ведёт OAuth2 login flow самостоятельно, без отдельного `bff/`
- **Token Exchange (RFC 8693), не TokenRelay** — BFF обменивает access token на internal JWT с `aud` конкретного микросервиса
- **Spring Authorization Server — постоянный IdP** — OIDC-совместимый; Keycloak/Auth0 только после детальной оценки

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

- `OWNER` — полный контроль:
  - редактирование · комментарии · управление доступом · удаление · передача владения
- `EDITOR` — редактирование, комментарии, управление доступом (если не ограничено Owner)
- `COMMENTER` — комментарии и предложения, без правки контента
- `VIEWER` — только чтение, без комментариев

### NoteVisibility

General access — уровень доступа ко всей заметке:

- `RESTRICTED` — только люди с явным доступом
- `ANYONE_WITH_LINK` + роль (`VIEWER` / `COMMENTER` / `EDITOR`) — любой с ссылкой, без входа в аккаунт
- `PUBLIC` + роль (`VIEWER` / `COMMENTER` / `EDITOR`) — любой может найти через поиск, без входа в аккаунт

### Клиенты

| Клиент              | Auth flow          | Токен хранит                | BFF                     |
|---------------------|--------------------|-----------------------------|-------------------------|
| Browser (Thymeleaf) | Authorization Code | Spring Session в thymeleaf/ | thymeleaf/ сам себе BFF |
| Browser (React/Vue) | Authorization Code | Spring Session в bff/       | bff/                    |
| Android / iOS       | Auth Code + PKCE   | Keychain / Keystore         | нет                     |
| B2B / CLI           | Client Credentials | не хранит                   | нет                     |

---

## Статус

> **БЛОКЕР:** Регистрация — выбрать стратегию (lazy / sync / events) до реализации `auth/`

### Нерешённые вопросы

- **Межсервисные вызовы** — `@ImportHttpServices` vs OpenFeign; выбрать до `user-note/feign/`
- **NoteVisibility** — в `note/domain/` или `user-note/domain/`; `note/domain/` создан без неё
- **`user/` временно хранит `password`** — до `auth/`; identity переедет в `auth/AuthUser`

### Порядок реализации

1. Решить: регистрация — lazy / sync / events ← **ТЕКУЩИЙ БЛОКЕР**
2. `domain/` во всех бизнес-сервисах (✓ `note/` ✓ `user-note/` ✓ `user/`)
3. `auth/` — Authorization Server + AuthUser + OIDC
4. Resource Server в `user/` · `note/` · `user-note/`
5. `bff/` — OAuth2 Client + Spring Session + Token Exchange
6. `thymeleaf/` — server-rendered BFF
7. Banking Phase 2 — MFA, token rotation, audit log
8. `user-note/feign/` — Spring HTTP Service Client или OpenFeign
9. `crud/` — shared library

---

## Принципы

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
**`ext {}`** — только для версий сторонних BOM (Spring Cloud, Modulith); в buildSrc не нужен — версии хранятся в convention plugins  
**Порядок зависимостей** — `domain` → `webmvc` → `data-jpa`

**Порядок в `settings.gradle`:**
- сервисы: `gateway` → `config` → `registry` → `auth` → `user` → `note` → `user-note`
- внутри сервиса: `application` → `domain` → `webmvc` → `data-jpa`

**Convention plugins:**
- ✅ Есть: `application` · `domain` · `webmvc` · `data-jpa` · `h2-database`
- ❌ Планируется: `resource-server` · `auth-server` · `oauth2-bff` · `openfeign`

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
- **Flyway + PostgreSQL** — стартер `spring-boot-starter-flyway` не включает драйвер БД; нужен отдельно:
  - `runtimeOnly 'org.flywaydb:flyway-database-postgresql'`
- **Обработка ошибок** — `ResponseEntityExceptionHandler` + `ProblemDetail` (RFC 9457)
- **Lombok** — `compileOnly` + `annotationProcessor` (не `implementation`); то же для тестов
- **Lombok + JPA** — `@Data` / `@EqualsAndHashCode` запрещены на entities; безопасны `@Getter` · `@Setter` · `@Builder`

---

## Spring Security

- **SecurityFilterChain** — бин `HttpSecurity`; несколько цепочек с `securityMatcher`
  - `SecurityContextHolder` хранит `Authentication` текущего потока (ThreadLocal)
- **Authentication** — носитель состояния аутентификации:
  - до верификации: `principal` = String (username), `credentials` = password
  - после верификации: `principal` = `UserDetails`, `credentials` очищается
  - `authorities` = коллекция `GrantedAuthority`; `authenticated` = флаг результата
- **AuthenticationManager** → `ProviderManager` → `AuthenticationProvider`
  - `DaoAuthenticationProvider` — `UserDetailsService.loadUserByUsername()` + `PasswordEncoder`
- **UserDetails** — `username` · `password` · `authorities` + флаги (`enabled` · `accountNonExpired` · `accountNonLocked`)
  - в проекте: `AuthUser` (credentials, `auth/`) ≠ `User` (profile, `user/`)
- **AuthorizationManager** — allow/deny; `@PreAuthorize` / `@PostAuthorize`
- **Resource Server** — `JwtDecoder` + `JwtAuthenticationConverter` → `GrantedAuthority`
- **Auth Server** — `OAuth2AuthorizationServerConfigurer` · `RegisteredClientRepository` · `OAuth2AuthorizationService`

---

## Null Safety

**`@NullMarked`** (JSpecify) через `package-info.java` в каждом пакете — non-null по умолчанию  
`org.springframework.lang` — deprecated с Framework 7, не использовать  
**Enforcement** — IntelliJ 2025.3+ (Java 17); NullAway требует JDK 21.0.8+
