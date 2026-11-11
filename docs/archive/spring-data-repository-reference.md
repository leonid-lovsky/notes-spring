# Справочник: иерархия репозиториев Spring Data по вендорам проекта

> **Назначение:** иерархия интерфейсов репозиториев Spring Data (`Repository`→`CrudRepository`/`PagingAndSortingRepository`→list-варианты, `QueryByExampleExecutor`) по вендорам проекта с классами-реализациями
> **Когда читать:** при выборе базового интерфейса репозитория для `data-*`, при вопросе об общем generic-репозитории
> **Статус:** справочник 2026-09-22 по Javadoc и исходникам spring-projects
> **Разделы:** «Базовые интерфейсы», «Дерево (наглядный вид)», «По вендорам проекта», «Почему у JDBC нет своего интерфейса, а у Mongo/R2DBC есть», «Что реально использует проект», «Источники»
> **Связано:** `docs/tree-repositories.md` (краткое дерево для вывода по запросу)

> Собрано 2026-09-22 по официальным источникам — Javadoc `docs.spring.io` (секции «All Superinterfaces»/«All Known Implementing Classes», читаны дословно) и исходники `github.com/spring-projects/spring-data-{commons,relational,mongodb}`. Повод — вопрос «почему у JDBC нет своего интерфейса репозитория, а у Mongo/R2DBC есть» и разбор возможности общего generic-репозитория для `data-jdbc`/`data-r2dbc`/`data-mongodb`/`data-mongodb-reactive`, см. `CLAUDE.md` → «Открытые решения» → центральный вопрос архитектуры и «Граница порта».

## Базовые интерфейсы (`spring-data-commons`)
- `Repository<T, ID>` — маркер-интерфейс без методов, корень всей иерархии
- `Repository` → `CrudRepository<T, ID>` → `ListCrudRepository<T, ID>` (те же методы, `List` вместо `Iterable`)
- `Repository` → `PagingAndSortingRepository<T, ID>` → `ListPagingAndSortingRepository<T, ID>` — независимая от `CrudRepository` ветка (в Spring Data 3.0 разъединены — `PagingAndSortingRepository` больше не наследует `CrudRepository`, оба напрямую под `Repository`)
- `Repository` → `ReactiveCrudRepository<T, ID>` — реактивный аналог `CrudRepository`
- `Repository` → `ReactiveSortingRepository<T, ID>` — реактивный аналог `PagingAndSortingRepository`
- `QueryByExampleExecutor<T>` / `ReactiveQueryByExampleExecutor<T>` — НЕ наследуют `Repository` вообще, отдельная ветка (один type-параметр, без `ID`), примешивается вендорными интерфейсами через множественный `extends`
- вне интереса проекта (не используются): `RevisionRepository<T, ID, N>` (auditing/versioning), `RxJava3CrudRepository<T, ID>`/`RxJava3SortingRepository<T, ID>` (стек RxJava3 — у проекта Reactor/WebFlux)

## Дерево (наглядный вид)

Вынесено в отдельный файл `docs/tree-repositories.md` 2026-09-22 («дерево репозиториев») — присылать дословно оттуда по соответствующему запросу.

