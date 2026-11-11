# Дерево вариантов

> **Назначение:** «дерево вариантов» — ASCII-рендер корневого дерева вариантов архитектуры (8 листьев: sync/reactive × технология доступа × вендор)
> **Когда читать:** перед любым архитектурным решением (прогнать по всем 8 листьям), при запросе «дерево вариантов» (выводить дословно)
> **Статус:** рендер; источник истины и обсуждение — CLAUDE.md → «Архитектура и структура проекта» → «Корневое дерево вариантов»
> **Разделы:** ASCII-дерево
> **Связано:** `docs/tree-modules.md`, CLAUDE.md → «Правила» → «Любое архитектурное решение проверять на корневом дереве вариантов»

> ASCII-рендер корневого дерева вариантов архитектуры. Присылать в этом виде по запросу «дерево вариантов». **Источник истины и полное обсуждение** (зачем оно нужно, как им пользоваться, ограничения, текущее состояние 8 листьев) — `CLAUDE.md` → «Архитектура и структура проекта» → «Корневое дерево вариантов»; при правке дерева (новая ветка/вендор/технология) обновлять сначала там, потом здесь.

```
общее — модели, порты, прикладное поведение, тесты на порт: всё, что обязано быть одним для всех веток (с 2026-09-29 — contract-commons: модели запроса/ответа)
├── синхронное (webmvc) — driving-модуль controller-webmvc
│   ├── JDBC — технология в листе (spring-boot-data-jdbc), код в service-synchronous
│   │   ├── H2 — application-h2
│   │   ├── MySQL — application-mysql
│   │   └── PostgreSQL — application-postgresql
│   └── Mongo — (spring-boot-data-mongodb) — application-mongodb
└── реактивное (webflux) — driving-модуль controller-webflux
    ├── R2DBC — технология в листе (spring-boot-data-r2dbc), код в service-reactive
    │   ├── H2 — application-h2-reactive
    │   ├── MySQL — application-mysql-reactive
    │   └── PostgreSQL — application-postgresql-reactive
    └── Mongo — (spring-boot-data-mongodb-reactive) — application-mongodb-reactive
```
