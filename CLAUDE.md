# CLAUDE.md — notes-spring

> Baseline-контекст: читается автоматически на любом устройстве.
> Детали, история и справочник — в memory-файлах `~/.claude/projects/…/memory/`.

---

## Правила

- Общение — **только русский**; код, идентификаторы, комментарии — **только английский**
- Никогда не изменять и не создавать файлы без явного «измени X в файле Y»
- Не трогать `.github/workflows/` без явного запроса
- Читать memory перед ответом — особо: security design, roadmap, problems
- Не предлагать первое попавшееся — взвешивать варианты, проверять соответствие принципам
- CLAUDE.md в приоритете над памятью; обновлять только по явному запросу

---

## Стек

Spring Boot 4 monorepo — banking-grade.

| Инструмент                   | Версия               |
|------------------------------|----------------------|
| Java                         | 17 (`.java-version`) |
| Gradle                       | 9.5.1                |
| Spring Boot                  | 4.0.6                |
| Spring Cloud                 | 2025.1.1             |
| Spring Dependency Management | 1.1.7                |

- `buildSrc` + convention plugins (Groovy)
- Нет root `build.gradle` · нет `buildSrc/settings.gradle` · нет `libs.versions.toml`

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
INFRA         registry/    Eureka Server
              config/      Spring Cloud Config Server
EXTERNAL      Redis        Spring Session backing (bff/ + thymeleaf/ при масштабировании)
              PostgreSQL×N по одной на: auth, user, note, user-note
              Kafka/MQ     только при событийной регистрации (решение не принято)