## По вендорам проекта
Схема: вендор (модуль) — интерфейс — состав (`extends`) — класс-реализация:
- **JPA** (`data-jpa`, в сборке нет с 2026-09-13, здесь для сравнения) — `JpaRepository<T, ID>` — `ListCrudRepository<T,ID>` + `ListPagingAndSortingRepository<T,ID>` + `QueryByExampleExecutor<T>` — `SimpleJpaRepository<T, ID>` (формально `implements JpaRepositoryImplementation<T,ID>` — подинтерфейс `JpaRepository`, добавляющий ещё `JpaSpecificationExecutor<T>`+`JpaRepositoryConfigurationAware`)
- **JDBC** (`data-jdbc`) — вендорного интерфейса нет — используется прямая композиция `CrudRepository<T,ID>` + `PagingAndSortingRepository<T,ID>` + `QueryByExampleExecutor<T>` — `SimpleJdbcRepository<T, ID>` (формально `implements` только эту тройку, хотя тела методов `findAll()`/`findAllById(...)`/`saveAll(...)` уже `List`-возвращающие — совместимо и с `ListCrudRepository` через рефлексивный dispatch прокси, не через статический cast)
- **R2DBC** (`data-r2dbc`) — `R2dbcRepository<T, ID>` — `ReactiveCrudRepository<T,ID>` + `ReactiveSortingRepository<T,ID>` + `ReactiveQueryByExampleExecutor<T>`, пустой (не добавляет методов) — `SimpleR2dbcRepository<T, ID>`
- **MongoDB sync** (`data-mongodb`) — `MongoRepository<T, ID>` — `ListCrudRepository<T,ID>` + `ListPagingAndSortingRepository<T,ID>` + `QueryByExampleExecutor<T>` — `SimpleMongoRepository<T, ID>`
- **MongoDB reactive** (`data-mongodb-reactive`) — `ReactiveMongoRepository<T, ID>` — `ReactiveCrudRepository<T,ID>` + `ReactiveSortingRepository<T,ID>` + `ReactiveQueryByExampleExecutor<T>` — `SimpleReactiveMongoRepository<T, ID extends Serializable>` — единственная из всех реализация с доп. ограничением на `ID` (`Serializable`), асимметрия внутри самой Mongo-пары sync/reactive, не между вендорами

## Почему у JDBC нет своего интерфейса, а у Mongo/R2DBC есть
- **JDBC** — архитектурно нечего добавлять: нет persistence context (нет ленивой загрузки, dirty tracking, сессии — `docs.spring.io` → «Why Spring Data JDBC?», дословно: «If you load an entity, SQL statements get run… If you save an entity, it gets saved. If you do not, it does not»), JPA-специфичные методы (`flush`/`getReferenceById`) без `EntityManager` реализовывать не на чем. Подтверждено напрямую мейнтейнером Spring Data — Mark Paluch, `github.com/spring-projects/spring-data-relational` issue #772 (2020, ex-DATAJDBC-552): «Right now, we do not declare a `JdbcRepository` interface as there was no need to do so». Запрошенную в том issue возможность (`List`-возвращающий `saveAll` вместо `Iterable`-возвращающего) решили не точечно для JDBC, а в `spring-data-commons` — так появился `ListCrudRepository`, доступный всем вендорам сразу
- **MongoDB** — интерфейс не пустой: добавляет `insert(S)`/`insert(Iterable<S>)` (sync) и `Mono<S> insert(S)`/`Flux<S> insert(Iterable<S>)` (reactive) — операцию «считать сущность новой, оптимизировать под чистую вставку», отличную по семантике от `save()` (upsert); отражает реальное различие Mongo-драйвера (`insertOne`/`insertMany` против `replaceOne` с upsert)
- **R2DBC** — интерфейс есть, но пустой (`extends ReactiveCrudRepository, ReactiveSortingRepository, ReactiveQueryByExampleExecutor {}`, ни одного своего метода); Javadoc и исходник причину не объясняют. Вероятное объяснение (НЕ подтверждено документацией, вывод по косвенным признакам) — единообразие имён между store-модулями Spring Data (`JpaRepository`/`MongoRepository`/`R2dbcRepository` — по одной точке входа на стор), унаследованное с тех пор, когда R2DBC был отдельным от `spring-data-relational` проектом

