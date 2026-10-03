# Термины: парадигма, веб-стек, технология доступа, диалект, драйвер, вендор (набросок 2026-10-03)

> **Назначение:** различение терминов, которые постоянно путаются при обсуждении `data-*` и листьев `application-*`
> **Статус:** НАБРОСОК обсуждения 2026-10-03, не источник истины и не решение (пользователь: обсуждения — только наброски и размышления); термины подлежат уточнению
> **Когда читать:** при вопросах «что такое диалект/драйвер/вендор», «на каком уровне что лежит», «что общее у sync и reactive»
> **Связано:** `docs/tree-variants.md`, `docs/tree-repositories.md`, `docs/spring-data-repository-reference.md`, CLAUDE.md → «Открытые решения» → центральный вопрос (терминология)

## Схема по уровню зависимости

Формат: уровень — термин — перечисление. Верхний уровень зависит от всех нижних, нижний ни от чего не зависит.

- 1 — парадигма — SYNC, REACTIVE
- 2 — библиотека реактивных типов (только reactive) — Project Reactor (`reactor-core`, плагин `project-reactor`)
- 3 — веб-стек — WEBMVC, WEBFLUX
- 4 — технология доступа — sync: Spring Data JPA, Spring Data JDBC, Spring Data MongoDB; reactive: Spring Data R2DBC, Spring Data MongoDB reactive
- 5 — диалект — `H2Dialect`, `MySqlDialect`, `PostgresDialect` (Spring Data JDBC), R2DBC-диалекты; у Mongo диалекта нет
- 6 — драйвер — JDBC-драйвер, R2DBC-драйвер, Mongo-драйвер sync, Mongo-драйвер reactive streams
- 7 — вендор — H2, MySQL, PostgreSQL, MongoDB

JPA в проекте нет (`data-jpa` удалён 2026-09-13), в схеме приведён для полноты.

## Что выбирается, а что следует из выбора

- выбираются: парадигма, технология доступа, вендор; веб-стек следует из парадигмы (MVC = sync, WebFlux = reactive)
- не выбираются сами: диалект и драйвер — следуют из пары «технология × вендор»; диалект подхватывается автоматически по соединению, драйвер подключается зависимостью в листе `application-<vendor>`
- примеры пары: Spring Data JDBC × PostgreSQL → `PostgresDialect` + JDBC-драйвер PostgreSQL; Spring Data JDBC × MySQL → `MySqlDialect` + `mysql-connector-j`; Spring Data MongoDB × MongoDB → диалекта нет + Mongo-драйвер

## Слои по технологиям доступа (сверху вниз по вызову)

Сравнение по двум осям (слой × технология) — таблица (правило «Списки вместо таблиц» отменено пользователем 2026-10-03). Сверено по документации: слои Spring Data JDBC/R2DBC/Mongo, слайс-аннотации; остальное — по памяти (см. «Оговорки»). Две верхние строки — тестовые, в рабочую цепочку не входят.

