# CLAUDE.md — notes-spring

> Последнее обновление: 2026-06-30T19:30Z
> **Всё временно** — любое решение подлежит обсуждению и изменению.

---

## Начало каждой сессии

1. Прочитать этот файл
2. Текущий приоритет — см. раздел **Задачи**
3. Актуализировать файл, убрать устаревшее
4. Коммит + пуш _(постоянная авторизация)_

---

## Правила

- **Язык** — общение на русском; код, идентификаторы — на английском
- **Файлы** — не изменять без явного указания; перед созданием изучить проект
- **Коммиты** — не коммитить без явного запроса _(исключение: начало сессии)_; «Зафиксируй» = обновить CLAUDE.md + дату → коммит → пуш
- **Дата перед коммитом** — `date -u +"%Y-%m-%dT%H:%MZ"`
- **CI** — не трогать `.github/workflows/` без запроса
- **Таблицы** — все строки одинаковой длины в Unicode-символах
- **Архитектурные решения** — изложить варианты + плюсы/минусы + склонение, дождаться выбора; не реализовывать до явного решения
- **Build** — после каждого логического шага: `./gradlew clean build` → CLAUDE.md → коммит → пуш
- **Подзадачи** — крупные задачи разбивать на подзадачи; коммитить+пушить после каждой выполненной подзадачи во избежание потерь при обрыве сессии

---

## Навигация по проекту

**Физическая структура:**
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

**Структура driven адаптера (пример data-jpa/):**
```
com.example.note.data.jpa.adapter      NoteAddJpaAdapter, ...
com.example.note.data.jpa.model        NoteEntity
com.example.note.data.jpa.mapper       NoteEntityMapperContract, NoteEntityMapper
com.example.note.data.jpa.repository   NoteJpaRepository
```
Все технологии используют `model/` (entity/document — внутренние имена классов). Driving адаптеры (`webmvc/`, `webflux/`) — плоско.

---

## ⚡ Задачи

```
ГОТОВО   note/  — domain · data-contract · data-contract-reactive · webmvc · webflux
                  data-jpa · data-mongodb · data-jdbc · data-r2dbc · data-mongodb-reactive
ГОТОВО   user/  — domain · data-contract · data-contract-reactive · webmvc · webflux
                  data-jpa · data-mongodb · data-jdbc · data-r2dbc · data-mongodb-reactive
ГОТОВО   user-note/ — domain · data-contract · data-contract-reactive · webmvc · webflux
                      data-jpa · data-mongodb · data-jdbc · data-r2dbc · data-mongodb-reactive
ОТЛОЖЕНО   graphql/ · инфраструктура · auth/
НЕ СОЗДАНО bff/ · thymeleaf/ · sharing/ · crud/
```

**Checkstyle — порядок импортов:**
```
// Группы: java.* → javax.* → * → org.springframework.*; пустая строка между группами
// jakarta.*, com.example.*, reactor.*, org.jspecify.* — все в группе *; без пустых строк внутри
import com.example.note.domain.NoteResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;
```

---

## Структура модулей

```
domain/                 records · enums · exceptions — чистая Java
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

---

## Именование

| Элемент                       | Паттерн                        | Пример                         |
|-------------------------------|--------------------------------|--------------------------------|
| Sync port interface           | `{Entity}{Op}Contract`         | `NoteAddContract`              |
| Reactive port interface       | `{Entity}{Op}ContractReactive` | `NoteAddContractReactive`      |
| Adapter impl                  | `{Entity}{Op}{Tech}Adapter`    | `NoteAddJpaAdapter`            |
| Mapper interface              | `{Entity}{Tech}MapperContract` | `NoteEntityMapperContract`     |
| Mapper impl                   | `{Entity}{Tech}Mapper`         | `NoteEntityMapper`             |
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

**Spring Boot 4 — критичные отличия от Boot 3 (источник: существующие `build.gradle` в проекте):**
- `starter-web` → `starter-webmvc`; `starter-aop` → `starter-aspectj`
- `@MockBean`/`@SpyBean` → `@MockitoBean`/`@MockitoSpyBean`
- OAuth2 стартеры: `oauth2-resource-server` → `security-oauth2-resource-server`; аналогично для `client` и `authorization-server`
- Spring Authorization Server — часть Spring Security 7; отдельной версии нет
- `@WebMvcTest`, `@DataJpaTest` — в отдельных `*-test` стартерах, не в `starter-test`
- `@SpringBootTest` не даёт MockMvc/WebClient автоматически — нужен `@AutoConfigureMockMvc`
- Jackson 3: пакет `tools.jackson` (кроме `jackson-annotations` — остался на `com.fasterxml`)
- Документация Spring (docs.spring.io) местами показывает Boot 3 — не доверять; смотреть в существующих файлах проекта

---

## Принятые решения

**Архитектура:**
- Hexagonal: `domain/` не знает о JPA/MongoDB/Spring; адаптеры знают только `data-contract/`
- Один контроллер на операцию (`NoteCreateController` → `POST /notes`)
- `service/` только при координации нескольких портов; для CRUD не нужен
- `existsById` в контракте — валидный паттерн, не заменять на `findById`
- `@Transactional` на методах адаптера
- `add` ≠ `replace`: JPA — `save(null id)` vs `save(id)`; MongoDB — `insert()` vs `save()`
- Составной ключ (`user-note/`) в R2DBC: Spring Data R2DBC не поддерживает `@EmbeddedId` — адаптеры используют
  `DatabaseClient` напрямую (без `model/`/`repository/`), как в `data-jdbc/`; MongoDB составной ключ
  поддерживает нативно (`UserNoteKey`/`UserNoteReactiveKey` как `_id`)

**HTTP / Ошибки:**
- `ResponseEntity<T>` в контроллерах
- `ProblemDetail` (RFC 9457); `spring.mvc.problemdetails.enabled=true`
- Доменные исключения (`NoteNotFoundException`) — в `domain/`

**Маппинг:**
- Ручной, без MapStruct; маппер — ответственность адаптера
- `Pageable`/`Page`/`Specification` — утечка инфраструктуры, не использовать в контрактах

**Reactive семантика:**
- `findById` → `Mono<NoteResponse>` (пустой Mono вместо Optional.empty())
- `findAll` → `Flux<NoteResponse>`
- `remove` → `Mono<Void>`
- `existsById` → `Mono<Boolean>`

**Convention plugins** (добавлены новые):
- `java-contract-conventions` — для `data-contract/`
- `java-contract-reactive-conventions` — для `data-contract-reactive/` (включает `reactor-core` как `api`)

**Стиль кода:**
- Импорты: `java.*` → `javax.*` → `*` → `org.springframework.*`; пустая строка между группами
- `@NullMarked` на каждый `package-info.java`
- `@Nullable` из `org.jspecify.annotations`
- `@SuppressWarnings("NullAway.Init")` на `protected` no-arg конструкторах JPA entity
- Промежуточная переменная перед `return`; не inline в `.body()`
- Строки до 120 символов

---

## Открытые решения

- **Стратегия активации адаптеров** — `@Profile("jpa")` _(склонение)_ vs отдельные `application-jpa/`
- **Регистрация auth/ ↔ user/** — Lazy / Sync / Events (Kafka)
- **Возврат мутирующего use case** — DTO _(склонение)_ vs `void`
- **PATCH** — поддерживать или нет
