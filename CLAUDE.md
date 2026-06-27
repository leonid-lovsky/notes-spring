# CLAUDE.md — notes-spring

> Живой документ проекта. Читается автоматически в начале каждой сессии.
> **Всё в этом документе и в коде — временно.** Ничто не является окончательно принятым паттерном.
> Любое решение подлежит обсуждению, изменению и уточнению — независимо от того, что уже написано.
> Последнее обновление: 2026-06-27T11:52Z

---

## Правила

### Начало каждой сессии

1. Прочитать этот файл полностью
2. **Текущий приоритет:** устранение `service/` — решить @Transactional · UUID · *UseCase по одному за раз
3. Актуализировать файл: убрать устаревшее, улучшить формулировки, устранить избыточность
4. Зафиксировать изменения коммитом и пушем _(постоянная авторизация, явный запрос не требуется)_

### Поведение

- **Язык** — общение на русском; код, идентификаторы, комментарии в коде — на английском
- **Формулировки** — любое сообщение пользователя — черновик; длина и стиль значения не имеют; ассистент всегда переформулирует его точно, грамотно и полно — в ответах и при записи в этот файл
- **Фокус** — в начале ответа называть текущую задачу из раздела Задачи; не предлагать отложенные задачи без запроса
- **Архитектурные решения** — перед ответом проверять: актуальна ли проблема? кто выигрывает? не упускаем ли что-то важное? Далее: изложить варианты с плюсами и минусами, дать склонение, дождаться выбора, обосновать. Не предлагать реализацию, пока открыты архитектурные вопросы
- **Единственный источник истины** — этот файл работает на любой машине и в любой сессии; локальная память — лишь кэш, недоступный другим пользователям и машинам. Всё важное фиксируется здесь: принципы, решения, договорённости, правила поведения

### Ограничения

- **Файлы** — не изменять и не создавать без явного указания («измени X в файле Y»)
- **Таблицы** — выравнивать колонки по ширине: каждая строка таблицы должна иметь одинаковую длину в Unicode-символах; несоответствие вызывает предупреждение IDEA
- **CI** — не изменять `.github/workflows/` без явного запроса
- **Коммиты** — не коммитить и не пушить без явного запроса _(исключение: начало сессии)_
- **Перед коммитом** — обновить дату: `date -u +"%Y-%m-%dT%H:%MZ"`

---

## Задачи

```
ГОТОВО      user/ · note/ · user-note/  — domain · service · webmvc · data-jpa · data-mongodb · data-jdbc
ТЕКУЩИЙ     устранение service/ — @Transactional · UUID · *UseCase — по одному вопросу за раз
СЛЕДУЮЩИЙ  решение по reactive/sync impedance + адаптеры — data-r2dbc · data-mongodb-reactive · webflux · graphql
ОТЛОЖЕНО    инфраструктура — actuator · eureka · config · oauth2 · gateway · logging · monitoring
ОТЛОЖЕНО    auth/  — Spring Authorization Server (реализация в самом конце)
НЕ СОЗДАНО  bff/ · thymeleaf/ · sharing/ · crud/
```

### Адаптеры

Реализовать driven adapters (для каждого сервиса: `user/` · `note/` · `user-note/`):

1. `data-jdbc/` — Spring Data JDBC / JdbcTemplate; явный `INSERT` vs `UPDATE` (add vs replace)
2. `data-r2dbc/` — реактивный SQL; **сначала принять решение по reactive/sync impedance**
3. `data-mongodb-reactive/` — реактивный MongoDB; та же проблема

Реализовать driving adapters (для каждого сервиса):

1. `webflux/` — реактивный REST (WebFlux); **сначала принять решение по reactive/sync impedance**
2. `graphql/` — GraphQL (Spring for GraphQL)

### Инфраструктура ← ОТЛОЖЕНО

> Реализовать после решения по reactive/sync impedance. Все изменения — только в `application/build.gradle` и `*.properties`; `domain/` и `service/` не затрагиваются.

**Корневая идея:** тот же принцип изоляции — на двух уровнях:
- **Intra-service** — hexagonal architecture: `domain/` + `service/` не знают о JPA, MongoDB, WebMVC
- **Inter-service** — convention plugins: бизнес-сервис не знает о Eureka, Config, Gateway, OAuth2

Замена любого инфраструктурного компонента = смена конфига или одной строки в `build.gradle`.

| Компонент  | MVP                      | Замена                                | Что меняется              |
|------------|--------------------------|---------------------------------------|---------------------------|
| Config src | native (classpath)       | Git-репо                              | одно property в `config/` |
| Logging    | plain text               | JSON (Loki/ELK)                       | `logback-spring.xml`      |
| Monitoring | Prometheus via Actuator  | Spring Boot Admin / Datadog / Grafana | конфиг, не код            |
| OAuth2     | resource server (плагин) | другой IdP                            | `issuer-uri` property     |

**Порядок реализации:**

1. `spring-boot-actuator-conventions` — добавить во все бизнес-сервисы (`user/`, `note/`, `user-note/`)
2. `spring-cloud-eureka-client-conventions` + `spring-cloud-config-client-conventions` — туда же
3. **Config Server** — `native` (classpath); конфиги в `config/src/main/resources/config/`
4. **OAuth2 Resource Server** — добавить плагин; `issuer-uri=http://localhost:9000` как placeholder
5. **Logging** — `logback-spring.xml` с профилем `json` для prod
6. **Monitoring** — `docker-compose.yml` с Prometheus + Grafana
7. **Gateway routing** — маршруты к `user/`, `note/`, `user-note/`

### После адаптеров

1. `sharing/` — Google Docs ACL сервис:
   - `sharing/domain/` — `NoteAccess`, `NotePublication`, use cases: `effectiveRole`, `share`, `transferOwnership`, `publish`
   - `sharing/service/` — бизнес-логика; координирует `NoteAccess` + `NotePublication` + вызовы к `user-note/` и `note/`
   - `sharing/webmvc/` — REST API
   - `sharing/data-jpa/` — хранение `NoteAccess` и `NotePublication`
   - `sharing/feign/` — клиенты к `note/` и `user-note/`
2. `bff/` — OAuth2 Client + Spring Session + Token Exchange
3. `thymeleaf/` — server-rendered BFF
4. Banking Phase 2 — MFA, token rotation, audit log
5. `crud/` — shared library
6. Широкий стек — один use case через WebMVC + WebFlux + gRPC + GraphQL

### `auth/` — В КОНЦЕ

> Скелет `auth/application/` создан. Convention plugins готовы.
> Реализация откладывается до завершения всего остального.

1. `auth/domain/` — `AuthUser` record + `AuthUserUseCase` + `AuthUserRepository`
2. `auth/data-jpa/` — JPA adapter + Flyway schema
3. Spring Authorization Server — OIDC endpoint + JWT issuer
4. Resource Server во всех сервисах; убрать `userId` из `UserNoteRequest`

---

## Открытые решения

> Не реализовывать до принятия явного решения.

### ⚠ Reactive/sync impedance

Блокирует реализацию `data-r2dbc/`, `data-mongodb-reactive/`, `webflux/`.

**Суть проблемы.** Порты `domain/` используют sync-типы Java:

```java
// NoteRepository (domain port)
Optional<Note> findById(UUID id);
List<Note>     findAll();
void           add(Note note);
```

R2DBC и MongoDB Reactive возвращают реактивные типы:

```java
// Что возвращает R2DBC-драйвер внутри адаптера
Mono<Note>  findById(UUID id);
Flux<Note>  findAll();
Mono<Void>  save(Note note);
```

Адаптер не может реализовать sync-порт без `.block()`. WebFlux-контроллер не может вернуть `Mono<ResponseEntity>`, если use case возвращает `Note` синхронно.

