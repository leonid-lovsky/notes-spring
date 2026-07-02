# CLAUDE.md — notes-spring

> Последнее обновление: 2026-07-02T11:32Z
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
build-logic/settings.gradle                     include ':convention'; included build,
                                                 подключён через pluginManagement { includeBuild(...) }
                                                 в корневом settings.gradle
build-logic/convention/src/main/groovy/         convention plugins (Groovy DSL, не Kotlin),
com.example.{name}.gradle                       namespaced id, без суффикса -conventions: слово
                                                 "convention" — только в пути (build-logic/convention/),
                                                 не в id (com.example.java-library, не -conventions)
settings.gradle                                 единственный, в корне репозитория; include ':...' на
                                                 все subprojects всех сервисов (per-service нет)
{service}/{module}/build.gradle                 применяет plugin через id 'com.example.{name}'
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
| Gradle        | 9.6.0    |
| Spring Boot   | 4.0.6    |
| Spring Cloud  | 2025.1.2 |

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

- Convention plugins — `build-logic/convention/` (included build), не `buildSrc/`: единственный
  `settings.gradle` в репозитории делает шаринг между несколькими `settings.gradle` неактуальным, но
  `build-logic` лучше по инкрементальности (правка одного convention-плагина не инвалидирует весь
  билд, как `buildSrc`) и ближе к текущей рекомендованной практике Gradle. `./gradlew clean build` из
  корня остаётся одной командой — `includeBuild` прозрачен для пересборки всего проекта целиком.
  Вложенный `convention/` (а не плагины прямо в `build-logic/`) — задел на будущие под-билды внутри
  `build-logic/`. Id namespaced (`com.example.{name}`, по аналогии с пакетами `com.example.*`),
  а не плоские — стандартная практика для precompiled script plugins, снимает конфликт имён с
  плагинами из внешних репозиториев
- Convention-плагины organized плоско в одном каталоге (`build-logic/convention/src/main/groovy/`),
  без подпапок по категориям — проверено эмпирически: каталог не влияет на id precompiled script
  plugin (Gradle берёт id только из имени файла), но у референсов (Now in Android) плагины лежат
  плоско, а сам naming (`spring-boot-*`, `spring-cloud-*`, `java-*`) уже даёт естественную
  алфавитную группировку без лишнего уровня вложенности
- Naming: id не привязанных к папке модуля плагинов (`spring-boot-client-rest`/`-client-web`,
  а не `restclient`/`webclient`) подбирается по смысловой роли, а не только по алфавиту — иначе
  риск ложной группировки (`webclient` рядом с `webflux`/`webmvc`, хотя это разные вещи: исходящий
  клиент vs driving-адаптер). Id плагинов, 1:1 соответствующих папке модуля (`webmvc`, `data-jpa`,
  `domain`, ...), никогда не переименовываются в отрыве от папки — совпадение имени плагина и
  папки модуля важнее любой алфавитной оптимизации
- Общий boilerplate convention-плагинов вынесен в `spring-boot-base`/`spring-cloud-base` (2 уровня
  иерархии), но не в третий уровень для `org.springframework.boot` + `spring-boot-starter-test`
  (нужны только 5 файлам, ~2 строки) — по принципу «три похожих строки лучше преждевременной
  абстракции», лишний уровень наследования не оправдан такой экономией
- `api` в `dependencies {}` — только когда тип зависимости используется в собственной публичной
  сигнатуре модуля (параметр/возврат публичного метода), иначе `implementation`; не полагаться на
  то, что тип и так транзитивно придёт потребителю другим путём. Проверено эмпирически на
  `reactor-core` в `com.example.java-reactor`: `webflux`/`data-r2dbc`/`data-mongodb-reactive`
  собираются с ним и как с `implementation` — `Mono`/`Flux` на их classpath приходят от
  собственных Spring-стартеров через BOM, а не транзитивно от `data-contract-reactive`, поэтому
  `api` там был не нужен (`./gradlew clean check` по всем сервисам подтвердил). `api project(':
  ...:domain')` в `data-contract*/build.gradle` — осознанное исключение: `NoteResponse` и т. п.
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
(исключение — 10 плагинов, 1:1 соответствующих папке модуля: `webmvc`, `webflux`, `data-jpa`,
`data-jdbc`, `data-r2dbc`, `data-mongodb`, `data-mongodb-reactive` — тут имя папки важнее алфавита,
не переименовывать в отрыве от неё). Плоская структура каталога (без подпапок по категориям) —
каталог не влияет на id precompiled script plugin (проверено эмпирически), а сам naming уже даёт
алфавитную группировку.

- `com.example.java` — минимальный: `java` + `java-codequality` + `junit-jupiter`. Было
  `java-domain`; переименовано — «domain» было именем архитектурного слоя, а не зависимости
