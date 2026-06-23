# CLAUDE.md — notes-spring

> Живой документ проекта. Читается автоматически в начале каждой сессии.
> Все решения — временные и подлежат уточнению и изменению по мере необходимости.
> Последнее обновление: 2026-06-23T19:02Z

---

## Правила

### Начало каждой сессии

1. Прочитать этот файл полностью
2. **Текущий приоритет:** `auth/` — все архитектурные вопросы по Google Docs ACL закрыты
3. Актуализировать файл: убрать устаревшее, улучшить формулировки, устранить избыточность
4. Зафиксировать изменения коммитом и пушем _(постоянная авторизация, явный запрос не требуется)_

### Поведение

- **Язык** — общение на русском; код, идентификаторы, комментарии в коде — на английском
- **Формулировки** — любое сообщение пользователя — черновик; длина и стиль значения не имеют; ассистент всегда переформулирует его точно, грамотно и полно — в ответах и при записи в этот файл
- **Фокус** — в начале ответа называть текущую задачу из раздела Задачи; не предлагать отложенные задачи без запроса
- **Архитектурные решения** — перед ответом проверять: актуальна ли проблема? кто выигрывает? не упускаем ли что-то важное? Далее: изложить варианты с плюсами и минусами, дать склонение, дождаться выбора, обосновать. Не предлагать реализацию, пока открыты архитектурные вопросы
- **Единственный источник истины** — этот файл работает на любой машине и в любой сессии; локальная память — лишь кэш, недоступный другим пользователям и машинам. Всё важное фиксируется здесь: принципы, решения, договорённости, правила поведения

### Ограничения

- **Файлы** — не изменять и не создавать без явного указания («измени X в файле Y»)
- **Таблицы** — выравнивать колонки по ширине: каждая строка таблицы должна иметь одинаковую длину в Unicode-символах; несоответствие вызывает предупреждение IDEA
- **CI** — не изменять `.github/workflows/` без явного запроса
- **Коммиты** — не коммитить и не пушить без явного запроса _(исключение: начало сессии)_
- **Перед коммитом** — обновить дату: `date -u +"%Y-%m-%dT%H:%MZ"`

---

## Задачи

```
ГОТОВО      user/ · note/ · user-note/  — domain · service · webmvc · data-jpa
ТЕКУЩИЙ     auth/  — Spring Authorization Server + AuthUser + OIDC
НЕ СОЗДАНО  bff/ · thymeleaf/ · auth/webmvc/ · auth/data-jpa/ · sharing/ · crud/
```

### Чистка перед `auth/`

1. Убрать `password` из `User` — `AuthUser ≠ User` уже принято; `user/` — профиль, не IdP
2. Доменные исключения — заменить `NoSuchElementException` как сигнал 404
3. `@ControllerAdvice` + `ProblemDetail` (RFC 9457) — убрать локальный `@ExceptionHandler` из каждого контроллера
4. `UserNoteService.update()` → `existsByUserIdAndNoteId` — лишний read

### `auth/` ← **ТЕКУЩИЙ ПРИОРИТЕТ**

> Скелет `auth/application/` уже создан (`AuthApplication.java`, пустой тест, пустой `application.properties`).
> Модули `domain/`, `data-jpa/` и сам Authorization Server — не созданы.

1. `auth/domain/` — `AuthUser` record + `AuthUserUseCase` + `AuthUserRepository`
2. `auth/data-jpa/` — JPA adapter + Flyway schema
3. Spring Authorization Server — OIDC endpoint + JWT issuer

### После `auth/`

1. Resource Server в одном сервисе — smoke test: `auth/` выдаёт токен, сервис принимает
2. Resource Server во всех сервисах; убрать `userId` из `UserNoteRequest` (берётся из JWT `sub`)
3. `sharing/` — Google Docs ACL сервис (после закрытия трёх открытых вопросов о домене):
   - `sharing/domain/` — `NoteAccess`, `NotePublication`, use cases: `effectiveRole`, `share`, `transferOwnership`, `publish`
   - `sharing/service/` — бизнес-логика; координирует `NoteAccess` + `NotePublication` + вызовы к `user-note/` и `note/`
   - `sharing/webmvc/` — REST API
   - `sharing/data-jpa/` — хранение `NoteAccess` и `NotePublication`
   - `sharing/feign/` — клиенты к `note/` и `user-note/`
