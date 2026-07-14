# CLAUDE.md — notes-spring

> Последнее обновление: Tue Jul 14 13:23:34 IDT 2026 **Всё временно** — любое решение подлежит обсуждению и изменению.

Многомодульный Spring Boot 4 проект (`note/`, `user/`, `user-note/`, ...), реализующий hexagonal architecture единообразно во всех сервисах через Gradle convention plugins. Этот файл — единственный источник истины по конвенциям, статусу и решениям проекта; вся необходимая для работы над проектом информация должна быть здесь, без обращения к внешним источникам.

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
- **Списки вместо таблиц** — структурированные данные (путь/статус/комментарий, элемент/паттерн/пример и т. п.) — плоским списком `- поле — поле — поле`, не markdown-таблицей: правка одной записи — это правка одной строки, без пересчёта соседних
- **Без ручных переносов строк** — один абзац/пункт списка/строка blockquote — одна строка файла, без жёсткого переноса на 80–120 символов; перенос — только смысловой (конец абзаца, новый пункт списка, пустая строка)
- **Доступность CLAUDE.md для ИИ-агента** — при любой правке этого файла формат оптимизируется для чтения/правки ИИ-агентом без потери информации: полный путь от корня репозитория в каждой строке каталога (строка самодостаточна без соседних заголовков), не более 2 уровней вложенности заголовков (`### сервис/` → `#### модуль/`), ASCII-статусы вместо emoji, явная схема полей строкой прямо перед списком — применять сразу, без дополнительного подтверждения
- **Архитектурные решения** — сначала варианты с плюсами/минусами и рекомендацией, дождаться выбора пользователя; не реализовывать до явного решения
- **Пересмотр решений** — статус «принято»/«зафиксировано» (в т. ч. в «Принятые решения») не закрывает вопрос навсегда: любое решение по CRUD-сервисам и по проекту в целом нужно поднимать заново для обсуждения и пересмотра в подходящий момент, а не только по отдельному запросу
- **Build** — после каждого логического шага: `./gradlew clean check` (быстрее `build`: без `assemble`/`bootJar`) → обновить CLAUDE.md → коммит → пуш; перед коммитом дополнительно `./gradlew clean build` (проверяет и паковку — `bootJar`/`resolveMainClassName`, что `check` не покрывает)
- **Подзадачи** — крупную задачу разбивать на подзадачи; коммитить и пушить после каждой завершённой подзадачи, чтобы не терять прогресс при обрыве сессии
- **⚠️ Лимит размера файла** (2026-07-14) — при приближении/превышении порога `⚠ CLAUDE.md is over the 150.0k-char limit` (мерить `wc -c CLAUDE.md`) сокращать можно, но не всё подряд: (1) раздел «Правила» — ни одна строка не удаляется, это правила для ИИ-агента; (2) блок «⛔ Референс: полный набор Spring Boot стартеров — ЗАПРЕЩЕНО УДАЛЯТЬ ИЛИ СОКРАЩАТЬ» (перенесён в самый конец файла 2026-07-14, объединён из 5 присланных снапшотов) — не сокращать, держать максимально полным; (3) «Каталог файлов проекта» — править только очень аккуратно: можно добавлять пропущенные файлы с пометкой `[REVIEW]`, можно удалять строки реально удалённых файлов, но пометку `[DONE]` ставить только по явному принудительному указанию пользователя, не по инициативе агента. Сокращать без ограничений — остальные разделы (сворачивать историческую избыточность в «Задачах»/«Принятые решения» и т. п.), не теряя решения по существу

---

## ⚡ Задачи

**Текущая работа** — построчный пересмотр «Каталог файлов проекта» (см. соответствующий раздел ниже): ровно один файл за раз — показать разбор → дождаться личного утверждения пользователем → пометить `[DONE]` в каталоге → перейти к следующей строке `[REVIEW]` сверху вниз. Не забегать вперёд, не разбирать несколько файлов пакетом. Была временно прервана более срочными задачами (переход `data-jdbc/`, `@NullUnmarked`, слияние референсов — все завершены 2026-07-14, см. ниже и «Открытые решения» → «Каталог `data/`») — можно возвращаться к построчному пересмотру.

**Validation-стартер и actuator — перенесены на подходящие общие convention-плагины (2026-07-14)**: (1) `spring-boot-starter-validation`(`-test`) добавлен прямо в `com.example.spring-boot` (родитель для любого Spring Boot модуля), рядом с уже существующими `spring-boot-starter`/`-test` — реализует первую половину открытого решения «Jakarta Bean Validation для Entity/Document» (сам механизм валидации доступен теперь во всех модулях; `@NotNull`/`@NotBlank`-аннотации на конкретных `Entity`/`Document`-классах — по-прежнему не расставлены, см. «Открытые решения»). Отдельный опциональный `com.example.spring-boot-validation` не создавался — по итогу обсуждения выбрано слияние в уже применяемый везде плагин, а не новый opt-in. (2) `spring-boot-starter-actuator`(`-test`) перенесён из отдельного `com.example.spring-boot-actuator` (применялся вручную только в `registry`/`config`/`gateway`) в `com.example.spring-boot-application` — вторую (bootable) ось иерархии: actuator имеет смысл только там, где реально есть `bootJar`/`main()`, а не в библиотечных модулях типа `data-jpa`/`webmvc`. Плагин `com.example.spring-boot-actuator.gradle.kts` удалён, явные `id("com.example.spring-boot-actuator")` убраны из `registry/application/`, `config/application/`, `gateway/application/` (актуатор приходит транзитивно через `spring-cloud-*` → `spring-boot-application`); `note/`/`user/`/`user-note/` (`application/`+`application-reactive/`) и `auth/` получили actuator впервые — раньше не был подключён нигде, кроме трёх инфраструктурных скелетов. Actuator одинаково работает в sync (`webmvc`) и reactive (`webflux`) стеке — сам определяет тип веб-приложения через autoconfiguration (`WebMvcEndpointHandlerMapping`/`WebFluxEndpointHandlerMapping`), отдельных артефактов не требует. `./gradlew clean check`+`clean build` по всему проекту — чисто.

**`jakarta.validation-api` — добавлен в `com.example.base` (2026-07-14)**: вторая половина открытого решения «Jakarta Bean Validation для Entity/Document» — `spring-boot-starter-validation` (см. выше) даёт полноценную валидацию (Hibernate Validator + Spring-интеграция) только в Spring Boot модулях (`data-*`/`webmvc`/`webflux`/...), но `domain/` (чистая Java, без Spring) не мог использовать сами аннотации `jakarta.validation.constraints.*` (`@NotNull`/`@NotBlank` и т. п.) на `NoteRequest`/`UserRequest`/`UserNoteRequest`. Добавлен `jakarta.validation:jakarta.validation-api:3.1.1` (версия сверена с тем, что реально резолвит Spring Boot 4.1.0 BOM — `./gradlew :note:application:dependencies`, тот же приём, что и у `reactor-core`) как `implementation` в `com.example.base` — единственный плагин, который все три `domain/`-модуля применяют напрямую (проверено `grep` — больше никто `com.example.base` напрямую не подключает, `library`/`reactor`/`spring-boot` идут через него как родителя, так что зависимость наследуется всей иерархией, включая уже имеющийся `spring-boot-starter-validation` — конфликта версий нет, Gradle берёт совпадающую). Версия — в `gradle/libs.versions.toml` (`jakarta-validation = "3.1.1"`), не текстовым литералом. `implementation`, не `api` — сама аннотация не входит в публичную сигнатуру метода record'а (`content(): String`, не `NotBlank`), только в саму разметку поля, согласуется с правилом «`api` только когда тип используется в публичной сигнатуре». Альтернатива (отдельный `com.example.domain`-плагин вместо правки `base`) была предложена и отклонена пользователем в пользу более широкой доступности, консистентно с решением по `validation`/`actuator` выше. Расстановка самих `@NotNull`/`@NotBlank` на полях `domain/`-record'ов — по-прежнему не сделана, см. «Открытые решения». Заодно при проверке — прогнан DFS по графу `id("com.example.*")` всех 36 convention-плагинов, циклов не найдено (чистый DAG, макс. глубина 4 уровня, единственное схождение путей — ромб `spring-cloud-config-server`/`eureka-server`/`gateway-webflux`/`gateway-webmvc` → `spring-cloud` и → `spring-boot-application`, оба пути сходятся в общем предке `spring-boot`, назад не идёт). `./gradlew clean check` — чисто.

**Общий маркер-интерфейс `{Entity}Persistable` + унификация `role` + `schema.sql` для R2DBC/JDBC — завершено 2026-07-14**: по запросу пользователя выделен общий интерфейс для всех Entity/Document — `NotePersistable`/`UserPersistable`/`UserNotePersistable` в `domain/` (единственный модуль, общий для sync+reactive технологий сразу), реализован всеми 15 model-классами. Заодно, в процессе формулирования требований к полям (`id`/`userId`/`noteId` — `NOT NULL`, `id` — автогенерируемый на уровне БД UUID, `userId+noteId` — составной unique key), закрыты два открытых вопроса: (1) `role` в `user-note` унифицирован до `UserNoteRole` enum везде (проверено docs.spring.io — Spring Data JDBC/R2DBC маппят enum в строку из коробки, ручная конвертация была не нужна); (2) «Управление схемой для R2DBC/JDBC» решено в пользу `schema.sql` — создан для всех трёх сущностей в `data-jdbc`/`data-r2dbc`. Подробности — см. «Принятые решения» → «Архитектура». `./gradlew clean check`+`clean build` — чисто.

**`data-jdbc/`: переход на Spring Data JDBC repository — завершено по всем трём сервисам 2026-07-14**: прежнее решение «сырой `NamedParameterJdbcTemplate`, без `model/`/`repository/`» признано пользователем ошибочным («если у нас есть стартер с готовым repository интерфейсом — не пишем свой repository»): раз convention-плагин уже подключает `spring-boot-starter-data-jdbc` (см. «Полнота R2DBC/JDBC-стартеров» выше), `data-jdbc/` получил `model/`+`repository/`+`mapper/` во всех трёх сервисах, по структуре зеркалируя `data-r2dbc/` (та же `org.springframework.data.relational.core.mapping.Table`/`Column`/`org.springframework.data.annotation.Id` — Spring Data JDBC и R2DBC используют один и тот же модуль `spring-data-relational`), адаптеры — через репозиторий (`ListCrudRepository`, не `CrudRepository` — доступен с Spring Data 3.0, даёт `findAll(): List<T>` напрямую) вместо сырого SQL, `mapper` — `toNewEntity`/`toExistingEntity`/`toResponse` вместо `fromRow(ResultSet, int)`. `note/`/`user/` — repository-based адаптер 1:1 копирует структуру одноимённого `data-jpa`-адаптера; `user/data-jdbc/`'шный `UserJdbcRepository` получил derived-методы `findByUsername`/`findByEmail` (`Optional`, sync — не `Mono` как у `UserR2dbcRepository`). `user-note/` — самый сложный случай (15 методов, разбивка по суррогатному `userNoteId` и составному `userId+noteId`): `UserNoteJdbcRepository extends ListCrudRepository<UserNoteJdbcEntity, UUID>` получил `findByUserId`/`findByNoteId`(`List`)/`findByUserIdAndNoteId`(`Optional`)/`existsByUserId`/`existsByNoteId`/`existsByUserIdAndNoteId` — все derived-методами, `@Query` не понадобился; адаптер `UserNoteService` — 1:1 копия структуры `user-note/data-jpa`-шного адаптера. `./gradlew clean check`+`clean build` по всему проекту — чисто на каждом из трёх шагов. Строка «Асимметрия `data-jdbc/`» в «Открытые решения» удалена (вопрос закрыт, не открытый больше), абзац о `data-jdbc/` в «Принятые решения» → «Архитектура» обновлён. **Не решено**: схема БД (`schema.sql`) по-прежнему не создана нигде — открытый вопрос «Управление схемой для R2DBC/JDBC» не затронут, `data-jdbc/` ни в одном сервисе не подключён к `application/`, так что реальные запросы к БД пока не проверялись end-to-end (только компиляция + Checkstyle + `contextLoads` — не применимо, `data-jdbc/` не в composition root)

**Слияние референсов — завершено 2026-07-14**: объединил все присланные Initializr-снапшоты в один блок и перенёс его в конец файла (см. «⛔ Референс...» в самом низу CLAUDE.md) — подробности объединения (4 источника, версии, 7 новых стартеров) записаны прямо в интро того раздела, не дублирую здесь.

Статус по сервисам и модулям:

