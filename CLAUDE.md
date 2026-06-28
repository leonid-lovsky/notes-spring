# CLAUDE.md — notes-spring

> Живой документ проекта. Читается автоматически в начале каждой сессии.
> **Всё в этом документе и в коде — временно.** Любое решение подлежит обсуждению и изменению.
> Последнее обновление: 2026-06-28T04:36Z

---

## Правила

### Начало каждой сессии

1. Прочитать этот файл полностью
2. **Текущий приоритет:** решение по reactive/sync impedance → реализовать адаптеры — data-r2dbc · data-mongodb-reactive · webflux · graphql
3. Актуализировать файл: убрать устаревшее, улучшить формулировки, устранить избыточность
4. Зафиксировать изменения коммитом и пушем _(постоянная авторизация, явный запрос не требуется)_

### Поведение

- **Язык** — общение на русском; код, идентификаторы, комментарии в коде — на английском
- **Формулировки** — любое сообщение пользователя — черновик; ассистент всегда переформулирует его точно, грамотно и полно
- **Фокус** — в начале ответа называть текущую задачу из раздела Задачи; не предлагать отложенные задачи без запроса
- **Архитектурные решения** — проверить актуальность; изложить варианты с плюсами и минусами, дать склонение, дождаться выбора. Не предлагать реализацию, пока открыты архитектурные вопросы
- **Единственный источник истины** — этот файл; локальная память — лишь кэш

### Ограничения

- **Файлы** — не изменять и не создавать без явного указания («измени X в файле Y»)
- **Перед решением о размещении файла** — изучить проект: прочитать все задействованные файлы, понять назначение каждого каталога; только потом предлагать или создавать
- **Build-инструментарий** — думать о сценарии выноса сервиса в отдельный репозиторий: решение должно работать и в монорепо, и после экстракции
- **Таблицы** — выравнивать колонки по ширине: каждая строка одинаковой длины в Unicode-символах
- **CI** — не изменять `.github/workflows/` без явного запроса
- **Коммиты** — не коммитить и не пушить без явного запроса _(исключение: начало сессии)_; «Зафиксируй» = обновить CLAUDE.md → коммит → пуш
- **Перед коммитом** — обновить дату: `date -u +"%Y-%m-%dT%H:%MZ"`

---

## Задачи

```
ГОТОВО      user/ · note/ · user-note/  — domain (DTO + port interfaces) · webmvc · data-jpa · data-mongodb · data-jdbc
            Принципы: крайний ISP · Single Data Flow · DTO в domain · маппер в адаптере · ProblemDetail
ТЕКУЩИЙ     решение по reactive/sync impedance + адаптеры — data-r2dbc · data-mongodb-reactive · webflux · graphql
ОТЛОЖЕНО    инфраструктура — actuator · eureka · config · oauth2 · gateway · logging · monitoring
ОТЛОЖЕНО    auth/  — Spring Authorization Server (реализация в самом конце)
НЕ СОЗДАНО  bff/ · thymeleaf/ · sharing/ · crud/
```

### Адаптеры

Реализовать driven adapters (для каждого сервиса: `user/` · `note/` · `user-note/`):

1. `data-r2dbc/` — реактивный SQL; **сначала принять решение по reactive/sync impedance**
2. `data-mongodb-reactive/` — реактивный MongoDB; та же проблема

Реализовать driving adapters (для каждого сервиса):

1. `webflux/` — реактивный REST (WebFlux); **сначала принять решение по reactive/sync impedance**
2. `graphql/` — GraphQL (Spring for GraphQL)

### Инфраструктура ← ОТЛОЖЕНО

> Только `application/build.gradle` и `*.properties`; `domain/` не затрагивается.

**Корневая идея:** два уровня изоляции:
- **Intra-service** — hexagonal: `domain/` не знает о JPA, MongoDB, WebMVC
- **Inter-service** — convention plugins: бизнес-сервис не знает о Eureka, Config, Gateway, OAuth2

| Компонент  | MVP                      | Замена                              | Что меняется              |
|------------|--------------------------|-------------------------------------|---------------------------|
| Config src | native (classpath)       | Git-репо                            | одно property в `config/` |
| Logging    | plain text               | JSON (Loki/ELK)                     | `logback-spring.xml`      |
| Monitoring | Prometheus via Actuator  | Grafana LGTM / Datadog / Boot Admin | конфиг, не код            |
| OAuth2     | resource server (плагин) | другой IdP                          | `issuer-uri` property     |