4. `bff/` — OAuth2 Client + Spring Session + Token Exchange
5. `thymeleaf/` — server-rendered BFF
6. Banking Phase 2 — MFA, token rotation, audit log
7. `crud/` — shared library
8. Широкий стек — один use case через WebMVC + gRPC + GraphQL; цель: доказать, что домен не зависит от протокола и хранилища

---

## Открытые решения

> Не реализовывать до принятия явного решения.

### Google Docs ACL

> Все архитектурные вопросы закрыты. Реализация после `auth/`.

### После Resource Server

- **Регистрация** — координация `auth/` ↔ `user/`; решить до реализации:
  - **Lazy** — `user/` создаёт профиль при первом запросе; нет email при регистрации; нет coupling
  - **Sync** — `auth/` → `user/` через RestClient; нарушает direction of dependencies
  - **Events** — Kafka/RabbitMQ; единственный вариант без нарушения SoC/SRP; требует брокера
- **Mapping** — где маппить между слоями; ручной / MapStruct; отдельные DTO/VO на каждом слое
- **PATCH** — решение открыто: обрабатывать в `service/` (fetch → modify → replace) или не поддерживать вовсе
- **Возврат из use case** — доменный объект / `void` для мутирующих; CQS / CQRS на уровне input port

### Отложено (фундаментальное)

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

> Spring Boot 4 monorepo — banking-grade с первого дня

| Инструмент                   | Целевая версия | Сейчас   |
|------------------------------|----------------|----------|
| Java                         | 17             | 17       |
| Gradle                       | 9.6.0          | 9.5.1    |
| Spring Boot                  | 4.1.0          | 4.0.6    |
| Spring Cloud                 | 2025.1.2       | 2025.1.1 |
| Spring Dependency Management | 1.1.7          | 1.1.7    |

> Обновить: `buildSrc/build.gradle.kts` (val-константы) + `gradle/wrapper/gradle-wrapper.properties`.

---

## Принципы

> Применяются с первого дня. Код, архитектура, тесты, деплой проектируются так, чтобы менять backing service (H2 → PostgreSQL, local → AWS) без правок в `domain/` и `service/`.

### Четыре приоритетных критерия

Каждое архитектурное решение проверяется по всем четырём:

- **Hexagonal Architecture** — ports & adapters; изоляция ядра от инфраструктуры
- **Clean Architecture** — зависимости направлены внутрь; use cases в центре; framework — деталь
- **SOLID** — SRP · OCP · LSP · ISP · DIP; на уровне классов, модулей и сервисов
- **Separation of Concerns** — каждый сервис / модуль отвечает за одну зону ответственности

### Классический принцип декомпозиции

**High Cohesion / Low Coupling** — основа всех решений о границах сервисов и модулей.

> «Куда добавить новую функциональность?» → туда, где она создаёт наибольшую связность (cohesion) и наименьшую зависимость (coupling).

Применяется для: `gateway` · `bff` · `auth` · `sharing` · `user` · `note` · `user-note` · любого нового сервиса.

Следствия:
- **Common Closure Principle** — в один сервис собирается то, что изменяется вместе и по одной причине
- **Acyclic Dependencies** — граф зависимостей между сервисами ацикличен (DAG); циклы запрещены

### Паттерны на рассмотрение

Инструменты реализации, применяемые по необходимости; не подменяют четыре основных критерия:

- **Bounded Contexts (DDD)** — явные границы доменов; определяет, кто владеет какими данными
- **Domain Events** — коммуникация между BC без прямой зависимости; альтернатива Feign для некритичных операций
- **Defense in Depth** — безопасность на нескольких слоях; для этого проекта: BFF + сетевая изоляция
- **CQRS** — разделение read/write моделей; актуально для `sharing/` (`effectiveRole` — query; `share`/`transferOwnership` — command)
- **SAGA** — распределённые транзакции без двухфазного коммита; актуально для `transferOwnership` (`sharing/` + `user-note/`)

### Остальные принципы кодовой базы

