# db-migration-tools-reference.md

> Справочный обзор (2026-07-15) — инструменты миграции/версионирования схемы для каждого возможного DB-адаптера проекта. Ничего из перечисленного не подключено ни в одном convention-плагине и ни в одном сервисе — выбор конкретного инструмента остаётся открытым решением (см. CLAUDE.md → «Открытые решения»). Источники — GitHub-репозитории инструментов, официальная документация, `spring-boot-starters-reference.md` (для официальных Spring Boot стартеров); не Maven Central поиск (см. правило в CLAUDE.md).

Формат каждой строки: **инструмент** — статус/зрелость — что покрывает — цена/ограничение.

---

## data-jpa · data-jdbc (blocking JDBC, H2)

Оба адаптера используют обычное blocking JDBC-подключение к H2 — инструмент миграции для них идентичен и не зависит от того, ORM это (JPA) или Spring Data JDBC.

- **Flyway** — официальный Spring Boot 4 стартер `spring-boot-starter-flyway` (+ `spring-boot-starter-flyway-test`, оба подтверждены в `spring-boot-starters-reference.md`) — версионированные `.sql`-файлы (`V1__init.sql`), H2 поддерживается `flyway-core` из коробки, отдельный `flyway-database-*`-модуль (есть для db2/derby/hsqldb/oracle/postgresql/mysql/sqlserver) для H2 не нужен
- **Liquibase** — официальный Spring Boot 4 стартер `spring-boot-starter-liquibase` (+ `spring-boot-starter-liquibase-test`) — changelog в XML/YAML/JSON/SQL, явный rollback-трекинг, более многословный формат
- **Текущий baseline (без инструмента)** — ручной `schema.sql`, уже используется в проекте (`data-r2dbc`/`data-jdbc`, см. «Управление схемой R2DBC/JDBC» в CLAUDE.md) — без версионирования истории изменений

## data-r2dbc (H2 через R2DBC)

Нативного инструмента миграции под R2DBC не существует ни у одного из известных инструментов — R2DBC-подключение асинхронное/не-blocking, а все ниже перечисленные инструменты требуют blocking JDBC-подключения к целевой БД.

- **Flyway/Liquibase через отдельное blocking JDBC-подключение** — стандартный обход сообщества (Baeldung, Codersee): Flyway/Liquibase мигрирует по blocking JDBC к тому же файлу H2 при старте приложения, R2DBC-подключение используется только для рантайм-трафика; открытый issue [flyway/flyway#2502](https://github.com/flyway/flyway/issues/2502) («R2DBC support») висит без движения с 2019 года
- **Flamingock SQL target system** (`io.flamingock:flamingock-sql-target-system`) — на 2026-07-15 версия `1.2.0-beta.6` (beta), тоже JDBC-based, не R2DBC-native
- **Текущий baseline (без инструмента)** — ручной `schema.sql`, уже реализован в проекте, исполняется при старте `application-reactive/` — без версионирования истории

## data-mongodb (MongoDB, sync-драйвер)

- **Mongock** — зрелый, широко известный инструмент под Spring Boot ([mongock/mongock](https://github.com/mongock/mongock)/[flamingock/mongock](https://github.com/flamingock/mongock)) — ⚠️ официально переведён в maintenance mode, объявленный EOL — конец 2026 года (только критические баг-фиксы и security-патчи, без новых фич); начинать новую интеграцию на нём сейчас рискованно — см. [Sunsetting Mongock: Flamingock is its successor](https://flamingock.io/blog/sunsetting-mongock/)
- **Flamingock** — официальный преемник Mongock от тех же авторов ([flamingock/mongock-java](https://github.com/flamingock/mongock)), актуальная версия `1.4.4` (июнь 2026); MongoDB покрыт через «MongoDB Spring Data target system», путь миграции с Mongock заявлен минимальным (существующие change unit'ы переносятся без изменений); проект и документация заметно моложе Mongock
- **liquibase-mongodb** — официальное open source расширение Liquibase ([liquibase/liquibase-mongodb](https://github.com/liquibase/liquibase-mongodb)) — `createCollection`/`dropCollection`/`createIndex`/`dropIndex`/`insertMany` и т. п. через `db.runCommand()`/`db.adminCommand()`; полное покрытие произвольных команд (MongoDB Pro extension, `mongosh`-based) — только в платной Liquibase Pro лицензии
- **Flyway MongoDB (Native Connectors)** — доступен начиная с Flyway 11.x, по данным документации Redgate работает и в Community edition; миграции — JSON или JavaScript-файлы, исполняемые напрямую через `mongosh`; требует установленный `mongosh` CLI в окружении сборки/рантайма — единственный из перечисленных инструментов с зависимостью на внешний нативный бинарник, а не чистую JVM/Gradle-зависимость

## data-mongodb-reactive (MongoDB, reactive-driver)

Миграция — по своей природе последовательная blocking-операция независимо от того, каким драйвером приложение читает/пишет данные в рантайме; «reactive»-варианты инструментов ниже реактивны только в смысле совместимости с classpath, не в смысle неблокирующего исполнения самих миграций.

- **Mongock (reactive driver)** — отдельный модуль под MongoDB Reactive Streams driver ([docs.mongock.io/v5/driver/mongodb-reactive](https://docs.mongock.io/v5/driver/mongodb-reactive/index.html)), но внутри всё равно исполняет изменения синхронно — Mongock документация прямо требует блокировать все вызовы к БД внутри `changeUnit` через утилиту `MongoSubscriberSync`; тот же EOL-риск, что и у sync-варианта выше
- **Flamingock** — поддержка reactive-driver отдельно не задокументирована на момент проверки (2026-07-15); вероятно закрывается тем же MongoDB Spring Data target system, что и sync-адаптер — требует отдельной проверки перед выбором
- **liquibase-mongodb / Flyway MongoDB Native Connectors** — оба инструмента работают напрямую с БД по своему собственному подключению, не зависят от того, какой MongoDB-драйвер использует само приложение — применимы к reactive-адаптеру так же, как и к sync

## data-elasticsearch

В проекте существует convention-плагин `com.example.spring-boot-data-elasticsearch`, но ни один сервис пока не подключает его как реальный driven-адаптер (см. «Задачи» в CLAUDE.md) — раздел приведён для полноты на случай будущего подключения.

- **elasticsearch-evolution** ([senacor/elasticsearch-evolution](https://github.com/senacor/elasticsearch-evolution)) — единственный известный специализированный инструмент, вдохновлён Flyway; версионированные миграционные скрипты — обычные REST-вызовы (например `PUT` для index template), состояние выполнения хранится во внутреннем индексе Elasticsearch/OpenSearch; поддерживает и Elasticsearch, и OpenSearch; есть отдельный Spring Boot starter-артефакт от самого проекта (не от `org.springframework.boot`)
- **Ручные index templates + `_reindex` API** — без сторонней библиотеки, полный контроль, ручное отслеживание версий
- Официального Spring Boot стартера, а также поддержки Elasticsearch у Flyway/Liquibase/Mongock/Flamingock — не существует