```
ГОТОВО   note/  — domain · contract · contract-reactive · webmvc · webflux
                  data-jpa · data-mongodb · data-jdbc · data-r2dbc · data-mongodb-reactive
ГОТОВО   user/  — domain · contract · contract-reactive · webmvc · webflux
                  data-jpa · data-mongodb · data-jdbc · data-r2dbc · data-mongodb-reactive
ГОТОВО   user-note/ — domain · contract · contract-reactive · webmvc · webflux
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

**Форматирование — без авто-форматтера, только Checkstyle** (2026-07-13, было «Spring JavaFormat»): `io.spring.javaformat` убран полностью — его `lineSplit=120` зашит в jar без возможности переопределить, а `join_wrapped_lines=true` пересобирает любой ручной перенос строки, а не просто режет по длине, поэтому смена движка (Spotless) тоже не решает задачу. Переносы — только вручную. Единственный гейт — Checkstyle: `SpringChecks` (зависимость/`configFile`/`configProperties` перенесены из удалённого `com.example.javaformat` в `com.example.checkstyle`) + 5 модулей (`FileTabCharacter`, 2× `RegexpMultiline`, `Indentation`, `EmptyLineSeparator`) под 10 персональных правил пользователя, см. «Стиль кода». `SpringLeadingWhitespaceCheck` исключён (требовал табы, конфликт с `FileTabCharacter`). 143 файла массово приведены в соответствие (115 — убрана пустая строка перед `}`, 28 — добавлена обязательная пустая строка в пустых телах).

**`spring-javaformat` — пин на `0.0.48-SNAPSHOT`** (2026-07-13): стабильного релиза с нужными чеками (`SpringNullabilityCheck`/`SpringAnnotationAttributeConciseValueCheck`/`SpringNoWhitespaceBeforeCheck`) нет — Maven Central подтверждает 0.0.47 как последний релиз. Все три проверены на проекте, нарушений не нашли. Требует репозитория `repo.spring.io/snapshot` в `com.example.checkstyle.gradle.kts`. **Риск принят осознанно**: SNAPSHOT мутирует без предупреждения (на практике уже увидели два разных SHA у одного номера версии) — вернуться на стабильный релиз, как только выйдет ≥ 0.0.48.

**Обновление версий до последних** (2026-07-13, по запросу «обнови всё»): сверено WebSearch + Maven Central API — WebSearch дважды ошибся (фантомная `jacoco 0.8.16`, стухший «latest» для `errorprone-core`, реально уже стоявшего на последней 2.50.0). Обновлено: `reactor-core` 3.8.6, `jacoco` 0.8.15, `junit` 6.1.2, `spring-boot` 4.1.0 (BOM резолвит тот же `reactor-core`, ни один заявленный breaking change проекта не касается), Java 25 (+ `foojay-resolver-convention` 1.0.0 для авто-докачки JDK тулчейна), `checkstyle` 9.3→13.7.0 (4 мажорных версии, прошёл без единой правки конфига). Уже были на последних: Gradle, Spring Cloud, `spring-dependency-management`, `errorprone-plugin`, `errorprone-core`, `nullaway`, `jspecify`. Каждый шаг проверен `clean check`+`clean build` отдельно.

**Переименования и новые модули** (2026-07-14): (1) `application-reactive/` — во всех трёх CRUD-сервисах, `webflux`+`data-r2dbc`+H2(R2DBC), зеркалит `application/`, новый плагин `com.example.spring-boot-r2dbc-h2-database` (без `spring-boot-h2console` — тот требует Servlet+JDBC, недоступно в webflux/R2DBC); схема R2DBC не создана, частично закрывает «Комбинации technology в `application/`». (2) `data-contract`/`-reactive` → `contract`/`contract-reactive` (пакеты уже были просто `contract`/`contract.reactive`, не менялись) — везде массово. (3) `contract-reactive/` — оба плагина, `com.example.library`+`com.example.reactor`, несмотря на частичное дублирование. (4) `{Entity}InterfaceReactive`/`ServiceInterfaceReactive`/`ControllerInterfaceReactive` → `{Entity}ReactiveInterface`/`ServiceReactiveInterface`/`ControllerReactiveInterface` (см. «Именование») — 9 файлов + usages.

**Полнота R2DBC/JDBC-стартеров** (2026-07-14, источник — Spring Initializr `start.spring.io/metadata/client`, не Maven Central): где Initializr даёт раздельно «Spring Data X» и «X API» (JDBC: `jdbc`+`data-jdbc`; R2DBC: `r2dbc`+`data-r2dbc`) — подключать оба + оба `-test`, даже при транзитивном дублировании. Добавлено: `spring-boot-starter-r2dbc`(`-test`) в `com.example.spring-boot-data-r2dbc`; `spring-boot-starter-jdbc`(`-test`) в `com.example.spring-boot-data-jdbc` (реальный пробел — модуль и так на сыром `NamedParameterJdbcTemplate`). JPA/MongoDB/MongoDB-reactive — уже полные, у них отдельного «plain API» стартера нет вообще. Полная копия референса — также в памяти (`reference_spring_boot_data_starters_matrix.md`).

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

- **Sync base port interface** — `{Entity}Interface` (был `{Entity}Contract` до 2026-07-13) — `NoteInterface`
- **Sync service port interface** — `{Entity}ServiceInterface` (был сам `{Entity}Contract`; переименован и стал наследником `{Entity}Interface`) — `NoteServiceInterface`
- **Sync controller port interface** — `{Entity}ControllerInterface` (новый, 2026-07-13) — `NoteControllerInterface`
- **Reactive base port interface** — `{Entity}ReactiveInterface` (был `{Entity}InterfaceReactive` до 2026-07-14, до этого `{Entity}ContractReactive`) — `NoteReactiveInterface`
- **Reactive service port interface** — `{Entity}ServiceReactiveInterface` (был `{Entity}ServiceInterfaceReactive`) — `NoteServiceReactiveInterface`
- **Reactive controller port interface** — `{Entity}ControllerReactiveInterface` (был `{Entity}ControllerInterfaceReactive`) — `NoteControllerReactiveInterface`
- **Adapter impl (sync и reactive)** — голый `{Entity}Service` в пакете своей технологии, без суффикса технологии в имени класса (был `{Entity}{Tech}Adapter`, например `NoteJpaAdapter`) — `com.example.note.data.jpa.adapter.NoteService`, `com.example.note.data.r2dbc.adapter.NoteService` и т. д.
- **Controller** — `{Entity}Controller` — `NoteController`
- **Mapper interface** — `{Entity}{Tech}MapperContract` — `NoteJpaMapperContract`
- **Mapper impl** — `{Entity}{Tech}Mapper` — `NoteJpaMapper`
- **Spring Data repo** — `{Entity}{Tech}Repository` — `NoteJpaRepository`
- **JPA model class** — `{Entity}Entity` — `NoteEntity`
- **R2DBC model class** — `{Entity}{Tech}Entity` — `NoteR2dbcEntity`
- **MongoDB model class** — `{Entity}Document` — `NoteDocument`
- **MongoDB reactive model class** — `{Entity}ReactiveDocument` — `NoteReactiveDocument`
- **Общий маркер-интерфейс модели сущности** (новое, 2026-07-14) — `{Entity}Persistable`, в `domain/` — `NotePersistable`, `UserPersistable`, `UserNotePersistable`; реализуют все 5 технологических model-классов сущности сразу (единственный общий предок sync+reactive). Осознанно выбрано имя, пересекающееся с реальным `org.springframework.data.domain.Persistable<ID>` — не конфликтует (префикс сущности делает simple name другим), но напоминает: сам маркер в `domain/` остаётся чистым Java без зависимости на `spring-data-commons`, а НАСТОЯЩИЙ `Persistable<ID>` (если понадобится `isNew()`) реализуется отдельно в каждом адаптере — см. «Принятые решения» → «Архитектура»

**Tech**: `Jpa` · `Mongo` · `Jdbc` · `R2dbc` · `MongoReactive` (в имени класса — только для `{Tech}MapperContract`/`{Tech}Mapper`/`{Tech}Repository`/`{Tech}Entity`; адаптеры и порт-интерфейсы с 2026-07-13 больше не несут суффикс технологии в имени, технология различается только пакетом/модулем)

**История**: первым это распространилось на `user-note/` (2026-07-13, три коммита в течение дня), затем в тот же день — на `note/` и `user/`, sync и reactive, по прямому запросу пользователя «обновить весь проект по аналогии». См. «Принятые решения» → «Архитектура» → «Трёхуровневая иерархия портов» — теперь общий паттерн для всех трёх CRUD-сервисов, не исключение

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

Технологии за пределами Spring-стека — уже используемые или отложенные на будущее (без привязки к конкретному модулю) — со справкой, что это за инструмент и как он соотносится с проектом, в логичном порядке освоения — от инструментов разработки к production-инфраструктуре и архитектурным паттернам:

- **Gradle** — система сборки для JVM-проектов: граф задач (`tasks`) с инкрементальным up-to-date tracking и build cache, зависимости — из Maven-репозиториев. Уже используется в проекте (версия — см. «Стек»): Kotlin DSL (`build.gradle.kts`), convention-плагины в `build-logic/convention/` (см. «Принятые решения» → «Архитектура»)
- **testcontainers** — библиотека для интеграционных тестов: поднимает реальные зависимости (БД, брокеры и т. д.) в Docker-контейнерах прямо из теста через JUnit-расширение, вместо моков
- **Docker Compose** — один YAML-файл описывает и запускает несколько контейнеров как одно окружение (общая сеть, `depends_on`); локальный оркестратор dev/test окружения, однохостовый предшественник Kubernetes
- **Kubernetes** — production-оркестратор контейнеров: реплики, self-healing, rolling update, сервис-дискавери поверх кластера машин (Pod/Deployment/Service/ConfigMap)
- **Terraform** — infrastructure as code (HashiCorp, HCL): декларативно описывает желаемое состояние инфраструктуры, сверяет с реальным через state-файл, показывает diff (`plan`) перед `apply`; провайдеры — плагины под API (AWS, GitHub). В проекте — provisioning облачных ресурсов + настройка репозитория (branch protection) через GitHub-провайдер
- **Jenkins** — CI/CD-сервер: по триггеру гоняет пайплайн (build → test → deploy) из `Jenkinsfile`
- **SonarQube** — статический анализ: баги, уязвимости, code smells, покрытие (агрегирует JaCoCo) с quality gate поверх PR; дополняет Checkstyle/NullAway/JaCoCo, типичный шаг в Jenkins-пайплайне
- **Amazon Web Services** — целевая cloud-платформа (EC2, S3, RDS, EKS, Lambda), обычно разворачивается через Terraform
- **Redis** — in-memory key-value: кэш, сессии, rate-limiting, pub/sub; кандидат под Spring Cache (см. «Задачи», ОТЛОЖЕНО)
- **Kafka / RabbitMQ** — message broker: Kafka — топики с retention, consumer groups, event-streaming; RabbitMQ — классическая очередь, сообщение удаляется после подтверждения (см. «Регистрация auth/ ↔ user/»)
- **Elastic Stack** — Elasticsearch (поиск/хранение документов) + Logstash/Beats (логи) + Kibana (визуализация) — альтернатива Loki
- **OAuth2** — RFC 6749: выдачу access-токена делегирует внешнему Authorization Server вместо проверки логина/пароля самим приложением. Роли: Resource Server/Client/Authorization Server. В Spring Boot 4 — три отдельных стартера (`security-oauth2-resource-server`/`-client`/`-authorization-server`); convention-плагины готовы, не применены (см. «Задачи», ОТЛОЖЕНО). **Кто держит роль Authorization Server** — открытый вопрос, две стратегии: (1) self-hosted — свой `spring-boot-starter-security-oauth2-authorization-server` (Spring Security 7, см. «Стек»), полный контроль, но своя инфраструктура/обновления/security-патчи; (2) managed identity provider (IdP) — внешний SaaS, приложение выступает только Resource Server/Client. Managed-варианты: **Okta** (коммерческий, зрелая экосистема, глубокая Spring Security интеграция), **Auth0** (тоже Okta-бренд с 2021, отдельный продукт/API), **AWS Cognito** (если инфраструктура на AWS, см. «Технологии» → AWS), **Azure AD / Entra ID** (аналогично для Azure), **Keycloak** (open-source, self-hosted как Docker-контейнер — компромисс между «свой код» и «managed SaaS», не требует писать Authorization Server самим, но требует эксплуатации). Пересекается с открытым решением «Регистрация auth/ ↔ user/» — выбор здесь определяет, останется ли `auth/` тонким Resource-Server-прокси или полноценным Authorization Server
- **jMolecules** — аннотации для разметки DDD/hexagonal-концепций (`@Entity`, `@ValueObject`, `@AggregateRoot`...); ценность — в связке с ArchUnit-правилами jMolecules, проверяющими, что код соответствует заявленной архитектуре
- **Axon Framework** — CQRS + Event Sourcing: команды порождают события (source of truth, append-only Event Store), read-модели строятся отдельно из тех же событий. Command/Event/Query Bus — внутренняя маршрутизация. В проекте — следующий уровень после текущей hexagonal-архитектуры

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
- **Вся Gradle-конфигурация — на Kotlin DSL** (было Groovy): convention-плагины, оба `settings.gradle.kts`, все `build.gradle.kts`. Рабочий код сервисов остался на Java. Мигрировали пошагово, проверяя `clean build` на каждом шаге; единственное легитимное ограничение (docs.gradle.org → Version Catalogs): precompiled script plugins не видят typed-accessor каталога — решение: локальная `val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")` + `libs.findVersion("key")...` в каждом нуждающемся плагине, без shared-файла (пробовали `Catalogs.kt` — работало, но не по документации). На время миграции `convention/build.gradle.kts` временно держал оба плагина (`kotlin-dsl` + `groovy-gradle-plugin`) для сосуществования старых/новых precompiled-плагинов
- Naming: id не привязанных к папке модуля плагинов (`spring-boot-client-rest`/`-client-web`, а не `restclient`/`webclient`) подбирается по смысловой роли, а не только по алфавиту — иначе риск ложной группировки (`webclient` рядом с `webflux`/`webmvc`, хотя это разные вещи: исходящий клиент vs driving-адаптер). Id плагинов, 1:1 соответствующих папке модуля и реальному Spring Boot starter (`webmvc`, `webflux`, `data-jpa`, `data-jdbc`, `data-r2dbc`, `data-mongodb`, `data-mongodb-reactive`), никогда не переименовываются в отрыве от папки. `domain/` и `contract*/` в это правило не попадают — их плагины (`java`, `library`, `reactor`) называются по зависимости, а не по слою, и папке уже не соответствуют
- `spring-boot-starter`/`-test` — общий для любого Spring Boot модуля, объявлен в `com.example.spring-boot`, не в `spring-boot-application`. Bootable-возможность (`id("org.springframework.boot")`, нужна ради `bootJar`/`resolveMainClassName`) — вторая, ортогональная BOM-цепочке ось иерархии convention-плагинов: см. «Convention plugins — принцип именования и структура» → «Иерархия» ниже
- **Отступ 4 пробела, не табы** — в convention-плагинах (`build-logic/`) и обоих `settings.gradle.kts`: раньше источником истины был `.springjavaformatconfig` (`indentation-style=spaces`), теперь (после удаления `.springjavaformatconfig`/`io.spring.javaformat`, см. «Задачи») — `FileTabCharacterCheck`/`IndentationCheck` в `gradle/checkstyle/checkstyle.xml`; для Java-кода сервисов это жёсткая проверка, для самих Kotlin build-скриптов (`build-logic/`) — по-прежнему только соглашение, Checkstyle Kotlin не проверяет
- **Плагин `idea` — удалён** (был в бывшем `java-domain`, контент которого сейчас — часть `com.example.base`): deprecated в Gradle, будет убран в Gradle 10, и был применён только в `domain/`-модулях (несогласованно, больше нигде в плагинах)
- **Иерархия convention-плагинов** — правила и диаграмма: см. «Convention plugins — принцип именования и структура» ниже. Дополнительно (не показано на диаграмме): `codequality`-плагины не объявляют `id("java")` сами — он нужен только там, где есть java-specific dep-конфигурации (`implementation`, `compileOnly`, `api` и т. д.); если плагин использует только plugin-specific конфигурации (`checkstyle`, `errorprone`, `jacoco` и т. д.) — `id("java")` не нужен (исключение — `com.example.nullaway`, см. ниже). Цикла не возникает: codequality-плагины не применяют `com.example.base`. `jacoco-report-aggregation` — без родителя вообще: плагину не нужен `java` функционально, лишнее не настраивается (autoconfiguration/defaults — не подгонять структуру под единообразие там, где это не даёт реальной пользы)
- Type-safe project accessors (`projects.note.domain` вместо `project(':note:domain')`) — включены через `enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")` в корневом `settings.gradle.kts`; требуют явного `rootProject.name` в обоих `settings.gradle.kts` (иначе Gradle предупреждает о нестабильности между чекаутами — исправлено там же). Фича остаётся incubating в Gradle 9.6.1 (не graduated to stable с версии 7.0), но работает без единого костыля — проверено `./gradlew clean build` по всем сервисам
- `api` в `dependencies {}` — только когда тип используется в публичной сигнатуре модуля, иначе `implementation`; не полагаться на транзитивный путь. Проверено на `reactor-core`: Spring-адаптеры получают `Mono`/`Flux` через собственный BOM, не транзитивно от `contract-reactive`, `api` там был не нужен. `api project(...)` в `contract*/build.gradle.kts` — осознанное исключение, `NoteResponse` и т. п. больше неоткуда получить
- Hexagonal: `domain/` не знает о JPA/MongoDB/Spring; адаптеры зависят от `contract*/` (порт) и явно `implementation(domain)` для типов, которые сами импортируют, не полагаясь на транзитивную утечку `api(domain)` (пересмотрено 2026-07-09, применено во всех 21 driving/driven-адаптере); `api(domain)` в самих `contract*/` остаётся — интерфейсы `{Entity}Contract` возвращают domain-типы в публичной сигнатуре
- **Один контроллер/контракт/адаптер на сущность, не на операцию** (пересмотрено 2026-07-08, было «один класс — одна операция»): `NoteController` с 6 методами вместо `NoteCreateController`/`NoteFindAllController`/... по одному на каждый HTTP-метод; `NoteContract`/`NoteContractReactive` с 6-8 методами вместо отдельного интерфейса на операцию; `NoteJpaAdapter` (и аналогично для остальных 4 driven-технологий) реализует контракт целиком одним классом. Причина пересмотра — количество файлов (~450 в каталоге проекта) признано более серьёзной проблемой, чем размер отдельного класса; слои (`domain/`/`contract*/`/`webmvc/`/`webflux/`/`data-*/`) и сама гексагональная изоляция не затронуты — меняется только гранулярность внутри слоя. Применено по всем трём CRUD-сервисам (`note/`, `user/`, `user-note/`); `mapper/`/`model/`/`repository/` уже были объединены на сущность и не менялись. Проверено `./gradlew clean build` по каждому сервису и по всему проекту
- `service/` — только при координации нескольких портов; для простого CRUD не нужен
- `existsById` в контракте — валидный паттерн, не заменять на `findById`
- `add` ≠ `replace`: JPA — `save(null id)` vs `save(id)`; MongoDB — `insert()` vs `save()`
- `user-note/`: суррогатный `UUID id` (а не составной `userId+noteId` как PK) во всех технологиях. `userId+noteId` — unique constraint/index, не PK. `id` выставлен в `domain/`/контрактах (`UserNoteResponse.id`, `UserNoteFindByIdContract`). Это сняло ограничение Spring Data R2DBC (нет `@EmbeddedId`) — `data-r2dbc/` полноценный model+repository, без `DatabaseClient`; тот же суррогатный `id` позже (2026-07-14) позволил перевести и `data-jdbc/` на `model/`+`repository/` без `@Query` для составного ключа — см. «⚡ Задачи»
- Unique constraint `userId+noteId` по технологиям: JPA — `@Table(uniqueConstraints=...)` (работает, Hibernate создаёт схему); MongoDB — `@CompoundIndex(unique=true)` (работает); у R2DBC/JDBC (`spring-data-relational`) `Table`/`Column` не имеют атрибутов unique/constraints вообще (проверено декомпиляцией, GitHub-исходники `spring-data-relational` подтвердили то же самое повторно 2026-07-14) — constraint для них создаётся только через схему
- **Управление схемой для R2DBC/JDBC — решено в пользу `schema.sql` (2026-07-14)**: добавлен `schema.sql` в `{note,user,user-note}/data-{jdbc,r2dbc}/src/main/resources/` — `id UUID DEFAULT RANDOM_UUID() PRIMARY KEY` (автогенерация на уровне БД, как и в JPA через `@GeneratedValue`, просто явным DDL вместо Hibernate), `NOT NULL` на всех непустых колонках, `UNIQUE(username, email)` для `users`, `UNIQUE(user_id, note_id)` для `user_notes`. Flyway/Liquibase не рассматривались как альтернатива на этом этапе — проект пока на H2, схема простая, три таблицы. Для R2DBC схема реально исполняется при старте (`data-r2dbc` уже подключён к `application-reactive/` — Spring Boot подхватывает `schema.sql` с classpath автоматически для embedded H2); для JDBC схема лежит наготове, но `data-jdbc` по-прежнему ни в одном сервисе не подключён к `application/` (см. «Открытые решения» это больше не блокирует, т.к. вопрос был именно про источник схемы, не про composition root)
- **`user-note/`: `role` — унифицирован до enum везде (2026-07-14, было `String` в R2DBC/JDBC)**: проверено через docs.spring.io (`spring-data-relational` reference, JDBC и R2DBC разделы «Mapping») — Spring Data JDBC маппит enum в `name()` из коробки без конвертера, Spring Data R2DBC по умолчанию тоже конвертирует Enum→String автоматически (кастомный `Converter`/`R2dbcCustomConversions` нужен только для нестандартного маппинга, например ordinal или нативный DB-enum). Ручная `.name()`/`valueOf()`-конвертация в `UserNoteR2dbcMapper`/`UserNoteJdbcMapper` была наследием эпохи сырого JDBC (до перехода на `ListCrudRepository`, см. «Задачи») и оказалась не нужна — `UserNoteR2dbcEntity`/`UserNoteJdbcEntity.role` теперь типа `UserNoteRole`, мэпперы делают прямой passthrough
- `data-jdbc/` — **решение отменено 2026-07-14** (было «намеренно без `model/`/`repository/`: `NamedParameterJdbcTemplate` + сырой SQL, `RowMapper` мапит `ResultSet` сразу в `*Response`»): признано ошибочным — раз есть `spring-boot-starter-data-jdbc` с готовым repository-интерфейсом, свой repository не пишем. Переход на `model/`+`repository/`(`ListCrudRepository`)+`mapper/` по структуре `data-r2dbc/` — **завершено по всем трём сервисам** (`note/`/`user/`/`user-note/`), см. «⚡ Задачи»
- **Трёхуровневая иерархия портов вместо единого `{Entity}Contract`/`{Entity}ContractReactive`** (2026-07-13, из `user-note/` перенесено на `note/`/`user/` по аналогии): `{Entity}Interface[Reactive]` (методы возвращают `Object`) → `{Entity}ServiceInterface[Reactive] extends {Entity}Interface[Reactive]` (конкретные типы: `Boolean`/`{Entity}Response`/`List<...>` sync, `Mono`/`Flux` reactive; реализуют адаптеры, переименованные из `{Entity}{Tech}Adapter` в голый `{Entity}Service`) → `{Entity}ControllerInterface[Reactive] extends {Entity}Interface[Reactive]` (`ResponseEntity<X>`/`Mono<ResponseEntity<X>>` — реализует `{Entity}Controller`). Приём — ковариантное переопределение `Object`: `Mono`/`Flux` — валидные подтипы `Object`, а примитивный `void` — нет, поэтому sync `remove`/`deleteBy*` возвращают `{Entity}Response` вместо `void` (reactive `Mono<Void>` не тронут — сам `Mono` уже объектный тип). `existsById`/`existsBy*` стали реальными `GET`-эндпоинтами. Разбивка методов по типу ключа — только в `user-note/` (см. следующий пункт), у `note/`/`user/` один `id`
- **`user-note/`: операции разведены по типу ключа** (2026-07-13, было 8 методов): суррогатный `userNoteId` и составной `userId+noteId` — отдельные методы вместо одного на операцию, контракт вырос до 15 (4 `existsBy*` + 11), sync и reactive. `findByUserId`/`findByNoteId` сами бросают `UserNotFoundException`/`NoteNotFoundException` (собственные типы в `usernote/domain/`, не переиспользуют `user/`/`note/` — сохраняет hexagonal-изоляцию) — контроллер больше не дублирует проверку
- **PUT (`replace*`) ≠ PATCH (`merge*`)** (2026-07-13, все три сервиса, sync и reactive — закрывает открытый вопрос «PATCH»): `replace*` — полная замена; `merge` — `null` в поле `request` означает «не менять», подставляется существующее значение. Оба бросают `{Entity}NotFoundException` при отсутствии сущности
- `existsById`/`existsBy*` — настоящие `GET`-эндпоинты (`/{id}/exists` и т. д.), не только внутренняя проверка — во всех трёх сервисах, sync и reactive
- **Диспетчеризация нескольких `@GetMapping` на одном пути** — `@RequestParam` не участвует в регистрации мэппинга (`HandlerMapping` не смотрит на аннотации параметров метода, только на атрибуты самой `@GetMapping`/`@RequestMapping`: `path`/`method`/`params`/`headers`/`consumes`/`produces`), поэтому несколько методов с одинаковым `path`+`GET`, но разными `@RequestParam`, — это `Ambiguous mapping` при старте контекста, а не разные маршруты. Разруливается атрибутом `params` (`@GetMapping(params = "userId")`, документировано в `ann-requestmapping.html` → «Parameters, headers», не в `ann-methods/requestparam.html`) — обнаружено и исправлено в `user-note/webmvc/UserNoteController` (2026-07-13, три конфликтующих `findBy*`-метода на одном `@GetMapping`), подтверждено живым `@SpringBootTest`; тот же приём применён в `user-note/webflux` при переводе со старой path-based схемы (`/note/{noteId}`, `/user/{userId}`) на query-параметры
- **Reactive `findById`/`findBy*` теперь бросают `{Entity}NotFoundException` вместо пустого `Mono`** (пересмотрено 2026-07-13 — было ранее зафиксировано как «Reactive-семантика»: см. ниже, актуализировано): проверка существования перенесена из контроллера в сервис/адаптер (`switchIfEmpty(Mono.error(...))`/`flatMap` с `existsBy*`), контроллер больше не делает `switchIfEmpty`/`flatMap`+`existsById` сам — консистентно с тем же переносом в sync
- **`note/webflux`/`user/webflux`: `findAll()` теперь оборачивает результат в `ResponseEntity<Flux<X>>`** (было найдено как расхождение с правилом «`ResponseEntity<T>` в контроллерах» при пересмотре каталога файлов 2026-07-08, исправлено 2026-07-13 попутно при переносе иерархии портов) — раньше возвращался голый `Flux<X>`

### HTTP / Ошибки

- `ResponseEntity<T>` в контроллерах
- `ProblemDetail` (RFC 9457); `spring.mvc.problemdetails.enabled=true`
- Доменные исключения (`NoteNotFoundException`) — в `domain/`

### Маппинг

- Ручной, без MapStruct; маппер — ответственность адаптера
- `Pageable`/`Page`/`Specification` — утечка инфраструктуры, не использовать в контрактах

### Reactive-семантика

- `findById`/`findBy*` (кроме `findAll`) → `Mono<{Entity}Response>`, завершается ошибкой `{Entity}NotFoundException` через `switchIfEmpty(Mono.error(...))`, если пусто (пересмотрено 2026-07-13 — было «пустой `Mono` вместо `Optional.empty()`», без ошибки; проверка перенесена из контроллера в сервис/адаптер, консистентно с sync)
- `findAll` → `Flux<{Entity}Response>`, в контроллере оборачивается в `ResponseEntity<Flux<{Entity}Response>>`
- `remove`/`deleteBy*` → `Mono<Void>` (не меняли — `Mono<Void>`, в отличие от примитивного `void`, уже является валидным подтипом `Object` и не потребовал правки под трёхуровневую иерархию портов)
- `existsById`/`existsBy*` → `Mono<Boolean>`

### Convention plugins — принцип именования и структура

Id называется по подключаемой зависимости/технологии, а не по имени использующего модуля/слоя (исключение — 7 плагинов, 1:1 соответствующих папке модуля и реальному Spring Boot starter: `webmvc`, `webflux`, `data-jpa`, `data-jdbc`, `data-r2dbc`, `data-mongodb`, `data-mongodb-reactive` — тут имя папки важнее алфавита, не переименовывать в отрыве от неё). Плоская структура каталога — см. «Принятые решения» → «Архитектура» выше. Префиксы убраны везде, кроме семей `spring-boot-*`/`spring-cloud-*` — остальные плагины называются голым именем зависимости/инструмента (`checkstyle`, `nullaway`, `jacoco`, `library`, `reactor`, ...).

**Иерархия** — две независимые оси, не одна. Первая — BOM-цепочка наследования (`base → library/reactor/spring-boot → spring-cloud → tech-plugin`): тут родитель всегда один, без исключений. Вторая — «bootable»-возможность (`id("org.springframework.boot")`, нужна ради `bootJar`/`resolveMainClassName`; `spring-boot-starter`/`-test` сюда не относятся — они базовые для любого Spring Boot модуля и объявлены в `com.example.spring-boot`, см. «Принятые решения» → «Архитектура») — она ортогональна первой оси и подключается вторым `id(...)` там, где нужна; это не нарушение правила «1 родитель» по первой оси, а отдельное измерение. Кроме неё, есть агрегирующий `codequality` (собирает 4 плагина — было 5 до удаления `javaformat` 2026-07-13, см. «Задачи»):
```
checkstyle      — id("checkstyle"); без com.example.* родителя; id("java") не нужен;
                  с 2026-07-13 сам несёт configFile/configProperties/spring-javaformat-checkstyle
                  dependency (перенесены из удалённого com.example.javaformat)
nullaway        — id("java-library") + id("net.ltgt.errorprone"); java-library (не java):
                  нужен api("org.jspecify") по документации NullAway; следствие: все модули
                  транзитивно получают java-library через base → codequality → nullaway
jacoco          — id("jacoco"); id("java") не нужен
jacoco-report-aggregation — без родителя вообще (autoconfig)
codequality     — агрегатор 4 плагинов выше

com.example.base (root)  — java + toolchain + junit-jupiter + codequality (1 родитель: codequality)
├── library                — java-library (Gradle core), без Spring
├── reactor                — reactor-core + reactor-tools + reactor-test; родитель base (был library:
│                            java-library теперь приходит транзитивно через nullaway)
└── spring-boot             — io.spring.dependency-management + Spring Boot BOM
    ├── spring-boot-*        (17 технологических плагинов)
    └── spring-cloud         — + BOM spring-cloud-dependencies
        └── spring-cloud-*    (9 технологических плагинов)
```

Дерево выше — только BOM-цепочка. Вторым родителем (bootable-ось, не показана на диаграмме) `spring-boot-application` (сам лежит внутри `spring-boot-*`) дополнительно подключён к 4 плагинам внутри `spring-cloud-*`: `spring-cloud-config-server`, `spring-cloud-eureka-server`, `spring-cloud-gateway-webflux`, `spring-cloud-gateway-webmvc`. `com.example.spring-boot-application` с 2026-07-14 сам несёт `spring-boot-starter-actuator`(`-test`) (было — отдельный опциональный `com.example.spring-boot-actuator`, применялся вручную только в `registry`/`config`/`gateway`) — actuator привязан именно к bootable-оси, а не к BOM-цепочке: он осмысленен только там, где реально есть `bootJar`, не в библиотечных модулях (`data-jpa`, `webmvc` и т. п.), см. «⚡ Задачи».

- `com.example.base` — корень: `java` + toolchain (версия из `.java-version` через `providers.fileContents(...).asText` — Provider API, корректно отслеживается configuration cache) + `com.example.codequality` + `junit-jupiter`/`junit-platform-launcher` + `useJUnitPlatform()`. Раньше codequality и junit жили в отдельных плагинах — теперь стянуты в один `com.example.base`, `domain/` применяет его напрямую
- `com.example.codequality-{errorprone,jacoco,jacoco-report-aggregation,javaformat}` переименованы в `com.example.{...}` без префиксов `java-`/`codequality-`; затем `errorprone` → `nullaway`, добавлен `checkstyle`. `nullaway` использует `id("java-library")` для `api(...)` по документации NullAway; остальные не объявляют java-плагин — приходит от `base`
- `com.example.javaformat` — **удалён 2026-07-13** (см. «Задачи» → «Форматирование без авто-форматтера»): предвиденный сценарий «раздельность `com.example.checkstyle`/`com.example.javaformat` держится на случай будущего отказа от javaformat» — реализовался; `configFile`/`configProperties`/зависимость `spring-javaformat-checkstyle` перенесены в `com.example.checkstyle` без единой правки по модулям-потребителям (они применяют `com.example.checkstyle` транзитивно через `codequality`/`base`, сам путь применения не менялся)
- `com.example.library` (был `java-contract`, затем `java-library`) — 1 родитель `com.example.base`; добавляет Gradle-плагин `java-library`, без Spring
- `com.example.reactor` (был `java-contract-reactive`, затем `java-reactor`) — родитель сменён с `library` на `base`: `java-library` теперь приходит транзитивно через `base → codequality → nullaway`, поэтому явный `library`-родитель избыточен. `reactor-core` + `reactor-tools` + `reactor-test` (`implementation`/`testImplementation`), версии — явные, из `libs.versions.reactor.core` (синхронизирована с тем, что резолвит Spring Boot BOM — см. «Синхронизация версий»), без `io.spring.dependency-management`/BOM — `domain`/`contract*` полностью свободны от Spring. `reactor-tools` сам по себе не активен — нужен явный вызов `ReactorDebugAgent.init()` в коде (обычно в `main()`) или `-javaagent`, просто наличие jar'а в classpath ничего не делает
- `com.example.spring-boot` / `com.example.spring-cloud` — 1 родитель `com.example.base`/`spring-boot` соответственно (`spring-cloud` добавляет BOM `spring-cloud-dependencies`) — `io.spring.dependency-management` + Spring Boot BOM; свой `useJUnitPlatform()` убран как дублирующий родителя. Были `spring-boot-base`/`spring-cloud-base` — суффикс `-base` убран. Плагины, требующие `bootJar` — `spring-boot-application` (`note/`/`user/`/`user-note/`/`auth/`) и 4 standalone `spring-cloud-*` (`config-server`/`eureka-server`/`gateway-webflux`/`gateway-webmvc` — у `registry/`/`config/`/`gateway/` тоже свой `@SpringBootApplication`); вторые 4 получают `id("org.springframework.boot")` вторым родителем через уже существующий `com.example.spring-boot-application`, не дублированием строк
- `com.example.spring-boot-client-rest` / `-client-web` (были `restclient`/`webclient`) — переименованы, чтобы не смешиваться с `webflux`/`webmvc` (server-side driving-адаптеры vs исходящие HTTP-клиенты). `-client-web` — без явного `reactor-test`: подтверждено через POM `spring-boot-starter-webclient-test` — приходит транзитивно (не универсально для всех `-test`-компаньонов, см. «Синхронизация версий» → `reactor-test`)

### Синхронизация версий

- **Spring-экосистема** (Spring Boot, Spring Cloud, dependency-management, spring-javaformat, errorprone-plugin) — единый источник `gradle/libs.versions.toml`. `build-logic` — отдельный included build, подключает тот же `.toml` отдельно в своём `settings.gradle.kts`. В обычных build-скриптах ключи с дефисом дают typed-аксессоры (`libs.versions.spring.boot.get()` и т. п.); внутри precompiled-плагинов (`com.example.{name}.gradle.kts`) недоступны — там `libs.findVersion("key").get().requiredVersion`. `spring-javaformat` (`0.0.48-SNAPSHOT`, было `0.0.47`) пиннит только `spring-javaformat-checkstyle` — Gradle-плагин форматирования убран вместе со своим typed-аксессором, ключ каталога не переименован
- **`com.example.spring-boot`** берёт BOM через `SpringBootPlugin.BOM_COORDINATES` — версия автоматически совпадает с версией плагина. **`com.example.spring-cloud`** собирает координаты вручную — у Spring Cloud нет своего Gradle-плагина, `BOM_COORDINATES`-аналога не существует, риск «два источника одной версии» для него неизбежен, для `spring-boot` — устранён сознательно
- **Инструменты codequality** (`jspecify`, `errorprone-core`, `nullaway`, `jacoco`, `checkstyle`) — тоже через каталог, не зашиты текстом в плагинах — раньше были разбросаны как строковые литералы
- **junit-jupiter / junit-platform** — оба явно из каталога в `com.example.base`. Реальный найденный баг: каталог был запинен на `junit-jupiter = "5.12.2"` (унаследовано от домена), но Spring Boot фактически управляет JUnit 6 — расхождение вылезло, когда `spring-boot`-плагины стали наследовать `java` и получили одновременно 5.x и 6.x — `TestEngine with ID 'junit-jupiter' failed to discover tests`. Исправлено на `6.1.2`/`6.1.2` (в JUnit 6 Platform/Jupiter унифицировали нумерацию)
- **reactor-core** (`libs.versions.reactor.core`, сейчас `3.8.6`) — два источника: `com.example.reactor` (`domain`/`contract*`) фиксирует явно, Spring-адаптеры получают через `mavenBom(SpringBootPlugin.BOM_COORDINATES)`. Gradle берёт старшую версию при конфликте, но синхронизация ручная — при апгрейде Spring Boot сверяться, что резолвит новый BOM (`./gradlew :note:webflux:dependencies --configuration compileClasspath | grep reactor-core`), тот же риск у `junit-jupiter`/`junit-platform`
- **reactor-test** — explicit `testImplementation("io.projectreactor:reactor-test")` убран из `spring-boot-webflux`/`-data-r2dbc`/`-data-mongodb-reactive` — приходит транзитивно через `*-test`-компаньоны (проверено эмпирически). Не универсально: `spring-boot-starter-webclient-test` содержит `reactor-test`, `-graphql-test` — нет, каждый `-test`-компаньон нужно проверять отдельно. `spring-cloud-gateway-webflux` своего `-test`-компаньона не имеет вообще — `reactor-test` там ниоткуда не приходит, оставлено осознанно (нет реактивных тестов в `gateway/`)
- **Gradle** — версия зафиксирована в `gradle/wrapper/gradle-wrapper.properties`; CI (`./gradlew`) наследует её автоматически, отдельной синхронизации не требует
- **Java** — единственный источник `.java-version` (корень репозитория): CI читает его через `actions/setup-java@v4` (`java-version-file`), Gradle — через `toolchain` в `com.example.base`. Читается через `providers.fileContents(rootProject.layout.projectDirectory.file('.java-version')) .asText.get().trim().toInteger()` (Provider API), не `rootProject.file(...).text` напрямую — корректно отслеживается configuration cache; применяется почти во всех модулях транзитивно

