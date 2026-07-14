# CLAUDE.md — notes-spring

> Последнее обновление: Tue Jul 14 23:38:08 IDT 2026 **Всё временно** — любое решение подлежит обсуждению и изменению.

Многомодульный Spring Boot 4 проект (`note/`, `user/`, `user-note/`, ...), реализующий hexagonal architecture единообразно во всех сервисах через Gradle convention plugins. Этот файл — единственный источник истины по конвенциям, статусу и решениям проекта; вся необходимая для работы над проектом информация должна быть здесь или в связанных `*.md` в корне репозитория (вынесены крупные справочные блоки — см. «Каталог файлов»/«Референс стартеров»), без обращения к внешним источникам.

**Оглавление:** [Начало сессии](#начало-каждой-сессии) · [Правила](#правила) · [Задачи](#-задачи) · [Архитектура и структура](#архитектура-и-структура-проекта) · [Именование](#именование) · [Стек](#стек) · [Технологии](#технологии) · [Принятые решения](#принятые-решения) · [Открытые решения](#открытые-решения) · [Каталог файлов](#каталог-файлов-проекта) · [Референс стартеров](#-референс-полный-набор-spring-boot-стартеров-объединение-всех-присланных-максимальный--запрещено-удалять-или-сокращать)

---

## Начало каждой сессии

1. Прочитать этот файл
2. Текущий приоритет — раздел [Задачи](#-задачи)
3. Актуализировать файл: убрать устаревшее, отразить новые решения
4. Коммит + пуш _(постоянная авторизация — искл. из правила «Коммиты» ниже)_

---

## Правила

- **⚠️ ИСТОЧНИК ИСТИНЫ ДЛЯ SPRING-ФАКТОВ — НЕ MAVEN CENTRAL ПОИСК** [НЕ СОКРАЩАТЬ/НЕ УДАЛЯТЬ — исключение из правила «убирать избыточность», даже при явной задаче уменьшить объём CLAUDE.md] (2026-07-14, резкая реакция пользователя на ошибку): вопрос «существует ли артефакт X» **никогда** не проверять через `search.maven.org` (Solr-индекс отстаёт от реального репозитория, `0 numFound` НЕ значит «не существует» — конкретная ошибка: заявил, что `spring-boot-starter-r2dbc` не существует по 0 результатам поиска, хотя это реальный отдельный от `data-r2dbc` стартер). Источник истины — **Spring Initializr** (`start.spring.io/metadata/client` — реальный список id/name/description), **GitHub spring-projects**, **официальная документация docs.spring.io**. Maven Central годится только для номера версии уже известного, существующего артефакта — не для вопроса о самом его существовании
- **Язык** — общение на русском; код и идентификаторы — на английском
- **Файлы** — не изменять без явного указания; перед созданием нового файла изучить проект
- **Коммиты** — не коммитить без явного запроса, кроме начала сессии (см. выше); «Зафиксируй» = обновить CLAUDE.md и дату → коммит → пуш
- **Дата перед коммитом** — брать из `date +"%a %b %d %H:%M:%S %Z %Y"` (локальное время, формат как в `gradle-wrapper.properties`), не выдумывать
- **CI** — не изменять `.github/workflows/` без отдельного запроса
- **Списки вместо таблиц** — структурированные данные (путь/статус/комментарий и т. п.) — плоским списком `- поле — поле — поле`, не markdown-таблицей: правка одной записи не требует пересчёта соседних строк
- **Без ручных переносов строк** — один абзац/пункт/строка blockquote = одна строка файла, без жёсткого переноса на 80–120 символов; перенос только смысловой
- **Доступность CLAUDE.md для ИИ-агента** — формат оптимизирован для чтения/правки ИИ без потери информации: полный путь от корня репозитория в каждой строке каталога (строка самодостаточна без соседних заголовков), ≤ 2 уровня вложенности заголовков (`### сервис/` → `#### модуль/`), ASCII-статусы вместо emoji, явная схема полей перед списком — применять сразу, без подтверждения
- **Архитектурные решения** — сначала варианты с плюсами/минусами и рекомендацией, дождаться выбора пользователя; не реализовывать до явного решения
- **Пересмотр решений** — статус «принято»/«зафиксировано» (в т. ч. в «Принятые решения») не закрывает вопрос навсегда: любое решение по CRUD-сервисам и по проекту в целом нужно поднимать заново для обсуждения и пересмотра в подходящий момент, а не только по отдельному запросу
- **Кольцевые/ромбовидные зависимости — отслеживать при каждом изменении графа** (введено 2026-07-14, полный аудит на тот момент циклов не нашёл, единственный ромб — задокументированная bootable-ось, см. «Принятые решения» → Convention plugins): при правке convention-плагинов, Gradle project-зависимостей (`projects.*`) или Java-иерархий портов/адаптеров — проверять, что граф остаётся DAG, а новое схождение путей либо уже объяснено в CLAUDE.md, либо обсуждено с пользователем. Ромб не всегда ошибка (Gradle идемпотентно применяет один plugin id с разных путей), но новый/необъяснённый цикл или ромб — сообщить пользователю, не чинить молча
- **Build** — после каждого логического шага: `./gradlew clean check` (быстрее `build`: без `assemble`/`bootJar`) → обновить CLAUDE.md → коммит → пуш; перед коммитом дополнительно `./gradlew clean build` (проверяет и паковку — `bootJar`/`resolveMainClassName`, что `check` не покрывает)
- **Подзадачи** — крупную задачу разбивать на подзадачи; коммитить и пушить после каждой завершённой подзадачи, чтобы не терять прогресс при обрыве сессии
- **⚠️ Лимит размера файла — при превышении сокращать самостоятельно, без напоминания** (2026-07-14, актуализировано 2026-07-14 по прямому запросу пользователя — три ограничения ниже закреплены здесь именно затем, чтобы не повторять их в каждом запросе «сократи файл»): триггер — сообщение `⚠ CLAUDE.md is over the 150.0k-char limit`. Мерить `wc -m CLAUDE.md`, **не** `wc -c` — тот считает байты UTF-8, а из-за кириллицы (2 байта/символ) байты почти вдвое больше реального числа символов, на котором основан лимит (проверено эмпирически 2026-07-14). Три вещи трогать нельзя, только переформулировать:
  1. Раздел «Правила» — ни одна инструкция не теряется по смыслу; формулировку каждого пункта можно сжимать (кроме отдельно помеченных исключений, см. правило про Maven Central выше)
  2. `spring-boot-starters-reference.md` (вынесен из CLAUDE.md 2026-07-14, см. «Каталог файлов»/«Референс стартеров» выше) — сам список зависимостей (и сам gradle-код) не сокращать и не удалять из него строки; допустима только проверка на дубликаты. Пояснительный текст вокруг списка и в CLAUDE.md — редактировать можно
  3. `file-catalog.md` и `convention-plugins.md` (вынесены из CLAUDE.md 2026-07-14) — список файлов и их статусы ([DONE]/[REVIEW]/[ADD]/[REMOVED]) не менять и не удалять по инициативе агента (можно добавлять пропущенные файлы `[REVIEW]`, можно убирать строки реально удалённых файлов, `[DONE]` — только по явному указанию пользователя); комментарий к каждому файлу — можно свободно сокращать вплоть до полного удаления
  Всё остальное (в первую очередь «Задачи»/«Принятые решения»/«Открытые решения» — исторические подробности) сокращать без ограничений, не теряя решения по существу

---

## ⚡ Задачи

**Текущая работа** — построчный пересмотр «Каталог файлов проекта»: ровно один файл за раз — разбор → утверждение пользователем → `[DONE]` → следующая строка `[REVIEW]` сверху вниз. Не пакетами, не забегая вперёд.

**Завершено 2026-07-14** (детали решений — см. «Принятые решения»/«Именование»/«Стиль кода», здесь только пойнтеры, чтобы не дублировать):
- Аудит кольцевых/ромбовидных зависимостей — по convention-плагинам, Gradle project-зависимостям и Java-иерархии портов: циклов/диамантов не найдено (единственное схождение путей — уже задокументированная bootable-ось, см. «Convention plugins» ниже)
- `spring-boot-starter-validation`(`-test`) → `com.example.spring-boot`; `spring-boot-starter-actuator`(`-test`) → `com.example.spring-boot-application` (bootable-ось, не BOM-цепочка) — `com.example.spring-boot-actuator` удалён
- `jakarta.validation-api:3.1.1` → `com.example.base` (`implementation`) — делает `jakarta.validation.constraints.*` видимыми в `domain/`; сами аннотации `@NotNull`/`@NotBlank` на полях по-прежнему не расставлены (см. «Открытые решения»)
- `{Entity}Persistable` — общий маркер-интерфейс в `domain/`, реализован всеми 15 model-классами; `user-note.role` унифицирован до `UserNoteRole` enum везде; `schema.sql` создан для `data-jdbc`/`data-r2dbc` во всех трёх сервисах — закрывает «Управление схемой для R2DBC/JDBC»
- `data-jdbc/` переведён с сырого `NamedParameterJdbcTemplate` на Spring Data JDBC (`model/`+`repository/`+`mapper/`, `ListCrudRepository`) по всем трём сервисам — прежнее «без repository» решение признано ошибочным
- Референсы Spring Initializr объединены в единый блок в конце файла

**Завершено 2026-07-13**: убран `io.spring.javaformat` (не даёт переопределить `lineSplit`/`join_wrapped_lines`) — единственный гейт стиля теперь Checkstyle (`SpringChecks` + 5 модулей под 10 личных правил, см. «Стиль кода»), `spring-javaformat` запинен на `0.0.48-SNAPSHOT` (нужные чеки есть только в snapshot, риск мутации принят осознанно). Версии подняты до последних (Java 25, Gradle 9.6.1, Spring Boot 4.1.0, JUnit 6.1.2, Checkstyle 13.7.0, reactor-core 3.8.6, jacoco 0.8.15 — см. «Синхронизация версий»). Переименования: трёхуровневая иерархия портов (`{Entity}Interface`/`ServiceInterface`/`ControllerInterface`), `data-contract`→`contract`, добавлен `application-reactive/` (см. «Именование»). Добавлены `spring-boot-starter-r2dbc`/`-jdbc` (plain API) рядом с `data-r2dbc`/`data-jdbc` по референсу Initializr.

**Пересмотр CRUD-сервисов** (2026-07-07) — статус ГОТОВО подтверждён по note/user/user-note; оставшиеся расхождения см. «Открытые решения».

Статус по сервисам и модулям:

```
ГОТОВО   note/ · user/ · user-note/ — domain · contract · contract-reactive · webmvc · webflux
                  data-jpa · data-mongodb · data-jdbc · data-r2dbc · data-mongodb-reactive
СКЕЛЕТ   registry/ · config/ · gateway/ — Eureka/Config/Gateway + Actuator подключены,
         сервисы стартуют и проходят contextLoads, но без routes/config-репозитория и
         без реальной регистрации note/·user/·user-note/
СКЕЛЕТ   auth/ — модуль создан, логики нет
ОТЛОЖЕНО   graphql/ · Spring Cache · OAuth2 · OpenFeign · Cloud LoadBalancer · Circuit Breaker
           — convention plugins готовы в build-logic, не применены ни в одном модуле
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

## Архитектура и структура проекта

**Многомодульная сборка (Gradle):**

```
build-logic/settings.gradle.kts            include(":convention"); included build, подключён через
                                            pluginManagement { includeBuild(...) } в корневом settings.gradle.kts
build-logic/convention/.../com.example.{name}.gradle.kts   convention plugins (Kotlin DSL), namespaced id
                                            без суффикса -conventions (com.example.library, не -conventions)
settings.gradle.kts                        единственный, в корне; include(":...") на все subprojects
{service}/{module}/build.gradle.kts        применяет plugin через id("com.example.{name}")
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
contract/          sync port interfaces         → domain/ (api)
contract-reactive/ reactive port interfaces     → domain/ (api) · reactor-core (implementation)
webmvc/                 driving adapter sync         → contract/
webflux/                driving adapter reactive     → contract-reactive/
data-jpa/               driven adapter JPA           → contract/
data-jdbc/              driven adapter JDBC          → contract/
data-r2dbc/             driven adapter R2DBC         → contract-reactive/
data-mongodb/           driven adapter MongoDB       → contract/
data-mongodb-reactive/  driven adapter Mongo rx      → contract-reactive/
application/            composition root             → все модули
```

**Внутренняя структура driven-адаптера** (пример `data-jpa/`; одинаково для всех технологий):

```
com.example.note.data.jpa.adapter      NoteAddJpaAdapter, ...
com.example.note.data.jpa.model        NoteEntity
com.example.note.data.jpa.mapper       NoteJpaMapperContract, NoteJpaMapper
com.example.note.data.jpa.repository   NoteJpaRepository
```

Пакет `model/` используется во всех технологиях (внутреннее имя класса — `Entity`/`{Tech}Entity` для JPA/R2DBC, `Document`/`ReactiveDocument` для MongoDB — см. «Именование»). Driving-адаптеры (`webmvc/`, `webflux/`) организованы плоско, без вложенных пакетов.

---

## Именование

Формат каждой строки: **элемент** — паттерн — пример.

- **Sync base port interface** — `{Entity}Interface` — `NoteInterface`
- **Sync service port interface** — `{Entity}ServiceInterface` (наследник `{Entity}Interface`) — `NoteServiceInterface`
- **Sync controller port interface** — `{Entity}ControllerInterface` — `NoteControllerInterface`
- **Reactive base port interface** — `{Entity}ReactiveInterface` — `NoteReactiveInterface`
- **Reactive service port interface** — `{Entity}ServiceReactiveInterface` — `NoteServiceReactiveInterface`
- **Reactive controller port interface** — `{Entity}ControllerReactiveInterface` — `NoteControllerReactiveInterface`
- **Adapter impl (sync и reactive)** — голый `{Entity}Service` в пакете своей технологии, без суффикса технологии в имени класса — `com.example.note.data.jpa.adapter.NoteService`, `com.example.note.data.r2dbc.adapter.NoteService` и т. д.
- **Controller** — `{Entity}Controller` — `NoteController`
- **Mapper interface** — `{Entity}{Tech}MapperContract` — `NoteJpaMapperContract`
- **Mapper impl** — `{Entity}{Tech}Mapper` — `NoteJpaMapper`
- **Spring Data repo** — `{Entity}{Tech}Repository` — `NoteJpaRepository`
- **JPA model class** — `{Entity}Entity` — `NoteEntity`
- **R2DBC model class** — `{Entity}{Tech}Entity` — `NoteR2dbcEntity`
- **MongoDB model class** — `{Entity}Document` — `NoteDocument`
- **MongoDB reactive model class** — `{Entity}ReactiveDocument` — `NoteReactiveDocument`
- **Общий маркер-интерфейс модели сущности** (новое, 2026-07-14) — `{Entity}Persistable`, в `domain/` — `NotePersistable`, `UserPersistable`, `UserNotePersistable`; реализуют все 5 технологических model-классов сущности сразу (единственный общий предок sync+reactive). Осознанно выбрано имя, пересекающееся с реальным `org.springframework.data.domain.Persistable<ID>` — не конфликтует (префикс сущности делает simple name другим), но напоминает: сам маркер в `domain/` остаётся чистым Java без зависимости на `spring-data-commons`, а НАСТОЯЩИЙ `Persistable<ID>` (если понадобится `isNew()`) реализуется отдельно в каждом адаптере — см. «Принятые решения» → «Архитектура»

**Tech**: `Jpa` · `Mongo` · `Jdbc` · `R2dbc` · `MongoReactive` (в имени класса — только для `{Tech}MapperContract`/`{Tech}Mapper`/`{Tech}Repository`/`{Tech}Entity`; адаптеры и порт-интерфейсы суффикс технологии в имени не несут — технология различается пакетом/модулем)

---

## Стек

Формат каждой строки: **инструмент** — версия.

- **Java** — 25 (было 21 до 2026-07-13; обе LTS — см. «Задачи» → «Обновление версий»)
- **Gradle** — 9.6.1
- **Spring Boot** — 4.1.0 (было 4.0.6 до 2026-07-13)
- **Spring Cloud** — 2025.1.2 (уже последняя, совместима и с Boot 4.1.0 — проверено 2026-07-13)

**Spring Boot 4 — критичные отличия от Boot 3** (источник: существующие `build.gradle.kts` в проекте; документация docs.spring.io местами показывает Boot 3 и не заслуживает доверия — сверяться с файлами проекта):

- `starter-web` → `starter-webmvc`; `starter-aop` → `starter-aspectj`
- `@MockBean`/`@SpyBean` → `@MockitoBean`/`@MockitoSpyBean`
- OAuth2-стартеры: `oauth2-resource-server` → `security-oauth2-resource-server`; аналогично для `client` и `authorization-server`
- Spring Authorization Server — часть Spring Security 7, отдельной версии не имеет
- `@WebMvcTest`, `@DataJpaTest` — в отдельных `*-test` стартерах, не в `starter-test`
- `@SpringBootTest` не подключает MockMvc/WebClient автоматически — нужен `@AutoConfigureMockMvc`
- Jackson 3: пакет `tools.jackson` (кроме `jackson-annotations` — остался на `com.fasterxml`)

---

## Технологии

Технологии за пределами Spring-стека (уже используемые или отложенные) — от инструментов разработки к production-инфраструктуре и архитектурным паттернам:

- **Gradle** — система сборки для JVM: граф задач с инкрементальным up-to-date tracking и build cache. Уже используется (см. «Стек»): Kotlin DSL, convention-плагины в `build-logic/convention/`
- **testcontainers** — интеграционные тесты через реальные зависимости (БД, брокеры) в Docker-контейнерах вместо моков
- **Docker Compose** — один YAML описывает и запускает несколько контейнеров как одно окружение; локальный оркестратор, однохостовый предшественник Kubernetes
- **Kubernetes** — production-оркестратор контейнеров: реплики, self-healing, rolling update, сервис-дискавери (Pod/Deployment/Service/ConfigMap)
- **Terraform** — infrastructure as code (HCL): декларативное состояние инфраструктуры, `plan`/`apply` через state-файл. В проекте — provisioning + branch protection через GitHub-провайдер
- **Jenkins** — CI/CD-сервер: пайплайн build → test → deploy из `Jenkinsfile`
- **SonarQube** — статический анализ (баги, уязвимости, code smells, покрытие через JaCoCo) с quality gate поверх PR; дополняет Checkstyle/NullAway/JaCoCo
- **Amazon Web Services** — целевая cloud-платформа (EC2, S3, RDS, EKS, Lambda), обычно через Terraform
- **Redis** — in-memory key-value: кэш, сессии, rate-limiting, pub/sub; кандидат под Spring Cache (см. «Задачи», ОТЛОЖЕНО)
- **Kafka / RabbitMQ** — message broker: Kafka — топики/consumer groups/event-streaming; RabbitMQ — классическая очередь (см. «Открытые решения» → «Регистрация auth/ ↔ user/»)
- **Elastic Stack** — Elasticsearch (поиск/хранение) + Logstash/Beats (логи) + Kibana (визуализация)
- **OAuth2** — RFC 6749, делегирует выдачу access-токена внешнему Authorization Server. В Spring Boot 4 — три отдельных стартера (`security-oauth2-resource-server`/`-client`/`-authorization-server`); convention-плагины готовы, не применены. **Кто держит роль Authorization Server** — открытый вопрос: (1) self-hosted (`spring-boot-starter-security-oauth2-authorization-server`, Spring Security 7) — полный контроль, своя эксплуатация; (2) managed IdP — Okta, Auth0, AWS Cognito, Azure AD/Entra ID, Keycloak (self-hosted компромисс). Пересекается с «Регистрация auth/ ↔ user/» — определяет, останется ли `auth/` тонким прокси или полноценным Authorization Server
- **jMolecules** — DDD/hexagonal-аннотации (`@Entity`, `@ValueObject`, `@AggregateRoot`); ценность — в связке с ArchUnit-правилами, проверяющими соответствие кода архитектуре
- **Axon Framework** — CQRS + Event Sourcing: команды → события (append-only Event Store), read-модели строятся отдельно. Следующий уровень после текущей hexagonal-архитектуры

Spring Cache, Spring OpenFeign, Spring Cloud LoadBalancer, Spring Cloud Circuit Breaker — часть Spring-стека, справки не здесь; статус — см. «Задачи».

---

## Принятые решения

### Архитектура

- **Convention plugins в `build-logic/convention/`** (included build, не `buildSrc/`) — лучше инкрементальность (правка плагина не инвалидирует весь билд), `./gradlew clean build` из корня остаётся одной командой. Id namespaced (`com.example.{name}`) — снимает конфликт с внешними плагинами. Плагины лежат плоско в одном каталоге — id берётся только из имени файла, алфавит уже даёт естественную группировку (`spring-boot-*`, `spring-cloud-*`, ...)
- Вся Gradle-конфигурация — Kotlin DSL; рабочий код сервисов — Java. Precompiled script plugins не видят typed-аксессоры каталога — `libs.findVersion("key").get().requiredVersion` вместо `libs.versions.x.get()`
- Id не привязанных к папке плагинов — по смысловой роли, не только алфавиту (`spring-boot-client-web`, не `webclient`, чтобы не путать с `webflux`). 7 плагинов 1:1 с папкой модуля/стартером (`webmvc`, `webflux`, `data-jpa`, `data-jdbc`, `data-r2dbc`, `data-mongodb`, `data-mongodb-reactive`) не переименовывать в отрыве от папки; `domain`/`contract*` — исключение (называются по зависимости, не по слою)
- `spring-boot-starter`/`-test` — в `com.example.spring-boot` (общий для любого Boot-модуля), не в `spring-boot-application`. Bootable-возможность (`org.springframework.boot`, ради `bootJar`) — вторая, ортогональная BOM-цепочке ось иерархии, см. диаграмму ниже
- Отступ 4 пробела, не табы — для Java жёсткая Checkstyle-проверка (`FileTabCharacterCheck`/`IndentationCheck`); для Kotlin build-скриптов — только соглашение
- Плагин `idea` — удалён (deprecated в Gradle, был только в domain/-модулях, несогласованно)
- Type-safe project accessors включены (`enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")`), требуют явного `rootProject.name` в обоих `settings.gradle.kts`
- `api` в `dependencies {}` — только когда тип в публичной сигнатуре модуля, иначе `implementation`, не полагаясь на транзитивный путь (проверено на `reactor-core`). `api project(...)` в `contract*/` — осознанное исключение (домен-типы больше неоткуда получить)
- Hexagonal: `domain/` не знает о JPA/MongoDB/Spring; адаптеры зависят от `contract*/` (порт) и явно `implementation(domain)`, не полагаясь на транзитивную `api(domain)`; `api(domain)` в самих `contract*/` остаётся — интерфейсы возвращают domain-типы
- **Один контроллер/контракт/адаптер на сущность, не на операцию** (пересмотрено 2026-07-08) — `NoteController` с 6 методами вместо по одному на HTTP-метод; применено по всем трём CRUD-сервисам, слои и hexagonal-изоляция не затронуты
- `service/` — только при координации нескольких портов; для простого CRUD не нужен
- `existsById` в контракте — валидный паттерн, не заменять на `findById`
- `add` ≠ `replace`: JPA — `save(null id)` vs `save(id)`; MongoDB — `insert()` vs `save()`
- `user-note/`: суррогатный `UUID id` (не составной `userId+noteId`) во всех технологиях; `userId+noteId` — unique constraint/index. Снимает ограничение Spring Data R2DBC (нет `@EmbeddedId`)
- Unique constraint `userId+noteId`: JPA — `@Table(uniqueConstraints=...)`; MongoDB — `@CompoundIndex(unique=true)`; R2DBC/JDBC (`spring-data-relational`) не имеют constraint-атрибутов вообще — только через схему
- **Управление схемой R2DBC/JDBC — `schema.sql`** (2026-07-14): `id UUID DEFAULT RANDOM_UUID() PRIMARY KEY`, `NOT NULL` на непустых колонках, `UNIQUE(...)` где нужно. Flyway/Liquibase не рассматривались (H2, схема простая). Для R2DBC реально исполняется при старте `application-reactive/`; для JDBC схема готова, но `data-jdbc` пока ни в одном сервисе не подключён к `application/`
- `user-note/role` — унифицирован до enum везде (2026-07-14) — Spring Data JDBC/R2DBC маппят enum в строку из коробки, ручная конвертация не нужна
- `data-jdbc/` — прежнее решение «сырой JDBC, без repository» отменено 2026-07-14 (признано ошибочным пользователем): теперь `model/`+`repository/`(`ListCrudRepository`)+`mapper/` по структуре `data-r2dbc/`, по всем трём сервисам
- **Трёхуровневая иерархия портов** вместо единого `{Entity}Contract`/`ContractReactive` (2026-07-13): `{Entity}Interface[Reactive]` (методы возвращают `Object`) → `{Entity}ServiceInterface[Reactive]` (конкретные типы, реализуют адаптеры `{Entity}Service`) → `{Entity}ControllerInterface[Reactive]` (`ResponseEntity`/`Mono<ResponseEntity>`). Приём — ковариантное переопределение `Object` (`Mono`/`Flux` — валидные подтипы, примитивный `void` — нет, поэтому sync `remove`/`deleteBy*` возвращают `Response`). `existsById`/`existsBy*` — реальные `GET`-эндпоинты. Разбивка методов по типу ключа — только в `user-note/`
- `user-note/`: операции разведены по типу ключа (суррогатный `userNoteId` и составной `userId+noteId` — отдельные методы, контракт из 15). `findByUserId`/`findByNoteId` сами бросают `UserNotFoundException`/`NoteNotFoundException` (собственные типы, не из `note/`/`user/` — сохраняет hexagonal-изоляцию между сервисами)
- **PUT (`replace*`) ≠ PATCH (`merge*`)**: `replace` — полная замена; `merge` — `null` в `request` значит «не менять». Оба бросают `NotFoundException` при отсутствии сущности
- **Диспетчеризация нескольких `@GetMapping` на одном пути** — `@RequestParam` не участвует в регистрации маппинга, нужен атрибут `params` (`@GetMapping(params = "userId")`), иначе `Ambiguous mapping` при старте контекста
- Reactive `findById`/`findBy*` бросают `{Entity}NotFoundException` вместо пустого `Mono` (пересмотрено 2026-07-13) — проверка перенесена из контроллера в сервис/адаптер, консистентно с sync
- `note/webflux`/`user/webflux`: `findAll()` оборачивает в `ResponseEntity<Flux<X>>` (было голый `Flux`)

### HTTP / Ошибки

- `ResponseEntity<T>` в контроллерах
- `ProblemDetail` (RFC 9457); `spring.mvc.problemdetails.enabled=true`
- Доменные исключения (`NoteNotFoundException`) — в `domain/`

### Маппинг

- Ручной, без MapStruct; маппер — ответственность адаптера
- `Pageable`/`Page`/`Specification` — утечка инфраструктуры, не использовать в контрактах

### Reactive-семантика

- `findById`/`findBy*` (кроме `findAll`) → `Mono<{Entity}Response>`, ошибка `{Entity}NotFoundException` через `switchIfEmpty` при пустом
- `findAll` → `Flux<{Entity}Response>`, в контроллере — `ResponseEntity<Flux<...>>`
- `remove`/`deleteBy*` → `Mono<Void>` (уже валидный подтип `Object`, правки не требовал)
- `existsById`/`existsBy*` → `Mono<Boolean>`

### Convention plugins — принцип именования и структура

Id — по зависимости/технологии, не по имени модуля/слоя (исключение — 7 плагинов 1:1 с папкой, см. выше). Префиксы убраны везде, кроме `spring-boot-*`/`spring-cloud-*`.

Иерархия — две независимые оси: BOM-цепочка (`base → library/reactor/spring-boot → spring-cloud → tech-plugin`, всегда 1 родитель) и bootable-ось (`org.springframework.boot`, подключается вторым `id(...)` где нужен `bootJar` — не нарушает правило «1 родитель» по первой оси, это отдельное измерение). Плюс агрегирующий `codequality` (4 плагина):

```
checkstyle      — id("checkstyle"); без com.example.* родителя; id("java") не нужен;
                  сам несёт configFile/configProperties/spring-javaformat-checkstyle dependency
nullaway        — id("java-library") + id("net.ltgt.errorprone"); java-library, не java:
                  нужен api("org.jspecify") по документации NullAway; следствие: все модули
                  транзитивно получают java-library через base → codequality → nullaway
jacoco          — id("jacoco"); id("java") не нужен
jacoco-report-aggregation — без родителя вообще (autoconfig)
codequality     — агрегатор 4 плагинов выше

com.example.base (root)  — java + toolchain + junit-jupiter + codequality (1 родитель: codequality)
├── library                — java-library (Gradle core), без Spring
├── reactor                — reactor-core + reactor-tools + reactor-test; родитель base
└── spring-boot             — io.spring.dependency-management + Spring Boot BOM
    ├── spring-boot-*        (17 технологических плагинов)
    └── spring-cloud         — + BOM spring-cloud-dependencies
        └── spring-cloud-*    (9 технологических плагинов)
```

Вторым родителем (bootable-ось) `spring-boot-application` дополнительно подключён к 4 standalone `spring-cloud-*`-плагинам (`config-server`/`eureka-server`/`gateway-webflux`/`gateway-webmvc`). `com.example.spring-boot-application` несёт `spring-boot-starter-actuator`(`-test`) — actuator привязан к bootable-оси, не к BOM-цепочке. `com.example.javaformat` удалён 2026-07-13 (см. «Задачи») — конфигурация перенесена в `com.example.checkstyle` без правок по модулям-потребителям.

### Синхронизация версий

- **Spring-экосистема** — единый источник `gradle/libs.versions.toml`; `build-logic` — отдельный included build, подключает тот же `.toml` отдельно (не видит корневой `gradle.properties` автоматически). Внутри precompiled-плагинов — `libs.findVersion("key")`, не typed-аксессоры
- `com.example.spring-boot` берёт BOM через `SpringBootPlugin.BOM_COORDINATES` (версия автоматически совпадает с плагином). `com.example.spring-cloud` собирает координаты вручную (у Spring Cloud нет своего Gradle-плагина/`BOM_COORDINATES`-аналога) — риск «два источника версии» неизбежен только здесь
- **junit-jupiter/junit-platform** — оба явно из каталога. Реальный найденный баг: расхождение 5.x/6.x при наследовании `java` через `spring-boot` (`TestEngine ... failed to discover tests`) — исправлено на `6.1.2`/`6.1.2` везде
- **reactor-core** — два источника (`com.example.reactor` фиксирует явно, Spring-адаптеры — через BOM); синхронизация ручная при апгрейде Boot (`./gradlew :note:webflux:dependencies | grep reactor-core`)
- **reactor-test** — убран explicit из `webflux`/`data-r2dbc`/`data-mongodb-reactive`, приходит транзитивно через `*-test`-компаньоны — не универсально, проверять каждый отдельно (`spring-cloud-gateway-webflux` своего `-test` не имеет вообще, `reactor-test` там ниоткуда не приходит осознанно)
- **Java** — единственный источник `.java-version`; CI — `actions/setup-java@v4` (`java-version-file`), Gradle — `toolchain` через Provider API (для configuration cache)

### Стиль кода

- Импорты: `java.*` → `javax.*` → `*` → `org.springframework.*`; пустая строка между группами
- `@NullMarked` на каждом `package-info.java`; `@Nullable` из `org.jspecify.annotations`
- **`@NullUnmarked` на классах Entity/Document** (пересмотрено 2026-07-14, было `@SuppressWarnings("NullAway.Init")` на конструкторе) — декомпиляция `spring-data-commons` показала, что framework всегда выбирает no-arg конструктор для чтения из БД через прямую рефлексию, минуя args-конструкторы; `@NullUnmarked` на классе — верный псевдоним «эта модель заполняется framework'ом вне null-checker'а», явные `@Nullable` внутри по-прежнему учитываются на границе. Применено ко всем 15 model-классам
- **Jakarta Bean Validation** — доступность подключена везде (`validation`-starter + `validation-api`), сами `@NotNull`/`@NotBlank` не расставлены ни на одном из 15 Entity/Document или domain-record'ов — не реализовывать до отдельного запроса
- **`@Service` на адаптерах, `@Repository` на Spring Data repository-интерфейсах** (2026-07-14, найдено и исправлено расхождение — все 15 `{Entity}Service` были на `@Repository`)
- Промежуточная переменная перед `return`, не inline в `.body()`
- **Длина строки не ограничена** (пересмотрено 2026-07-13) — переносы только вручную, авто-форматтер не используется
- **10 персональных правил форматирования** (реализовано 2026-07-13, п. 8 «порядок методов = порядок в интерфейсе» отложен — см. «Открытые решения»): неограниченная длина строки; никогда пустая строка перед `}`, кроме пустого тела метода/класса (там она обязательна); всегда `\n` в конце файла; ровно одна пустая строка между методами, не более одной подряд; аннотации перед полями/классами/методами — каждая на своей строке, перед параметрами — никогда не переносятся; отступ 4 пробела без табов. Ловушка: Checkstyle-сообщения форматируются через `MessageFormat` — литеральные `{`/`}` в тексте `message` ломают парсинг

---

## Открытые решения

- **Порядок методов реализации = порядок объявления в интерфейсе** (п. 8 из 10 личных правил форматирования, остальные 9 реализованы, см. «Стиль кода») — нужна семантическая привязка к реализуемому интерфейсу, синтаксическому Checkstyle не хватает резолвинга типов. Кандидаты: (1) кастомный Error Prone `BugChecker` (в проекте уже есть `net.ltgt.errorprone`); (2) JUnit-тест на `com.github.javaparser:javaparser-core` сравнивающий порядок методов интерфейс/реализация. Не реализовывать до отдельного запроса
- **Стратегия активации адаптеров** — `@Profile("jpa")` vs отдельные `application-jpa/`
- **Регистрация auth/ ↔ user/** — Lazy / Sync / Events (Kafka)
- **Каталог `data/` на уровне сервиса** — группировать `data-jpa`/`data-jdbc`/`data-r2dbc`/`data-mongodb`/`data-mongodb-reactive` в подкаталог `data/` (только каталог, не Gradle-модуль). Рекомендация — оставить как есть: нет функциональной пользы, а цена реорганизации реальна (`settings.gradle.kts`, typesafe-accessors меняют форму, сбивает пути в идущем пересмотре каталога). Пересмотреть при появлении конкретного драйвера
- **Возврат мутирующего use case** — DTO vs `void`
- **`@Transactional` на методах адаптера** — решение было зафиксировано, но не реализовано ни в одном адаптере ни одной технологии (обнаружено при пересмотре 2026-07-07). Решить: реализовать по всем адаптерам (~150 файлов) или снять как устаревшее (Spring Data репозитории и так транзакционны на уровне метода)
- **Комбинации technology в `application/`** — частично закрыто: `application-reactive/` (`webflux`+`data-r2dbc`+H2) добавлен во всех трёх CRUD-сервисах, зеркалит `application/`. Схема для R2DBC есть (`schema.sql`), но end-to-end работа не перепроверена отдельным тестом. MongoDB намеренно не подключена — рассматривается как будущая замена JPA/JDBC/R2DBC, не третья одновременная связка
- **`user/`: `findByEmail`/`findByUsername` без HTTP-входа** — доведены до всех driven-адаптеров, не выведены в `webmvc`/`webflux`. Варианты: задел под будущий `auth/`, добавить контроллеры сейчас, или убрать как неиспользуемое
- **Javadoc в `package-info.java`** — ни один пакет не содержит package-level Javadoc, только `@NullMarked`; `checkstyle.xml` сейчас исключает javadoc-проверки — включение потребует такого комментария в каждом пакете
- **Composite build на границе сервисов** — каждый сервис мог бы подключаться через `includeBuild(...)` вместо `include(...)`. Ни один сервис не ссылается на модули другого, так что accessors не пострадали бы, но цена (7 новых `settings.gradle.kts`, ручная агрегация `build`/`check`/`clean`) больше выигрыша без текущего драйвера (независимое CI/версионирование, разъезд по репозиториям)

---

## Каталог файлов проекта

Вынесен в [file-catalog.md](file-catalog.md) (2026-07-14, снятие объёма с CLAUDE.md) — полный git-отслеживаемый список файлов со статусами ([DONE]/[REVIEW]/[ADD]/[REMOVED]); precompiled script plugins `build-logic/` — отдельно в [convention-plugins.md](convention-plugins.md). Правила ведения (что можно/нельзя менять по инициативе агента) — см. «Правила» ниже, не переносились вместе с содержимым.

## ⛔ Референс: полный набор Spring Boot стартеров (объединение всех присланных, максимальный) — ЗАПРЕЩЕНО УДАЛЯТЬ ИЛИ СОКРАЩАТЬ

Вынесен в [spring-boot-starters-reference.md](spring-boot-starters-reference.md) (2026-07-14, снятие объёма с CLAUDE.md) — дословная копия со Spring Initializr, источник истины при проверке полноты convention-плагинов на пропущенные `-test`-компаньоны/plain-API-варианты. Сам список не сокращать и не удалять из него строки (см. «Правила»); допустима только проверка на дубликаты.
