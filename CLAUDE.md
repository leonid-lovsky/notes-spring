# CLAUDE.md — notes-spring

> Baseline контекст — читается автоматически на каждом устройстве, где есть репозиторий.  
> Детальный контекст (история решений, security deep-dive, справочник) — в memory файлах  
> `~/.claude/projects/…/memory/` (локально на каждом устройстве).

---

## Язык

- Общение с пользователем — **только русский**
- Код, идентификаторы, комментарии в коде — **только английский**
- Иностранные термины (Gateway, BFF, Token Exchange, Zero Trust и т.п.) — на английском

---

## Правила поведения (обязательные)

- **Никогда не изменять файлы проекта** без явного "измени X в файле Y"
- **Не создавать `.java` файлы** без явного запроса — работа по умолчанию означает конфигурацию (`build.gradle`, `*.properties`)
- **Не трогать `.github/workflows/`** без явного запроса
- **Читать память перед ответом** — особо: security design, roadmap, problems
- **Не предлагать первое попавшееся** — взвешивать варианты, проверять соответствие принципам

---

## Проект

Spring Boot 4 monorepo — учебный backend с banking-grade уровнем безопасности.

**Версии:**
- Java: 17 (через `.java-version`, без toolchain в convention plugins)
- Gradle: 9.5.1
- Spring Boot: 4.0.6
- Spring Cloud: 2025.1.1
- Spring Dependency Management plugin: 1.1.7

**Build структура:**
- `buildSrc/build.gradle` — plugin versions как `implementation` deps
- Convention plugins в `buildSrc/src/main/groovy/`
- Нет root `build.gradle`. Нет `buildSrc/settings.gradle`. Нет `libs.versions.toml`
- `settings.gradle` — только `include` statements

---

## MVP vs Production

> **MVP = правильная распределённая архитектура.**  
> **Production = MVP + внешние backing services.**

Архитектура финальная с первого дня — не упрощается для MVP и не меняется для Production. Что меняется — только backing services:

```
MVP:        Spring Session in-memory  ·  H2 для локальной разработки
Production: Redis (session scaling)   ·  PostgreSQL × N  ·  Kafka (если event-driven)
```

---

## Канонический список сервисов

```
EDGE          gateway/      Spring Cloud Gateway — stateless, только routing + rate limiting
PRESENTATION  bff/          Spring MVC + OAuth2 Client + Spring Session + Token Exchange (для SPA)
              thymeleaf/    Spring MVC + Thymeleaf + OAuth2 Client (self-contained BFF, server HTML)
IDENTITY      auth/         Spring Authorization Server — OIDC-compliant, JWT issuer
BUSINESS      user/         Spring MVC + Data JPA + Resource Server
              note/         Spring MVC + Data JPA + Resource Server
              user-note/    Spring MVC + Data JPA + Resource Server + Feign → user, note
INFRA         registry/     Eureka Server
              config/       Spring Cloud Config Server
EXTERNAL      Redis         Spring Session backing для bff/ + thymeleaf/ при масштабировании
              PostgreSQL×N  по одной на: auth, user, note, user-note (H2 в локальной разработке)
              Kafka/RabbitMQ  только при событийной регистрации — решение не принято
```

**Ещё не создано:** `bff/` · `thymeleaf/` · `*/domain/` · `auth/webmvc/` · `auth/data-jpa/` · `user-note/feign/` · `crud/`

---

## Архитектура модулей (Hexagonal)

Каждый бизнес-сервис (`user/`, `note/`, `user-note/`, `auth/`):

```
domain/       чистая Java — entities + port interfaces. Без Spring. Ни от чего не зависит.
application/  Spring Boot app, composition root. Знает все адаптеры.
webmvc/       incoming HTTP adapter. Зависит только от domain/.
data-jpa/     persistence adapter. Зависит только от domain/.
feign/        outgoing HTTP adapter (только в user-note/). Зависит только от domain/.
```

**Правило:** адаптеры зависят только от `domain/`. Импорт из `application/` в адаптер — нарушение изоляции.

---

## Принятые архитектурные решения (финальные)