### Стиль кода

- Импорты: `java.*` → `javax.*` → `*` → `org.springframework.*`; пустая строка между группами
- `@NullMarked` на каждый `package-info.java`
- `@Nullable` из `org.jspecify.annotations`
- **`@NullUnmarked` на классах Entity/Document** (пересмотрено 2026-07-14, было `@SuppressWarnings("NullAway.Init")` на `protected` no-arg конструкторе): decompилирован `spring-data-commons-4.1.0` (`PreferredConstructorDiscoverer$Discoverers$1`) — при наличии у класса хотя бы одного no-arg конструктора и отсутствии `@PersistenceCreator` framework **всегда** выбирает именно no-arg конструктор для чтения из БД (`Field.set(...)` рефлексией напрямую, минуя args-конструкторы), независимо от их числа — то есть NullAway абсолютно корректно ловит «поле не инициализировано» сразу после `protected EntityX()`, просто не видит пост-конструкторную рефлексию. Suppression на одном конструкторе — точечный костыль под конкретный симптом; `@NullUnmarked org.jspecify.annotations` на самом классе — правильный псевдоним «эта модель заполняется framework'ом вне контроля null-checker'а», отключает null-checking для всего класса разом. Явные `@Nullable` (например, на `id` до присвоения БД) внутри `@NullUnmarked`-класса по спецификации JSpecify по-прежнему учитываются — проверка на границе (`Objects.requireNonNull(entity.getId())` в мэпперах) не потеряна. Применено ко всем 15 model-классам (`{Entity}Entity`/`{Entity}Document`/`{Entity}ReactiveDocument`/`{Entity}R2dbcEntity`/`{Entity}JdbcEntity` × `note`/`user`/`user-note`), `./gradlew clean check`+`clean build` — чисто. Альтернатива `@PersistenceCreator` на args-конструкторе (убрать no-arg вовсе, получить нормальный constructor binding) — рассмотрена и отклонена пользователем в пользу `@NullUnmarked`
- **Jakarta Bean Validation для Entity/Document — доступность подключена везде, аннотации не расставлены** (было «принято к реализации, не начато» до 2026-07-14): `spring-boot-starter-validation`(`-test`) — в `com.example.spring-boot` (полная валидация с Hibernate Validator + Spring-интеграцией во всех Spring Boot модулях), `jakarta.validation-api` (сама спецификация, без реализации/Spring) — в `com.example.base` (доступна и в `domain/`, см. «⚡ Задачи»). Не сделано: `jakarta.validation.constraints.*` (`@NotNull`/`@NotBlank` и т. п.) по-прежнему не расставлены ни на одном из 15 `Entity`/`Document`-классов и ни на одном из `domain/`-record'ов (`NoteRequest`/`UserRequest`/`UserNoteRequest`), `@Valid` не используется ни в одном контроллере/адаптере — сам механизм присутствует в classpath везде, но нигде не применяется. Отдельный от JSpecify/NullAway механизм: там — статический null-checking на этапе компиляции, тут — runtime-валидация (`@Valid`/`Validator`) — не взаимозаменяемы, дополняют друг друга. Не реализовывать расстановку аннотаций до отдельного запроса
- **`@Service` на классах-адаптерах, `@Repository` на Spring Data repository-интерфейсах** (2026-07-14): найдено и исправлено реальное расхождение — все 15 классов `{Entity}Service` (во всех 5 driven-технологиях × `note`/`user`/`user-note`) были помечены `@Repository` вместо `@Service` (унаследовано от переименования `{Entity}{Tech}Adapter` → `{Entity}Service`, аннотацию при этом не поправили). Исправлено на `@Service` во всех 15 файлах. Дополнительно — все 15 `{Entity}{Tech}Repository`-интерфейсов (`JpaRepository`/`MongoRepository`/`ReactiveMongoRepository`/`ReactiveCrudRepository`/`ListCrudRepository`) вообще не имели явной аннотации (Spring Data обнаруживает их и так, через маркерный интерфейс, а не component-scan по стереотипу) — добавлен explicit `@Repository` на все 15 для ясности слоя. `./gradlew clean check` — чисто
- Промежуточная переменная перед `return`, не inline в `.body()`
- **Длина строки — не ограничена** (пересмотрено 2026-07-13, было «до 120 символов») — переносы только вручную, авто-форматтер не используется (см. «Задачи» → «Форматирование без авто-форматтера»)
- **10 персональных правил форматирования** (реализовано 2026-07-13, пункты 1–7/9/10 из 10; пункт 8 отложен, см. «Открытые решения»): неограниченная длина строки без авто-переноса; никогда не пустая строка перед закрывающей скобкой, **кроме** пустого тела метода/класса/record — там, наоборот, обязательна ровно одна пустая строка между `{` и `}` (2 модуля `RegexpMultiline`: первый требует непустого нестандартного символа перед пустой строкой — `[^\s{]` — чтобы не путать «пустая строка после реального кода» с «пустая строка — единственное содержимое тела», второй явно требует эту пустую строку там, где тело иначе было бы `{}`/`{\n}`); всегда `\n` после последней закрывающей скобки (`NewlineAtEndOfFileCheck`, уже был в `SpringChecks`); ровно одна пустая строка между методами и не более одной пустой строки подряд где-либо (`EmptyLineSeparator`, `allowMultipleEmptyLines=false` + `allowMultipleEmptyLinesInsideClassMembers=false`); аннотации перед полями/классами/интерфейсами/методами — всегда каждая на своей строке (`SpringAnnotationLocationCheck`, уже был в `SpringChecks`); аннотации перед параметрами — никогда не переносятся (тот же чек сознательно не проверяет `PARAMETER_DEF`); отступ 4 пробела без табов (`IndentationCheck`/`FileTabCharacterCheck`). **Ловушка**: сообщения `RegexpMultiline`/любых Checkstyle-модулей форматируются через `java.text.MessageFormat` — литеральные `{`/`}` в тексте `message` ломают парсинг (`can't parse argument number`), в тексте сообщения фигурные скобки нужно либо избегать, либо экранировать одинарными кавычками

---

## Открытые решения

- **Порядок методов реализации = порядок объявления в интерфейсе** (пункт 8 из 10 персональных правил форматирования пользователя — остальные 9 реализованы 2026-07-13, см. «Стиль кода» и «Задачи») — должен быть жёстко проверяемым, но готового Checkstyle-правила под него нет: нужна семантическая привязка к конкретному реализуемому интерфейсу, которой синтаксическому Checkstyle не хватает без резолвинга типов (Checkstyle читает один файл за раз, без резолва типов — кросс-файловая сверка «класс implements X» → «прочитать файл X» в него плохо ложится). Два кандидата, оба не требуют переизобретения резолвинга типов с нуля: (1) кастомный Error Prone `BugChecker` — в проекте уже есть `net.ltgt.errorprone`/NullAway инфраструктура, Error Prone в отличие от Checkstyle работает поверх резолвленных `javac`-символов; (2) JUnit-тест на `com.github.javaparser:javaparser-core` (актуальная версия — проверить WebSearch на момент реализации), сравнивающий порядок объявления методов в файле интерфейса и файле реализации — `Class.getDeclaredMethods()` через reflection не подходит, порядок не гарантирован спецификацией JVM. Не реализовывать до отдельного запроса
- **Стратегия активации адаптеров** — `@Profile("jpa")` _(склонение)_ vs отдельные `application-jpa/`
- **Регистрация auth/ ↔ user/** — Lazy / Sync / Events (Kafka)
- **Каталог `data/` на уровне сервиса** — группировать `data-jpa`/`data-jdbc`/`data-r2dbc`/`data-mongodb`/`data-mongodb-reactive` в подкаталог `data/` внутри каждого сервиса (только каталог, не Gradle-модуль — нет `data/build.gradle.kts`) — вопрос поднят 2026-07-14, явного решения не было. Два варианта: (1) оставить как есть (без изменений) — рекомендую: `data/` не даёт функциональной пользы (не Gradle-модуль, не агрегирует таски, не добавляет общую конфигурацию), а правило «id плагина 1:1 папке модуля» и так уже соблюдено на текущем плоском уровне; цена реорганизации реальная — `settings.gradle.kts` (30 строк `include(...)` на 3 сервиса), typesafe project accessors меняют форму (`projects.note.dataJpa` → `projects.note.data.dataJpa`) везде, где driving-адаптеры/`application` зависят от driven, плюс сбивает пути в идущем построчном пересмотре «Каталог файлов проекта» (~50 уже вычитанных строк, часть с `[DONE]`); прецедент — «Composite build на границе сервисов» ниже отклонён по той же логике «нет текущего драйвера, цена больше выигрыша»; (2) сделать группировку — плюс только визуальный (короче листинг `ls note/`, зримое отделение driven от driving/composition-root). Пересмотреть, если появится конкретный драйвер (агрегирующий тул/задача, которой нужна именно каталожная группировка, а не просто naming convention)
- **Возврат мутирующего use case** — DTO _(склонение)_ vs `void`
- **`@Transactional` на методах адаптера** — решение было зафиксировано, но при пересмотре CRUD-сервисов (2026-07-07) выяснилось, что оно не реализовано ни в одном адаптере ни одной технологии ни одного сервиса. Решить: реализовать по всем адаптерам (~150 файлов) или снять решение как устаревшее (Spring Data репозитории уже транзакционны на уровне отдельного метода)
- **Комбинации technology в `application/`** — частично закрыто 2026-07-14: добавлен `application-reactive/` (`webflux`+`data-r2dbc`+H2 через R2DBC, новый плагин `com.example.spring-boot-h2-r2dbc-database`) во всех трёх CRUD-сервисах, зеркалит `application/` (`webmvc`+`data-jpa`+H2). Схема для R2DBC теперь есть (`schema.sql`, см. «Принятые решения» → «Архитектура» — вопрос «Управление схемой для R2DBC/JDBC» закрыт 2026-07-14), `application-reactive` должна работать end-to-end, не только проходить `contextLoads()` — не перепроверено отдельным тестом с реальными запросами. MongoDB (sync и reactive) — намеренно не подключена, рассматривается как будущая замена JPA/JDBC/R2DBC, а не третья одновременная связка (autoconfiguration не разводит два driven-технологии в одном контексте однозначно)
- **`user/`: `findByEmail`/`findByUsername` без HTTP-входа** — доведены до всех driven-адаптеров (jpa/mongodb/mongodb-reactive/r2dbc/jdbc), но не выведены в `webmvc`/`webflux`. Варианты: оставить как задел под будущий `auth/` (поиск пользователя при логине), добавить контроллеры уже сейчас, или убрать как неиспользуемое до появления реального потребителя
- **Javadoc-комментарии в `package-info.java`** — сейчас ни один `package-info.java` в проекте не содержит package-level Javadoc (`/** ... */`), только `@NullMarked`; можно сгенерировать в будущем — дополнительно поможет соответствовать конвенциям Spring JavaFormat/Checkstyle, т. к. `gradle/checkstyle/checkstyle.xml` сейчас содержит excludes на javadoc-проверки (см. «Каталог файлов проекта» → `gradle/`) — включение этих проверок потребует такого комментария в каждом пакете
- **Composite build на границе сервисов** — обсуждалось при пересмотре `settings.gradle.kts` (2026-07-08): каждый сервис (`note/`, `user/`, `user-note/`, `auth/`, `config/`, `gateway/`, `registry/`) мог бы подключаться в корневой `settings.gradle.kts` через `includeBuild(...)` (composite build) вместо `include(...)`, оставляя `include(...)` для модулей внутри каждого сервиса без изменений. Проверено: ни один сервис сейчас не ссылается на модули другого (`grep` по `project(...)`/`projects.*` — пусто), значит typesafe-accessor'ы не пострадали бы. Цена: 7 новых `settings.gradle.kts` с дублированным `pluginManagement`/version-catalog boilerplate + ручная агрегация `build`/`check`/`clean` в корне вместо бесплатной от Gradle. Решено не делать сейчас — нет текущего драйвера (независимое CI/версionирование, план на разъезд по репозиториям), цена ощутимо больше выигрыша; пересмотреть, если такой драйвер появится

---

## Каталог файлов проекта

> Составлено 2026-07-08: полный проход по всем 452 git-отслеживаемым файлам репозитория, папка за папкой, свежим взглядом и со сверкой с этим документом. Статус `[REVIEW]` — для каждого существующего файла по умолчанию (правило «Пересмотр решений»: ничего не считается окончательно принятым при первом просмотре); `[DONE]` проставляется только точечно, когда конкретный файл явно обсуждён и закрыт в разговоре. `[ADD]` — файла сейчас нет, но он нужен для реальной работоспособности/проверяемости сервиса (в основном — тесты, которых сейчас нет вообще ни в одном из трёх ГОТОВО-сервисов, и схема БД для R2DBC/JDBC).
>
> Порядок — дерево построено обычным способом (папки над файлами, алфавит на каждом уровне: `### сервис/` → `#### модуль/` → файлы), затем перевёрнуто целиком, рекурсивно на каждом уровне — меняется местами не только порядок внутри группы, но и сами группы («папки» / «файлы»). Отсюда на верхнем уровне сначала «Корень репозитория» (файлы, лежавшие в дереве ниже всех папок), затем сами каталоги-сервисы в обратном алфавите (`user-note/` → `user/` → `registry/` → `note/` → `gradle/` → `gateway/` → `config/` → `build-logic/` → `auth/` → `.github/`). Та же логика — внутри каждого модуля: `build.gradle.kts` (файл на уровне модуля, вне `src/`) идёт первым, перед перевёрнутым деревом `src/`; в `gradle/` — `libs.versions.toml` первым, затем `wrapper/`, затем `checkstyle/`; в `build-logic/` — «корневые файлы» (`settings.gradle.kts`, `convention/build.gradle.kts`) первыми, затем список precompiled-плагинов. Находки/предлагаемые файлы — вне этой логики, расставлены по смыслу (находки первыми, `[ADD]`-раздел последним — это не git-отслеживаемые файлы). Внутри листового пакета (без вложенных папок) — файлы в обратном алфавитном порядке по имени. Ровно 2 уровня заголовков: `### сервис/` → `#### модуль/`; перед каждым `####` — пустая строка; всё глубже — плоский список под заголовком модуля, без под-заголовков по подпакетам. Формат каждой строки: `путь-от-корня-репозитория` — статус — комментарий — путь всегда полный (не только имя файла), строка самодостаточна без чтения окружающих заголовков.
>
> **Продолжение пересмотра в новой сессии:** идёт построчный пересмотр каждого файла, ровно один файл за раз — разобрать → дождаться личного утверждения пользователем → `[DONE]` → следующая строка `[REVIEW]` сверху вниз; не пакетами, не забегая вперёд по дереву (см. «⚡ Задачи» → «Текущая работа»).

### Корень репозитория (8 файлов; было 9 — `.springjavaformatconfig` удалён 2026-07-13, см. «Задачи»)
- `settings.gradle.kts` — [DONE] — `includeBuild`, `TYPESAFE_PROJECT_ACCESSORS`, `rootProject.name`; состав `include(...)` совпадает со статусами в «Задачах»; порядок блоков — инфраструктурные сервисы (`auth`/`config`/`gateway`/`registry`) выше CRUD (`note`/`user`/`user-note`), прямой алфавит внутри каждой группы; дважды подтверждено `./gradlew clean check` (BUILD SUCCESSFUL). С 2026-07-13 дополнительно `plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0" }` — нужен для авто-докачки JDK 25 тулчейна (см. «Задачи» → «Обновление версий»), версия захардкожена прямо в блоке (не через `libs.versions.toml` — каталог ненадёжно доступен на этапе `plugins{}` в `settings.gradle.kts`, стандартная практика для этого плагина)
- `gradlew.bat` — [DONE] — стандартный сгенерированный wrapper-скрипт (пересобран при апгрейде Gradle до 9.6.1 в этой сессии, не редактировался руками); заголовочный комментарий внутри «gradlew startup script» вместо «Gradle startup script» — так генератор называет по имени исполняемого файла, не опечатка
- `gradlew` — [DONE] — стандартный сгенерированный wrapper-скрипт (пересобран при апгрейде до 9.6.1 в этой сессии); executable-бит `755` и LF-окончания подтверждены, соответствует `.gitattributes`
- `gradle.properties` — [DONE] — `configuration-cache.problems=fail` (ужесточено с `warn` в этой сессии): `./gradlew clean check` и `./gradlew clean build` дважды каждый прошли чисто, без единого предупреждения о configuration cache, включая `bootJar` всех 7 `application/`-модулей; `parallel`/`caching`/`configureondemand`/`configuration-cache`/`configuration-cache.parallel` — разумный набор для мультимодульного проекта, замечаний нет
- `CLAUDE.md` — [DONE] — сам документ; самореференциально по природе (см. «Всё временно» в шапке) — статус означает «строка каталога точна», не «содержимое неизменно»; записи `[DONE]` намеренно не удаляются после утверждения (см. «Пересмотр решений»)
- `.java-version` — [DONE] — `25` (было `21` до 2026-07-13, обе LTS); единственный источник версии Java, читается через toolchain в `com.example.base` и `java-version-file` в CI (`.github/workflows/gradle.yml`); проверено по всему репозиторию — хардкода версии больше нигде нет, `.idea/` не отслеживается git
- `.springjavaformatconfig` — [REMOVED] 2026-07-13 — единственный потребитель (`io.spring.javaformat` Gradle-плагин) удалён вместе с ним, см. «Задачи» → «Форматирование без авто-форматтера»; строка сохранена для истории, не удалена целиком (записи о принятых в прошлом файлах не стираются молча)
- `.gitignore` — [DONE] — стандартный Spring Initializr `.gitignore` (HELP.md, `.gradle`, `build/`, STS/IntelliJ/NetBeans/VS Code) + 2 добавленных вручную раздела под AI-инструменты (`.claude/`, `.junie/` — JetBrains AI); `.claude/` реально существует локально, `.junie/` пока не создавался — оба корректно не отслеживаются git
- `.gitattributes` — [DONE] — LF для `gradlew`, CRLF для `*.bat`, binary для `*.jar`

### user-note/ (87 файлов, было 84 — добавлены `UserNotePersistable` (domain) + 2× `schema.sql` (data-jdbc/data-r2dbc) 2026-07-14 + 18 предложенных)

**Все статусы — `[REVIEW]`**: согласно правилу «Пересмотр решений» в CLAUDE.md, ничто не считается окончательно принятым при первом просмотре; `[DONE]` ставит только человек. **2026-07-13** (в течение одного дня, 4 коммита): `UserNoteContract`/`UserNoteContractReactive` заменены на трёхуровневую иерархию (`UserNoteInterface`+`UserNoteServiceInterface`+`UserNoteControllerInterface`, и reactive-аналоги) — этот паттерн зародился именно здесь и затем в тот же день перенесён в `note/`/`user/` (см. «Принятые решения» → «Архитектура»). Контракт вырос с 8 до 15 методов (разбивка по `userNoteId`/`userId+noteId`), добавлены `merge()` (PATCH) и реальные `existsBy*`-эндпоинты, исправлен `Ambiguous mapping` в обоих контроллерах (webmvc и webflux) через `params`.

#### Главные находки

1. **Тестов нет вообще, кроме `UserNoteApplicationTests.contextLoads()`.** Не специфика user-note — `note/`/`user/` в том же состоянии.
2. **Управления схемой для R2DBC/JDBC по-прежнему нет** — подтверждает открытый вопрос «Управление схемой для R2DBC/JDBC».
3. **`@Transactional` отсутствует во всех методах всех 5 driven-адаптеров** — подтверждает открытый вопрос «`@Transactional` на методах адаптера»: решение по-прежнему не реализовано.
4. **Реальное расхождение (стиль, не семантика), сохранилось после переноса иерархии портов**: метод `add`/`create` в `UserNoteService` (`data-mongodb`) строит/использует `document` иначе, чем аналогичный метод в `note/data-mongodb`/`user/data-mongodb` — там паттерн «сначала `document = mapper.toNewDocument(request)`, потом `insert(document)`, в ответе — тот же `document`»; в `user-note` — `document` строится из значения, возвращённого `insert(...)`, с инлайновым вызовом маппера внутри `insert(...)`. Поведенчески идентично, стилистически не единообразно между тремя сервисами — не трогали намеренно (вне скоупа задачи).
5. **Внутри самого `user-note`** все 5 driven-технологий ведут себя единообразно по каждой из 15 операций (было 8 до 2026-07-13) — сигнатуры, обработка отсутствия (`throw`/`Mono.error` вместо `Optional`/пустого `Mono`), порядок «проверить существование → бросить `NotFoundException` → обновить» в `replace*`/`merge*` — везде одинаковы, включая reactive.
6. **Новая находка (2026-07-13): метод создания называется `create` в `user-note`, но `add` в `note/`/`user/`** — тройка сервисов теперь расходится по имени этого одного метода порта (остальные 6 общих метода — `existsById`/`findAll`/`findById`/`replace`/`merge`/`remove` в `note/`/`user/` — совпадают буквально). Причина — `user-note` первым прошёл переименование `add`→`create` ещё в исходном коммите 2026-07-13 13:00 (до появления трёхуровневой иерархии), `note/`/`user/` при переносе паттерна сохранили исходное имя `add` (см. «Принятые решения» → «Именование»/`add` ≠ `replace`»). Не унифицировано — решить, привести ли `user-note` к `add` или наоборот.

#### user-note/webmvc/ (5 файлов)
- `user-note/webmvc/build.gradle.kts` — [DONE] — `spring-boot-webmvc` + `implementation(contract)` + `implementation(domain)` (добавлено 2026-07-09, см. «Принятые решения» → «Hexagonal»)
- `user-note/webmvc/src/main/java/com/example/usernote/webmvc/package-info.java` — [DONE] — `@NullMarked`
- `user-note/webmvc/src/main/java/com/example/usernote/webmvc/UserNoteExceptionHandler.java` — [REVIEW] — `@ControllerAdvice extends ResponseEntityExceptionHandler`, без `setTitle` — идентично `NoteExceptionHandler`/`UserExceptionHandler` в webmvc `note/`/`user/`; не затронуто переносом иерархии портов
- `user-note/webmvc/src/main/java/com/example/usernote/webmvc/UserNoteControllerInterface.java` — [REVIEW] — новый (2026-07-13): `extends UserNoteInterface`, 15 методов типа `ResponseEntity<X>`/`ResponseEntity<Boolean>`; реализует `UserNoteController`
- `user-note/webmvc/src/main/java/com/example/usernote/webmvc/UserNoteController.java` — [REVIEW] — теперь `implements UserNoteControllerInterface`; guard-проверки убраны (перенесены в сервис); `existsBy*` стали реальными `GET`-эндпоинтами (`/{userNoteId}/exists`, `/exists?userId=`, `/exists?noteId=`, `/exists?userId=&noteId=`); добавлен `params` на 3 `@GetMapping` (`findByUserId`/`findByNoteId`/`findByUserIdAndNoteId`) — без него был `Ambiguous mapping` при старте контекста, подтверждено живым `@SpringBootTest`; добавлены `mergeByUserNoteId`/`mergeByUserIdAndNoteId` (PATCH)

#### user-note/webflux/ (5 файлов)
- `user-note/webflux/build.gradle.kts` — [DONE] — `spring-boot-webflux` + `implementation(contractReactive)` + `implementation(domain)` (добавлено 2026-07-09, см. «Принятые решения» → «Hexagonal»)
- `user-note/webflux/src/main/java/com/example/usernote/webflux/package-info.java` — [DONE] — `@NullMarked`
- `user-note/webflux/src/main/java/com/example/usernote/webflux/UserNoteExceptionHandler.java` — [REVIEW] — `@RestControllerAdvice` без наследования, `ProblemDetail` + `setTitle(...)` — идентично паттерну `NoteExceptionHandler`/`UserExceptionHandler` в webflux `note/`/`user/`; не затронуто переносом иерархии портов
- `user-note/webflux/src/main/java/com/example/usernote/webflux/UserNoteControllerReactiveInterface.java` — [REVIEW] — новый (2026-07-13): `extends UserNoteReactiveInterface`, `Mono<ResponseEntity<X>>`/`ResponseEntity<Flux<UserNoteResponse>>` для `findByUserId`/`findByNoteId`; реализует `UserNoteController`
- `user-note/webflux/src/main/java/com/example/usernote/webflux/UserNoteController.java` — [REVIEW] — теперь `implements UserNoteControllerReactiveInterface`; переведён со старой path-based схемы (`/note/{noteId}`, `/user/{userId}`, `/{userId}/{noteId}`) на query-параметры с `params`-диспетчеризацией — консистентно с sync webmvc-версией; `existsBy*` стали реальными `GET`-эндпоинтами; добавлены `mergeByUserNoteId`/`mergeByUserIdAndNoteId`; `findByUserId`/`findByNoteId` больше не голый `Flux` — обёрнуты в `ResponseEntity<Flux<X>>`