**No partial abstractions** · **Twelve-Factor** · **KISS** · **DRY** · **SSOT** · **Law of Demeter** · **Fail Fast**

---

## Архитектура

Структура модулей:

```
application/  Spring Boot app — composition root; знает все модули
domain/       entities + port interfaces (UseCase + Repository) — чистая Java, без Spring
service/      use case implementations (@Service, @Transactional) — зависит только от domain/
webmvc/       driving adapter (HTTP/REST)      →  domain/
grpc/         driving adapter (gRPC/Protobuf)  →  domain/
graphql/      driving adapter (GraphQL)        →  domain/
data-jpa/     driven adapter  (JPA/SQL)        →  domain/
feign/        driven adapter  (HTTP client)    →  domain/  (sharing/feign/)
```

**Dependency direction** — `application/` знает всё; адаптеры знают только `domain/`; `domain/` — ничего снаружи
**Database per service** — cross-service JOIN запрещён
**Stateless** — `gateway/` · `config/` · `registry/` · `user/` · `note/` · `user-note/`
**Stateful** — `bff/` · `thymeleaf/` → Spring Session; `auth/` → OAuth2Authorization в PostgreSQL

### Тестирование (пирамида)

| Модуль         | Тест-слой                | Что проверяет                                      |
|----------------|--------------------------|----------------------------------------------------|
| `domain/`      | JUnit (чистая Java)      | Доменная логика без Spring context                 |
| `service/`     | Spring context + Mockito | Use case; Repository мокируется                    |
| `webmvc/`      | `@WebMvcTest` (MockMvc)  | HTTP binding, статусы, сериализация                |
| `data-jpa/`    | `@DataJpaTest` + TC      | SQL, маппинг; Testcontainers = реальный PostgreSQL |
| `application/` | `@SpringBootTest` + TC   | Полный smoke test; все слои вместе                 |

**ArchUnit** — архитектурные тесты в CI; проверяет, что `domain/` не импортирует из адаптеров.

### Семантика репозиториев

`*Repository` в `domain/` говорит на языке домена. Коллекционная семантика (Evans):

```java
boolean existsById(UUID id);       // containsKey
Optional<Note> findById(UUID id);  // get
List<Note> findAll();              // values
void add(Note note);               // put
void replace(Note note);           // replace (full)
void remove(UUID id);              // remove
```

UUID генерируется в `service/` до вызова порта. `replace` — полная замена; PATCH решается в `webmvc/` + `service/`.

### Принятые решения