**Порядок реализации:**

1. `spring-boot-actuator-conventions` — добавить во все бизнес-сервисы
2. `spring-cloud-eureka-client-conventions` + `spring-cloud-config-client-conventions`
3. **Config Server** — `native` (classpath); конфиги в `config/src/main/resources/config/`
4. **OAuth2 Resource Server** — плагин; `issuer-uri=http://localhost:9000` как placeholder
5. **Logging** — `logback-spring.xml` с профилем `json` для prod
6. **Monitoring** — `compose.yaml` с `grafana/otel-lgtm`; Prometheus через Actuator + `micrometer-registry-prometheus`
7. **Gateway routing** — маршруты к `user/`, `note/`, `user-note/`

### После адаптеров

1. `sharing/` — Google Docs ACL: `domain/` · `service/` · `webmvc/` · `data-jpa/` · `feign/`
2. `bff/` — OAuth2 Client + Spring Session + Token Exchange
3. `thymeleaf/` — server-rendered BFF
4. Banking Phase 2 — MFA, token rotation, audit log
5. `crud/` — shared library

### `auth/` — В КОНЦЕ

> Скелет `auth/application/` создан. Реализация после завершения всего остального: `domain/` (ISP-порты) · `data-jpa/` (Flyway) · Authorization Server · Resource Server во всех сервисах.

---

## Открытые решения

> Не реализовывать до принятия явного решения.

### ⚠ Reactive/sync impedance

Блокирует реализацию `data-r2dbc/`, `data-mongodb-reactive/`, `webflux/`.

**Суть проблемы.** Порты `domain/` используют sync-типы Java:

```java
NoteResponse          add(NoteRequest request);
Optional<NoteResponse> findById(UUID id);
List<NoteResponse>    findAll();
```

R2DBC и MongoDB Reactive возвращают `Mono`/`Flux`. Адаптер не может реализовать sync-порт без `.block()`. WebFlux-контроллер не может вернуть `Mono<ResponseEntity>`, если use case возвращает `NoteResponse` синхронно.

Совместимость адаптеров:

| driving \ driven              | `data-jpa` | `data-mongodb` | `data-jdbc` | `data-r2dbc` | `data-mongodb-reactive` |
|-------------------------------|------------|----------------|-------------|--------------|-------------------------|
| `webmvc`                      | ✓          | ✓              | ✓           | ✗            | ✗                       |
| `webflux`                     | ✗          | ✗              | ✗           | ✓            | ✓                       |
| `graphql` (WebMVC транспорт)  | ✓          | ✓              | ✓           | ✗            | ✗                       |
| `graphql` (WebFlux транспорт) | ✗          | ✗              | ✗           | ✓            | ✓                       |

Варианты:
- **Параллельные порты** _(склонение)_ — sync-интерфейсы + reactive-интерфейсы (`ReactiveNoteAddPort`, ...); `Mono`/`Flux` входят в `domain/`; два набора адаптеров
- **Sync обёртка с `.block()`** — адаптер реализует sync-интерфейс, блокируя поток; риск deadlock в event-loop; не настоящий reactive
- **Отдельные реактивные сервисы** — `domain/` дублируется; нарушает DRY

### После Resource Server