- **Gateway ≠ BFF**: `gateway/` — stateless edge. `bff/` и `thymeleaf/` — отдельные stateful сервисы с Spring Session
- **BFF — один на UX** (Sam Newman: *"one experience, one BFF"*; Phil Calçado: *"BFF — часть приложения"*): если BFF признан необходимым — по одному на каждый тип пользовательского опыта, не один общий. BFF содержит Presentation Model конкретного UI. Дублирование логики между BFF устраняется выделением доменного микросервиса снизу — не shared library (`user-note/` как ACL — пример этого паттерна). В нашем проекте основная мотивация — безопасность: скрытие JWT от браузера (XSS), OAuth2 session management; агрегация — вторична.
- **Thymeleaf = self-contained BFF**: сам ведёт OAuth2 login flow, не нуждается в отдельном `bff/`
- **Mobile без BFF**: PKCE + Keychain/Keystore → Gateway → микросервисы напрямую
- **Token Exchange (не Relay)**: `bff/` и `thymeleaf/` обменивают токен на internal JWT с `aud` перед каждым вызовом микросервиса — с первого дня, не откладывается
- **Zero Trust**: каждый микросервис сам валидирует JWT (`aud` + `scope` + `sig` + `exp`). Не доверять Gateway.
- **Spring Authorization Server — постоянный IdP**: OIDC-совместимый по дизайну (`issuer-uri` в properties, стандартные claims, OIDC Discovery). Keycloak/Auth0/Okta не планируются — только после детальной оценки.
- **Banking-grade**: фаза 1 — основа (с первого дня), фаза 2 — MFA + token rotation, фаза 3 — DPoP + mTLS

---

## Stateful / Stateless

```
STATELESS (масштабируются свободно):
  gateway/  ·  user/  ·  note/  ·  user-note/  ·  registry/  ·  config/

STATEFUL (требуют backing service при горизонтальном масштабировании):
  bff/        → Spring Session (default: in-memory → Redis при масштабировании)
  thymeleaf/  → Spring Session (default: in-memory → Redis при масштабировании)
  auth/       → OAuth2Authorization в PostgreSQL (token store, НЕ user session)
```

`auth/` — token store, не session store. User session живёт только в `bff/` и `thymeleaf/`.

---

## Безопасность — распределение ответственности

> Архитектура = система разделения доверия (trust boundaries), не просто система логина.  
> Каждый слой доверяет только тому, что сам проверил.

**Принцип разграничения доменов:** `identity ≠ profile ≠ business ≠ permissions`

```
auth/       → identity (credentials, tokens, OIDC)
user/       → profile (name, avatar, address)
note/       → business content
user-note/  → permissions (userId ↔ noteId ↔ role)
```

| Слой            | Модуль                | Ответственность                                                                       |
|-----------------|-----------------------|---------------------------------------------------------------------------------------|
| Edge            | `gateway/`            | Routing, rate limiting, TLS, security headers — stateless                             |
| BFF             | `bff/` · `thymeleaf/` | Presentation Model; OAuth2 login flow, session cookie, UI aggregation, Token Exchange |
| Auth / IdP      | `auth/`               | JWT issuing, credentials (AuthUser), OIDC endpoints                                   |
| Resource Server | `*/webmvc/`           | JWT validation per request — Zero Trust                                               |
| ACL             | `user-note/`          | `UserNote { userId, noteId, role }` — права доступа                                   |

**JWT claims:** `sub`, `iss`, `aud`, `exp`, `jti`, `acr`, `amr`, `scope` — только стандартные.

**Все методы входа** (password / social / TOTP / Passkey / Biometric) обрабатываются в `auth/` и дают на выходе единый JWT. Клиенты не знают, каким методом вошёл пользователь.

---

## Клиенты

| Клиент              | Auth flow          | Токен хранит                | BFF                      |
|---------------------|--------------------|-----------------------------|--------------------------|
| Browser (Thymeleaf) | Authorization Code | Spring Session в thymeleaf/ | thymeleaf/ сам себе BFF  |
| Browser (React/Vue) | Authorization Code | Spring Session в bff/       | bff/                     |
| Android / iOS       | Auth Code + PKCE   | Keychain / Keystore         | нет — напрямую к Gateway |
| B2B / CLI           | Client Credentials | не хранит                   | нет — напрямую к Gateway |

Mobile: PKCE обязательно, нет `client_secret`, токен в OS-хранилище. XSS физически невозможен.

---

## Нерешённые вопросы (блокируют реализацию)

**Координация при регистрации** — выбрать до реализации `auth/`:
1. **Lazy** — `user/` создаёт профиль при первом запросе (нет email при регистрации, нет coupling)
2. **Sync** — `auth/` вызывает `user/` через RestClient (нарушает направление зависимостей)
3. **Events** — Kafka/RabbitMQ, архитектурно верно, требует нового backing service

---

## Порядок реализации