#### user-note/domain/ (9 файлов, было 8 — добавлен `UserNotePersistable` 2026-07-14)
- `user-note/domain/build.gradle.kts` — [REVIEW] — `id("com.example.base")` — плоский Java-модуль, без Spring
- `user-note/domain/src/main/java/com/example/usernote/domain/package-info.java` — [DONE] — `@NullMarked`
- `user-note/domain/src/main/java/com/example/usernote/domain/UserNoteRole.java` — [REVIEW] — `enum UserNoteRole { OWNER, EDITOR, VIEWER }`
- `user-note/domain/src/main/java/com/example/usernote/domain/UserNoteResponse.java` — [REVIEW] — `record UserNoteResponse(UUID id, UUID userId, UUID noteId, UserNoteRole role)` — `id` выставлен явно (суррогатный ключ), соответствует принятому решению
- `user-note/domain/src/main/java/com/example/usernote/domain/UserNoteRequest.java` — [REVIEW] — `record`, чистая Java; поля не `@Nullable`, но `merge()`-логика в адаптерах всё равно проверяет их на `null` в рантайме — jspecify-аннотации не влияют на десериализацию Jackson, реальный `null` в JSON возможен независимо от типа (см. «Принятые решения» → «Архитектура» → PUT ≠ PATCH)
- `user-note/domain/src/main/java/com/example/usernote/domain/UserNoteNotFoundException.java` — [REVIEW] — Два конструктора (`UUID id` и `UUID userId, UUID noteId`) — используются в webmvc/webflux/всех технологиях единообразно; сообщение — `"UserNote not found: userId=" + userId + ", noteId=" + noteId` (именованные поля, не просто `"/"`, для читаемости) — чистая Java, без зависимостей на инфраструктуру
- `user-note/domain/src/main/java/com/example/usernote/domain/UserNotFoundException.java` — [REVIEW] — новый (2026-07-13, коммит переименования Contract→Service): конструктор от `UUID userId`; используется в `findByUserId`/`existsByUserId`-проверке (throw при отсутствии записей по пользователю)
- `user-note/domain/src/main/java/com/example/usernote/domain/NoteNotFoundException.java` — [REVIEW] — новый, аналогично: конструктор от `UUID noteId`; используется в `findByNoteId`. **Не совпадает** с `note/domain/NoteNotFoundException` — сознательно собственный тип, чтобы `user-note/` не тянул зависимость на чужой домен `note/` (сохраняет hexagonal-изоляцию между сервисами)
- `user-note/domain/src/main/java/com/example/usernote/domain/UserNotePersistable.java` — [REVIEW] — новый (2026-07-14): общий маркер-интерфейс для всех 5 model-классов `user-note/` (`@Nullable UUID getId()` + `UUID getUserId()` + `UUID getNoteId()` + `UserNoteRole getRole()` — теперь единый тип `UserNoteRole` во всех технологиях, см. «Принятые решения» → «Архитектура»), см. «Именование»

#### user-note/data-r2dbc/ (11 файлов, было 10 — добавлен `schema.sql` 2026-07-14)
- `user-note/data-r2dbc/build.gradle.kts` — [DONE] — `spring-boot-data-r2dbc` + `implementation(contractReactive)` + `implementation(domain)` (добавлено 2026-07-09, см. «Принятые решения» → «Hexagonal»)
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/repository/package-info.java` — [DONE] — `@NullMarked`
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/repository/UserNoteR2dbcRepository.java` — [REVIEW] — `ReactiveCrudRepository<UserNoteR2dbcEntity, UUID>` + derived queries; добавлены `existsByUserId`/`existsByNoteId` (2026-07-13)
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/model/package-info.java` — [DONE] — `@NullMarked`
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/model/UserNoteR2dbcEntity.java` — [REVIEW] — `{Entity}{Tech}Entity`, `@Column("role") private UserNoteRole role` (было `String` до 2026-07-14 — см. «Принятые решения» → «Архитектура»: Spring Data R2DBC маппит enum в строку из коробки, ручной конвертации не требовалось); unique-constraint на `user_id+note_id` теперь через `schema.sql` (см. ниже), не аннотации. Реализует `UserNotePersistable`
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/mapper/package-info.java` — [DONE] — `@NullMarked`
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/mapper/UserNoteR2dbcMapperContract.java` — [REVIEW]
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/mapper/UserNoteR2dbcMapper.java` — [REVIEW] — упрощён 2026-07-14: прямой passthrough `request.role()`/`entity.getRole()`, ручная `.name()`/`valueOf()`-конвертация убрана вместе с переходом `role` на `UserNoteRole` в entity
- `user-note/data-r2dbc/src/main/resources/schema.sql` — [REVIEW] — новый (2026-07-14): `user_notes` с `id UUID DEFAULT RANDOM_UUID() PRIMARY KEY`, `user_id`/`note_id`/`role` `NOT NULL`, `UNIQUE(user_id, note_id)` — закрывает открытый вопрос «Управление схемой для R2DBC/JDBC», реально исполняется при старте `application-reactive/`
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/adapter/package-info.java` — [DONE] — `@NullMarked`
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/adapter/UserNoteService.java` — [REVIEW] — переименован из `UserNoteR2dbcAdapter` (2026-07-13): `implements UserNoteServiceReactiveInterface`, реализует все 15 операций (было 8); `findByUserNoteId`/`findByUserIdAndNoteId`/`replace*`/`merge*`/`deleteBy*` сами бросают `UserNoteNotFoundException` через `switchIfEmpty`/`Mono.error`; `findByUserId`/`findByNoteId` проверяют `existsByUserId`/`existsByNoteId` через `flatMapMany`

#### user-note/data-mongodb-reactive/ (10 файлов)
- `user-note/data-mongodb-reactive/build.gradle.kts` — [DONE] — `spring-boot-data-mongodb-reactive` + `implementation(contractReactive)` + `implementation(domain)` (добавлено 2026-07-09, см. «Принятые решения» → «Hexagonal»)
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/repository/package-info.java` — [DONE] — `@NullMarked`
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/repository/UserNoteMongoReactiveRepository.java` — [REVIEW] — `ReactiveMongoRepository<UserNoteReactiveDocument, UUID>` + derived queries; добавлены `existsByUserId`/`existsByNoteId` (2026-07-13)
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/model/package-info.java` — [DONE] — `@NullMarked`
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/model/UserNoteReactiveDocument.java` — [REVIEW] — `{Entity}ReactiveDocument`, `@CompoundIndex(unique = true)` на `userId+noteId` — соответствует принятому решению
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/mapper/package-info.java` — [DONE] — `@NullMarked`
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/mapper/UserNoteMongoReactiveMapperContract.java` — [REVIEW]
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/mapper/UserNoteMongoReactiveMapper.java` — [REVIEW]
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/adapter/package-info.java` — [DONE] — `@NullMarked`
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/adapter/UserNoteService.java` — [REVIEW] — переименован из `UserNoteMongoReactiveAdapter` (2026-07-13): `implements UserNoteServiceReactiveInterface`, та же схема из 15 операций, что и в `data-r2dbc`

#### user-note/data-mongodb/ (10 файлов)
- `user-note/data-mongodb/build.gradle.kts` — [DONE] — `spring-boot-data-mongodb` + `implementation(contract)` + `implementation(domain)` (добавлено 2026-07-09, см. «Принятые решения» → «Hexagonal»)
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/repository/package-info.java` — [DONE] — `@NullMarked`
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/repository/UserNoteMongoRepository.java` — [REVIEW] — `MongoRepository<UserNoteDocument, UUID>` + derived queries; добавлены `existsByUserId`/`existsByNoteId` (2026-07-13)
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/model/package-info.java` — [DONE] — `@NullMarked`
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/model/UserNoteDocument.java` — [REVIEW] — `{Entity}Document`, `@CompoundIndex(unique = true)` на `userId+noteId` — соответствует принятому решению
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/mapper/package-info.java` — [DONE] — `@NullMarked`
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/mapper/UserNoteMongoMapperContract.java` — [REVIEW]
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/mapper/UserNoteMongoMapper.java` — [REVIEW]
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/adapter/package-info.java` — [DONE] — `@NullMarked`
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/adapter/UserNoteService.java` — [REVIEW] — переименован из `UserNoteMongoAdapter` (2026-07-13): `implements UserNoteServiceInterface`, 15 операций; `add`/`create` по-прежнему через `MongoTemplate` с инлайновым мэппером внутри `insert(...)` (находка №4 — стилистическое расхождение с `note/`/`user/`, не трогали)

#### user-note/data-jpa/ (10 файлов)
- `user-note/data-jpa/build.gradle.kts` — [DONE] — `spring-boot-data-jpa` + `implementation(contract)` + `implementation(domain)` (добавлено 2026-07-09, см. «Принятые решения» → «Hexagonal»)
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/repository/package-info.java` — [DONE] — `@NullMarked`
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/repository/UserNoteJpaRepository.java` — [REVIEW] — `JpaRepository<UserNoteEntity, UUID>` + derived queries (`findByUserId`, `findByNoteId`, `findByUserIdAndNoteId`, `existsByUserId`/`existsByNoteId`/`existsByUserIdAndNoteId`) — `deleteByUserIdAndNoteId` убран (2026-07-13, больше не используется — адаптер сам делает `find`+`delete(entity)`, чтобы вернуть удалённую сущность)
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/model/package-info.java` — [DONE] — `@NullMarked`
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/model/UserNoteEntity.java` — [REVIEW] — `{Entity}Entity`, `@Table(uniqueConstraints = {user_id, note_id})`, `@Enumerated(EnumType.STRING)` для `role`, `@NullUnmarked` на классе (2026-07-14, было `@SuppressWarnings("NullAway.Init")` на конструкторе — см. «Стиль кода») — соответствует принятым решениям
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/mapper/package-info.java` — [DONE] — `@NullMarked`
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/mapper/UserNoteJpaMapperContract.java` — [REVIEW]
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/mapper/UserNoteJpaMapper.java` — [REVIEW] — `toNewEntity`/`toExistingEntity`/`toResponse`, ручной маппинг без MapStruct
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/adapter/package-info.java` — [DONE] — `@NullMarked`
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/adapter/UserNoteService.java` — [REVIEW] — переименован из `UserNoteJpaAdapter` (2026-07-13, три коммита за день — переименование интерфейса, добавление existsByUserId/existsByNoteId, разбивка по ключу): `implements UserNoteServiceInterface`, 15 методов; `findByUserId`/`findByNoteId` сами бросают `UserNotFoundException`/`NoteNotFoundException`; `merge*` — настоящая PATCH-семантика (null-поле в request сохраняет старое значение), `replace*` — полная замена (PUT); `deleteByUserNoteId`/`deleteByUserIdAndNoteId` возвращают `UserNoteResponse` вместо `void`

#### user-note/data-jdbc/ (11 файлов, было 6 — переход на Spring Data JDBC repository + `schema.sql` 2026-07-14, см. «⚡ Задачи»)
- `user-note/data-jdbc/build.gradle.kts` — [DONE] — `spring-boot-data-jdbc` + `implementation(contract)` + `implementation(domain)` (добавлено 2026-07-09, см. «Принятые решения» → «Hexagonal»); не менялся при переходе на repository (зависимости те же)
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/repository/package-info.java` — [REVIEW] — новый (2026-07-14) — `@NullMarked`
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/repository/UserNoteJdbcRepository.java` — [REVIEW] — новый (2026-07-14): `extends ListCrudRepository<UserNoteJdbcEntity, UUID>` + derived `findByUserId`/`findByNoteId`(`List`)/`findByUserIdAndNoteId`(`Optional`)/`existsByUserId`/`existsByNoteId`/`existsByUserIdAndNoteId`(`boolean`) — 1:1 сигнатуры с `UserNoteJpaRepository`, `@Query` не понадобился
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/model/package-info.java` — [REVIEW] — новый (2026-07-14) — `@NullMarked`
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/model/UserNoteJdbcEntity.java` — [REVIEW] — новый (2026-07-14): `@Table("user_notes")`, `@Id private @Nullable UUID id`, `@Column("user_id")`/`@Column("note_id")`/`@Column("role")` (`role` типа `UserNoteRole` — Spring Data JDBC маппит enum в `name()` из коробки, см. «Принятые решения» → «Архитектура») — структура 1:1 с `UserNoteR2dbcEntity`; unique constraint через `schema.sql` (см. ниже). Реализует `UserNotePersistable`
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/mapper/package-info.java` — [DONE] — `@NullMarked`
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/mapper/UserNoteJdbcMapperContract.java` — [REVIEW] — переписан 2026-07-14 (было `fromRow(ResultSet, int)`): теперь `toNewEntity`/`toExistingEntity`/`toResponse`, сигнатура 1:1 с `UserNoteR2dbcMapperContract`
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/mapper/UserNoteJdbcMapper.java` — [REVIEW] — упрощён 2026-07-14: прямой passthrough, ручная `.name()`/`valueOf()`-конвертация убрана вместе с переходом `role` на `UserNoteRole` в entity — закрывает бывший открытый вопрос «role как String в R2DBC/JDBC»
- `user-note/data-jdbc/src/main/resources/schema.sql` — [REVIEW] — новый (2026-07-14): идентичен `user-note/data-r2dbc/schema.sql`; `data-jdbc` по-прежнему ни в одном сервисе не подключён к `application/`
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/adapter/package-info.java` — [DONE] — `@NullMarked`
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/adapter/UserNoteService.java` — [REVIEW] — переписан 2026-07-14 (было 15 операций через сырой SQL с ручными `UPDATE`-запросами для `merge*`, переименован из `UserNoteJdbcAdapter` ещё в 2026-07-13): `implements UserNoteServiceInterface`, теперь через `UserNoteJdbcRepository`/`UserNoteJdbcMapperContract` — 1:1 копия структуры `user-note/data-jpa/adapter/UserNoteService`, все 15 методов (4 `existsBy*` + `create`/`findByUserNoteId`/`findByUserId`/`findByNoteId`/`findByUserIdAndNoteId`/`replaceBy*`×2/`mergeBy*`×2/`deleteBy*`×2)

#### user-note/contract-reactive/ (4 файлов)
- `user-note/contract-reactive/build.gradle.kts` — [REVIEW] — `com.example.reactor` + `api(projects.userNote.domain)`
- `user-note/contract-reactive/src/main/java/com/example/usernote/contract/reactive/package-info.java` — [DONE] — `@NullMarked`
- `user-note/contract-reactive/src/main/java/com/example/usernote/contract/reactive/UserNoteReactiveInterface.java` — [REVIEW] — новый (2026-07-13): базовый порт, 15 методов возвращают `Object` — зеркалирует sync `UserNoteInterface`
- `user-note/contract-reactive/src/main/java/com/example/usernote/contract/reactive/UserNoteServiceReactiveInterface.java` — [REVIEW] — переименован из `UserNoteContractReactive` (2026-07-13): `extends UserNoteReactiveInterface`, `Mono<X>`/`Flux<X>`/`Mono<Boolean>`; было 8 методов без разбивки по ключу — теперь 15, с разбивкой `userNoteId`/`userId+noteId`, как в sync

#### user-note/contract/ (4 файлов)
- `user-note/contract/build.gradle.kts` — [REVIEW] — `com.example.library` + `api(projects.userNote.domain)`
- `user-note/contract/src/main/java/com/example/usernote/contract/package-info.java` — [DONE] — `@NullMarked`
- `user-note/contract/src/main/java/com/example/usernote/contract/UserNoteInterface.java` — [REVIEW] — новый (2026-07-13): базовый порт, 15 методов (4 `existsBy*` + 11) возвращают `Object`
- `user-note/contract/src/main/java/com/example/usernote/contract/UserNoteServiceInterface.java` — [REVIEW] — переименован из `UserNoteContract` (2026-07-13, три коммита за день): `extends UserNoteInterface`, конкретные типы (`Boolean`/`UserNoteResponse`); контракт вырос с 8 до 15 методов — разбивка по суррогатному `userNoteId` и составному `userId+noteId`, добавлен `merge*` (PATCH)

#### user-note/application-reactive/ (6 файлов)
Новое 2026-07-14, зеркалит `user-note/application/` (см. note/ выше — тот же паттерн): `build.gradle.kts`, `package-info.java`, `UserNoteReactiveApplication.java`, `application.properties` (main), `UserNoteReactiveApplicationTests.java`, `application.properties` (test) — все [REVIEW].

#### user-note/application/ (6 файлов)
- `user-note/application/build.gradle.kts` — [REVIEW] — Соответствует конвенции: `spring-boot-application` + `spring-boot-h2-database`, зависимости domain+contract+webmvc+data-jpa — единственная связка technology (см. открытый вопрос «Комбинации technology в application/»); с 2026-07-14 транзитивно несёт actuator (см. «⚡ Задачи»)
- `user-note/application/src/test/resources/application.properties` — [REVIEW] — Пустой файл (0 байт) — идентично `note/application/src/test/resources/application.properties`, не аномалия
- `user-note/application/src/test/java/com/example/usernote/UserNoteApplicationTests.java` — [REVIEW] — Единственный тест во всём сервисе — тривиальный `contextLoads()`. См. «Главные находки» п. 1
- `user-note/application/src/main/resources/application.properties` — [REVIEW]
- `user-note/application/src/main/java/com/example/usernote/package-info.java` — [DONE] — `@NullMarked`
- `user-note/application/src/main/java/com/example/usernote/UserNoteApplication.java` — [REVIEW]

#### user-note/ — предлагаемые отсутствующие файлы (`[ADD]`, 18)
- `user-note/webmvc/src/test/java/com/example/usernote/webmvc/UserNoteExceptionHandlerTest.java` — [ADD] — Проверка `ProblemDetail` (статус, detail) для `UserNoteNotFoundException`
- `user-note/webmvc/src/test/java/com/example/usernote/webmvc/UserNoteControllerTest.java` — [ADD] — `@WebMvcTest` + `@MockitoBean` на контракт, все 7 HTTP-операций одним классом — после пересмотра 2026-07-08 контроллер один, не семь; включая кейсы 404 при `!exists`
- `user-note/webflux/src/test/java/com/example/usernote/webflux/UserNoteExceptionHandlerTest.java` — [ADD] — Проверка `ProblemDetail` + `setTitle("UserNote Not Found")`
- `user-note/webflux/src/test/java/com/example/usernote/webflux/UserNoteControllerTest.java` — [ADD] — `@WebFluxTest` + `WebTestClient`, все 7 HTTP-операций одним классом
- `user-note/domain/src/test/java/com/example/usernote/domain/UserNoteNotFoundExceptionTest.java` — [ADD] — Unit-тест на оба конструктора исключения (по `id` и по `userId+noteId`) и текст сообщения
- `user-note/data-r2dbc/src/test/java/com/example/usernote/data/r2dbc/mapper/UserNoteR2dbcMapperTest.java` — [ADD] — Unit-тест конвертации `role` enum↔String
- `user-note/data-r2dbc/src/test/java/com/example/usernote/data/r2dbc/adapter/UserNoteR2dbcAdapterIT.java` — [ADD] — Testcontainers-тест на все 8 адаптеров; заблокирован открытым вопросом «Управление схемой для R2DBC/JDBC» — нужен `schema.sql` (см. ниже), иначе таблицы неоткуда взять
- `user-note/data-r2dbc/src/main/resources/schema.sql` — [REVIEW] — реализовано 2026-07-14 (было `[ADD]`), см. запись в «user-note/data-r2dbc/» выше
- `user-note/data-mongodb/src/test/java/com/example/usernote/data/mongodb/mapper/UserNoteMongoMapperTest.java` — [ADD] — Unit-тест маппера
- `user-note/data-mongodb/src/test/java/com/example/usernote/data/mongodb/adapter/UserNoteMongoAdapterIT.java` — [ADD] — Testcontainers/embedded-Mongo тест на все 8 адаптеров, включая `@CompoundIndex(unique = true)`
- `user-note/data-mongodb-reactive/src/test/java/com/example/usernote/data/mongodb/reactive/mapper/UserNoteMongoReactiveMapperTest.java` — [ADD] — Unit-тест маппера
- `user-note/data-mongodb-reactive/src/test/java/com/example/usernote/data/mongodb/reactive/adapter/UserNoteMongoReactiveAdapterIT.java` — [ADD] — Testcontainers-тест с `StepVerifier` на все 8 reactive-адаптеров
- `user-note/data-jpa/src/test/java/com/example/usernote/data/jpa/mapper/UserNoteJpaMapperTest.java` — [ADD] — Unit-тест ручного маппинга `toNewEntity`/`toExistingEntity`/`toResponse`
- `user-note/data-jpa/src/test/java/com/example/usernote/data/jpa/adapter/UserNoteJpaAdapterIT.java` — [ADD] — `@DataJpaTest`/testcontainers-тест на все 8 адаптеров JPA (add/exists/find*/remove/replace), включая проверку unique constraint `user_id+note_id` и `UserNoteNotFoundException` в `replace`
- `user-note/data-jdbc/src/test/java/com/example/usernote/data/jdbc/mapper/UserNoteJdbcMapperTest.java` — [ADD] — Unit-тест `fromRow(ResultSet, int)` (мок `ResultSet`)
- `user-note/data-jdbc/src/test/java/com/example/usernote/data/jdbc/adapter/UserNoteJdbcAdapterIT.java` — [ADD] — Testcontainers-тест на все 8 адаптеров; тот же блокер по схеме, что и у R2DBC
- `user-note/data-jdbc/src/main/resources/schema.sql` — [REVIEW] — реализовано 2026-07-14 (было `[ADD]`), см. запись в «user-note/data-jdbc/» выше
- `user-note/application/src/test/java/com/example/usernote/UserNoteEndToEndIT.java` — [ADD] — Сквозной `@SpringBootTest` + `MockMvc`/testcontainers-БД, проверяющий реальную цепочку webmvc→data-jpa (единственная подключённая в `application/` связка)

### user/ (84 файла, было 81 — добавлены `UserPersistable` (domain) + 2× `schema.sql` (data-jdbc/data-r2dbc) 2026-07-14 + 13 предложенных)

**2026-07-13**: перенесена трёхуровневая иерархия портов из `user-note/` (см. «Принятые решения» → «Архитектура») — `UserContract`/`UserContractReactive` заменены на `UserInterface`+`UserServiceInterface`(+`Reactive`), добавлены `UserControllerInterface`(+`Reactive`), все 5 адаптеров переименованы в голый `UserService`; добавлен `merge()` (PATCH); `existsById` — реальный `GET /{id}/exists`. Находка №2 ниже (`findByEmail`/`findByUsername` без HTTP) — по-прежнему актуальна: эти два метода остались только в `UserServiceInterface`(+`Reactive`), не попали в `UserControllerInterface`(+`Reactive`) — открытый вопрос не решён этим переносом, решён отдельно.

#### Ключевые находки

1. Все файлы соответствуют паттернам раздела «Именование» из CLAUDE.md без единого нарушения (актуализировано под новую схему `{Entity}Interface`/`{Entity}ServiceInterface`/`{Entity}ControllerInterface`/голый `{Entity}Service`, см. 2026-07-13 выше) — единообразно по всем 5 driven-технологиям и обоим driving-адаптерам (webmvc/webflux).
2. Подтверждено дословно: `findByEmail`/`findByUsername` реализованы в `UserServiceInterface`/`UserServiceReactiveInterface` и во всех 5 driven-адаптерах (jpa/mongodb/ mongodb-reactive/r2dbc/jdbc), но не выведены НИ в `webmvc`, НИ в `webflux` — ни одного HTTP-эндпоинта для поиска по email/username не существует нигде в сервисе. Не изменилось при переносе иерархии портов 2026-07-13 (сознательно — `UserControllerInterface`(+`Reactive`) не объявляет эти методы).
3. В сервисе нет ни одного unit/integration-теста, кроме тривиального `UserApplicationTests.contextLoads()` — ни `domain/`, ни один driven-адаптер/mapper, ни один controller не покрыты тестами; `application/src/test/resources/application.properties` — пустой файл (0 байт).
4. `application/` подключает единственную комбинацию technology — `webmvc` + `data-jpa` (+ `spring-boot-h2-database`) — остальные 8 driven/driving модулей компилируются, но не запускаются ни в одной сборке (открытое решение «Комбинации technology в `application/`»).
5. Unique constraint на `username`/`email` реализован нативно в JPA (`@Column(unique=true)`) и MongoDB/MongoDB reactive (`@Indexed(unique=true)`), но полностью отсутствует в R2DBC/JDBC — ни схемы, ни constraint-аннотаций нет вообще (открытое решение «Управление схемой для R2DBC/JDBC»). `domain/UserRequest`/`UserResponse` не содержат поле пароля — ожидаемо, т. к. `auth/` пока скелет (см. открытое решение «Регистрация auth/ ↔ user/»).

Статус по умолчанию для каждого файла — `[REVIEW]` (правило «Пересмотр решений»: ничего не считается окончательно принятым при первом просмотре).

#### user/webmvc/ (5 файлов)
- `user/webmvc/build.gradle.kts` — [DONE] — `id("com.example.spring-boot-webmvc")` + `implementation(projects.user.contract)` + `implementation(projects.user.domain)` (добавлено 2026-07-09, см. «Принятые решения» → «Hexagonal»)
- `user/webmvc/src/main/java/com/example/user/webmvc/package-info.java` — [DONE] — `@NullMarked`
- `user/webmvc/src/main/java/com/example/user/webmvc/UserExceptionHandler.java` — [REVIEW] — `@ControllerAdvice extends ResponseEntityExceptionHandler`, соответствует конвенции `webmvc` (см. комментарий к reactive-версии по асимметрии)
- `user/webmvc/src/main/java/com/example/user/webmvc/UserControllerInterface.java` — [REVIEW] — новый (2026-07-13): `extends UserInterface`, 7 методов типа `ResponseEntity<X>` (без `findByEmail`/`findByUsername` — см. находку №2); реализует `UserController`
- `user/webmvc/src/main/java/com/example/user/webmvc/UserController.java` — [REVIEW] — теперь `implements UserControllerInterface`; методы переименованы под порт (`create`→`add`, `update`→`replace`, `delete`→`remove`); guard-проверки убраны (перенесены в сервис); добавлены `existsById` (`GET /{id}/exists`) и `merge` (`PATCH /{id}`); `remove` — `200 OK` с `UserResponse` вместо `204 No Content`; findByEmail/findByUsername по-прежнему без HTTP-входа

#### user/webflux/ (5 файлов)
- `user/webflux/build.gradle.kts` — [DONE] — `id("com.example.spring-boot-webflux")` + `implementation(projects.user.contractReactive)` + `implementation(projects.user.domain)` (добавлено 2026-07-09, см. «Принятые решения» → «Hexagonal»)
- `user/webflux/src/main/java/com/example/user/webflux/package-info.java` — [DONE] — `@NullMarked`
- `user/webflux/src/main/java/com/example/user/webflux/UserExceptionHandler.java` — [REVIEW] — `@RestControllerAdvice` без наследования (не `ResponseEntityExceptionHandler`, в отличие от `webmvc`-версии) — асимметрия оправдана: `ResponseEntityExceptionHandler` — MVC-специфичный класс, для WebFlux нет прямого аналога; уже выравнивалось при пересмотре CRUD-сервисов 2026-07-07
- `user/webflux/src/main/java/com/example/user/webflux/UserControllerReactiveInterface.java` — [REVIEW] — новый (2026-07-13): `extends UserReactiveInterface`, `Mono<ResponseEntity<X>>`/`ResponseEntity<Flux<UserResponse>>` для `findAll`; реализует `UserController`
- `user/webflux/src/main/java/com/example/user/webflux/UserController.java` — [REVIEW] — теперь `implements UserControllerReactiveInterface`; `findAll` оборачивает `Flux` в `ResponseEntity`; `findById`/`replace`/`remove` больше не делают `switchIfEmpty`/`existsById`+`flatMap` сами; добавлены `existsById` и `merge`; findByEmail/findByUsername по-прежнему без HTTP-входа

#### user/domain/ (6 файлов, было 5 — добавлен `UserPersistable` 2026-07-14)
- `user/domain/build.gradle.kts` — [REVIEW] — `id("com.example.base")`, соответствует конвенции чистого Java-модуля без инфраструктурных зависимостей
- `user/domain/src/main/java/com/example/user/domain/package-info.java` — [DONE] — `@NullMarked`
- `user/domain/src/main/java/com/example/user/domain/UserResponse.java` — [REVIEW] — `record(UUID id, String username, String email)`
- `user/domain/src/main/java/com/example/user/domain/UserRequest.java` — [REVIEW] — `record(String username, String email)` — нет поля пароля/credentials; ожидаемо на текущем этапе, т. к. `auth/` — скелет без логики (открытое решение «Регистрация auth/ ↔ user/»)
- `user/domain/src/main/java/com/example/user/domain/UserNotFoundException.java` — [REVIEW] — Два конструктора (`UUID`/`String`) — используется и для поиска по id, и потенциально по email/username; доменное исключение, без зависимостей на инфраструктуру
- `user/domain/src/main/java/com/example/user/domain/UserPersistable.java` — [REVIEW] — новый (2026-07-14): общий маркер-интерфейс для всех 5 model-классов `user/` (`@Nullable UUID getId()` + `String getUsername()` + `String getEmail()`), см. «Именование»

#### user/data-r2dbc/ (11 файлов, было 10 — добавлен `schema.sql` 2026-07-14)
- `user/data-r2dbc/build.gradle.kts` — [DONE] — `id("com.example.spring-boot-data-r2dbc")` + `implementation(projects.user.contractReactive)` + `implementation(projects.user.domain)` (добавлено 2026-07-09, см. «Принятые решения» → «Hexagonal»)
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/repository/package-info.java` — [DONE] — `@NullMarked`
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/repository/UserR2dbcRepository.java` — [REVIEW] — `ReactiveCrudRepository<UserR2dbcEntity, UUID>` + кастомные `findByUsername`/`findByEmail`
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/model/package-info.java` — [DONE] — `@NullMarked`
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/model/UserR2dbcEntity.java` — [REVIEW] — `{Entity}{Tech}Entity`, `@Id private @Nullable UUID id`, но у `@Column("username")`/`@Column("email")` НЕТ атрибутов unique/constraints (у `spring-data-relational` их вообще нет) — constraint теперь создаётся через `schema.sql` (см. ниже), не через аннотации. Реализует `UserPersistable` (2026-07-14)
- `user/data-r2dbc/src/main/resources/schema.sql` — [REVIEW] — новый (2026-07-14): `users` с `id UUID DEFAULT RANDOM_UUID() PRIMARY KEY`, `UNIQUE(username)`, `UNIQUE(email)` — закрывает открытый вопрос «Управление схемой для R2DBC/JDBC», реально исполняется при старте `application-reactive/`
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/mapper/package-info.java` — [DONE] — `@NullMarked`
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/mapper/UserR2dbcMapperContract.java` — [REVIEW]
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/mapper/UserR2dbcMapper.java` — [REVIEW] — `Objects.requireNonNull(entity.getId())` в `toResponse`, идентичен по структуре JPA-мэпперу
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/adapter/package-info.java` — [DONE] — `@NullMarked`
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/adapter/UserService.java` — [REVIEW] — переименован из `UserR2dbcAdapter` (2026-07-13): `implements UserServiceReactiveInterface`; `findById`/`findByEmail`/`findByUsername` сами бросают `UserNotFoundException` через `switchIfEmpty`; добавлен `merge()`

