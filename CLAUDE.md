# CLAUDE.md — notes-spring

> Последнее обновление: 2026-07-02T22:54Z
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
- **Build** — после каждого логического шага: `./gradlew clean check` (быстрее `build`: без
  `assemble`/`bootJar`) → обновить CLAUDE.md → коммит → пуш; перед коммитом дополнительно
  `./gradlew clean build` (проверяет и паковку — `bootJar`/`resolveMainClassName`, что `check`
  не покрывает)
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
СКЕЛЕТ   registry/ · config/ · gateway/ — Eureka server/client, Config server/client,
         Gateway (webflux) + Actuator подключены через convention plugins; сервисы
         стартуют и проходят contextLoads, но без routes/config-репозитория и без
         реальной регистрации note/ · user/ · user-note/ (эти сервисы пока не знают
         про eureka-client/config-client)
СКЕЛЕТ   auth/ — модуль создан (com.example.spring-boot-application), логики нет
ОТЛОЖЕНО   graphql/
ОТЛОЖЕНО   Spring Cache · Spring OAuth2 · Spring OpenFeign · Spring Cloud LoadBalancer ·
                            Spring Cloud Circuit Breaker — convention plugins подготовлены
                            в build-logic, но не применены ни в одном модуле
НЕ СОЗДАНО bff/ · thymeleaf/ · sharing/ · crud/
```

Технологии за пределами Spring-стека, отложенные на будущее (без привязки к конкретному модулю),
в логичном порядке освоения — от инструментов разработки к production-инфраструктуре и архитектурным
паттернам:

- **testcontainers** — интеграционные тесты в реальных Docker-контейнерах
- **Docker Compose** — локальный оркестратор dev/test окружения
- **Kubernetes** — production-оркестрация контейнеров
- **Terraform** — infrastructure as code: декларативное provisioning облачных ресурсов, кластеров,
  а также platform-независимая настройка репозитория (branch protection и т. п.)
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
build-logic/settings.gradle.kts                 include(":convention"); included build,
                                                 подключён через pluginManagement { includeBuild(...) }
                                                 в корневом settings.gradle.kts
build-logic/convention/src/main/kotlin/         convention plugins (Kotlin DSL — вся Gradle-конфигурация
com.example.{name}.gradle.kts                   на Kotlin, но рабочий код сервисов остаётся на Java,
                                                 см. «Принятые решения» → «Архитектура»), namespaced id,
                                                 без суффикса -conventions: слово "convention" — только
                                                 в пути (build-logic/convention/), не в id
                                                 (com.example.library, не -conventions)
settings.gradle.kts                             единственный, в корне репозитория; include(":...") на
                                                 все subprojects всех сервисов (per-service нет)
{service}/{module}/build.gradle.kts             применяет plugin через id("com.example.{name}")
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
data-contract-reactive/ reactive port interfaces     → domain/ (api) · reactor-core (implementation)
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

| Элемент                 | Паттерн                        | Пример                    |
|-------------------------|--------------------------------|---------------------------|
| Sync port interface     | `{Entity}{Op}Contract`         | `NoteAddContract`         |
| Reactive port interface | `{Entity}{Op}ContractReactive` | `NoteAddContractReactive` |
| Adapter impl            | `{Entity}{Op}{Tech}Adapter`    | `NoteAddJpaAdapter`       |
| Mapper interface        | `{Entity}{Tech}MapperContract` | `NoteJpaMapperContract`   |
| Mapper impl             | `{Entity}{Tech}Mapper`         | `NoteJpaMapper`           |
| Spring Data repo        | `{Entity}{Tech}Repository`     | `NoteJpaRepository`       |
| JPA / R2DBC model class | `{Entity}Entity`               | `NoteEntity`              |
| MongoDB model class     | `{Entity}Document`             | `NoteDocument`            |

**Tech**: `Jpa` · `Mongo` · `Jdbc` · `R2dbc` · `MongoReactive`

---

## Стек

| Инструмент   | Версия   |
|--------------|----------|
| Java         | 21       |
| Gradle       | 9.6.0    |
| Spring Boot  | 4.0.6    |
| Spring Cloud | 2025.1.2 |

**Spring Boot 4 — критичные отличия от Boot 3** (источник: существующие `build.gradle.kts` в проекте;
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

- Convention plugins — `build-logic/convention/` (included build), не `buildSrc/`: единственный
  `settings.gradle.kts` в репозитории делает шаринг между несколькими `settings.gradle.kts`
  неактуальным, но
  `build-logic` лучше по инкрементальности (правка одного convention-плагина не инвалидирует весь
  билд, как `buildSrc`) и ближе к текущей рекомендованной практике Gradle. `./gradlew clean build` из
  корня остаётся одной командой — `includeBuild` прозрачен для пересборки всего проекта целиком.
  Вложенный `convention/` (а не плагины прямо в `build-logic/`) — задел на будущие под-билды внутри
  `build-logic/`. Id namespaced (`com.example.{name}`, по аналогии с пакетами `com.example.*`),
  а не плоские — стандартная практика для precompiled script plugins, снимает конфликт имён с
  плагинами из внешних репозиториев
- Convention-плагины organized плоско в одном каталоге (`build-logic/convention/src/main/kotlin/`),
  без подпапок по категориям — проверено эмпирически: каталог не влияет на id precompiled script
  plugin (Gradle берёт id только из имени файла), но у референсов (Now in Android) плагины лежат
  плоско, а сам naming (`spring-boot-*`, `spring-cloud-*`, ...) уже даёт естественную алфавитную
  группировку без лишнего уровня вложенности
- Вся Gradle-конфигурация (convention-плагины, `settings.gradle.kts`, все `build.gradle.kts`
  модулей) — на Kotlin DSL, было Groovy. Рабочий код сервисов остаётся на Java — миграция затронула
  только build-скрипты. Мигрировали пошагово (сначала `build-logic`, потом внешний слой), проверяя
  `./gradlew clean build` на каждом шаге — обошлось без костылей, кроме одного легитимного
  (не костыль, а официально документированное ограничение Gradle, см. docs.gradle.org →
  Version Catalogs): precompiled script plugins не видят typed-accessor каталога
  (`libs.versions.x.get()`) тем же способом, что обычные build-скрипты — версии читаются через
  локальную `val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")`
  в начале каждого нуждающегося в версиях плагина + `libs.findVersion("key").get()
  .requiredVersion` — ровно паттерн из официальной документации, без отдельного shared-файла
  (пробовали `Catalogs.kt` с shared extension property — работало, но не соответствовало
  документации и добавляло лишний файл не туда, куда нужно; сошлись на локальном объявлении
  в каждом файле). На время миграции `build-logic/convention/build.gradle.kts`
  временно применял оба плагина (`kotlin-dsl` + `groovy-gradle-plugin`), чтобы старые
  (`src/main/groovy/`) и новые (`src/main/kotlin/`) precompiled-плагины сосуществовали в одном
  модуле `convention` — подтверждено эмпирически, оба source set компилируются независимо
  без конфликтов
