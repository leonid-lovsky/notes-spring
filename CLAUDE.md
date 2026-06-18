# CLAUDE.md — notes-spring

> Читается автоматически в начале каждой сессии.
> Детали, история, справочники — в `~/.claude/projects/…/memory/`.
> Последнее обновление: 2026-06-18 (9)

---

## Правила

- **Язык** — общение на русском; код, идентификаторы, комментарии — на английском
- **Файлы** — не изменять и не создавать без явного «измени X в файле Y»
- **Коммиты** — не коммитить и не пушить без явного запроса
- **CI** — не трогать `.github/workflows/` без явного запроса
- **Подход** — читать память перед ответом; называть текущий приоритет, не отложенное
- **Решение проблем** — сначала: актуальна ли проблема? кто выигрывает? не упускаем ли что-то?  
  затем: варианты с плюсами/минусами + склонение → ждать выбора → обосновать  
  не предлагать реализационные задачи пока открыты архитектурные вопросы
- **CLAUDE.md** — в приоритете над памятью; обновлять при каждом изменении проекта или принципов  
  всё, что пользователь просит запомнить — отражать здесь
- **Перед коммитом** — рефакторинг CLAUDE.md → синхронизация памяти → обновление даты
- **≤ 400 строк** — при превышении удалять второстепенное (детали реализации в первую очередь)

---

## Задачи

```
ГОТОВО      user/ · note/ · user-note/  — domain · service · webmvc · data-jpa
ТЕКУЩИЙ     auth/  — Spring Authorization Server + AuthUser + OIDC
НЕ СОЗДАНО  bff/ · thymeleaf/ · auth/webmvc/ · auth/data-jpa/ · user-note/feign/ · crud/
```

### Прямо сейчас (по возрастанию сложности)

1. `UserNoteService.update()` — лишний read; заменить на `existsByUserIdAndNoteId` (1 строка)
2. `@ControllerAdvice` + `ProblemDetail` (RFC 9457) — убрать дублирующий `@ExceptionHandler` из каждого контроллера
3. Доменные исключения — заменить `NoSuchElementException` как сигнал 404

### Требует решения перед `auth/`

1. **`password` в `user/`** — `User` содержит пароль, но `user/` — Resource Server, не IdP
   - Вариант 1 — убрать из `User`; пароль хранит только `auth/` в `AuthUser`
   - Вариант 2 — оставить; `auth/` делегирует проверку в `user/` через `UserDetailsService`
2. **`auth/`** — Authorization Server + AuthUser + OIDC ← **ТЕКУЩИЙ ПРИОРИТЕТ**

### Далее (последовательно)

1. Resource Server в `user/` · `note/` · `user-note/`; убрать `userId` из `UserNoteRequest` (берётся из JWT)
2. `bff/` — OAuth2 Client + Spring Session + Token Exchange
3. `thymeleaf/` — server-rendered BFF
4. Banking Phase 2 — MFA, token rotation, audit log
5. `user-note/feign/` — OpenFeign
6. `crud/` — shared library
7. Широкий стек — после выбора стратегии активации

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
BUSINESS      user/        Resource Server  (User: id, username, email, password*)
              note/        Resource Server  (Note: id, content)
              user-note/   Resource Server  (UserNote: userId, noteId, role)
INFRA         config/      Spring Cloud Config Server
              registry/    Eureka Server
EXTERNAL      Redis        Spring Session backing (bff/ + thymeleaf/ при масштабировании)
              PostgreSQL×N по одной на: auth, user, note, user-note
              Kafka/MQ     только при событийной регистрации (решение не принято)
```

> `*` — `password` в `User` временный; решение открыто (см. Задачи п.4)

---

## Открытые решения

Откладываются при взаимозависимости или преждевременности.

### Блокеры

**Регистрация** — стратегия координации `auth/` ↔ `user/` ← **отложено**
- Lazy: `user/` создаёт профиль при первом запросе; нет email до явного обновления
- Sync: `auth/` → `user/` через RestClient; нарушает direction of dependencies
- Events: Kafka/RabbitMQ; архитектурно чисто; требует брокера

### Дизайн HTTP-слоя (после Resource Server)

- **Mapping** — где маппить между слоями; ручной / MapStruct; отдельные DTO/VO на каждом слое
- **PATCH** — только в `service/` (полный объект → `replace`) / в output port / не поддерживать
- **Возврат из service/** — доменный объект / `void` для мутирующих; CQS / CQRS на уровне input port

### Отложено

- **NoteVisibility** — в `note/domain/` или `user-note/domain/` (требует ясности по ACL)
- **Reactive/sync impedance** — параллельные порты в `domain/` / sync обёртка / отдельные сервисы
- **Стратегия активации адаптеров** — Spring Profiles / отдельные `application-jpa/`, `application-r2dbc/`
- **Название output adapter при нескольких реализациях** — `NoteOutputAdapter` / `NoteJpaOutputAdapter`

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

- `starter-web` → `starter-webmvc` · `starter-test` → индивидуальные `*-test`
- Jackson 3: `com.fasterxml.jackson` → `tools.jackson`
- RestTemplate deprecated → `RestClient`
- Flyway + PostgreSQL: нужен `runtimeOnly 'org.flywaydb:flyway-database-postgresql'`
- Обработка ошибок: `ResponseEntityExceptionHandler` + `ProblemDetail` (RFC 9457)
- Lombok: `compileOnly` + `annotationProcessor`; `@Data`/`@EqualsAndHashCode` запрещены на entities

### Spring Security

**Цепочка:** `SecurityFilterChain` → `AuthenticationManager` → `ProviderManager` → `DaoAuthenticationProvider` → `UserDetailsService` + `PasswordEncoder`  
**Resource Server** — `JwtDecoder` + `JwtAuthenticationConverter` → `GrantedAuthority`  
**Auth Server** — `OAuth2AuthorizationServerConfigurer` · `RegisteredClientRepository`

### Null Safety и стиль

**`@NullMarked`** (JSpecify) через `package-info.java` — non-null по умолчанию; `org.springframework.lang` deprecated  
**Импорты** — `com.example.*` + `org.*` / `jakarta.*`, затем `java.*`; wildcard при 3+  
**Имена полей** — camelCase от типа: `noteOutputPort`, `noteJpaRepository`; не `repository`, не `port`  
**Пустое тело** — одна пустая строка · **EOF** — один `\n`
