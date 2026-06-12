# CLAUDE.md — notes-spring

> Читается автоматически в начале каждой сессии.
> Детали, история, справочники — в `~/.claude/projects/…/memory/`.
> Последнее обновление: 2026-06-12 (5)

---

## Правила

- **Язык** — общение на русском; код, идентификаторы, комментарии — на английском
- **Файлы** — не изменять и не создавать без явного «измени X в файле Y»
- **Коммиты** — не коммитить и не пушить без явного запроса
- **CI** — не трогать `.github/workflows/` без явного запроса
- **Подход** — читать память перед ответом; взвешивать варианты, проверять соответствие принципам
- **Решение проблем** — сначала: актуальна ли проблема? кто выигрывает? не упускаем ли что-то? не является ли текущее решение правильным?  
  затем: варианты с плюсами/минусами + склонение → ждать выбора → обосновать  
  не предлагать реализационные задачи пока открыты архитектурные вопросы
- **CLAUDE.md** — в приоритете над памятью; обновлять при каждом изменении проекта или принципов  
  всё, что пользователь просит запомнить — отражать здесь
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

**Регистрация** — не выбрана стратегия координации между `auth/` и `user/`: ← **отложено: сложная и неоднозначная; ни один вариант не очевиден**
- Lazy: `user/` создаёт профиль при первом запросе; нет email до явного обновления
- Sync: `auth/` → `user/` через RestClient; нарушает direction of dependencies
- Events: Kafka/RabbitMQ; архитектурно чисто; требует брокера

### Ожидают решения

**Название output port** — не решено; возможные варианты:
- `NoteRepository` — DDD/Spring Data конвенция; несёт чужой контекст
- `NoteStore` — нейтрально, без фреймворк-коннотаций (склонение)
- `NoteCollection` — максимально соответствует семантике; конфликт с `java.util.Collection`
- `NoteGateway` — явный hexagonal термин; читается неестественно для persistence
- `NoteDao` · `NotePersistence` · `NoteOutputPort` · `Notes` · `NoteAccess` · `NoteProvider` — и другие

**PATCH / частичное обновление** — нужен ли отдельный метод в output port:
- Вариант 1 — только в `service/` (input port): сервис строит полный объект → `replace`; output port не меняется
- Вариант 2 — `void patch(UUID id, NoteFields fields)` в output port; адаптер решает как обновить частично
- Вариант 3 — не поддерживать PATCH; только PUT (полная замена) на HTTP-уровне

**NoteVisibility** — в `note/domain/` или `user-note/domain/` ← **отложено: требует ясности по ACL-дизайну**

**Reactive/sync impedance** — sync порт несовместим с реактивными адаптерами ← **отложено: фундаментально, влияет на `domain/`**
- Вариант 1 — параллельные порты в `domain/` (склонение): `NoteRepository` + `ReactiveNoteRepository`
- Вариант 2 — sync `domain/`, обёртка `Mono.fromCallable().subscribeOn(boundedElastic())`
- Вариант 3 — отдельные реактивные сервисы; дублирование домена

**Стратегия активации адаптеров** ← **отложено: зависит от Reactive/sync impedance**
- Spring Profiles (`@Profile("jpa")`) — просто, но грубо
- Отдельные `application-jpa/` / `application-r2dbc/` модули — чисто, но дублирует composition root

### Порядок реализации

1. Название output port + PATCH решение ← **ТЕКУЩИЙ ПРИОРИТЕТ**
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

| Модель                    | Driving                               | Driven                                    |
|---------------------------|---------------------------------------|-------------------------------------------|
| Sync + Virtual Threads    | `webmvc/`, `shell/`, `batch/`         | `data-jpa/`, `data-jdbc/`, `jooq/`        |
| Reactive (Project Reactor)| `webflux/`, `rsocket/`                | `data-r2dbc/`, `data-mongodb-rx/`         |
| Async messaging           | `kafka/` consumer, `amqp/` consumer   | `kafka/` producer, `amqp/` producer       |

WebFlux + Virtual Threads несовместимы → один `application/` собирает одну модель.  
Полные таблицы стартеров адаптеров — в `memory/reference_spring_starters.md`.

### Семантика output port

Output port определяется доменом, не адаптером. Говорит на языке домена, не хранилища.

**Допустимые пространства:** DDD/Коллекции (`add`, `replace`, `remove`) · CRUD (`create`, `update`, `delete`)  
**Недопустимы:** JPA (`persist`, `merge`) · SQL (`INSERT`, `UPDATE`) · HTTP (`POST`, `PUT`) · Document store  
**Выбор:** коллекционная семантика — порт как in-memory коллекция агрегатов (Evans); UUID как ключ

```java
boolean existsById(UUID id);       // containsKey
Optional<Note> findById(UUID id);  // get
List<Note> findAll();              // values
void add(Note note);               // add to collection
void replace(Note note);           // replace element (full)
void remove(UUID id);              // remove from collection
```

`void` для мутирующих: UUID генерируется до вызова порта, адаптер ничего не обогащает.  
`replace` — полная замена; PATCH решается в `webmvc/` + `service/`.  
Upsert (`save`) — только для sync/offline-first; сервер — источник истины.

### Принятые решения