- Naming: id не привязанных к папке модуля плагинов (`spring-boot-client-rest`/`-client-web`,
  а не `restclient`/`webclient`) подбирается по смысловой роли, а не только по алфавиту — иначе
  риск ложной группировки (`webclient` рядом с `webflux`/`webmvc`, хотя это разные вещи: исходящий
  клиент vs driving-адаптер). Id плагинов, 1:1 соответствующих папке модуля и реальному Spring Boot
  starter (`webmvc`, `webflux`, `data-jpa`, `data-jdbc`, `data-r2dbc`, `data-mongodb`,
  `data-mongodb-reactive`), никогда не переименовываются в отрыве от папки. `domain/` и
  `data-contract*/` в это правило не попадают — их плагины (`java`, `library`, `reactor`)
  называются по зависимости, а не по слою, и папке уже не соответствуют
- Общий boilerplate convention-плагинов вынесен в `spring-boot`/`spring-cloud` (2 уровня
  иерархии), но не в третий уровень для `org.springframework.boot` + `spring-boot-starter-test`
  (нужны только 5 файлам, ~2 строки) — по принципу «три похожих строки лучше преждевременной
  абстракции», лишний уровень наследования не оправдан такой экономией
- Convention-плагины (`build-logic/`) и оба `settings.gradle.kts` — отступ 4 пробела, не табы:
  `.springjavaformatconfig` (`indentation-style=spaces`) — источник истины по стилю для всего
  репозитория, а `build-logic/` изначально не форматировался этим правилом (Spring JavaFormat
  форматирует Java, не Groovy build-скрипты) и разошёлся на табы. Плагин `idea` (был в бывшем
  `java-domain`, контент которого сейчас — часть `com.example.base`) — удалён: deprecated
  в Gradle, будет убран в Gradle 10, и был применён только в `domain/`-модулях (несогласованно,
  больше нигде в плагинах)