Совместимость адаптеров:

| driving \ driven              | `data-jpa` | `data-mongodb` | `data-jdbc` | `data-r2dbc` | `data-mongodb-reactive` |
|-------------------------------|------------|----------------|-------------|--------------|-------------------------|
| `webmvc`                      | ✓          | ✓              | ✓           | ✗            | ✗                       |
| `webflux`                     | ✗          | ✗              | ✗           | ✓            | ✓                       |
| `graphql` (WebMVC транспорт)  | ✓          | ✓              | ✓           | ✗            | ✗                       |
| `graphql` (WebFlux транспорт) | ✗          | ✗              | ✗           | ✓            | ✓                       |

Варианты решения:
- **Параллельные порты** _(склонение)_ — `NoteRepository` + `ReactiveNoteRepository` в `domain/`; реакторные типы входят в `domain/`; два набора сервисов
- **Sync обёртка с `.block()`** — адаптер реализует sync-интерфейс, блокируя реактивный поток; риск deadlock в event-loop потоке; не настоящий reactive
- **Отдельные реактивные сервисы** — `domain/` + `service/` дублируются для каждого стека; нарушает DRY

### ⚠ Устранение сервисного слоя для CRUD-сервисов ← **ТЕКУЩИЙ ПРИОРИТЕТ**

Принцип принят: `service/` убирается из `user/` · `note/` · `user-note/`; контроллер вызывает порт репозитория напрямую.
Для `sharing/` — сервисный слой остаётся (координация нескольких портов + сложная транзакционная граница).

Три вопроса для последовательного решения:

1. **`@Transactional`** — на методы адаптера или иначе?
2. **UUID** — генерировать в контроллере?
3. **`*UseCase` интерфейсы в `domain/`** — убрать вместе с сервисом?

### ⚠ Стратегия проектирования портов

Открытый вопрос: стратегия группировки — один `UseCase` на сущность (текущее) или один на операцию (Uncle Bob)? — **в анализе**

Решить до начала реализации `auth/domain/`.

### Google Docs ACL

> Все архитектурные вопросы закрыты. Реализация после `auth/`.

### После Resource Server

- **Регистрация** — координация `auth/` ↔ `user/`; решить до реализации:
  - **Lazy** — `user/` создаёт профиль при первом запросе; нет email при регистрации; нет coupling
  - **Sync** — `auth/` → `user/` через RestClient; нарушает direction of dependencies
  - **Events** — Kafka/RabbitMQ; единственный вариант без нарушения SoC/SRP; требует брокера
- **Возврат из use case** — что возвращает мутирующий use case (`create`, `update`):
  - **A — доменный объект** _(склонение)_ — вызывающей стороне не нужен второй запрос; HTTP-контракт ожидает тело
  - **B — `void`** — чистый CQS; POST/PUT требует дополнительного `findById`
- **Mapping** — где маппить между слоями; ручной / MapStruct; отдельные DTO/VO на каждом слое
- **PATCH** — обрабатывать в `service/` (fetch → modify → replace) или не поддерживать вовсе

### Отложено (фундаментальное)

- ~~**Именование output adapter**~~ — **закрыто**: `[Entity][Tech]Adapter`; суффикс `Output` избыточен
- **Стратегия активации адаптеров** — как выбрать реализацию порта при нескольких:
  - **A — `@Profile("jpa")`** _(склонение)_ — просто; убирается когда появится вторая реализация
  - **B — Отдельные `application-jpa/` · `application-r2dbc/`** — чисто на уровне сборки; дублирует composition root

---

## Стек

> Spring Boot 4 monorepo — banking-grade с первого дня

| Инструмент                   | Целевая версия | Сейчас   |
|------------------------------|----------------|----------|
| Java                         | 17             | 17       |
| Gradle                       | 9.6.0          | 9.5.1    |
| Spring Boot                  | 4.1.0          | 4.0.6    |
| Spring Cloud                 | 2025.1.2       | 2025.1.1 |
| Spring Dependency Management | 1.1.7          | 1.1.7    |

> Обновить: `buildSrc/build.gradle.kts` (val-константы) + `gradle/wrapper/gradle-wrapper.properties`.

---

## Принципы

> Применяются с первого дня. Код, архитектура, тесты, деплой проектируются так, чтобы менять backing service (H2 → PostgreSQL, local → AWS) без правок в `domain/` и `service/`.

### Четыре приоритетных критерия

Каждое архитектурное решение проверяется по всем четырём:

- **Hexagonal Architecture** — ports & adapters; изоляция ядра от инфраструктуры
- **Clean Architecture** — зависимости направлены внутрь; use cases в центре; framework — деталь
- **SOLID** — SRP · OCP · LSP · ISP · DIP; на уровне классов, модулей и сервисов
- **Separation of Concerns** — каждый сервис / модуль отвечает за одну зону ответственности

### Классический принцип декомпозиции

**High Cohesion / Low Coupling** — основа всех решений о границах сервисов и модулей.

> «Куда добавить новую функциональность?» → туда, где она создаёт наибольшую связность (cohesion) и наименьшую зависимость (coupling).

Применяется для: `gateway` · `bff` · `auth` · `sharing` · `user` · `note` · `user-note` · любого нового сервиса.

Следствия:
- **Common Closure Principle** — в один сервис собирается то, что изменяется вместе и по одной причине
- **Acyclic Dependencies** — граф зависимостей между сервисами ацикличен (DAG); циклы запрещены

### Паттерны на рассмотрение

Инструменты реализации, применяемые по необходимости; не подменяют четыре основных критерия:

- **Bounded Contexts (DDD)** — явные границы доменов; определяет, кто владеет какими данными
- **Domain Events** — коммуникация между BC без прямой зависимости; альтернатива Feign для некритичных операций
- **Defense in Depth** — безопасность на нескольких слоях; для этого проекта: BFF + сетевая изоляция
- **CQRS** — разделение read/write моделей; актуально для `sharing/` (`effectiveRole` — query; `share`/`transferOwnership` — command)
- **SAGA** — распределённые транзакции без двухфазного коммита; актуально для `transferOwnership` (`sharing/` + `user-note/`)

### Метод решения архитектурных проблем

Когда возникают вопросы о проектировании портов (именование, сигнатуры, ответственность, возвращаемые типы) — **добавить второй адаптер** к существующей реализации.

> Интерфейс порта должен следовать из потребностей адаптеров, а не проектироваться абстрактно.

Пример: есть `data-jpa/` → добавить `data-inmemory/`. Вопросы именования, сигнатур и ответственности разрешатся из конкретного кода, а не из абстрактного обсуждения. Если второй адаптер не реализует интерфейс чисто — интерфейс нужно менять.

Применяется к: именованию адаптеров · сигнатурам методов Repository · ответственности UseCase · стратегии активации адаптеров.

### Что легко менять и почему

Цель архитектуры — сделать каждый слой независимо заменяемым. `domain/` — единственное, что не меняется без причины; всё вокруг — сменные детали.

| # | Что менять             | Механизм                                                           | Цена    |
|---|------------------------|--------------------------------------------------------------------|---------|
| 1 | Бизнес-логику          | `service/` зависит только от `domain/`; адаптеры не трогаются      | Дёшево  |
| 2 | Порты                  | Порты — контракты в `domain/`; смена порта = смена контракта       | Дорого  |
| 3 | Адаптеры               | Одна строка в `application/build.gradle`; convention plugin        | Дёшево  |
| 4 | Внешние инструменты    | backing services — меняется конфиг и plugin; код не меняется       | Дёшево  |
| 5 | Внутренние инструменты | Spring, Hibernate — только в адаптерах; `domain/` их не видит      | Дёшево  |
| 6 | Язык реализации        | `domain/` — чистая логика без фреймворка; порты — контракты        | Принцип |
| 7 | Фреймворк              | Фреймворк — деталь адаптера; нет `import org.springframework.*`    | Принцип |
| 8 | Платформу              | Паттерн языко-независим; Java → C#/TypeScript/Python — механически | Принцип |