- **Gateway ≠ BFF** — `gateway/` stateless edge; `bff/` и `thymeleaf/` — отдельные stateful-сервисы
- **BFF — один на UX** — один BFF на тип устройства, не общий
- **Token Exchange (RFC 8693)** — BFF обменивает access token на internal JWT с `aud` микросервиса
- **Spring Authorization Server — постоянный IdP** — Keycloak/Auth0 только после детальной оценки
- **OpenFeign для `user-note/feign/`** — `spring-cloud-starter-openfeign`
- **`@GeneratedValue` не используется** — UUID генерируется в `service/`
- **`service/` модуль** — use case implementations; зависит только от `domain/`; не реализовывать до закрытия названия порта и PATCH
- **Input port — интерфейс в `domain/`** — `NoteUseCase` / `UserUseCase` / `UserNoteUseCase`; `webmvc/` зависит от интерфейса, не от `service/`
- **Domain objects — Java records** — `withXxx()` для изменённой копии; JPA entities — обычные классы
- **Контроллеры: `ResponseEntity<T>` везде** — статусы явно через `HttpStatus`
- **`existsById` в output port** — валидный паттерн; не заменять на `findById`

---

## Безопасность

> `identity ≠ profile ≠ business ≠ permissions`

**Zero Trust** — каждый слой валидирует JWT самостоятельно  
**JWT claims** — только стандартные: `sub` · `iss` · `aud` · `exp` · `jti` · `acr` · `amr` · `scope`  
**Banking-grade** — фаза 1: основа · фаза 2: MFA + token rotation · фаза 3: DPoP + mTLS

| Слой            | Модуль                | Ответственность                              |
|-----------------|-----------------------|----------------------------------------------|
| Edge            | `gateway/`            | Routing, rate limiting, TLS                  |
| BFF             | `bff/` · `thymeleaf/` | OAuth2 login flow, session, Token Exchange   |
| IdP             | `auth/`               | JWT issuing, credentials, OIDC               |
| Resource Server | `*/webmvc/`           | JWT validation per request                   |
| ACL             | `user-note/`          | `UserNote { userId, noteId, role }`          |

**UserNoteRole:** `OWNER` · `EDITOR` · `COMMENTER` _(не MVP)_ · `VIEWER`  
**NoteVisibility:** `RESTRICTED` · `ANYONE_WITH_LINK` + role · `PUBLIC` + role  
**Клиенты:** Browser/Thymeleaf (AuthCode+Session) · Browser/SPA (AuthCode+BFF) · Mobile (PKCE) · B2B (ClientCredentials)

---

## Принципы

**Hexagonal Architecture** — главный принцип; все решения проверяются на соответствие  
**SoC / SRP** — на всех уровнях; **Видимость** — `default` или минимально необходимая  
**No partial abstractions** — полное устранение или явное дублирование  
**Twelve-Factor:** III Config · VI Stateless · IX Graceful shutdown · X Testcontainers · XI stdout · XII Flyway  
**Остальные** — SOLID · KISS · DRY · SSOT · Law of Demeter · Fail Fast

---

## Gradle

**`buildSrc`** + convention plugins (Groovy DSL) — единственный механизм; flat, без вложенности  
**Нет:** root `build.gradle` · `buildSrc/settings.gradle` · `libs.versions.toml`  
**Порядок блоков:** `plugins` → `java` → `repositories` → [`ext`] → `dependencies` → `dependencyManagement` → `test`  
**Порядок зависимостей:** `domain` → `service` → `webmvc` → `data-jpa`  
**`settings.gradle`:** `gateway` → `config` → `registry` → `auth` → `user` → `note` → `user-note`; внутри: `application` → `domain` → `service` → `webmvc` → `data-jpa`  
**Convention plugins:** ✅ `application` · `domain` · `webmvc` · `data-jpa` · `h2-database` | ❌ планируется ≈16 плагинов  
**`domain`-plugin** — только `java`; без Spring BOM; JSpecify и JUnit с явными версиями

---

## Spring Boot 4

**Ключевые изменения:**
- `starter-web` → `starter-webmvc` · `starter-test` → индивидуальные `*-test`
- Jackson 3: `com.fasterxml.jackson` → `tools.jackson`
- RestTemplate deprecated → `RestClient`; `dependency-management` не применяется автоматически

**Flyway + PostgreSQL** — нужен `runtimeOnly 'org.flywaydb:flyway-database-postgresql'`  
**Обработка ошибок** — `ResponseEntityExceptionHandler` + `ProblemDetail` (RFC 9457)  
**Lombok** — `compileOnly` + `annotationProcessor`; `@Data`/`@EqualsAndHashCode` запрещены на entities

---

## Spring Security

**Цепочка:** `SecurityFilterChain` → `AuthenticationManager` → `ProviderManager` → `DaoAuthenticationProvider` → `UserDetailsService` + `PasswordEncoder`  
**Resource Server** — `JwtDecoder` + `JwtAuthenticationConverter` → `GrantedAuthority`  
**Auth Server** — `OAuth2AuthorizationServerConfigurer` · `RegisteredClientRepository`  
**В проекте:** `AuthUser` (`auth/`) ≠ `User` (`user/`)

---

## Null Safety и стиль

**`@NullMarked`** (JSpecify) через `package-info.java` — non-null по умолчанию; `org.springframework.lang` deprecated  
**Импорты** — `com.example.*` + `org.*` / `jakarta.*`, затем `java.*`; wildcard при 3+  
**Имена полей** — полные: `noteRepository`, не `repository` · **Пустое тело** — одна пустая строка · **EOF** — один `\n`