```
1.  Решить: регистрация — lazy / sync / events        ← ТЕКУЩИЙ БЛОКЕР
2.  domain/ во всех бизнес-сервисах (user, note, user-note)
3.  auth/ — Authorization Server + AuthUser + OIDC endpoints
4.  Resource Server в user/ · note/ · user-note/
5.  bff/ — OAuth2 Client + Spring Session + Token Exchange
6.  thymeleaf/ — server-rendered BFF
7.  Banking Phase 2 — MFA, token rotation, audit log
8.  user-note/feign/ — межсервисные вызовы
9.  crud/ — shared library
```

Redis, PostgreSQL, Kafka — production backing services, подключаются при реальной потребности, не как шаги архитектуры.

---

## Twelve-Factor App (руководящий принцип)

Проверять каждое предложение. Чек-лист для нового сервиса:

```
□ III  URL, пароли, порты — в Config Server / env vars. Не в коде.
□ IV   Backing services (DB, Redis, Kafka) — URL из конфига, не хардкодить.
□ VI   Сервис stateless: нет in-memory state между запросами.
       Если нужна сессия — Spring Session (backing store конфигурируется отдельно).
□ IX   server.shutdown=graceful в application.properties.
□ X    Testcontainers для интеграционных тестов. Не H2 вместо PostgreSQL.
□ XI   Только stdout в логах. Никаких FileAppender.
□ XII  Миграции через Flyway/Liquibase. Не вручную.
```

---

## Принципы дизайна

**Приоритет 1 — SoC и SRP** на всех уровнях: сервисы, модули, классы, методы.

Остальные принципы:
- **Максимум Spring Boot autoconfigure** — не конфигурировать то, что Spring делает по умолчанию
- **Максимум Spring starters** — готовые стартеры приоритетнее ручной сборки зависимостей
- **Минимум настроек** — только то, что реально отличается от defaults
- **Минимум кастома** — стандартный Spring механизм всегда приоритетнее самописного
- **Минимум внешних инструментов** — backing services только при явной потребности, не upfront
- **DRY** — convention plugins как единственное место для build-логики
- **SSOT** — `domain/` — единственное место для бизнес-модели
- **YAGNI** — не добавлять то, что не нужно прямо сейчас
- **KISS** — простое решение лучше умного
- **SOLID** — особо D: зависеть от абстракций (`domain/`), не от реализаций
- **Law of Demeter** — `webmvc/` видит `domain/`, не `data-jpa/`
- **Fail Fast** — ошибка немедленно, никогда молча
- **No partial abstractions** — полное устранение или явное дублирование. Частичное сокращение строк хуже обоих.

---

## Gradle

- Convention plugins — **единственный механизм** для build-логики
- Flat структура: conventions НЕ применяют другие conventions
- Inline всё: нет version catalogs, нет `[versions]`, нет внешних version файлов
- `settings.gradle` — только `include`
- `buildSrc/build.gradle` — plugin versions как `implementation` + `repositories` + `groovy-gradle-plugin`
- Нет root `build.gradle`. Нет `buildSrc/settings.gradle`.
- Порядок блоков в convention: `plugins` → `java` → `repositories` → `dependencyManagement` → `dependencies` → `tasks.named('test')` в конце
- FQN для BOM: `org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES`
- Версии плагинов только в `buildSrc/build.gradle` как `implementation` deps — никогда в `plugins {}` convention plugins

**Избегать ("макароны"):** convention применяет convention · version catalogs · `subprojects`/`allprojects` · `pluginManagement` с вложенными `plugins` · абстракции ради 3 строк

---

## Spring

- **Максимум стандартных стартеров** — кастомный код только там, где Spring не покрывает
- **Единственный источник правды** для координат: `start.spring.io` (не Maven Central)
- Maven Central ≠ авторитет для Boot 4: 0 результатов не значит что зависимость не существует
- `spring-boot-starter-web` → в Boot 4 называется `spring-boot-starter-webmvc`
- Каждый starter имеет `-test` вариант
- `org.springframework.boot` plugin НЕ применяет автоматически `io.spring.dependency-management` — оба объявлять явно
- Предпочтительные стандартные решения: `JdbcUserDetailsManager`, Spring Session, Spring Authorization Server built-in endpoints

---

## Convention plugins (текущие и планируемые)

```
✅ spring-boot-application-conventions.gradle
✅ spring-webmvc-adapter-conventions.gradle
✅ spring-data-jpa-adapter-conventions.gradle
✅ spring-h2-database-conventions.gradle
❌ spring-security-resource-server-conventions.gradle
❌ spring-authorization-server-conventions.gradle
❌ spring-oauth2-client-bff-conventions.gradle
❌ spring-openfeign-adapter-conventions.gradle
```