- **Gateway ≠ BFF** · **BFF — один на UX** (Sam Newman)
- **Token Exchange (RFC 8693)** — BFF обменивает access token на internal JWT с `aud` микросервиса
- **Spring Authorization Server — постоянный IdP**; Keycloak/Auth0 только после детальной оценки
- **OpenFeign** — `spring-cloud-starter-openfeign` для `sharing/feign/`
- **Input port — интерфейс в `domain/`** — `webmvc/` зависит от интерфейса, не от `service/`
- **Domain objects — Java records** — `withXxx()` для изменённой копии; JPA entities — обычные классы
- **`existsById` в Repository** — валидный паттерн; не заменять на `findById`
- **Именование** — `*UseCase` (input port) · `*Repository` (output port, `domain/`) · `*JpaRepository` (Spring Data, `data-jpa/`) · `*OutputAdapter` (driven adapter)
- **`ResponseEntity<T>` везде** — статусы явно через `HttpStatus`
- **`AuthUser` (`auth/`) ≠ `User` (`user/`)**
- **Wire format в адаптере** — `.proto` в `grpc/`, `.graphqls` в `graphql/`; Protobuf/GraphQL типы не проникают в `domain/`
- **`service/` оправдан и остаётся** — UUID генерируется в `service/` (решение уровня приложения, не БД); `@Transactional` принадлежит use case, не адаптеру; перенос в `data-jpa/` дал бы двойную роль (output port + input port); подтверждено сценариями `share()` и `transferOwnership()` в `sharing/`
- **`user/` · `note/` · `user-note/` — чистые REST CRUD сервисы** — каждый знает только свои данные; любая бизнес-логика поверх CRUD реализуется в `sharing/`, а не внутри самих сервисов
- **`sharing/` — отдельный гексагональный сервис** — реализует всю бизнес-логику Google Docs ACL; вызывает `user-note/` и `note/` через output ports (Feign); CRUD сервисы не знают о `sharing/` вообще
- **Enforcement — BFF + сетевая изоляция** — перед вызовом `note/` BFF проверяет `sharing/effectiveRole`; `note/` — чистый Resource Server (JWT, без ACL); Token Exchange — ответственность `auth/` + `bff/`; сетевая изоляция исключает прямой доступ к CRUD-сервисам в обход BFF
- **`NoteVisibility` — НЕ в `note/domain/`** — `note/` не знает о своей видимости; принадлежит `sharing/` или отдельной сущности в его домене
- **Google Docs ACL модель** — принята как целевая модель доступа:
  - `UserNote { userId, noteId, role }` — явные права; `UserNoteRole`: `OWNER · EDITOR · COMMENTER · VIEWER`
  - **Share with others:**
    - People with access — явные `UserNote` записи
    - General access: `RESTRICTED` (только явные) · `ANYONE_WITH_LINK` (viewer / commenter / editor)
    - Settings: `editorsCanShare` (разрешить редакторам менять права и делиться); `canDownloadCopyPrint` (editors / commenters+viewers)
  - **Publish to web** — отдельная концепция, не link sharing:
    - Link publish · Embed publish
    - `autoRepublish` — автоматически переопубликовывать при изменениях
  - **`effectiveRole(userId, noteId)`**: явная `UserNote` → иначе general access → deny
  - **`share(callerId, noteId, targetUserId, role)`**: caller = OWNER (или EDITOR при `editorsCanShare`); role ≤ роли caller'а
  - **`transferOwnership(callerId, noteId, newOwnerId)`**: атомарно — старый OWNER → EDITOR, новый → OWNER
  - Вся логика живёт в `sharing/service/`; `sharing/feign/` вызывает `note/` и `user-note/`
- **Один `OWNER` на заметку** — доменный инвариант в `sharing/`; два `OWNER` одновременно = невалидное состояние; `transferOwnership` атомарно: старый OWNER → EDITOR, новый → OWNER
- **`NoteAccess`** — сущность в `sharing/domain/`: `{ noteId, generalAccess, editorsCanShare, canDownloadCopyPrint }`; `generalAccess`: `RESTRICTED · VIEWER · COMMENTER · EDITOR` (роль для «anyone with the link»)
- **`NotePublication`** — отдельная сущность в `sharing/domain/`: `{ noteId, linkPublished, linkAutoRepublish, embedPublished, embedAutoRepublish }`; «publish to web» ≠ «share with link» — разные причины меняться (SRP)

---

## Сервисы

```
EDGE          gateway/     Spring Cloud Gateway — stateless, routing + rate limiting
PRESENTATION  bff/         OAuth2 Client + Spring Session + Token Exchange (SPA)
              thymeleaf/   Thymeleaf + OAuth2 Client — self-contained BFF
IDENTITY      auth/        Spring Authorization Server — OIDC-compliant, JWT issuer
BUSINESS      user/        Resource Server  (User: id, username, email) — чистый REST CRUD
              note/        Resource Server  (Note: id, content) — чистый REST CRUD
              user-note/   Resource Server  (UserNote: userId, noteId, role) — чистый REST CRUD
              sharing/     Resource Server  (Google Docs ACL бизнес-логика: effectiveRole, share, transferOwnership, publish)
INFRA         config/      Spring Cloud Config Server
              registry/    Eureka Server
EXTERNAL      Redis        JTI Blocklist + Spring Session (bff/ + thymeleaf/)
              PostgreSQL×N по одной БД на: auth, user, note, user-note, sharing
              Kafka/MQ     только при событийной регистрации (решение не принято)
```

> **MVP** — та же архитектура и код; H2 вместо PostgreSQL, `MapSessionRepository` вместо Redis.
> Backing services подключаются при реальной потребности, не как архитектурный шаг.

---

## Безопасность

> `identity ≠ profile ≠ business ≠ permissions` — смешивание нарушает SoC и усложняет смену IdP