#### user/data-mongodb-reactive/ (10 файлов)
- `user/data-mongodb-reactive/build.gradle.kts` — [DONE] — `id("com.example.spring-boot-data-mongodb-reactive")` + `implementation(projects.user.contractReactive)` + `implementation(projects.user.domain)` (добавлено 2026-07-09, см. «Принятые решения» → «Hexagonal»)
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/repository/package-info.java` — [DONE] — `@NullMarked`
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/repository/UserMongoReactiveRepository.java` — [REVIEW] — `ReactiveMongoRepository<UserReactiveDocument, UUID>`
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/model/package-info.java` — [DONE] — `@NullMarked`
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/model/UserReactiveDocument.java` — [REVIEW] — `{Entity}ReactiveDocument`, `@Indexed(unique=true)` на `username`/`email`
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/mapper/package-info.java` — [DONE] — `@NullMarked`
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/mapper/UserMongoReactiveMapperContract.java` — [REVIEW]
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/mapper/UserMongoReactiveMapper.java` — [REVIEW] — Соответствует конвенции, идентичен sync-версии по структуре
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/adapter/package-info.java` — [DONE] — `@NullMarked`
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/adapter/UserService.java` — [REVIEW] — переименован из `UserMongoReactiveAdapter` (2026-07-13): `implements UserServiceReactiveInterface`, та же схема, что и в `data-r2dbc`

#### user/data-mongodb/ (10 файлов)
- `user/data-mongodb/build.gradle.kts` — [DONE] — `id("com.example.spring-boot-data-mongodb")` + `implementation(projects.user.contract)` + `implementation(projects.user.domain)` (добавлено 2026-07-09, см. «Принятые решения» → «Hexagonal»)
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/repository/package-info.java` — [DONE] — `@NullMarked`
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/repository/UserMongoRepository.java` — [REVIEW] — `MongoRepository<UserDocument, UUID>` + кастомные `findByUsername`/`findByEmail`
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/model/package-info.java` — [DONE] — `@NullMarked`
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/model/UserDocument.java` — [REVIEW] — `{Entity}Document`, `@Indexed(unique=true)` на `username`/`email` — unique constraint создаётся, `id` не `@Nullable` (в отличие от JPA/R2DBC) — корректно, т. к. ID присваивается вручную в мэппере ещё до вставки, не генерируется БД
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/mapper/package-info.java` — [DONE] — `@NullMarked`
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/mapper/UserMongoMapperContract.java` — [REVIEW]
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/mapper/UserMongoMapper.java` — [REVIEW] — ID генерируется мэппером (`UUID.randomUUID()`) для нового документа
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/adapter/package-info.java` — [DONE] — `@NullMarked`
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/adapter/UserService.java` — [REVIEW] — переименован из `UserMongoAdapter` (2026-07-13): `implements UserServiceInterface`; `findById`/`findByEmail`/`findByUsername` сами бросают `UserNotFoundException`; добавлен `merge()`

#### user/data-jpa/ (10 файлов)
- `user/data-jpa/build.gradle.kts` — [DONE] — `id("com.example.spring-boot-data-jpa")` + `implementation(projects.user.contract)` + `implementation(projects.user.domain)` (добавлено 2026-07-09, см. «Принятые решения» → «Hexagonal»)
- `user/data-jpa/src/main/java/com/example/user/data/jpa/repository/package-info.java` — [DONE] — `@NullMarked`
- `user/data-jpa/src/main/java/com/example/user/data/jpa/repository/UserJpaRepository.java` — [REVIEW] — `JpaRepository<UserEntity, UUID>` + кастомные `findByUsername`/`findByEmail` — максимально использует Spring Data, соответствует памяти пользователя
- `user/data-jpa/src/main/java/com/example/user/data/jpa/model/package-info.java` — [DONE] — `@NullMarked`
- `user/data-jpa/src/main/java/com/example/user/data/jpa/model/UserEntity.java` — [REVIEW] — `{Entity}Entity`, `@Id @GeneratedValue(UUID)`, `@Column(unique=true)` на `username`/`email` — unique constraint реально создаётся (в отличие от R2DBC/JDBC), `@NullUnmarked` на классе (2026-07-14, было `@SuppressWarnings("NullAway.Init")` — см. «Стиль кода»)
- `user/data-jpa/src/main/java/com/example/user/data/jpa/mapper/package-info.java` — [DONE] — `@NullMarked`
- `user/data-jpa/src/main/java/com/example/user/data/jpa/mapper/UserJpaMapperContract.java` — [REVIEW]
- `user/data-jpa/src/main/java/com/example/user/data/jpa/mapper/UserJpaMapper.java` — [REVIEW] — Ручной маппинг, `Objects.requireNonNull(entity.getId())` в `toResponse` — корректная обработка `@Nullable UUID id`
- `user/data-jpa/src/main/java/com/example/user/data/jpa/adapter/package-info.java` — [DONE] — `@NullMarked`
- `user/data-jpa/src/main/java/com/example/user/data/jpa/adapter/UserService.java` — [REVIEW] — переименован из `UserJpaAdapter` (2026-07-13): `implements UserServiceInterface` (был `UserContract`); `findById`/`findByEmail`/`findByUsername` бросают `UserNotFoundException` вместо `Optional.empty()`; добавлен `merge()`; `remove()` возвращает `UserResponse` вместо `void`

#### user/data-jdbc/ (11 файлов, было 6 — переход на Spring Data JDBC repository + `schema.sql` 2026-07-14, см. «⚡ Задачи»)
- `user/data-jdbc/build.gradle.kts` — [DONE] — `id("com.example.spring-boot-data-jdbc")` + `implementation(projects.user.contract)` + `implementation(projects.user.domain)` (добавлено 2026-07-09, см. «Принятые решения» → «Hexagonal»); не менялся при переходе на repository (зависимости те же)
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/repository/package-info.java` — [REVIEW] — новый (2026-07-14) — `@NullMarked`
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/repository/UserJdbcRepository.java` — [REVIEW] — новый (2026-07-14): `extends ListCrudRepository<UserJdbcEntity, UUID>` + derived `Optional<UserJdbcEntity> findByUsername(String)`/`findByEmail(String)` — зеркалирует `UserJpaRepository` (sync `Optional`, не `Mono` как у `UserR2dbcRepository`)
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/model/package-info.java` — [REVIEW] — новый (2026-07-14) — `@NullMarked`
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/model/UserJdbcEntity.java` — [REVIEW] — новый (2026-07-14): `@Table("users")`, `@Id private @Nullable UUID id`, `@Column("username")`/`@Column("email")` — структура 1:1 с `UserR2dbcEntity`; unique constraint теперь через `schema.sql` (см. ниже), не аннотации. Реализует `UserPersistable`
- `user/data-jdbc/src/main/resources/schema.sql` — [REVIEW] — новый (2026-07-14): идентичен `user/data-r2dbc/schema.sql`; `data-jdbc` по-прежнему ни в одном сервисе не подключён к `application/` (см. «Открытые решения» → «Комбинации technology в `application/`»)
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/mapper/package-info.java` — [DONE] — `@NullMarked`
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/mapper/UserJdbcMapperContract.java` — [REVIEW] — переписан 2026-07-14 (было `fromRow(ResultSet, int)`): теперь `toNewEntity`/`toExistingEntity`/`toResponse`, сигнатура 1:1 с `UserR2dbcMapperContract`
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/mapper/UserJdbcMapper.java` — [REVIEW] — переписан 2026-07-14: `Objects.requireNonNull(entity.getId())` в `toResponse` — структура идентична `UserR2dbcMapper`/`UserJpaMapper`
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/adapter/package-info.java` — [DONE] — `@NullMarked`
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/adapter/UserService.java` — [REVIEW] — переписан 2026-07-14 (было на `NamedParameterJdbcTemplate` + сырой SQL, переименован из `UserJdbcAdapter` ещё в 2026-07-13): `implements UserServiceInterface`, теперь через `UserJdbcRepository`/`UserJdbcMapperContract` — 1:1 копия структуры `user/data-jpa/adapter/UserService`; `findById`/`findByEmail`/`findByUsername`/`replace`/`remove` через `orElseThrow(UserNotFoundException)`, `merge()` — null-preserving PATCH, как и раньше

#### user/contract-reactive/ (4 файлов)
- `user/contract-reactive/build.gradle.kts` — [REVIEW] — `id("com.example.reactor")` + `api(projects.user.domain)`
- `user/contract-reactive/src/main/java/com/example/user/contract/reactive/package-info.java` — [DONE] — `@NullMarked`
- `user/contract-reactive/src/main/java/com/example/user/contract/reactive/UserReactiveInterface.java` — [REVIEW] — новый (2026-07-13): базовый порт, 7 методов возвращают `Object` (без `findByEmail`/`findByUsername` — см. находку №2)
- `user/contract-reactive/src/main/java/com/example/user/contract/reactive/UserServiceReactiveInterface.java` — [REVIEW] — переименован из `UserContractReactive` (2026-07-13): `extends UserReactiveInterface`, `Mono<X>`/`Flux<X>`; `findByEmail`/`findByUsername` объявлены напрямую (не через `@Override`) — только на этом уровне, без controller-аналога; добавлен `merge()`

#### user/contract/ (4 файлов)
- `user/contract/build.gradle.kts` — [REVIEW] — `id("com.example.library")` + `api(projects.user.domain)`, соответствует принятому решению об `api` только когда тип в публичной сигнатуре
- `user/contract/src/main/java/com/example/user/contract/package-info.java` — [DONE] — `@NullMarked`
- `user/contract/src/main/java/com/example/user/contract/UserInterface.java` — [REVIEW] — новый (2026-07-13): базовый порт, 7 методов (`existsById`/`add`/`findAll`/`findById`/`replace`/`merge`/`remove`) возвращают `Object`
- `user/contract/src/main/java/com/example/user/contract/UserServiceInterface.java` — [REVIEW] — переименован из `UserContract` (2026-07-13): `extends UserInterface`, конкретные типы; `findByEmail`/`findByUsername` объявлены напрямую (не `@Override` — нет аналога в `UserInterface`/`UserControllerInterface`); добавлен `merge()`, `remove()` теперь `UserResponse` вместо `void`

#### user/application-reactive/ (6 файлов)
Новое 2026-07-14, зеркалит `user/application/` (см. note/ выше — тот же паттерн): `build.gradle.kts`, `package-info.java`, `UserReactiveApplication.java`, `application.properties` (main), `UserReactiveApplicationTests.java`, `application.properties` (test) — все [REVIEW].

#### user/application/ (6 файлов)
- `user/application/build.gradle.kts` — [REVIEW] — Единственная technology-комбинация `webmvc`+`data-jpa`+`spring-boot-h2-database` — соответствует открытому решению «Комбинации technology в `application/`», замечаний по синтаксису нет; с 2026-07-14 транзитивно несёт actuator (см. «⚡ Задачи»)
- `user/application/src/test/resources/application.properties` — [REVIEW] — Файл существует, но пуст (0 байт) — не переопределяет ничего для тестового профиля
- `user/application/src/test/java/com/example/user/UserApplicationTests.java` — [REVIEW] — Единственный тест во всём сервисе — тривиальный `contextLoads()`; нет ни одного другого теста ни в одном модуле `user/`
- `user/application/src/main/resources/application.properties` — [REVIEW] — `spring.application.name`+`spring.mvc.problemdetails.enabled=true`, соответствует принятому решению «ProblemDetail»
- `user/application/src/main/java/com/example/user/package-info.java` — [DONE] — `@NullMarked` присутствует
- `user/application/src/main/java/com/example/user/UserApplication.java` — [REVIEW] — Стандартный `@SpringBootApplication`

#### user/ — предлагаемые отсутствующие файлы (`[ADD]`, 13)
- `user/webmvc/src/test/java/com/example/user/webmvc/UserControllerTest.java` — [ADD] — Нет `@WebMvcTest`-покрытия контроллера `webmvc` вообще
- `user/webmvc/src/main/java/com/example/user/webmvc/UserController.java` (доп. методы `findByEmail`/`findByUsername`) — [ADD] — Один из вариантов закрытия открытого вопроса «`findByEmail`/`findByUsername` без HTTP-входа» — добавить эндпоинты в уже существующий контроллер (альтернатива: оставить как задел под `auth/`, либо убрать как неиспользуемое)
- `user/webflux/src/test/java/com/example/user/webflux/UserControllerTest.java` — [ADD] — Нет `@WebFluxTest`-покрытия контроллера `webflux` вообще
- `user/webflux/src/main/java/com/example/user/webflux/UserController.java` (доп. методы `findByEmail`/`findByUsername`) — [ADD] — Reactive-аналог, тот же открытый вопрос
- `user/domain/src/test/java/com/example/user/domain/UserNotFoundExceptionTest.java` — [ADD] — Нет ни одного unit-теста на domain-слой во всём сервисе
- `user/data-r2dbc/src/test/java/com/example/user/data/r2dbc/adapter/UserR2dbcAdapterTest.java` — [ADD] — Нет тестов R2DBC-адаптеров; потребует тестовую схему
- `user/data-r2dbc/src/main/resources/schema.sql` — [REVIEW] — реализовано 2026-07-14 (было `[ADD]`), см. запись в «user/data-r2dbc/» выше
- `user/data-mongodb/src/test/java/com/example/user/data/mongodb/adapter/UserMongoAdapterTest.java` — [ADD] — Нет тестов MongoDB-адаптеров (Testcontainers Mongo)
- `user/data-mongodb-reactive/src/test/java/com/example/user/data/mongodb/reactive/adapter/UserMongoReactiveAdapterTest.java` — [ADD] — Нет тестов reactive Mongo-адаптеров (`StepVerifier` + Testcontainers)
- `user/data-jpa/src/test/java/com/example/user/data/jpa/adapter/UserJpaAdapterTest.java` — [ADD] — Нет integration-тестов адаптеров/mapper'а JPA (например, через `@DataJpaTest` или Testcontainers)
- `user/data-jdbc/src/test/java/com/example/user/data/jdbc/adapter/UserJdbcAdapterTest.java` — [ADD] — Нет тестов JDBC-адаптеров; потребует тестовую схему (см. следующую строку)
- `user/data-jdbc/src/main/resources/schema.sql` — [REVIEW] — реализовано 2026-07-14 (было `[ADD]`), см. запись в «user/data-jdbc/» выше
- `user/application-reactive/build.gradle.kts` — [REVIEW] — реализовано 2026-07-14 (было `[ADD]` под именем `application-webflux-r2dbc`, имя скорректировано): `webflux`+`data-r2dbc`+H2 (R2DBC) — см. «Открытые решения» → «Комбинации technology в `application/`»

### registry/ (6 файлов)

Skeleton-сервис Eureka server. Структурно идентичен `gateway/`/`config/`/`auth/`: convention-плагины + Application-класс + package-info + application.properties (main/test) + тривиальный `contextLoads()`.

#### registry/application/ (6 файлов)
- `registry/build.gradle.kts` — [REVIEW] — `spring-cloud-eureka-server` (actuator приходит транзитивно через `com.example.spring-boot-application`, был отдельным `id("com.example.spring-boot-actuator")` до 2026-07-14, см. «⚡ Задачи») — совпадает со «Скелет»
- `registry/src/test/resources/application.properties` — [REVIEW] — дублирует eureka-флаги main-конфига под тестовый профиль
- `registry/src/test/java/com/example/registry/RegistryApplicationTests.java` — [REVIEW] — только `contextLoads()`, других тестов нет
- `registry/src/main/resources/application.properties` — [REVIEW] — `register-with-eureka=false`, `fetch-registry=false` — сервер не регистрирует сам себя, ожидаемо
- `registry/application/src/main/java/com/example/registry/package-info.java` — [DONE] — `@NullMarked`
- `registry/application/src/main/java/com/example/registry/RegistryApplication.java` — [REVIEW] — `@EnableEurekaServer` + `@SpringBootApplication`, стандартно

### note/ (84 файла, было 81 — добавлены `NotePersistable` (domain) + 2× `schema.sql` (data-jdbc/data-r2dbc) 2026-07-14 + 19 предложенных)

Каждый файл прочитан целиком и сверен с CLAUDE.md (naming, слои, принятые решения). Статус `[REVIEW]` — по умолчанию для всех строк (в проекте ничего не считается окончательно принятым при первом просмотре, см. правило «Пересмотр решений»); `[DONE]` ставит только человек. `[ADD]` — файлов сейчас нет, предложены для реальной работоспособности сервиса. **2026-07-13**: перенесена трёхуровневая иерархия портов из `user-note/` (см. «Принятые решения» → «Архитектура») — `NoteContract`/`NoteContractReactive` заменены на `NoteInterface`+`NoteServiceInterface`(+`Reactive`), добавлены `NoteControllerInterface`(+`Reactive`), все 5 адаптеров (`NoteJpaAdapter`/`NoteMongoAdapter`/`NoteJdbcAdapter`/`NoteR2dbcAdapter`/`NoteMongoReactiveAdapter`) переименованы в голый `NoteService` в своём пакете; добавлен `merge()` (PATCH); `existsById` — реальный `GET /{id}/exists`; `remove()`/`deleteBy*` возвращают `NoteResponse` вместо `void` (sync) — подробности см. в подсекциях ниже. Новые/переименованные файлы помечены `[REVIEW]` согласно общему правилу.

#### Главные находки

1. **Тестов нет вообще** — ни одного unit-теста на мэпперы/адаптеры/контроллеры, ни одного integration-теста (testcontainers) ни по одной из 5 driven-технологий, ни slice-тестов (`@WebMvcTest`/`@WebFluxTest`, `@DataJpaTest` и т. п.) — статус ГОТОВО в CLAUDE.md по факту означает «компилируется и стартует», но не «проверено тестами».
2. ~~`webflux` `NoteController.findAll()` не оборачивает ответ в `ResponseEntity`~~ — **исправлено 2026-07-13** при переносе иерархии портов: теперь `ResponseEntity<Flux<NoteResponse>>`, консистентно с остальными методами и с правилом «`ResponseEntity<T>` в контроллерах».
3. **`NoteExceptionHandler` в `webflux` и `webmvc` расходятся сильнее, чем предполагает запись в CLAUDE.md** о выравнивании при пересмотре 2026-07-07: `webmvc`-версия наследует `org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler`, `webflux` — нет, хотя в classpath (`spring-webflux-7.0.7.jar`, подтверждено байткодом) есть reactive-аналог `org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler`. Плюс имена методов различаются (`handleNotFound` vs `handleNoteNotFound`), и только `webflux` вызывает `problem.setTitle(...)`. Не затронуто переносом иерархии портов 2026-07-13.
4. **`data-mongodb` (sync) непоследователен внутри себя и относительно `data-mongodb-reactive`**: методы `add`/`replace` в новом `NoteService` (`data-mongodb/adapter`, был `NoteMongoAdapter`) обходят `NoteMongoRepository` и работают напрямую через `MongoTemplate`, тогда как остальные операции того же класса и весь `NoteService` в `data-mongodb-reactive` используют репозиторий/`insert()`. Подтверждено декомпиляцией (`spring-data-mongodb-5.0.5.jar`): технического ограничения нет, чистая стилевая непоследовательность — сохранилась и после переноса иерархии портов 2026-07-13 (не трогали намеренно, вне скоупа задачи).
5. **Схема БД для `data-r2dbc`/`data-jdbc` по-прежнему не создаётся нигде** (соответствует открытому решению «Управление схемой для R2DBC/JDBC» в CLAUDE.md) — `note/application/` подключает только `webmvc`+`data-jpa`; `NoteR2dbcEntity` не имеет аналога `@GeneratedValue`.

#### note/webmvc/ (5 файлов)
- `note/webmvc/build.gradle.kts` — [DONE] — `spring-boot-webmvc` + `implementation(projects.note.contract)` + `implementation(projects.note.domain)` (добавлено 2026-07-09, см. «Принятые решения» → «Hexagonal») — соответствует
- `note/webmvc/src/main/java/com/example/note/webmvc/package-info.java` — [DONE] — `@NullMarked`, соответствует
- `note/webmvc/src/main/java/com/example/note/webmvc/NoteExceptionHandler.java` — [REVIEW] — наследует `ResponseEntityExceptionHandler` (servlet) — см. находку №3 (расхождение с `webflux`)
- `note/webmvc/src/main/java/com/example/note/webmvc/NoteControllerInterface.java` — [REVIEW] — новый (2026-07-13): `extends NoteInterface`, 7 методов (`existsById`/`add`/`findAll`/`findById`/`replace`/`merge`/`remove`) типа `ResponseEntity<X>` — реализует `NoteController`
- `note/webmvc/src/main/java/com/example/note/webmvc/NoteController.java` — [REVIEW] — теперь `implements NoteControllerInterface`; методы переименованы под имена порта (`create`→`add`, `update`→`replace`, `delete`→`remove`, URL/HTTP-verb не менялись); guard-проверки убраны (перенесены в сервис/адаптер); добавлены `existsById` (`GET /{id}/exists`) и `merge` (`PATCH /{id}`); `remove` теперь возвращает `200 OK` с `NoteResponse` вместо `204 No Content` (следствие ковариантной `Object`-иерархии — `void` не подходит, `NoteResponse` подходит)

#### note/webflux/ (5 файлов)
- `note/webflux/build.gradle.kts` — [DONE] — `spring-boot-webflux` + `implementation(projects.note.contractReactive)` + `implementation(projects.note.domain)` (добавлено 2026-07-09, см. «Принятые решения» → «Hexagonal») — соответствует
- `note/webflux/src/main/java/com/example/note/webflux/package-info.java` — [DONE] — `@NullMarked`, соответствует
- `note/webflux/src/main/java/com/example/note/webflux/NoteExceptionHandler.java` — [REVIEW] — **находка**: не наследует reactive-аналог `ResponseEntityExceptionHandler` (в отличие от `webmvc`), имя метода `handleNoteNotFound` (в `webmvc` — `handleNotFound`), дополнительно вызывает `problem.setTitle(...)` — см. находку №3, не затронуто переносом 2026-07-13
- `note/webflux/src/main/java/com/example/note/webflux/NoteControllerReactiveInterface.java` — [REVIEW] — новый (2026-07-13): `extends NoteReactiveInterface`, `Mono<ResponseEntity<X>>`/`ResponseEntity<Flux<NoteResponse>>` для `findAll` — реализует `NoteController`
- `note/webflux/src/main/java/com/example/note/webflux/NoteController.java` — [REVIEW] — теперь `implements NoteControllerReactiveInterface`; `findAll` оборачивает `Flux` в `ResponseEntity` (бывшая находка №2, исправлено); `findById`/`replace`/`remove` больше не делают `switchIfEmpty`/`existsById`+`flatMap` сами — перенесено в `NoteService` (data-r2dbc/data-mongodb-reactive); добавлены `existsById` и `merge`

#### note/domain/ (6 файлов, было 5 — добавлен `NotePersistable` 2026-07-14)
- `note/domain/build.gradle.kts` — [REVIEW] — `id("com.example.base")` — соответствует
- `note/domain/src/main/java/com/example/note/domain/package-info.java` — [DONE] — `@NullMarked`, соответствует
- `note/domain/src/main/java/com/example/note/domain/NoteResponse.java` — [REVIEW] — `record NoteResponse(UUID id, String content)` — соответствует
- `note/domain/src/main/java/com/example/note/domain/NoteRequest.java` — [REVIEW] — `record NoteRequest(String content)` — соответствует
- `note/domain/src/main/java/com/example/note/domain/NoteNotFoundException.java` — [REVIEW] — `extends RuntimeException`, конструктор от `UUID id` — чистое доменное исключение, без зависимостей на инфраструктуру — соответствует
- `note/domain/src/main/java/com/example/note/domain/NotePersistable.java` — [REVIEW] — новый (2026-07-14): общий маркер-интерфейс для всех 5 model-классов `note/` (`@Nullable UUID getId()` + `String getContent()`), см. «Именование» → «Общий маркер-интерфейс модели сущности»

#### note/data-r2dbc/ (11 файлов, было 10 — добавлен `schema.sql` 2026-07-14)
- `note/data-r2dbc/build.gradle.kts` — [DONE] — `spring-boot-data-r2dbc` + `implementation(projects.note.contractReactive)` + `implementation(projects.note.domain)` (добавлено 2026-07-09, см. «Принятые решения» → «Hexagonal») — соответствует (в отличие от `data-jdbc`, использующего sync-контракт, что верно, т. к. r2dbc реактивен)
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/repository/package-info.java` — [DONE] — `@NullMarked`, соответствует
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/repository/NoteR2dbcRepository.java` — [REVIEW] — `extends ReactiveCrudRepository<NoteR2dbcEntity, UUID>`, а не технологически-специфичный `org.springframework.data.r2dbc.repository.R2dbcRepository` (единственный из технологий, чей репозиторий не расширяет tech-specific интерфейс — JPA/Mongo/MongoReactive все расширяют `JpaRepository`/`MongoRepository`/`ReactiveMongoRepository`). Функционально не расходится: декомпиляция `spring-data-r2dbc-4.0.5.jar` показала, что `R2dbcRepository` не добавляет собственных методов (в отличие от `MongoRepository`/`ReactiveMongoRepository`, у которых есть `insert()`) — но стилистическая непоследовательность в выборе базового интерфейса есть
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/model/package-info.java` — [DONE] — `@NullMarked`, соответствует
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/model/NoteR2dbcEntity.java` — [REVIEW] — `@Table("notes")`, `@Id @Nullable UUID`, `@Column("content")` — структурно идентична `NoteEntity` (JPA), без аналога `@GeneratedValue` на уровне аннотации (Spring Data R2DBC его не поддерживает) — `id` для новой записи в Java остаётся `null` до чтения обратно из БД; с 2026-07-14 генерация реально происходит на уровне БД (`DEFAULT RANDOM_UUID()` в `schema.sql`, см. ниже), закрывает находку №5. Реализует `NotePersistable` (2026-07-14)
- `note/data-r2dbc/src/main/resources/schema.sql` — [REVIEW] — новый (2026-07-14): `CREATE TABLE IF NOT EXISTS notes (id UUID DEFAULT RANDOM_UUID() PRIMARY KEY, content VARCHAR NOT NULL)` — закрывает открытый вопрос «Управление схемой для R2DBC/JDBC», реально исполняется при старте `application-reactive/` (H2, classpath-автозагрузка)
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/mapper/package-info.java` — [DONE] — `@NullMarked`, соответствует
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/mapper/NoteR2dbcMapperContract.java` — [REVIEW] — соответствует
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/mapper/NoteR2dbcMapper.java` — [REVIEW] — `Objects.requireNonNull(entity.getId())` в `toResponse` — структурно идентична `NoteJpaMapper` — соответствует
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/adapter/package-info.java` — [DONE] — `@NullMarked`, соответствует
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/adapter/NoteService.java` — [REVIEW] — переименован из `NoteR2dbcAdapter` (2026-07-13): `implements NoteServiceReactiveInterface`; `findById` теперь сам делает `switchIfEmpty(Mono.error(new NoteNotFoundException(id)))`; `replace`/`remove` — `existsById().flatMap(...)` + ошибка, если не найдено; добавлен `merge()` (null-preserving PATCH)