```

**Ещё не создано:** `bff/` · `thymeleaf/` · `user/domain/` · `note/domain/` · `auth/webmvc/` · `auth/data-jpa/` · `user-note/feign/` · `crud/`

---

## Архитектура

**Hexagonal Architecture (Ports & Adapters):**

```
domain/       entities + port interfaces — чистая Java, без Spring
application/  Spring Boot app, composition root
webmvc/       incoming HTTP adapter → domain/
data-jpa/     persistence adapter → domain/
feign/        outgoing HTTP adapter (только user-note/) → domain/
```

- Адаптеры зависят **только** от `domain/`
- **Stateless:** `gateway/` · `user/` · `note/` · `user-note/` · `registry/` · `config/`
- **Stateful:** `bff/` · `thymeleaf/` → Spring Session; `auth/` → OAuth2Authorization в PostgreSQL

---

## Безопасность

- `identity ≠ profile ≠ business ≠ permissions`
- **Zero Trust** — каждый слой проверяет JWT самостоятельно, не доверяя Gateway
- **JWT claims:** `sub` · `iss` · `aud` · `exp` · `jti` · `acr` · `amr` · `scope` — только стандартные
- **Banking-grade:** фаза 1 — основа · фаза 2 — MFA + token rotation · фаза 3 — DPoP + mTLS

| Слой            | Модуль                | Ответственность                                   |
|-----------------|-----------------------|---------------------------------------------------|
| Edge            | `gateway/`            | Routing, rate limiting, TLS, security headers     |
| BFF             | `bff/` · `thymeleaf/` | OAuth2 login flow, session cookie, Token Exchange |
| Auth / IdP      | `auth/`               | JWT issuing, credentials (AuthUser), OIDC         |
| Resource Server | `*/webmvc/`           | JWT validation per request                        |
| ACL             | `user-note/`          | `UserNote { userId, noteId, role }`               |

**UserNoteRole** — `OWNER` · `EDITOR` · `COMMENTER` · `VIEWER`

**NoteVisibility** *(нерешённо)* — `{ generalAccess, generalRole }`:
- `RESTRICTED` — только явные участники из `UserNote`
- `LINK` + `generalRole` (VIEWER / COMMENTER / EDITOR) — любой с ссылкой

---

## Клиенты

| Клиент              | Auth flow          | Токен хранит                | BFF                     |
|---------------------|--------------------|-----------------------------|-------------------------|
| Browser (Thymeleaf) | Authorization Code | Spring Session в thymeleaf/ | thymeleaf/ сам себе BFF |
| Browser (React/Vue) | Authorization Code | Spring Session в bff/       | bff/                    |
| Android / iOS       | Auth Code + PKCE   | Keychain / Keystore         | нет                     |
| B2B / CLI           | Client Credentials | не хранит                   | нет                     |

---

## Принятые решения

- **Gateway ≠ BFF**: `gateway/` — stateless edge; `bff/` и `thymeleaf/` — отдельные stateful-сервисы
- **BFF — один на UX** (Sam Newman, Phil Calçado): один BFF на тип UX, не общий
- **Thymeleaf = self-contained BFF**: ведёт OAuth2 login flow самостоятельно
- **Token Exchange (RFC 8693), не TokenRelay**: BFF → internal JWT с `aud` перед каждым вызовом микросервиса
- **Zero Trust**: каждый микросервис валидирует JWT — `aud` + `scope` + `sig` + `exp`
- **Spring Authorization Server — постоянный IdP**: OIDC-совместимый; Keycloak/Auth0 — только после детальной оценки
- **MVP = правильная архитектура**: структура финальная; меняются только backing services

---

## Нерешённые вопросы

- **Регистрация** (lazy / sync / events) — выбрать до реализации `auth/`
- **Межсервисные вызовы** (`@ImportHttpServices` vs OpenFeign) — выбрать до `user-note/feign/`
- **NoteVisibility** — `note/domain/` или `user-note/domain/`; выбрать до `note/domain/`

---

## Порядок реализации

```
1. Решить: регистрация — lazy / sync / events       ← ТЕКУЩИЙ БЛОКЕР
2. domain/ во всех бизнес-сервисах
3. auth/ — Authorization Server + AuthUser + OIDC
4. Resource Server в user/ · note/ · user-note/
5. bff/ — OAuth2 Client + Spring Session + Token Exchange
6. thymeleaf/ — server-rendered BFF
7. Banking Phase 2 — MFA, token rotation, audit log
8. user-note/feign/ — Spring HTTP Service Client или OpenFeign
9. crud/ — shared library
```

---

## Принципы

- **SoC / SRP** — на всех уровнях: сервисы, модули, классы, методы
- **Twelve-Factor App** — обязателен для каждого сервиса:
  - III — Config в env vars / Config Server
  - VI — Stateless; Spring Session вместо in-memory state
  - IX — `server.shutdown=graceful`
  - X — Testcontainers, не H2 вместо PostgreSQL в тестах
  - XI — Только stdout
  - XII — Flyway/Liquibase
- **Остальные:** SOLID · KISS · YAGNI · DRY · SSOT · Law of Demeter · Fail Fast · No partial abstractions

---

## Gradle

Convention plugins — **единственный механизм**; flat (без вложенности).

- Нет version catalogs; версии плагинов — только в `buildSrc/build.gradle`
- Порядок блоков: `plugins` → `java` → `repositories` → `dependencyManagement` → `dependencies` → `test`
- **`domain`-plugin** — только `java`; никакого Spring BOM; JSpecify и JUnit с явными версиями
- ✅ Есть: `domain` · `application` · `webmvc` · `data-jpa` · `h2-database`
- ❌ Планируется: `resource-server` · `auth-server` · `oauth2-bff` · `openfeign`

---

## Spring

- `start.spring.io` — источник правды для координат (не Maven Central)
- `spring-boot-starter-web` → в Boot 4: `spring-boot-starter-webmvc`
- Плагин `org.springframework.boot` НЕ применяет `io.spring.dependency-management` автоматически
- **RestTemplate** deprecated → `RestClient` (sync) или `WebClient` (reactive)
- **Jackson 3:** `com.fasterxml.jackson` → `tools.jackson`; `Jackson2ObjectMapperBuilder` → `JsonMapper.Builder`
- **Обработка ошибок:** `ResponseEntityExceptionHandler` + `ProblemDetail` (RFC 9457)
- **Lombok + JPA:** `@Data` / `@EqualsAndHashCode` запрещены на entities

---

## Null Safety

- `@NullMarked` (JSpecify) через `package-info.java` в каждом пакете — non-null по умолчанию
- `org.springframework.lang` — deprecated с Framework 7, не использовать
- Enforcement: IntelliJ 2025.3+ (Java 17); NullAway требует JDK 21.0.8+
