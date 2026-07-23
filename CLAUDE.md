# CLAUDE.md — notes-spring

> Последнее обновление: Thu Jul 23 21:15:06 IDT 2026 **Всё временно** — любое решение подлежит обсуждению и изменению.

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

- **⚠️ ИСТОЧНИК ИСТИНЫ ДЛЯ SPRING-ФАКТОВ — НЕ MAVEN CENTRAL ПОИСК** [НЕ СОКРАЩАТЬ/НЕ УДАЛЯТЬ — исключение из правила «убирать избыточность», даже при явной задаче уменьшить объём CLAUDE.md] (2026-07-14, резкая реакция пользователя на ошибку): вопрос «существует ли артефакт X» **никогда** не проверять через `search.maven.org` (Solr-индекс отстаёт от реального репозитория, `0 numFound` НЕ значит «не существует» — конкретная ошибка: заявил, что `spring-boot-starter-r2dbc` не существует по 0 результатам поиска, хотя это реальный отдельный от `data-r2dbc` стартер). Источник истины — **Spring Initializr** (`start.spring.io/metadata/client` — реальный список id/name/description), **GitHub spring-projects**, **официальная документация docs.spring.io**. Maven Central годится только для номера версии уже известного, существующего артефакта — не для вопроса о самом его существовании. **`spring-boot-starters-reference.md`** (2026-07-14) — дословная копия со Spring Initializr, лежит в репозитории локально: для вопроса «существует ли стартер X» и «как называется» обращаться сначала туда, не в web-поиск. При выборе/добавлении стартера под конкретную технологию — сверяться по этому файлу, есть ли для неё отдельный `-test`-компаньон (`spring-boot-starter-{tech}-test`), и подключать его вместе с основным, если существует — не пропускать по своему усмотрению (см. прецедент с `spring-boot-starter-r2dbc`/`-jdbc`/`-elasticsearch`, где `-test`-версия изначально была упущена). Также перед добавлением зависимости в Spring Boot модуль — сверяться, нет ли для этой технологии уже готового `spring-boot-starter-*`: если есть, подключать именно его, а не «сырую» внешнюю зависимость напрямую (starter уже несёт нужный BOM-managed набор транзитивных зависимостей и autoconfiguration — обходить его напрямую значит терять то и другое)
- **Язык** — общение на русском; код и идентификаторы — на английском
- **Файлы** — не изменять без явного указания; перед созданием нового файла изучить проект
- **Актуальность `file-catalog.md`** — при любом создании/удалении/переименовании файла в проекте синхронизировать `file-catalog.md`: новый файл — строка со статусом `[REVIEW]`, реально удалённый — `[REMOVED]` (строка сохраняется для истории), переименованный/перемещённый — обновить путь. Делать сразу как часть той же правки, не откладывая до отдельного запроса; сами правила допустимых изменений каталога — см. «Лимит размера файла» ниже
- **Коммиты** — не коммитить без явного запроса, кроме начала сессии (см. выше); «Зафиксируй» = обновить CLAUDE.md и дату → коммит → пуш
- **Дата перед коммитом** — брать из `date +"%a %b %d %H:%M:%S %Z %Y"` (локальное время, формат как в `gradle-wrapper.properties`), не выдумывать
- **CI** — не изменять `.github/workflows/` без отдельного запроса
- **Списки вместо таблиц** — структурированные данные (путь/статус/комментарий и т. п.) — плоским списком `- поле — поле — поле`, не markdown-таблицей: правка одной записи не требует пересчёта соседних строк
- **Без ручных переносов строк** — один абзац/пункт/строка blockquote = одна строка файла, без жёсткого переноса на 80–120 символов; перенос только смысловой
- **Доступность CLAUDE.md для ИИ-агента** — формат оптимизирован для чтения/правки ИИ без потери информации: полный путь от корня репозитория в каждой строке каталога (строка самодостаточна без соседних заголовков), ≤ 2 уровня вложенности заголовков (`### сервис/` → `#### модуль/`), ASCII-статусы вместо emoji, явная схема полей перед списком — применять сразу, без подтверждения
- **Архитектурные решения** — сначала варианты с плюсами/минусами и рекомендацией, дождаться выбора пользователя; не реализовывать до явного решения
- **Пересмотр решений** — статус «принято»/«зафиксировано» (в т. ч. в «Принятые решения») не закрывает вопрос навсегда: любое решение по CRUD-сервисам и по проекту в целом нужно поднимать заново для обсуждения и пересмотра в подходящий момент, а не только по отдельному запросу
- **Кольцевые/ромбовидные зависимости — отслеживать всегда, без исключения для «кажется безопасным»** (введено 2026-07-14, ужесточено 2026-07-15): при любой правке convention-плагинов, Gradle project-зависимостей (`projects.*`) или Java-иерархий портов/адаптеров — проверять граф на циклы и на схождение путей (диамант). Сообщать о **каждой** находке пользователю — что найдено, уровень опасности, вариант(ы) устранения — даже если диамант доказанно безопасен и не создаёт видимых проблем (техническая безопасность — не повод оставить как есть по умолчанию или промолчать; решение оставить/устранить — за пользователем, не за агентом). Прецедент: диамант `spring-cloud-config-server`/`eureka-server`/`gateway-webflux`/`gateway-webmvc` → `spring-cloud`+`spring-boot-application` → `spring-boot` был доказан безопасным (идемпотентность Gradle `PluginManager`, баг gradle/gradle#13252 про cross-project apply — исключён грепом), но пользователь всё равно потребовал устранить — решено новым линейным плагином `com.example.spring-cloud-application` (см. «Принятые решения» → Convention plugins). Никаких shared-функций/helper-кода в `build-logic/` для устранения подобных схождений — только композиция через `id(...)` (явный отказ пользователя от варианта с общей Kotlin-функцией). **Два разных вида схождения — не путать**: вид 1 — диамант внутри самого convention-плагина (файл в `build-logic/` сам в своём `plugins{}` вызывает 2+ `id("com.example.*")`, сходящихся в общем предке) — устранять по этому правилу, прецедент выше. Вид 2 — листовой модуль (`build.gradle.kts` сервиса) напрямую применяет 2+ convention-плагина, сходящихся в общем предке (например `note/application` — `spring-boot-application`+`spring-boot-h2-database`; `gateway/application` — сразу 3 `spring-cloud-*`) — **не диамант для устранения**, а осознанная композиция независимых convention-плагинов в точке использования, ради которой вся система convention-плагинов и построена (решено пользователем 2026-07-15, не поднимать заново без нового явного повода)
- **Только convention-плагины — листовые модули не применяют ничего напрямую** (проверено и закреплено 2026-07-15): ни один из 39 листовых `build.gradle.kts` (`note`/`user`/`user-note`/`auth`/`registry`/`config`/`gateway`) не должен применять plugin id, не начинающийся с `com.example.*`, и не должен объявлять зависимость иначе как `implementation(projects....)`/`api(projects....)` (project-to-project). Все внешние зависимости, плагины, версии и настройка сборки — только через `build-logic/convention/`. Проверка: `grep -rn 'id("' --include="build.gradle.kts" note user user-note auth registry config gateway | grep -v 'id("com\.example\.'` и аналогично по `dependencies{}`-блокам — оба должны быть пустыми. Прогонять эту проверку при добавлении новой зависимости/плагина в любой листовой модуль
- **Плагины-расширители (`java-library` и подобные) — только там, где реально нужна расширенная конфигурация, не каскадом «на всякий случай»** (введено 2026-07-15): прецедент — `com.example.jspecify` подключал `id("java-library")` только ради `api("org.jspecify")`, и через каскад `base → codequality → jspecify` эта конфигурация просачивалась во все 39 модулей, хотя видимость зависимости на потребителях от `api`/`implementation` не зависела (jspecify достаётся каждому модулю напрямую через собственный каскад `id(...)`, а не через project-to-project зависимость, где `api` реально что-то протаскивает дальше). Исправлено на `id("java")` + `implementation(...)`; `java-library` остался только в `com.example.library`, применяемом 6 модулями `contract`/`contract-reactive`, где `api(projects.*.domain)` — настоящая публичная сигнатура. При добавлении/правке convention-плагина, если он подключает id, расширяющий базовый (`java-library` поверх `java` и аналоги) — проверять: (1) нужна ли расширенная конфигурация именно этому плагину для собственной декларации зависимостей, или сводится к базовой; (2) не полагаться на то, что зависимость «протекает» api-путём к потребителям, если фактическая топология — каскадное применение `id(...)` внутри одного проекта, а не project-to-project. Проверка: `grep -rln 'id("java-library")'` по `build-logic/convention/` — список плагинов должен совпадать со списком, где реально объявлен `api(...)`
- **Build** — после каждого логического шага: `./gradlew clean check` (быстрее `build`: без `assemble`/`bootJar`) → обновить CLAUDE.md → коммит → пуш; перед коммитом дополнительно `./gradlew clean build` (проверяет и паковку — `bootJar`/`resolveMainClassName`, что `check` не покрывает)
- **Подзадачи** — крупную задачу разбивать на подзадачи; коммитить и пушить после каждой завершённой подзадачи, чтобы не терять прогресс при обрыве сессии
- **⚠️ Лимит размера файла — при превышении сокращать самостоятельно, без напоминания** (2026-07-14, актуализировано 2026-07-14 по прямому запросу пользователя — три ограничения ниже закреплены здесь именно затем, чтобы не повторять их в каждом запросе «сократи файл»): триггер — сообщение `⚠ CLAUDE.md is over the 150.0k-char limit`. Мерить `wc -m CLAUDE.md`, **не** `wc -c` — тот считает байты UTF-8, а из-за кириллицы (2 байта/символ) байты почти вдвое больше реального числа символов, на котором основан лимит (проверено эмпирически 2026-07-14). Три вещи трогать нельзя, только переформулировать:
  1. Раздел «Правила» — ни одна инструкция не теряется по смыслу; формулировку каждого пункта можно сжимать (кроме отдельно помеченных исключений, см. правило про Maven Central выше)
  2. `spring-boot-starters-reference.md` (вынесен из CLAUDE.md 2026-07-14, см. «Каталог файлов»/«Референс стартеров» выше) — сам список зависимостей (и сам gradle-код) не сокращать и не удалять из него строки; допустима только проверка на дубликаты. Пояснительный текст вокруг списка и в CLAUDE.md — редактировать можно
  3. `file-catalog.md` (вынесен из CLAUDE.md 2026-07-14) — список файлов и их статусы ([DONE]/[REVIEW]/[ADD]/[REMOVED]) не менять и не удалять по инициативе агента (можно добавлять пропущенные файлы `[REVIEW]`, можно убирать строки реально удалённых файлов, `[DONE]` — только по явному указанию пользователя); комментарий к каждому файлу — можно свободно сокращать вплоть до полного удаления
  Всё остальное (в первую очередь «Задачи»/«Принятые решения»/«Открытые решения» — исторические подробности) сокращать без ограничений, не теряя решения по существу

---

## ⚡ Задачи

**Текущая работа** — построчный пересмотр «Каталог файлов проекта»: ровно один файл за раз — разбор → утверждение пользователем → `[DONE]` → следующая строка `[REVIEW]` сверху вниз. Не пакетами, не забегая вперёд.

**Завершено 2026-07-23**: сверка версий стека с GitHub releases API — актуально всё, кроме NullAway `0.13.7`→`0.13.8` (вышел 2026-07-19; заметное изменение — добавлена поддержка Reactor, актуально для проекта с активным `reactor-core`). `./gradlew clean check` пройден, новых warnings не появилось

**Завершено 2026-07-23** (детали — см. «Принятые решения» → «Архитектура» → «Технологическая и вендорная ось адаптеров»): полная матрица composition-root модулей — 5 на сервис (`application-jpa`/`application-jdbc`/`application-mongodb`/`application-r2dbc`/`application-mongodb-reactive`, было 2) × 3 сервиса = 15; вендорная ось H2/PostgreSQL/MySQL для JPA/JDBC/R2DBC через 4 новых convention-плагина + рантайм-профили, без новых Gradle-модулей. `application/`→`application-jpa/`, `application-reactive/`→`application-r2dbc/` (классы и `spring.application.name` тоже переименованы). Все узлы верифицированы вживую: H2-дефолт (в т. ч. R2DBC — embedded-detection есть и там, не только у JDBC), PostgreSQL-профиль (`HikariPool` реально подключился). Testcontainers/Docker Compose для MongoDB построены и верифицированы, но в тот же день сняты с применения по замечанию о непоследовательности (см. «Принятые решения») — convention-плагины остались про запас. `./gradlew clean check` пройден на каждом шаге

**Завершено 2026-07-15** (детали — см. «Принятые решения» → «Convention plugins», «Правила»):
- Диамант у 4 standalone `spring-cloud-*`-плагинов (`config-server`/`eureka-server`/`gateway-webflux`/`gateway-webmvc`) сначала устранён новым линейным плагином `com.example.spring-cloud-application` (единственный родитель `spring-boot-application`, `dependencyManagement`-блок продублирован из `spring-cloud`), затем в тот же день пользователь пересмотрел это решение: `spring-cloud-application` переведён на 2 прямых родителя — `spring-cloud`+`spring-boot-application` — убирает дублирование кода ценой воссоздания того же диаманта. Диамант был доказанно безопасен (идемпотентность Gradle `PluginManager`, баг gradle/gradle#13252 исключён грепом); агент явно указал на воссоздание диаманта и рекомендовал линейный вариант — пользователь всё равно выбрал композицию. Правило «Кольцевые/ромбовидные зависимости» в «Правила» ужесточено: отслеживать и сообщать всегда, решение — за пользователем. Без shared-функций (явный отказ пользователя) — только композиция `id(...)`
- Разграничены два вида схождения путей: вид 1 (диамант внутри самого convention-плагина) — устранять; вид 2 (листовой модуль напрямую применяет 2+ convention-плагина, например `note/application` — `spring-boot-application`+`spring-boot-h2-database`) — решено пользователем: НЕ диамант, осознанная композиция, ради которой и построена система convention-плагинов, не трогать
- `jspecify` вынесен из `com.example.nullaway` в отдельный `com.example.jspecify`, применяется через `com.example.codequality` (стало 5 плагинов). `com.example.nullaway` переведён на `id("java")` вместо `id("java-library")` — `api(jspecify)` была единственной причиной `java-library` там, после выноса стала не нужна. **Пересмотрено в тот же день**: `com.example.jspecify` изначально был `id("java-library")` + `api(...)`, из-за чего `java-library` каскадом через `base → codequality → jspecify` протекал во все 39 модулей — не обязанность jspecify решать это за всех потребителей. Исправлено на `id("java")` + `implementation(...)` (видимость на потребителях не зависит от `api`/`implementation` здесь — jspecify достаётся каждому модулю напрямую через собственный каскад `id(...)`, а не через project-to-project зависимость). `java-library` теперь только там, где реально нужен `api()` — явно в `com.example.library`, применяемом 6 модулями `contract`/`contract-reactive` (`api(projects.*.domain)`); проверено грепом по всем convention-плагинам и 39 build.gradle.kts
- Проверено и закреплено правилом: все 39 листовых модулей применяют только `id("com.example.*")` и только `project()`-зависимости — ни одного самостоятельного plugin id/внешней зависимости в обход convention-плагинов не найдено
- Сверка версий всего стека (`gradle/libs.versions.toml`, Java, Gradle) с официальными источниками (GitHub releases API — надёжнее агрегированного веб-поиска, который на Checkstyle дал ложный результат «уже латест» при первой проверке): актуальны все, кроме Checkstyle — `13.7.0` → `13.8.0` (вышел 2026-07-12, добавляет поддержку JEP 512 compact source files и OpenJDK style guide checks). `./gradlew clean check` пройден

**Завершено 2026-07-14** (детали решений — см. «Принятые решения»/«Именование»/«Стиль кода», здесь только пойнтеры, чтобы не дублировать):
- Аудит кольцевых/ромбовидных зависимостей — по convention-плагинам, Gradle project-зависимостям и Java-иерархии портов: циклов не найдено; единственное на тот момент схождение путей (bootable-ось у 4 standalone `spring-cloud-*`) позже устранено 2026-07-15, см. выше
- `spring-boot-starter-validation`(`-test`) → `com.example.spring-boot`; `spring-boot-starter-actuator`(`-test`) → `com.example.spring-boot-application` (bootable-ось, не BOM-цепочка) — `com.example.spring-boot-actuator` удалён
- `jakarta.validation-api:3.1.1` → `com.example.base` (`implementation`) — делает `jakarta.validation.constraints.*` видимыми в `domain/`; сами аннотации `@NotNull`/`@NotBlank` на полях по-прежнему не расставлены (см. «Открытые решения»)
- `{Entity}Persistable` — общий маркер-интерфейс в `domain/`, реализован всеми 15 model-классами; `user-note.role` унифицирован до `UserNoteRole` enum везде; `schema.sql` создан для `data-jdbc`/`data-r2dbc` во всех трёх сервисах — закрывает «Управление схемой для R2DBC/JDBC»
- `data-jdbc/` переведён с сырого `NamedParameterJdbcTemplate` на Spring Data JDBC (`model/`+`repository/`+`mapper/`, `ListCrudRepository`) по всем трём сервисам — прежнее «без repository» решение признано ошибочным
- Референсы Spring Initializr объединены в единый блок в конце файла

**Завершено 2026-07-13**: убран `io.spring.javaformat` (не даёт переопределить `lineSplit`/`join_wrapped_lines`) — единственный гейт стиля теперь Checkstyle (`SpringChecks` + 5 модулей под 10 личных правил, см. «Стиль кода»), `spring-javaformat` запинен на `0.0.48-SNAPSHOT` (нужные чеки есть только в snapshot, риск мутации принят осознанно). Версии подняты до последних (Java 25, Gradle 9.6.1, Spring Boot 4.1.0, JUnit 6.1.2, Checkstyle 13.8.0, reactor-core 3.8.6, jacoco 0.8.15 — см. «Синхронизация версий»). Переименования: трёхуровневая иерархия портов (`{Entity}Interface`/`ServiceInterface`/`ControllerInterface`), `data-contract`→`contract`, добавлен `application-reactive/` (см. «Именование»). Добавлены `spring-boot-starter-r2dbc`/`-jdbc` (plain API) рядом с `data-r2dbc`/`data-jdbc` по референсу Initializr.

**Пересмотр CRUD-сервисов** (2026-07-07) — статус ГОТОВО подтверждён по note/user/user-note; оставшиеся расхождения см. «Открытые решения».

Статус по сервисам и модулям:

```
ГОТОВО   note/ · user/ · user-note/ — domain · contract · contract-reactive · webmvc · webflux
                  data-jpa · data-mongodb · data-jdbc · data-r2dbc · data-mongodb-reactive
                  application-jpa · application-jdbc · application-mongodb ·
                  application-r2dbc · application-mongodb-reactive (полная технологическая
                  матрица 5×3, PostgreSQL/MySQL — вендорная ось поверх jpa/jdbc/r2dbc)
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
application-jpa/               composition root (sync)      → webmvc + data-jpa
application-jdbc/              composition root (sync)      → webmvc + data-jdbc
application-mongodb/           composition root (sync)      → webmvc + data-mongodb
application-r2dbc/             composition root (reactive)  → webflux + data-r2dbc
application-mongodb-reactive/  composition root (reactive)  → webflux + data-mongodb-reactive
```

Ровно один driven-адаптер на classpath каждого composition root (технологическая ось) — Spring Boot `*RepositoriesAutoConfiguration` активируется без ручных `@EnableXxxRepositories`. Вендор БД (H2/PostgreSQL/MySQL для jpa/jdbc/r2dbc) — не отдельные модули, а `runtimeOnly`-драйверы + `spring.profiles.active`, см. «Принятые решения» → «Архитектура».

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
- **Управление схемой R2DBC/JDBC — `schema.sql`** (2026-07-14): `id UUID DEFAULT RANDOM_UUID() PRIMARY KEY`, `NOT NULL` на непустых колонках, `UNIQUE(...)` где нужно. Flyway/Liquibase не рассматривались (H2, схема простая). Исполняется при старте `application-r2dbc/` и `application-jdbc/` (последний подключён 2026-07-23, см. «Технологическая и вендорная ось адаптеров»)
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

Иерархия — две независимые оси: BOM-цепочка (`base → library/reactor/spring-boot → spring-cloud → tech-plugin`, всегда 1 родитель) и bootable-ось (`org.springframework.boot`, подключается вторым `id(...)` где нужен `bootJar` — не нарушает правило «1 родитель» по первой оси, это отдельное измерение). Плюс агрегирующий `codequality` (5 плагинов):

```
checkstyle      — id("checkstyle"); без com.example.* родителя; id("java") не нужен;
                  сам несёт configFile/configProperties/spring-javaformat-checkstyle dependency
jspecify        — id("java") + implementation("org.jspecify") (2026-07-15, вынесен из nullaway;
                  изначально был id("java-library") + api(...), пересмотрено в тот же день —
                  java-library не должен просачиваться во все модули через codequality ради
                  зависимости, чья видимость на потребителях от api/implementation не зависит)
nullaway        — id("java") (было id("java-library") до 2026-07-15 — downgrade сразу же после
                  выноса jspecify, т. к. api(jspecify) была единственной причиной java-library
                  здесь) + id("net.ltgt.errorprone")
jacoco          — id("jacoco"); id("java") не нужен
jacoco-report-aggregation — без родителя вообще (autoconfig)
codequality     — агрегатор 5 плагинов выше

com.example.base (root)  — java + toolchain + junit-jupiter + codequality (1 родитель: codequality)
├── library                — java-library (Gradle core), без Spring
├── reactor                — reactor-core + reactor-tools + reactor-test; родитель base
└── spring-boot             — io.spring.dependency-management + Spring Boot BOM
    ├── spring-boot-*        (22 технологических плагина, без spring-boot-application ниже —
    │                         включает 4 вендорных БД-плагина 2026-07-23: postgresql-database,
    │                         mysql-database, r2dbc-postgresql-database, r2dbc-mysql-database,
    │                         + testcontainers (нейтральная обвязка) + testcontainers-mongodb
    │                         (только MongoDB-артефакт, композиция через 2 id(...) в leaf —
    │                         не единый комбинированный плагин); docker-compose — родитель
    │                         spring-boot (bootable — прямой id("org.springframework.boot"),
    │                         как у spring-boot-application, не через него как родителя —
    │                         исправлено 2026-07-23, см. текст ниже)
    ├── spring-boot-application — + id("org.springframework.boot") + actuator (bootable-ось)
    │   └── spring-cloud-application — 2 прямых родителя (диамант, восстановлен осознанно
    │       │                          2026-07-15, см. текст ниже): spring-boot-application (слева)
    │       │                          и spring-cloud (справа, стрелка от spring-cloud ниже)
    │       └── 4 standalone: config-server/eureka-server/gateway-webflux/gateway-webmvc
    └── spring-cloud         — + BOM spring-cloud-dependencies
        ├── spring-cloud-application — второй родитель (см. стрелку выше)
        └── spring-cloud-*    (5 остальных: openfeign/loadbalancer/eureka-client/
                                config-client/circuit-breaker)
```

**Диамант устранён, затем в тот же день осознанно восстановлен пользователем — 2026-07-15.** Исходно: 4 standalone `spring-cloud-*`-плагина применяли двух прямых родителей — `spring-cloud` и `spring-boot-application` — оба сходились в общем предке `spring-boot`. Технически безопасно (идемпотентность Gradle `PluginManager`; известное исключение gradle/gradle#13252 про cross-project apply — исключено грепом), но устранено по прямому требованию пользователя (см. правило «Кольцевые/ромбовидные зависимости» — отслеживать и сообщать всегда, даже про безопасные). Первый шаг — новый `com.example.spring-cloud-application` как точная копия `dependencyManagement`-блока `com.example.spring-cloud`, но с единственным родителем `spring-boot-application` вместо `spring-boot` — граф строго линеен, ценой дублирования ~6 строк Spring Cloud BOM-импорта в двух файлах.

Пользователь пересмотрел это решение в тот же день: `com.example.spring-cloud-application` переведён на двух прямых родителей — `id("com.example.spring-cloud")` + `id("com.example.spring-boot-application")` — убирает дублирование BOM-импорта, но воссоздаёт ровно тот же диамант (оба пути сходятся в `spring-boot`), который несколькими часами ранее был устранён по этому же правилу. Диамант заявлен агентом явно, с рекомендацией оставить линейный вариант (A) — пользователь всё равно выбрал композицию (B). `./gradlew clean check` пройден. Итог: диамант присутствует осознанно, дублирование кода устранено; правило «сообщать о каждой находке» не требует автоматического устранения — решение остаётся за пользователем. `com.example.spring-cloud`/`com.example.spring-boot-application` не менялись, остальные 5 `spring-cloud-*`-плагинов и 17 модулей с одиночным `spring-boot-application` не затронуты. `com.example.spring-boot-application` несёт `spring-boot-starter-actuator`(`-test`) — actuator привязан к bootable-оси, не к BOM-цепочке. `com.example.javaformat` удалён 2026-07-13 (см. «Задачи») — конфигурация перенесена в `com.example.checkstyle` без правок по модулям-потребителям.

**Технологическая и вендорная ось адаптеров** (2026-07-23, закрывает «Стратегия активации адаптеров» и «Комбинации technology в `application/`» из «Открытые решения») — задача: скелет под полный выбор адаптера/драйвера БД пользователем, расширяемый под будущие неизвестные технологии без переписывания существующего кода. Решены две независимые оси:
- **Технология адаптера** (jpa/jdbc/r2dbc/mongodb/mongodb-reactive) — отдельный composition-root модуль на каждую (`application-jpa`/`application-jdbc`/`application-mongodb`/`application-r2dbc`/`application-mongodb-reactive`, было 2 — `application`/`application-reactive`, переименованы), а не `@Profile` внутри одного модуля. Причина — не только отсутствие диаманта: на classpath каждого модуля ровно одна repository-технология, поэтому `*RepositoriesAutoConfiguration`-классы Spring Boot (`@ConditionalOnClass`-гейтед) активируются автоматически; `@Profile`-вариант с двумя адаптерами сразу потребовал бы ручного `@EnableJpaRepositories`/`@EnableMongoRepositories(basePackages=...)`, что противоречит принципу «полагаться на autoconfiguration всегда, когда возможно» — вариант отдельных модулей аддитивен: новая технология в будущем — только новые файлы, ни один существующий модуль не редактируется. Это прямо отменяет прежнюю формулировку «MongoDB намеренно не подключена... не третья одновременная связка» — пересмотр, не продолжение (см. «Пересмотр решений» в «Правила»)
- **Вендор БД** (H2/PostgreSQL/MySQL для jpa/jdbc/r2dbc — код `data-jpa`/`data-jdbc`/`data-r2dbc` идентичен для любого вендора) — НЕ отдельные модули: 4 новых convention-плагина (`spring-boot-postgresql-database`, `-mysql-database`, `-r2dbc-postgresql-database`, `-r2dbc-mysql-database`) кладут драйверы `runtimeOnly` (зеркалят `spring-boot-h2-database`/`-r2dbc-h2-database`, без версии — Spring Boot BOM), активный вендор — `spring.profiles.active=postgresql|mysql` + `application-{vendor}.properties` (`spring.datasource.url`/`spring.r2dbc.url`). Без активного профиля — H2 in-memory для JDBC (`DataSourceAutoConfiguration`, embeddable-enum H2/Derby/HSQLDB) **и для R2DBC** (embedded-detection есть и там — Spring Boot docs: «You need not provide any connection URLs... only include a build dependency to the embedded database»; проверено эмпирически 2026-07-23: `application-r2dbc` без `spring.r2dbc.url` и без активного профиля стартует на H2 даже с Postgres/MySQL-драйверами на classpath). Первая попытка (`spring.profiles.default=h2`+`application-h2.properties`) была лишним усложнением на основании ошибочного предположения «у R2DBC нет embedded-detection» — откачено, `application.properties` во всех `application-r2dbc/` не содержит вообще никакого упоминания БД, симметрично `application-jpa`/`application-jdbc`
- **MongoDB не имеет embedded-аналога H2** — построены и вживую верифицированы (не только `clean check`) `spring-boot-testcontainers` (нейтральная обвязка, годится для любого будущего Testcontainers-модуля) + `spring-boot-testcontainers-mongodb` (только `testcontainers-mongodb`-артефакт, композиция двух `id(...)` — исходно был один плагин, разделён в тот же день по замечанию о нарушении композиционного принципа; `MongoDBContainer` из пакета `org.testcontainers.mongodb`, сменился с `org.testcontainers.containers`; один контейнер/`MongoConnectionDetails` общий для sync и reactive клиента, Spring Boot сам выбирает тип по classpath — подтверждено docs.spring.io) и `spring-boot-docker-compose` (родитель `spring-boot` + прямой `id("org.springframework.boot")`, как у `spring-boot-application`, а не через него — тоже исправлено по замечанию о композиции; `bootRun` реально поднимал `mongo:8` без ручного `docker run`, `test` реально поднимал контейнер через Testcontainers). **Пересмотрено в тот же день** — оба плагина применялись в `application-mongodb*`, но пользователь указал на асимметрию: у `jpa`/`jdbc`/`r2dbc` H2 даёт бесплатный embedded zero-config путь, а PostgreSQL/MySQL там намеренно opt-in без какой-либо инфраструктурной обвязки (без Testcontainers/Compose для них); закрывать этот же пробел у MongoDB Testcontainers/Compose, пока проект на стадии подготовки скелета, а не реальной инфраструктуры — непоследовательно. **Итог**: оба convention-плагина (`spring-boot-testcontainers`/`-testcontainers-mongodb`/`spring-boot-docker-compose`) остаются в `build-logic/` про запас, но ни один `application-mongodb*`-модуль их больше не применяет — только `spring-boot-application`, без вендор/инфраструктурной обвязки, симметрично `jpa`/`jdbc`/`r2dbc`. `compose.yaml` и Testcontainers-based тесты удалены, `NoteMongoApplicationTests`-и-аналоги — обычный `contextLoads()` (проверено — проходит без запущенной Mongo, драйвер подключается лениво). Подключить обратно — когда будет настраиваться реальная инфраструктура (см. «Открытые решения»)
- Вендорное подключение (PostgreSQL/MySQL для jpa/jdbc/r2dbc) верифицировано вживую один раз (`application-jpa` + `--spring.profiles.active=postgresql`, Hikari реально подключился), но не покрыто автотестами/CI — как и решено выше, оставлено без инфраструктурной обвязки до отдельной задачи по инфраструктуре

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

- **Подключение Testcontainers/Docker Compose для MongoDB** (2026-07-23) — `spring-boot-testcontainers`/`-testcontainers-mongodb`/`spring-boot-docker-compose` построены, вживую верифицированы, лежат в `build-logic/`, но не применяются ни в одном `application-mongodb*`-модуле (см. «Принятые решения» → «Архитектура»). Подключить обратно, когда будет настраиваться реальная инфраструктура проекта — тогда же логично пересмотреть и вендорную ось (PostgreSQL/MySQL тоже без инфраструктурной обвязки сегодня, только `spring.profiles.active` + расчёт на существующую внешнюю БД) на предмет аналогичного Testcontainers/Compose-покрытия, чтобы решение было симметричным по всем адаптерам сразу, а не точечным
- **Область подключения `spring-boot-starter-validation`** (2026-07-15) — сейчас в `com.example.spring-boot` (общий родитель всех 16 технологических плагинов), попадает во все Boot-модули, включая `data-mongodb`/`data-mongodb-reactive`/`data-r2dbc`/`data-jdbc`/`data-elasticsearch`/`h2-database`/`r2dbc-h2-database`/`client-rest`/`client-web`/`oauth2-*`/`spring-cloud`. Автоматический триггер bean-validation есть только у `data-jpa` (Hibernate валидирует entity на pre-persist/pre-update) и `webmvc`/`webflux` (`@Valid` на входящих данных); `graphql` — отдельно, Spring for GraphQL поддерживает `@Valid` на `@Argument`. Для MongoDB автотриггера нет, но поддержка есть — `ValidatingMongoEventListener` (`org.springframework.data.mongodb.core.mapping.event`), требует явной регистрации bean (`LocalValidatorFactoryBean`), Spring Boot её не автоконфигурирует; альтернатива — `MongoJsonSchemaCreator` (генерация MongoDB-нативного `$jsonSchema` из JSR-303 аннотаций, тоже нужно применить вручную). У R2DBC/JDBC (Spring Data Relational) — ни автотриггера, ни штатного listener-механизма нет вообще. Сама аннотация ограничения (`jakarta.validation.constraints.NotNull` и т. д.) технологически нейтральна — одна и та же что для JPA, что для Mongo, что для R2DBC/JDBC; разница только в том, кто её проверяет. Варианты по месту объявления `starter-validation`: (A) оставить как есть — одна точка объявления, работает сразу для любой технологии, включая будущий `ValidatingMongoEventListener` для Mongo, цена — `hibernate-validator`+`jakarta.el`+`classmate` в модулях без реального сценария на сегодня (`h2-database`/`client-rest`/`oauth2-*` и т. п., где validation в принципе не нужна); (B) сузить до `spring-boot-webmvc`/`spring-boot-webflux`/`spring-boot-data-jpa`(+`graphql`) — classpath точно соответствует сегодняшнему использованию, цена — дублирование объявления и необходимость руками добавлять зависимость в `data-mongodb`/`data-mongodb-reactive`, когда/если появится `ValidatingMongoEventListener`. Сами `@NotNull`/`@NotBlank` на 15 model-классах по-прежнему не расставлены (см. выше). Не реализовывать до отдельного запроса
- **Порядок методов реализации = порядок объявления в интерфейсе** (п. 8 из 10 личных правил форматирования, остальные 9 реализованы, см. «Стиль кода») — нужна семантическая привязка к реализуемому интерфейсу, синтаксическому Checkstyle не хватает резолвинга типов. Кандидаты: (1) кастомный Error Prone `BugChecker` (в проекте уже есть `net.ltgt.errorprone`); (2) JUnit-тест на `com.github.javaparser:javaparser-core` сравнивающий порядок методов интерфейс/реализация. Не реализовывать до отдельного запроса
- **Регистрация auth/ ↔ user/** — Lazy / Sync / Events (Kafka)
- **Каталог `data/` на уровне сервиса** — группировать `data-jpa`/`data-jdbc`/`data-r2dbc`/`data-mongodb`/`data-mongodb-reactive` в подкаталог `data/` (только каталог, не Gradle-модуль). Рекомендация — оставить как есть: нет функциональной пользы, а цена реорганизации реальна (`settings.gradle.kts`, typesafe-accessors меняют форму, сбивает пути в идущем пересмотре каталога). Пересмотреть при появлении конкретного драйвера
- **Возврат мутирующего use case** — DTO vs `void`
- **`@Transactional` на методах адаптера** — решение было зафиксировано, но не реализовано ни в одном адаптере ни одной технологии (обнаружено при пересмотре 2026-07-07). Решить: реализовать по всем адаптерам (~150 файлов) или снять как устаревшее (Spring Data репозитории и так транзакционны на уровне метода)
- **`user/`: `findByEmail`/`findByUsername` без HTTP-входа** — доведены до всех driven-адаптеров, не выведены в `webmvc`/`webflux`. Варианты: задел под будущий `auth/`, добавить контроллеры сейчас, или убрать как неиспользуемое
- **Javadoc в `package-info.java`** — ни один пакет не содержит package-level Javadoc, только `@NullMarked`; `checkstyle.xml` сейчас исключает javadoc-проверки — включение потребует такого комментария в каждом пакете
- **Composite build на границе сервисов** — каждый сервис мог бы подключаться через `includeBuild(...)` вместо `include(...)`. Ни один сервис не ссылается на модули другого, так что accessors не пострадали бы, но цена (7 новых `settings.gradle.kts`, ручная агрегация `build`/`check`/`clean`) больше выигрыша без текущего драйвера (независимое CI/версионирование, разъезд по репозиториям)
- **Инструмент миграции БД по адаптерам** (2026-07-15) — справочный обзор кандидатов (Flyway/Liquibase для data-jpa·data-jdbc, обход через blocking JDBC или Flamingock SQL target system для data-r2dbc, Mongock/Flamingock/liquibase-mongodb/Flyway Native Connectors для data-mongodb·data-mongodb-reactive, elasticsearch-evolution для data-elasticsearch) вынесен в [db-migration-tools-reference.md](db-migration-tools-reference.md). Ключевая находка: **Mongock в maintenance mode, официальный EOL — конец 2026 года**, преемник — Flamingock (те же авторы, v1.4.4). Выбор конкретного инструмента не сделан — не реализовывать до отдельного запроса

---

## Каталог файлов проекта

Вынесен в [file-catalog.md](file-catalog.md) (2026-07-14, снятие объёма с CLAUDE.md) — полный git-отслеживаемый список файлов со статусами ([DONE]/[REVIEW]/[ADD]/[REMOVED]). Правила ведения (что можно/нельзя менять по инициативе агента) — см. «Правила» ниже, не переносились вместе с содержимым.

## ⛔ Референс: полный набор Spring Boot стартеров (объединение всех присланных, максимальный) — ЗАПРЕЩЕНО УДАЛЯТЬ ИЛИ СОКРАЩАТЬ

Вынесен в [spring-boot-starters-reference.md](spring-boot-starters-reference.md) (2026-07-14, снятие объёма с CLAUDE.md) — дословная копия со Spring Initializr, источник истины при проверке полноты convention-плагинов на пропущенные `-test`-компаньоны/plain-API-варианты. Сам список не сокращать и не удалять из него строки (см. «Правила»); допустима только проверка на дубликаты.

Инструменты миграции/версионирования схемы БД по каждому возможному DB-адаптеру (Flyway/Liquibase/Mongock/Flamingock/liquibase-mongodb/elasticsearch-evolution и др.) — отдельный справочник [db-migration-tools-reference.md](db-migration-tools-reference.md) (2026-07-15), см. пойнтер в «Открытые решения».