#### note/data-mongodb-reactive/ (10 файлов)
- `note/data-mongodb-reactive/build.gradle.kts` — [DONE] — `spring-boot-data-mongodb-reactive` + `implementation(projects.note.contractReactive)` + `implementation(projects.note.domain)` (добавлено 2026-07-09, см. «Принятые решения» → «Hexagonal») — соответствует
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/repository/package-info.java` — [DONE] — `@NullMarked`, соответствует
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/repository/NoteMongoReactiveRepository.java` — [REVIEW] — `extends ReactiveMongoRepository<NoteReactiveDocument, UUID>` — соответствует
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/model/package-info.java` — [DONE] — `@NullMarked`, соответствует
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/model/NoteReactiveDocument.java` — [REVIEW] — идентична `NoteDocument` по структуре — оправданное дублирование между sync/reactive модулями по принятой архитектуре
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/mapper/package-info.java` — [DONE] — `@NullMarked`, соответствует
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/mapper/NoteMongoReactiveMapperContract.java` — [REVIEW] — соответствует
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/mapper/NoteMongoReactiveMapper.java` — [REVIEW] — структурно идентична sync-мэпперу — соответствует
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/adapter/package-info.java` — [DONE] — `@NullMarked`, соответствует
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/adapter/NoteService.java` — [REVIEW] — переименован из `NoteMongoReactiveAdapter` (2026-07-13): `implements NoteServiceReactiveInterface`, та же схема `switchIfEmpty`/`existsById`-проверок и `merge()`, что и в `data-r2dbc`

#### note/data-mongodb/ (10 файлов)
- `note/data-mongodb/build.gradle.kts` — [DONE] — `spring-boot-data-mongodb` + `implementation(projects.note.contract)` + `implementation(projects.note.domain)` (добавлено 2026-07-09, см. «Принятые решения» → «Hexagonal») — соответствует
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/repository/package-info.java` — [DONE] — `@NullMarked`, соответствует
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/repository/NoteMongoRepository.java` — [REVIEW] — `extends MongoRepository<NoteDocument, UUID>` — соответствует, но фактически недоиспользуется (см. находку выше — `insert`/`save` не вызываются через него для add/replace)
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/model/package-info.java` — [DONE] — `@NullMarked`, соответствует
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/model/NoteDocument.java` — [REVIEW] — `@Document(collection="notes")`, `@Id UUID` (не `@Nullable` — id всегда задан мэппером до создания) — соответствует
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/mapper/package-info.java` — [DONE] — `@NullMarked`, соответствует
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/mapper/NoteMongoMapperContract.java` — [REVIEW] — соответствует
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/mapper/NoteMongoMapper.java` — [REVIEW] — `toNewDocument` сам генерирует `UUID.randomUUID()` — соответствует (симметрично reactive-версии)
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/adapter/package-info.java` — [DONE] — `@NullMarked`, соответствует
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/adapter/NoteService.java` — [REVIEW] — переименован из `NoteMongoAdapter` (2026-07-13): `implements NoteServiceInterface`; расхождение MongoTemplate/repository между add/replace и остальными операциями (находка №4) сохраняется; добавлен `merge()`, `findById`/`replace`/`remove` сами бросают `NoteNotFoundException`

#### note/data-jpa/ (10 файлов)
- `note/data-jpa/build.gradle.kts` — [DONE] — `spring-boot-data-jpa` + `implementation(projects.note.contract)` + `implementation(projects.note.domain)` (добавлено 2026-07-09, см. «Принятые решения» → «Hexagonal») — соответствует
- `note/data-jpa/src/main/java/com/example/note/data/jpa/repository/package-info.java` — [DONE] — `@NullMarked`, соответствует
- `note/data-jpa/src/main/java/com/example/note/data/jpa/repository/NoteJpaRepository.java` — [REVIEW] — пустой `extends JpaRepository<NoteEntity, UUID>` — максимально использует Spring Data, соответствует
- `note/data-jpa/src/main/java/com/example/note/data/jpa/model/package-info.java` — [DONE] — `@NullMarked`, соответствует
- `note/data-jpa/src/main/java/com/example/note/data/jpa/model/NoteEntity.java` — [REVIEW] — `@Entity`/`@Table("notes")`, `@GeneratedValue(UUID)`, `@Nullable UUID id`, `protected` no-arg конструктор + `@NullUnmarked` на классе (2026-07-14, было `@SuppressWarnings("NullAway.Init")` — см. «Стиль кода») — точно соответствует принятым решениям и стилю кода
- `note/data-jpa/src/main/java/com/example/note/data/jpa/mapper/package-info.java` — [DONE] — `@NullMarked`, соответствует
- `note/data-jpa/src/main/java/com/example/note/data/jpa/mapper/NoteJpaMapperContract.java` — [REVIEW] — соответствует `{Entity}{Tech}MapperContract`
- `note/data-jpa/src/main/java/com/example/note/data/jpa/mapper/NoteJpaMapper.java` — [REVIEW] — `toNewEntity`/`toExistingEntity`/`toResponse`, `Objects.requireNonNull(entity.getId())` в `toResponse` — соответствует
- `note/data-jpa/src/main/java/com/example/note/data/jpa/adapter/package-info.java` — [DONE] — `@NullMarked`, соответствует
- `note/data-jpa/src/main/java/com/example/note/data/jpa/adapter/NoteService.java` — [REVIEW] — переименован из `NoteJpaAdapter` (2026-07-13): `implements NoteServiceInterface` (был `NoteContract`); `findById` бросает `NoteNotFoundException` вместо `Optional.empty()`; `replace` проверяет `existsById` сам (была проверка в контроллере); добавлен `merge()` (PATCH, null-preserving); `remove()` возвращает `NoteResponse` вместо `void`