**Zero Trust** — каждый слой валидирует JWT самостоятельно
**JWT claims** — только стандартные: `sub` · `iss` · `aud` · `exp` · `jti` · `acr` · `amr` · `scope`
**Браузер не видит JWT** — только HttpOnly session cookie в `bff/` · `thymeleaf/` (защита от XSS)

| Слой            | Модуль                | Ответственность                            |
|-----------------|-----------------------|--------------------------------------------|
| Edge            | `gateway/`            | Routing, rate limiting, TLS                |
| BFF             | `bff/` · `thymeleaf/` | OAuth2 login flow, session, Token Exchange |
| IdP             | `auth/`               | JWT issuing, credentials, OIDC             |
| Resource Server | `*/webmvc/`           | JWT validation per request                 |
| ACL             | `sharing/`            | effectiveRole, share, transferOwnership    |

**UserNoteRole:** `OWNER` · `EDITOR` · `COMMENTER` _(не MVP)_ · `VIEWER`
**General access:** `RESTRICTED` · `ANYONE_WITH_LINK` (viewer / commenter / editor)
**Publish to web:** link · embed; `autoRepublish` — отдельная концепция, не link sharing
**ACL resolution:** явная `UserNote` → иначе general access → deny; логика в `sharing/service/`

### Клиенты

| Клиент               | Grant Type         | Хранит токен         | BFF          |
|----------------------|--------------------|----------------------|--------------|
| Browser / Thymeleaf  | Authorization Code | Серверная сессия     | Сам себе BFF |
| Browser / SPA        | Authorization Code | Серверная сессия BFF | `bff/`       |
| Mobile (Android/iOS) | AuthCode + PKCE    | Keychain / Keystore  | Нет          |
| B2B / CLI            | Client Credentials | Не хранит            | Нет          |

### Аутентификация

**Все методы → единый JWT** с `acr` (1 = single factor, 2 = MFA) и `amr` (pwd / google / passkey / totp)
**Social Login** — `auth/` сам OAuth2 Client к Google/GitHub; их токен никогда не покидает `auth/`
**MFA** — `@EnableMultiFactorAuthentication` (Spring Security 7) + `FactorGrantedAuthority`
**Step-up auth** — `/oauth2/authorize?acr_values=2` → MFA challenge → новый токен с `acr=2`

### Токены

**Flow:** User → `auth/` → access_token → BFF Token Exchange (RFC 8693) → internal JWT (`aud`=сервис) → Microservice
**access_token** 15 мин · **refresh_token** 30–90 дней; хранится в сессии BFF или Keychain/Keystore; в микросервисы не отправляется
**Rotation** — каждый refresh → новый refresh_token; повторное использование → revoke вся семья → re-login
**JTI Blocklist** — Redis `SET jti:{jti} "revoked" EX ttl`; ~0.5 ms на запрос; logout < 1 с
**Back-channel logout** — `auth/` → POST `bff/logout/connect/back-channel`; при горизонтальном масштабировании BFF требует Redis Session

### Banking-grade фазы

| Фаза         | Содержание                                                                                  |
|--------------|---------------------------------------------------------------------------------------------|
| 1 — основа   | PKCE · `aud`/`scope`/`jti` claims · refresh rotation · rate limiting · TLS 1.3 · stateless  |
| 2 — MFA      | TOTP/Passkey · Social Login · JTI Blocklist · Step-up auth · device tracking · audit log    |
| 3 — максимум | DPoP · mTLS · Certificate pinning · App attestation                                         |

---

## Техническая база

### Gradle

**`buildSrc`** + convention plugins (Kotlin DSL) — единственный механизм; flat, без вложенности
**Файлы:** `buildSrc/build.gradle.kts` (версии плагинов — в `val`-константах); convention plugins — `src/main/kotlin/*.gradle.kts`; субпроекты — `build.gradle` (Groovy, только `id '...'`)
**Нет:** root `build.gradle` · `buildSrc/settings.gradle` · `libs.versions.toml`
**Порядок блоков:** `plugins` → `repositories` → `dependencyManagement` → `dependencies` → `test`
**Порядок зависимостей:** `domain` → `service` → `webmvc` → `data-jpa`
**`settings.gradle`:** `gateway` → `config` → `registry` → `auth` → `user` → `note` → `user-note`; внутри: `application` → `domain` → `service` → `webmvc` → `data-jpa`