| Слой | Что это | JdbcClient | Spring Data JDBC | Spring Data R2DBC | Spring Data JPA | Spring Data MongoDB | Spring Data Reactive MongoDB |
|---|---|---|---|---|---|---|---|
| Тест-слайс | аннотация теста | нет | `@DataJdbcTest` | `@DataR2dbcTest` | `@DataJpaTest` | `@DataMongoTest` | `@DataMongoTest` |
| Подготовка данных в тесте | помощник | `JdbcClient` | `JdbcClient` / `JdbcTemplate` | `DatabaseClient` | `TestEntityManager` | `MongoTemplate` | `ReactiveMongoTemplate` |
| Репозиторий | интерфейс Spring Data | нет | `CrudRepository` | `ReactiveCrudRepository` | `CrudRepository` | `CrudRepository` | `ReactiveCrudRepository` |
| Технология доступа | реализация репозитория | нет | Spring Data JDBC | Spring Data R2DBC | Spring Data JPA | Spring Data MongoDB | Spring Data Reactive MongoDB |
| Диалект | реализация, пишет SQL | нет (SQL пишет автор кода) | `JdbcDialect` | `R2dbcDialect` | `Dialect` Hibernate | нет | нет |
| Spring-обёртка | реализация | `JdbcClient` | Spring JDBC (`NamedParameterJdbcOperations`) | Spring R2DBC (`DatabaseClient`) | нет | `MongoTemplate` | `ReactiveMongoTemplate` |
| API | интерфейс | JDBC (`java.sql`) | JDBC (`java.sql`) | R2DBC SPI | JPA (`EntityManager`), затем JDBC | нет общего API | нет общего API |
| Провайдер API | реализация | нет | нет | нет | Hibernate | нет | нет |
| Драйвер | реализация API под вендора | JDBC-драйвер | JDBC-драйвер | R2DBC-драйвер | JDBC-драйвер | Mongo-драйвер (sync) | Mongo-драйвер (reactive streams) |
| Вендор | сама база | H2 / MySQL / PostgreSQL | H2 / MySQL / PostgreSQL | H2 / MySQL / PostgreSQL | H2 / MySQL / PostgreSQL | MongoDB | MongoDB |

- `JdbcClient` стоит параллельно Spring Data JDBC: общие нижние слои (API, драйвер, вендор), нет верхних (репозиторий, технология, диалект)
- Spring Data MongoDB и Reactive MongoDB — раздельные технологии (разные стартеры `data-mongodb` и `data-mongodb-reactive`, разные драйверы); слайс-аннотация одна (`docs/spring-boot-testing-reference.md`)
- Spring Data JPA в проекте нет (`data-jpa` удалён 2026-09-13)

## Определения в одну строку

- вендор — сама база данных (PostgreSQL)
- драйвер — библиотека под пару «вендор × API», общается с базой по её протоколу (`mysql-connector-j`)
- диалект — класс Spring Data (или Hibernate), который пишет SQL под вендора и учитывает особенности базы или её драйвера (`PostgresDialect`)

## Оговорки

- документация Spring Data JDBC использует слово «диалект» нестрого: перечисляет поддерживаемые базы (DB2, H2, HSQLDB, MariaDB, SQL Server, MySQL, Oracle, PostgreSQL) и говорит «если для базы нет диалекта, приложение не запустится»; диалект при этом — компонент (`JdbcDialect`), вендор — база; цитаты — docs.spring.io → Spring Data Relational → JDBC → Getting Started, R2DBC → Getting Started
- по той же документации диалект инкапсулирует поведение, специфичное для базы или её драйвера, — разграничение «диалект про вендора, драйвер про транспорт» — упрощение для ориентира
- не проверено (по памяти): имена обёрток `DatabaseClient`/`ReactiveMongoTemplate`/`MongoTemplate`; что `JdbcClient` (Spring Framework 6.1+) оборачивает `JdbcTemplate`/`NamedParameterJdbcTemplate`; `TestEntityManager` и подготовка данных в тестах (кроме `JdbcClient`); названия R2DBC-диалектов по вендорам; что R2DBC SPI и Mongo reactive streams драйвер используют интерфейс Reactive Streams, а не Reactor (по памяти); детали JPA/Hibernate
- «JDBC», «R2DBC» — название сразу нескольких слоёв (API драйвера, драйверы, Spring Data JDBC/R2DBC как слой репозиториев); JPA — спецификация, реализует Hibernate со своим `Dialect`
- Mongo повторяется в технологиях и в вендорах: технология и вендор названы одним словом

## Sync/reactive — ось, а не слой

- рассекает все уровни выше вендора: порт (`S` против `Mono<S>`), контроллер, репозиторий (`CrudRepository` против `ReactiveCrudRepository`), диалект, API драйвера (JDBC против R2DBC SPI), драйвер
- вендор общий: у одного вендора минимум два драйвера — это граница sync/reactive
- в проекте — 4 sync + 4 reactive листа; правило «Полная изоляция стеков» (CLAUDE.md → «Правила»)