#### note/data-jdbc/ (11 файлов, было 6 — переход на Spring Data JDBC repository + `schema.sql` 2026-07-14, см. «⚡ Задачи»)
- `note/data-jdbc/build.gradle.kts` — [DONE] — `spring-boot-data-jdbc` + `implementation(projects.note.contract)` (sync-контракт, верно для JDBC) + `implementation(projects.note.domain)` (добавлено 2026-07-09, см. «Принятые решения» → «Hexagonal») — соответствует; не менялся при переходе на repository (зависимости те же)
- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/repository/package-info.java` — [REVIEW] — новый (2026-07-14) — `@NullMarked`
- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/repository/NoteJdbcRepository.java` — [REVIEW] — новый (2026-07-14): `extends ListCrudRepository<NoteJdbcEntity, UUID>` (не `CrudRepository` — `ListCrudRepository`, Spring Data 3.0+, даёт `findAll(): List<T>` напрямую), пустое тело — зеркалирует `NoteR2dbcRepository`/`NoteJpaRepository`
- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/model/package-info.java` — [REVIEW] — новый (2026-07-14) — `@NullMarked`
- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/model/NoteJdbcEntity.java` — [REVIEW] — новый (2026-07-14): `@Table("notes")`, `@Id private @Nullable UUID id`, `@Column("content")` — структура 1:1 с `NoteR2dbcEntity` (оба на `spring-data-relational`), `@NullUnmarked` на классе (см. «Стиль кода»); реализует `NotePersistable`
- `note/data-jdbc/src/main/resources/schema.sql` — [REVIEW] — новый (2026-07-14): идентичен `note/data-r2dbc/schema.sql` — таблица пока используется только этим модулем в изоляции, т. к. `data-jdbc` ни в одном сервисе не подключён к `application/` (см. «Открытые решения» → «Комбинации technology в `application/`»)
- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/mapper/package-info.java` — [DONE] — `@NullMarked`, соответствует
- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/mapper/NoteJdbcMapperContract.java` — [REVIEW] — переписан 2026-07-14 (было `fromRow(ResultSet, int)`, `RowMapper`-стиль): теперь `toNewEntity`/`toExistingEntity`/`toResponse`, сигнатура один-в-один с `NoteR2dbcMapperContract`, старое обоснование «оправданная адаптация под RowMapper» снято вместе с переходом на repository
- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/mapper/NoteJdbcMapper.java` — [REVIEW] — переписан 2026-07-14: `Objects.requireNonNull(entity.getId())` в `toResponse` — структура идентична `NoteR2dbcMapper`/`NoteJpaMapper`
- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/adapter/package-info.java` — [DONE] — `@NullMarked`, соответствует
- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/adapter/NoteService.java` — [REVIEW] — переписан 2026-07-14 (было на `NamedParameterJdbcTemplate` + сырой SQL, переименован из `NoteJdbcAdapter` ещё в 2026-07-13): `implements NoteServiceInterface`, теперь через `NoteJdbcRepository`/`NoteJdbcMapperContract` — 1:1 копия структуры `note/data-jpa/adapter/NoteService` (repository-based `existsById`/`save`/`findById().orElseThrow`); `findById`/`replace`/`remove` через `orElseThrow(NoteNotFoundException)`, `merge()` — null-preserving PATCH, как и раньше

#### note/contract-reactive/ (4 файлов)
- `note/contract-reactive/build.gradle.kts` — [REVIEW] — `id("com.example.reactor")` + `api(projects.note.domain)` — соответствует
- `note/contract-reactive/src/main/java/com/example/note/contract/reactive/package-info.java` — [DONE] — `@NullMarked`, соответствует
- `note/contract-reactive/src/main/java/com/example/note/contract/reactive/NoteReactiveInterface.java` — [REVIEW] — новый (2026-07-13): базовый порт, 7 методов возвращают `Object`
- `note/contract-reactive/src/main/java/com/example/note/contract/reactive/NoteServiceReactiveInterface.java` — [REVIEW] — переименован из `NoteContractReactive` (2026-07-13): `extends NoteReactiveInterface`, `Mono<X>`/`Flux<X>` как и раньше; добавлен `merge()`

#### note/contract/ (4 файлов)
- `note/contract/build.gradle.kts` — [REVIEW] — `id("com.example.library")` + `api(projects.note.domain)` — соответствует
- `note/contract/src/main/java/com/example/note/contract/package-info.java` — [DONE] — `@NullMarked`, соответствует
- `note/contract/src/main/java/com/example/note/contract/NoteInterface.java` — [REVIEW] — новый (2026-07-13): базовый порт, 7 методов (`existsById`/`add`/`findAll`/`findById`/`replace`/`merge`/`remove`) возвращают `Object`
- `note/contract/src/main/java/com/example/note/contract/NoteServiceInterface.java` — [REVIEW] — переименован из `NoteContract` (2026-07-13): `extends NoteInterface`, конкретные типы (`Boolean`/`NoteResponse`/`List<NoteResponse>`); добавлен `merge()`, `remove()` теперь возвращает `NoteResponse` вместо `void`

#### note/application-reactive/ (6 файлов)
Новое 2026-07-14, зеркалит `note/application/` в реактиве (`webflux`+`data-r2dbc`+H2 R2DBC вместо `webmvc`+`data-jpa`+H2 JDBC): `build.gradle.kts` — [REVIEW]; `package-info.java` — [REVIEW]; `NoteReactiveApplication.java` — [REVIEW] (зеркалит `NoteApplication`); `application.properties` (main, `spring.webflux.problemdetails.enabled=true`+`spring.r2dbc.url`) — [REVIEW]; `NoteReactiveApplicationTests.java` (`contextLoads()`) — [REVIEW]; `application.properties` (test, пустой) — [REVIEW]. Схема R2DBC не создана — открытый вопрос, реальные запросы к БД упадут без таблиц.

#### note/application/ (6 файлов)
- `note/application/build.gradle.kts` — [REVIEW] — `spring-boot-application` + `spring-boot-h2-database`; implementation на domain/contract/webmvc/dataJpa — единственная связка technology в проекте (см. открытое решение «Комбинации technology в application/»); с 2026-07-14 транзитивно несёт actuator (см. «⚡ Задачи») — впервые появился в CRUD-сервисах, раньше был только в `registry`/`config`/`gateway`
- `note/application/src/test/resources/application.properties` — [REVIEW] — файл пуст (0 байт) — неясно, нужен ли вообще как заглушка
- `note/application/src/test/java/com/example/note/NoteApplicationTests.java` — [REVIEW] — единственный тест во всём сервисе — только `contextLoads()`, см. находку №1
- `note/application/src/main/resources/application.properties` — [REVIEW] — `spring.application.name` + `spring.mvc.problemdetails.enabled=true` — соответствует принятому решению по `ProblemDetail`; datasource не сконфигурирован явно (полагается на дефолты `spring-boot-h2-database`)
- `note/application/src/main/java/com/example/note/package-info.java` — [DONE] — `@NullMarked`, соответствует
- `note/application/src/main/java/com/example/note/NoteApplication.java` — [REVIEW]

#### note/ — предлагаемые отсутствующие файлы (`[ADD]`, 19)
- `note/webmvc/src/test/java/com/example/note/webmvc/NoteExceptionHandlerTest.java` — [ADD] — тест `ProblemDetail`-ответа на `NoteNotFoundException`
- `note/webmvc/src/test/java/com/example/note/webmvc/NoteControllerTest.java` — [ADD] — `@WebMvcTest` slice-тест на все 5 методов (create/findAll/findById/update/delete) одним классом — после пересмотра 2026-07-08 контроллер один, не пять
- `note/webflux/src/test/java/com/example/note/webflux/NoteExceptionHandlerTest.java` — [ADD] — тест `ProblemDetail`-ответа, заодно проверит найденное расхождение с `webmvc`
- `note/webflux/src/test/java/com/example/note/webflux/NoteControllerTest.java` — [ADD] — `@WebFluxTest` slice-тест на все 5 методов одним классом — заодно зафиксирует найденное расхождение `findAll()` по `ResponseEntity`
- `note/domain/src/test/java/com/example/note/domain/NoteNotFoundExceptionTest.java` — [ADD] — простые `record`ы (`NoteRequest`/`NoteResponse`) тестов не требуют, но исключение стоит покрыть
- `note/data-r2dbc/src/test/java/com/example/note/data/r2dbc/mapper/NoteR2dbcMapperTest.java` — [ADD] — unit-тест мэппера
- `note/data-r2dbc/src/test/java/com/example/note/data/r2dbc/adapter/NoteR2dbcAdapterIntegrationTest.java` — [ADD] — testcontainers Postgres/`@DataR2dbcTest` — потребует schema.sql (см. ниже)
- `note/data-r2dbc/src/main/resources/schema.sql` — [REVIEW] — реализовано 2026-07-14 (было `[ADD]`), см. запись в «note/data-r2dbc/» выше
- `note/data-mongodb/src/test/java/com/example/note/data/mongodb/mapper/NoteMongoMapperTest.java` — [ADD] — unit-тест мэппера
- `note/data-mongodb/src/test/java/com/example/note/data/mongodb/adapter/NoteMongoAdapterIntegrationTest.java` — [ADD] — testcontainers MongoDB — заодно проверит найденное расхождение insert()/save() через MongoTemplate vs repository
- `note/data-mongodb-reactive/src/test/java/com/example/note/data/mongodb/reactive/mapper/NoteMongoReactiveMapperTest.java` — [ADD] — unit-тест мэппера
- `note/data-mongodb-reactive/src/test/java/com/example/note/data/mongodb/reactive/adapter/NoteMongoReactiveAdapterIntegrationTest.java` — [ADD] — testcontainers MongoDB + `StepVerifier`
- `note/data-jpa/src/test/java/com/example/note/data/jpa/mapper/NoteJpaMapperTest.java` — [ADD] — unit-тест мэппера (toNewEntity/toExistingEntity/toResponse)
- `note/data-jpa/src/test/java/com/example/note/data/jpa/adapter/NoteJpaAdapterIntegrationTest.java` — [ADD] — `@DataJpaTest` на все 6 операций — сейчас 0% покрытия по JPA-адаптерам
- `note/data-jdbc/src/test/java/com/example/note/data/jdbc/mapper/NoteJdbcMapperTest.java` — [ADD] — unit-тест `fromRow(ResultSet, rowNum)`
- `note/data-jdbc/src/test/java/com/example/note/data/jdbc/adapter/NoteJdbcAdapterIntegrationTest.java` — [ADD] — `@JdbcTest`/testcontainers — заодно проверит найденное поведение `NoteReplaceJdbcAdapter` при несуществующем id
- `note/data-jdbc/src/main/resources/schema.sql` — [REVIEW] — реализовано 2026-07-14 (было `[ADD]`), см. запись в «note/data-jdbc/» выше
- `note/application/src/test/java/com/example/note/NoteCreateEndpointIntegrationTest.java` — [ADD] — сквозной тест реального стека (`webmvc`+`data-jpa`+H2) через `MockMvc`, а не только `contextLoads()`
- `note/application-reactive/build.gradle.kts` — [REVIEW] — реализовано 2026-07-14 (было `[ADD]` под именем `application-webflux`): `webflux`+`data-r2dbc`+H2 (R2DBC) — см. «Открытые решения» → «Комбинации technology в `application/`»

### gradle/ (4 файла)

`libs.versions.toml` и `gradle-wrapper.properties` — версии совпадают с «Стек»/«Синхронизация версий» (Gradle 9.6.1, Spring Boot 4.1.0, Spring Cloud 2025.1.2, JUnit 6.1.2). `spring-javaformat` — `0.0.48-SNAPSHOT` с 2026-07-13 (было `0.0.47`, см. «Задачи»). `checkstyle/checkstyle.xml` — с 2026-07-13 не только `SpringChecks` (excludes javadoc-проверок + `SpringLeadingWhitespaceCheck`), но и 5 добавленных модулей (`FileTabCharacter`, 2× `RegexpMultiline`, `Indentation`, `EmptyLineSeparator`) под 10 персональных правил форматирования пользователя — см. «Стиль кода» и «Задачи».

#### gradle/ — отдельные файлы (1)
- `gradle/libs.versions.toml` — [REVIEW] — версии совпадают со «Стек»/«Синхронизация версий»

#### gradle/wrapper/ (2 файлов)
- `gradle/wrapper/gradle-wrapper.properties` — [REVIEW] — Gradle 9.6.1, совпадает со «Стек»
- `gradle/wrapper/gradle-wrapper.jar` — [REVIEW] — бинарный файл wrapper, не проверяется построчно

#### gradle/checkstyle/ (1 файл)
- `gradle/checkstyle/checkstyle.xml` — [REVIEW] — `SpringChecks` (excludes javadoc-проверок + `SpringLeadingWhitespaceCheck`, версия `spring-javaformat` = `0.0.48-SNAPSHOT`) + 5 новых модулей (`FileTabCharacter`/2× `RegexpMultiline` — root-level; `Indentation`/`EmptyLineSeparator` — в новом `TreeWalker`, соседнем с внутренним `TreeWalker` из `SpringChecks`) — добавлены 2026-07-13 под 10 персональных правил форматирования пользователя, единственный жёсткий гейт стиля с тех пор, как убран `io.spring.javaformat`. Второй `RegexpMultiline` (пустая строка обязательна в изначально пустом теле метода/класса) и первый (запрет пустой строки перед `}` при реальном содержимом) — согласованная пара, первый явно исключает `{` как предшествующий символ (`[^\s{]`), чтобы не конфликтовать со вторым

### gateway/ (6 файлов)

Skeleton-сервис Spring Cloud Gateway (webflux). `spring.config.import=optional:configserver:` — опциональный импорт, не сломает старт без config-сервера. Без routes — соответствует статусу СКЕЛЕТ в «Задачах».

#### gateway/application/ (6 файлов)
- `gateway/build.gradle.kts` — [REVIEW] — `spring-cloud-gateway-webflux` + `spring-cloud-eureka-client` + `spring-cloud-config-client` (actuator приходит транзитивно через `com.example.spring-boot-application`, был отдельным `id("com.example.spring-boot-actuator")` до 2026-07-14, см. «⚡ Задачи»)
- `gateway/src/test/resources/application.properties` — [REVIEW] — `spring.cloud.config.enabled=false`, `spring.cloud.discovery.enabled=false` — тест изолирован от registry/config
- `gateway/src/test/java/com/example/gateway/GatewayApplicationTests.java` — [REVIEW] — только `contextLoads()`
- `gateway/src/main/resources/application.properties` — [REVIEW] — `spring.config.import=optional:configserver:` — опциональный импорт, не сломает старт без config-сервера
- `gateway/application/src/main/java/com/example/gateway/package-info.java` — [DONE] — `@NullMarked`
- `gateway/application/src/main/java/com/example/gateway/GatewayApplication.java` — [REVIEW] — стандартный `@SpringBootApplication`, без routes-класса

### config/ (6 файлов)

Skeleton Config Server. `spring.profiles.active=native`, но `search-locations` не задан — сервер стартует, но конфигурацию пока не отдаст (соответствует «без config-репозитория» в «Задачах»).

#### config/application/ (6 файлов)
- `config/build.gradle.kts` — [REVIEW] — `spring-cloud-config-server` (actuator приходит транзитивно через `com.example.spring-boot-application`, был отдельным `id("com.example.spring-boot-actuator")` до 2026-07-14, см. «⚡ Задачи»)
- `config/src/test/resources/application.properties` — [REVIEW] — `spring.profiles.active=native`, дублирует main
- `config/src/test/java/com/example/config/ConfigApplicationTests.java` — [REVIEW] — только `contextLoads()`
- `config/src/main/resources/application.properties` — [REVIEW] — `spring.profiles.active=native`, но `search-locations` не задан — реальную конфигурацию пока не отдаст
- `config/application/src/main/java/com/example/config/package-info.java` — [DONE] — `@NullMarked`
- `config/application/src/main/java/com/example/config/ConfigApplication.java` — [REVIEW] — `@EnableConfigServer` + `@SpringBootApplication`

### build-logic/ (38 файлов; было 39 — `com.example.spring-boot-actuator.gradle.kts` удалён 2026-07-14, actuator перенесён в `com.example.spring-boot-application`, см. «⚡ Задачи»; до этого было 38 — добавлен `com.example.spring-boot-r2dbc-h2-database.gradle.kts` 2026-07-14)

**Главные находки** (снимок на 2026-07-08, до удаления `com.example.javaformat` — см. актуализацию ниже):

1. Реальных расхождений между кодом `build-logic/` и текстом CLAUDE.md **не найдено**. Обе оси иерархии (BOM-цепочка `base → library/reactor/spring-boot → spring-cloud → tech-plugin` и ортогональная bootable-ось `org.springframework.boot`) воспроизведены в коде ровно так, как описано: ровно 4 standalone `spring-cloud-*`-плагина (`config-server`, `eureka-server`, `gateway-webflux`, `gateway-webmvc`) подключают `com.example.spring-boot-application` вторым родителем, остальные 5 `spring-cloud-*` — нет.
2. Числа сходятся точно: 17 технологических `spring-boot-*`-плагинов, 9 `spring-cloud-*`-плагинов, 7 плагинов "1:1 с папкой модуля" (`webmvc`, `webflux`, `data-jpa`, `data-jdbc`, `data-r2dbc`, `data-mongodb`, `data-mongodb-reactive`) — совпадает с формулировками в «Иерархия». **Актуализация 2026-07-13**: внутри `codequality` было 5 плагинов на момент этого прохода, теперь 4 — `javaformat` удалён (см. «Задачи» → «Форматирование без авто-форматтера»).
3. Версии в `gradle/libs.versions.toml` на момент прохода 2026-07-08 (`spring-boot=4.0.6`, `spring-cloud=2025.1.2`, `reactor-core=3.8.5`, `junit-jupiter=junit-platform=6.0.3`, `spring-javaformat=0.0.47`, `checkstyle=9.3`, `jacoco=0.8.14`) совпадали со всем, что зафиксировано в «Синхронизация версий» и «Стек». **Актуализация 2026-07-13**: все версии подняты до последних — `spring-boot=4.1.0`, `reactor-core=3.8.6`, `junit-jupiter=junit-platform=6.1.2`, `spring-javaformat=0.0.48-SNAPSHOT`, `checkstyle=13.7.0`, `jacoco=0.8.15`; `spring-cloud` осталась `2025.1.2` (уже последняя). См. «Задачи» → «Обновление версий до последних».
4. `com.example.spring-boot` берёт BOM через `SpringBootPlugin.BOM_COORDINATES`, а `com.example.spring-cloud` — вручную строкой через `libs.findVersion("spring-cloud")` — ровно та асимметрия, что объяснена в «Синхронизация версий» (у Spring Cloud нет своего Gradle-плагина).
5. Отступ везде 4 пробела, ни одного таба — проверено программно по всем 39 файлам. Не найдено ни одного файла, отсутствие которого противоречило бы перечисленному в CLAUDE.md (счётчики плагинов из п. 2 сошлись без остатка) — записей `[ADD]` в списке нет.

На момент прохода 2026-07-08 была тонкость (не нестыковка, а подтверждение задокументированного намеренного решения): `com.example.checkstyle` и `com.example.javaformat` независимо конфигурировали один и тот же extension `checkstyle {}`. С 2026-07-13 вопрос снят — `com.example.javaformat` удалён, вся конфигурация (`configFile`/`configProperties`/зависимость `spring-javaformat-checkstyle`) — в одном `com.example.checkstyle`, без дублирования.

#### build-logic/ — корневые файлы (2 файла)
- `build-logic/settings.gradle.kts` — [REVIEW] — `rootProject.name = "build-logic"`, `versionCatalogs { create("libs") { from(files("../gradle/libs.versions.toml")) } }` — совпадает с «Синхронизация версий»: «build-logic — отдельный included build и не видит корневой gradle.properties/каталог автоматически, поэтому build-logic/settings.gradle.kts подключает тот же .toml-файл отдельно». `include(":convention")` совпадает с «Convention plugins — build-logic/convention/» и деревом `build-logic/settings.gradle.kts include(":convention")`.
- `build-logic/convention/build.gradle.kts` — [REVIEW] — `kotlin-dsl` + classpath-зависимости (`spring-boot-gradle-plugin`, `dependency-management-plugin`, `gradle-errorprone-plugin`) через **typed-аксессоры** (`libs.versions.spring.boot.get()` и т. п.) — совпадает с «Синхронизация версий»: typed-аксессоры доступны именно в обычных build-скриптах, не в precompiled-плагинах. Отступ 4 пробела, без табов. `spring-javaformat-gradle-plugin` убран из classpath 2026-07-13 (плагин `io.spring.javaformat` больше нигде не применяется, см. «Задачи»); `spring-javaformat-checkstyle` (отдельный артефакт, ruleset) сюда не относится и не был здесь никогда — он подключается через `checkstyle(...)`-конфигурацию в `com.example.checkstyle.gradle.kts`, не через classpath плагина

#### build-logic/convention/src/main/kotlin/ — precompiled script plugins (36 файлов; было 37 — `com.example.spring-boot-actuator.gradle.kts` удалён 2026-07-14, до этого 37 — `com.example.javaformat.gradle.kts` удалён; арифметика пересчитана по факту (`ls` в каталоге), а не по цепочке +1/-1 в тексте — расхождение найдено при проверке convention-плагинов на кольцевые зависимости)
- `build-logic/com.example.spring-cloud.gradle.kts` — [REVIEW] — `id("com.example.spring-boot")` + ручной BOM через `libs.findVersion("spring-cloud").get().requiredVersion` = 2025.1.2 (строка, не через плагин-константу) — дословно совпадает с обоснованием в «Синхронизация версий» → com.example.spring-cloud (у Spring Cloud нет своего Gradle-плагина/`BOM_COORDINATES`-аналога). Собственных `junit-platform-launcher`/`useJUnitPlatform()` нет — совпадает с «убран как дублирующий то, что уже даёт родитель com.example.base».
- `build-logic/com.example.spring-cloud-openfeign.gradle.kts` — [REVIEW] — `id("com.example.spring-cloud")` только — не входит в список 4 standalone-плагинов, совпадает.
- `build-logic/com.example.spring-cloud-loadbalancer.gradle.kts` — [REVIEW] — `id("com.example.spring-cloud")` только — не входит в список 4 standalone-плагинов, совпадает.
- `build-logic/com.example.spring-cloud-gateway-webmvc.gradle.kts` — [REVIEW] — `id("com.example.spring-cloud")` + `id("com.example.spring-boot-application")` — один из 4 standalone-сервисных плагинов, совпадает дословно.
- `build-logic/com.example.spring-cloud-gateway-webflux.gradle.kts` — [REVIEW] — `id("com.example.spring-cloud")` + `id("com.example.spring-boot-application")` — standalone, совпадает. Без `reactor-test` — совпадает с «Синхронизация версий» → reactor-test: «Spring Cloud gateway-webflux — плагин Spring Cloud, своего -test-компаньона не существует... оставлено осознанно без замены».
- `build-logic/com.example.spring-cloud-eureka-server.gradle.kts` — [REVIEW] — `id("com.example.spring-cloud")` + `id("com.example.spring-boot-application")` — один из 4 standalone-сервисных плагинов, совпадает дословно со списком в CLAUDE.md.
- `build-logic/com.example.spring-cloud-eureka-client.gradle.kts` — [REVIEW] — `id("com.example.spring-cloud")` только — не входит в список 4 standalone-плагинов, bootable-ось отсутствует корректно.
- `build-logic/com.example.spring-cloud-config-server.gradle.kts` — [REVIEW] — `id("com.example.spring-cloud")` + `id("com.example.spring-boot-application")` — один из 4 standalone-сервисных плагинов с bootable-осью («Плагины, требующие bootJar — ... spring-cloud-config-server, ...»), совпадает дословно.
- `build-logic/com.example.spring-cloud-config-client.gradle.kts` — [REVIEW] — `id("com.example.spring-cloud")` только — не входит в список «4 standalone-сервисных spring-cloud-*», bootable-ось отсутствует, как и должно быть.
- `build-logic/com.example.spring-cloud-circuit-breaker.gradle.kts` — [REVIEW] — `id("com.example.spring-cloud")` только — один из 5 «нестандэлон» `spring-cloud-*`-плагинов без bootable-оси, совпадает с деревом иерархии.
- `build-logic/com.example.spring-boot.gradle.kts` — [REVIEW] — `id("com.example.base")` + `id("io.spring.dependency-management")`, BOM через `SpringBootPlugin.BOM_COORDINATES`, свой `spring-boot-starter`/`-test` — дословно совпадает с «Синхронизация версий» → com.example.spring-boot и «Архитектура» → «spring-boot-starter/-test — общий для любого Spring Boot модуля, объявлен в com.example.spring-boot». С 2026-07-14 сюда же добавлен `spring-boot-starter-validation`(`-test`) — доступен теперь во всех Spring Boot модулях без отдельного opt-in плагина, см. «⚡ Задачи» и «Открытые решения» → Jakarta Bean Validation
- `build-logic/com.example.spring-boot-webmvc.gradle.kts` — [REVIEW] — Зависимость `spring-boot-starter-webmvc`(`-test`) — совпадает со «Стек»: `starter-web` → `starter-webmvc`.
- `build-logic/com.example.spring-boot-webflux.gradle.kts` — [REVIEW] — `id("com.example.spring-boot")`, зависимость `spring-boot-starter-webflux`(`-test`), без явного `reactor-test` — совпадает со «Стек» (не `starter-web`) и «Синхронизация версий» → reactor-test (транзитивно через `-webflux-test`).
- `build-logic/com.example.spring-boot-oauth2-resource-server.gradle.kts` — [REVIEW] — Зависимость `spring-boot-starter-security-oauth2-resource-server`(`-test`) — совпадает со «Стек» → «OAuth2-стартеры: oauth2-resource-server → security-oauth2-resource-server».
- `build-logic/com.example.spring-boot-oauth2-client.gradle.kts` — [REVIEW] — Зависимость `spring-boot-starter-security-oauth2-client`(`-test`) — совпадает со «Стек» → «OAuth2-стартеры».
- `build-logic/com.example.spring-boot-oauth2-authorization-server.gradle.kts` — [REVIEW] — Зависимость `spring-boot-starter-security-oauth2-authorization-server`(`-test`) — совпадает со «Стек» → «OAuth2-стартеры: ... аналогично для client и authorization-server» и «Spring Authorization Server — часть Spring Security 7, отдельной версии не имеет» (своей версии в каталоге нет — только Spring Boot BOM через родителя).
- `build-logic/com.example.spring-boot-h2-database.gradle.kts` — [REVIEW] — `id("com.example.spring-boot")`, зависимости `spring-boot-h2console` (`implementation`) + `com.h2database:h2` (`runtimeOnly`); отдельно текстом CLAUDE.md не описан.
- `build-logic/com.example.spring-boot-graphql.gradle.kts` — [REVIEW] — `id("com.example.spring-boot")`; сам сервис `graphql/` — статус ОТЛОЖЕНО в «Задачи», но существование готового convention-плагина этому не противоречит: текст описывает именно «convention plugins подготовлены... но не применены ни в одном модуле». Упомянут в «Синхронизация версий» → reactor-test: `spring-boot-starter-graphql-test` не содержит `reactor-test` — файл его и не добавляет.
- `build-logic/com.example.spring-boot-data-r2dbc.gradle.kts` — [REVIEW] — `id("com.example.spring-boot")`; один из «7 плагинов 1:1 с папкой модуля», без явного `reactor-test` — совпадает с «Синхронизация версий» → reactor-test (приходит транзитивно через `spring-boot-starter-data-r2dbc-test`).
- `build-logic/com.example.spring-boot-data-mongodb.gradle.kts` — [REVIEW] — `id("com.example.spring-boot")`; один из «7 плагинов 1:1 с папкой модуля» — id совпадает с именем папки/starter'а.
- `build-logic/com.example.spring-boot-data-mongodb-reactive.gradle.kts` — [REVIEW] — `id("com.example.spring-boot")`; один из «7 плагинов 1:1 с папкой модуля» — id совпадает с именем папки/starter'а.
- `build-logic/com.example.spring-boot-data-jpa.gradle.kts` — [REVIEW] — `id("com.example.spring-boot")`; один из «7 плагинов 1:1 с папкой модуля» — id совпадает с именем папки/starter'а.
- `build-logic/com.example.spring-boot-data-jdbc.gradle.kts` — [REVIEW] — `id("com.example.spring-boot")`; один из «7 плагинов 1:1 с папкой модуля» — id совпадает с именем папки/starter'а, как того требует правило именования. С 2026-07-14 несёт и `spring-boot-starter-data-jdbc`(`-test`), и `spring-boot-starter-jdbc`(`-test`) — модуль фактически использует сырой `NamedParameterJdbcTemplate` (см. «Принятые решения» → «Архитектура» → `data-jdbc/`), Spring Data JDBC-часть раньше была подключена без явного plain-JDBC-стартера
- `build-logic/com.example.spring-boot-data-elasticsearch.gradle.kts` — [REVIEW] — `id("com.example.spring-boot")`; с 2026-07-14 несёт и `spring-boot-starter-data-elasticsearch`(`-test`), и plain `spring-boot-starter-elasticsearch`(`-test`) — тот же пробел/паттерн, что был у `data-jdbc`/`data-r2dbc`, найден по референсу (см. «Задачи»).
- `build-logic/com.example.spring-boot-client-web.gradle.kts` — [REVIEW] — `id("com.example.spring-boot")`, зависимость `spring-boot-starter-webclient`(`-test`), без явного `reactor-test` — совпадает с «spring-boot-client-web — 1 родитель (spring-boot), без явного reactor-test: ... reactor-test:3.8.5 приходит транзитивно».
- `build-logic/com.example.spring-boot-client-rest.gradle.kts` — [REVIEW] — `id("com.example.spring-boot")`, зависимость `spring-boot-starter-restclient`(`-test`) — имя плагина = переименованный `restclient`, как в «com.example.spring-boot-client-rest / com.example.spring-boot-client-web (были restclient/webclient) — переименованы».
- `build-logic/com.example.spring-boot-application.gradle.kts` — [REVIEW] — `id("com.example.spring-boot")` + `id("org.springframework.boot")` — ровно вторая (bootable) ось иерархии, как задокументировано в «Convention plugins — принцип именования и структура». С 2026-07-14 сюда же перенесён `spring-boot-starter-actuator`(`-test`) из удалённого `com.example.spring-boot-actuator` — actuator привязан к bootable-оси, не к BOM-цепочке, см. «⚡ Задачи»
- `build-logic/com.example.spring-boot-actuator.gradle.kts` — [REMOVED] 2026-07-14 — раньше `id("com.example.spring-boot")` + `spring-boot-starter-actuator`(`-test`), применялся вручную только в `registry`/`config`/`gateway`; удалён целиком, зависимость перенесена в `com.example.spring-boot-application` (bootable-ось) — см. «⚡ Задачи»; строка сохранена для истории, не удалена целиком (записи о принятых в прошлом файлах не стираются молча)
- `build-logic/com.example.reactor.gradle.kts` — [REVIEW] — `id("com.example.base")` — родитель сменён с `library` на `base`, как задокументировано; `reactor-core`/`reactor-tools` (`implementation`) + `reactor-test` (`testImplementation`) из `libs.findVersion("reactor-core")` = 3.8.6, без `io.spring.dependency-management` — совпадает дословно с «com.example.reactor» и «Синхронизация версий» → reactor-core.
- `build-logic/com.example.spring-boot-r2dbc-h2-database.gradle.kts` — [REVIEW] — новый (2026-07-14): `id("com.example.spring-boot")` + `runtimeOnly("io.r2dbc:r2dbc-h2")` — R2DBC-аналог `com.example.spring-boot-h2-database` (JDBC); без `spring-boot-h2console` — тот требует Servlet-приложение + JDBC `DataSource`, ни того ни другого нет в `webflux`+R2DBC-стеке (`H2ConsoleAutoConfiguration` не активируется)
- `build-logic/com.example.nullaway.gradle.kts` — [REVIEW] — `id("java-library")` + `id("net.ltgt.errorprone")`, без `com.example`-родителя (вне BOM-цепочки через `base`) — совпадает с «исключение — com.example.nullaway» и «следствие: все модули транзитивно получают java-library через base → codequality → nullaway». `jspecify`/`errorprone-core`/`nullaway` — из каталога, версии совпадают.
- `build-logic/com.example.library.gradle.kts` — [REVIEW] — `id("com.example.base")` + `id("java-library")` — 1 родитель `base`, без Spring — совпадает с «com.example.library — 1 родитель com.example.base; добавляет Gradle-плагин java-library, без Spring».
- `build-logic/com.example.javaformat.gradle.kts` — [REMOVED] 2026-07-13 — раньше `id("io.spring.javaformat")` + `id("checkstyle")`, независимо конфигурировал тот же extension `checkstyle {}` (`toolVersion`, `configFile`, `configProperties`) параллельно с `com.example.checkstyle`; удалён целиком, вся конфигурация (включая зависимость `spring-javaformat-checkstyle`) перенесена в `com.example.checkstyle.gradle.kts` — см. «Задачи» → «Форматирование без авто-форматтера»
- `build-logic/com.example.jacoco.gradle.kts` — [REVIEW] — `id("jacoco")` без `com.example`-родителя, `toolVersion` из `libs.findVersion("jacoco")` = 0.8.15 (было 0.8.14 до 2026-07-13) через каталог — совпадает с «jacoco — id("jacoco"); id("java") не нужен» и «Синхронизация версий».
- `build-logic/com.example.jacoco-report-aggregation.gradle.kts` — [REVIEW] — `id("jacoco-report-aggregation")`, вообще без родителя и без доп. конфигурации — совпадает с «jacoco-report-aggregation — без родителя вообще (autoconfig)».
- `build-logic/com.example.codequality.gradle.kts` — [REVIEW] — Агрегирует 4 плагина (`checkstyle`, `nullaway`, `jacoco`, `jacoco-report-aggregation`; было 5 с `javaformat` до 2026-07-13) — совпадает с «агрегатор 4 плагинов выше»; не применяет `com.example.base` — подтверждает «цикла не возникает: codequality-плагины не применяют com.example.base».
- `build-logic/com.example.checkstyle.gradle.kts` — [REVIEW] — `id("checkstyle")` без `com.example.*`-родителя, `id("java")` не подключается — совпадает с «checkstyle — id("checkstyle"); без com.example.* родителя; id("java") не нужен». `toolVersion` + `checkstyle`-зависимость (puppycrawl) из `libs.findVersion("checkstyle")` = 9.3 — через каталог. С 2026-07-13 дополнительно несёт `configFile`/`configProperties` (`gradle/checkstyle/checkstyle.xml`, `projectRootPackage=com.example`) и зависимость `spring-javaformat-checkstyle` (версия `spring-javaformat` = `0.0.48-SNAPSHOT`) — перенесены из удалённого `com.example.javaformat`, единственный источник конфигурации checkstyle в проекте. Собственный `repositories { maven { url = uri("https://repo.spring.io/snapshot") } }` добавлен тем же днём — нужен для резолва SNAPSHOT-координаты, которой нет на `mavenCentral()`
- `build-logic/com.example.base.gradle.kts` — [REVIEW] — `id("java")` + `id("com.example.codequality")`, toolchain через `providers.fileContents(...).asText` (Provider API) из `.java-version`, `junit-jupiter`/`junit-platform-launcher` из каталога (`testImplementation`/`testRuntimeOnly`), `useJUnitPlatform()` — дословно совпадает с «Иерархия» (единственный родитель — `codequality`) и «Синхронизация версий» → Java/junit-jupiter. Версии в каталоге — 6.1.2/6.1.2 (были 6.0.3/6.0.3 до 2026-07-13) — согласуются с тем, что Spring Boot 4.1.0 продолжает управлять JUnit 6-й линейкой. С 2026-07-14 сюда же добавлен `jakarta.validation:jakarta.validation-api:3.1.1` (`implementation`, версия синхронизирована с тем, что резолвит Spring Boot 4.1.0 BOM) — единственный плагин, применяемый `domain/`-модулями напрямую, поэтому единственное место, где `jakarta.validation.constraints.*`-аннотации становятся видны в чистой Java без затягивания Spring, см. «⚡ Задачи» и «Открытые решения» → Jakarta Bean Validation

### auth/ (6 файлов)

Skeleton-модуль (`com.example.spring-boot-application`, логики нет). `src/test/resources/application.properties` — пустой файл.

#### auth/application/ (6 файлов)
- `auth/build.gradle.kts` — [REVIEW] — только `spring-boot-application` — соответствует статусу СКЕЛЕТ, логики нет; с 2026-07-14 транзитивно несёт и actuator (перенесён в `com.example.spring-boot-application`, см. «⚡ Задачи») — раньше actuator не было ни в одном из трёх CRUD-сервисов и в `auth/`, только в `registry`/`config`/`gateway`
- `auth/src/test/resources/application.properties` — [REVIEW] — файл пустой — вероятно, задел на будущее
- `auth/src/test/java/com/example/auth/AuthApplicationTests.java` — [REVIEW] — только `contextLoads()`
- `auth/src/main/resources/application.properties` — [REVIEW] — только `spring.application.name`
- `auth/application/src/main/java/com/example/auth/package-info.java` — [DONE] — `@NullMarked`
- `auth/application/src/main/java/com/example/auth/AuthApplication.java` — [REVIEW] — пустой `@SpringBootApplication`-класс без логики

### .github/ (1 файл)
- `.github/workflows/gradle.yml` — [REVIEW] — гоняет `./gradlew build` (без `clean`) на push/PR в `main`; не разведены `check`/`build`, в отличие от локального workflow из раздела «Правила». Править только по отдельному запросу (правило «CI»).

---

## ⛔ Референс: полный набор Spring Boot стартеров (объединение всех присланных, максимальный) — ЗАПРЕЩЕНО УДАЛЯТЬ ИЛИ СОКРАЩАТЬ

**Прямое повторное указание пользователя (2026-07-14, четыре раза подряд) — этот блок и его заголовок никогда не трогать ни при какой задаче по сокращению/актуализации CLAUDE.md, включая явную задачу «уменьши размер файла».** Эталонный полный набор Spring Boot стартеров (Initializr «select all») — источник истины при проверке полноты convention-плагинов на предмет пропущенных `-test`-компаньонов/plain-API-вариантов при добавлении новой технологии в проект (см. «Правила» → «Полнота R2DBC/JDBC-стартеров», [[feedback_add_all_starters_no_pruning]] в памяти). **Уже подтвердил свою ценность**: по нему найден и исправлен реальный пробел в `com.example.spring-boot-data-elasticsearch` — не хватало plain `spring-boot-starter-elasticsearch`(`-test`) рядом с `spring-boot-starter-data-elasticsearch`(`-test`), тот же паттерн, что раньше нашли для JDBC/R2DBC.

**Объединено 2026-07-14 из 4 присланных Initializr-снапшотов** (восстановлены из сырого лога сессии — после компакции сам текст пастов был недоступен, только их резюме): (1) снапшот-«матрица» data/persistence-стартеров, Boot 4.1.0 (сохранён отдельно в памяти как `reference_spring_boot_data_starters_matrix.md`) — ему предшествовал точечный R2DBC+H2 снапшот (тот самый, с которого начался весь разговор про plain-API-стартеры), но он оказался строгим подмножеством этого снапшота (все 8 его строк уже есть в снапшоте-«матрице», проверено программно) — самостоятельным пятым источником не считается; (2) «select all» с Lombok+Vaadin, Boot 4.1.0; (3) «select all» без Lombok, с Vaadin, Boot 4.1.0; (4) «select all» с Azure/GCP/Tanzu/gRPC/Sentry, Boot 4.0.7 (был единственным источником этого блока раньше). Объединение — по коду проекта (Python-скрипт, не вручную): каждый стартер, встретившийся хотя бы в одном снапшоте, попал в итоговый список; при конфликте версий одного плагина/BOM-переменной — выбрана большая (сравнение по числовым компонентам версии); один случайный дубль строки (`spring-security-messaging`, дважды в снапшоте (4)) — убран как явный артефакт вставки, не переносился в CLAUDE.md изначально. Итог — 7 новых стартеров, которых не было в прежнем единственном источнике (снапшот (4)): `spring-boot-starter-batch-data-mongodb`(`-test`), `spring-boot-starter-grpc-client`(`-test`), `spring-boot-starter-grpc-server`(`-test`), `spring-integration-grpc` — Boot 4.1.0 переименовал/добавил нативные gRPC-стартеры, которых не было в более старом снапшоте (4) на Boot 4.0.7. Versions: `org.springframework.boot` 4.0.7→**4.1.0**, `org.hibernate.orm` 7.2.19.Final→**7.4.1.Final**, `org.graalvm.buildtools.native` 0.11.5→**1.1.1**, `com.google.protobuf` (plugin) 0.9.5→**0.9.6**, `com.vaadin` 25.1.8→**25.2.1**, `springBootAdminVersion` 4.0.4→**4.1.2**, `springModulithVersion` 2.0.7→**2.1.0**, `vaadinVersion` 25.1.8→**25.2.1** — везде взята старшая из встретившихся, не досверялась WebSearch'ем отдельно (см. «Правила» → источник истины для Spring-фактов: сравнение версий между уже предоставленными пользователем референсами не требует Maven Central/WebSearch, там нет вопроса о существовании артефакта). **Перенесено в конец файла 2026-07-14** (было в разделе «⚡ Задачи») — по прямому указанию пользователя, как отдельный самодостаточный референс-раздел.

```gradle
plugins {
    id 'java'
    id 'org.springframework.boot' version '4.1.0'
    id 'io.spring.dependency-management' version '1.1.7'
    id 'com.netflix.dgs.codegen' version '8.3.0'
    id 'org.hibernate.orm' version '7.4.1.Final'
    id 'org.graalvm.buildtools.native' version '1.1.1'
    id 'org.cyclonedx.bom' version '3.2.4'
    id 'org.springframework.cloud.contract' version '5.0.3'
    id 'com.google.protobuf' version '0.9.6'
    id 'org.asciidoctor.jvm.convert' version '4.0.5'
    id 'com.vaadin' version '25.2.1'
}

group = 'com.example'
version = '0.0.1-SNAPSHOT'

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
    maven { url = 'https://build.shibboleth.net/maven/releases' }
}

ext {
    set('snippetsDir', file("build/generated-snippets"))
    set('datasourceMicrometerVersion', "2.2.1")
    set('sentryVersion', "8.27.0")
    set('springBootAdminVersion', "4.1.2")
    set('springCloudAzureVersion', "7.3.0")
    set('springCloudGcpVersion', "8.0.5")
    set('springCloudServicesVersion', "4.4.1")
    set('springCloudVersion', "2025.1.2")
    set('springGrpcVersion', "1.0.3")
    set('springModulithVersion', "2.1.0")
    set('tanzuSpringSdkVersion', "1.0.0")
    set('vaadinVersion', "25.2.1")
}

dependencies {
    implementation 'com.azure.spring:spring-cloud-azure-starter'
    implementation 'com.azure.spring:spring-cloud-azure-starter-active-directory'
    implementation 'com.azure.spring:spring-cloud-azure-starter-actuator'
    implementation 'com.azure.spring:spring-cloud-azure-starter-data-cosmos'
    implementation 'com.azure.spring:spring-cloud-azure-starter-integration-storage-queue'
    implementation 'com.azure.spring:spring-cloud-azure-starter-jdbc-mysql'
    implementation 'com.azure.spring:spring-cloud-azure-starter-jdbc-postgresql'
    implementation 'com.azure.spring:spring-cloud-azure-starter-keyvault'
    implementation 'com.azure.spring:spring-cloud-azure-starter-storage'
    implementation 'com.google.cloud:spring-cloud-gcp-starter'
    implementation 'com.google.cloud:spring-cloud-gcp-starter-pubsub'
    implementation 'com.google.cloud:spring-cloud-gcp-starter-storage'
    implementation 'com.vaadin:vaadin-spring-boot-starter'
    implementation 'com.vmware.tanzu.spring:tanzu-spring-starter'
    implementation 'de.codecentric:spring-boot-admin-starter-client'
    implementation 'de.codecentric:spring-boot-admin-starter-server'
    implementation 'io.grpc:grpc-services'
    implementation 'io.pivotal.spring.cloud:spring-cloud-services-starter-config-client'
    implementation 'io.pivotal.spring.cloud:spring-cloud-services-starter-service-registry'
    implementation 'io.sentry:sentry-spring-boot-4-starter'
    implementation 'net.ttddyy.observation:datasource-micrometer-opentelemetry'
    implementation 'net.ttddyy.observation:datasource-micrometer-spring-boot'
    implementation 'org.apache.kafka:kafka-streams'
    implementation 'org.flywaydb:flyway-database-db2'
    implementation 'org.flywaydb:flyway-database-derby'
    implementation 'org.flywaydb:flyway-database-hsqldb'
    implementation 'org.flywaydb:flyway-database-oracle'
    implementation 'org.flywaydb:flyway-database-postgresql'
    implementation 'org.flywaydb:flyway-mysql'
    implementation 'org.flywaydb:flyway-sqlserver'
    implementation 'org.jobrunr:jobrunr-spring-boot-4-starter:8.7.0'
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.2'
    implementation 'org.springframework.amqp:spring-rabbit-stream'
    implementation 'org.springframework.boot:spring-boot-h2console'
    implementation 'org.springframework.boot:spring-boot-starter-activemq'
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
    implementation 'org.springframework.boot:spring-boot-starter-amqp'
    implementation 'org.springframework.boot:spring-boot-starter-artemis'
    implementation 'org.springframework.boot:spring-boot-starter-batch'
    implementation 'org.springframework.boot:spring-boot-starter-batch-data-mongodb'
    implementation 'org.springframework.boot:spring-boot-starter-batch-jdbc'
    implementation 'org.springframework.boot:spring-boot-starter-cache'
    implementation 'org.springframework.boot:spring-boot-starter-cassandra'
    implementation 'org.springframework.boot:spring-boot-starter-cloudfoundry'
    implementation 'org.springframework.boot:spring-boot-starter-couchbase'
    implementation 'org.springframework.boot:spring-boot-starter-data-cassandra'
    implementation 'org.springframework.boot:spring-boot-starter-data-cassandra-reactive'
    implementation 'org.springframework.boot:spring-boot-starter-data-couchbase'
    implementation 'org.springframework.boot:spring-boot-starter-data-couchbase-reactive'
    implementation 'org.springframework.boot:spring-boot-starter-data-elasticsearch'
    implementation 'org.springframework.boot:spring-boot-starter-data-jdbc'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-data-ldap'
    implementation 'org.springframework.boot:spring-boot-starter-data-mongodb'
    implementation 'org.springframework.boot:spring-boot-starter-data-mongodb-reactive'
    implementation 'org.springframework.boot:spring-boot-starter-data-neo4j'
    implementation 'org.springframework.boot:spring-boot-starter-data-r2dbc'
    implementation 'org.springframework.boot:spring-boot-starter-data-redis'
    implementation 'org.springframework.boot:spring-boot-starter-data-redis-reactive'
    implementation 'org.springframework.boot:spring-boot-starter-data-rest'
    implementation 'org.springframework.boot:spring-boot-starter-elasticsearch'
    implementation 'org.springframework.boot:spring-boot-starter-flyway'
    implementation 'org.springframework.boot:spring-boot-starter-freemarker'
    implementation 'org.springframework.boot:spring-boot-starter-graphql'
    implementation 'org.springframework.boot:spring-boot-starter-groovy-templates'
    implementation 'org.springframework.boot:spring-boot-starter-grpc-client'
    implementation 'org.springframework.boot:spring-boot-starter-grpc-server'
    implementation 'org.springframework.boot:spring-boot-starter-hateoas'
    implementation 'org.springframework.boot:spring-boot-starter-hazelcast'
    implementation 'org.springframework.boot:spring-boot-starter-integration'
    implementation 'org.springframework.boot:spring-boot-starter-jdbc'
    implementation 'org.springframework.boot:spring-boot-starter-jersey'
    implementation 'org.springframework.boot:spring-boot-starter-jooq'
    implementation 'org.springframework.boot:spring-boot-starter-kafka'
    implementation 'org.springframework.boot:spring-boot-starter-ldap'
    implementation 'org.springframework.boot:spring-boot-starter-liquibase'
    implementation 'org.springframework.boot:spring-boot-starter-mail'
    implementation 'org.springframework.boot:spring-boot-starter-mongodb'
    implementation 'org.springframework.boot:spring-boot-starter-mustache'
    implementation 'org.springframework.boot:spring-boot-starter-neo4j'
    implementation 'org.springframework.boot:spring-boot-starter-opentelemetry'
    implementation 'org.springframework.boot:spring-boot-starter-pulsar'
    implementation 'org.springframework.boot:spring-boot-starter-quartz'
    implementation 'org.springframework.boot:spring-boot-starter-r2dbc'
    implementation 'org.springframework.boot:spring-boot-starter-restclient'
    implementation 'org.springframework.boot:spring-boot-starter-rsocket'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-security-oauth2-authorization-server'
    implementation 'org.springframework.boot:spring-boot-starter-security-oauth2-client'
    implementation 'org.springframework.boot:spring-boot-starter-security-oauth2-resource-server'
    implementation 'org.springframework.boot:spring-boot-starter-security-saml2'
    implementation 'org.springframework.boot:spring-boot-starter-session-data-redis'
    implementation 'org.springframework.boot:spring-boot-starter-session-jdbc'
    implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.springframework.boot:spring-boot-starter-webclient'
    implementation 'org.springframework.boot:spring-boot-starter-webflux'
    implementation 'org.springframework.boot:spring-boot-starter-webmvc'
    implementation 'org.springframework.boot:spring-boot-starter-webservices'
    implementation 'org.springframework.boot:spring-boot-starter-websocket'
    implementation 'org.springframework.boot:spring-boot-starter-zipkin'
    implementation 'org.springframework.cloud:spring-cloud-bus'
    implementation 'org.springframework.cloud:spring-cloud-config-server'
    implementation 'org.springframework.cloud:spring-cloud-function-web'
    implementation 'org.springframework.cloud:spring-cloud-starter'
    implementation 'org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j'
    implementation 'org.springframework.cloud:spring-cloud-starter-config'
    implementation 'org.springframework.cloud:spring-cloud-starter-consul-config'
    implementation 'org.springframework.cloud:spring-cloud-starter-consul-discovery'
    implementation 'org.springframework.cloud:spring-cloud-starter-gateway-server-webflux'
    implementation 'org.springframework.cloud:spring-cloud-starter-gateway-server-webmvc'
    implementation 'org.springframework.cloud:spring-cloud-starter-loadbalancer'
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-server'
    implementation 'org.springframework.cloud:spring-cloud-starter-openfeign'
    implementation 'org.springframework.cloud:spring-cloud-starter-task'
    implementation 'org.springframework.cloud:spring-cloud-starter-vault-config'
    implementation 'org.springframework.cloud:spring-cloud-starter-zookeeper-config'
    implementation 'org.springframework.cloud:spring-cloud-starter-zookeeper-discovery'
    implementation 'org.springframework.cloud:spring-cloud-stream'
    implementation 'org.springframework.cloud:spring-cloud-stream-binder-kafka'
    implementation 'org.springframework.cloud:spring-cloud-stream-binder-kafka-streams'
    implementation 'org.springframework.cloud:spring-cloud-stream-binder-pulsar'
    implementation 'org.springframework.cloud:spring-cloud-stream-binder-rabbit'
    implementation 'org.springframework.data:spring-data-rest-hal-explorer'
    implementation 'org.springframework.grpc:spring-grpc-client-spring-boot-starter'
    implementation 'org.springframework.grpc:spring-grpc-server-web-spring-boot-starter'
    implementation 'org.springframework.integration:spring-integration-amqp'
    implementation 'org.springframework.integration:spring-integration-grpc'
    implementation 'org.springframework.integration:spring-integration-http'
    implementation 'org.springframework.integration:spring-integration-jdbc'
    implementation 'org.springframework.integration:spring-integration-jms'
    implementation 'org.springframework.integration:spring-integration-jpa'
    implementation 'org.springframework.integration:spring-integration-kafka'
    implementation 'org.springframework.integration:spring-integration-mail'
    implementation 'org.springframework.integration:spring-integration-mongodb'
    implementation 'org.springframework.integration:spring-integration-r2dbc'
    implementation 'org.springframework.integration:spring-integration-redis'
    implementation 'org.springframework.integration:spring-integration-rsocket'
    implementation 'org.springframework.integration:spring-integration-stomp'
    implementation 'org.springframework.integration:spring-integration-webflux'
    implementation 'org.springframework.integration:spring-integration-websocket'
    implementation 'org.springframework.integration:spring-integration-ws'
    implementation 'org.springframework.modulith:spring-modulith-events-api'
    implementation 'org.springframework.modulith:spring-modulith-starter-core'
    implementation 'org.springframework.modulith:spring-modulith-starter-insight'
    implementation 'org.springframework.modulith:spring-modulith-starter-jdbc'
    implementation 'org.springframework.modulith:spring-modulith-starter-jpa'
    implementation 'org.springframework.modulith:spring-modulith-starter-mongodb'
    implementation 'org.springframework.modulith:spring-modulith-starter-neo4j'
    implementation 'org.springframework.security:spring-security-messaging'
    implementation 'org.springframework.security:spring-security-rsocket'
    implementation 'org.springframework.security:spring-security-webauthn'
    implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity6'
    compileOnly 'org.projectlombok:lombok'
    developmentOnly 'com.azure.spring:spring-cloud-azure-docker-compose'
    developmentOnly 'com.vaadin:vaadin-dev'
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
    developmentOnly 'org.springframework.boot:spring-boot-docker-compose'
    runtimeOnly 'com.h2database:h2'
    runtimeOnly 'com.ibm.db2:jcc'
    runtimeOnly 'com.microsoft.sqlserver:mssql-jdbc'
    runtimeOnly 'com.mysql:mysql-connector-j'
    runtimeOnly 'com.oracle.database.jdbc:ojdbc11'
    runtimeOnly 'com.oracle.database.r2dbc:oracle-r2dbc'
    runtimeOnly 'io.asyncer:r2dbc-mysql'
    runtimeOnly 'io.micrometer:micrometer-registry-datadog'
    runtimeOnly 'io.micrometer:micrometer-registry-dynatrace'
    runtimeOnly 'io.micrometer:micrometer-registry-graphite'
    runtimeOnly 'io.micrometer:micrometer-registry-influx'
    runtimeOnly 'io.micrometer:micrometer-registry-new-relic'
    runtimeOnly 'io.micrometer:micrometer-registry-otlp'
    runtimeOnly 'io.micrometer:micrometer-registry-prometheus'
    runtimeOnly 'io.r2dbc:r2dbc-h2'
    runtimeOnly 'io.r2dbc:r2dbc-mssql:1.0.0.RELEASE'
    runtimeOnly 'org.apache.derby:derby'
    runtimeOnly 'org.apache.derby:derbytools'
    runtimeOnly 'org.hsqldb:hsqldb'
    runtimeOnly 'org.mariadb.jdbc:mariadb-java-client'
    runtimeOnly 'org.mariadb:r2dbc-mariadb:1.1.3'
    runtimeOnly 'org.postgresql:postgresql'
    runtimeOnly 'org.postgresql:r2dbc-postgresql'
    runtimeOnly 'org.springframework.modulith:spring-modulith-events-jms'
    runtimeOnly 'org.springframework.modulith:spring-modulith-runtime'
    runtimeOnly 'org.xerial:sqlite-jdbc'
    annotationProcessor 'org.projectlombok:lombok'
    annotationProcessor 'org.springframework.boot:spring-boot-configuration-processor'
    testImplementation 'com.azure.spring:spring-cloud-azure-testcontainers'
    testImplementation 'com.unboundid:unboundid-ldapsdk'
    testImplementation 'io.projectreactor:reactor-test'
    testImplementation 'io.rest-assured:spring-web-test-client'
    testImplementation 'org.springframework.boot:spring-boot-starter-activemq-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-actuator-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-amqp-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-artemis-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-batch-data-mongodb-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-batch-jdbc-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-batch-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-cache-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-cassandra-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-cloudfoundry-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-couchbase-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-data-cassandra-reactive-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-data-cassandra-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-data-couchbase-reactive-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-data-couchbase-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-data-elasticsearch-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-data-jdbc-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-data-jpa-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-data-ldap-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-data-mongodb-reactive-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-data-mongodb-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-data-neo4j-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-data-r2dbc-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-data-redis-reactive-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-data-redis-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-data-rest-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-elasticsearch-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-flyway-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-freemarker-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-graphql-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-groovy-templates-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-grpc-client-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-grpc-server-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-hateoas-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-hazelcast-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-jdbc-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-jersey-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-jooq-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-kafka-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-ldap'
    testImplementation 'org.springframework.boot:spring-boot-starter-ldap-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-liquibase-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-mail-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-mongodb-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-mustache-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-neo4j-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-opentelemetry-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-pulsar-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-quartz-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-r2dbc-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-restclient-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-restdocs'
    testImplementation 'org.springframework.boot:spring-boot-starter-rsocket-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-security-oauth2-authorization-server-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-security-oauth2-client-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-security-oauth2-resource-server-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-security-saml2-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-security-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-session-data-redis-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-session-jdbc-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-thymeleaf-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-validation-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-webclient-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-webflux-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-webmvc-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-webservices-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-websocket-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-zipkin-test'
    testImplementation 'org.springframework.boot:spring-boot-testcontainers'
    testImplementation 'org.springframework.cloud:spring-cloud-starter-contract-stub-runner'
    testImplementation 'org.springframework.cloud:spring-cloud-starter-contract-verifier'
    testImplementation 'org.springframework.cloud:spring-cloud-stream-test-binder'
    testImplementation 'org.springframework.grpc:spring-grpc-test'
    testImplementation 'org.springframework.integration:spring-integration-test'
    testImplementation 'org.springframework.modulith:spring-modulith-starter-test'
    testImplementation 'org.springframework.restdocs:spring-restdocs-mockmvc'
    testImplementation 'org.testcontainers:testcontainers-activemq'
    testImplementation 'org.testcontainers:testcontainers-cassandra'
    testImplementation 'org.testcontainers:testcontainers-consul'
    testImplementation 'org.testcontainers:testcontainers-couchbase'
    testImplementation 'org.testcontainers:testcontainers-db2'
    testImplementation 'org.testcontainers:testcontainers-elasticsearch'
    testImplementation 'org.testcontainers:testcontainers-gcloud'
    testImplementation 'org.testcontainers:testcontainers-grafana'
    testImplementation 'org.testcontainers:testcontainers-junit-jupiter'
    testImplementation 'org.testcontainers:testcontainers-kafka'
    testImplementation 'org.testcontainers:testcontainers-mariadb'
    testImplementation 'org.testcontainers:testcontainers-mongodb'
    testImplementation 'org.testcontainers:testcontainers-mssqlserver'
    testImplementation 'org.testcontainers:testcontainers-mysql'
    testImplementation 'org.testcontainers:testcontainers-neo4j'
    testImplementation 'org.testcontainers:testcontainers-oracle-free'
    testImplementation 'org.testcontainers:testcontainers-postgresql'
    testImplementation 'org.testcontainers:testcontainers-pulsar'
    testImplementation 'org.testcontainers:testcontainers-r2dbc'
    testImplementation 'org.testcontainers:testcontainers-rabbitmq'
    testImplementation 'org.testcontainers:testcontainers-vault'
    testCompileOnly 'org.projectlombok:lombok'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
    testAnnotationProcessor 'org.projectlombok:lombok'
}

dependencyManagement {
    imports {
        mavenBom "org.springframework.modulith:spring-modulith-bom:${springModulithVersion}"
        mavenBom "com.vaadin:vaadin-bom:${vaadinVersion}"
        mavenBom "org.springframework.grpc:spring-grpc-dependencies:${springGrpcVersion}"
        mavenBom "de.codecentric:spring-boot-admin-dependencies:${springBootAdminVersion}"
        mavenBom "io.pivotal.spring.cloud:spring-cloud-services-dependencies:${springCloudServicesVersion}"
        mavenBom "io.sentry:sentry-bom:${sentryVersion}"
        mavenBom "net.ttddyy.observation:datasource-micrometer-bom:${datasourceMicrometerVersion}"
        mavenBom "com.vmware.tanzu.spring:tanzu-spring-sdk-dependencies:${tanzuSpringSdkVersion}"
        mavenBom "com.azure.spring:spring-cloud-azure-dependencies:${springCloudAzureVersion}"
        mavenBom "com.google.cloud:spring-cloud-gcp-dependencies:${springCloudGcpVersion}"
        mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
    }
}

generateJava {
    schemaPaths = ["${projectDir}/src/main/resources/graphql-client"]
    packageName = 'com.example.demo.codegen'
    generateClient = true
}

hibernate {
    enhancement {
    }
}

contracts {
    testMode = 'WebTestClient'
}

protobuf {
    protoc {
        artifact = 'com.google.protobuf:protoc'
    }
    plugins {
        grpc {
            artifact = 'io.grpc:protoc-gen-grpc-java'
        }
    }
    generateProtoTasks {
        all()*.plugins {
            grpc {
                option '@generated=omit'
            }
        }
    }
}

tasks.named('contractTest') {
    useJUnitPlatform()
}

tasks.named('test') {
    outputs.dir snippetsDir
    useJUnitPlatform()
}

tasks.named('asciidoctor') {
    inputs.dir snippetsDir
    dependsOn test
}
```