Пункт 2 намеренно дорогой: смена порта — это смена контракта между бизнес-логикой и инфраструктурой. Если это дёшево — граница размыта.

Пункты 6–8 достигаются дисциплиной, не кодом: если `domain/` — чистая логика без единой инфраструктурной зависимости, его можно переписать на любом языке механически. Convention plugins — Java/Gradle-выражение этого принципа; в любом стеке есть эквивалент (NuGet, npm workspaces, pyproject.toml).

**Классические формулировки.** Эти 8 целей — содержание **Clean Architecture** (Robert Martin, 2017): *Framework Independent · Testable · UI Independent · Database Independent · External Agency Independent*. Структурный механизм — **Hexagonal Architecture / Ports & Adapters** (Alistair Cockburn, 2005). Единственное правило, из которого всё следует: **Dependency Rule** — зависимости идут только внутрь. Если `domain/` не импортирует ничего снаружи, все 8 целей достижимы.

**Как обеспечить соблюдение.** Три механизма в порядке надёжности:

1. **Convention plugins** — нарушение не компилируется; `domain/` физически не видит `data-jpa/` в classpath
2. **ArchUnit** — нарушение обнаруживается в CI; проверяет отсутствие `import org.springframework.*` в `domain/`
3. **Тесты домена без Spring** — если тест `domain/` не поднимает Spring context, граница чистая

Критерий выбора пути: **автоматически ли нарушение обнаруживается?** Структурные ограничения лучше автотестов; автотесты лучше ревью.

**Самый сложный пункт — порты (2), не язык (6–8).** Проектирование порта — единственное место, где нужно думать о будущем: слишком узкий — трудно добавить адаптер; слишком широкий — нарушает ISP. Лучший способ проверить: добавить второй адаптер. Если он не реализует интерфейс чисто — порт спроектирован неверно.

### YAGNI для сервисного слоя

Сервисный слой оправдан только в трёх случаях:

- **Координация нескольких портов** — use case обращается к нескольким репозиториям или внешним сервисам
- **Сложная транзакционная граница** — несколько шагов внутри одной транзакции, которую неудобно держать в адаптере
- **Доменная политика приложения** — правила, не принадлежащие ни сущности, ни репозиторию, ни контроллеру

**Классика:** **Application Service** (Evans, DDD) — «координирует объекты домена и не содержит бизнес-правил»; если координировать нечего — класс пустой и его быть не должно. **Transaction Script** (Fowler, PoEAA) — для тривиального CRUD прямой вызов репозитория из входящего адаптера валиден и достаточен. **YAGNI** (Beck, XP) — не создавай слой до появления потребности.

### Глубина пакетов как сигнал нового модуля

В идеале пакет каждого модуля одноуровневый: `com.example.note.data.jpa`, `com.example.note.web.mvc`. Появление подпакетов (`.mapping`, `.entity`, `.query`) — сигнал, что модуль взял на себя слишком много.

**Классика:** **Common Closure Principle** (Martin) — в один компонент собирается то, что изменяется вместе и по одной причине; независимые зоны изменений → новый компонент. **Common Reuse Principle** — подпакеты внутри адаптера почти никогда не переиспользуются независимо. **SRP на уровне модуля** — один модуль, одна причина для изменения.

Вопрос при появлении подпакета: это новый модуль? нарушение SRP? или просто раздутый класс, который нужно упростить? В большинстве случаев — упростить.

### Остальные принципы кодовой базы

**No partial abstractions** · **Twelve-Factor** · **KISS** · **DRY** · **SSOT** · **Law of Demeter** · **Fail Fast**

---

## Архитектура

Структура модулей:

```
application/            Spring Boot app — composition root; знает все модули
domain/                 entities + port interfaces (UseCase + Repository) — чистая Java, без Spring
service/                use case implementations (@Service, @Transactional) — зависит только от domain/
webmvc/                 driving adapter (sync REST/HTTP)       →  domain/
webflux/                driving adapter (reactive REST/HTTP)   →  domain/
grpc/                   driving adapter (gRPC/Protobuf)        →  domain/
graphql/                driving adapter (GraphQL)              →  domain/
data-jpa/               driven adapter  (JPA/SQL, ORM)        →  domain/
data-jdbc/              driven adapter  (JDBC/SQL, no ORM)    →  domain/
data-r2dbc/             driven adapter  (reactive SQL)        →  domain/
data-mongodb/           driven adapter  (MongoDB)             →  domain/
data-mongodb-reactive/  driven adapter  (reactive MongoDB)    →  domain/
feign/                  driven adapter  (HTTP client)         →  domain/  (sharing/feign/)
```

**Dependency direction** — `application/` знает всё; адаптеры знают только `domain/`; `domain/` — ничего снаружи
**Database per service** — cross-service JOIN запрещён
**Stateless** — `gateway/` · `config/` · `registry/` · `user/` · `note/` · `user-note/`
**Stateful** — `bff/` · `thymeleaf/` → Spring Session; `auth/` → OAuth2Authorization в PostgreSQL

### Тестирование (пирамида)

| Модуль         | Тест-слой                | Что проверяет                                      |
|----------------|--------------------------|----------------------------------------------------|
| `domain/`      | JUnit (чистая Java)      | Доменная логика без Spring context                 |
| `service/`     | Spring context + Mockito | Use case; Repository мокируется                    |
| `webmvc/`      | `@WebMvcTest` (MockMvc)  | HTTP binding, статусы, сериализация                |
| `data-jpa/`    | `@DataJpaTest` + TC      | SQL, маппинг; Testcontainers = реальный PostgreSQL |
| `application/` | `@SpringBootTest` + TC   | Полный smoke test; все слои вместе                 |

**ArchUnit** — архитектурные тесты в CI; проверяет, что `domain/` не импортирует из адаптеров.

### Семантика репозиториев

`*Repository` в `domain/` говорит на языке домена. Коллекционная семантика (Evans):

```java
boolean existsById(UUID id);       // containsKey
Optional<Note> findById(UUID id);  // get
List<Note> findAll();              // values
void add(Note note);               // put
void replace(Note note);           // replace (full)
void remove(UUID id);              // remove
```

UUID генерируется в `service/` до вызова порта. `replace` — полная замена; PATCH решается в `webmvc/` + `service/`.

**`add` ≠ `replace` — осознанная семантика, не случайная:** реализовано во всех адаптерах:
- JPA: `add` → `em.persist()` (бросает при коллизии) · `replace` → `JpaRepository.save()` (всегда `merge()` при заданном ID)
- MongoDB: `add` → `mongoTemplate.insert()` (бросает при коллизии) · `replace` → `mongoTemplate.save()`
- JDBC: `add` → `INSERT INTO ...` (бросает при коллизии) · `replace` → `UPDATE ... WHERE id = ...`
Spring Data скрывает это за `repository.save()`, теряя семантику. Адаптеры сохраняют её явно.

**`*JpaRepository` / `*MongoRepository` — не архитектурные элементы:** тонкие обёртки над `EntityManager` / `MongoTemplate`, которые Spring Data генерирует автоматически; живут внутри адаптера и невидимы из `domain/`.

**`*Repository` (domain port) не изменился ни разу** — ни для JPA, ни для MongoDB, ни при Spring Data, ни при `MongoTemplate`. Порт изолирован от инфраструктуры.

**Spring Data — деталь реализации адаптера, не архитектурный выбор.** Адаптер можно реализовать через Spring Data (`NoteJpaRepository`) или вручную через `EntityManager` / `MongoTemplate` — `domain/` и `service/` не меняются ни в одном из случаев. Spring Data подключается или убирается внутри адаптера без последствий для архитектуры.