- Иерархия convention-плагинов — максимум один родительский `com.example.*`-плагин, кроме
  агрегирующего `com.example.codequality` (собирает 4 плагина) и `spring-boot-client-web`
  (см. ниже, по прямому запросу). Корень —
  `com.example.base`: `java` + toolchain + `com.example.codequality` + `junit-jupiter` слиты
  в одном плагине (были раздельными — `java-codequality` и `com.example.junit-jupiter`).
  `codequality`-плагины (`errorprone`, `jacoco`, `jacoco-report-aggregation`, `javaformat`)
  сами применяют голый `id("java")` (Gradle core), не `com.example.base` — иначе цикл
  `base → codequality → errorprone → base → ...`. От `com.example.base` линейно строятся
  `library` → `reactor` и `spring-boot` → `spring-cloud` → технологические листья. Раньше
  каждый технологический плагин (и даже сами codequality-плагины) независимо применяли голый
  `id("java")` без общего корня — теперь все они (кроме самих codequality-детей) трассируются
  к одному `com.example.base`. `jacoco-report-aggregation` — без родителя вообще: плагину не
  нужен `java` функционально, лишнее не настраивается (autoconfiguration/defaults — не
  подгонять структуру под единообразие там, где это не даёт реальной пользы)
- Type-safe project accessors (`projects.note.domain` вместо `project(':note:domain')`) —
  включены через `enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")` в корневом `settings.gradle.kts`;
  требуют явного `rootProject.name` в обоих `settings.gradle.kts` (иначе Gradle предупреждает о
  нестабильности между чекаутами — исправлено там же). Фича остаётся incubating в Gradle 9.6.0
  (не graduated to stable с версии 7.0), но работает без единого костыля — проверено
  `./gradlew clean build` по всем сервисам
- `api` в `dependencies {}` — только когда тип зависимости используется в собственной публичной
  сигнатуре модуля (параметр/возврат публичного метода), иначе `implementation`; не полагаться на
  то, что тип и так транзитивно придёт потребителю другим путём. Проверено эмпирически на
  `reactor-core` в `com.example.reactor`: `webflux`/`data-r2dbc`/`data-mongodb-reactive`
  собираются с ним и как с `implementation` — `Mono`/`Flux` на их classpath приходят от
  собственных Spring-стартеров через BOM, а не транзитивно от `data-contract-reactive`, поэтому
  `api` там был не нужен (`./gradlew clean check` по всем сервисам подтвердил). `api project(':
  ...:domain)` в `data-contract*/build.gradle.kts` — осознанное исключение: `NoteResponse` и т. п.
  больше неоткуда получить, это единственный путь
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

### Convention plugins — принцип именования и структура

Id называется по подключаемой зависимости/технологии, а не по имени использующего модуля/слоя
(исключение — 7 плагинов, 1:1 соответствующих папке модуля и реальному Spring Boot starter:
`webmvc`, `webflux`, `data-jpa`, `data-jdbc`, `data-r2dbc`, `data-mongodb`, `data-mongodb-reactive`
— тут имя папки важнее алфавита, не переименовывать в отрыве от неё). Плоская структура каталога
(без подпапок по категориям) — каталог не влияет на id precompiled script plugin (проверено
эмпирически), а сам naming уже даёт алфавитную группировку. Префиксы убраны везде, кроме семей
`spring-boot-*`/`spring-cloud-*` — остальные плагины называются голым именем зависимости
(`errorprone`, `jacoco`, `library`, `reactor`, ...).

**Иерархия** — максимум 1 родительский `com.example.*`-плагин на плагин, кроме агрегирующего
`codequality` (собирает 4 плагина) и `spring-boot-client-web` (2 родителя по прямому запросу,
см. «Принятые решения» → «Архитектура»):
```
codequality-{errorprone,jacoco,javaformat} — родитель голый id("java") (Gradle core), НЕ
                                              com.example.base: иначе цикл base → codequality →
                                              errorprone → base → ...
jacoco-report-aggregation                  — вообще без родителя: плагину не нужен java
                                              функционально, лишнее не настраиваем (autoconfig)
codequality                                — агрегатор 4 плагинов выше

com.example.base (root)  — java + toolchain + junit-jupiter + codequality (1 родитель: codequality)
├── library                — java-library (Gradle core), без Spring
│   └── reactor             — reactor-core + reactor-test, явная версия, без Spring BOM
└── spring-boot             — io.spring.dependency-management + Spring Boot BOM
    ├── spring-boot-*        (17 технологических плагинов)
    └── spring-cloud         — + BOM spring-cloud-dependencies
        └── spring-cloud-*    (9 технологических плагинов)
```

