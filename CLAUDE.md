# CLAUDE.md — notes-spring

> Baseline контекст — читается автоматически на любом устройстве где есть репозиторий.
> Детальный контекст (история решений, security deep-dive, reference) — в memory файлах
> `~/.claude/projects/…/memory/` (локально на каждом устройстве).

## Язык

- Общение с пользователем — **только русский**
- Код, идентификаторы, комментарии в коде — **только английский**

---

## Правила поведения (обязательные)

- **Никогда не изменять файлы проекта** без явного "измени X в файле Y" от пользователя
- **Не создавать `.java` файлы** без явного запроса — работа означает конфигурацию: `build.gradle`, `*.properties`
- **Не трогать `.github/workflows/`** без явного запроса
- **Читать память перед ответом** — особо: security design, roadmap, problems
- **Не предлагать первое попавшееся** — взвешивать варианты, учитывать принципы
- **Перед любым предложением проверять** соответствие Twelve-Factor App и принципам дизайна

---

## Проект

Spring Boot 4 monorepo — учебный backend с банковским уровнем безопасности.

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

## Канонический список сервисов

```
EDGE          gateway/      Spring Cloud Gateway — stateless, routing + rate limiting только
PRESENTATION  bff/          Spring MVC + OAuth2 Client + Session → Redis (для SPA React/Vue)
              thymeleaf/    Spring MVC + Thymeleaf + OAuth2 Client (self-contained BFF, server HTML)
IDENTITY      auth/         Spring Authorization Server — OIDC-compliant, JWT issuer
BUSINESS      user/         Spring MVC + Data JPA + Resource Server
              note/         Spring MVC + Data JPA + Resource Server
              user-note/    Spring MVC + Data JPA + Resource Server + Feign → user, note
INFRA         registry/     Eureka Server
              config/       Spring Cloud Config Server
EXTERNAL      Redis         сессии bff/ + thymeleaf/ (Twelve-Factor VI)
              PostgreSQL×N  по одной: auth, user, note, user-note
              Kafka/RabbitMQ  [если событийная регистрация — решение не принято]
```

**Что не создано ещё:** `bff/` · `thymeleaf/` · `*/domain/` · `auth/webmvc/` · `auth/data-jpa/` · `user-note/feign/` · `crud/`

---

## Архитектура модулей (Hexagonal)

Каждый бизнес-сервис (`user/`, `note/`, `user-note/`, `auth/`):

```
domain/       чистая Java — entities + port interfaces. Без Spring. Ни от чего не зависит.
application/  Spring Boot app, composition root. Знает все адаптеры.
webmvc/       incoming HTTP adapter. Зависит только от domain/. Не от application/.
data-jpa/     persistence adapter. Зависит только от domain/. Не от application/.
feign/        outgoing HTTP adapter (только в user-note/). Зависит только от domain/.
```

**Правило:** адаптеры зависят только от `domain/`. Импорт из `application/` в адаптер — нарушение изоляции.

---

## Принятые архитектурные решения (финальные)

- **Gateway ≠ BFF**: `gateway/` — stateless edge. `bff/` и `thymeleaf/` — отдельные stateful сервисы с сессиями в Redis
- **Thymeleaf = self-contained BFF**: сам ведёт OAuth2 login flow, не нуждается в отдельном `bff/`
- **Mobile без BFF**: PKCE + Keychain/Keystore → Gateway → Microservices напрямую
- **Token Exchange (не Relay)**: `bff/` и `thymeleaf/` обменивают токен через auth/ на узкий `internal_token` с `aud` перед каждым вызовом микросервиса
- **Zero Trust**: каждый микросервис сам валидирует JWT (`aud` + `scope` + `sig` + `exp`). Не доверять gateway.
- **Spring Authorization Server для MVP**: Keycloak-ready — `issuer-uri` в properties, стандартные claims, OIDC Discovery
- **Keycloak не сейчас**: замена IdP = смена `issuer-uri`. Бизнес-сервисы не трогаются.
- **Banking-grade цель**: фаза 1 — основа, фаза 2 — MFA + token rotation + session control, фаза 3 — DPoP + mTLS

---

## Stateful / Stateless

```
STATELESS (масштабируются свободно):
  gateway/  user/  note/  user-note/  registry/  config/

STATEFUL с внешним хранилищем (масштабируются горизонтально):
  bff/        → HttpSession в Redis        (user session для SPA)
  thymeleaf/  → HttpSession в Redis        (user session для web)
  auth/       → OAuth2Authorization в PostgreSQL  (token store, НЕ user session)
```

`auth/` — token store, не session store. User session живёт только в `bff/` и `thymeleaf/`.

---

## Безопасность — распределение ответственности

| Слой            | Модуль                | Ответственность                                           |
|-----------------|-----------------------|-----------------------------------------------------------|
| Edge            | `gateway/`            | Routing, rate limiting, TLS, security headers — stateless |
| BFF             | `bff/` · `thymeleaf/` | OAuth2 login flow, session cookie, Token Exchange         |
| Auth/IdP        | `auth/`               | Issuing JWT, credentials (AuthUser), OIDC endpoints       |
| Resource Server | `*/webmvc/`           | JWT validation per request — Zero Trust                   |
| ACL             | `user-note/`          | `UserNote { userId, noteId, role }` — права доступа       |

