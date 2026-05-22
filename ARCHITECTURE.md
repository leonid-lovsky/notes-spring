# Architecture

## Структура проекта

Multi-service Spring Boot 4 монорепо. Каждый сервис — отдельный Gradle composite build.

```
notes-spring/
├── build-logic/         convention plugins

├── crud/                generic CRUD library (без application/)
│   ├── domain/
│   ├── webmvc/
│   └── data-jpa/

├── auth/                Spring Authorization Server
│   ├── domain/
│   ├── application/
│   ├── webmvc/
│   └── data-jpa/

├── user/
│   ├── domain/
│   ├── application/
│   ├── webmvc/
│   └── data-jpa/

├── note/
│   ├── domain/
│   ├── application/
│   ├── webmvc/
│   └── data-jpa/

├── user-note/
│   ├── domain/
│   ├── application/
│   ├── webmvc/
│   ├── data-jpa/
│   └── feign/           OpenFeign + Circuit Breaker → user, note

├── gateway/             Spring Cloud Gateway
│   └── application/

├── registry/            Eureka Server (Service Discovery)
│   └── application/

└── config/              Spring Cloud Config Server
    └── application/
```

## Domain models

| Сервис      | Entity                                                          |
|-------------|-----------------------------------------------------------------|
| `auth`      | `AuthUser { id, username, passwordHash, refreshToken }`        |
| `user`      | `User { id, username, email }`                                  |
| `note`      | `Note { id, content }`                                          |
| `user-note` | `UserNote { id, userId, noteId, role }`                         |

`UserNote.role`: `CREATOR` | `OWNER` | `EDITOR` | `VIEWER`

## Hexagonal Architecture

Каждый бизнес-сервис следует ports & adapters:

```
domain/       чистый Java, без фреймворков: entities + port interfaces
webmvc/       входящий HTTP адаптер        → domain/
data-jpa/     persistence адаптер          → domain/
feign/        исходящий HTTP адаптер       → domain/
application/  Spring Boot, use cases,
              composition root             → domain/ + все адаптеры
```

Адаптеры зависят только от `domain/` — ничего не знают про `application/`.

## Spring Cloud компоненты

| Компонент               | Тип             | Модуль              |
|-------------------------|-----------------|---------------------|
| Spring Cloud Gateway    | сервис          | `gateway/`          |
| Eureka Server           | сервис          | `registry/`         |
| Spring Cloud Config     | сервис          | `config/`           |
| Spring Authorization Server | сервис      | `auth/`             |
| Spring Cloud OpenFeign  | библиотека      | `*/feign/`          |
| Spring Circuit Breaker  | библиотека      | `*/feign/`          |

## Взаимодействие

```
Client → gateway → auth
                 → user
                 → note
                 → user-note → user  (feign)
                             → note  (feign)

все сервисы → registry  (Eureka)
все сервисы → config    (Spring Cloud Config)
```

## Внешние инструменты

- Prometheus + Grafana — метрики
- Zipkin — distributed tracing
- ELK / Loki — централизованные логи