Следствия для проектирования портов:

- **Методы в `*Repository` добавляются по потребности домена, не по возможностям Spring Data.** Добавлять метод только потому, что Spring Data его сгенерирует — нарушение SRP.
- **Возвращаемые типы — только Java или доменные типы.** `Pageable` · `Page` · `Slice` · `Specification` из Spring Data в `domain/` — утечка инфраструктуры. При необходимости пагинации: `List<Note> findAll(int offset, int limit)` или собственный `Page<T>` record в `domain/`.
- **`add` / `replace` — осознанное различие, не случайное.** Spring Data скрывает их за `save()`, теряя семантику. Домен должен различать «добавить новое» и «заменить существующее» явно.
- **Совпадение имён методов со Spring Data — удобство, не архитектурная связь.** `findByUsername` есть в `UserRepository` потому, что домен это требует, а не потому, что Spring Data это генерирует.

### Принятые решения

- **Gateway ≠ BFF** · **BFF — один на UX** (Sam Newman)
- **Token Exchange (RFC 8693)** — BFF обменивает access token на internal JWT с `aud` микросервиса
- **Spring Authorization Server — постоянный IdP**; Keycloak/Auth0 только после детальной оценки
- **OpenFeign** — `spring-cloud-starter-openfeign` для `sharing/feign/`
- **Input port — интерфейс в `domain/`** — `webmvc/` зависит от интерфейса, не от `service/`
- **Domain objects — Java records** — `withXxx()` для изменённой копии; JPA entities — обычные классы
- **`existsById` в Repository** — валидный паттерн; не заменять на `findById`
- **Именование** — `*UseCase` (input port) · `*Repository` (output port, `domain/`) · `*JpaRepository` (Spring Data, `data-jpa/`) · `*[Tech]Adapter` (driven adapter)
- **`ResponseEntity<T>` везде** — статусы явно через `HttpStatus`
- **`AuthUser` (`auth/`) ≠ `User` (`user/`)** — `User { id, username, email }`; пароль хранит только `auth/`
- **Wire format в адаптере** — `.proto` в `grpc/`, `.graphqls` в `graphql/`; Protobuf/GraphQL типы не проникают в `domain/`
- **`service/` только когда оправдан** — для `sharing/`: координация нескольких портов + `@Transactional` через несколько шагов; для `user/` · `note/` · `user-note/`: убирается (принцип принят, детали реализации открыты — см. «Открытые решения»)
- **`user/` · `note/` · `user-note/` — чистые REST CRUD сервисы** — каждый знает только свои данные; любая бизнес-логика поверх CRUD реализуется в `sharing/`
- **`sharing/` — отдельный гексагональный сервис** — реализует всю бизнес-логику Google Docs ACL; вызывает `user-note/` и `note/` через output ports (Feign); CRUD сервисы не знают о `sharing/` вообще
- **Enforcement — BFF + сетевая изоляция** — перед вызовом `note/` BFF проверяет `sharing/effectiveRole`; `note/` — чистый Resource Server (JWT, без ACL); сетевая изоляция исключает прямой доступ в обход BFF
- **`NoteVisibility` — НЕ в `note/domain/`** — `note/` не знает о своей видимости; принадлежит `sharing/`
- **Google Docs ACL модель** — принята как целевая модель доступа:
  - `UserNote { userId, noteId, role }` — явные права; `UserNoteRole`: `OWNER · EDITOR · COMMENTER · VIEWER`
  - **Share with others:**
    - People with access — явные `UserNote` записи
    - General access: `RESTRICTED` (только явные) · `ANYONE_WITH_LINK` (viewer / commenter / editor)
    - Settings: `editorsCanShare`; `canDownloadCopyPrint`
  - **Publish to web** — отдельная концепция, не link sharing: link · embed; `autoRepublish`
  - **`effectiveRole(userId, noteId)`**: явная `UserNote` → иначе general access → deny
  - **`share(callerId, noteId, targetUserId, role)`**: caller = OWNER (или EDITOR при `editorsCanShare`); role ≤ роли caller'а
  - **`transferOwnership(callerId, noteId, newOwnerId)`**: атомарно — старый OWNER → EDITOR, новый → OWNER
  - Вся логика живёт в `sharing/service/`; `sharing/feign/` вызывает `note/` и `user-note/`
- **Один `OWNER` на заметку** — доменный инвариант; `transferOwnership` атомарно
- **`NoteAccess`** — `{ noteId, generalAccess, editorsCanShare, canDownloadCopyPrint }`; `generalAccess`: `RESTRICTED · VIEWER · COMMENTER · EDITOR`
- **`NotePublication`** — `{ noteId, linkPublished, linkAutoRepublish, embedPublished, embedAutoRepublish }`; «publish to web» ≠ «share with link» (SRP)
- **Доменные исключения** — `UserNotFoundException` · `NoteNotFoundException` · `UserNoteNotFoundException` в `domain/`

---

## Сервисы

```
EDGE          gateway/     Spring Cloud Gateway — stateless, routing + rate limiting
PRESENTATION  bff/         OAuth2 Client + Spring Session + Token Exchange (SPA)
              thymeleaf/   Thymeleaf + OAuth2 Client — self-contained BFF
IDENTITY      auth/        Spring Authorization Server — OIDC-compliant, JWT issuer
BUSINESS      user/        Resource Server  (User: id, username, email) — чистый REST CRUD
              note/        Resource Server  (Note: id, content) — чистый REST CRUD
              user-note/   Resource Server  (UserNote: userId, noteId, role) — чистый REST CRUD
              sharing/     Resource Server  (Google Docs ACL бизнес-логика: effectiveRole, share, transferOwnership, publish)
INFRA         config/      Spring Cloud Config Server
              registry/    Eureka Server
EXTERNAL      Redis        JTI Blocklist + Spring Session (bff/ + thymeleaf/)
              PostgreSQL×N по одной БД на: auth, user, note, user-note, sharing
              Kafka/MQ     только при событийной регистрации (решение не принято)
```

> **MVP** — та же архитектура и код; H2 вместо PostgreSQL, `MapSessionRepository` вместо Redis.
> Backing services подключаются при реальной потребности, не как архитектурный шаг.

---

## Безопасность

> `identity ≠ profile ≠ business ≠ permissions` — смешивание нарушает SoC и усложняет смену IdP

**Zero Trust** — каждый слой валидирует JWT самостоятельно
**JWT claims** — только стандартные: `sub` · `iss` · `aud` · `exp` · `jti` · `acr` · `amr` · `scope`
**Браузер не видит JWT** — только HttpOnly session cookie в `bff/` · `thymeleaf/` (защита от XSS)

| Слой            | Модуль                | Ответственность                            |
|-----------------|-----------------------|--------------------------------------------|
| Edge            | `gateway/`            | Routing, rate limiting, TLS                |
| BFF             | `bff/` · `thymeleaf/` | OAuth2 login flow, session, Token Exchange |
| IdP             | `auth/`               | JWT issuing, credentials, OIDC             |
| Resource Server | `*/webmvc/`           | JWT validation per request                 |
| ACL             | `sharing/`            | effectiveRole, share, transferOwnership    |

**UserNoteRole:** `OWNER` · `EDITOR` · `COMMENTER` _(не MVP)_ · `VIEWER`
**General access:** `RESTRICTED` · `ANYONE_WITH_LINK` (viewer / commenter / editor)
**Publish to web:** link · embed; `autoRepublish` — отдельная концепция, не link sharing
**ACL resolution:** явная `UserNote` → иначе general access → deny; логика в `sharing/service/`

### Клиенты

