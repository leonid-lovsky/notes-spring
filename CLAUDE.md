# CLAUDE.md — notes-spring

> Baseline-контекст: читается автоматически на любом устройстве.
> Детали, история и справочник — в memory-файлах `~/.claude/projects/…/memory/`.
> Последнее обновление: 2026-06-09

---

## Правила

- Общение — **только русский**; код, идентификаторы, комментарии — **только английский**
- Никогда не изменять и не создавать файлы без явного «измени X в файле Y»
- Не трогать `.github/workflows/` без явного запроса
- Читать CLAUDE.md и memory в начале каждой сессии — особо: security design, roadmap, problems
- Не предлагать первое попавшееся — взвешивать варианты, проверять соответствие принципам
- CLAUDE.md в приоритете над памятью; обновлять только по явному запросу
- Не коммитить без явного запроса — пользователь должен проверить изменения
- **Перед коммитом:** рефакторинг + улучшение читаемости CLAUDE.md → синхронизация памяти → обновление даты
- **CLAUDE.md ≤ 300 строк**; при превышении удалять второстепенное (детали реализации в первую очередь)

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
BUSINESS      user/        Resource Server  (User: id, username, email, password)
              note/        Resource Server  (Note: id, content)
              user-note/   Resource Server  (UserNote: userId, noteId, role)
INFRA         config/      Spring Cloud Config Server
              registry/    Eureka Server
EXTERNAL      Redis        Spring Session backing (bff/ + thymeleaf/ при масштабировании)
              PostgreSQL×N по одной на: auth, user, note, user-note
              Kafka/MQ     только при событийной регистрации (решение не принято)
```

**Ещё не создано:** `bff/` · `thymeleaf/` · `auth/webmvc/` · `auth/data-jpa/` · `user-note/feign/` · `crud/`

---

## Нерешённые вопросы

- **Регистрация** (lazy / sync / events) — выбрать до реализации `auth/` ← **БЛОКЕР**
- **Межсервисные вызовы** (`@ImportHttpServices` vs OpenFeign) — выбрать до `user-note/feign/`
- **NoteVisibility** — в `note/domain/` или `user-note/domain/`; `note/domain/` создан без неё
- **`user/` временно хранит `password`** — до реализации `auth/`; identity переедет в `auth/AuthUser`

---

## Порядок реализации

```
1. Решить: регистрация — lazy / sync / events            ← ТЕКУЩИЙ БЛОКЕР
2. domain/ во всех бизнес-сервисах (✓ note/ ✓ user-note/ ✓ user/)
3. auth/ — Authorization Server + AuthUser + OIDC
4. Resource Server в user/ · note/ · user-note/
5. bff/ — OAuth2 Client + Spring Session + Token Exchange
6. thymeleaf/ — server-rendered BFF
7. Banking Phase 2 — MFA, token rotation, audit log
8. user-note/feign/ — Spring HTTP Service Client или OpenFeign
9. crud/ — shared library
```

---

## Архитектура

Hexagonal Architecture (Ports & Adapters):

```
application/  Spring Boot app, composition root
domain/       entities + port interfaces — только java plugin, без Spring
webmvc/       incoming HTTP adapter  →  domain/
data-jpa/     persistence adapter    →  domain/
feign/        outbound HTTP adapter  →  domain/  (только user-note/)
```

- **Dependency direction** — `application/` → `domain/` ← adapters; адаптеры не зависят друг от друга и не знают `application/`
- **Stateless:** `gateway/` · `config/` · `registry/` · `user/` · `note/` · `user-note/`
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

**UserNoteRole:**
- `OWNER` — полный контроль: редактирование, комментарии, управление доступом, удаление, передача владения
- `EDITOR` — редактирование, комментарии, управление доступом (если не ограничено Owner)
- `COMMENTER` — комментарии и предложения, без правки контента
- `VIEWER` — только чтение, без комментариев

**NoteVisibility** — general access на уровне Note:
- `RESTRICTED` — только люди с явным доступом
- `ANYONE_WITH_LINK` + роль (`VIEWER` / `COMMENTER` / `EDITOR`) — любой с ссылкой, без входа в аккаунт
- `PUBLIC` + роль (`VIEWER` / `COMMENTER` / `EDITOR`) — любой может найти через поиск, без входа в аккаунт

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

- **Gateway ≠ BFF** — `gateway/` stateless edge; `bff/` и `thymeleaf/` — отдельные stateful-сервисы
- **BFF — один на UX** (Sam Newman, Phil Calçado) — один BFF на тип устройства, не общий
- **Thymeleaf = self-contained BFF** — ведёт OAuth2 login flow самостоятельно
- **Token Exchange (RFC 8693), не TokenRelay** — BFF → internal JWT с `aud` перед каждым вызовом микросервиса
- **Spring Authorization Server — постоянный IdP** — OIDC-совместимый; Keycloak/Auth0 только после детальной оценки

---

## Принципы

- **SoC / SRP** — на всех уровнях: сервисы, модули, классы, методы
- **Видимость** — `default` (package-private) или минимально необходимая; `public` только для реального публичного API
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

- Convention plugins — **единственный механизм**; flat (без вложенности)
- Нет version catalogs; версии плагинов — только в `buildSrc/build.gradle`
- Порядок блоков: `plugins` → `java` → `repositories` → `dependencyManagement` → `dependencies` → `test`
- Порядок зависимостей в `build.gradle`: `domain` → `webmvc` → `data-jpa`
- Порядок модулей в `settings.gradle`: `gateway` → `config` → `registry` → `auth` → `user` → `note` → `user-note`; внутри сервиса: `application` → `domain` → `webmvc` → `data-jpa`
- **`domain`-plugin** — только `java`; никакого Spring BOM; JSpecify и JUnit с явными версиями
- ✅ Есть: `application` · `domain` · `webmvc` · `data-jpa` · `h2-database`
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

## Spring Security

- **SecurityFilterChain** — бин `HttpSecurity`; несколько цепочек с `securityMatcher`; `SecurityContextHolder` — `Authentication` текущего потока
- **Authentication** — `principal` (String → `UserDetails` после auth) · `credentials` (очищается) · `authorities` · `authenticated`; impl: `UsernamePasswordAuthenticationToken` · `JwtAuthenticationToken` · `OAuth2AuthenticationToken`
- **AuthenticationManager** → `ProviderManager` → `AuthenticationProvider`; `DaoAuthenticationProvider` — `UserDetailsService.loadUserByUsername()` + `PasswordEncoder`
- **UserDetails** — `username` · `password` · `authorities` · флаги (`enabled` · `accountNonExpired` · `accountNonLocked`); в проекте: `AuthUser` (credentials, `auth/`) ≠ `User` (profile, `user/`)
- **AuthorizationManager** — allow/deny (Security 6); `@PreAuthorize` / `@PostAuthorize`
- **Resource Server:** `JwtDecoder` + `JwtAuthenticationConverter` → `GrantedAuthority`
- **Auth Server (`auth/`):** `OAuth2AuthorizationServerConfigurer` · `RegisteredClientRepository` · `OAuth2AuthorizationService`

---

## Null Safety

- `@NullMarked` (JSpecify) через `package-info.java` в каждом пакете — non-null по умолчанию
- `org.springframework.lang` — deprecated с Framework 7, не использовать
- Enforcement: IntelliJ 2025.3+ (Java 17); NullAway требует JDK 21.0.8+