**Convention plugins:**

| Plugin ID                                   | Назначение                       |
|---------------------------------------------|----------------------------------|
| `spring-boot-application-conventions`       | `application/` — Boot app        |
| `java-domain-conventions`                   | `domain/` — чистая Java, без BOM |
| `spring-service-conventions`                | `service/` — BOM + spring-tx     |
| `spring-webmvc-adapter-conventions`         | `webmvc/` — driving adapter      |
| `spring-data-jpa-adapter-conventions`       | `data-jpa/` — driven adapter     |
| `spring-h2-database-conventions`            | add-on: H2 + h2console           |
| `spring-oauth2-resource-server-conventions` | add-on: JWT-валидация            |
| `spring-oauth2-client-conventions`          | add-on: OAuth2 Client            |

- **`domain`** — только `java`; без Spring BOM; JSpecify и JUnit с явными версиями
- **`service`** — требует явный `implementation("org.springframework:spring-tx")`; `spring-boot-starter` не тянет его транзитивно
- **`oauth2-resource-server`** — транспортно-независимая JWT-валидация; применим к `webmvc/`, `webflux/`, `graphql/` — не переименовывать в `webmvc-oauth2-*`
- **`h2-database`** — add-on поверх `data-jpa`; не содержит `repositories {}`; применять совместно

### Spring Boot 4

- `starter-web` → `starter-webmvc`
- `starter-test` — JUnit/Mockito/AssertJ; слайсы (`@WebMvcTest`, `@DataJpaTest` и др.) выведены в отдельные `*-test` стартеры (в Boot 3 входили в `starter-test`)
- OAuth2 стартеры: `oauth2-*` (Boot 3) → `security-oauth2-*` (Boot 4); `docs.spring.io/spring-security` ссылается на Boot 3 имена — не доверять
- Jackson 3: `com.fasterxml.jackson` → `tools.jackson`
- RestTemplate deprecated → `RestClient`
- Flyway + PostgreSQL: нужен `runtimeOnly("org.flywaydb:flyway-database-postgresql")`
- Обработка ошибок: `ResponseEntityExceptionHandler` + `ProblemDetail` (RFC 9457)
- Lombok: `compileOnly` + `annotationProcessor`;
  `@Data` / `@EqualsAndHashCode` запрещены на entities — нарушают Hibernate lifecycle;
  `equals()` / `hashCode()` по ID вручную: `hashCode() { return getClass().hashCode(); }`

### Spring Security

**Цепочка:** `SecurityFilterChain` → `AuthenticationManager` → `ProviderManager` → `DaoAuthenticationProvider` → `UserDetailsService` + `PasswordEncoder`
**Resource Server** — `JwtDecoder` + `JwtAuthenticationConverter` → `GrantedAuthority`; минимум: property `spring.security.oauth2.resourceserver.jwt.issuer-uri`
**Auth Server** — `OAuth2AuthorizationServerConfigurer` · `RegisteredClientRepository`; 4 обязательных бина: `SecurityFilterChain` × 2, `UserDetailsService`, `JWKSource`

### Null Safety и стиль

**JSpecify** (`@NullMarked` через `package-info.java`) — non-null по умолчанию; каждый пакет требует своего `package-info.java`. Выбран вместо `org.springframework.lang` (deprecated) и JSR-305 (заброшен).
**`@Nullable`** — `@Target(TYPE_USE)`: `private @Nullable String field`; массивы: `Object @Nullable []` (nullable ссылка), `@Nullable Object[]` (nullable элементы)
**Spring Cloud (2025.1.x)** — ещё не null-safe в `registry/`, `config/`, `gateway/`; при нужде: `@NullUnmarked`
**Импорты** — `com.example.*` + `org.*` / `jakarta.*`, затем `java.*`; wildcard при 3+
**Имена полей** — camelCase от типа: `noteUseCase`, `noteRepository`, `noteJpaRepository`
**Промежуточная переменная** — перед `return` всегда извлекать результат; не inline в `.body()`
**Пустое тело** — одна пустая строка · **EOF** — один `\n`

### Качество и наблюдаемость