| Клиент               | Grant Type         | Хранит токен         | BFF          |
|----------------------|--------------------|----------------------|--------------|
| Browser / Thymeleaf  | Authorization Code | Серверная сессия     | Сам себе BFF |
| Browser / SPA        | Authorization Code | Серверная сессия BFF | `bff/`       |
| Mobile (Android/iOS) | AuthCode + PKCE    | Keychain / Keystore  | Нет          |
| B2B / CLI            | Client Credentials | Не хранит            | Нет          |

### Аутентификация

**Все методы → единый JWT** с `acr` (1 = single factor, 2 = MFA) и `amr` (pwd / google / passkey / totp)
**Social Login** — `auth/` сам OAuth2 Client к Google/GitHub; их токен никогда не покидает `auth/`
**MFA** — `@EnableMultiFactorAuthentication` (Spring Security 7) + `FactorGrantedAuthority`
**Step-up auth** — `/oauth2/authorize?acr_values=2` → MFA challenge → новый токен с `acr=2`

### Токены

**Flow:** User → `auth/` → access_token → BFF Token Exchange (RFC 8693) → internal JWT (`aud`=сервис) → Microservice
**access_token** 15 мин · **refresh_token** 30–90 дней; хранится в сессии BFF или Keychain/Keystore; в микросервисы не отправляется
**Rotation** — каждый refresh → новый refresh_token; повторное использование → revoke вся семья → re-login
**JTI Blocklist** — Redis `SET jti:{jti} "revoked" EX ttl`; ~0.5 ms на запрос; logout < 1 с
**Back-channel logout** — `auth/` → POST `bff/logout/connect/back-channel`; при горизонтальном масштабировании BFF требует Redis Session

### Banking-grade фазы

| Фаза         | Содержание                                                                                  |
|--------------|---------------------------------------------------------------------------------------------|
| 1 — основа   | PKCE · `aud`/`scope`/`jti` claims · refresh rotation · rate limiting · TLS 1.3 · stateless  |
| 2 — MFA      | TOTP/Passkey · Social Login · JTI Blocklist · Step-up auth · device tracking · audit log    |
| 3 — максимум | DPoP · mTLS · Certificate pinning · App attestation                                         |

---

## Техническая база

### Gradle

**`buildSrc`** + convention plugins (Kotlin DSL) — единственный механизм; flat, без вложенности
**Файлы:** `buildSrc/build.gradle.kts` (версии плагинов — в `val`-константах); convention plugins — `src/main/kotlin/*.gradle.kts`; субпроекты — `build.gradle` (Groovy, только `id '...'`)
**Нет:** root `build.gradle` · `buildSrc/settings.gradle` · `libs.versions.toml`
**Порядок блоков:** `plugins` → `repositories` → `dependencyManagement` → `dependencies` → `test`
**Порядок зависимостей:** `domain` → `service` → `webmvc` → `data-jpa`
**`settings.gradle`:** `gateway` → `config` → `registry` → `auth` → `user` → `note` → `user-note`; внутри: `application` → `domain` → `service` → `webmvc` → `data-jpa`

**Convention plugins:**

| Plugin ID                                               | Назначение                                        |
|---------------------------------------------------------|---------------------------------------------------|
| `spring-boot-application-conventions`                   | `application/` — Boot app                         |
| `java-domain-conventions`                               | `domain/` — чистая Java, без BOM                  |
| `spring-boot-service-conventions`                       | `service/` — BOM + spring-tx                      |
| `spring-boot-webmvc-adapter-conventions`                | `webmvc/` — driving adapter (sync REST)           |
| `spring-boot-webflux-adapter-conventions`               | `webflux/` — driving adapter (reactive REST)      |
| `spring-boot-graphql-adapter-conventions`               | `graphql/` — driving adapter (GraphQL)            |
| `spring-boot-data-jpa-adapter-conventions`              | `data-jpa/` — driven adapter (JPA/SQL, ORM)       |
| `spring-boot-data-jdbc-adapter-conventions`             | `data-jdbc/` — driven adapter (JDBC/SQL, no ORM)  |
| `spring-boot-data-r2dbc-adapter-conventions`            | `data-r2dbc/` — driven adapter (reactive SQL)     |
| `spring-boot-data-mongodb-adapter-conventions`          | `data-mongodb/` — driven adapter (MongoDB)        |
| `spring-boot-data-mongodb-reactive-adapter-conventions` | `data-mongodb-reactive/` — driven adapter         |
| `spring-boot-data-elasticsearch-adapter-conventions`    | `data-elasticsearch/` — driven adapter            |
| `spring-cloud-openfeign-adapter-conventions`            | `feign/` — driven adapter (HTTP client)           |
| `spring-boot-restclient-conventions`                    | add-on: RestClient (sync HTTP)                    |
| `spring-boot-webclient-conventions`                     | add-on: WebClient (reactive HTTP)                 |
| `spring-cloud-gateway-webflux-conventions`              | `gateway/` — reactive gateway (WebFlux-based app) |
| `spring-cloud-gateway-webmvc-conventions`               | `gateway/` — sync gateway (WebMVC-based app)      |
| `spring-cloud-config-server-conventions`                | `config/` — Config Server app                     |
| `spring-cloud-config-client-conventions`                | add-on: Config Client                             |
| `spring-cloud-eureka-server-conventions`                | `registry/` — Eureka Server app                   |
| `spring-cloud-eureka-client-conventions`                | add-on: Eureka Client                             |
| `spring-cloud-circuit-breaker-conventions`              | add-on: Resilience4j Circuit Breaker (reactive)   |
| `spring-cloud-loadbalancer-conventions`                 | add-on: Spring Cloud LoadBalancer                 |
| `spring-boot-h2-database-conventions`                   | add-on: H2 + h2console                            |
| `spring-boot-actuator-conventions`                      | add-on: Actuator                                  |
| `spring-boot-oauth2-authorization-server-conventions`   | `auth/` — Authorization Server                    |
| `spring-boot-oauth2-resource-server-conventions`        | add-on: JWT-валидация (Resource Server)           |
| `spring-boot-oauth2-client-conventions`                 | add-on: OAuth2 Client                             |

**Cloud BOM** — координаты инлайн прямо в каждом Cloud convention plugin: `"org.springframework.cloud:spring-cloud-dependencies:2025.1.2"`; отдельный файл для одной строки избыточен.

- **`domain`** — только `java`; без Spring BOM; JSpecify и JUnit с явными версиями
- **`service`** — требует явный `implementation("org.springframework:spring-tx")`; `spring-boot-starter` не тянет его транзитивно
- **`oauth2-resource-server`** — транспортно-независимая JWT-валидация; применим к `webmvc/`, `webflux/`, `graphql/` — не переименовывать в `webmvc-oauth2-*`
- **`h2-database`** — add-on поверх `data-jpa`; не содержит `repositories {}`; применять совместно
- **`gateway-webflux`** / **`gateway-webmvc`** / **`config-server`** / **`eureka-server`** — включают `org.springframework.boot` plugin (это Boot app, не просто модуль); не комбинировать с `spring-boot-application-conventions`
- **`restclient`** / **`webclient`** — не адаптеры в гексагональном смысле; инструменты внутри других адаптеров (Feign, reactive adapter); именно поэтому в имени нет суффикса `-adapter-`
- **`circuit-breaker`** — только реактивный вариант (Resilience4j reactor); для sync-стека: `resilience4j-spring-boot3` без Spring Cloud starter
- **Convention plugin = Gradle-выражение гексагональной архитектуры**: каждый тип адаптера → один plugin; `build.gradle` модуля объявляет только project-зависимости; всё остальное — в plugin; это SRP на уровне сборки

### Spring Boot 4

