# CLAUDE.md — notes-spring

> Последнее обновление: Wed Jul 08 19:29:37 IDT 2026 **Всё временно** — любое решение подлежит обсуждению и изменению.

Многомодульный Spring Boot 4 проект (`note/`, `user/`, `user-note/`, ...), реализующий hexagonal architecture единообразно во всех сервисах через Gradle convention plugins. Этот файл — единственный источник истины по конвенциям, статусу и решениям проекта; вся необходимая для работы над проектом информация должна быть здесь, без обращения к внешним источникам.

**Оглавление:** [Начало сессии](#начало-каждой-сессии) · [Правила](#правила) · [Задачи](#-задачи) · [Архитектура и структура](#архитектура-и-структура-проекта) · [Именование](#именование) · [Стек](#стек) · [Технологии](#технологии) · [Принятые решения](#принятые-решения) · [Открытые решения](#открытые-решения) · [Каталог файлов](#каталог-файлов-проекта)

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
- **Коммиты** — не коммитить без явного запроса, кроме начала сессии (см. выше); «Зафиксируй» = обновить CLAUDE.md и дату → коммит → пуш
- **Дата перед коммитом** — брать из `date +"%a %b %d %H:%M:%S %Z %Y"` (локальное время, формат как в `gradle-wrapper.properties`), не выдумывать
- **CI** — не изменять `.github/workflows/` без отдельного запроса
- **Списки вместо таблиц** — структурированные данные (путь/статус/комментарий, элемент/паттерн/пример и т. п.) — плоским списком `- поле — поле — поле`, не markdown-таблицей: правка одной записи — это правка одной строки, без пересчёта соседних
- **Без ручных переносов строк** — один абзац/пункт списка/строка blockquote — одна строка файла, без жёсткого переноса на 80–120 символов; перенос — только смысловой (конец абзаца, новый пункт списка, пустая строка)
- **Доступность CLAUDE.md для ИИ-агента** — при любой правке этого файла формат оптимизируется для чтения/правки ИИ-агентом без потери информации: полный путь от корня репозитория в каждой строке каталога (строка самодостаточна без соседних заголовков), не более 2 уровней вложенности заголовков (`### сервис/` → `#### модуль/`), ASCII-статусы вместо emoji, явная схема полей строкой прямо перед списком — применять сразу, без дополнительного подтверждения
- **Архитектурные решения** — сначала варианты с плюсами/минусами и рекомендацией, дождаться выбора пользователя; не реализовывать до явного решения
- **Пересмотр решений** — статус «принято»/«зафиксировано» (в т. ч. в «Принятые решения») не закрывает вопрос навсегда: любое решение по CRUD-сервисам и по проекту в целом нужно поднимать заново для обсуждения и пересмотра в подходящий момент, а не только по отдельному запросу
- **Build** — после каждого логического шага: `./gradlew clean check` (быстрее `build`: без `assemble`/`bootJar`) → обновить CLAUDE.md → коммит → пуш; перед коммитом дополнительно `./gradlew clean build` (проверяет и паковку — `bootJar`/`resolveMainClassName`, что `check` не покрывает)
- **Подзадачи** — крупную задачу разбивать на подзадачи; коммитить и пушить после каждой завершённой подзадачи, чтобы не терять прогресс при обрыве сессии

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

**Пересмотр CRUD-сервисов проведён** (`note/`/`user/`/`user-note/`, 2026-07-07) — статус ГОТОВО подтверждён по всем 6 пунктам (единообразие между сервисами, соответствие принятым решениям, актуальность именования, hexagonal-изоляция, SRP, комбинации technology в `application/`). Часть найденных расхождений уже выровнена в коде (webflux exception handler и update-контроллер `note/`, порядок зависимостей `note/application/`, раздел «Именование») — как и любое решение в проекте, это не окончательно и может быть пересмотрено при обсуждении. Остальные находки прямо оставлены открытыми вопросами: см. «Открытые решения» → `@Transactional`, «Комбинации technology в `application/`», SRP, `findByEmail`/`findByUsername` без HTTP-входа, `role` как `String` в R2DBC/JDBC.

**Spring JavaFormat** (`io.spring.javaformat`): таски `format`/`checkFormat`; `checkFormat` автоматически выполняется при стандартном `check`. После правок импортов/форматирования — гонять `./gradlew format`, не чинить импорты руками.

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
build-logic/settings.gradle.kts                 include(":convention"); included build,
                                                 подключён через pluginManagement { includeBuild(...) }
                                                 в корневом settings.gradle.kts
build-logic/convention/src/main/kotlin/         convention plugins (Kotlin DSL — вся Gradle-конфигурация
com.example.{name}.gradle.kts                   на Kotlin, но рабочий код сервисов остаётся на Java,
                                                 см. «Принятые решения» → «Архитектура»), namespaced id,
                                                 без суффикса -conventions: слово "convention" — только
                                                 в пути (build-logic/convention/), не в id
                                                 (com.example.library, не -conventions)
settings.gradle.kts                             единственный, в корне репозитория; include(":...") на
                                                 все subprojects всех сервисов (per-service нет)
{service}/{module}/build.gradle.kts             применяет plugin через id("com.example.{name}")
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
data-contract-reactive/ reactive port interfaces     → domain/ (api) · reactor-core (implementation)
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

Пакет `model/` используется во всех технологиях (внутреннее имя класса — `Entity`/`{Tech}Entity` для JPA/R2DBC, `Document`/`ReactiveDocument` для MongoDB — см. «Именование»). Driving-адаптеры (`webmvc/`, `webflux/`) организованы плоско, без вложенных пакетов.

---

## Именование

Формат каждой строки: **элемент** — паттерн — пример.

- **Sync port interface** — `{Entity}{Op}Contract` — `NoteAddContract`
- **Reactive port interface** — `{Entity}{Op}ContractReactive` — `NoteAddContractReactive`
- **Adapter impl** — `{Entity}{Op}{Tech}Adapter` — `NoteAddJpaAdapter`
- **Mapper interface** — `{Entity}{Tech}MapperContract` — `NoteJpaMapperContract`
- **Mapper impl** — `{Entity}{Tech}Mapper` — `NoteJpaMapper`
- **Spring Data repo** — `{Entity}{Tech}Repository` — `NoteJpaRepository`
- **JPA model class** — `{Entity}Entity` — `NoteEntity`
- **R2DBC model class** — `{Entity}{Tech}Entity` — `NoteR2dbcEntity`
- **MongoDB model class** — `{Entity}Document` — `NoteDocument`
- **MongoDB reactive model class** — `{Entity}ReactiveDocument` — `NoteReactiveDocument`

**Tech**: `Jpa` · `Mongo` · `Jdbc` · `R2dbc` · `MongoReactive`

---

## Стек

Формат каждой строки: **инструмент** — версия.

- **Java** — 21
- **Gradle** — 9.6.0
- **Spring Boot** — 4.0.6
- **Spring Cloud** — 2025.1.2

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

Технологии за пределами Spring-стека — уже используемые или отложенные на будущее (без привязки к конкретному модулю) — со справкой, что это за инструмент и как он соотносится с проектом, в логичном порядке освоения — от инструментов разработки к production-инфраструктуре и архитектурным паттернам:

- **Gradle** — система сборки для JVM-проектов: граф задач (`tasks`) с инкрементальным up-to-date tracking и build cache, зависимости — из Maven-репозиториев. Уже используется в проекте (версия — см. «Стек»): Kotlin DSL (`build.gradle.kts`), convention-плагины в `build-logic/convention/` (см. «Принятые решения» → «Архитектура»)
- **testcontainers** — библиотека для интеграционных тестов: поднимает реальные зависимости (БД, брокеры и т. д.) в Docker-контейнерах прямо из теста через JUnit-расширение, вместо моков
- **Docker Compose** — один YAML-файл описывает и запускает несколько контейнеров как одно окружение (общая сеть, `depends_on`); локальный оркестратор dev/test окружения, однохостовый предшественник Kubernetes
- **Kubernetes** — production-оркестратор контейнеров: реплики, self-healing, rolling update, сервис-дискавери поверх кластера машин (Pod/Deployment/Service/ConfigMap)
- **Terraform** — infrastructure as code (HashiCorp, язык конфигурации HCL): декларативно описывает желаемое состояние инфраструктуры, сверяет его с реальным через state-файл и показывает diff (`plan`) перед применением (`apply`); провайдеры — плагины под конкретные API (AWS, GitHub и т. д.). В проекте — декларативное provisioning облачных ресурсов, кластеров, а также platform-независимая настройка репозитория (branch protection и т. п.) через GitHub-провайдер
- **Jenkins** — сервер автоматизации CI/CD: по триггеру (пуш, PR, расписание) гоняет пайплайн (build → test → deploy), описанный в `Jenkinsfile`
- **SonarQube** — платформа статического анализа кода: баги, уязвимости, code smells, покрытие (агрегирует отчёты JaCoCo) в общем дашборде с историей по коммитам/веткам и quality gate поверх PR; дополняет, не заменяет уже используемые в проекте Checkstyle/NullAway/JaCoCo — типичный шаг внутри Jenkins-пайплайна
- **Amazon Web Services** — целевая cloud-платформа (EC2, S3, RDS, EKS, Lambda и т. д.), обычно тоже разворачивается через Terraform
- **Redis** — in-memory key-value хранилище: кэш, сессии, rate-limiting, pub/sub; кандидат на роль провайдера под Spring Cache (см. «Задачи», статус ОТЛОЖЕНО)
- **Kafka / RabbitMQ** — message broker: в Kafka producers пишут в топики (партиционированные, реплицируемые), consumers читают независимо через consumer groups, сообщение хранится по retention-политике даже после доставки (может использоваться и как event-streaming платформа); RabbitMQ — классическая очередь, удаляющая сообщение после подтверждения обработки (см. открытое решение «Регистрация auth/ ↔ user/»)
- **Elastic Stack** — Elasticsearch (полнотекстовый поиск и хранение документов) + Logstash/Beats (сбор логов) + Kibana (визуализация); логирование и поиск, альтернатива Loki
- **OAuth2** — протокол авторизации (RFC 6749): делегирует выдачу access-токена внешнему серверу авторизации (Authorization Server) вместо того, чтобы приложение само проверяло логин/пароль пользователя. Роли: Resource Server (API, проверяет токен), Client (запрашивает токен от имени пользователя), Authorization Server (выдаёт токены). В Spring Boot 4 — три отдельных стартера (`security-oauth2-resource-server`/`-client`/`-authorization-server`, см. «Стек» → «Spring Boot 4 — критичные отличия»); convention-плагины подготовлены в `build-logic`, но не применены ни в одном модуле (см. «Задачи», статус ОТЛОЖЕНО)
- **jMolecules** — библиотека аннотаций для явной разметки DDD/hexagonal-концепций в коде (`@Entity`, `@ValueObject`, `@AggregateRoot`, `@Repository`, `@Service` и т. п. — не совпадают с одноимёнными аннотациями Spring/JPA); сами по себе не меняют поведение в рантайме — ценность в связке с ArchUnit-правилами jMolecules, которые проверяют, что код действительно соответствует заявленной архитектуре (например, что `domain/` не зависит от инфраструктуры)
- **Axon Framework** — Java-фреймворк для CQRS (Command Query Responsibility Segregation) и Event Sourcing: команды обрабатывают Aggregate'ы и порождают события, события — source of truth, хранятся в append-only Event Store вместо перезаписи состояния; читающая сторона (Query) строит read-модели (проекции) из тех же событий, отдельно от записи. Command Bus/Event Bus/Query Bus — внутренняя маршрутизация; Axon Server — опциональный выделенный event store + messaging (можно обойтись и без него). В проекте — следующий уровень после текущей hexagonal-архитектуры

Spring Cache, Spring OpenFeign, Spring Cloud LoadBalancer, Spring Cloud Circuit Breaker — сами являются частью Spring-стека (не внешним протоколом/инструментом, как всё перечисленное выше), поэтому справки не здесь; статус — см. «Задачи».

---

## Принятые решения

### Архитектура

- **Convention plugins — `build-logic/convention/`** (included build), не `buildSrc/`:
  - совместное использование между несколькими `settings.gradle.kts` неактуально (в репозитории он один), но `build-logic` лучше по инкрементальности (правка одного convention-плагина не инвалидирует весь билд, как `buildSrc`) и ближе к текущей рекомендованной практике Gradle
  - `./gradlew clean build` из корня остаётся одной командой — `includeBuild` прозрачен для пересборки всего проекта целиком
  - вложенный `convention/` (а не плагины прямо в `build-logic/`) — задел на будущие под-билды внутри `build-logic/`
  - id namespaced (`com.example.{name}`, по аналогии с пакетами `com.example.*`), а не плоские — стандартная практика для precompiled script plugins, снимает конфликт имён с плагинами из внешних репозиториев
- Convention-плагины организованы плоско в одном каталоге (`build-logic/convention/src/main/kotlin/`), без подпапок по категориям — проверено эмпирически: каталог не влияет на id precompiled script plugin (Gradle берёт id только из имени файла), но у референсов (Now in Android) плагины лежат плоско, а сам naming (`spring-boot-*`, `spring-cloud-*`, ...) уже даёт естественную алфавитную группировку без лишнего уровня вложенности
- **Вся Gradle-конфигурация — на Kotlin DSL** (было Groovy): convention-плагины, `settings.gradle.kts`, все `build.gradle.kts` модулей. Рабочий код сервисов остаётся на Java — миграция затронула только build-скрипты
  - мигрировали пошагово (сначала `build-logic`, потом внешний слой), проверяя `./gradlew clean build` на каждом шаге — обошлось без костылей, кроме одного легитимного ограничения (официально документированного, см. docs.gradle.org → Version Catalogs): precompiled script plugins не видят typed-accessor каталога (`libs.versions.x.get()`) тем же способом, что обычные build-скрипты
  - решение: локальная `val libs = extensions.getByType(VersionCatalogsExtension::class.java) .named("libs")` в начале каждого нуждающегося в версиях плагина + `libs.findVersion("key").get().requiredVersion` — ровно паттерн из официальной документации, без отдельного shared-файла (пробовали `Catalogs.kt` с shared extension property — работало, но не соответствовало документации; сошлись на локальном объявлении в каждом файле)
  - на время миграции `build-logic/convention/build.gradle.kts` временно применял оба плагина (`kotlin-dsl` + `groovy-gradle-plugin`), чтобы старые (`src/main/groovy/`) и новые (`src/main/kotlin/`) precompiled-плагины сосуществовали в одном модуле `convention` — подтверждено эмпирически, оба source set компилируются независимо без конфликтов
- Naming: id не привязанных к папке модуля плагинов (`spring-boot-client-rest`/`-client-web`, а не `restclient`/`webclient`) подбирается по смысловой роли, а не только по алфавиту — иначе риск ложной группировки (`webclient` рядом с `webflux`/`webmvc`, хотя это разные вещи: исходящий клиент vs driving-адаптер). Id плагинов, 1:1 соответствующих папке модуля и реальному Spring Boot starter (`webmvc`, `webflux`, `data-jpa`, `data-jdbc`, `data-r2dbc`, `data-mongodb`, `data-mongodb-reactive`), никогда не переименовываются в отрыве от папки. `domain/` и `data-contract*/` в это правило не попадают — их плагины (`java`, `library`, `reactor`) называются по зависимости, а не по слою, и папке уже не соответствуют
- `spring-boot-starter`/`-test` — общий для любого Spring Boot модуля, объявлен в `com.example.spring-boot`, не в `spring-boot-application`. Bootable-возможность (`id("org.springframework.boot")`, нужна ради `bootJar`/`resolveMainClassName`) — вторая, ортогональная BOM-цепочке ось иерархии convention-плагинов: см. «Convention plugins — принцип именования и структура» → «Иерархия» ниже
- **Отступ 4 пробела, не табы** — в convention-плагинах (`build-logic/`) и обоих `settings.gradle.kts`: `.springjavaformatconfig` (`indentation-style=spaces`) — источник истины по стилю для всего репозитория, а `build-logic/` изначально не форматировался этим правилом (Spring JavaFormat форматирует Java, не Groovy build-скрипты) и разошёлся на табы
- **Плагин `idea` — удалён** (был в бывшем `java-domain`, контент которого сейчас — часть `com.example.base`): deprecated в Gradle, будет убран в Gradle 10, и был применён только в `domain/`-модулях (несогласованно, больше нигде в плагинах)
- **Иерархия convention-плагинов** — правила и диаграмма: см. «Convention plugins — принцип именования и структура» ниже. Дополнительно (не показано на диаграмме): `codequality`-плагины не объявляют `id("java")` сами — он нужен только там, где есть java-specific dep-конфигурации (`implementation`, `compileOnly`, `api` и т. д.); если плагин использует только plugin-specific конфигурации (`checkstyle`, `errorprone`, `jacoco` и т. д.) — `id("java")` не нужен (исключение — `com.example.nullaway`, см. ниже). Цикла не возникает: codequality-плагины не применяют `com.example.base`. `jacoco-report-aggregation` — без родителя вообще: плагину не нужен `java` функционально, лишнее не настраивается (autoconfiguration/defaults — не подгонять структуру под единообразие там, где это не даёт реальной пользы)
- Type-safe project accessors (`projects.note.domain` вместо `project(':note:domain')`) — включены через `enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")` в корневом `settings.gradle.kts`; требуют явного `rootProject.name` в обоих `settings.gradle.kts` (иначе Gradle предупреждает о нестабильности между чекаутами — исправлено там же). Фича остаётся incubating в Gradle 9.6.0 (не graduated to stable с версии 7.0), но работает без единого костыля — проверено `./gradlew clean build` по всем сервисам
- `api` в `dependencies {}` — только когда тип зависимости используется в собственной публичной сигнатуре модуля (параметр/возврат публичного метода), иначе `implementation`; не полагаться на то, что тип и так транзитивно придёт потребителю другим путём. Проверено эмпирически на `reactor-core` в `com.example.reactor`: `webflux`/`data-r2dbc`/`data-mongodb-reactive` собираются с ним и как с `implementation` — `Mono`/`Flux` на их classpath приходят от собственных Spring-стартеров через BOM, а не транзитивно от `data-contract-reactive`, поэтому `api` там был не нужен (`./gradlew clean check` по всем сервисам подтвердил). `api project(': ...:domain)` в `data-contract*/build.gradle.kts` — осознанное исключение: `NoteResponse` и т. п. больше неоткуда получить, это единственный путь
- Hexagonal: `domain/` не знает о JPA/MongoDB/Spring; адаптеры знают только `data-contract/`
- Один контроллер на операцию (`NoteCreateController` → `POST /notes`)
- `service/` — только при координации нескольких портов; для простого CRUD не нужен
- `existsById` в контракте — валидный паттерн, не заменять на `findById`
- `add` ≠ `replace`: JPA — `save(null id)` vs `save(id)`; MongoDB — `insert()` vs `save()`
- `user-note/`: суррогатный `UUID id` (а не составной `userId+noteId` как PK) во всех технологиях, кроме `data-jdbc/` (там нет `model/`/`repository/` в принципе). `userId+noteId` — unique constraint/index, не PK. `id` выставлен в `domain/`/контрактах (`UserNoteResponse.id`, `UserNoteFindByIdContract`). Это сняло ограничение Spring Data R2DBC (нет `@EmbeddedId`) — `data-r2dbc/` теперь полноценный model+repository, без `DatabaseClient`
- Unique constraint `userId+noteId` по технологиям: JPA — `@Table(uniqueConstraints=...)` (работает, Hibernate создаёт схему); MongoDB — `@CompoundIndex(unique=true)` (работает); **Spring Data R2DBC enforces unique composite constraints at the database level rather than through entity annotations** — у `Table`/`Column` (`spring-data-relational`) нет атрибутов unique/constraints вообще (проверено декомпиляцией), а в проекте пока нет ни `schema.sql`, ни Flyway/Liquibase, ни подключения `data-r2dbc`/`data-jdbc` к `application/` — поэтому для R2DBC/JDBC constraint сейчас не создаётся нигде. Решается вместе с открытым вопросом «Управление схемой для R2DBC/JDBC»
- `data-jdbc/` (все сервисы) — намеренно без `model/`/`repository/`: `NamedParameterJdbcTemplate` + сырой SQL, `RowMapper` мапит `ResultSet` сразу в `*Response`

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

Id называется по подключаемой зависимости/технологии, а не по имени использующего модуля/слоя (исключение — 7 плагинов, 1:1 соответствующих папке модуля и реальному Spring Boot starter: `webmvc`, `webflux`, `data-jpa`, `data-jdbc`, `data-r2dbc`, `data-mongodb`, `data-mongodb-reactive` — тут имя папки важнее алфавита, не переименовывать в отрыве от неё). Плоская структура каталога — см. «Принятые решения» → «Архитектура» выше. Префиксы убраны везде, кроме семей `spring-boot-*`/`spring-cloud-*` — остальные плагины называются голым именем зависимости/инструмента (`checkstyle`, `nullaway`, `jacoco`, `library`, `reactor`, ...).

**Иерархия** — две независимые оси, не одна. Первая — BOM-цепочка наследования (`base → library/reactor/spring-boot → spring-cloud → tech-plugin`): тут родитель всегда один, без исключений. Вторая — «bootable»-возможность (`id("org.springframework.boot")`, нужна ради `bootJar`/`resolveMainClassName`; `spring-boot-starter`/`-test` сюда не относятся — они базовые для любого Spring Boot модуля и объявлены в `com.example.spring-boot`, см. «Принятые решения» → «Архитектура») — она ортогональна первой оси и подключается вторым `id(...)` там, где нужна; это не нарушение правила «1 родитель» по первой оси, а отдельное измерение. Кроме неё, есть агрегирующий `codequality` (собирает 5 плагинов):
```
checkstyle      — id("checkstyle"); без com.example.* родителя; id("java") не нужен
javaformat      — id("io.spring.javaformat") + id("checkstyle"); id("java") не нужен
nullaway        — id("java-library") + id("net.ltgt.errorprone"); java-library (не java):
                  нужен api("org.jspecify") по документации NullAway; следствие: все модули
                  транзитивно получают java-library через base → codequality → nullaway
jacoco          — id("jacoco"); id("java") не нужен
jacoco-report-aggregation — без родителя вообще (autoconfig)
codequality     — агрегатор 5 плагинов выше

com.example.base (root)  — java + toolchain + junit-jupiter + codequality (1 родитель: codequality)
├── library                — java-library (Gradle core), без Spring
├── reactor                — reactor-core + reactor-tools + reactor-test; родитель base (был library:
│                            java-library теперь приходит транзитивно через nullaway)
└── spring-boot             — io.spring.dependency-management + Spring Boot BOM
    ├── spring-boot-*        (17 технологических плагинов)
    └── spring-cloud         — + BOM spring-cloud-dependencies
        └── spring-cloud-*    (9 технологических плагинов)
```

Дерево выше — только BOM-цепочка. Вторым родителем (bootable-ось, не показана на диаграмме) `spring-boot-application` (сам лежит внутри `spring-boot-*`) дополнительно подключён к 4 плагинам внутри `spring-cloud-*`: `spring-cloud-config-server`, `spring-cloud-eureka-server`, `spring-cloud-gateway-webflux`, `spring-cloud-gateway-webmvc`.

- `com.example.base` — корень: `java` + toolchain (версия из `.java-version`, читается через `providers.fileContents(...).asText` — Provider API, а не `file.text` напрямую: корректно отслеживается configuration cache без дополнительных костылей) + `com.example.codequality` + `junit-jupiter`/`junit-platform-launcher` (testImplementation/testRuntimeOnly, обе версии из каталога) + `useJUnitPlatform()`. Раньше codequality и junit жили в отдельных плагинах (`java-codequality`, `com.example.junit-jupiter`) — теперь всё стянуто в один базовый `com.example.base`, и `domain/` (плоские Java-модули) применяет его напрямую, без отдельного identity для junit
- `com.example.codequality-{errorprone,jacoco,jacoco-report-aggregation,javaformat}` переименованы в `com.example.{errorprone,jacoco,jacoco-report-aggregation,javaformat}` — без префиксов `java-`/`codequality-` вообще; затем `errorprone` → `nullaway` (отражает основную роль плагина), добавлен новый `checkstyle`. `nullaway` использует `id("java-library")` для доступа к `api(...)` по документации NullAway; остальные не объявляют java-плагин — он приходит от `base` раньше по цепочке применения
- `com.example.checkstyle` остаётся отдельным плагином, хотя сейчас `javaformat` всегда применяется вместе с ним и оба независимо конфигурируют один и тот же extension `checkstyle {}` — раздельность держится на случай будущего отказа от `com.example.javaformat`: тогда `checkstyle`-проверки должны продолжить работать сами по себе, без правки по всем модулям заново
- `com.example.library` (был `java-contract`, затем `java-library`) — 1 родитель `com.example.base`; добавляет Gradle-плагин `java-library`, без Spring
- `com.example.reactor` (был `java-contract-reactive`, затем `java-reactor`) — родитель сменён с `library` на `base`: `java-library` теперь приходит транзитивно через `base → codequality → nullaway`, поэтому явный `library`-родитель избыточен. `reactor-core` + `reactor-tools` + `reactor-test` (`implementation`/`testImplementation`), версии — явные, из `libs.versions.reactor.core` (синхронизирована с тем, что резолвит Spring Boot BOM — см. «Синхронизация версий»), без `io.spring.dependency-management`/BOM — `domain`/`data-contract*` полностью свободны от Spring. `reactor-tools` сам по себе не активен — нужен явный вызов `ReactorDebugAgent.init()` в коде (обычно в `main()`) или `-javaagent`, просто наличие jar'а в classpath ничего не делает
- `com.example.spring-boot` / `com.example.spring-cloud` — 1 родитель `com.example.base`/ `spring-boot` соответственно (`spring-cloud` применяет `spring-boot` и добавляет BOM `spring-cloud-dependencies`) — `io.spring.dependency-management` + Spring Boot BOM; свой junit-platform-launcher/`useJUnitPlatform()` убран как дублирующий то, что уже даёт родитель `com.example.base`. Были `spring-boot-base`/`spring-cloud-base` — суффикс `-base` убран, как и `-conventions` ранее. Плагины, требующие `bootJar` — `spring-boot-application` (используют `note/`, `user/`, `user-note/`, `auth/`) и 4 standalone-сервисных `spring-cloud-*`-плагина (`spring-cloud-config-server`, `spring-cloud-eureka-server`, `spring-cloud-gateway-webflux`, `spring-cloud-gateway-webmvc` — `registry/`/`config/`/`gateway/` тоже имеют собственный `@SpringBootApplication`-класс, критерий тот же, что у обычных сервисов). `spring-boot-starter`/ `-test` у всех получателей и так есть транзитивно через родителя `spring-boot` (см. выше); вторые 4 получают именно `id("org.springframework.boot")`, применяя вторым родителем уже существующий `com.example.spring-boot-application` — не дублированием строк и не через третий базовый плагин
- `com.example.spring-boot-client-rest` / `com.example.spring-boot-client-web` (были `restclient`/ `webclient`) — переименованы, чтобы не смешиваться алфавитно и по смыслу с `webflux`/`webmvc` (это server-side driving-адаптеры, а `client-*` — исходящие HTTP-клиенты, разные вещи). `spring-boot-client-web` — 1 родитель (`spring-boot`), без явного `reactor-test`: подтверждено через POM `spring-boot-starter-webclient-test` на Maven Central — `reactor-test:3.8.5` приходит транзитивно (не универсально для всех `-test`-компаньонов Spring Boot 4 — см. «Синхронизация версий» → `reactor-test`)

### Синхронизация версий

- **Spring-экосистема** (Spring Boot, Spring Cloud, dependency-management, spring-javaformat, errorprone-plugin) — единый источник `gradle/libs.versions.toml` (Gradle Version Catalog). `build-logic` — отдельный included build и не видит корневой `gradle.properties`/каталог автоматически, поэтому `build-logic/settings.gradle.kts` подключает тот же `.toml`-файл отдельно (`dependencyResolutionManagement.versionCatalogs.create("libs") { from(files("../gradle/ libs.versions.toml")) }`). В обычных Kotlin build-скриптах (`build-logic/convention/ build.gradle.kts`) ключи с дефисом (`spring-boot`, `spring-cloud`, ...) дают typed-аксессоры: `libs.versions.spring.boot.get()`, `libs.versions.spring.cloud.get()`, `libs.versions.spring.dependency.management.get()`, `libs.versions.spring.javaformat.get()`, `libs.versions.errorprone.plugin.get()`. Внутри самих precompiled script plugins (`com.example.{name}.gradle.kts`) этот typed-аксессор недоступен (ограничение Gradle) — там `libs.findVersion("spring-cloud").get().requiredVersion` (см. «Принятые решения» → «Архитектура»)
- **`com.example.spring-boot`** берёт BOM через константу `SpringBootPlugin.BOM_COORDINATES` (класс из `spring-boot-gradle-plugin`, версия которого — `libs.versions.spring.boot` — уже на classpath `build-logic/convention/build.gradle.kts`) — версия BOM автоматически совпадает с версией плагина, каталог внутри precompiled-плагина не нужен. **`com.example.spring-cloud`** собирает координаты BOM вручную (`libs.findVersion("spring-cloud")...`) не по недосмотру: у Spring Cloud, в отличие от Spring Boot, нет собственного Gradle-плагина — `spring-cloud-dependencies` публикуется как обычный Maven BOM, аналога `SpringCloudPlugin.BOM_COORDINATES` не существует. Литеральная строка с версией из каталога (как у `spring-cloud`) технически сработала бы и для `spring-boot` — сейчас обе версии берутся из одного ключа `spring-boot` в каталоге, — но это заново вводит риск «два независимых источника одной версии», уже описанный ниже для `reactor-core`/`junit-jupiter`: разойдись однажды каталог и classpath-зависимость плагина, `BOM_COORDINATES` продолжит совпадать с реально применённым плагином по построению (версия читается из его собственного jar'а), а литеральная строка — нет. Для `spring-cloud` этот риск неизбежен (плагина, а значит и константы, не существует), для `spring-boot` — устранён сознательно
- **Инструменты codequality** (`jspecify`, `errorprone-core`, `nullaway`, `jacoco`, `checkstyle`) — тоже через каталог (`libs.versions.jspecify.get()` и т. д.), не зашиты текстом в `com.example.nullaway`/`-jacoco`/`-checkstyle` — раньше были разбросаны по файлам как строковые литералы
- **junit-jupiter / junit-platform** — оба явно из каталога в `com.example.base`. Реальный баг, найденный при объединении `java` с codequality/junit (см. «Принятые решения» → «Архитектура»): каталог был запинен на `junit-jupiter = "5.12.2"` (унаследовано от домена, где Spring не участвовал), но Spring Boot 4.0.6 фактически управляет **JUnit 6** (`junit-bom:6.0.3`) — расхождение было незаметно, пока `spring-boot`-плагины не стали наследовать `java` и не получили одновременно 5.x (из явного пина) и 6.x (из Spring BOM) — `TestEngine with ID 'junit-jupiter' failed to discover tests` из-за рассинхрона `junit-platform-launcher`/ `-engine`. Исправлено: `junit-jupiter = "6.0.3"`, `junit-platform = "6.0.3"` — в JUnit 6 Platform/Jupiter унифицировали нумерацию версий (раньше Platform жил на отдельной ветке `1.x`)
- **reactor-core** (`libs.versions.reactor.core`, сейчас `3.8.5`) — два независимых источника версии: `com.example.reactor` (используется в `domain`/`data-contract*`, без Spring) фиксирует версию явно, а Spring-адаптеры (`webflux`, `data-r2dbc`, `data-mongodb-reactive`, `spring-cloud-gateway-webflux`) получают `reactor-core` через `mavenBom(SpringBootPlugin.BOM_COORDINATES)`. Gradle не конфликтует — при расхождении версий побеждает старшая (resolution strategy «newest wins»), но синхронизация всё равно ручная:
  - **при апгрейде Spring Boot** — свериться, какую версию `reactor-core` резолвит новый BOM (`./gradlew :note:webflux:dependencies --configuration compileClasspath | grep reactor-core`) и обновить `libs.versions.toml`, иначе pinned-версия в `domain`/`data-contract*` молча устареет и будет переопределена BOM только за счёт того, что он окажется новее
  - тот же риск — у `junit-jupiter`/`junit-platform` выше: проверять оба при каждом апгрейде Spring Boot
- **reactor-test** — explicit `testImplementation("io.projectreactor:reactor-test")` убран из `spring-boot-webflux`/`spring-boot-data-r2dbc`/`spring-boot-data-mongodb-reactive`/ `spring-cloud-gateway-webflux`: проверено эмпирически (`./gradlew :note:webflux:dependencies --configuration testCompileClasspath`) — для первых трёх `reactor-test` приходит транзитивно через `*-test`-компаньоны (`spring-boot-starter-webflux-test` и т. п., фича Spring Boot 4 «на каждый стартер — свой `-test`-стартер»). Это не универсально: POM `spring-boot-starter-webclient- test` на Maven Central содержит `reactor-test`, а `spring-boot-starter-graphql-test` — нет (транзитивные зависимости каждого `-test`-компаньона нужно проверять отдельно, не экстраполировать по аналогии). `spring-cloud-gateway-webflux` — плагин Spring Cloud, а не Spring Boot, своего `-test`-компаньона не существует вообще, поэтому `reactor-test` там сейчас ниоткуда не приходит; оставлено осознанно без замены — ни один тест в `gateway/` пока не использует `Mono`/`Flux`/`StepVerifier`, добавлять зависимость превентивно не стали (см. «Правила»); вернуть явной строкой, когда появится реальный реактивный тест
- **Gradle** — версия зафиксирована в `gradle/wrapper/gradle-wrapper.properties`; CI (`./gradlew`) наследует её автоматически, отдельной синхронизации не требует
- **Java** — единственный источник `.java-version` (корень репозитория): CI читает его через `actions/setup-java@v4` (`java-version-file`), Gradle — через `toolchain` в `com.example.base`. Читается через `providers.fileContents(rootProject.layout.projectDirectory.file('.java-version')) .asText.get().trim().toInteger()` (Provider API), не `rootProject.file(...).text` напрямую — корректно отслеживается configuration cache; применяется почти во всех модулях транзитивно

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
- **Управление схемой для R2DBC/JDBC** — `schema.sql` vs Flyway/Liquibase; нужно для unique constraint `userId+noteId` в `user-note/data-r2dbc`/`data-jdbc` (см. «Принятые решения» → «Архитектура»)
- **Регистрация auth/ ↔ user/** — Lazy / Sync / Events (Kafka)
- **Возврат мутирующего use case** — DTO _(склонение)_ vs `void`
- **PATCH** — поддерживать или нет
- **`@Transactional` на методах адаптера** — решение было зафиксировано, но при пересмотре CRUD-сервисов (2026-07-07) выяснилось, что оно не реализовано ни в одном адаптере ни одной технологии ни одного сервиса. Решить: реализовать по всем адаптерам (~150 файлов) или снять решение как устаревшее (Spring Data репозитории уже транзакционны на уровне отдельного метода)
- **Комбинации technology в `application/`** — сейчас у каждого сервиса ровно одна связка (`webmvc`+`data-jpa`); решить, нужны ли доп. `application/`-модули (или профили) для остальных технологий, чтобы webflux/r2dbc/mongo и т. д. были реально запускаемы, а не только компилируемы
- **SRP: один класс/интерфейс — одна операция** — при пересмотре CRUD-сервисов (2026-07-07) подтверждено, что принцип соблюдается везде без нарушений; решить, стоит ли явно зафиксировать его как принятое архитектурное решение, или оставить неявным соглашением
- **`user/`: `findByEmail`/`findByUsername` без HTTP-входа** — доведены до всех driven-адаптеров (jpa/mongodb/mongodb-reactive/r2dbc/jdbc), но не выведены в `webmvc`/`webflux`. Варианты: оставить как задел под будущий `auth/` (поиск пользователя при логине), добавить контроллеры уже сейчас, или убрать как неиспользуемое до появления реального потребителя
- **`user-note/`: `role` — enum в JPA/MongoDB, но `String` в R2DBC/JDBC** — нативно в JPA (`@Enumerated(EnumType.STRING)`) и MongoDB/MongoDB reactive, но в R2DBC и JDBC — ручной `.name()`/`valueOf()` в мэппере, т. к. Spring Data R2DBC и сырой JDBC (`ResultSet`/`RowMapper`) не поддерживают enum-колонки нативно без конвертера. Обсудить: оставить ручной подход как оправданный технологическими ограничениями, или написать конвертер для R2DBC
- **Асимметрия `data-jdbc/`** — единственный driven-адаптер без `model/`/`repository/`/`mapper/`, тогда как остальные технологии единообразны (решение принято намеренно, см. «Принятые решения» → «Архитектура» → `data-jdbc/`). Но пока ни один `data-jdbc/` не подключён к `application/` ни в одном сервисе — пересмотреть при первом реальном использовании: не всплывёт ли потребность в паттерне ближе к остальным технологиям (например, ради тестируемости или переиспользования маппинга)

---

## Каталог файлов проекта

> Составлено 2026-07-08: полный проход по всем 452 git-отслеживаемым файлам репозитория, папка за папкой, свежим взглядом и со сверкой с этим документом. Статус `[REVIEW]` — для каждого существующего файла по умолчанию (правило «Пересмотр решений»: ничего не считается окончательно принятым при первом просмотре); `[DONE]` проставляется только точечно, когда конкретный файл явно обсуждён и закрыт в разговоре. `[ADD]` — файла сейчас нет, но он нужен для реальной работоспособности/проверяемости сервиса (в основном — тесты, которых сейчас нет вообще ни в одном из трёх ГОТОВО-сервисов, и схема БД для R2DBC/JDBC).
>
> Порядок разделов — по каталогам верхнего уровня (`user-note/` → `user/` → `registry/` → `note/` → `gradle/` → `gateway/` → `config/` → `build-logic/` → `auth/` → `.github/`), внутри раздела — по модулям в алфавитном порядке. Ровно 2 уровня заголовков: `### сервис/` → `#### модуль/`; всё глубже — плоский список под заголовком модуля, без под-заголовков по подпакетам. Формат каждой строки: `путь-от-корня-репозитория` — статус — комментарий — путь всегда полный (не только имя файла), строка самодостаточна без чтения окружающих заголовков.

### Корень репозитория (9 файлов)

- `.springjavaformatconfig` — [DONE] — `indentation-style=spaces` — источник истины по стилю (см. «Отступ 4 пробела»); поддерживает также `java-baseline` (V8/V17) — намеренно не задан
- `.java-version` — [REVIEW] — `21`; единственный источник версии Java, читается через toolchain в `com.example.base`
- `.gitignore` — [REVIEW] — стандартный Spring/IDE boilerplate; `.claude/`, `.idea/`, `.gradle/`, `build/` корректно исключены
- `.gitattributes` — [REVIEW] — LF для `gradlew`, CRLF для `*.bat`, binary для `*.jar`
- `settings.gradle.kts` — [REVIEW] — `includeBuild`, `TYPESAFE_PROJECT_ACCESSORS`, `rootProject.name`; состав `include(...)` совпадает со статусами в «Задачах»
- `gradlew.bat` — [REVIEW] — стандартный сгенерированный wrapper-скрипт
- `gradlew` — [REVIEW] — стандартный сгенерированный wrapper-скрипт
- `gradle.properties` — [REVIEW] — `configuration-cache.problems=warn` — раз config cache уже подтверждена рабочей без костылей, не ужесточить ли до `fail`?
- `CLAUDE.md` — [REVIEW] — сам документ


### user-note/ (133 файла + 30 предложенных)

**Все статусы — `[REVIEW]`**: согласно правилу «Пересмотр решений» в CLAUDE.md, ничто не считается окончательно принятым при первом просмотре; `[DONE]` ставит только человек.

#### Главные находки

1. **Тестов нет вообще, кроме `UserNoteApplicationTests.contextLoads()`.** Ни одного unit-теста мапперов, ни одного адаптерного/интеграционного теста (JPA/MongoDB/JDBC/R2DBC/MongoDB reactive), ни одного `@WebMvcTest`/`@WebFluxTest` для контроллеров. Это **не специфика user-note** — `note/` и `user/` находятся в том же состоянии (`git ls-files` подтверждает: только `*ApplicationTests.java` + пустой `application.properties` в каждом сервисе) — то есть отсутствие тестов является проектным, а не сервисным пробелом.
2. **Управления схемой для R2DBC/JDBC по-прежнему нет** — ни `schema.sql`, ни Flyway/Liquibase нигде в репозитории (`find -iname "*.sql"` — пусто). Unique constraint `user_id+note_id`, задокументированный как принятое решение для JPA/MongoDB, для R2DBC/JDBC остаётся нереализованным — подтверждает открытый вопрос «Управление схемой для R2DBC/JDBC» в CLAUDE.md.
3. **`@Transactional` отсутствует во всех ~35 адаптерах всех 5 driven-технологий** — подтверждает открытый вопрос «`@Transactional` на методах адаптера»: решение по-прежнему не реализовано.
4. **Реальное расхождение (стиль, не семантика)**: `UserNoteAddMongoAdapter` и `UserNoteAddMongoReactiveAdapter` строят/используют `document` иначе, чем аналогичные адаптеры в `note/`/`user/` (`NoteAddMongoAdapter`, `UserAddMongoReactiveAdapter` и т. п.) — там паттерн «сначала `document = mapper.toNewDocument(request)`, потом `insert(document)`, в ответе — тот же `document`»; в `user-note` — `document`/ответ строится из значения, возвращённого `insert(...)`, с инлайновым вызовом маппера внутри `insert(...)`. Поведенчески идентично (id генерируется вручную до вставки), но стилистически не единообразно между тремя CRUD-сервисами.
5. **Внутри самого `user-note`** все 5 driven-технологий (JPA/MongoDB/MongoDB reactive/R2DBC/JDBC) ведут себя единообразно по каждой из 8 операций (add/exists/findById/findByNoteId/ findByUserIdAndNoteId/findByUserId/remove/replace) — сигнатуры, обработка null/empty (`Optional`/`Mono`/`List`/`Flux`), порядок «проверить существование → бросить `NotFoundException` → обновить» в `replace` — везде одинаковы. Расхождений между технологиями не найдено.

#### user-note/application/ (6 файлов)

- `user-note/application/src/main/java/com/example/usernote/UserNoteApplication.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/application/src/main/java/com/example/usernote/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user-note/application/build.gradle.kts` — [REVIEW] — Соответствует конвенции: `spring-boot-application` + `spring-boot-h2-database`, зависимости domain+data-contract+webmvc+data-jpa — единственная связка technology (см. открытый вопрос «Комбинации technology в application/»)
- `user-note/application/src/main/resources/application.properties` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/application/src/test/java/com/example/usernote/UserNoteApplicationTests.java` — [REVIEW] — Единственный тест во всём сервисе — тривиальный `contextLoads()`. См. «Главные находки» п. 1
- `user-note/application/src/test/resources/application.properties` — [REVIEW] — Пустой файл (0 байт) — идентично `note/application/src/test/resources/application.properties`, не аномалия

#### user-note/data-contract-reactive/ (10 файлов)

- `user-note/data-contract-reactive/src/main/java/com/example/usernote/contract/reactive/UserNoteAddContractReactive.java` — [REVIEW] — `{Entity}{Op}ContractReactive`, `Mono<UserNoteResponse>` — соответствует конвенции
- `user-note/data-contract-reactive/src/main/java/com/example/usernote/contract/reactive/UserNoteExistsByUserIdAndNoteIdContractReactive.java` — [REVIEW] — `Mono<Boolean>` — соответствует reactive-семантике из CLAUDE.md
- `user-note/data-contract-reactive/src/main/java/com/example/usernote/contract/reactive/UserNoteFindByIdContractReactive.java` — [REVIEW] — `Mono<UserNoteResponse>` (пустой Mono вместо Optional.empty) — соответствует конвенции
- `user-note/data-contract-reactive/src/main/java/com/example/usernote/contract/reactive/UserNoteFindByNoteIdContractReactive.java` — [REVIEW] — `Flux<UserNoteResponse>` — соответствует конвенции
- `user-note/data-contract-reactive/src/main/java/com/example/usernote/contract/reactive/UserNoteFindByUserIdAndNoteIdContractReactive.java` — [REVIEW] — `Mono<UserNoteResponse>` — соответствует конвенции
- `user-note/data-contract-reactive/src/main/java/com/example/usernote/contract/reactive/UserNoteFindByUserIdContractReactive.java` — [REVIEW] — `Flux<UserNoteResponse>` — соответствует конвенции
- `user-note/data-contract-reactive/src/main/java/com/example/usernote/contract/reactive/UserNoteRemoveContractReactive.java` — [REVIEW] — `Mono<Void> remove(userId, noteId)` — соответствует reactive-семантике; по бизнес-ключу `userId+noteId`, а не по суррогатному `id` — согласуется с сигнатурой sync-контракта
- `user-note/data-contract-reactive/src/main/java/com/example/usernote/contract/reactive/UserNoteReplaceContractReactive.java` — [REVIEW] — `Mono<UserNoteResponse> replace(userId, noteId, request)` — соответствует конвенции
- `user-note/data-contract-reactive/src/main/java/com/example/usernote/contract/reactive/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user-note/data-contract-reactive/build.gradle.kts` — [REVIEW] — `com.example.reactor` + `api(projects.userNote.domain)` — соответствует конвенции

#### user-note/data-contract/ (10 файлов)

- `user-note/data-contract/src/main/java/com/example/usernote/contract/UserNoteAddContract.java` — [REVIEW] — `{Entity}{Op}Contract` — соответствует конвенции
- `user-note/data-contract/src/main/java/com/example/usernote/contract/UserNoteExistsByUserIdAndNoteIdContract.java` — [REVIEW] — `boolean` — соответствует конвенции («existsById в контракте — валидный паттерн»)
- `user-note/data-contract/src/main/java/com/example/usernote/contract/UserNoteFindByIdContract.java` — [REVIEW] — `Optional<UserNoteResponse>` — соответствует sync-семантике (аналог reactive Mono)
- `user-note/data-contract/src/main/java/com/example/usernote/contract/UserNoteFindByNoteIdContract.java` — [REVIEW] — `List<UserNoteResponse>` — соответствует конвенции
- `user-note/data-contract/src/main/java/com/example/usernote/contract/UserNoteFindByUserIdAndNoteIdContract.java` — [REVIEW] — `Optional<UserNoteResponse>` — соответствует конвенции
- `user-note/data-contract/src/main/java/com/example/usernote/contract/UserNoteFindByUserIdContract.java` — [REVIEW] — `List<UserNoteResponse>` — соответствует конвенции
- `user-note/data-contract/src/main/java/com/example/usernote/contract/UserNoteRemoveContract.java` — [REVIEW] — `void remove(userId, noteId)` — зеркалит reactive-контракт (`Mono<Void>`) — соответствует конвенции
- `user-note/data-contract/src/main/java/com/example/usernote/contract/UserNoteReplaceContract.java` — [REVIEW] — `UserNoteResponse replace(userId, noteId, request)` — соответствует конвенции
- `user-note/data-contract/src/main/java/com/example/usernote/contract/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user-note/data-contract/build.gradle.kts` — [REVIEW] — `com.example.library` + `api(projects.userNote.domain)` — соответствует конвенции

#### user-note/data-jdbc/ (13 файлов)

- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/adapter/UserNoteAddJdbcAdapter.java` — [REVIEW] — `NamedParameterJdbcTemplate` + сырой SQL, `id` генерируется вручную (`UUID.randomUUID()`) — соответствует принятому решению по `data-jdbc/` (нет model/repository)
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/adapter/UserNoteExistsByUserIdAndNoteIdJdbcAdapter.java` — [REVIEW] — `SELECT COUNT(*)` + `count != null && count > 0` — соответствует конвенции
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/adapter/UserNoteFindByIdJdbcAdapter.java` — [REVIEW] — Ручной `RowMapper` через `UserNoteJdbcMapperContract::fromRow`, `.stream().findFirst()` для `Optional` — соответствует конвенции
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/adapter/UserNoteFindByNoteIdJdbcAdapter.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/adapter/UserNoteFindByUserIdAndNoteIdJdbcAdapter.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/adapter/UserNoteFindByUserIdJdbcAdapter.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/adapter/UserNoteRemoveJdbcAdapter.java` — [REVIEW] — `DELETE ... WHERE user_id = :userId AND note_id = :noteId` — соответствует конвенции
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/adapter/UserNoteReplaceJdbcAdapter.java` — [REVIEW] — `SELECT id` → `orElseThrow(UserNoteNotFoundException)` → `UPDATE ... SET role`; та же структура, что и в JPA/Mongo/R2DBC/MongoReactive-вариантах — единообразно внутри сервиса
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/adapter/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/mapper/UserNoteJdbcMapper.java` — [REVIEW] — `{Entity}{Tech}Mapper`, `fromRow(ResultSet, int)`, `UserNoteRole.valueOf(...)` — соответствует открытому вопросу «role как String в R2DBC/JDBC» (здесь маппинг вручную, ожидаемо)
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/mapper/UserNoteJdbcMapperContract.java` — [REVIEW] — `{Entity}{Tech}MapperContract` — соответствует конвенции
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/mapper/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user-note/data-jdbc/build.gradle.kts` — [REVIEW] — `spring-boot-data-jdbc` + `implementation(dataContract)` — соответствует конвенции

#### user-note/data-jpa/ (17 файлов)

- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/adapter/UserNoteAddJpaAdapter.java` — [REVIEW] — `repository.save(mapper.toNewEntity(request))` — соответствует конвенции (`add` через `save(null id)`)
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/adapter/UserNoteExistsByUserIdAndNoteIdJpaAdapter.java` — [REVIEW] — Делегирует в derived query репозитория — соответствует конвенции
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/adapter/UserNoteFindByIdJpaAdapter.java` — [REVIEW] — `repository.findById(id).map(mapper::toResponse)` — соответствует конвенции
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/adapter/UserNoteFindByNoteIdJpaAdapter.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/adapter/UserNoteFindByUserIdAndNoteIdJpaAdapter.java` — [REVIEW] — Строка 28 — ровно 120 символов (лимит «до 120» соблюдён на грани); функционально и стилистически иначе замечаний нет
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/adapter/UserNoteFindByUserIdJpaAdapter.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/adapter/UserNoteRemoveJpaAdapter.java` — [REVIEW] — `repository.deleteByUserIdAndNoteId(userId, noteId)` — соответствует конвенции
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/adapter/UserNoteReplaceJpaAdapter.java` — [REVIEW] — `findByUserIdAndNoteId(...).orElseThrow(NotFound)` → `save(toExistingEntity(...))` — та же структура, что и в остальных технологиях
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/adapter/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/mapper/UserNoteJpaMapper.java` — [REVIEW] — `toNewEntity`/`toExistingEntity`/`toResponse`, ручной маппинг без MapStruct — соответствует конвенции
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/mapper/UserNoteJpaMapperContract.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/mapper/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/model/UserNoteEntity.java` — [REVIEW] — `{Entity}Entity`, `@Table(uniqueConstraints = {user_id, note_id})`, `@Enumerated(EnumType.STRING)` для `role`, `@SuppressWarnings("NullAway.Init")` на protected no-arg конструкторе — соответствует принятым решениям
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/model/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/repository/UserNoteJpaRepository.java` — [REVIEW] — `JpaRepository<UserNoteEntity, UUID>` + derived queries (`findByUserId`, `findByNoteId`, `findByUserIdAndNoteId`, `existsByUserIdAndNoteId`, `deleteByUserIdAndNoteId`) — соответствует конвенции
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/repository/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user-note/data-jpa/build.gradle.kts` — [REVIEW] — `spring-boot-data-jpa` + `implementation(dataContract)` — соответствует конвенции

#### user-note/data-mongodb-reactive/ (17 файлов)

- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/adapter/UserNoteAddMongoReactiveAdapter.java` — [REVIEW] — **Расхождение стиля с `note/`/`user/`**: `repository.insert(mapper.toNewDocument(request)).map(mapper::toResponse)` — маппер вызывается инлайново внутри `insert(...)`, без промежуточной переменной `document`; в `note/data-mongodb-reactive/NoteAddMongoReactiveAdapter` и `user/data-mongodb-reactive/UserAddMongoReactiveAdapter` сначала строится `document`, затем `repository.insert(document)`. Поведенчески эквивалентно, стилистически не единообразно между сервисами
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/adapter/UserNoteExistsByUserIdAndNoteIdMongoReactiveAdapter.java` — [REVIEW] — `Mono<Boolean>` через derived query — соответствует конвенции
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/adapter/UserNoteFindByIdMongoReactiveAdapter.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/adapter/UserNoteFindByNoteIdMongoReactiveAdapter.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/adapter/UserNoteFindByUserIdAndNoteIdMongoReactiveAdapter.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/adapter/UserNoteFindByUserIdMongoReactiveAdapter.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/adapter/UserNoteRemoveMongoReactiveAdapter.java` — [REVIEW] — `Mono<Void>` через `repository.deleteByUserIdAndNoteId(...)` — соответствует конвенции
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/adapter/UserNoteReplaceMongoReactiveAdapter.java` — [REVIEW] — `findByUserIdAndNoteId(...).switchIfEmpty(Mono.error(NotFound)).flatMap(...).map(...)` — та же структура, что и в R2DBC-варианте; соответствует конвенции
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/adapter/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/mapper/UserNoteMongoReactiveMapper.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/mapper/UserNoteMongoReactiveMapperContract.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/mapper/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/model/UserNoteReactiveDocument.java` — [REVIEW] — `{Entity}ReactiveDocument`, `@CompoundIndex(unique = true)` на `userId+noteId` — соответствует принятому решению
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/model/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/repository/UserNoteMongoReactiveRepository.java` — [REVIEW] — `ReactiveMongoRepository<UserNoteReactiveDocument, UUID>` + derived queries — соответствует конвенции
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/repository/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user-note/data-mongodb-reactive/build.gradle.kts` — [REVIEW] — `spring-boot-data-mongodb-reactive` + `implementation(dataContractReactive)` — соответствует конвенции

#### user-note/data-mongodb/ (17 файлов)

- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/adapter/UserNoteAddMongoAdapter.java` — [REVIEW] — **Расхождение стиля с `note/`/`user/`**: `UserNoteDocument document = mongoTemplate.insert(mapper.toNewDocument(request))` — переменная `document` получает значение из возврата `insert(...)`; в `note/data-mongodb/NoteAddMongoAdapter` и `user/data-mongodb/UserAddMongoAdapter` — сначала `document = mapper.toNewDocument(request)`, затем отдельным вызовом `mongoTemplate.insert(document)`, ответ строится из исходной переменной. Поведенчески эквивалентно (id генерируется вручную заранее), но стилистически расходится
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/adapter/UserNoteExistsByUserIdAndNoteIdMongoAdapter.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/adapter/UserNoteFindByIdMongoAdapter.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/adapter/UserNoteFindByNoteIdMongoAdapter.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/adapter/UserNoteFindByUserIdAndNoteIdMongoAdapter.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/adapter/UserNoteFindByUserIdMongoAdapter.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/adapter/UserNoteRemoveMongoAdapter.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/adapter/UserNoteReplaceMongoAdapter.java` — [REVIEW] — Использует одновременно `MongoTemplate` (для `save`) и `UserNoteMongoRepository` (для `findByUserIdAndNoteId`) — в `note/`/`user/` (там id = входной параметр) такой комбинации нет, но здесь она структурно обоснована суррогатным `id`: сначала нужно найти существующий документ по `userId+noteId`, чтобы узнать его `id`. Не нарушение, а следствие принятого решения про суррогатный ключ в `user-note/`
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/adapter/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/mapper/UserNoteMongoMapper.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/mapper/UserNoteMongoMapperContract.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/mapper/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/model/UserNoteDocument.java` — [REVIEW] — `{Entity}Document`, `@CompoundIndex(unique = true)` на `userId+noteId` — соответствует принятому решению
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/model/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/repository/UserNoteMongoRepository.java` — [REVIEW] — `MongoRepository<UserNoteDocument, UUID>` + derived queries — соответствует конвенции
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/repository/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user-note/data-mongodb/build.gradle.kts` — [REVIEW] — `spring-boot-data-mongodb` + `implementation(dataContract)` — соответствует конвенции

#### user-note/data-r2dbc/ (17 файлов)

- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/adapter/UserNoteAddR2dbcAdapter.java` — [REVIEW] — `repository.save(mapper.toNewEntity(request)).map(mapper::toResponse)` — тот же inline-паттерн, что и в JPA/`note`/`user` для этой технологии (там он не расходится — только у Mongo/MongoReactive расхождение, см. выше) — соответствует конвенции
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/adapter/UserNoteExistsByUserIdAndNoteIdR2dbcAdapter.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/adapter/UserNoteFindByIdR2dbcAdapter.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/adapter/UserNoteFindByNoteIdR2dbcAdapter.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/adapter/UserNoteFindByUserIdAndNoteIdR2dbcAdapter.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/adapter/UserNoteFindByUserIdR2dbcAdapter.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/adapter/UserNoteRemoveR2dbcAdapter.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/adapter/UserNoteReplaceR2dbcAdapter.java` — [REVIEW] — `switchIfEmpty(Mono.error(NotFound))` + `Objects.requireNonNull(existing.getId())` — та же структура, что и в MongoReactive-варианте
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/adapter/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/mapper/UserNoteR2dbcMapper.java` — [REVIEW] — `UserNoteRole.valueOf(entity.getRole())` / `request.role().name()` — ручная конвертация enum↔String, согласуется с открытым вопросом «role как String в R2DBC/JDBC»
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/mapper/UserNoteR2dbcMapperContract.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/mapper/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/model/UserNoteR2dbcEntity.java` — [REVIEW] — `{Entity}{Tech}Entity`, `@Column("role") private String role` — соответствует принятому решению (R2DBC без нативного enum); **нет unique-constraint на `user_id+note_id`** на уровне аннотаций — ожидаемо и задокументировано (`spring-data-relational` не поддерживает constraint-атрибуты у `@Table`/`@Column`), см. открытый вопрос «Управление схемой для R2DBC/JDBC»
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/model/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/repository/UserNoteR2dbcRepository.java` — [REVIEW] — `ReactiveCrudRepository<UserNoteR2dbcEntity, UUID>` + derived queries — соответствует конвенции
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/repository/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user-note/data-r2dbc/build.gradle.kts` — [REVIEW] — `spring-boot-data-r2dbc` + `implementation(dataContractReactive)` — соответствует конвенции

#### user-note/domain/ (6 файлов)

- `user-note/domain/src/main/java/com/example/usernote/domain/UserNoteNotFoundException.java` — [REVIEW] — Два конструктора (`UUID id` и `UUID userId, UUID noteId`) — используются в webmvc/webflux/всех технологиях единообразно; чистая Java, без зависимостей на инфраструктуру — соответствует hexagonal-изоляции
- `user-note/domain/src/main/java/com/example/usernote/domain/UserNoteRequest.java` — [REVIEW] — `record`, чистая Java — соответствует конвенции
- `user-note/domain/src/main/java/com/example/usernote/domain/UserNoteResponse.java` — [REVIEW] — `record UserNoteResponse(UUID id, UUID userId, UUID noteId, UserNoteRole role)` — `id` выставлен явно (суррогатный ключ), соответствует принятому решению
- `user-note/domain/src/main/java/com/example/usernote/domain/UserNoteRole.java` — [REVIEW] — `enum UserNoteRole { OWNER, EDITOR, VIEWER }` — соответствует конвенции
- `user-note/domain/src/main/java/com/example/usernote/domain/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user-note/domain/build.gradle.kts` — [REVIEW] — `id("com.example.base")` — плоский Java-модуль, без Spring — соответствует конвенции

#### user-note/webflux/ (10 файлов)

- `user-note/webflux/src/main/java/com/example/usernote/webflux/UserNoteCreateController.java` — [REVIEW] — `POST /user-notes`, `Mono<ResponseEntity<UserNoteResponse>>`, `HttpStatus.CREATED` — один контроллер на операцию, соответствует конвенции
- `user-note/webflux/src/main/java/com/example/usernote/webflux/UserNoteDeleteController.java` — [REVIEW] — `DELETE /user-notes/{userId}/{noteId}`, проверка `existsByUserIdAndNoteId` → `Mono.error(NotFound)`/`remove` — соответствует конвенции
- `user-note/webflux/src/main/java/com/example/usernote/webflux/UserNoteExceptionHandler.java` — [REVIEW] — `@RestControllerAdvice` без наследования, `ProblemDetail` + `setTitle(...)` — идентично паттерну `NoteExceptionHandler`/`UserExceptionHandler` в webflux `note/`/`user/` (расхождение webmvc/webflux по стилю — общее для всех трёх сервисов, не находка user-note)
- `user-note/webflux/src/main/java/com/example/usernote/webflux/UserNoteFindByIdController.java` — [REVIEW] — `GET /user-notes/{id}`, `switchIfEmpty(Mono.error(NotFound))` — соответствует конвенции
- `user-note/webflux/src/main/java/com/example/usernote/webflux/UserNoteFindByNoteIdController.java` — [REVIEW] — `GET /user-notes/note/{noteId}`, `Flux<UserNoteResponse>` без обёртки в `ResponseEntity` — соответствует конвенции коллекционных эндпоинтов
- `user-note/webflux/src/main/java/com/example/usernote/webflux/UserNoteFindByUserIdAndNoteIdController.java` — [REVIEW] — `GET /user-notes/{userId}/{noteId}` — соответствует конвенции
- `user-note/webflux/src/main/java/com/example/usernote/webflux/UserNoteFindByUserIdController.java` — [REVIEW] — `GET /user-notes/user/{userId}`, `Flux<UserNoteResponse>` — соответствует конвенции
- `user-note/webflux/src/main/java/com/example/usernote/webflux/UserNoteUpdateController.java` — [REVIEW] — `PUT /user-notes/{userId}/{noteId}`, проверка existence → `Mono.error(NotFound)`/`replace` — структура идентична `NoteUpdateController` (после пересмотра CRUD-сервисов 2026-07-07)
- `user-note/webflux/src/main/java/com/example/usernote/webflux/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user-note/webflux/build.gradle.kts` — [REVIEW] — `spring-boot-webflux` + `implementation(dataContractReactive)` — соответствует конвенции

#### user-note/webmvc/ (10 файлов)

- `user-note/webmvc/src/main/java/com/example/usernote/webmvc/UserNoteCreateController.java` — [REVIEW] — `POST /user-notes`, `ResponseEntity<UserNoteResponse>`, `HttpStatus.CREATED` — соответствует конвенции
- `user-note/webmvc/src/main/java/com/example/usernote/webmvc/UserNoteDeleteController.java` — [REVIEW] — `if (!exists) throw NotFound; remove(...)` — соответствует конвенции
- `user-note/webmvc/src/main/java/com/example/usernote/webmvc/UserNoteExceptionHandler.java` — [REVIEW] — `@ControllerAdvice extends ResponseEntityExceptionHandler`, без `setTitle` — идентично `NoteExceptionHandler`/`UserExceptionHandler` в webmvc `note/`/`user/`; не находка user-note
- `user-note/webmvc/src/main/java/com/example/usernote/webmvc/UserNoteFindByIdController.java` — [REVIEW] — `orElseThrow(() -> new UserNoteNotFoundException(id))` — соответствует конвенции
- `user-note/webmvc/src/main/java/com/example/usernote/webmvc/UserNoteFindByNoteIdController.java` — [REVIEW] — `ResponseEntity<List<UserNoteResponse>>` — соответствует конвенции
- `user-note/webmvc/src/main/java/com/example/usernote/webmvc/UserNoteFindByUserIdAndNoteIdController.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/webmvc/src/main/java/com/example/usernote/webmvc/UserNoteFindByUserIdController.java` — [REVIEW] — Соответствует конвенции, замечаний нет
- `user-note/webmvc/src/main/java/com/example/usernote/webmvc/UserNoteUpdateController.java` — [REVIEW] — `if (!exists) throw NotFound; replace(...)` — структура идентична `NoteUpdateController`
- `user-note/webmvc/src/main/java/com/example/usernote/webmvc/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user-note/webmvc/build.gradle.kts` — [REVIEW] — `spring-boot-webmvc` + `implementation(dataContract)` — соответствует конвенции

#### user-note/ — предлагаемые отсутствующие файлы (`[ADD]`, 30)

- `user-note/domain/src/test/java/com/example/usernote/domain/UserNoteNotFoundExceptionTest.java` — [ADD] — Unit-тест на оба конструктора исключения (по `id` и по `userId+noteId`) и текст сообщения
- `user-note/data-jpa/src/test/java/com/example/usernote/data/jpa/mapper/UserNoteJpaMapperTest.java` — [ADD] — Unit-тест ручного маппинга `toNewEntity`/`toExistingEntity`/`toResponse`
- `user-note/data-jpa/src/test/java/com/example/usernote/data/jpa/adapter/UserNoteJpaAdapterIT.java` — [ADD] — `@DataJpaTest`/testcontainers-тест на все 8 адаптеров JPA (add/exists/find*/remove/replace), включая проверку unique constraint `user_id+note_id` и `UserNoteNotFoundException` в `replace`
- `user-note/data-mongodb/src/test/java/com/example/usernote/data/mongodb/mapper/UserNoteMongoMapperTest.java` — [ADD] — Unit-тест маппера
- `user-note/data-mongodb/src/test/java/com/example/usernote/data/mongodb/adapter/UserNoteMongoAdapterIT.java` — [ADD] — Testcontainers/embedded-Mongo тест на все 8 адаптеров, включая `@CompoundIndex(unique = true)`
- `user-note/data-mongodb-reactive/src/test/java/com/example/usernote/data/mongodb/reactive/mapper/UserNoteMongoReactiveMapperTest.java` — [ADD] — Unit-тест маппера
- `user-note/data-mongodb-reactive/src/test/java/com/example/usernote/data/mongodb/reactive/adapter/UserNoteMongoReactiveAdapterIT.java` — [ADD] — Testcontainers-тест с `StepVerifier` на все 8 reactive-адаптеров
- `user-note/data-r2dbc/src/test/java/com/example/usernote/data/r2dbc/mapper/UserNoteR2dbcMapperTest.java` — [ADD] — Unit-тест конвертации `role` enum↔String
- `user-note/data-r2dbc/src/test/java/com/example/usernote/data/r2dbc/adapter/UserNoteR2dbcAdapterIT.java` — [ADD] — Testcontainers-тест на все 8 адаптеров; заблокирован открытым вопросом «Управление схемой для R2DBC/JDBC» — нужен `schema.sql` (см. ниже), иначе таблицы неоткуда взять
- `user-note/data-r2dbc/src/main/resources/schema.sql` (или `application/src/main/resources/db/migration/...` при выборе Flyway/Liquibase) — [ADD] — Реализация открытого вопроса «Управление схемой для R2DBC/JDBC»: таблица `user_notes` + unique constraint `user_id, note_id`, которого сейчас нет ни в одной технологии, кроме JPA/MongoDB
- `user-note/data-jdbc/src/test/java/com/example/usernote/data/jdbc/mapper/UserNoteJdbcMapperTest.java` — [ADD] — Unit-тест `fromRow(ResultSet, int)` (мок `ResultSet`)
- `user-note/data-jdbc/src/test/java/com/example/usernote/data/jdbc/adapter/UserNoteJdbcAdapterIT.java` — [ADD] — Testcontainers-тест на все 8 адаптеров; тот же блокер по схеме, что и у R2DBC
- `user-note/data-jdbc/src/main/resources/schema.sql` (или общий с R2DBC) — [ADD] — Аналогично — для сырого SQL в `data-jdbc/`
- `user-note/webmvc/UserNoteCreateControllerTest.java` — [ADD] — `@WebMvcTest` + `@MockitoBean` на контракт — по одному классу на контроллер (8 контроллеров всего в webmvc)
- `user-note/webmvc/UserNoteDeleteControllerTest.java` — [ADD] — Включая кейс 404 при `!exists`
- `user-note/webmvc/UserNoteFindByIdControllerTest.java` — [ADD] — Включая кейс 404 через `UserNoteExceptionHandler`
- `user-note/webmvc/UserNoteFindByNoteIdControllerTest.java` — [ADD] — —
- `user-note/webmvc/UserNoteFindByUserIdAndNoteIdControllerTest.java` — [ADD] — —
- `user-note/webmvc/UserNoteFindByUserIdControllerTest.java` — [ADD] — —
- `user-note/webmvc/UserNoteUpdateControllerTest.java` — [ADD] — Включая кейс 404 при `!exists`
- `user-note/webmvc/UserNoteExceptionHandlerTest.java` — [ADD] — Проверка `ProblemDetail` (статус, detail) для `UserNoteNotFoundException`
- `user-note/webflux/UserNoteCreateControllerTest.java` — [ADD] — `@WebFluxTest` + `WebTestClient` — по одному классу на контроллер (8 контроллеров в webflux)
- `user-note/webflux/UserNoteDeleteControllerTest.java` — [ADD] — —
- `user-note/webflux/UserNoteFindByIdControllerTest.java` — [ADD] — —
- `user-note/webflux/UserNoteFindByNoteIdControllerTest.java` — [ADD] — —
- `user-note/webflux/UserNoteFindByUserIdAndNoteIdControllerTest.java` — [ADD] — —
- `user-note/webflux/UserNoteFindByUserIdControllerTest.java` — [ADD] — —
- `user-note/webflux/UserNoteUpdateControllerTest.java` — [ADD] — —
- `user-note/webflux/UserNoteExceptionHandlerTest.java` — [ADD] — Проверка `ProblemDetail` + `setTitle("UserNote Not Found")`
- `user-note/application/src/test/java/com/example/usernote/UserNoteEndToEndIT.java` — [ADD] — Сквозной `@SpringBootTest` + `MockMvc`/testcontainers-БД, проверяющий реальную цепочку webmvc→data-jpa (единственная подключённая в `application/` связка)

### user/ (128 файлов + 15 предложенных)

#### Ключевые находки

1. Все 128 файлов соответствуют паттернам раздела «Именование» из CLAUDE.md без единого нарушения — `Contract`/`ContractReactive`/`{Tech}Adapter`/`{Tech}MapperContract`/`{Tech}Mapper`/ `{Tech}Repository`/`Entity`/`{Tech}Entity`/`Document`/`ReactiveDocument` выдержаны единообразно по всем 5 driven-технологиям и обоим driving-адаптерам (webmvc/webflux).
2. Подтверждено дословно: `findByEmail`/`findByUsername` реализованы в обоих контрактах (`data-contract`, `data-contract-reactive`) и во всех 5 driven-адаптерах (jpa/mongodb/ mongodb-reactive/r2dbc/jdbc), но не выведены НИ в `webmvc`, НИ в `webflux` — ни одного HTTP-эндпоинта для поиска по email/username не существует нигде в сервисе.
3. В сервисе нет ни одного unit/integration-теста, кроме тривиального `UserApplicationTests.contextLoads()` — ни `domain/`, ни один driven-адаптер/mapper, ни один controller не покрыты тестами; `application/src/test/resources/application.properties` — пустой файл (0 байт).
4. `application/` подключает единственную комбинацию technology — `webmvc` + `data-jpa` (+ `spring-boot-h2-database`) — остальные 8 driven/driving модулей компилируются, но не запускаются ни в одной сборке (открытое решение «Комбинации technology в `application/`»).
5. Unique constraint на `username`/`email` реализован нативно в JPA (`@Column(unique=true)`) и MongoDB/MongoDB reactive (`@Indexed(unique=true)`), но полностью отсутствует в R2DBC/JDBC — ни схемы, ни constraint-аннотаций нет вообще (открытое решение «Управление схемой для R2DBC/JDBC»). `domain/UserRequest`/`UserResponse` не содержат поле пароля — ожидаемо, т. к. `auth/` пока скелет (см. открытое решение «Регистрация auth/ ↔ user/»).

Статус по умолчанию для каждого файла — `[REVIEW]` (правило «Пересмотр решений»: ничего не считается окончательно принятым при первом просмотре).

#### user/application/ (6 файлов)

- `user/application/src/main/java/com/example/user/UserApplication.java` — [REVIEW] — Стандартный `@SpringBootApplication`, соответствует конвенции, замечаний нет
- `user/application/src/main/java/com/example/user/package-info.java` — [REVIEW] — `@NullMarked` присутствует, соответствует конвенции
- `user/application/build.gradle.kts` — [REVIEW] — Единственная technology-комбинация `webmvc`+`data-jpa`+`spring-boot-h2-database` — соответствует открытому решению «Комбинации technology в `application/`», замечаний по синтаксису нет
- `user/application/src/main/resources/application.properties` — [REVIEW] — `spring.application.name`+`spring.mvc.problemdetails.enabled=true`, соответствует принятому решению «ProblemDetail»
- `user/application/src/test/java/com/example/user/UserApplicationTests.java` — [REVIEW] — Единственный тест во всём сервисе — тривиальный `contextLoads()`; нет ни одного другого теста ни в одном модуле `user/`
- `user/application/src/test/resources/application.properties` — [REVIEW] — Файл существует, но пуст (0 байт) — не переопределяет ничего для тестового профиля

#### user/data-contract/ (10 файлов)

- `user/data-contract/src/main/java/com/example/user/contract/UserAddContract.java` — [REVIEW] — `{Entity}{Op}Contract`, соответствует конвенции
- `user/data-contract/src/main/java/com/example/user/contract/UserExistsByIdContract.java` — [REVIEW] — `boolean existsById(UUID)` — соответствует принятому решению «`existsById` в контракте — валидный паттерн»
- `user/data-contract/src/main/java/com/example/user/contract/UserFindAllContract.java` — [REVIEW] — Возвращает `List<UserResponse>`, соответствует конвенции
- `user/data-contract/src/main/java/com/example/user/contract/UserFindByEmailContract.java` — [REVIEW] — `Optional<UserResponse> findByEmail(String)` реализован, но не имеет HTTP-входа ни в `webmvc`, ни в `webflux` — см. открытое решение «`findByEmail`/`findByUsername` без HTTP-входа»
- `user/data-contract/src/main/java/com/example/user/contract/UserFindByIdContract.java` — [REVIEW] — `Optional<UserResponse>`, соответствует reactive/sync-семантике из принятых решений
- `user/data-contract/src/main/java/com/example/user/contract/UserFindByUsernameContract.java` — [REVIEW] — Реализован, но без HTTP-входа — тот же открытый вопрос, что и `UserFindByEmailContract`
- `user/data-contract/src/main/java/com/example/user/contract/UserRemoveContract.java` — [REVIEW] — `void remove(UUID)`, соответствует sync-семантике
- `user/data-contract/src/main/java/com/example/user/contract/UserReplaceContract.java` — [REVIEW] — `UserResponse replace(UUID, UserRequest)` — DTO-возврат, соответствует текущему (открытому) решению «Возврат мутирующего use case»
- `user/data-contract/src/main/java/com/example/user/contract/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user/data-contract/build.gradle.kts` — [REVIEW] — `id("com.example.library")` + `api(projects.user.domain)`, соответствует принятому решению об `api` только когда тип в публичной сигнатуре

#### user/data-contract-reactive/ (10 файлов)

- `user/data-contract-reactive/src/main/java/com/example/user/contract/reactive/UserAddContractReactive.java` — [REVIEW] — `Mono<UserResponse>`, соответствует reactive-семантике
- `user/data-contract-reactive/src/main/java/com/example/user/contract/reactive/UserExistsByIdContractReactive.java` — [REVIEW] — `Mono<Boolean>`, соответствует принятой reactive-семантике `existsById`
- `user/data-contract-reactive/src/main/java/com/example/user/contract/reactive/UserFindAllContractReactive.java` — [REVIEW] — `Flux<UserResponse>`, соответствует конвенции
- `user/data-contract-reactive/src/main/java/com/example/user/contract/reactive/UserFindByEmailContractReactive.java` — [REVIEW] — `Mono<UserResponse>` (не `Optional`) — корректно для reactive; реализован во всех reactive-адаптерах, но без HTTP-входа в `webflux` — тот же открытый вопрос
- `user/data-contract-reactive/src/main/java/com/example/user/contract/reactive/UserFindByIdContractReactive.java` — [REVIEW] — `Mono<UserResponse>`, пустой `Mono` вместо `Optional.empty()` — соответствует принятому решению
- `user/data-contract-reactive/src/main/java/com/example/user/contract/reactive/UserFindByUsernameContractReactive.java` — [REVIEW] — Реализован, но без HTTP-входа в `webflux` — тот же открытый вопрос
- `user/data-contract-reactive/src/main/java/com/example/user/contract/reactive/UserRemoveContractReactive.java` — [REVIEW] — `Mono<Void>`, соответствует reactive-семантике
- `user/data-contract-reactive/src/main/java/com/example/user/contract/reactive/UserReplaceContractReactive.java` — [REVIEW] — `Mono<UserResponse>`, соответствует конвенции
- `user/data-contract-reactive/src/main/java/com/example/user/contract/reactive/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user/data-contract-reactive/build.gradle.kts` — [REVIEW] — `id("com.example.reactor")` + `api(projects.user.domain)`, соответствует конвенции

#### user/data-jdbc/ (13 файлов)

- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/adapter/UserAddJdbcAdapter.java` — [REVIEW] — `NamedParameterJdbcTemplate` + сырой SQL, ID генерируется в адаптере (`UUID.randomUUID()`) — соответствует намеренному отсутствию `model/`/`repository/` в `data-jdbc/`
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/adapter/UserExistsByIdJdbcAdapter.java` — [REVIEW] — `SELECT COUNT(*)`, соответствует конвенции
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/adapter/UserFindAllJdbcAdapter.java` — [REVIEW] — Использует `UserJdbcMapperContract::fromRow` как `RowMapper`, соответствует конвенции
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/adapter/UserFindByEmailJdbcAdapter.java` — [REVIEW] — `.stream().findFirst()` вместо `queryForObject` — корректно возвращает `Optional`, реализован, но без HTTP-входа
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/adapter/UserFindByIdJdbcAdapter.java` — [REVIEW] — Тот же паттерн `.stream().findFirst()`, соответствует конвенции
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/adapter/UserFindByUsernameJdbcAdapter.java` — [REVIEW] — Тот же паттерн, реализован, но без HTTP-входа
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/adapter/UserRemoveJdbcAdapter.java` — [REVIEW] — `DELETE FROM users WHERE id = :id`, соответствует конвенции
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/adapter/UserReplaceJdbcAdapter.java` — [REVIEW] — Блайндовый `UPDATE` без проверки affected rows — не расхождение с другими технологиями (те тоже не проверяют на уровне адаптера: existence-check делает вызывающий `webmvc`/`webflux` контроллер), но стоит иметь в виду при добавлении unit-тестов на адаптер
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/adapter/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/mapper/UserJdbcMapper.java` — [REVIEW] — `RowMapper`-совместимая сигнатура `fromRow(ResultSet, int)`, соответствует конвенции
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/mapper/UserJdbcMapperContract.java` — [REVIEW] — `{Entity}{Tech}MapperContract`, соответствует конвенции
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/mapper/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user/data-jdbc/build.gradle.kts` — [REVIEW] — `id("com.example.spring-boot-data-jdbc")` + `implementation(projects.user.dataContract)`, соответствует конвенции

#### user/data-jpa/ (17 файлов)

- `user/data-jpa/src/main/java/com/example/user/data/jpa/adapter/UserAddJpaAdapter.java` — [REVIEW] — `save(toNewEntity(...))`, соответствует принятому решению `add` через `save(null id)`
- `user/data-jpa/src/main/java/com/example/user/data/jpa/adapter/UserExistsByIdJpaAdapter.java` — [REVIEW] — Делегирует `JpaRepository.existsById`, соответствует конвенции
- `user/data-jpa/src/main/java/com/example/user/data/jpa/adapter/UserFindAllJpaAdapter.java` — [REVIEW] — `findAll().stream().map(...).toList()`, соответствует конвенции
- `user/data-jpa/src/main/java/com/example/user/data/jpa/adapter/UserFindByEmailJpaAdapter.java` — [REVIEW] — Делегирует кастомному `UserJpaRepository.findByEmail`, реализован, но без HTTP-входа
- `user/data-jpa/src/main/java/com/example/user/data/jpa/adapter/UserFindByIdJpaAdapter.java` — [REVIEW] — Соответствует конвенции
- `user/data-jpa/src/main/java/com/example/user/data/jpa/adapter/UserFindByUsernameJpaAdapter.java` — [REVIEW] — Делегирует кастомному `UserJpaRepository.findByUsername`, реализован, но без HTTP-входа
- `user/data-jpa/src/main/java/com/example/user/data/jpa/adapter/UserRemoveJpaAdapter.java` — [REVIEW] — `deleteById`, соответствует конвенции
- `user/data-jpa/src/main/java/com/example/user/data/jpa/adapter/UserReplaceJpaAdapter.java` — [REVIEW] — `save(toExistingEntity(id, ...))`, соответствует принятому решению `replace` через `save(id)`
- `user/data-jpa/src/main/java/com/example/user/data/jpa/adapter/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user/data-jpa/src/main/java/com/example/user/data/jpa/mapper/UserJpaMapper.java` — [REVIEW] — Ручной маппинг, `Objects.requireNonNull(entity.getId())` в `toResponse` — корректная обработка `@Nullable UUID id`, соответствует конвенции
- `user/data-jpa/src/main/java/com/example/user/data/jpa/mapper/UserJpaMapperContract.java` — [REVIEW] — Соответствует конвенции
- `user/data-jpa/src/main/java/com/example/user/data/jpa/mapper/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user/data-jpa/src/main/java/com/example/user/data/jpa/model/UserEntity.java` — [REVIEW] — `{Entity}Entity`, `@Id @GeneratedValue(UUID)`, `@Column(unique=true)` на `username`/`email` — unique constraint реально создаётся (в отличие от R2DBC/JDBC), `@SuppressWarnings("NullAway.Init")` на protected-конструкторе — соответствует конвенции
- `user/data-jpa/src/main/java/com/example/user/data/jpa/model/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user/data-jpa/src/main/java/com/example/user/data/jpa/repository/UserJpaRepository.java` — [REVIEW] — `JpaRepository<UserEntity, UUID>` + кастомные `findByUsername`/`findByEmail` — максимально использует Spring Data, соответствует памяти пользователя
- `user/data-jpa/src/main/java/com/example/user/data/jpa/repository/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user/data-jpa/build.gradle.kts` — [REVIEW] — `id("com.example.spring-boot-data-jpa")`, соответствует конвенции

#### user/data-mongodb/ (17 файлов)

- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/adapter/UserAddMongoAdapter.java` — [REVIEW] — `MongoTemplate.insert(...)`, соответствует принятому решению `add` через `insert()`
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/adapter/UserExistsByIdMongoAdapter.java` — [REVIEW] — Делегирует `MongoRepository.existsById`, соответствует конвенции
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/adapter/UserFindAllMongoAdapter.java` — [REVIEW] — Соответствует конвенции
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/adapter/UserFindByEmailMongoAdapter.java` — [REVIEW] — Реализован, но без HTTP-входа
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/adapter/UserFindByIdMongoAdapter.java` — [REVIEW] — Соответствует конвенции
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/adapter/UserFindByUsernameMongoAdapter.java` — [REVIEW] — Реализован, но без HTTP-входа
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/adapter/UserRemoveMongoAdapter.java` — [REVIEW] — `deleteById`, соответствует конвенции
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/adapter/UserReplaceMongoAdapter.java` — [REVIEW] — `MongoTemplate.save(...)`, соответствует принятому решению `replace` через `save()`
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/adapter/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/mapper/UserMongoMapper.java` — [REVIEW] — ID генерируется мэппером (`UUID.randomUUID()`) для нового документа, соответствует конвенции
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/mapper/UserMongoMapperContract.java` — [REVIEW] — Соответствует конвенции
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/mapper/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/model/UserDocument.java` — [REVIEW] — `{Entity}Document`, `@Indexed(unique=true)` на `username`/`email` — unique constraint создаётся, `id` не `@Nullable` (в отличие от JPA/R2DBC) — корректно, т. к. ID присваивается вручную в мэппере ещё до вставки, не генерируется БД
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/model/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/repository/UserMongoRepository.java` — [REVIEW] — `MongoRepository<UserDocument, UUID>` + кастомные `findByUsername`/`findByEmail`, соответствует конвенции
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/repository/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user/data-mongodb/build.gradle.kts` — [REVIEW] — `id("com.example.spring-boot-data-mongodb")`, соответствует конвенции

#### user/data-mongodb-reactive/ (17 файлов)

- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/adapter/UserAddMongoReactiveAdapter.java` — [REVIEW] — `repository.insert(...)`, соответствует конвенции
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/adapter/UserExistsByIdMongoReactiveAdapter.java` — [REVIEW] — `Mono<Boolean>`, соответствует конвенции
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/adapter/UserFindAllMongoReactiveAdapter.java` — [REVIEW] — `Flux<UserResponse>`, соответствует конвенции
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/adapter/UserFindByEmailMongoReactiveAdapter.java` — [REVIEW] — Реализован, но без HTTP-входа
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/adapter/UserFindByIdMongoReactiveAdapter.java` — [REVIEW] — Соответствует конвенции
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/adapter/UserFindByUsernameMongoReactiveAdapter.java` — [REVIEW] — Строка возврата — 119 символов (лимит 120), укладывается впритык; реализован, но без HTTP-входа
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/adapter/UserRemoveMongoReactiveAdapter.java` — [REVIEW] — `Mono<Void>` через `deleteById`, соответствует конвенции
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/adapter/UserReplaceMongoReactiveAdapter.java` — [REVIEW] — `repository.save(...)`, соответствует конвенции
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/adapter/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/mapper/UserMongoReactiveMapper.java` — [REVIEW] — Соответствует конвенции, идентичен sync-версии по структуре
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/mapper/UserMongoReactiveMapperContract.java` — [REVIEW] — Соответствует конвенции
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/mapper/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/model/UserReactiveDocument.java` — [REVIEW] — `{Entity}ReactiveDocument`, `@Indexed(unique=true)` на `username`/`email`, соответствует конвенции
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/model/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/repository/UserMongoReactiveRepository.java` — [REVIEW] — `ReactiveMongoRepository<UserReactiveDocument, UUID>`, соответствует конвенции
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/repository/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user/data-mongodb-reactive/build.gradle.kts` — [REVIEW] — `id("com.example.spring-boot-data-mongodb-reactive")`, соответствует конвенции

#### user/data-r2dbc/ (17 файлов)

- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/adapter/UserAddR2dbcAdapter.java` — [REVIEW] — `repository.save(toNewEntity(...))`, соответствует конвенции
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/adapter/UserExistsByIdR2dbcAdapter.java` — [REVIEW] — `Mono<Boolean>`, соответствует конвенции
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/adapter/UserFindAllR2dbcAdapter.java` — [REVIEW] — `Flux<UserResponse>`, соответствует конвенции
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/adapter/UserFindByEmailR2dbcAdapter.java` — [REVIEW] — Реализован, но без HTTP-входа
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/adapter/UserFindByIdR2dbcAdapter.java` — [REVIEW] — Соответствует конвенции
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/adapter/UserFindByUsernameR2dbcAdapter.java` — [REVIEW] — Реализован, но без HTTP-входа
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/adapter/UserRemoveR2dbcAdapter.java` — [REVIEW] — `Mono<Void>` через `deleteById`, соответствует конвенции
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/adapter/UserReplaceR2dbcAdapter.java` — [REVIEW] — `repository.save(toExistingEntity(id, ...))`, соответствует конвенции
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/adapter/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/mapper/UserR2dbcMapper.java` — [REVIEW] — `Objects.requireNonNull(entity.getId())` в `toResponse`, идентичен по структуре JPA-мэпперу — соответствует конвенции
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/mapper/UserR2dbcMapperContract.java` — [REVIEW] — Соответствует конвенции
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/mapper/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/model/UserR2dbcEntity.java` — [REVIEW] — `{Entity}{Tech}Entity`, `@Id private @Nullable UUID id`, но у `@Column("username")`/`@Column("email")` НЕТ атрибутов unique/constraints (у `spring-data-relational` их вообще нет) — unique constraint не создаётся нигде, соответствует открытому решению «Управление схемой для R2DBC/JDBC»
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/model/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/repository/UserR2dbcRepository.java` — [REVIEW] — `ReactiveCrudRepository<UserR2dbcEntity, UUID>` + кастомные `findByUsername`/`findByEmail`, соответствует конвенции
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/repository/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user/data-r2dbc/build.gradle.kts` — [REVIEW] — `id("com.example.spring-boot-data-r2dbc")`, соответствует конвенции

#### user/domain/ (5 файлов)

- `user/domain/src/main/java/com/example/user/domain/UserNotFoundException.java` — [REVIEW] — Два конструктора (`UUID`/`String`) — используется и для поиска по id, и потенциально по email/username; доменное исключение, без зависимостей на инфраструктуру, соответствует конвенции
- `user/domain/src/main/java/com/example/user/domain/UserRequest.java` — [REVIEW] — `record(String username, String email)` — нет поля пароля/credentials; ожидаемо на текущем этапе, т. к. `auth/` — скелет без логики (открытое решение «Регистрация auth/ ↔ user/»)
- `user/domain/src/main/java/com/example/user/domain/UserResponse.java` — [REVIEW] — `record(UUID id, String username, String email)`, соответствует конвенции
- `user/domain/src/main/java/com/example/user/domain/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user/domain/build.gradle.kts` — [REVIEW] — `id("com.example.base")`, соответствует конвенции чистого Java-модуля без инфраструктурных зависимостей

#### user/webflux/ (8 файлов)

- `user/webflux/src/main/java/com/example/user/webflux/UserCreateController.java` — [REVIEW] — `Mono<ResponseEntity<UserResponse>>`, один контроллер на операцию, соответствует конвенции
- `user/webflux/src/main/java/com/example/user/webflux/UserDeleteController.java` — [REVIEW] — `existsById` → `flatMap` → `remove`/`Mono.error(UserNotFoundException)`, соответствует конвенции
- `user/webflux/src/main/java/com/example/user/webflux/UserExceptionHandler.java` — [REVIEW] — `@RestControllerAdvice` без наследования (не `ResponseEntityExceptionHandler`, в отличие от `webmvc`-версии) — асимметрия оправдана: `ResponseEntityExceptionHandler` — MVC-специфичный класс, для WebFlux нет прямого аналога; уже выравнивалось при пересмотре CRUD-сервисов 2026-07-07
- `user/webflux/src/main/java/com/example/user/webflux/UserFindAllController.java` — [REVIEW] — `Flux<UserResponse>` напрямую без `ResponseEntity`-обёртки (в отличие от `UserFindByIdController`), соответствует существующему паттерну `note/`
- `user/webflux/src/main/java/com/example/user/webflux/UserFindByIdController.java` — [REVIEW] — `switchIfEmpty(Mono.error(...))` вместо `Optional.orElseThrow`, корректная reactive-семантика
- `user/webflux/src/main/java/com/example/user/webflux/UserUpdateController.java` — [REVIEW] — `existsById` → `flatMap` → `replace`/`Mono.error`, соответствует конвенции; нет отдельных контроллеров для `findByEmail`/`findByUsername` — подтверждает открытый вопрос
- `user/webflux/src/main/java/com/example/user/webflux/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user/webflux/build.gradle.kts` — [REVIEW] — `id("com.example.spring-boot-webflux")` + `implementation(projects.user.dataContractReactive)`, соответствует конвенции

#### user/webmvc/ (8 файлов)

- `user/webmvc/src/main/java/com/example/user/webmvc/UserCreateController.java` — [REVIEW] — `ResponseEntity<UserResponse>`, `HttpStatus.CREATED`, соответствует конвенции
- `user/webmvc/src/main/java/com/example/user/webmvc/UserDeleteController.java` — [REVIEW] — `existsById`-проверка перед `remove`, throw `UserNotFoundException`, соответствует конвенции
- `user/webmvc/src/main/java/com/example/user/webmvc/UserExceptionHandler.java` — [REVIEW] — `@ControllerAdvice extends ResponseEntityExceptionHandler`, соответствует конвенции `webmvc` (см. комментарий к reactive-версии по асимметрии)
- `user/webmvc/src/main/java/com/example/user/webmvc/UserFindAllController.java` — [REVIEW] — `ResponseEntity<List<UserResponse>>`, соответствует конвенции
- `user/webmvc/src/main/java/com/example/user/webmvc/UserFindByIdController.java` — [REVIEW] — `Optional.orElseThrow(UserNotFoundException::new)`, соответствует конвенции
- `user/webmvc/src/main/java/com/example/user/webmvc/UserUpdateController.java` — [REVIEW] — `existsById`-проверка перед `replace`, соответствует конвенции; нет отдельных контроллеров для `findByEmail`/`findByUsername` — подтверждает открытый вопрос
- `user/webmvc/src/main/java/com/example/user/webmvc/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `user/webmvc/build.gradle.kts` — [REVIEW] — `id("com.example.spring-boot-webmvc")` + `implementation(projects.user.dataContract)`, соответствует конвенции

#### user/ — предлагаемые отсутствующие файлы (`[ADD]`, 15)

- `user/domain/src/test/java/com/example/user/domain/UserNotFoundExceptionTest.java` — [ADD] — Нет ни одного unit-теста на domain-слой во всём сервисе
- `user/data-jpa/src/test/java/com/example/user/data/jpa/adapter/UserJpaAdapterTest.java` — [ADD] — Нет integration-тестов адаптеров/mapper'а JPA (например, через `@DataJpaTest` или Testcontainers)
- `user/data-mongodb/src/test/java/com/example/user/data/mongodb/adapter/UserMongoAdapterTest.java` — [ADD] — Нет тестов MongoDB-адаптеров (Testcontainers Mongo)
- `user/data-mongodb-reactive/src/test/java/com/example/user/data/mongodb/reactive/adapter/UserMongoReactiveAdapterTest.java` — [ADD] — Нет тестов reactive Mongo-адаптеров (`StepVerifier` + Testcontainers)
- `user/data-jdbc/src/test/java/com/example/user/data/jdbc/adapter/UserJdbcAdapterTest.java` — [ADD] — Нет тестов JDBC-адаптеров; потребует тестовую схему (см. следующую строку)
- `user/data-jdbc/src/main/resources/schema.sql` — [ADD] — Нет схемы БД для JDBC вообще — `data-jdbc` не подключён ни к одному `application/`; нужна, чтобы включить unique constraint `username`/`email` (открытое решение «Управление схемой для R2DBC/JDBC»)
- `user/data-r2dbc/src/test/java/com/example/user/data/r2dbc/adapter/UserR2dbcAdapterTest.java` — [ADD] — Нет тестов R2DBC-адаптеров; потребует тестовую схему
- `user/data-r2dbc/src/main/resources/schema.sql` — [ADD] — Аналогично — `spring-data-relational` не поддерживает unique constraint через аннотации, нужна явная схема
- `user/webmvc/src/main/java/com/example/user/webmvc/UserFindByEmailController.java` — [ADD] — Один из вариантов закрытия открытого вопроса «`findByEmail`/`findByUsername` без HTTP-входа» — добавить контроллеры уже сейчас (альтернатива: оставить как задел под `auth/`, либо убрать как неиспользуемое)
- `user/webmvc/src/main/java/com/example/user/webmvc/UserFindByUsernameController.java` — [ADD] — То же для `findByUsername`, тот же открытый вопрос
- `user/webmvc/src/test/java/com/example/user/webmvc/UserFindByIdControllerTest.java` — [ADD] — Нет `@WebMvcTest`-покрытия ни одного контроллера `webmvc`
- `user/webflux/src/main/java/com/example/user/webflux/UserFindByEmailController.java` — [ADD] — Reactive-аналог, тот же открытый вопрос
- `user/webflux/src/main/java/com/example/user/webflux/UserFindByUsernameController.java` — [ADD] — Reactive-аналог, тот же открытый вопрос
- `user/webflux/src/test/java/com/example/user/webflux/UserFindByIdControllerTest.java` — [ADD] — Нет `@WebFluxTest`-покрытия ни одного контроллера `webflux`
- `user/application-webflux-r2dbc/build.gradle.kts` — [ADD] — Пример одного из вариантов закрытия открытого решения «Комбинации technology в `application/`» — сделать `webflux`+`data-r2dbc` реально запускаемой связкой, а не только компилируемой

### registry/ (6 файлов)

Skeleton-сервис Eureka server. Структурно идентичен `gateway/`/`config/`/`auth/`: convention-плагины + Application-класс + package-info + application.properties (main/test) + тривиальный `contextLoads()`.

#### registry/application/ (6 файлов)

- `registry/application/src/main/java/com/example/registry/RegistryApplication.java` — [REVIEW] — `@EnableEurekaServer` + `@SpringBootApplication`, стандартно
- `registry/application/src/main/java/com/example/registry/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `registry/build.gradle.kts` — [REVIEW] — `spring-cloud-eureka-server` + `spring-boot-actuator` — совпадает со «Скелет»
- `registry/src/main/resources/application.properties` — [REVIEW] — `register-with-eureka=false`, `fetch-registry=false` — сервер не регистрирует сам себя, ожидаемо
- `registry/src/test/java/com/example/registry/RegistryApplicationTests.java` — [REVIEW] — только `contextLoads()`, других тестов нет
- `registry/src/test/resources/application.properties` — [REVIEW] — дублирует eureka-флаги main-конфига под тестовый профиль

### note/ (114 файлов + 27 предложенных)

Каждый файл прочитан целиком и сверен с CLAUDE.md (naming, слои, принятые решения). Статус `[REVIEW]` — по умолчанию для всех строк (в проекте ничего не считается окончательно принятым при первом просмотре, см. правило «Пересмотр решений»); `[DONE]` ставит только человек. `[ADD]` — файлов сейчас нет, предложены для реальной работоспособности сервиса.

#### Главные находки

1. **Тестов нет вообще** — во всех 114 файлах ровно один тест: `NoteApplicationTests.contextLoads()` (application/). Ни одного unit-теста на мэпперы/адаптеры/контроллеры, ни одного integration-теста (testcontainers) ни по одной из 5 driven-технологий, ни slice-тестов (`@WebMvcTest`/`@WebFluxTest`, `@DataJpaTest` и т. п.) — статус ГОТОВО в CLAUDE.md по факту означает «компилируется и стартует», но не «проверено тестами».
2. **`webflux` `NoteFindAllController` не оборачивает ответ в `ResponseEntity`** (возвращает голый `Flux<NoteResponse>`), тогда как `webmvc`-аналог и все остальные 5 контроллеров `webflux` (`create`/`delete`/`findById`/`update`) корректно возвращают `Mono<ResponseEntity<...>>` / `ResponseEntity<...>` — нарушение принятого правила «`ResponseEntity<T>` в контроллерах» (раздел «HTTP / Ошибки»).
3. **`NoteExceptionHandler` в `webflux` и `webmvc` расходятся сильнее, чем предполагает запись в CLAUDE.md** о выравнивании при пересмотре 2026-07-07: `webmvc`-версия наследует `org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler`, `webflux` — нет, хотя в classpath (`spring-webflux-7.0.7.jar`, подтверждено байткодом) есть reactive-аналог `org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler`. Плюс имена методов различаются (`handleNotFound` vs `handleNoteNotFound`), и только `webflux` вызывает `problem.setTitle(...)`.
4. **`data-mongodb` (sync) непоследователен внутри себя и относительно `data-mongodb-reactive`**: `NoteAddMongoAdapter`/`NoteReplaceMongoAdapter` обходят `NoteMongoRepository` и работают напрямую через `MongoTemplate`, тогда как остальные 4 sync-адаптера того же модуля и все 6 адаптеров `data-mongodb-reactive` используют репозиторий. Подтверждено декомпиляцией (`spring-data-mongodb-5.0.5.jar`): `MongoRepository` (sync), как и `ReactiveMongoRepository`, имеет собственный `insert(S)` — технического ограничения для использования репозитория в sync `add`/`replace` нет, это чистая стилевая непоследовательность.
5. **Схема БД для `data-r2dbc`/`data-jdbc` по-прежнему не создаётся нигде** (соответствует открытому решению «Управление схемой для R2DBC/JDBC» в CLAUDE.md) — `note/application/` подключает только `webmvc`+`data-jpa`, эти два модуля даже не скомпонованы ни в один `application/`; `NoteR2dbcEntity` не имеет аналога `@GeneratedValue` (Spring Data R2DBC не поддерживает автогенерацию на уровне аннотаций), значит `INSERT` для новой записи полагается на `DEFAULT`/`GENERATED` в реальной схеме, которой в репозитории нет.

#### note/application/ (6 файлов)

- `note/application/src/main/java/com/example/note/NoteApplication.java` — [REVIEW] — соответствует конвенции, замечаний нет
- `note/application/src/main/java/com/example/note/package-info.java` — [REVIEW] — `@NullMarked`, соответствует
- `note/application/build.gradle.kts` — [REVIEW] — `spring-boot-application` + `spring-boot-h2-database`; implementation на domain/dataContract/webmvc/dataJpa — единственная связка technology в проекте (см. открытое решение «Комбинации technology в application/»)
- `note/application/src/main/resources/application.properties` — [REVIEW] — `spring.application.name` + `spring.mvc.problemdetails.enabled=true` — соответствует принятому решению по `ProblemDetail`; datasource не сконфигурирован явно (полагается на дефолты `spring-boot-h2-database`)
- `note/application/src/test/java/com/example/note/NoteApplicationTests.java` — [REVIEW] — единственный тест во всём сервисе — только `contextLoads()`, см. находку №1
- `note/application/src/test/resources/application.properties` — [REVIEW] — файл пуст (0 байт) — неясно, нужен ли вообще как заглушка

#### note/data-contract/ (8 файлов)

- `note/data-contract/src/main/java/com/example/note/contract/NoteAddContract.java` — [REVIEW] — `{Entity}{Op}Contract`, соответствует конвенции, замечаний нет
- `note/data-contract/src/main/java/com/example/note/contract/NoteExistsByIdContract.java` — [REVIEW] — `boolean existsById(UUID id)` — соответствует принятому решению «existsById в контракте — валидный паттерн»
- `note/data-contract/src/main/java/com/example/note/contract/NoteFindAllContract.java` — [REVIEW] — `List<NoteResponse> findAll()` — соответствует
- `note/data-contract/src/main/java/com/example/note/contract/NoteFindByIdContract.java` — [REVIEW] — `Optional<NoteResponse> findById(UUID id)` — соответствует sync-семантике
- `note/data-contract/src/main/java/com/example/note/contract/NoteRemoveContract.java` — [REVIEW] — `void remove(UUID id)` — соответствует
- `note/data-contract/src/main/java/com/example/note/contract/NoteReplaceContract.java` — [REVIEW] — `NoteResponse replace(UUID id, NoteRequest request)` — соответствует
- `note/data-contract/src/main/java/com/example/note/contract/package-info.java` — [REVIEW] — `@NullMarked`, соответствует
- `note/data-contract/build.gradle.kts` — [REVIEW] — `id("com.example.library")` + `api(projects.note.domain)` — соответствует

#### note/data-contract-reactive/ (8 файлов)

- `note/data-contract-reactive/src/main/java/com/example/note/contract/reactive/NoteAddContractReactive.java` — [REVIEW] — `Mono<NoteResponse> add(...)` — соответствует `{Entity}{Op}ContractReactive`
- `note/data-contract-reactive/src/main/java/com/example/note/contract/reactive/NoteExistsByIdContractReactive.java` — [REVIEW] — `Mono<Boolean> existsById(...)` — точно соответствует reactive-семантике из CLAUDE.md
- `note/data-contract-reactive/src/main/java/com/example/note/contract/reactive/NoteFindAllContractReactive.java` — [REVIEW] — `Flux<NoteResponse> findAll()` — соответствует
- `note/data-contract-reactive/src/main/java/com/example/note/contract/reactive/NoteFindByIdContractReactive.java` — [REVIEW] — `Mono<NoteResponse> findById(...)` (пустой Mono вместо `Optional.empty()`) — соответствует
- `note/data-contract-reactive/src/main/java/com/example/note/contract/reactive/NoteRemoveContractReactive.java` — [REVIEW] — `Mono<Void> remove(...)` — соответствует
- `note/data-contract-reactive/src/main/java/com/example/note/contract/reactive/NoteReplaceContractReactive.java` — [REVIEW] — `Mono<NoteResponse> replace(...)` — соответствует
- `note/data-contract-reactive/src/main/java/com/example/note/contract/reactive/package-info.java` — [REVIEW] — `@NullMarked`, соответствует
- `note/data-contract-reactive/build.gradle.kts` — [REVIEW] — `id("com.example.reactor")` + `api(projects.note.domain)` — соответствует

#### note/data-jdbc/ (11 файлов)

- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/adapter/NoteAddJdbcAdapter.java` — [REVIEW] — ручной `INSERT` через `NamedParameterJdbcTemplate`, `UUID.randomUUID()` вручную — соответствует принятому решению «`data-jdbc/` намеренно без model/repository»; нет `@Transactional` (общий открытый вопрос, не специфично для файла)
- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/adapter/NoteExistsByIdJdbcAdapter.java` — [REVIEW] — `SELECT COUNT(*)`, корректно обрабатывает возможный `null` (`count != null && count > 0`) — соответствует
- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/adapter/NoteFindAllJdbcAdapter.java` — [REVIEW] — `query(..., mapper::fromRow)` — соответствует
- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/adapter/NoteFindByIdJdbcAdapter.java` — [REVIEW] — `query(...).stream().findFirst()` → `Optional<NoteResponse>` — соответствует sync-семантике
- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/adapter/NoteRemoveJdbcAdapter.java` — [REVIEW] — `DELETE`, `void` — соответствует
- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/adapter/NoteReplaceJdbcAdapter.java` — [REVIEW] — **находка**: `UPDATE` не проверяет число затронутых строк — если `id` не существует, метод молча вернёт `NoteResponse` с данными запроса, как будто replace прошёл успешно (0 строк реально обновлено). В отличие от JPA/Mongo/R2dbc (`save()` реально создаёт/апсертит запись), здесь при прямом вызове контракта (не через HTTP, где `webmvc` сам делает `existsById`) поведение расходится с остальными технологиями
- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/adapter/package-info.java` — [REVIEW] — `@NullMarked`, соответствует
- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/mapper/NoteJdbcMapper.java` — [REVIEW] — реализует `RowMapper`-подобный `fromRow(ResultSet, rowNum)` — соответствует `{Entity}{Tech}Mapper`
- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/mapper/NoteJdbcMapperContract.java` — [REVIEW] — сигнатура специфична для JDBC (`fromRow`, а не `toNewX/toExistingX/toResponse` как у остальных технологий) — оправданная адаптация под `RowMapper`, не расхождение
- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/mapper/package-info.java` — [REVIEW] — `@NullMarked`, соответствует
- `note/data-jdbc/build.gradle.kts` — [REVIEW] — `spring-boot-data-jdbc` + `implementation(projects.note.dataContract)` (sync-контракт, верно для JDBC) — соответствует

#### note/data-jpa/ (15 файлов)

- `note/data-jpa/src/main/java/com/example/note/data/jpa/adapter/NoteAddJpaAdapter.java` — [REVIEW] — `save(toNewEntity(...))` — соответствует принятому «add: save(null id)»
- `note/data-jpa/src/main/java/com/example/note/data/jpa/adapter/NoteExistsByIdJpaAdapter.java` — [REVIEW] — делегирует `repository.existsById` — соответствует
- `note/data-jpa/src/main/java/com/example/note/data/jpa/adapter/NoteFindAllJpaAdapter.java` — [REVIEW] — `findAll().stream().map(...)` — соответствует
- `note/data-jpa/src/main/java/com/example/note/data/jpa/adapter/NoteFindByIdJpaAdapter.java` — [REVIEW] — `findById().map(...)` → `Optional<NoteResponse>` — соответствует
- `note/data-jpa/src/main/java/com/example/note/data/jpa/adapter/NoteRemoveJpaAdapter.java` — [REVIEW] — `deleteById`, `void` — соответствует
- `note/data-jpa/src/main/java/com/example/note/data/jpa/adapter/NoteReplaceJpaAdapter.java` — [REVIEW] — `save(toExistingEntity(id, ...))` — соответствует принятому «replace: save(id)»; поведение при отсутствующем `id` не покрыто тестами (нет тестов вообще, см. находку №1), стоит проверить фактическое поведение Hibernate `merge` на detached-сущности с несуществующим id
- `note/data-jpa/src/main/java/com/example/note/data/jpa/adapter/package-info.java` — [REVIEW] — `@NullMarked`, соответствует
- `note/data-jpa/src/main/java/com/example/note/data/jpa/mapper/NoteJpaMapper.java` — [REVIEW] — `toNewEntity`/`toExistingEntity`/`toResponse`, `Objects.requireNonNull(entity.getId())` в `toResponse` — соответствует
- `note/data-jpa/src/main/java/com/example/note/data/jpa/mapper/NoteJpaMapperContract.java` — [REVIEW] — соответствует `{Entity}{Tech}MapperContract`
- `note/data-jpa/src/main/java/com/example/note/data/jpa/mapper/package-info.java` — [REVIEW] — `@NullMarked`, соответствует
- `note/data-jpa/src/main/java/com/example/note/data/jpa/model/NoteEntity.java` — [REVIEW] — `@Entity`/`@Table("notes")`, `@GeneratedValue(UUID)`, `@Nullable UUID id`, `protected` no-arg конструктор + `@SuppressWarnings("NullAway.Init")` — точно соответствует принятым решениям и стилю кода
- `note/data-jpa/src/main/java/com/example/note/data/jpa/model/package-info.java` — [REVIEW] — `@NullMarked`, соответствует
- `note/data-jpa/src/main/java/com/example/note/data/jpa/repository/NoteJpaRepository.java` — [REVIEW] — пустой `extends JpaRepository<NoteEntity, UUID>` — максимально использует Spring Data, соответствует
- `note/data-jpa/src/main/java/com/example/note/data/jpa/repository/package-info.java` — [REVIEW] — `@NullMarked`, соответствует
- `note/data-jpa/build.gradle.kts` — [REVIEW] — `spring-boot-data-jpa` + `implementation(projects.note.dataContract)` — соответствует

#### note/data-mongodb/ (15 файлов)

- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/adapter/NoteAddMongoAdapter.java` — [REVIEW] — **находка**: внедряет `MongoTemplate` и вызывает `insert()` напрямую, минуя `NoteMongoRepository`, хотя `NoteMongoRepository.insert()` доступен (подтверждено декомпиляцией `MongoRepository`) и именно так делает reactive-аналог `NoteAddMongoReactiveAdapter` — расхождение sync/reactive внутри одного сервиса, см. находку №4
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/adapter/NoteExistsByIdMongoAdapter.java` — [REVIEW] — `repository.existsById` — соответствует
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/adapter/NoteFindAllMongoAdapter.java` — [REVIEW] — `repository.findAll().stream().map(...)` — соответствует
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/adapter/NoteFindByIdMongoAdapter.java` — [REVIEW] — `repository.findById().map(...)` — соответствует
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/adapter/NoteRemoveMongoAdapter.java` — [REVIEW] — `repository.deleteById` — соответствует
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/adapter/NoteReplaceMongoAdapter.java` — [REVIEW] — `MongoTemplate.save()` — корректно соответствует «add ≠ replace: insert() vs save()», но опять напрямую через `MongoTemplate`, а не `NoteMongoRepository.save()`, минуя уже используемый в модуле репозиторий (см. находку №4)
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/adapter/package-info.java` — [REVIEW] — `@NullMarked`, соответствует
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/mapper/NoteMongoMapper.java` — [REVIEW] — `toNewDocument` сам генерирует `UUID.randomUUID()` — соответствует (симметрично reactive-версии)
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/mapper/NoteMongoMapperContract.java` — [REVIEW] — соответствует
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/mapper/package-info.java` — [REVIEW] — `@NullMarked`, соответствует
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/model/NoteDocument.java` — [REVIEW] — `@Document(collection="notes")`, `@Id UUID` (не `@Nullable` — id всегда задан мэппером до создания) — соответствует
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/model/package-info.java` — [REVIEW] — `@NullMarked`, соответствует
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/repository/NoteMongoRepository.java` — [REVIEW] — `extends MongoRepository<NoteDocument, UUID>` — соответствует, но фактически недоиспользуется (см. находку выше — `insert`/`save` не вызываются через него для add/replace)
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/repository/package-info.java` — [REVIEW] — `@NullMarked`, соответствует
- `note/data-mongodb/build.gradle.kts` — [REVIEW] — `spring-boot-data-mongodb` + `implementation(projects.note.dataContract)` — соответствует

#### note/data-mongodb-reactive/ (15 файлов)

- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/adapter/NoteAddMongoReactiveAdapter.java` — [REVIEW] — `repository.insert(document)` — соответствует, в отличие от sync-аналога использует репозиторий, а не `MongoTemplate` напрямую (см. находку №4)
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/adapter/NoteExistsByIdMongoReactiveAdapter.java` — [REVIEW] — соответствует
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/adapter/NoteFindAllMongoReactiveAdapter.java` — [REVIEW] — соответствует
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/adapter/NoteFindByIdMongoReactiveAdapter.java` — [REVIEW] — соответствует
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/adapter/NoteRemoveMongoReactiveAdapter.java` — [REVIEW] — соответствует
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/adapter/NoteReplaceMongoReactiveAdapter.java` — [REVIEW] — `repository.save(document)` — соответствует, тоже не использует `MongoTemplate`, в отличие от sync-аналога
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/adapter/package-info.java` — [REVIEW] — `@NullMarked`, соответствует
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/mapper/NoteMongoReactiveMapper.java` — [REVIEW] — структурно идентична sync-мэпперу — соответствует
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/mapper/NoteMongoReactiveMapperContract.java` — [REVIEW] — соответствует
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/mapper/package-info.java` — [REVIEW] — `@NullMarked`, соответствует
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/model/NoteReactiveDocument.java` — [REVIEW] — идентична `NoteDocument` по структуре — оправданное дублирование между sync/reactive модулями по принятой архитектуре
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/model/package-info.java` — [REVIEW] — `@NullMarked`, соответствует
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/repository/NoteMongoReactiveRepository.java` — [REVIEW] — `extends ReactiveMongoRepository<NoteReactiveDocument, UUID>` — соответствует
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/repository/package-info.java` — [REVIEW] — `@NullMarked`, соответствует
- `note/data-mongodb-reactive/build.gradle.kts` — [REVIEW] — `spring-boot-data-mongodb-reactive` + `implementation(projects.note.dataContractReactive)` — соответствует

#### note/data-r2dbc/ (15 файлов)

- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/adapter/NoteAddR2dbcAdapter.java` — [REVIEW] — `repository.save(toNewEntity(...))` — соответствует «add: save(null id)»
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/adapter/NoteExistsByIdR2dbcAdapter.java` — [REVIEW] — соответствует
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/adapter/NoteFindAllR2dbcAdapter.java` — [REVIEW] — соответствует
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/adapter/NoteFindByIdR2dbcAdapter.java` — [REVIEW] — `Mono<NoteResponse>` (пустой Mono вместо Optional.empty()) — соответствует
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/adapter/NoteRemoveR2dbcAdapter.java` — [REVIEW] — `deleteById` → `Mono<Void>` — соответствует
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/adapter/NoteReplaceR2dbcAdapter.java` — [REVIEW] — `repository.save(toExistingEntity(id, ...))` — соответствует «replace: save(id)»
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/adapter/package-info.java` — [REVIEW] — `@NullMarked`, соответствует
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/mapper/NoteR2dbcMapper.java` — [REVIEW] — `Objects.requireNonNull(entity.getId())` в `toResponse` — структурно идентична `NoteJpaMapper` — соответствует
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/mapper/NoteR2dbcMapperContract.java` — [REVIEW] — соответствует
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/mapper/package-info.java` — [REVIEW] — `@NullMarked`, соответствует
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/model/NoteR2dbcEntity.java` — [REVIEW] — `@Table("notes")`, `@Id @Nullable UUID`, `@Column("content")` — структурно идентична `NoteEntity` (JPA), но без аналога `@GeneratedValue` (Spring Data R2DBC не поддерживает автогенерацию на уровне аннотаций) — `id` для новой записи остаётся `null` до присвоения в БД/схеме, которой сейчас нет (см. находку №5)
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/model/package-info.java` — [REVIEW] — `@NullMarked`, соответствует
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/repository/NoteR2dbcRepository.java` — [REVIEW] — `extends ReactiveCrudRepository<NoteR2dbcEntity, UUID>`, а не технологически-специфичный `org.springframework.data.r2dbc.repository.R2dbcRepository` (единственный из технологий, чей репозиторий не расширяет tech-specific интерфейс — JPA/Mongo/MongoReactive все расширяют `JpaRepository`/`MongoRepository`/`ReactiveMongoRepository`). Функционально не расходится: декомпиляция `spring-data-r2dbc-4.0.5.jar` показала, что `R2dbcRepository` не добавляет собственных методов (в отличие от `MongoRepository`/`ReactiveMongoRepository`, у которых есть `insert()`) — но стилистическая непоследовательность в выборе базового интерфейса есть
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/repository/package-info.java` — [REVIEW] — `@NullMarked`, соответствует
- `note/data-r2dbc/build.gradle.kts` — [REVIEW] — `spring-boot-data-r2dbc` + `implementation(projects.note.dataContractReactive)` — соответствует (в отличие от `data-jdbc`, использующего sync-контракт, что верно, т. к. r2dbc реактивен)

#### note/domain/ (5 файлов)

- `note/domain/src/main/java/com/example/note/domain/NoteNotFoundException.java` — [REVIEW] — `extends RuntimeException`, конструктор от `UUID id` — чистое доменное исключение, без зависимостей на инфраструктуру — соответствует
- `note/domain/src/main/java/com/example/note/domain/NoteRequest.java` — [REVIEW] — `record NoteRequest(String content)` — соответствует
- `note/domain/src/main/java/com/example/note/domain/NoteResponse.java` — [REVIEW] — `record NoteResponse(UUID id, String content)` — соответствует
- `note/domain/src/main/java/com/example/note/domain/package-info.java` — [REVIEW] — `@NullMarked`, соответствует
- `note/domain/build.gradle.kts` — [REVIEW] — `id("com.example.base")` — соответствует

#### note/webflux/ (8 файлов)

- `note/webflux/src/main/java/com/example/note/webflux/NoteCreateController.java` — [REVIEW] — `Mono<ResponseEntity<NoteResponse>>`, `POST /notes` — соответствует «1 контроллер на операцию»
- `note/webflux/src/main/java/com/example/note/webflux/NoteDeleteController.java` — [REVIEW] — `Mono<ResponseEntity<Void>>`, `existsById().flatMap(...)` — поведенчески симметричен `webmvc`-аналогу, соответствует
- `note/webflux/src/main/java/com/example/note/webflux/NoteExceptionHandler.java` — [REVIEW] — **находка**: не наследует reactive-аналог `ResponseEntityExceptionHandler` (в отличие от `webmvc`), имя метода `handleNoteNotFound` (в `webmvc` — `handleNotFound`), дополнительно вызывает `problem.setTitle(...)`, чего нет в `webmvc` — см. находку №3, вопреки записи в CLAUDE.md о «выравнивании» при пересмотре 2026-07-07
- `note/webflux/src/main/java/com/example/note/webflux/NoteFindAllController.java` — [REVIEW] — **находка**: возвращает голый `Flux<NoteResponse>`, без обёртки `ResponseEntity` — единственный из 12 контроллеров (`webmvc`+`webflux`), нарушающий правило «`ResponseEntity<T>` в контроллерах», см. находку №2
- `note/webflux/src/main/java/com/example/note/webflux/NoteFindByIdController.java` — [REVIEW] — `Mono<ResponseEntity<NoteResponse>>`, `switchIfEmpty(Mono.error(...))` — соответствует
- `note/webflux/src/main/java/com/example/note/webflux/NoteUpdateController.java` — [REVIEW] — соответствует, симметричен `webmvc`-аналогу (подтверждает запись в CLAUDE.md о выравнивании update-контроллера)
- `note/webflux/src/main/java/com/example/note/webflux/package-info.java` — [REVIEW] — `@NullMarked`, соответствует
- `note/webflux/build.gradle.kts` — [REVIEW] — `spring-boot-webflux` + `implementation(projects.note.dataContractReactive)` — соответствует

#### note/webmvc/ (8 файлов)

- `note/webmvc/src/main/java/com/example/note/webmvc/NoteCreateController.java` — [REVIEW] — `ResponseEntity<NoteResponse>`, `POST /notes` — соответствует
- `note/webmvc/src/main/java/com/example/note/webmvc/NoteDeleteController.java` — [REVIEW] — `existsById` → `remove` или `NoteNotFoundException` — соответствует
- `note/webmvc/src/main/java/com/example/note/webmvc/NoteExceptionHandler.java` — [REVIEW] — наследует `ResponseEntityExceptionHandler` (servlet) — см. находку №3 (расхождение с `webflux`)
- `note/webmvc/src/main/java/com/example/note/webmvc/NoteFindAllController.java` — [REVIEW] — `ResponseEntity<List<NoteResponse>>` — корректно следует правилу `ResponseEntity<T>` (контраст с `webflux`, см. находку №2)
- `note/webmvc/src/main/java/com/example/note/webmvc/NoteFindByIdController.java` — [REVIEW] — `findById(id).orElseThrow(...)` → `ResponseEntity<NoteResponse>` — соответствует
- `note/webmvc/src/main/java/com/example/note/webmvc/NoteUpdateController.java` — [REVIEW] — соответствует
- `note/webmvc/src/main/java/com/example/note/webmvc/package-info.java` — [REVIEW] — `@NullMarked`, соответствует
- `note/webmvc/build.gradle.kts` — [REVIEW] — `spring-boot-webmvc` + `implementation(projects.note.dataContract)` — соответствует

#### note/ — предлагаемые отсутствующие файлы (`[ADD]`, 27)

- `note/domain/src/test/java/com/example/note/domain/NoteNotFoundExceptionTest.java` — [ADD] — простые `record`ы (`NoteRequest`/`NoteResponse`) тестов не требуют, но исключение стоит покрыть
- `note/data-jpa/src/test/java/com/example/note/data/jpa/mapper/NoteJpaMapperTest.java` — [ADD] — unit-тест мэппера (toNewEntity/toExistingEntity/toResponse)
- `note/data-jpa/src/test/java/com/example/note/data/jpa/adapter/NoteJpaAdapterIntegrationTest.java` — [ADD] — `@DataJpaTest` на все 6 операций — сейчас 0% покрытия по JPA-адаптерам
- `note/data-mongodb/src/test/java/com/example/note/data/mongodb/mapper/NoteMongoMapperTest.java` — [ADD] — unit-тест мэппера
- `note/data-mongodb/src/test/java/com/example/note/data/mongodb/adapter/NoteMongoAdapterIntegrationTest.java` — [ADD] — testcontainers MongoDB — заодно проверит найденное расхождение insert()/save() через MongoTemplate vs repository
- `note/data-mongodb-reactive/src/test/java/com/example/note/data/mongodb/reactive/mapper/NoteMongoReactiveMapperTest.java` — [ADD] — unit-тест мэппера
- `note/data-mongodb-reactive/src/test/java/com/example/note/data/mongodb/reactive/adapter/NoteMongoReactiveAdapterIntegrationTest.java` — [ADD] — testcontainers MongoDB + `StepVerifier`
- `note/data-r2dbc/src/test/java/com/example/note/data/r2dbc/mapper/NoteR2dbcMapperTest.java` — [ADD] — unit-тест мэппера
- `note/data-r2dbc/src/test/java/com/example/note/data/r2dbc/adapter/NoteR2dbcAdapterIntegrationTest.java` — [ADD] — testcontainers Postgres/`@DataR2dbcTest` — потребует schema.sql (см. ниже)
- `note/data-r2dbc/src/main/resources/schema.sql` — [ADD] — у `NoteR2dbcEntity` нет `@GeneratedValue`-аналога — без схемы с `DEFAULT`/`GENERATED` для `id` реальный `INSERT` не заработает; см. открытое решение «Управление схемой для R2DBC/JDBC»
- `note/data-jdbc/src/test/java/com/example/note/data/jdbc/mapper/NoteJdbcMapperTest.java` — [ADD] — unit-тест `fromRow(ResultSet, rowNum)`
- `note/data-jdbc/src/test/java/com/example/note/data/jdbc/adapter/NoteJdbcAdapterIntegrationTest.java` — [ADD] — `@JdbcTest`/testcontainers — заодно проверит найденное поведение `NoteReplaceJdbcAdapter` при несуществующем id
- `note/data-jdbc/src/main/resources/schema.sql` — [ADD] — `NamedParameterJdbcTemplate` не создаёт схему сам — нужна явная схема таблицы `notes`
- `note/webmvc/NoteCreateControllerTest.java` — [ADD] — `@WebMvcTest` slice-тест
- `note/webmvc/NoteDeleteControllerTest.java` — [ADD] — `@WebMvcTest` slice-тест
- `note/webmvc/NoteFindAllControllerTest.java` — [ADD] — `@WebMvcTest` slice-тест
- `note/webmvc/NoteFindByIdControllerTest.java` — [ADD] — `@WebMvcTest` slice-тест
- `note/webmvc/NoteUpdateControllerTest.java` — [ADD] — `@WebMvcTest` slice-тест
- `note/webmvc/NoteExceptionHandlerTest.java` — [ADD] — тест `ProblemDetail`-ответа на `NoteNotFoundException`
- `note/webflux/NoteCreateControllerTest.java` — [ADD] — `@WebFluxTest` slice-тест
- `note/webflux/NoteDeleteControllerTest.java` — [ADD] — `@WebFluxTest` slice-тест
- `note/webflux/NoteFindAllControllerTest.java` — [ADD] — `@WebFluxTest` slice-тест — заодно зафиксирует найденное расхождение по `ResponseEntity`
- `note/webflux/NoteFindByIdControllerTest.java` — [ADD] — `@WebFluxTest` slice-тест
- `note/webflux/NoteUpdateControllerTest.java` — [ADD] — `@WebFluxTest` slice-тест
- `note/webflux/NoteExceptionHandlerTest.java` — [ADD] — тест `ProblemDetail`-ответа, заодно проверит найденное расхождение с `webmvc`
- `note/application/src/test/java/com/example/note/NoteCreateEndpointIntegrationTest.java` — [ADD] — сквозной тест реального стека (`webmvc`+`data-jpa`+H2) через `MockMvc`, а не только `contextLoads()`
- `note/application-webflux/build.gradle.kts` — [ADD] (концептуально) — вторая связка technology (`webflux`+`data-r2dbc` или `data-mongodb-reactive`), чтобы реактивный стек был реально запускаем и тестируем — см. открытое решение «Комбинации technology в `application/`»

### gradle/ (4 файла)

`libs.versions.toml` и `gradle-wrapper.properties` — версии совпадают с «Стек»/«Синхронизация версий» (Gradle 9.6.0, Spring Boot 4.0.6, Spring Cloud 2025.1.2, JUnit 6.0.3). `checkstyle/checkstyle.xml` — `SpringChecks` + excludes javadoc-проверок, согласуется с memory-заметкой про версию spring-javaformat 0.0.47.

#### gradle/wrapper/ (2 файлов)

- `gradle/wrapper/gradle-wrapper.jar` — [REVIEW] — бинарный файл wrapper, не проверяется построчно
- `gradle/wrapper/gradle-wrapper.properties` — [REVIEW] — Gradle 9.6.0, совпадает со «Стек»


#### gradle/ — отдельные файлы (2)

- `gradle/checkstyle/checkstyle.xml` — [REVIEW] — `SpringChecks` + excludes javadoc-проверок, согласуется с memory-заметкой про версию 0.0.47
- `gradle/libs.versions.toml` — [REVIEW] — версии совпадают со «Стек»/«Синхронизация версий»



### gateway/ (6 файлов)

Skeleton-сервис Spring Cloud Gateway (webflux). `spring.config.import=optional:configserver:` — опциональный импорт, не сломает старт без config-сервера. Без routes — соответствует статусу СКЕЛЕТ в «Задачах».

#### gateway/application/ (6 файлов)

- `gateway/application/src/main/java/com/example/gateway/GatewayApplication.java` — [REVIEW] — стандартный `@SpringBootApplication`, без routes-класса
- `gateway/application/src/main/java/com/example/gateway/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `gateway/build.gradle.kts` — [REVIEW] — `spring-cloud-gateway-webflux` + `spring-boot-actuator` + `spring-cloud-eureka-client` + `spring-cloud-config-client`
- `gateway/src/main/resources/application.properties` — [REVIEW] — `spring.config.import=optional:configserver:` — опциональный импорт, не сломает старт без config-сервера
- `gateway/src/test/java/com/example/gateway/GatewayApplicationTests.java` — [REVIEW] — только `contextLoads()`
- `gateway/src/test/resources/application.properties` — [REVIEW] — `spring.cloud.config.enabled=false`, `spring.cloud.discovery.enabled=false` — тест изолирован от registry/config

### config/ (6 файлов)

Skeleton Config Server. `spring.profiles.active=native`, но `search-locations` не задан — сервер стартует, но конфигурацию пока не отдаст (соответствует «без config-репозитория» в «Задачах»).

#### config/application/ (6 файлов)

- `config/application/src/main/java/com/example/config/ConfigApplication.java` — [REVIEW] — `@EnableConfigServer` + `@SpringBootApplication`
- `config/application/src/main/java/com/example/config/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `config/build.gradle.kts` — [REVIEW] — `spring-cloud-config-server` + `spring-boot-actuator`
- `config/src/main/resources/application.properties` — [REVIEW] — `spring.profiles.active=native`, но `search-locations` не задан — реальную конфигурацию пока не отдаст
- `config/src/test/java/com/example/config/ConfigApplicationTests.java` — [REVIEW] — только `contextLoads()`
- `config/src/test/resources/application.properties` — [REVIEW] — `spring.profiles.active=native`, дублирует main

### build-logic/ (39 файлов)

**Главные находки:**

1. Реальных расхождений между кодом `build-logic/` и текстом CLAUDE.md **не найдено**. Обе оси иерархии (BOM-цепочка `base → library/reactor/spring-boot → spring-cloud → tech-plugin` и ортогональная bootable-ось `org.springframework.boot`) воспроизведены в коде ровно так, как описано: ровно 4 standalone `spring-cloud-*`-плагина (`config-server`, `eureka-server`, `gateway-webflux`, `gateway-webmvc`) подключают `com.example.spring-boot-application` вторым родителем, остальные 5 `spring-cloud-*` — нет.
2. Числа сходятся точно: 17 технологических `spring-boot-*`-плагинов, 9 `spring-cloud-*`-плагинов, 5 плагинов внутри `codequality`, 7 плагинов "1:1 с папкой модуля" (`webmvc`, `webflux`, `data-jpa`, `data-jdbc`, `data-r2dbc`, `data-mongodb`, `data-mongodb-reactive`) — совпадает с формулировками в «Иерархия».
3. Версии в `gradle/libs.versions.toml` (`spring-boot=4.0.6`, `spring-cloud=2025.1.2`, `reactor-core=3.8.5`, `junit-jupiter=junit-platform=6.0.3`, `spring-javaformat=0.0.47`, `checkstyle=9.3`, `jacoco=0.8.14`) совпадают со всем, что зафиксировано в «Синхронизация версий» и «Стек».
4. `com.example.spring-boot` берёт BOM через `SpringBootPlugin.BOM_COORDINATES`, а `com.example.spring-cloud` — вручную строкой через `libs.findVersion("spring-cloud")` — ровно та асимметрия, что объяснена в «Синхронизация версий» (у Spring Cloud нет своего Gradle-плагина).
5. Отступ везде 4 пробела, ни одного таба — проверено программно по всем 39 файлам. Не найдено ни одного файла, отсутствие которого противоречило бы перечисленному в CLAUDE.md (счётчики плагинов из п. 2 сошлись без остатка) — записей `[ADD]` в списке нет.

Единственная тонкость, которая на первый взгляд похожа на нестыковку, но по факту — подтверждение уже задокументированного намеренного решения: `com.example.checkstyle` и `com.example.javaformat` независимо конфигурируют один и тот же extension `checkstyle {}` (оба выставляют `toolVersion`, `javaformat` — ещё и `configFile`/`configProperties`) — ровно так, как описано в «`com.example. checkstyle` остаётся отдельным плагином...».

#### build-logic/ — корневые файлы (2 файла)

- `build-logic/convention/build.gradle.kts` — [REVIEW] — `kotlin-dsl` + classpath-зависимости (`spring-boot-gradle-plugin`, `dependency-management-plugin`, `spring-javaformat-gradle-plugin`, `gradle-errorprone-plugin`) через **typed-аксессоры** (`libs.versions.spring.boot.get()` и т. п.) — совпадает с «Синхронизация версий»: typed-аксессоры доступны именно в обычных build-скриптах, не в precompiled-плагинах. Отступ 4 пробела, без табов.
- `build-logic/settings.gradle.kts` — [REVIEW] — `rootProject.name = "build-logic"`, `versionCatalogs { create("libs") { from(files("../gradle/libs.versions.toml")) } }` — совпадает с «Синхронизация версий»: «build-logic — отдельный included build и не видит корневой gradle.properties/каталог автоматически, поэтому build-logic/settings.gradle.kts подключает тот же .toml-файл отдельно». `include(":convention")` совпадает с «Convention plugins — build-logic/convention/» и деревом `build-logic/settings.gradle.kts include(":convention")`.


#### build-logic/convention/src/main/kotlin/ — precompiled script plugins (37 файлов)

- `build-logic/com.example.base.gradle.kts` — [REVIEW] — `id("java")` + `id("com.example.codequality")`, toolchain через `providers.fileContents(...).asText` (Provider API) из `.java-version`, `junit-jupiter`/`junit-platform-launcher` из каталога (`testImplementation`/`testRuntimeOnly`), `useJUnitPlatform()` — дословно совпадает с «Иерархия» (единственный родитель — `codequality`) и «Синхронизация версий» → Java/junit-jupiter. Версии в каталоге (6.0.3/6.0.3) совпадают с зафиксированным фактом «Spring Boot 4.0.6 управляет JUnit 6».
- `build-logic/com.example.checkstyle.gradle.kts` — [REVIEW] — `id("checkstyle")` без `com.example.*`-родителя, `id("java")` не подключается — совпадает с «checkstyle — id("checkstyle"); без com.example.* родителя; id("java") не нужен». `toolVersion` + `checkstyle`-зависимость (puppycrawl) из `libs.findVersion("checkstyle")` = 9.3 — через каталог, как описано в «Синхронизация версий».
- `build-logic/com.example.codequality.gradle.kts` — [REVIEW] — Агрегирует ровно 5 плагинов (`checkstyle`, `javaformat`, `nullaway`, `jacoco`, `jacoco-report-aggregation`) — совпадает с «агрегатор 5 плагинов выше»; не применяет `com.example.base` — подтверждает «цикла не возникает: codequality-плагины не применяют com.example.base».
- `build-logic/com.example.jacoco-report-aggregation.gradle.kts` — [REVIEW] — `id("jacoco-report-aggregation")`, вообще без родителя и без доп. конфигурации — совпадает с «jacoco-report-aggregation — без родителя вообще (autoconfig)».
- `build-logic/com.example.jacoco.gradle.kts` — [REVIEW] — `id("jacoco")` без `com.example`-родителя, `toolVersion` из `libs.findVersion("jacoco")` = 0.8.14 через каталог — совпадает с «jacoco — id("jacoco"); id("java") не нужен» и «Синхронизация версий».
- `build-logic/com.example.javaformat.gradle.kts` — [REVIEW] — `id("io.spring.javaformat")` + `id("checkstyle")` (core-плагин, не `com.example.checkstyle`), независимо конфигурирует тот же extension `checkstyle {}` (`toolVersion`, `configFile`, `configProperties`) — ровно преднамеренное дублирование из «`com.example.checkstyle` остаётся отдельным плагином...». `checkstyle`-зависимость (`spring-javaformat-checkstyle`) добавлена вручную — версия каталога `spring-javaformat = 0.0.47` согласуется с memory-заметкой «checkstyle-зависимость обязательна вручную в 0.0.47».
- `build-logic/com.example.library.gradle.kts` — [REVIEW] — `id("com.example.base")` + `id("java-library")` — 1 родитель `base`, без Spring — совпадает с «com.example.library — 1 родитель com.example.base; добавляет Gradle-плагин java-library, без Spring».
- `build-logic/com.example.nullaway.gradle.kts` — [REVIEW] — `id("java-library")` + `id("net.ltgt.errorprone")`, без `com.example`-родителя (вне BOM-цепочки через `base`) — совпадает с «исключение — com.example.nullaway» и «следствие: все модули транзитивно получают java-library через base → codequality → nullaway». `jspecify`/`errorprone-core`/`nullaway` — из каталога, версии совпадают.
- `build-logic/com.example.reactor.gradle.kts` — [REVIEW] — `id("com.example.base")` — родитель сменён с `library` на `base`, как задокументировано; `reactor-core`/`reactor-tools` (`implementation`) + `reactor-test` (`testImplementation`) из `libs.findVersion("reactor-core")` = 3.8.5, без `io.spring.dependency-management` — совпадает дословно с «com.example.reactor» и «Синхронизация версий» → reactor-core.
- `build-logic/com.example.spring-boot-actuator.gradle.kts` — [REVIEW] — `id("com.example.spring-boot")` — обычный технологический плагин (без bootable-оси). Отдельно текстом в CLAUDE.md не описан (упомянут лишь как часть подключённого стека `registry/`/`config/`/`gateway/`), но соответствует общему паттерну `implementation`/`testImplementation` через `-test`-компаньон.
- `build-logic/com.example.spring-boot-application.gradle.kts` — [REVIEW] — `id("com.example.spring-boot")` + `id("org.springframework.boot")` — ровно вторая (bootable) ось иерархии, как задокументировано в «Convention plugins — принцип именования и структура».
- `build-logic/com.example.spring-boot-client-rest.gradle.kts` — [REVIEW] — `id("com.example.spring-boot")`, зависимость `spring-boot-starter-restclient`(`-test`) — имя плагина = переименованный `restclient`, как в «com.example.spring-boot-client-rest / com.example.spring-boot-client-web (были restclient/webclient) — переименованы».
- `build-logic/com.example.spring-boot-client-web.gradle.kts` — [REVIEW] — `id("com.example.spring-boot")`, зависимость `spring-boot-starter-webclient`(`-test`), без явного `reactor-test` — совпадает с «spring-boot-client-web — 1 родитель (spring-boot), без явного reactor-test: ... reactor-test:3.8.5 приходит транзитивно».
- `build-logic/com.example.spring-boot-data-elasticsearch.gradle.kts` — [REVIEW] — `id("com.example.spring-boot")`; отдельно текстом CLAUDE.md не описан, паттерн стандартный (`implementation`/`testImplementation` через `-test`-компаньон).
- `build-logic/com.example.spring-boot-data-jdbc.gradle.kts` — [REVIEW] — `id("com.example.spring-boot")`; один из «7 плагинов 1:1 с папкой модуля» — id совпадает с именем папки/starter'а, как того требует правило именования.
- `build-logic/com.example.spring-boot-data-jpa.gradle.kts` — [REVIEW] — `id("com.example.spring-boot")`; один из «7 плагинов 1:1 с папкой модуля» — id совпадает с именем папки/starter'а.
- `build-logic/com.example.spring-boot-data-mongodb-reactive.gradle.kts` — [REVIEW] — `id("com.example.spring-boot")`; один из «7 плагинов 1:1 с папкой модуля» — id совпадает с именем папки/starter'а.
- `build-logic/com.example.spring-boot-data-mongodb.gradle.kts` — [REVIEW] — `id("com.example.spring-boot")`; один из «7 плагинов 1:1 с папкой модуля» — id совпадает с именем папки/starter'а.
- `build-logic/com.example.spring-boot-data-r2dbc.gradle.kts` — [REVIEW] — `id("com.example.spring-boot")`; один из «7 плагинов 1:1 с папкой модуля», без явного `reactor-test` — совпадает с «Синхронизация версий» → reactor-test (приходит транзитивно через `spring-boot-starter-data-r2dbc-test`).
- `build-logic/com.example.spring-boot-graphql.gradle.kts` — [REVIEW] — `id("com.example.spring-boot")`; сам сервис `graphql/` — статус ОТЛОЖЕНО в «Задачи», но существование готового convention-плагина этому не противоречит: текст описывает именно «convention plugins подготовлены... но не применены ни в одном модуле». Упомянут в «Синхронизация версий» → reactor-test: `spring-boot-starter-graphql-test` не содержит `reactor-test` — файл его и не добавляет.
- `build-logic/com.example.spring-boot-h2-database.gradle.kts` — [REVIEW] — `id("com.example.spring-boot")`, зависимости `spring-boot-h2console` (`implementation`) + `com.h2database:h2` (`runtimeOnly`); отдельно текстом CLAUDE.md не описан.
- `build-logic/com.example.spring-boot-oauth2-authorization-server.gradle.kts` — [REVIEW] — Зависимость `spring-boot-starter-security-oauth2-authorization-server`(`-test`) — совпадает со «Стек» → «OAuth2-стартеры: ... аналогично для client и authorization-server» и «Spring Authorization Server — часть Spring Security 7, отдельной версии не имеет» (своей версии в каталоге нет — только Spring Boot BOM через родителя).
- `build-logic/com.example.spring-boot-oauth2-client.gradle.kts` — [REVIEW] — Зависимость `spring-boot-starter-security-oauth2-client`(`-test`) — совпадает со «Стек» → «OAuth2-стартеры».
- `build-logic/com.example.spring-boot-oauth2-resource-server.gradle.kts` — [REVIEW] — Зависимость `spring-boot-starter-security-oauth2-resource-server`(`-test`) — совпадает со «Стек» → «OAuth2-стартеры: oauth2-resource-server → security-oauth2-resource-server».
- `build-logic/com.example.spring-boot-webflux.gradle.kts` — [REVIEW] — `id("com.example.spring-boot")`, зависимость `spring-boot-starter-webflux`(`-test`), без явного `reactor-test` — совпадает со «Стек» (не `starter-web`) и «Синхронизация версий» → reactor-test (транзитивно через `-webflux-test`).
- `build-logic/com.example.spring-boot-webmvc.gradle.kts` — [REVIEW] — Зависимость `spring-boot-starter-webmvc`(`-test`) — совпадает со «Стек»: `starter-web` → `starter-webmvc`.
- `build-logic/com.example.spring-boot.gradle.kts` — [REVIEW] — `id("com.example.base")` + `id("io.spring.dependency-management")`, BOM через `SpringBootPlugin.BOM_COORDINATES`, свой `spring-boot-starter`/`-test` — дословно совпадает с «Синхронизация версий» → com.example.spring-boot и «Архитектура» → «spring-boot-starter/-test — общий для любого Spring Boot модуля, объявлен в com.example.spring-boot».
- `build-logic/com.example.spring-cloud-circuit-breaker.gradle.kts` — [REVIEW] — `id("com.example.spring-cloud")` только — один из 5 «нестандэлон» `spring-cloud-*`-плагинов без bootable-оси, совпадает с деревом иерархии.
- `build-logic/com.example.spring-cloud-config-client.gradle.kts` — [REVIEW] — `id("com.example.spring-cloud")` только — не входит в список «4 standalone-сервисных spring-cloud-*», bootable-ось отсутствует, как и должно быть.
- `build-logic/com.example.spring-cloud-config-server.gradle.kts` — [REVIEW] — `id("com.example.spring-cloud")` + `id("com.example.spring-boot-application")` — один из 4 standalone-сервисных плагинов с bootable-осью («Плагины, требующие bootJar — ... spring-cloud-config-server, ...»), совпадает дословно.
- `build-logic/com.example.spring-cloud-eureka-client.gradle.kts` — [REVIEW] — `id("com.example.spring-cloud")` только — не входит в список 4 standalone-плагинов, bootable-ось отсутствует корректно.
- `build-logic/com.example.spring-cloud-eureka-server.gradle.kts` — [REVIEW] — `id("com.example.spring-cloud")` + `id("com.example.spring-boot-application")` — один из 4 standalone-сервисных плагинов, совпадает дословно со списком в CLAUDE.md.
- `build-logic/com.example.spring-cloud-gateway-webflux.gradle.kts` — [REVIEW] — `id("com.example.spring-cloud")` + `id("com.example.spring-boot-application")` — standalone, совпадает. Без `reactor-test` — совпадает с «Синхронизация версий» → reactor-test: «Spring Cloud gateway-webflux — плагин Spring Cloud, своего -test-компаньона не существует... оставлено осознанно без замены».
- `build-logic/com.example.spring-cloud-gateway-webmvc.gradle.kts` — [REVIEW] — `id("com.example.spring-cloud")` + `id("com.example.spring-boot-application")` — один из 4 standalone-сервисных плагинов, совпадает дословно.
- `build-logic/com.example.spring-cloud-loadbalancer.gradle.kts` — [REVIEW] — `id("com.example.spring-cloud")` только — не входит в список 4 standalone-плагинов, совпадает.
- `build-logic/com.example.spring-cloud-openfeign.gradle.kts` — [REVIEW] — `id("com.example.spring-cloud")` только — не входит в список 4 standalone-плагинов, совпадает.
- `build-logic/com.example.spring-cloud.gradle.kts` — [REVIEW] — `id("com.example.spring-boot")` + ручной BOM через `libs.findVersion("spring-cloud").get().requiredVersion` = 2025.1.2 (строка, не через плагин-константу) — дословно совпадает с обоснованием в «Синхронизация версий» → com.example.spring-cloud (у Spring Cloud нет своего Gradle-плагина/`BOM_COORDINATES`-аналога). Собственных `junit-platform-launcher`/`useJUnitPlatform()` нет — совпадает с «убран как дублирующий то, что уже даёт родитель com.example.base».


### auth/ (6 файлов)

Skeleton-модуль (`com.example.spring-boot-application`, логики нет). `src/test/resources/application.properties` — пустой файл.

#### auth/application/ (6 файлов)

- `auth/application/src/main/java/com/example/auth/AuthApplication.java` — [REVIEW] — пустой `@SpringBootApplication`-класс без логики
- `auth/application/src/main/java/com/example/auth/package-info.java` — [REVIEW] — `@NullMarked`, соответствует конвенции
- `auth/build.gradle.kts` — [REVIEW] — только `spring-boot-application` — соответствует статусу СКЕЛЕТ, логики нет
- `auth/src/main/resources/application.properties` — [REVIEW] — только `spring.application.name`
- `auth/src/test/java/com/example/auth/AuthApplicationTests.java` — [REVIEW] — только `contextLoads()`
- `auth/src/test/resources/application.properties` — [REVIEW] — файл пустой — вероятно, задел на будущее

### .github/ (1 файл)

`workflows/gradle.yml` — гоняет `./gradlew build` (без `clean`) на push/PR в `main`; не разведены `check`/`build`, в отличие от локального workflow из раздела «Правила». Править только по отдельному запросу (правило «CI»).

- `.github/workflows/gradle.yml` — [REVIEW] — `./gradlew build` без `clean` на push/PR в `main`; не разведены `check`/`build`, отдельно от локального workflow — править только по отдельному запросу