- `com.example.base` — корень: `java` + toolchain (версия из `.java-version`, читается через
  `providers.fileContents(...).asText` — Provider API, а не `file.text` напрямую: корректно
  отслеживается configuration cache без дополнительных костылей) + `com.example.codequality`
  + `junit-jupiter`/`junit-platform-launcher` (testImplementation/testRuntimeOnly, обе версии
  из каталога) + `useJUnitPlatform()`. Раньше codequality и junit жили в отдельных плагинах
  (`java-codequality`, `com.example.junit-jupiter`) — теперь всё стянуто в один базовый
  `com.example.base`, и `domain/` (плоские Java-модули) применяет его напрямую, без отдельного
  identity для junit
- `com.example.codequality-{errorprone,jacoco,jacoco-report-aggregation,javaformat}` переименованы
  в `com.example.{errorprone,jacoco,jacoco-report-aggregation,javaformat}` — без префиксов
  `java-`/`codequality-` вообще. Каждый (кроме jacoco-report-aggregation) сам объявляет голый
  `id("java")` + `repositories { mavenCentral() }` — не наследует их от `com.example.base` (иначе
  цикл, см. иерархию выше)
- `com.example.library` (был `java-contract`, затем `java-library`) — 1 родитель `com.example.base`;
  добавляет Gradle-плагин `java-library`, без Spring
- `com.example.reactor` (был `java-contract-reactive`, затем `java-reactor`) — 1 родитель `library`;
  `reactor-core` (`implementation`, не `api` — см. «Принятые решения» → «Архитектура») + теперь
  также `reactor-tools` (`implementation`) и `reactor-test` (`testImplementation`), версии — явные,
  из `libs.versions.reactor.core` (синхронизирована с тем, что резолвит Spring Boot BOM — см.
  «Синхронизация версий»), без `io.spring.dependency-management`/BOM — `domain`/`data-contract*`
  полностью свободны от Spring. `reactor-tools` сам по себе не активен — нужен явный вызов
  `ReactorDebugAgent.init()` в коде (обычно в `main()`) или `-javaagent`, просто наличие jar'а
  в classpath ничего не делает
- `com.example.spring-boot` / `com.example.spring-cloud` — 1 родитель `com.example.base`/
  `spring-boot` соответственно (`spring-cloud` применяет `spring-boot` и добавляет BOM
  `spring-cloud-dependencies`) — `io.spring.dependency-management` + Spring Boot BOM; свой
  junit-platform-launcher/`useJUnitPlatform()` убран как дублирующий то, что уже даёт родитель
  `com.example.base`. Были `spring-boot-base`/`spring-cloud-base` — суффикс `-base` убран, как
  и `-conventions` ранее. Плагины, требующие `bootJar` (`spring-boot-application`,
  `spring-cloud-config-server`, `spring-cloud-eureka-server`, `spring-cloud-gateway-webflux`,
  `spring-cloud-gateway-webmvc`) добавляют `id("org.springframework.boot")` +
  `spring-boot-starter-test` сами — 5 файлов × 2 строки не выносились в третий базовый плагин
  (см. «Принятые решения» → «Архитектура»)
- `com.example.spring-boot-client-rest` / `com.example.spring-boot-client-web` (были `restclient`/
  `webclient`) — переименованы, чтобы не смешиваться алфавитно и по смыслу с `webflux`/`webmvc`
  (это server-side driving-адаптеры, а `client-*` — исходящие HTTP-клиенты, разные вещи).
  `spring-boot-client-web` — единственное исключение из правила «1 родитель»: применяет и
  `spring-boot`, и `reactor` (по прямому запросу, а не автономным решением) — `reactor-test`
  для `spring-boot-starter-webclient` теперь приходит из `com.example.reactor`, а не отдельной
  версией из Spring Boot BOM. Риск тот же, что описан для `reactor-core`/`junit` выше
  («Синхронизация версий») — два независимых источника версии для одного артефакта; пока не
  проявлялся, но не исключён при апгрейде Spring Boot