- `starter-web` → `starter-webmvc`; `starter-aop` → `starter-aspectj`; `starter-tomcat` → `starter-tomcat-runtime` (war)
- `starter-test` — JUnit/Mockito/AssertJ; слайсы (`@WebMvcTest`, `@DataJpaTest` и др.) выведены в отдельные `*-test` стартеры (в Boot 3 входили в `starter-test`)
- `@SpringBootTest` больше не предоставляет MockMVC / WebClient / TestRestTemplate автоматически; добавить `@AutoConfigureMockMvc` / `@AutoConfigureTestRestTemplate` / `@AutoConfigureRestTestClient`
- OAuth2 стартеры: `oauth2-*` (Boot 3) → `security-oauth2-*` (Boot 4); `docs.spring.io/spring-security` ссылается на Boot 3 имена — не доверять
- **Spring Authorization Server** теперь часть Spring Security 7.0; `spring-authorization-server.version` property убран — использовать версию Security
- Jackson 3: `com.fasterxml.jackson` → `tools.jackson`; `jackson-annotations` остаётся на `com.fasterxml.jackson.core`
  - `@JsonComponent` → `@JacksonComponent`; `@JsonMixin` → `@JacksonMixin`
  - `Jackson2ObjectMapperBuilderCustomizer` → `JsonMapperBuilderCustomizer`
  - `spring.jackson.read.*` / `spring.jackson.write.*` → `spring.jackson.json.read.*` / `spring.jackson.json.write.*`
  - Для миграции без кода: `spring-boot-jackson2` (deprecated, временный)
- RestTemplate deprecated → `RestClient` (`spring-boot-starter-restclient`); реактивный аналог — `WebClient` (`spring-boot-starter-webclient`)
- HTTP Service Clients — декларативные REST-клиенты из аннотированных Java-интерфейсов; интегрируются с auto-configuration и properties
- API Versioning auto-configuration: `spring.mvc.apiversion.*` (WebMVC) / `spring.webflux.apiversion.*` (WebFlux)
- Flyway + PostgreSQL: нужен `runtimeOnly("org.flywaydb:flyway-database-postgresql")`
- Обработка ошибок: `ResponseEntityExceptionHandler` + `ProblemDetail` (RFC 9457)
- `@MockBean` / `@SpyBean` (Boot 3) → `@MockitoBean` / `@MockitoSpyBean` (Boot 4); в `@Configuration`-классах запрещены — только на полях тест-класса
- `@AutoConfigureWebServer` — новая аннотация для тестов с embedded web server factory beans _(4.1)_
- Spock 2.4 поддерживается (Groovy 5); в 4.0 была удалена _(4.1)_
- `hibernate-jpamodelgen` → `hibernate-processor`
- Elasticsearch: `RestClient` → `Rest5Client`; `RestClientBuilderCustomizer` → `Rest5ClientBuilderCustomizer`
- Spring Batch: `spring-boot-starter-batch` — in-memory (без БД); `spring-boot-starter-batch-jdbc` — с БД (восстанавливает прежнее поведение)
- Spring Batch MongoDB: `spring-boot-starter-batch-data-mongodb` + `spring.batch.data.mongo.schema.initialize` _(4.1)_
- Kafka Streams: `StreamBuilderFactoryBeanCustomizer` → `StreamsBuilderFactoryBeanConfigurer`
- jOOQ: минимальная версия 3.20, требует Java 21 _(4.1)_
- Spring Data JPA: `deferred` bootstrap теперь требует бин `AsyncTaskExecutor`; `lazy` больше не устанавливает executor _(4.1)_
- Undertow больше не поддерживается (несовместим с Servlet 6.1)
- Liveness / Readiness probes включены по умолчанию; отключить: `management.endpoint.health.probes.enabled=false`
- DevTools LiveReload: в 4.0 отключён по умолчанию; в 4.1 объявлен deprecated (замены нет)
- Redis: Master/Replica auto-configuration (только Lettuce): `spring.data.redis.masterreplica.nodes`
- `spring.datasource.connection-fetch=lazy` — отложенное получение JDBC-соединения _(4.1)_
- OAuth2 JWT authorities via SpEL: `spring.security.oauth2.resourceserver.jwt.authorities-claim-expressions`; префикс: `spring.security.oauth2.resourceserver.jwt.authority-prefix` _(4.1)_
- `spring.session.redis.*` → `spring.session.data.redis.*`; `spring.session.mongodb.*` → `spring.session.data.mongodb.*`
- `spring.dao.exceptiontranslation.enabled` → `spring.persistence.exceptiontranslation.enabled`
- Миграция properties: добавить `spring-boot-properties-migrator` как `runtimeOnly`; удалить после завершения
- Apache Derby deprecated в 4.1 (проект закрыт); рекомендована миграция на H2 или HSQLDB
- Dynatrace Micrometer: V1 API deprecated в 4.1 → использовать V2
- gRPC: нативная поддержка `spring-boot-starter-grpc-client` / `spring-boot-starter-grpc-server` _(4.1)_
- Kotlin serialization: `spring-boot-starter-kotlinx-serialization-json` (`spring.kotlinx.serialization.json.*`)
- OpenTelemetry: новый `spring-boot-starter-opentelemetry` для OTLP metric + trace export
- Gradle 9 поддерживается; минимум — 8.14
- Lombok: `compileOnly` + `annotationProcessor`;
  `@Data` / `@EqualsAndHashCode` запрещены на entities — нарушают Hibernate lifecycle;
  `equals()` / `hashCode()` по ID вручную: `hashCode() { return getClass().hashCode(); }`

### Spring Security

**Цепочка:** `SecurityFilterChain` → `AuthenticationManager` → `ProviderManager` → `DaoAuthenticationProvider` → `UserDetailsService` + `PasswordEncoder`
**Resource Server** — `JwtDecoder` + `JwtAuthenticationConverter` → `GrantedAuthority`; минимум: property `spring.security.oauth2.resourceserver.jwt.issuer-uri`
**Auth Server** — `OAuth2AuthorizationServerConfigurer` · `RegisteredClientRepository`; 4 обязательных бина: `SecurityFilterChain` × 2, `UserDetailsService`, `JWKSource`

### Null Safety и стиль

**JSpecify** (`@NullMarked` через `package-info.java`) — non-null по умолчанию; каждый пакет требует своего `package-info.java`. Выбран вместо `org.springframework.lang` (deprecated) и JSR-305 (заброшен).
**`@Nullable`** — `@Target(TYPE_USE)`: `private @Nullable String field`; массивы: `Object @Nullable []` (nullable ссылка), `@Nullable Object[]` (nullable элементы)
**Spring Cloud (2025.1.x)** — ещё не null-safe в `registry/`, `config/`, `gateway/`; при нужде: `@NullUnmarked`
**Импорты** — `com.example.*` + `org.*` / `jakarta.*`, затем `java.*`; wildcard при 3+
**Имена полей** — camelCase от типа: `noteUseCase`, `noteRepository`, `noteJpaRepository`
**Промежуточная переменная** — перед `return` всегда извлекать результат; не inline в `.body()`
**Пустое тело** — одна пустая строка · **EOF** — один `\n`

### Качество и наблюдаемость

**ArchUnit** — `testImplementation("com.tngtech.archunit:archunit-junit5:<version>")`; версию брать с Maven Central; проверяет, что `domain/` не импортирует из адаптеров
**JaCoCo** — встроен в Gradle (`jacoco`), версия не нужна; источник покрытия для SonarQube
**SonarQube** — используется внешне: IDE-плагин, CI pipeline, standalone server, SonarCloud; не добавляется как Gradle-зависимость в проект
**Actuator** — только в `application/`; `management.server.port` — отдельный порт; в Resource Server — отдельный `SecurityFilterChain` для `/actuator/**`
**OWASP Dependency-Check** — `org.owasp.dependencycheck`; artifact: `org.owasp:dependency-check-gradle`; версию брать с Maven Central
**Renovate** — автоматические PR на обновление зависимостей; конфигурируется через `renovate.json`

