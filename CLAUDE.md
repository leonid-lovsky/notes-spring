# CLAUDE.md — notes-spring

> Последнее обновление: 2026-07-01T08:49Z
> **Всё временно** — любое решение подлежит обсуждению и изменению.

Многомодульный Spring Boot 4 проект (`note/`, `user/`, `user-note/`, ...), реализующий hexagonal
architecture единообразно во всех сервисах через Gradle convention plugins. Этот файл — единственный
источник истины по конвенциям, статусу и решениям проекта; вся необходимая для работы над проектом
информация должна быть здесь, без обращения к внешним источникам.

---

## Начало каждой сессии

1. Прочитать этот файл
2. Текущий приоритет — раздел [Задачи](#-задачи)
3. Актуализировать файл: убрать устаревшее, отразить новые решения
4. Коммит + пуш _(постоянная авторизация — искл. из правила «Коммиты» ниже)_

---

## Правила

- **Язык** — общение на русском; код и идентификаторы — на английском
- **Файлы** — не изменять без явного указания; перед созданием нового файла изучить проект
- **Коммиты** — не коммитить без явного запроса, кроме начала сессии (см. выше);
  «Зафиксируй» = обновить CLAUDE.md и дату → коммит → пуш
- **Дата перед коммитом** — брать из `date -u +"%Y-%m-%dT%H:%MZ"`, не выдумывать
- **CI** — не изменять `.github/workflows/` без отдельного запроса
- **Таблицы** — во всех markdown-таблицах строки одинаковой длины в Unicode-символах
- **Архитектурные решения** — сначала варианты с плюсами/минусами и рекомендацией, дождаться
  выбора пользователя; не реализовывать до явного решения
- **Build** — после каждого логического шага: `./gradlew clean build` → обновить CLAUDE.md → коммит → пуш
- **Подзадачи** — крупную задачу разбивать на подзадачи; коммитить и пушить после каждой
  завершённой подзадачи, чтобы не терять прогресс при обрыве сессии

---

## ⚡ Задачи

Статус по сервисам и модулям:

```
ГОТОВО   note/  — domain · data-contract · data-contract-reactive · webmvc · webflux
                  data-jpa · data-mongodb · data-jdbc · data-r2dbc · data-mongodb-reactive
ГОТОВО   user/  — domain · data-contract · data-contract-reactive · webmvc · webflux
                  data-jpa · data-mongodb · data-jdbc · data-r2dbc · data-mongodb-reactive
ГОТОВО   user-note/ — domain · data-contract · data-contract-reactive · webmvc · webflux
                      data-jpa · data-mongodb · data-jdbc · data-r2dbc · data-mongodb-reactive
ОТЛОЖЕНО   graphql/ · auth/
ОТЛОЖЕНО   инфраструктура — Spring Config · Spring Eureka · Spring Actuator · Spring Cache ·
                            Spring OAuth2 · Spring OpenFeign · Spring Gateway (и другие)
НЕ СОЗДАНО bff/ · thymeleaf/ · sharing/ · crud/
```

Технологии за пределами Spring-стека, отложенные на будущее (без привязки к конкретному модулю),
в логичном порядке освоения — от инструментов разработки к production-инфраструктуре и архитектурным
паттернам:

- **testcontainers** — интеграционные тесты в реальных Docker-контейнерах
- **Docker Compose** — локальный оркестратор dev/test окружения
- **Kubernetes** — production-оркестрация контейнеров
- **Jenkins** — CI/CD pipeline
- **Amazon Web Services** — целевая cloud-платформа
- **Kafka / RabbitMQ** — message broker (см. открытое решение «Регистрация auth/ ↔ user/»)
- **Elastic Stack** — логирование и поиск (альтернатива Loki)
- **jMolecules** — явная разметка DDD/hexagonal-концепций в коде
- **Axon Framework** — CQRS/Event Sourcing, следующий уровень после текущей hexagonal-архитектуры

**Spring JavaFormat** (`io.spring.javaformat`): таски `format`/`checkFormat`; `checkFormat`
автоматически выполняется при стандартном `check`. После правок импортов/форматирования — гонять
`./gradlew format`, не чинить импорты руками.

**Checkstyle — порядок импортов:**
```
// Группы: java.* → javax.* → * → org.springframework.*; пустая строка между группами
// jakarta.*, com.example.*, reactor.*, org.jspecify.* — все в группе *; без пустых строк внутри
import com.example.note.domain.NoteResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;
```

---

## Архитектура и структура проекта

**Многомодульная сборка (Gradle):**
```
buildSrc/src/main/groovy/        convention plugins (Groovy DSL, не Kotlin)
{service}/settings.gradle        объявляет subprojects сервиса
{service}/{module}/build.gradle  применяет plugin через id '...'
```

**Пакеты по сервисам:**
```
note/       → com.example.note
user/       → com.example.user
user-note/  → com.example.usernote   (не user.note!)
```

**Слои внутри сервиса** (зависимость → куда смотрит):
```
domain/                 records · enums · exceptions — чистая Java, без зависимостей на инфраструктуру
data-contract/          sync port interfaces         → domain/ (api)
data-contract-reactive/ reactive port interfaces     → domain/ (api) · reactor-core (api)
webmvc/                 driving adapter sync         → data-contract/
webflux/                driving adapter reactive     → data-contract-reactive/
data-jpa/               driven adapter JPA           → data-contract/
data-jdbc/              driven adapter JDBC          → data-contract/
data-r2dbc/             driven adapter R2DBC         → data-contract-reactive/
data-mongodb/           driven adapter MongoDB       → data-contract/
data-mongodb-reactive/  driven adapter Mongo rx      → data-contract-reactive/
application/            composition root             → все модули
```

**Внутренняя структура driven-адаптера** (пример `data-jpa/`; одинаково для всех технологий):
```
com.example.note.data.jpa.adapter      NoteAddJpaAdapter, ...
com.example.note.data.jpa.model        NoteEntity
com.example.note.data.jpa.mapper       NoteJpaMapperContract, NoteJpaMapper
com.example.note.data.jpa.repository   NoteJpaRepository
```
Пакет `model/` используется во всех технологиях (внутреннее имя класса — `Entity` для JPA/R2DBC,
`Document` для MongoDB). Driving-адаптеры (`webmvc/`, `webflux/`) организованы плоско, без вложенных
пакетов.

---

## Именование

| Элемент                       | Паттерн                        | Пример                         |
|-------------------------------|--------------------------------|--------------------------------|
| Sync port interface           | `{Entity}{Op}Contract`         | `NoteAddContract`              |
| Reactive port interface       | `{Entity}{Op}ContractReactive` | `NoteAddContractReactive`      |
| Adapter impl                  | `{Entity}{Op}{Tech}Adapter`    | `NoteAddJpaAdapter`            |
| Mapper interface              | `{Entity}{Tech}MapperContract` | `NoteJpaMapperContract`        |
| Mapper impl                   | `{Entity}{Tech}Mapper`         | `NoteJpaMapper`                |
| Spring Data repo              | `{Entity}{Tech}Repository`     | `NoteJpaRepository`            |
| JPA / R2DBC model class       | `{Entity}Entity`               | `NoteEntity`                   |
| MongoDB model class           | `{Entity}Document`             | `NoteDocument`                 |

**Tech**: `Jpa` · `Mongo` · `Jdbc` · `R2dbc` · `MongoReactive`

---

## Стек

| Инструмент    | Версия   |
|---------------|----------|
| Java          | 21       |
| Gradle        | 9.5.1    |
| Spring Boot   | 4.0.6    |
| Spring Cloud  | 2025.1.1 |

**Spring Boot 4 — критичные отличия от Boot 3** (источник: существующие `build.gradle` в проекте;
документация docs.spring.io местами показывает Boot 3 и не заслуживает доверия — сверяться с
файлами проекта):
- `starter-web` → `starter-webmvc`; `starter-aop` → `starter-aspectj`
- `@MockBean`/`@SpyBean` → `@MockitoBean`/`@MockitoSpyBean`
- OAuth2-стартеры: `oauth2-resource-server` → `security-oauth2-resource-server`; аналогично для
  `client` и `authorization-server`
- Spring Authorization Server — часть Spring Security 7, отдельной версии не имеет
- `@WebMvcTest`, `@DataJpaTest` — в отдельных `*-test` стартерах, не в `starter-test`
- `@SpringBootTest` не подключает MockMvc/WebClient автоматически — нужен `@AutoConfigureMockMvc`
- Jackson 3: пакет `tools.jackson` (кроме `jackson-annotations` — остался на `com.fasterxml`)

---

## Принятые решения

### Архитектура

- Hexagonal: `domain/` не знает о JPA/MongoDB/Spring; адаптеры знают только `data-contract/`
- Один контроллер на операцию (`NoteCreateController` → `POST /notes`)
- `service/` — только при координации нескольких портов; для простого CRUD не нужен
- `existsById` в контракте — валидный паттерн, не заменять на `findById`
- `@Transactional` — на методах адаптера
- `add` ≠ `replace`: JPA — `save(null id)` vs `save(id)`; MongoDB — `insert()` vs `save()`
- `user-note/`: суррогатный `UUID id` (а не составной `userId+noteId` как PK) во всех технологиях,
  кроме `data-jdbc/` (там нет `model/`/`repository/` в принципе). `userId+noteId` — unique
  constraint/index, не PK. `id` выставлен в `domain/`/контрактах (`UserNoteResponse.id`,
  `UserNoteFindByIdContract`). Это сняло ограничение Spring Data R2DBC (нет `@EmbeddedId`) —
  `data-r2dbc/` теперь полноценный model+repository, без `DatabaseClient`
- Unique constraint `userId+noteId` по технологиям: JPA — `@Table(uniqueConstraints=...)` (работает,
  Hibernate создаёт схему); MongoDB — `@CompoundIndex(unique=true)` (работает); **Spring Data R2DBC
  enforces unique composite constraints at the database level rather than through entity
  annotations** — у `Table`/`Column` (`spring-data-relational`) нет атрибутов unique/constraints
  вообще (проверено декомпиляцией), а в проекте пока нет ни `schema.sql`, ни Flyway/Liquibase, ни
  подключения `data-r2dbc`/`data-jdbc` к `application/` — поэтому для R2DBC/JDBC constraint сейчас
  не создаётся нигде. Решается вместе с открытым вопросом «Управление схемой для R2DBC/JDBC»
- `data-jdbc/` (все сервисы) — намеренно без `model/`/`repository/`: `NamedParameterJdbcTemplate` +
  сырой SQL, `RowMapper` мапит `ResultSet` сразу в `*Response`

### HTTP / Ошибки

- `ResponseEntity<T>` в контроллерах
- `ProblemDetail` (RFC 9457); `spring.mvc.problemdetails.enabled=true`
- Доменные исключения (`NoteNotFoundException`) — в `domain/`

### Маппинг

- Ручной, без MapStruct; маппер — ответственность адаптера
- `Pageable`/`Page`/`Specification` — утечка инфраструктуры, не использовать в контрактах

### Reactive-семантика

- `findById` → `Mono<NoteResponse>` (пустой `Mono` вместо `Optional.empty()`)
- `findAll` → `Flux<NoteResponse>`
- `remove` → `Mono<Void>`
- `existsById` → `Mono<Boolean>`

### Convention plugins (добавленные новые)

- `java-contract-conventions` — для `data-contract/`
- `java-contract-reactive-conventions` — для `data-contract-reactive/` (включает `reactor-core` как `api`)

### Стиль кода

- Импорты: `java.*` → `javax.*` → `*` → `org.springframework.*`; пустая строка между группами
- `@NullMarked` на каждый `package-info.java`
- `@Nullable` из `org.jspecify.annotations`
- `@SuppressWarnings("NullAway.Init")` на `protected` no-arg конструкторах JPA entity
- Промежуточная переменная перед `return`, не inline в `.body()`
- Строки до 120 символов

---

## Открытые решения

- **Стратегия активации адаптеров** — `@Profile("jpa")` _(склонение)_ vs отдельные `application-jpa/`
- **Управление схемой для R2DBC/JDBC** — `schema.sql` vs Flyway/Liquibase; нужно для unique constraint
  `userId+noteId` в `user-note/data-r2dbc`/`data-jdbc` (см. «Принятые решения» → «Архитектура»)
- **Регистрация auth/ ↔ user/** — Lazy / Sync / Events (Kafka)
- **Возврат мутирующего use case** — DTO _(склонение)_ vs `void`
- **PATCH** — поддерживать или нет