- `com.example.java-library` — минимальный: `java-library` + `java-codequality`, без Spring.
  Было `java-contract`
- `com.example.java-reactor` — `com.example.java-library` + `reactor-core` (`implementation`, не
  `api` — см. «Принятые решения» → «Архитектура») с явной версией (`libs.versions.reactor.core`,
  синхронизирована с тем, что резолвит Spring Boot BOM — см. «Синхронизация версий»), без
  `io.spring.dependency-management`/BOM. Было `java-contract-reactive`, которая тянула Spring
  Boot BOM ради версии `reactor-core` — теперь `domain`/`data-contract*` полностью свободны от
  Spring
- `com.example.java-codequality` — агрегирует `com.example.java-codequality-{errorprone,jacoco,
  jacoco-report-aggregation,javaformat}` (переименованы с префиксом `java-codequality-`, были
  `java-errorprone` и т. д., без префикса)
- `com.example.spring-boot` / `com.example.spring-cloud` (`spring-cloud` применяет `spring-boot`
  и добавляет BOM `spring-cloud-dependencies`) — общий boilerplate (`java` +
  `io.spring.dependency-management` + `java-codequality` + `repositories` + BOM-импорт +
  `junit-platform-launcher` + `useJUnitPlatform()`), который раньше дублировался в 29 из 34
  файлов; остальные convention-плагины применяют один из этих двух базовых и добавляют только
  свой `dependencies {}`. Были `spring-boot-base`/`spring-cloud-base` — суффикс `-base` убран,
  как и `-conventions` ранее. Плагины, требующие `bootJar` (`spring-boot-application`,
  `spring-cloud-config-server`, `spring-cloud-eureka-server`, `spring-cloud-gateway-webflux`,
  `spring-cloud-gateway-webmvc`) добавляют `id 'org.springframework.boot'` +
  `spring-boot-starter-test` сами — 5 файлов × 2 строки не выносились в третий базовый плагин
  (см. «Принятые решения» → «Архитектура»)
- `com.example.spring-boot-client-rest` / `com.example.spring-boot-client-web` (были `restclient`/
  `webclient`) — переименованы, чтобы не смешиваться алфавитно и по смыслу с `webflux`/`webmvc`
  (это server-side driving-адаптеры, а `client-*` — исходящие HTTP-клиенты, разные вещи)

### Синхронизация версий

- **Spring-экосистема** (Spring Boot, Spring Cloud, dependency-management, spring-javaformat,
  errorprone-plugin) — единый источник `gradle/libs.versions.toml` (Gradle Version Catalog).
  `build-logic` — отдельный included build и не видит корневой `gradle.properties`/каталог
  автоматически, поэтому `build-logic/settings.gradle` подключает тот же `.toml`-файл отдельно
  (`dependencyResolutionManagement.versionCatalogs.libs.from(files('../gradle/libs.versions.toml'))`).
  Ключи с дефисом (`spring-boot`, `spring-cloud`, ...) дают вложенные аксессоры:
  `libs.versions.spring.boot.get()`, `libs.versions.spring.cloud.get()`,
  `libs.versions.spring.dependency.management.get()`, `libs.versions.spring.javaformat.get()`,
  `libs.versions.errorprone.plugin.get()`
- **reactor-core** (`libs.versions.reactor.core`, сейчас `3.8.5`) — `com.example.java-reactor`
  (используется в `domain`/`data-contract*`, без Spring) пинит версию явно, а Spring-адаптеры
  (`webflux`, `data-r2dbc`, `data-mongodb-reactive`, `spring-cloud-gateway-webflux`) получают
  `reactor-core` через `mavenBom(SpringBootPlugin.BOM_COORDINATES)`. Gradle не конфликтует —
  при расхождении версий побеждает старшая (resolution strategy «newest wins»), но конфликта
  без ручной синхронизации не избежать: при апгрейде Spring Boot нужно вручную свериться, какую
  версию `reactor-core` резолвит новый BOM (`./gradlew :note:webflux:dependencies --configuration
  compileClasspath | grep reactor-core`), и обновить `libs.versions.toml`, иначе pinned-версия в
  `domain`/`data-contract*` молча устареет и будет переопределена BOM только за счёт того, что он
  окажется новее
- **Gradle** — версия зафиксирована в `gradle/wrapper/gradle-wrapper.properties`; CI (`./gradlew`)
  наследует её автоматически, отдельной синхронизации не требует
- **Java** — единственный источник `.java-version` (корень репозитория): CI читает его через
  `actions/setup-java@v4` (`java-version-file`), Gradle — через `toolchain` в
  `com.example.java-codequality` (`JavaLanguageVersion.of(rootProject.file('.java-version').text.
  trim().toInteger())`), применяется почти во всех модулях транзитивно

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