**ArchUnit** — `testImplementation("com.tngtech.archunit:archunit-junit5:<version>")`; версию брать с Maven Central; проверяет, что `domain/` не импортирует из адаптеров
**JaCoCo** — встроен в Gradle (`jacoco`), версия не нужна; источник покрытия для SonarQube
**SonarQube** — используется внешне: IDE-плагин, CI pipeline, standalone server, SonarCloud; не добавляется как Gradle-зависимость в проект
**Actuator** — только в `application/`; `management.server.port` — отдельный порт; в Resource Server — отдельный `SecurityFilterChain` для `/actuator/**`
**OWASP Dependency-Check** — `org.owasp.dependencycheck`; artifact: `org.owasp:dependency-check-gradle`; версию брать с Maven Central
**Renovate** — автоматические PR на обновление зависимостей; конфигурируется через `renovate.json`

### CI/CD

Платформы используются последовательно: **GitHub Actions** → **GitLab CI** → **Jenkins**.
Текущий: **GitHub Actions** (`.github/workflows/`) — не трогать без явного запроса.

### Для опыта

Используются последовательно по мере развития проекта:

- **Spring Modulith** — миграция multi-module → modular monolith; Gradle-модули дают более сильную compile-time enforcement
- **jMolecules** — аннотирует архитектурные роли явно: `@DrivingAdapter`, `@AggregateRoot`, `@Repository`
- **Docker · Docker Compose · Kubernetes** — контейнеризация и оркестрация
- **AWS** (ECS Fargate → EKS, RDS, ElastiCache, MSK, ALB, ACM, Secrets Manager, CloudWatch + X-Ray)
- **Elastic Stack** — Elasticsearch + Logstash + Kibana

---

## Развёртывание

> Код не меняется при смене backing service. Меняется только конфигурация и convention plugin (`h2-database` → `data-jpa` + PostgreSQL driver).

| Этап        | Инструменты                       | Backing services                        |
|-------------|-----------------------------------|-----------------------------------------|
| Local / MVP | JVM + H2 + `MapSessionRepository` | Не нужны                                |
| Staging     | Docker + Docker Compose           | PostgreSQL · Redis · Kafka · ELK        |
| Production  | Docker + ECS Fargate → EKS        | AWS managed services                    |

---

## Верифицированные координаты (Boot 4.x / Cloud 2025.1.x)

> Источник правды — `start.spring.io`. Maven Central не является авторитетом для Boot 4 стартеров.

### `implementation`

**`org.springframework.boot`**

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
spring-boot-starter-grpc-client
spring-boot-starter-grpc-server
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

**`org.springframework.cloud`** (2025.1.x)

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
org.postgresql:postgresql
org.postgresql:r2dbc-postgresql
com.h2database:h2
org.flywaydb:flyway-database-postgresql   ← обязателен с spring-boot-starter-flyway + PostgreSQL
org.flywaydb:flyway-mysql
```

### `developmentOnly`

```
org.springframework.boot:spring-boot-devtools
org.springframework.boot:spring-boot-docker-compose
```

### `testImplementation`

**`org.springframework.boot`**

```
spring-boot-starter-test
spring-boot-starter-data-jpa-test
spring-boot-starter-grpc-client-test
spring-boot-starter-grpc-server-test
spring-boot-starter-security-test
spring-boot-starter-security-oauth2-authorization-server-test
spring-boot-starter-security-oauth2-client-test
spring-boot-starter-security-oauth2-resource-server-test
spring-boot-starter-webmvc-test
spring-boot-testcontainers
```

**Testcontainers** (версия управляется Spring Boot BOM)

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

### Прочее

```
# Maven (third-party, явная версия)
org.thymeleaf.extras:thymeleaf-extras-springsecurity6   ← имя "6", даже с Security 7
org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.2
com.tngtech.archunit:archunit-junit5:<version>           ← версию брать с Maven Central

# Gradle plugins (third-party, явная версия)
com.google.protobuf version 0.9.6            ← обязателен для gRPC (кодогенерация из .proto)
org.owasp.dependencycheck version <version>  ← artifact: org.owasp:dependency-check-gradle

# Gradle plugins (встроенные, версия не нужна)
jacoco
```