**JWT claims:** `sub`, `iss`, `aud`, `exp`, `jti`, `acr`, `amr`, `scope` — только стандартные.

**Все методы входа** (password / social / TOTP / Passkey / Biometric) обрабатываются в `auth/` и дают на выходе единый JWT. Клиенты не знают, каким методом вошёл пользователь.

---

## Клиенты

| Клиент              | Auth flow          | Токен хранит                 | BFF                      |
|---------------------|--------------------|------------------------------|--------------------------|
| Browser (Thymeleaf) | Authorization Code | Сессия в thymeleaf/ → Redis  | thymeleaf/ сам себе BFF  |
| Browser (React/Vue) | Authorization Code | Сессия в bff/ → Redis        | bff/                     |
| Android / iOS       | Auth Code + PKCE   | Keychain / Keystore          | Нет — прямо к Gateway    |
| B2B / CLI           | Client Credentials | Не хранит                    | Нет — прямо к Gateway    |

Mobile: PKCE обязательно, нет client_secret, токен в OS-хранилище. XSS физически невозможен.

---

## Нерешённые вопросы (блокируют реализацию)

**Координация при регистрации** — выбрать один вариант до реализации `auth/`:
1. Lazy — `user/` создаёт профиль при первом запросе (нет email при регистрации)
2. Sync — `auth/` вызывает `user/` через RestClient (нарушает направление зависимостей)
3. Events — брокер (Kafka/RabbitMQ), архитектурно верно, требует нового сервиса

---

## Порядок реализации

```
1.  Решить: регистрация — lazy / sync / events        ← ТЕКУЩИЙ БЛОКЕР
2.  domain/ во всех бизнес-сервисах (user, note, user-note)
3.  auth/ — Authorization Server + AuthUser + OIDC
4.  Resource Server в user/ · note/ · user-note/
5.  bff/ — OAuth2 Client + Session + Token Exchange
6.  thymeleaf/ — server-rendered BFF
7.  Redis — сессии bff/ + thymeleaf/
8.  Banking Phase 2 — MFA, token rotation, audit log
9.  user-note/feign/ — межсервисные вызовы
10. crud/ — shared library
```

---

## Twelve-Factor App (руководящий принцип)

Проверять каждое предложение. Чек-лист для нового сервиса:

```
□ III   URL, пароли, порты — в Config Server / env vars. Не в коде.
□ IV    Backing services (DB, Redis, Kafka) — URL из конфига, не захардкожен.
□ VI    Сервис stateless: нет in-memory state между запросами.
        Если нужна сессия — Redis. Если нужен state — база данных.
□ IX    server.shutdown=graceful в application.properties.
□ X     Testcontainers для интеграционных тестов. Не H2 вместо PostgreSQL.
□ XI    Только stdout в логах. Никаких FileAppender.
□ XII   Миграции через Flyway/Liquibase. Не вручную.
```

---

## Принципы дизайна

**Приоритет 1 — SoC и SRP** на всех уровнях: сервисы, модули, классы, методы.

Остальные принципы в порядке применения:
- **DRY** — convention plugins как единственное место для build-логики
- **SSOT** — `domain/` — единственное место для бизнес-модели
- **YAGNI** — не добавлять то, что не нужно прямо сейчас
- **KISS** — простое решение лучше умного
- **SOLID** — особо D: зависеть от абстракций (`domain/`), не от реализаций
- **Law of Demeter** — `webmvc/` видит `domain/`, не `data-jpa/`
- **Fail Fast** — ошибка немедленно, никогда молча
- **No partial abstractions** — полное устранение дублирования или явное дублирование. Частичное сокращение строк хуже обоих.

---

## Gradle

- Convention plugins — **единственный механизм** для build-логики
- Flat структура: conventions НЕ применяют другие conventions
- Inline всё: нет version catalogs, нет `[versions]`, нет внешних version файлов
- `settings.gradle` = только `include`
- `buildSrc/build.gradle` = plugin versions как `implementation` + `repositories` + `groovy-gradle-plugin`
- Нет root `build.gradle`. Нет `buildSrc/settings.gradle`.
- Порядок блоков в convention: `plugins` → `java` → `repositories` → `dependencyManagement` → `dependencies` → `tasks.named('test')` в конце
- FQN для BOM: `org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES`
- Версии плагинов только в `buildSrc/build.gradle` как `implementation` deps — никогда в `plugins {}` convention plugins

**"Макароны" (избегать):** convention применяет convention · version catalogs · subprojects/allprojects · pluginManagement с вложенными plugins · абстракции ради 3 строк

---

## Spring

- **Максимум стандартных стартеров** — кастомный код только там, где Spring не покрывает
- **Единственный источник правды** для координат: `start.spring.io` (не Maven Central)
- Maven Central ≠ авторитет для Boot 4: 0 результатов не значит что зависимости нет
- `spring-boot-starter-web` → в Boot 4 называется `spring-boot-starter-webmvc`
- Каждый starter имеет `-test` вариант
- `org.springframework.boot` plugin НЕ применяет автоматически `io.spring.dependency-management` — оба объявлять явно
- Предпочтительные стандартные решения: `JdbcUserDetailsManager`, Spring Session, Spring Authorization Server встроенные endpoints

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