## Что реально использует проект
- Все 4 `data-*`-модуля (`data-jdbc`/`data-mongodb` — sync, `data-r2dbc`/`data-mongodb-reactive` — reactive) каждого из трёх сервисов объявляют `{E}Repository` (`NoteRepository`/`UserRepository`/`UserNoteRepository`, 2026-09-29) через голые `ListCrudRepository`/`ReactiveCrudRepository` из `spring-data-commons`, не через вендорные `MongoRepository`/`ReactiveMongoRepository`/`R2dbcRepository` — то есть уже единообразно на самом нижнем общем узле; вендорную «надбавку» (`insert(...)` у Mongo) нигде не используют
- Связано с вопросом общего generic-репозитория для `data-*` (обобщённый предок, параметризованный типом сущности, отдельно sync/reactive из-за `reactor-core`) — см. `CLAUDE.md` → «Открытые решения» → «ОТКРЫТЫЙ ВОПРОС ВСЕЙ АРХИТЕКТУРЫ — как писать тесты и избегать дублирования в мульти-вендорном приложении»

## Как репозиторий становится бином и зачем не нужна `@Repository` (2026-10-03)

- проверено по документации Spring Data Commons 4.1.1 («Creating Repository Instances») и Spring Boot («SQL Databases»): `@Repository` на интерфейсе репозитория не нужна; инфраструктура Spring Data сканирует базовый пакет и находит каждый интерфейс, наследующий `Repository` технологии; бин создаёт `FactoryBean` технологии (прокси), имя бина — по имени интерфейса (`UserNoteRepository` → `userNoteRepository`)
- в Spring Boot базовый пакет по автоконфигурации — пакет `@SpringBootApplication`/`@EnableAutoConfiguration` и его подпакеты; `@EnableJdbcRepositories`/`@EnableJpaRepositories` нужны только чтобы взять настройку под свой контроль
- `@Repository` из Spring Framework — другой механизм (метка класса для сканирования компонентов, как `@Service`); у репозитория бин создаётся первым механизмом, второй не нужен
- не проверено по документации (по памяти): что сканирование компонентов пропускает интерфейсы; что `@Repository` включает перевод исключений хранилища в `DataAccessException` и что перевод в репозиториях Spring Data работает без неё; нужна ли `@Repository` классу `…Impl` фрагмента
- проверено запуском 2026-10-03: слайс-тесты `contextLoads()` всех четырёх `data-*` проходят (`--rerun`) с одним `@Id` на сущности и `@Service` на сервисе, без `@Repository`/`@Table`/`@Document`; запуск не доказывает работу с хранилищем (схема, `save`, запросы)
- `@Table`/`@Document`/`@Column`/`@Field` для запуска не нужны (2026-10-03): задают необязательные имена, без них действуют правила по умолчанию — JDBC/R2DBC snake_case (`UserNoteEntity` → таблица `user_note`, `userId` → колонка `user_id`), Mongo — короткое имя класса в camelCase (`userNote`), поля из полей записи (документация Spring Data Relational/MongoDB Mapping); сущность находится через репозиторий `CrudRepository<UserNote, UUID>`, нужен только `@Id`; таблицу/коллекцию при старте стартер не открывает — имя нужно при первом запросе, поэтому `contextLoads()` проходит; прямой фразы «`@Table` необязательна» в документации не читали — вывод из правил имён по умолчанию и из запуска; понадобятся, если имя по умолчанию не подходит, или при нескольких модулях Spring Data на classpath (строгий режим, ниже)
- строгий режим: если на classpath несколько модулей Spring Data, репозитории привязываются по интерфейсу технологии или по аннотации сущности (`@Entity`/`@Document`); у проекта один модуль на лист — не мешает

## Источники
- Javadoc (дословно читаны секции «All Superinterfaces»/«All Known Implementing Classes», проверено 2026-09-22): `docs.spring.io/spring-data/{commons,jpa,jdbc,r2dbc,mongodb}/docs/current/api/...`
- `github.com/spring-projects/spring-data-relational` issue #772 (ex-DATAJDBC-552)
- Исходники: `github.com/spring-projects/spring-data-{relational,mongodb}` (`SimpleJdbcRepository.java`/`SimpleR2dbcRepository.java`/`SimpleMongoRepository.java`/`SimpleReactiveMongoRepository.java`/`MongoRepository.java`/`ReactiveMongoRepository.java`)
