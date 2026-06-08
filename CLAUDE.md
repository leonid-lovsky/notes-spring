# CLAUDE.md — notes-spring

> Baseline-контекст: читается автоматически на любом устройстве.
> Детали решений, история и справочник по Spring API — в memory-файлах `~/.claude/projects/…/memory/`.

---

## Язык

- Общение с пользователем — **только русский**
- Код, идентификаторы, комментарии в коде — **только английский**
- Профессиональные термины (Gateway, BFF, Token Exchange, Zero Trust и др.) — на английском

---

## Правила поведения

- Никогда не изменять и не создавать файлы проекта без явного «измени X в файле Y»
- Не трогать `.github/workflows/` без явного запроса
- Читать memory перед ответом — особо: security design, roadmap, problems
- Не предлагать первое попавшееся — взвешивать варианты, проверять соответствие принципам
- CLAUDE.md — живой документ; обновлять только по явному запросу

---

## Проект

Spring Boot 4 monorepo — учебный backend с banking-grade уровнем безопасности.

| Инструмент                  | Версия                                          |
|-----------------------------|-------------------------------------------------|
| Java                        | 17 (`.java-version`, без toolchain в conventions) |
| Gradle                      | 9.5.1                                           |
| Spring Boot                 | 4.0.6                                           |
| Spring Cloud                | 2025.1.1                                        |
| Spring Dependency Management | 1.1.7                                          |

Build: `buildSrc/build.gradle` — версии плагинов как `implementation` deps; convention plugins в `buildSrc/src/main/groovy/`. Нет root `build.gradle`, нет `buildSrc/settings.gradle`, нет `libs.versions.toml`.

---

## Сервисы

```
EDGE          gateway/     Spring Cloud Gateway — stateless, routing + rate limiting
PRESENTATION  bff/         OAuth2 Client + Spring Session + Token Exchange (для SPA)
              thymeleaf/   Thymeleaf + OAuth2 Client — self-contained BFF, server-rendered
IDENTITY      auth/        Spring Authorization Server — OIDC-compliant, JWT issuer
BUSINESS      user/        Resource Server — профиль  (User: id, username, email)
              note/        Resource Server — контент  (Note: id, content)
              user-note/   Resource Server — ACL      (UserNote: userId, noteId, role)
INFRA         registry/    Eureka Server
              config/      Spring Cloud Config Server
EXTERNAL      Redis        Spring Session backing при масштабировании bff/ + thymeleaf/
              PostgreSQL×N по одной на: auth, user, note, user-note
              Kafka/MQ     только при событийной регистрации (решение не принято)
```

**Ещё не создано:** `bff/` · `thymeleaf/` · `*/domain/` · `auth/webmvc/` · `auth/data-jpa/` · `user-note/feign/` · `crud/`

---

## Архитектура

**Hexagonal Architecture (Ports & Adapters)** — каждый бизнес-сервис:

```
domain/       Чистая Java — entities + port interfaces. Без Spring. Ни от чего не зависит.
application/  Spring Boot app, composition root. Знает все адаптеры.
webmvc/       Incoming HTTP adapter — depends on domain/
data-jpa/     Persistence adapter — depends on domain/
feign/        Outgoing HTTP adapter (только user-note/) — depends on domain/
```

Адаптеры зависят только от `domain/`. Импорт из `application/` в адаптер — нарушение изоляции.

**Stateless:** `gateway/` · `user/` · `note/` · `user-note/` · `registry/` · `config/`

**Stateful:** `bff/` · `thymeleaf/` → Spring Session; `auth/` → OAuth2Authorization в PostgreSQL (token store, не user session)

---

## Безопасность

**Принцип:** `identity ≠ profile ≠ business ≠ permissions`. Zero Trust — каждый слой доверяет только тому, что сам проверил.

| Слой            | Модуль                | Ответственность                                     |
|-----------------|-----------------------|-----------------------------------------------------|
| Edge            | `gateway/`            | Routing, rate limiting, TLS, security headers       |
| BFF             | `bff/` · `thymeleaf/` | OAuth2 login flow, session cookie, Token Exchange   |
| Auth / IdP      | `auth/`               | JWT issuing, credentials (AuthUser), OIDC endpoints |
| Resource Server | `*/webmvc/`           | JWT validation per request — Zero Trust             |
| ACL             | `user-note/`          | `UserNote { userId, noteId, role }`                 |

**JWT claims:** `sub` · `iss` · `aud` · `exp` · `jti` · `acr` · `amr` · `scope` — только стандартные.

**Banking-grade:** фаза 1 — основа (с первого дня) · фаза 2 — MFA + token rotation · фаза 3 — DPoP + mTLS.

---

## Клиенты

| Клиент              | Auth flow          | Токен хранит                | BFF                      |
|---------------------|--------------------|-----------------------------|--------------------------|
| Browser (Thymeleaf) | Authorization Code | Spring Session в thymeleaf/ | thymeleaf/ сам себе BFF  |
| Browser (React/Vue) | Authorization Code | Spring Session в bff/       | bff/                     |
| Android / iOS       | Auth Code + PKCE   | Keychain / Keystore         | нет                      |
| B2B / CLI           | Client Credentials | не хранит                   | нет                      |