### CI/CD

Платформы используются последовательно: **GitHub Actions** → **GitLab CI** → **Jenkins**.
Текущий: **GitHub Actions** (`.github/workflows/`) — не трогать без явного запроса.

### Для опыта

Используются последовательно по мере развития проекта:

- **Spring Modulith** — миграция multi-module → modular monolith; Gradle-модули дают более сильную compile-time enforcement
- **jMolecules** — аннотирует архитектурные роли явно: `@DrivingAdapter`, `@AggregateRoot`, `@Repository`
- **Docker · Docker Compose · Kubernetes** — контейнеризация и оркестрация
- **AWS** (ECS Fargate → EKS, RDS, ElastiCache, MSK, ALB, ACM, Secrets Manager, CloudWatch + X-Ray)
- **Elastic Stack** — Elasticsearch + Logstash + Kibana

---

## Развёртывание

> Код не меняется при смене backing service. Меняется только конфигурация и convention plugin (`h2-database` → `data-jpa` + PostgreSQL driver).

| Этап        | Инструменты                       | Backing services                        |
|-------------|-----------------------------------|-----------------------------------------|
| Local / MVP | JVM + H2 + `MapSessionRepository` | Не нужны                                |
| Staging     | Docker + Docker Compose           | PostgreSQL · Redis · Kafka · ELK        |
| Production  | Docker + ECS Fargate → EKS        | AWS managed services                    |

---

## Верифицированные координаты (Boot 4.x / Cloud 2025.1.x)

> Источник правды — `start.spring.io`. Maven Central не является авторитетом для Boot 4 стартеров.

### `implementation`

**`org.springframework.boot`**

```
spring-boot-h2console
spring-boot-starter-activemq
spring-boot-starter-actuator
spring-boot-starter-amqp
spring-boot-starter-artemis
spring-boot-starter-batch
spring-boot-starter-batch-data-mongodb
spring-boot-starter-batch-jdbc
spring-boot-starter-cache
spring-boot-starter-cassandra
spring-boot-starter-cloudfoundry
spring-boot-starter-couchbase
spring-boot-starter-data-cassandra
spring-boot-starter-data-cassandra-reactive
spring-boot-starter-data-couchbase
spring-boot-starter-data-couchbase-reactive
spring-boot-starter-data-elasticsearch
spring-boot-starter-data-jdbc
spring-boot-starter-data-jpa
spring-boot-starter-data-ldap
spring-boot-starter-data-mongodb
spring-boot-starter-data-mongodb-reactive
spring-boot-starter-data-neo4j
spring-boot-starter-data-r2dbc
spring-boot-starter-data-redis
spring-boot-starter-data-redis-reactive
spring-boot-starter-data-rest
spring-boot-starter-elasticsearch
spring-boot-starter-flyway
spring-boot-starter-freemarker
spring-boot-starter-graphql
spring-boot-starter-groovy-templates
spring-boot-starter-grpc-client
spring-boot-starter-grpc-server
spring-boot-starter-hateoas
spring-boot-starter-integration
spring-boot-starter-jdbc
spring-boot-starter-jersey
spring-boot-starter-kafka
spring-boot-starter-ldap
spring-boot-starter-liquibase
spring-boot-starter-mail
spring-boot-starter-mongodb
spring-boot-starter-mustache
spring-boot-starter-neo4j
spring-boot-starter-opentelemetry
spring-boot-starter-pulsar
spring-boot-starter-quartz
spring-boot-starter-r2dbc
spring-boot-starter-restclient
spring-boot-starter-rsocket
spring-boot-starter-security
spring-boot-starter-security-oauth2-authorization-server
spring-boot-starter-security-oauth2-client
spring-boot-starter-security-oauth2-resource-server
spring-boot-starter-security-saml2
spring-boot-starter-session-data-redis
spring-boot-starter-session-jdbc
spring-boot-starter-thymeleaf
spring-boot-starter-validation
spring-boot-starter-webclient
spring-boot-starter-webflux
spring-boot-starter-webmvc
spring-boot-starter-webservices
spring-boot-starter-websocket
spring-boot-starter-zipkin
```

**`org.springframework.cloud`** (2025.1.x)

```
spring-cloud-bus
spring-cloud-config-server
spring-cloud-function-web
spring-cloud-starter
spring-cloud-starter-circuitbreaker-reactor-resilience4j
spring-cloud-starter-config
spring-cloud-starter-consul-config
spring-cloud-starter-consul-discovery
spring-cloud-starter-gateway-server-webmvc
spring-cloud-starter-gateway-server-webflux
spring-cloud-starter-loadbalancer
spring-cloud-starter-netflix-eureka-client
spring-cloud-starter-netflix-eureka-server
spring-cloud-starter-openfeign
spring-cloud-starter-task
spring-cloud-starter-vault-config
spring-cloud-starter-zookeeper-config
spring-cloud-starter-zookeeper-discovery
spring-cloud-stream
spring-cloud-stream-binder-kafka
spring-cloud-stream-binder-kafka-streams
spring-cloud-stream-binder-pulsar
spring-cloud-stream-binder-rabbit
```

**`org.springframework.integration`** (версия управляется Spring Boot BOM)

```
spring-integration-amqp
spring-integration-grpc
spring-integration-http
spring-integration-jdbc
spring-integration-jms
spring-integration-jpa
spring-integration-kafka
spring-integration-mail
spring-integration-mongodb
spring-integration-r2dbc
spring-integration-redis
spring-integration-rsocket
spring-integration-stomp
spring-integration-webflux
spring-integration-websocket
spring-integration-ws
```

**`org.springframework.modulith`** (BOM: `spring-modulith-bom:<version>`)

```
spring-modulith-events-api
spring-modulith-starter-core
spring-modulith-starter-insight
spring-modulith-starter-jdbc
spring-modulith-starter-jpa
spring-modulith-starter-mongodb
spring-modulith-starter-neo4j
```

**`org.springframework.security`** (версия управляется Spring Boot BOM)

```
spring-security-messaging
spring-security-rsocket
spring-security-webauthn
```

**`org.apache.kafka`**

```
org.apache.kafka:kafka-streams
```

**`org.springframework.amqp`**

```
org.springframework.amqp:spring-rabbit-stream
```

**`org.springframework.data`**

```
org.springframework.data:spring-data-rest-hal-explorer
```

**Third-party (явная версия)**

```
de.codecentric:spring-boot-admin-starter-client   ← BOM: spring-boot-admin-dependencies
de.codecentric:spring-boot-admin-starter-server   ← BOM: spring-boot-admin-dependencies
org.jobrunr:jobrunr-spring-boot-4-starter:8.7.0   ← явная версия (не в BOM)
```

### `runtimeOnly`