- **Регистрация auth/ ↔ user/** — Lazy / Sync (нарушает direction) / Events (Kafka — без нарушения SoC)
- **Возврат мутирующего use case** — **A — DTO** _(склонение)_ vs **B — `void`** (чистый CQS)
- **PATCH** — fetch → modify → replace в `webmvc/` или не поддерживать

### Стратегия активации адаптеров — отложено

- **A — `@Profile("jpa")`** _(склонение)_ — просто; убирается когда появится вторая реализация
- **B — Отдельные `application-jpa/` · `application-r2dbc/`** — чисто на уровне сборки; дублирует composition root

---

## Стек

> Spring Boot 4 monorepo — banking-grade с первого дня

| Инструмент                   | Целевая версия | Сейчас   |
|------------------------------|----------------|----------|
| Java                         | 21             | 21       |
| Gradle                       | 9.6.0          | 9.5.1    |
| Spring Boot                  | 4.1.0          | 4.0.6    |
| Spring Cloud                 | 2025.1.2       | 2025.1.1 |
| Spring Dependency Management | 1.1.7          | 1.1.7    |

> Обновить: `buildSrc/build.gradle.kts` (val-константы) + `gradle/wrapper/gradle-wrapper.properties`.

---

## Принципы

> Применяются с первого дня. Все принципы доводятся до абсолюта — не как ориентир, а как требование.

**Единый принцип декомпозиции — Single Data Flow:** каждый класс — только то, что нужно для одного потока данных:
- **Порт** — один интерфейс, один метод (`NoteAddPort`, `NoteFindByIdPort`)
- **Адаптер** — один bean, один порт (`NoteAddPortAdapter implements NoteAddPort`)
- **Контроллер** — один bean, одна операция (`NoteCreateController` → `POST /notes`)

Любой компромисс требует явного обоснования и фиксации в этом файле.

### Четыре приоритетных критерия

- **Hexagonal Architecture** — ports & adapters; изоляция ядра от инфраструктуры
- **Clean Architecture** — зависимости направлены внутрь; framework — деталь
- **SOLID** — SRP · OCP · LSP · ISP · DIP; на уровне классов, модулей и сервисов
- **Separation of Concerns** — каждый сервис / модуль отвечает за одну зону ответственности

### Clean Architecture

```
Frameworks & Drivers  ←  webmvc/ · data-jpa/ · data-mongodb/ · data-jdbc/
Interface Adapters    ←  (маппинг; живёт внутри адаптеров)
Use Cases             ←  service/ (только когда оправдан)
Entities              ←  domain/ (DTO + port interfaces)
```

**Screaming Architecture** — структура кода кричит о предметной области: `note/`, `user/`, `sharing/`.

### High Cohesion / Low Coupling

Основа всех решений о границах. Common Closure Principle: в один сервис — то, что изменяется вместе. Acyclic Dependencies: граф зависимостей между сервисами ацикличен (DAG).

### Паттерны на рассмотрение

- **Bounded Contexts (DDD)** — явные границы; кто владеет какими данными
- **Domain Events** — коммуникация между BC без прямой зависимости
- **CQRS** — актуально для `sharing/` (`effectiveRole` — query; `share`/`transferOwnership` — command)
- **SAGA** — актуально для `transferOwnership` (`sharing/` + `user-note/`)

### Метод решения архитектурных проблем

При вопросах о портах — **добавить второй адаптер.** Интерфейс порта следует из потребностей адаптеров, а не проектируется абстрактно. Если второй адаптер не реализует интерфейс чисто — интерфейс нужно менять.

### Что легко менять и почему

| # | Что менять             | Механизм                                                           | Цена    |
|---|------------------------|--------------------------------------------------------------------|---------|
| 1 | Бизнес-логику          | в `domain/`; адаптеры не трогаются                                 | Дёшево  |
| 2 | Порты                  | смена порта = смена контракта                                      | Дорого  |
| 3 | Адаптеры               | одна строка в `application/build.gradle`; convention plugin        | Дёшево  |
| 4 | Внешние инструменты    | меняется конфиг и plugin; код не меняется                          | Дёшево  |
| 5 | Внутренние инструменты | Spring, Hibernate — только в адаптерах; `domain/` их не видит      | Дёшево  |
| 6 | Язык реализации        | `domain/` — чистая логика без фреймворка                           | Принцип |
| 7 | Фреймворк              | фреймворк — деталь адаптера; нет `import org.springframework.*`    | Принцип |
| 8 | Платформу              | паттерн языко-независим                                            | Принцип |

Пункт 2 намеренно дорогой: смена порта — смена контракта между бизнес-логикой и инфраструктурой.

**Enforcement (в порядке надёжности):**
1. **Convention plugins** — нарушение не компилируется; `domain/` физически не видит `data-jpa/`
2. **ArchUnit** — нарушение в CI; проверяет `import org.springframework.*` в `domain/`
3. **Тесты домена без Spring** — если тест не поднимает Spring context, граница чистая

### YAGNI для сервисного слоя

Оправдан только когда: (1) координация нескольких портов, (2) сложная транзакционная граница через несколько шагов, (3) доменная политика не принадлежащая ни сущности, ни репозиторию, ни контроллеру. Для тривиального CRUD — прямой вызов из адаптера достаточен.

### Глубина пакетов как сигнал нового модуля

Появление подпакетов (`.mapping`, `.entity`, `.query`) — сигнал, что модуль взял на себя слишком много. Вопрос: это новый модуль, нарушение SRP или раздутый класс?

### Остальные принципы

**No partial abstractions** · **Twelve-Factor** · **KISS** · **DRY** · **SSOT** · **Law of Demeter** · **Fail Fast**

---

## Архитектура

```
application/            Spring Boot app — composition root; знает все модули
domain/                 DTO + port interfaces — чистая Java, без Spring
service/                use case implementations — только при координации нескольких портов
webmvc/                 driving adapter (sync REST/HTTP)       →  domain/
webflux/                driving adapter (reactive REST/HTTP)   →  domain/
grpc/                   driving adapter (gRPC/Protobuf)        →  domain/
graphql/                driving adapter (GraphQL)              →  domain/
data-jpa/               driven adapter  (JPA/SQL, ORM)        →  domain/
data-jdbc/              driven adapter  (JDBC/SQL, no ORM)    →  domain/
data-r2dbc/             driven adapter  (reactive SQL)        →  domain/
data-mongodb/           driven adapter  (MongoDB)             →  domain/
data-mongodb-reactive/  driven adapter  (reactive MongoDB)    →  domain/
feign/                  driven adapter  (HTTP client)         →  domain/  (sharing/feign/)
```

**Dependency direction** — `application/` знает всё; адаптеры знают только `domain/`; `domain/` — ничего снаружи
**Database per service** — cross-service JOIN запрещён
**Stateless** — `gateway/` · `config/` · `registry/` · `user/` · `note/` · `user-note/`
**Stateful** — `bff/` · `thymeleaf/` → Spring Session; `auth/` → OAuth2Authorization в PostgreSQL

### Тестирование (пирамида)

| Модуль         | Тест-слой                | Что проверяет                                      |
|----------------|--------------------------|----------------------------------------------------|
| `domain/`      | JUnit (чистая Java)      | Доменная логика без Spring context                 |
| `service/`     | Spring context + Mockito | Use case при наличии; Repository мокируется        |
| `webmvc/`      | `@WebMvcTest` (MockMvc)  | HTTP binding, статусы, сериализация                |
| `data-jpa/`    | `@DataJpaTest` + TC      | SQL, маппинг; Testcontainers = реальный PostgreSQL |
| `application/` | `@SpringBootTest` + TC   | Полный smoke test; все слои вместе                 |

### Семантика портов

```java
// Порты note/ (аналогично user/ и user-note/)
NoteResponse           add(NoteRequest request);         // put — UUID генерирует адаптер
Optional<NoteResponse> findById(UUID id);                // get
List<NoteResponse>     findAll();                        // values
NoteResponse           replace(UUID id, NoteRequest r);  // replace (full)
void                   remove(UUID id);                  // remove
boolean                existsById(UUID id);              // containsKey
```

**`add` ≠ `replace` — осознанная семантика:**
- JPA: `add` → `save()` (null ID → persist) · `replace` → `save()` (merge)
- MongoDB: `add` → `mongoTemplate.insert()` (бросает при коллизии) · `replace` → `mongoTemplate.save()`
- JDBC: `add` → `INSERT INTO` · `replace` → `UPDATE ... WHERE id = ...`

**Маппинг — ответственность адаптера:** `[Entity][Tech]Mapper` (interface) + `[Entity][Tech]MapperImpl` (`@Component`); ручной; без MapStruct. Порты не знают о `NoteEntity`, `NoteDocument`.
**Spring Data — деталь адаптера:** `JpaRepository` / `MongoRepository` живут внутри адаптера, невидимы из `domain/`.
**Методы портов — по потребности домена**, не по возможностям Spring Data. Возвращаемые типы — только Java-типы или DTO; `Pageable`/`Page`/`Specification` — утечка инфраструктуры.

### Принятые решения

- **Gateway ≠ BFF** · **BFF — один на UX** (Sam Newman)
- **Token Exchange (RFC 8693)** — BFF обменивает access token на internal JWT с `aud` микросервиса
- **Spring Authorization Server — постоянный IdP**
- **OpenFeign** — `spring-cloud-starter-openfeign` для `sharing/feign/`
- **DTO в `domain/`** — `NoteRequest` · `NoteResponse`; domain objects удалены; JPA entities / MongoDB documents — внутри адаптеров
- **`existsById` в port** — валидный паттерн; не заменять на `findById`
- **Один контроллер на операцию** — `NoteCreateController` · `NoteFindByIdController` · `NoteFindAllController` · `NoteUpdateController` · `NoteDeleteController`
- **`ResponseEntity<T>` в контроллерах** — `ResponseEntityExceptionHandler` + `ProblemDetail` (RFC 9457); `spring.mvc.problemdetails.enabled=true`
- **`AuthUser` (`auth/`) ≠ `User` (`user/`)** — `User { id, username, email }`; пароль хранит только `auth/`
- **Wire format в адаптере** — `.proto` в `grpc/`, `.graphqls` в `graphql/`
- **`@Transactional` на методах адаптера**
- **`*UseCase` интерфейсы убраны** — `domain/` содержит только DTO + output ports
- **`service/` только когда оправдан** — для `sharing/`; для `user/` · `note/` · `user-note/` удалён
- **`sharing/` — отдельный гексагональный сервис** — вызывает `user-note/` и `note/` через Feign; CRUD сервисы не знают о нём
- **Enforcement — BFF + сетевая изоляция** — BFF проверяет `sharing/effectiveRole` перед вызовом `note/`
- **`NoteVisibility` — НЕ в `note/domain/`** — принадлежит `sharing/`
- **Google Docs ACL модель:**
  - `UserNote { userId, noteId, role }` — `UserNoteRole`: `OWNER · EDITOR · COMMENTER · VIEWER`
  - `NoteAccess { noteId, generalAccess, editorsCanShare, canDownloadCopyPrint }` — `generalAccess`: `RESTRICTED · VIEWER · COMMENTER · EDITOR`
  - `NotePublication { noteId, linkPublished, linkAutoRepublish, embedPublished, embedAutoRepublish }` — «publish to web» ≠ «share with link»
  - `effectiveRole`: явная `UserNote` → general access → deny
  - `share`: caller = OWNER (или EDITOR при `editorsCanShare`); role ≤ роли caller'а
  - `transferOwnership`: атомарно; один `OWNER` — доменный инвариант; логика в `sharing/service/`
- **Доменные исключения** — `UserNotFoundException` · `NoteNotFoundException` · `UserNoteNotFoundException` в `domain/`
- **Делегировать Spring Data** — JPA → `JpaRepository`; MongoDB → `MongoRepository`; JDBC → `NamedParameterJdbcTemplate`

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
              sharing/     Resource Server  (Google Docs ACL: effectiveRole, share, transferOwnership, publish)
INFRA         config/      Spring Cloud Config Server
              registry/    Eureka Server
EXTERNAL      Redis        JTI Blocklist + Spring Session (bff/ + thymeleaf/)
              PostgreSQL×N по одной БД на: auth, user, note, user-note, sharing
              Kafka/MQ     только при событийной регистрации (решение не принято)
```

> **MVP** — та же архитектура; H2 вместо PostgreSQL, `MapSessionRepository` вместо Redis.

---

## Безопасность

> `identity ≠ profile ≠ business ≠ permissions`

**Zero Trust** — каждый слой валидирует JWT самостоятельно
**JWT claims** — только стандартные: `sub` · `iss` · `aud` · `exp` · `jti` · `acr` · `amr` · `scope`
**Браузер не видит JWT** — только HttpOnly session cookie в `bff/` · `thymeleaf/`

| Слой            | Модуль                | Ответственность                            |
|-----------------|-----------------------|--------------------------------------------|
| Edge            | `gateway/`            | Routing, rate limiting, TLS                |
| BFF             | `bff/` · `thymeleaf/` | OAuth2 login flow, session, Token Exchange |
| IdP             | `auth/`               | JWT issuing, credentials, OIDC             |
| Resource Server | `*/webmvc/`           | JWT validation per request                 |
| ACL             | `sharing/`            | effectiveRole, share, transferOwnership    |

### Клиенты

| Клиент               | Grant Type         | Хранит токен         | BFF          |
|----------------------|--------------------|----------------------|--------------|
| Browser / Thymeleaf  | Authorization Code | Серверная сессия     | Сам себе BFF |
| Browser / SPA        | Authorization Code | Серверная сессия BFF | `bff/`       |
| Mobile (Android/iOS) | AuthCode + PKCE    | Keychain / Keystore  | Нет          |
| B2B / CLI            | Client Credentials | Не хранит            | Нет          |

### Токены и аутентификация

**Flow:** User → `auth/` → access_token → BFF Token Exchange (RFC 8693) → internal JWT → Microservice
**access_token** 15 мин · **refresh_token** 30–90 дней; в микросервисы не отправляется
**Rotation** — каждый refresh → новый refresh_token; повторное использование → revoke вся семья
**JTI Blocklist** — Redis `SET jti:{jti} "revoked" EX ttl`; logout < 1 с
**Social Login** — `auth/` сам OAuth2 Client; чужой токен не покидает `auth/`
**MFA** — `@EnableMultiFactorAuthentication` + `FactorGrantedAuthority`; Step-up: `/oauth2/authorize?acr_values=2`

### Banking-grade фазы

| Фаза         | Содержание                                                                                 |
|--------------|--------------------------------------------------------------------------------------------|
| 1 — основа   | PKCE · `aud`/`scope`/`jti` · refresh rotation · rate limiting · TLS 1.3 · stateless        |
| 2 — MFA      | TOTP/Passkey · Social Login · JTI Blocklist · Step-up · device tracking · audit log        |
| 3 — максимум | DPoP · mTLS · Certificate pinning · App attestation                                        |

---

## Техническая база

### Gradle

**`buildSrc`** + convention plugins (Kotlin DSL) — единственный механизм; flat, без вложенности
**Файлы:** `buildSrc/build.gradle.kts` (версии — в `val`-константах); convention plugins — `src/main/kotlin/*.gradle.kts`; субпроекты — `build.gradle` (Groovy, только `id '...'`)
**Нет:** root `build.gradle` · `buildSrc/settings.gradle` · `libs.versions.toml`
**Cloud BOM** — инлайн в каждом Cloud convention plugin: `"org.springframework.cloud:spring-cloud-dependencies:2025.1.2"`
**`settings.gradle`:** `gateway` → `config` → `registry` → `auth` → `user` → `note` → `user-note`; внутри: `application` → `domain` → `webmvc` → `data-jpa`

**Convention plugins:**

| Plugin ID                                               | Назначение                                        |
|---------------------------------------------------------|---------------------------------------------------|
| `spring-boot-application-conventions`                   | `application/` — Boot app                         |
| `java-domain-conventions`                               | `domain/` — чистая Java, без BOM                  |
| `spring-boot-webmvc-adapter-conventions`                | `webmvc/` — driving adapter (sync REST)           |
| `spring-boot-webflux-adapter-conventions`               | `webflux/` — driving adapter (reactive REST)      |
| `spring-boot-graphql-adapter-conventions`               | `graphql/` — driving adapter (GraphQL)            |
| `spring-boot-data-jpa-adapter-conventions`              | `data-jpa/` — driven adapter (JPA/SQL, ORM)       |
| `spring-boot-data-jdbc-adapter-conventions`             | `data-jdbc/` — driven adapter (JDBC/SQL, no ORM)  |
| `spring-boot-data-r2dbc-adapter-conventions`            | `data-r2dbc/` — driven adapter (reactive SQL)     |
| `spring-boot-data-mongodb-adapter-conventions`          | `data-mongodb/` — driven adapter (MongoDB)        |
| `spring-boot-data-mongodb-reactive-adapter-conventions` | `data-mongodb-reactive/` — driven adapter         |
| `spring-boot-data-elasticsearch-adapter-conventions`    | `data-elasticsearch/` — driven adapter            |
| `spring-cloud-openfeign-adapter-conventions`            | `feign/` — driven adapter (HTTP client)           |
| `spring-boot-restclient-conventions`                    | add-on: RestClient (sync HTTP)                    |
| `spring-boot-webclient-conventions`                     | add-on: WebClient (reactive HTTP)                 |
| `spring-cloud-gateway-webflux-conventions`              | `gateway/` — reactive gateway (WebFlux-based app) |
| `spring-cloud-gateway-webmvc-conventions`               | `gateway/` — sync gateway (WebMVC-based app)      |
| `spring-cloud-config-server-conventions`                | `config/` — Config Server app                     |
| `spring-cloud-config-client-conventions`                | add-on: Config Client                             |
| `spring-cloud-eureka-server-conventions`                | `registry/` — Eureka Server app                   |
| `spring-cloud-eureka-client-conventions`                | add-on: Eureka Client                             |
| `spring-cloud-circuit-breaker-conventions`              | add-on: Resilience4j Circuit Breaker (reactive)   |
| `spring-cloud-loadbalancer-conventions`                 | add-on: Spring Cloud LoadBalancer                 |
| `spring-boot-h2-database-conventions`                   | add-on: H2 + h2console                            |
| `spring-boot-actuator-conventions`                      | add-on: Actuator                                  |
| `spring-boot-oauth2-authorization-server-conventions`   | `auth/` — Authorization Server                    |
| `spring-boot-oauth2-resource-server-conventions`        | add-on: JWT-валидация (Resource Server)           |
| `spring-boot-oauth2-client-conventions`                 | add-on: OAuth2 Client                             |
| `java-codequality-conventions`                          | quality: мета-плагин — все четыре ниже            |
| `java-javaformat-conventions`                           | quality: io.spring.javaformat + Checkstyle        |
| `java-errorprone-conventions`                           | quality: ErrorProne + NullAway + JSpecify         |
| `java-jacoco-conventions`                               | quality: JaCoCo (покрытие)                        |
| `java-jacoco-report-aggregation-conventions`            | quality: JaCoCo агрегация (все модули)            |

**Заметки:**
- `domain` — только `java`; без Spring BOM; JUnit с явными версиями
- `oauth2-resource-server` — транспортно-независимый; применим к `webmvc/`, `webflux/`, `graphql/`
- `java-codequality-conventions` — мета-плагин; каждый convention plugin объявляет его явно (Explicit over Implicit)
- `h2-database` — add-on поверх `data-jpa`; не содержит `repositories {}`
- `gateway-*` / `config-server` / `eureka-server` — включают `org.springframework.boot` plugin; не комбинировать с `application-conventions`
- `restclient` / `webclient` — не адаптеры; инструменты внутри других адаптеров
- `circuit-breaker` — только реактивный (Resilience4j reactor); для sync-стека иначе

### Spring Boot 4

- `starter-web` → `starter-webmvc`; `starter-aop` → `starter-aspectj`
- `starter-test` слайсы (`@WebMvcTest`, `@DataJpaTest`) — в отдельных `*-test` стартерах (в Boot 3 входили в `starter-test`)
- `@SpringBootTest` — не предоставляет MockMVC/WebClient/TestRestTemplate; добавить `@AutoConfigureMockMvc` / `@AutoConfigureTestRestTemplate`
- OAuth2 стартеры: `oauth2-*` (Boot 3) → `security-oauth2-*` (Boot 4); `docs.spring.io/spring-security` ссылается на Boot 3 — не доверять
- **Spring Authorization Server** — часть Spring Security 7.0; `spring-authorization-server.version` property убран
- Jackson 3: `com.fasterxml.jackson` → `tools.jackson`; `jackson-annotations` остаётся на `com.fasterxml.jackson.core`; `@JsonComponent` → `@JacksonComponent`; `Jackson2ObjectMapperBuilderCustomizer` → `JsonMapperBuilderCustomizer`
- `RestTemplate` deprecated → `spring-boot-starter-restclient`; реактивный — `spring-boot-starter-webclient`
- `@MockBean` / `@SpyBean` → `@MockitoBean` / `@MockitoSpyBean`; только на полях тест-класса, не в `@Configuration`
- `hibernate-jpamodelgen` → `hibernate-processor`
- Liveness/Readiness probes включены по умолчанию; отключить: `management.endpoint.health.probes.enabled=false`
- Undertow не поддерживается (несовместим с Servlet 6.1)
- gRPC — нативная поддержка: `spring-boot-starter-grpc-client` / `spring-boot-starter-grpc-server` _(4.1)_
- Flyway + PostgreSQL: `runtimeOnly("org.flywaydb:flyway-database-postgresql")`
- Обработка ошибок: `ResponseEntityExceptionHandler` + `ProblemDetail` (RFC 9457); `spring.mvc.problemdetails.enabled=true`
- Lombok: `compileOnly` + `annotationProcessor`; `@Data`/`@EqualsAndHashCode` запрещены на entities; `hashCode() { return getClass().hashCode(); }`

### Spring Security

**Цепочка:** `SecurityFilterChain` → `AuthenticationManager` → `ProviderManager` → `DaoAuthenticationProvider` → `UserDetailsService` + `PasswordEncoder`
**Resource Server** — `JwtDecoder` + `JwtAuthenticationConverter`; минимум: `spring.security.oauth2.resourceserver.jwt.issuer-uri`
**Auth Server** — 4 обязательных бина: `SecurityFilterChain` × 2, `UserDetailsService`, `JWKSource`

### Null Safety и стиль

**JSpecify** (`@NullMarked` через `package-info.java`) — non-null по умолчанию; каждый пакет требует `package-info.java`; `org.jspecify:jspecify:1.0.0` (`compileOnly`) из `java-errorprone-conventions`.
**NullAway** — `check("NullAway", CheckSeverity.ERROR)` + `option("NullAway:AnnotatedPackages", "com.example")`; для тестов: `disable("NullAway")`.
**`@Nullable`** — `@Target(TYPE_USE)`: `private @Nullable String field`; `@Nullable UUID id` — для JPA полей с `@GeneratedValue`.
**`@SuppressWarnings("NullAway.Init")`** — только на `protected` no-arg конструкторах framework entities.
**Spring Cloud (2025.1.x)** — не null-safe в `registry/`, `config/`, `gateway/`; при нужде: `@NullUnmarked`.
**Импорты** — порядок групп (SpringImportOrderCheck): `java.*` → `javax.*` → `*` (jakarta, org, com.example и т.д.) → `org.springframework.*`; blank line между группами; без blank внутри группы; wildcard запрещён (AvoidStarImportCheck).
**Промежуточная переменная** — перед `return` всегда извлекать результат; не inline в `.body()`.

### Качество и наблюдаемость

- **spring-javaformat** `0.0.47` — `checkstyle toolVersion = "9.3"`; строки до 120 символов; `springJavaFormat {}` — Groovy DSL only; в Kotlin DSL precompiled scripts — только задачи `format`/`checkFormat`; README: `https://github.com/spring-io/spring-javaformat/blob/v0.0.47/README.adoc`
- **SpringChecks** — `io.spring.javaformat.checkstyle.SpringChecks` загружает `spring-checkstyle.xml` из classpath JAR через `getResourceAsStream("spring-checkstyle.xml")`; отключение конкретных проверок — через `<property name="excludes" value="..."/>` с comma-separated именами классов; список всех проверок — в `spring-checkstyle.xml` репозитория
- **applyDefaultConfig()** — добавлен в 0.0.48, недоступен на Maven Central; блок `springJavaFormat { checkstyle { } }` в convention plugin закомментирован — не удалять
- **configProperties** → `SpringImportOrderCheck`: `configProperties = ['projectRootPackage': 'com.example']` в Gradle НЕ пробрасывается внутрь SpringChecks; `SpringImportOrderCheck` использует дефолтный `projectRootPackage = "org.springframework"` → `org.springframework.*` — последняя группа импортов
- **`format` не переупорядочивает импорты** — Eclipse JDT форматирует тело, но не меняет порядок групп импортов
- **ErrorProne** `net.ltgt.errorprone 5.1.0` — `error_prone_core 2.50.0`; Java 21 требует 2.43+
- **NullAway** `0.13.7` — плагин к ErrorProne; без `AnnotatedPackages` не запускается
- **JaCoCo** `0.8.14` — `test.finalizedBy(jacocoTestReport)`; `jacoco-report-aggregation` — агрегация по monorepo
- **ArchUnit** — `archunit-junit5`; версию брать с Maven Central; не реализован (convention plugins дают compile-time enforcement)
- **SonarQube** — только внешне; не добавляется как Gradle-зависимость
- **Actuator** — только в `application/`; отдельный `SecurityFilterChain` для `/actuator/**`
- **`testRuntimeOnly`** — `org.junit.platform:junit-platform-launcher` (обязательно)
- **Reactor tests** — `io.projectreactor:reactor-test` (StepVerifier); для R2DBC / WebFlux / MongoDB Reactive
- **Testcontainers** — в Boot 4 модули: `testcontainers-postgresql`, `testcontainers-mongodb`, `testcontainers-kafka` и т.д.

### CI/CD и опыт

**GitHub Actions** → **GitLab CI** → **Jenkins**. Текущий: GitHub Actions — не трогать без явного запроса.

Последовательно по мере развития: **Spring Modulith** · **jMolecules** · **Docker / Compose / Kubernetes** · **Terraform** · **AWS** · **Elastic Stack**

---

## Развёртывание

| Этап        | Инструменты                       | Backing services                 |
|-------------|-----------------------------------|----------------------------------|
| Local / MVP | JVM + H2 + `MapSessionRepository` | Не нужны                         |
| Staging     | Docker + Docker Compose           | PostgreSQL · Redis · Kafka · ELK |
| Production  | Docker + ECS Fargate → EKS        | AWS managed services             |