---

## Принятые решения

- **Gateway ≠ BFF**: `gateway/` — stateless edge; `bff/` и `thymeleaf/` — отдельные stateful-сервисы
- **BFF — один на UX** (Sam Newman, Phil Calçado): один BFF на тип пользовательского опыта, не общий для всех
- **Thymeleaf = self-contained BFF**: ведёт OAuth2 login flow самостоятельно, не требует отдельного `bff/`
- **Token Exchange (RFC 8693), не TokenRelay**: BFF обменивает public JWT на internal JWT с `aud` перед каждым вызовом микросервиса
- **Zero Trust**: каждый микросервис валидирует JWT самостоятельно — `aud` + `scope` + `sig` + `exp`
- **Spring Authorization Server — постоянный IdP**: OIDC-совместимый по дизайну; Keycloak/Auth0 — только после детальной оценки
- **MVP = правильная архитектура**: структура финальная с первого дня; меняются только backing services

---

## Нерешённые вопросы

- **Координация при регистрации** (lazy / sync / events) — выбрать до реализации `auth/`
- **Межсервисные вызовы** (`@ImportHttpServices` vs OpenFeign) — выбрать до реализации `user-note/feign/`

---

## Порядок реализации

```
1. Решить: регистрация — lazy / sync / events       ← ТЕКУЩИЙ БЛОКЕР
2. domain/ во всех бизнес-сервисах
3. auth/ — Authorization Server + AuthUser + OIDC endpoints
4. Resource Server в user/ · note/ · user-note/
5. bff/ — OAuth2 Client + Spring Session + Token Exchange
6. thymeleaf/ — server-rendered BFF
7. Banking Phase 2 — MFA, token rotation, audit log
8. user-note/feign/ — Spring HTTP Service Client или OpenFeign
9. crud/ — shared library
```

---

## Принципы

**Tier 1 — SoC и SRP** на всех уровнях: сервисы, модули, классы, методы.

**Twelve-Factor App — обязателен.** Чек-лист для нового сервиса:

```
□ III  Config в Config Server / env vars — не в коде
□ VI   Stateless: Spring Session вместо in-memory state между запросами
□ IX   server.shutdown=graceful
□ X    Testcontainers — не H2 вместо PostgreSQL в тестах
□ XI   Только stdout, никаких FileAppender
□ XII  Миграции через Flyway/Liquibase
```

**Остальные принципы:** SOLID · KISS · YAGNI · DRY · SSOT · Law of Demeter · Fail Fast · No partial abstractions · Convention over Configuration · Composition over Inheritance

---

## Gradle

- Convention plugins — **единственный механизм** для build-логики
- Flat структура: conventions НЕ применяют другие conventions
- Inline всё: нет version catalogs, нет `[versions]`, нет внешних version-файлов
- Порядок блоков в convention: `plugins` → `java` → `repositories` → `dependencyManagement` → `dependencies` → `tasks.named('test')`
- FQN для BOM: `org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES`
- Версии плагинов — только в `buildSrc/build.gradle` как `implementation` deps, никогда в `plugins {}`

Существуют: ✅ `application` · `webmvc` · `data-jpa` · `h2-database`
Планируются: ❌ `resource-server` · `auth-server` · `oauth2-bff` · `openfeign`

---

## Spring

- Источник правды для координат: `start.spring.io` — не Maven Central (0 результатов ≠ зависимость не существует)
- `spring-boot-starter-web` → в Boot 4: `spring-boot-starter-webmvc`
- Плагин `org.springframework.boot` НЕ применяет `io.spring.dependency-management` автоматически — оба объявлять явно
- **RestTemplate** — deprecated в Framework 7.0, удалён в 8.0 → `RestClient` (sync) или `WebClient` (reactive)
- **`@Retryable` / `@ConcurrencyLimit`** — встроены в Framework 7; активация: `@EnableResilientMethods`
- **Jackson 3:** пакет `com.fasterxml.jackson` → `tools.jackson`; `Jackson2ObjectMapperBuilder` удалён → `JsonMapper.Builder`
- **MFA:** `@EnableMultiFactorAuthentication` + `FactorGrantedAuthority` — встроено в Security 7 (фаза 2)
- **Обработка ошибок:** `@ControllerAdvice extends ResponseEntityExceptionHandler` + `ProblemDetail` (RFC 9457); `spring.mvc.problemdetails.enabled=true`
- **Lombok + JPA:** `@Data` и `@EqualsAndHashCode` запрещены на entities; безопасны: `@Getter`, `@Setter`, `@Builder`

---

## Null Safety

- `@NullMarked` (JSpecify) через `package-info.java` в каждом пакете — non-null по умолчанию в аннотированной области
- `org.springframework.lang` — **deprecated** с Framework 7, не использовать
- Enforcement: IDE IntelliJ 2025.3+ (текущий вариант при Java 17); NullAway требует JDK 21.0.8+