```
# PostgreSQL
org.postgresql:postgresql
org.postgresql:r2dbc-postgresql

# H2
com.h2database:h2
io.r2dbc:r2dbc-h2

# MySQL / MariaDB
com.mysql:mysql-connector-j
io.asyncer:r2dbc-mysql
org.mariadb.jdbc:mariadb-java-client
org.mariadb:r2dbc-mariadb:1.1.3       ← явная версия (не в BOM)

# SQL Server
com.microsoft.sqlserver:mssql-jdbc
io.r2dbc:r2dbc-mssql:1.0.0.RELEASE    ← явная версия (не в BOM)

# Oracle
com.oracle.database.jdbc:ojdbc11
com.oracle.database.r2dbc:oracle-r2dbc

# Derby (deprecated в Boot 4.1 — проект закрыт; мигрировать на H2 или HSQLDB)
org.apache.derby:derby
org.apache.derby:derbytools

# Other
org.hsqldb:hsqldb
org.xerial:sqlite-jdbc

# Flyway drivers (нужны при использовании spring-boot-starter-flyway)
org.flywaydb:flyway-database-derby              ← для Derby (deprecated в Boot 4.1)
org.flywaydb:flyway-database-hsqldb
org.flywaydb:flyway-database-oracle
org.flywaydb:flyway-database-postgresql
org.flywaydb:flyway-mysql
org.flywaydb:flyway-sqlserver

# Micrometer registries (для Actuator /actuator/prometheus и др.)
io.micrometer:micrometer-registry-datadog
io.micrometer:micrometer-registry-dynatrace          ← V1 deprecated в Boot 4.1; использовать V2 API
io.micrometer:micrometer-registry-graphite
io.micrometer:micrometer-registry-influx
io.micrometer:micrometer-registry-new-relic
io.micrometer:micrometer-registry-otlp
io.micrometer:micrometer-registry-prometheus

# Spring Modulith runtime
org.springframework.modulith:spring-modulith-events-amqp
org.springframework.modulith:spring-modulith-events-jms
org.springframework.modulith:spring-modulith-runtime
```

### `annotationProcessor`

```
org.springframework.boot:spring-boot-configuration-processor
```

### `developmentOnly`

```
org.springframework.boot:spring-boot-devtools
org.springframework.boot:spring-boot-docker-compose
```

### `testImplementation`

**`org.springframework.boot`**

```
spring-boot-starter-test
spring-boot-starter-activemq-test
spring-boot-starter-actuator-test
spring-boot-starter-amqp-test
spring-boot-starter-artemis-test
spring-boot-starter-batch-data-mongodb-test
spring-boot-starter-batch-jdbc-test
spring-boot-starter-batch-test
spring-boot-starter-cache-test
spring-boot-starter-cassandra-test
spring-boot-starter-cloudfoundry-test
spring-boot-starter-couchbase-test
spring-boot-starter-data-cassandra-test
spring-boot-starter-data-cassandra-reactive-test
spring-boot-starter-data-couchbase-test
spring-boot-starter-data-couchbase-reactive-test
spring-boot-starter-data-elasticsearch-test
spring-boot-starter-data-jdbc-test
spring-boot-starter-data-jpa-test
spring-boot-starter-data-ldap-test
spring-boot-starter-data-mongodb-test
spring-boot-starter-data-mongodb-reactive-test
spring-boot-starter-data-neo4j-test
spring-boot-starter-data-r2dbc-test
spring-boot-starter-data-redis-test
spring-boot-starter-data-redis-reactive-test
spring-boot-starter-data-rest-test
spring-boot-starter-elasticsearch-test
spring-boot-starter-flyway-test
spring-boot-starter-freemarker-test
spring-boot-starter-graphql-test
spring-boot-starter-groovy-templates-test
spring-boot-starter-grpc-client-test
spring-boot-starter-grpc-server-test
spring-boot-starter-hateoas-test
spring-boot-starter-jdbc-test
spring-boot-starter-jersey-test
spring-boot-starter-kafka-test
spring-boot-starter-ldap                              ← без -test; нужен в testImplementation для embedded LDAP
spring-boot-starter-ldap-test
spring-boot-starter-liquibase-test
spring-boot-starter-mail-test
spring-boot-starter-mongodb-test
spring-boot-starter-mustache-test
spring-boot-starter-neo4j-test
spring-boot-starter-opentelemetry-test
spring-boot-starter-pulsar-test
spring-boot-starter-quartz-test
spring-boot-starter-r2dbc-test
spring-boot-starter-restclient-test
spring-boot-starter-restdocs
spring-boot-starter-rsocket-test
spring-boot-starter-security-test
spring-boot-starter-security-oauth2-authorization-server-test
spring-boot-starter-security-oauth2-client-test
spring-boot-starter-security-oauth2-resource-server-test
spring-boot-starter-security-saml2-test
spring-boot-starter-session-data-redis-test
spring-boot-starter-session-jdbc-test
spring-boot-starter-thymeleaf-test
spring-boot-starter-validation-test
spring-boot-starter-webclient-test
spring-boot-starter-webflux-test
spring-boot-starter-webmvc-test
spring-boot-starter-webservices-test
spring-boot-starter-websocket-test
spring-boot-starter-zipkin-test
spring-boot-testcontainers
```

**Reactor**

```
io.projectreactor:reactor-test   ← StepVerifier; нужен для тестов R2DBC, WebFlux, MongoDB Reactive
```

**Testcontainers** (версия управляется Spring Boot BOM; Boot 4 использует `testcontainers-*` префикс)

```
org.testcontainers:testcontainers-activemq
org.testcontainers:testcontainers-cassandra
org.testcontainers:testcontainers-consul
org.testcontainers:testcontainers-couchbase
org.testcontainers:testcontainers-elasticsearch
org.testcontainers:testcontainers-grafana
org.testcontainers:testcontainers-junit-jupiter
org.testcontainers:testcontainers-kafka
org.testcontainers:testcontainers-mariadb
org.testcontainers:testcontainers-mongodb
org.testcontainers:testcontainers-mssqlserver
org.testcontainers:testcontainers-mysql
org.testcontainers:testcontainers-neo4j
org.testcontainers:testcontainers-oracle-free
org.testcontainers:testcontainers-postgresql
org.testcontainers:testcontainers-pulsar
org.testcontainers:testcontainers-r2dbc
org.testcontainers:testcontainers-rabbitmq
org.testcontainers:testcontainers-vault
# Redis: нет официального TC модуля → GenericContainer("redis:latest")
```

**Spring Cloud / Integration / Modulith**

```
org.springframework.cloud:spring-cloud-starter-contract-stub-runner
org.springframework.cloud:spring-cloud-starter-contract-verifier
org.springframework.cloud:spring-cloud-stream-test-binder
org.springframework.integration:spring-integration-test
org.springframework.modulith:spring-modulith-starter-test
```

**REST Docs / REST Assured**

```
org.springframework.restdocs:spring-restdocs-mockmvc
io.rest-assured:spring-web-test-client
```

**LDAP**

```
com.unboundid:unboundid-ldapsdk   ← embedded LDAP для тестов
```

### `testRuntimeOnly`

```
org.junit.platform:junit-platform-launcher
```

### Прочее

```
# Maven (third-party, явная версия)
org.thymeleaf.extras:thymeleaf-extras-springsecurity6   ← имя "6", даже с Security 7
org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.2
com.tngtech.archunit:archunit-junit5:<version>           ← версию брать с Maven Central

# Gradle plugins (third-party, явная версия)
com.google.protobuf version 0.9.6             ← обязателен для gRPC (кодогенерация из .proto)
com.netflix.dgs.codegen version 8.3.0         ← Netflix DGS codegen (GraphQL client)
com.vaadin version 25.2.0                     ← Vaadin UI framework
org.asciidoctor.jvm.convert version 4.0.5    ← Spring REST Docs (Asciidoctor)
org.cyclonedx.bom version 3.2.4              ← CycloneDX SBOM generation
org.owasp.dependencycheck version <version>   ← artifact: org.owasp:dependency-check-gradle
org.springframework.cloud.contract version 5.0.3 ← Spring Cloud Contract (Consumer-Driven)

# Gradle plugins (встроенные, версия не нужна)
jacoco

# BOM (третьи стороны, инлайн в convention plugin)
org.springframework.modulith:spring-modulith-bom:<version>         ← Spring Modulith
com.vaadin:vaadin-bom:<version>                                    ← Vaadin
de.codecentric:spring-boot-admin-dependencies:<version>            ← Spring Boot Admin

# Maven repositories (нестандартные)
# https://build.shibboleth.net/maven/releases   ← обязателен для spring-boot-starter-security-saml2
```