### Синхронизация версий

- **Spring-экосистема** (Spring Boot, Spring Cloud, dependency-management, spring-javaformat,
  errorprone-plugin) — единый источник `gradle/libs.versions.toml` (Gradle Version Catalog).
  `build-logic` — отдельный included build и не видит корневой `gradle.properties`/каталог
  автоматически, поэтому `build-logic/settings.gradle.kts` подключает тот же `.toml`-файл отдельно
  (`dependencyResolutionManagement.versionCatalogs.create("libs") { from(files("../gradle/
  libs.versions.toml")) }`). В обычных Kotlin build-скриптах (`build-logic/convention/
  build.gradle.kts`) ключи с дефисом (`spring-boot`, `spring-cloud`, ...) дают typed-аксессоры:
  `libs.versions.spring.boot.get()`, `libs.versions.spring.cloud.get()`,
  `libs.versions.spring.dependency.management.get()`, `libs.versions.spring.javaformat.get()`,
  `libs.versions.errorprone.plugin.get()`. Внутри самих precompiled script plugins
  (`com.example.{name}.gradle.kts`) этот typed-аксессор недоступен (ограничение Gradle) — там
  `libs.findVersion("spring-cloud").get().requiredVersion` (см. «Принятые решения» →
  «Архитектура»)
- **Инструменты codequality** (`jspecify`, `errorprone-core`, `nullaway`, `jacoco`) — тоже через
  каталог (`libs.versions.jspecify.get()` и т. д.), не зашиты текстом в `com.example.errorprone`/
  `-jacoco` — раньше были разбросаны по файлам как строковые литералы
- **junit-jupiter / junit-platform** — оба явно из каталога в `com.example.base`. Реальный баг,
  найденный при объединении `java` с codequality/junit (см. «Принятые решения» → «Архитектура»):
  каталог был запинен на `junit-jupiter = "5.12.2"` (унаследовано от домена, где Spring не
  участвовал), но Spring Boot 4.0.6 фактически управляет **JUnit 6** (`junit-bom:6.0.3`) —
  расхождение было незаметно, пока `spring-boot`-плагины не стали наследовать `java` и не
  получили одновременно 5.x (из явного пина) и 6.x (из Spring BOM) — `TestEngine with ID
  'junit-jupiter' failed to discover tests` из-за рассинхрона `junit-platform-launcher`/
  `-engine`. Исправлено: `junit-jupiter = "6.0.3"`, `junit-platform = "6.0.3"` — в JUnit 6
  Platform/Jupiter унифицировали нумерацию версий (раньше Platform жил на отдельной ветке `1.x`)
- **reactor-core** (`libs.versions.reactor.core`, сейчас `3.8.5`) — `com.example.reactor`
  (используется в `domain`/`data-contract*`, без Spring) фиксирует версию явно, а Spring-адаптеры
  (`webflux`, `data-r2dbc`, `data-mongodb-reactive`, `spring-cloud-gateway-webflux`) получают
  `reactor-core` через `mavenBom(SpringBootPlugin.BOM_COORDINATES)`. Gradle не конфликтует —
  при расхождении версий побеждает старшая (resolution strategy «newest wins»), но конфликта
  без ручной синхронизации не избежать: при апгрейде Spring Boot нужно вручную свериться, какую
  версию `reactor-core` резолвит новый BOM (`./gradlew :note:webflux:dependencies --configuration
  compileClasspath | grep reactor-core`), и обновить `libs.versions.toml`, иначе pinned-версия в
  `domain`/`data-contract*` молча устареет и будет переопределена BOM только за счёт того, что он
  окажется новее. Тот же риск актуален и для `junit-jupiter`/`junit-platform` выше — проверять
  оба при каждом апгрейде Spring Boot
- **Gradle** — версия зафиксирована в `gradle/wrapper/gradle-wrapper.properties`; CI (`./gradlew`)
  наследует её автоматически, отдельной синхронизации не требует
- **Java** — единственный источник `.java-version` (корень репозитория): CI читает его через
  `actions/setup-java@v4` (`java-version-file`), Gradle — через `toolchain` в `com.example.base`.
  Читается через `providers.fileContents(rootProject.layout.projectDirectory.file('.java-version'))
  .asText.get().trim().toInteger()` (Provider API), не `rootProject.file(...).text` напрямую —
  корректно отслеживается configuration cache; применяется почти во всех модулях транзитивно

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
