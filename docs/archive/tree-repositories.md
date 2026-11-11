# Дерево репозиториев

> **Назначение:** «дерево репозиториев» — иерархия интерфейсов Spring Data по вендорам проекта
> **Когда читать:** при запросе «дерево репозиториев» (выводить дословно)
> **Статус:** краткая форма; при правке сначала `docs/spring-data-repository-reference.md`, потом здесь
> **Разделы:** ASCII-дерево
> **Связано:** `docs/spring-data-repository-reference.md`

> Иерархия интерфейсов Spring Data по вендорам проекта. Присылать этот блок дословно по запросу «дерево репозиториев», без изменений формата (базовые интерфейсы + разделитель + вендорные интерфейсы с составом и impl-классом), без дополнительных комментариев сверх того, что уже в блоке. **Полный разбор** (почему у JDBC нет своего интерфейса, что реально использует проект, источники) — `docs/spring-data-repository-reference.md`; при правке дерева обновлять сначала там, потом здесь.

```
Repository<T, ID>                                   [spring-data-commons, маркер, без методов]
│
├── CrudRepository<T, ID>
│   └── ListCrudRepository<T, ID>
│
├── PagingAndSortingRepository<T, ID>
│   └── ListPagingAndSortingRepository<T, ID>
│
├── ReactiveCrudRepository<T, ID>
│
└── ReactiveSortingRepository<T, ID>

QueryByExampleExecutor<T>                            [отдельно, Repository не наследует]
ReactiveQueryByExampleExecutor<T>                    [отдельно, реактивный аналог]


────────── вендорные интерфейсы (композиция веток выше) ──────────

JPA (data-jpa) — в сборке сейчас нет (удалён 2026-09-13), дерево — для сравнения
  JpaRepository<T, ID> = ListCrudRepository<T,ID> + ListPagingAndSortingRepository<T,ID> + QueryByExampleExecutor<T>
  └── impl: SimpleJpaRepository<T, ID>

JDBC (data-jdbc) — вендорного интерфейса нет
  CrudRepository<T,ID> + PagingAndSortingRepository<T,ID> + QueryByExampleExecutor<T>
  └── impl: SimpleJdbcRepository<T, ID>

R2DBC (data-r2dbc)
  R2dbcRepository<T, ID> = ReactiveCrudRepository<T,ID> + ReactiveSortingRepository<T,ID> + ReactiveQueryByExampleExecutor<T>
  └── impl: SimpleR2dbcRepository<T, ID>

MongoDB sync (data-mongodb)
  MongoRepository<T, ID> = ListCrudRepository<T,ID> + ListPagingAndSortingRepository<T,ID> + QueryByExampleExecutor<T>
  └── impl: SimpleMongoRepository<T, ID>

MongoDB reactive (data-mongodb-reactive)
  ReactiveMongoRepository<T, ID> = ReactiveCrudRepository<T,ID> + ReactiveSortingRepository<T,ID> + ReactiveQueryByExampleExecutor<T>
  └── impl: SimpleReactiveMongoRepository<T, ID extends Serializable>
```

Правки статуса модулей (например «`data-jpa` в сборке сейчас нет») в этот блок не добавлять по умолчанию сверх уже присутствующей — актуальный статус модулей смотреть в CLAUDE.md, не дублировать сюда.
