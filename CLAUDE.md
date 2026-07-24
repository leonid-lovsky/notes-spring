# CLAUDE.md — notes-spring

> Последнее обновление: Fri Jul 24 17:01:16 IDT 2026 **Всё временно** — любое решение подлежит обсуждению и изменению.

Многомодульный Spring Boot 4 проект (`note/`, `user/`, `user-note/`, ...), реализующий hexagonal architecture единообразно во всех сервисах через Gradle convention plugins. Этот файл — точка входа: правила, текущая задача, активно пересматриваемые открытые решения. Вся необходимая для работы над проектом информация — здесь или в справочных файлах ниже, без обращения к внешним источникам. Формат оптимизирован для чтения/правки ИИ-агентом — см. «Правила» → «Доступность CLAUDE.md для ИИ-агента».

**Справочные файлы репозитория** (корень репо, читать по мере необходимости — не каждую сессию, в отличие от этого файла):
- [file-catalog.md](file-catalog.md) — полный каталог файлов проекта со статусами `[DONE]`/`[REVIEW]`/`[ADD]`/`[REMOVED]` — текущая активная задача построчного пересмотра, см. «Задачи»
- [decisions-log.md](decisions-log.md) — хронология принятых архитектурных решений (Архитектура/HTTP/Маппинг/Reactive/Convention plugins/Версии/Стиль кода)
- [tech-glossary.md](tech-glossary.md) — глоссарий внешних технологий, в основном ещё не используются (Kubernetes/Terraform/Kafka/OAuth2/Axon и т. д.)
- [spring-boot-starters-reference.md](spring-boot-starters-reference.md) — полный список Spring Boot стартеров, эталон полноты для convention-плагинов (⛔ не сокращать)
- [db-migration-tools-reference.md](db-migration-tools-reference.md) — инструменты миграции БД по каждому driven-адаптеру

**Оглавление CLAUDE.md:** [Начало сессии](#начало-каждой-сессии) · [Правила](#правила) · [Задачи](#-задачи) · [Архитектура и структура](#архитектура-и-структура-проекта) · [Именование](#именование) · [Стек](#стек) · [Технологии](#технологии) · [Принятые решения](#принятые-решения) · [Открытые решения](#открытые-решения)

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
- **Пересмотр решений** — статус «принято»/«зафиксировано» (в т. ч. в `decisions-log.md`) не закрывает вопрос навсегда: любое решение по CRUD-сервисам и по проекту в целом нужно поднимать заново для обсуждения и пересмотра в подходящий момент, а не только по отдельному запросу
- **Кольцевые/ромбовидные зависимости — отслеживать всегда, без исключения для «кажется безопасным»** (введено 2026-07-14, ужесточено 2026-07-15): при любой правке convention-плагинов, Gradle project-зависимостей (`projects.*`) или Java-иерархий портов/адаптеров — проверять граф на циклы и на схождение путей (диамант). Сообщать о **каждой** находке пользователю — что найдено, уровень опасности, вариант(ы) устранения — даже если диамант доказанно безопасен и не создаёт видимых проблем (техническая безопасность — не повод оставить как есть по умолчанию или промолчать; решение оставить/устранить — за пользователем, не за агентом). Прецедент: диамант `spring-cloud-config-server`/`eureka-server`/`gateway-webflux`/`gateway-webmvc` → `spring-cloud`+`spring-boot-application` → `spring-boot` был доказан безопасным (идемпотентность Gradle `PluginManager`, баг gradle/gradle#13252 про cross-project apply — исключён грепом), но пользователь всё равно потребовал устранить — решено новым линейным плагином `com.example.spring-cloud-application` (см. `decisions-log.md` → Convention plugins). Никаких shared-функций/helper-кода в `build-logic/` для устранения подобных схождений — только композиция через `id(...)` (явный отказ пользователя от варианта с общей Kotlin-функцией). **Два разных вида схождения — не путать**: вид 1 — диамант внутри самого convention-плагина (файл в `build-logic/` сам в своём `plugins{}` вызывает 2+ `id("com.example.*")`, сходящихся в общем предке) — устранять по этому правилу, прецедент выше. Вид 2 — листовой модуль (`build.gradle.kts` сервиса) напрямую применяет 2+ convention-плагина, сходящихся в общем предке (например `note/application-jpa` — `spring-boot-application`+`spring-boot-database-h2`; `gateway/application` — сразу 3 `spring-cloud-*`) — **не диамант для устранения**, а осознанная композиция независимых convention-плагинов в точке использования, ради которой вся система convention-плагинов и построена (решено пользователем 2026-07-15, не поднимать заново без нового явного повода)
- **Только convention-плагины — листовые модули не применяют ничего напрямую** (проверено и закреплено 2026-07-15): ни один из 39 листовых `build.gradle.kts` (`note`/`user`/`user-note`/`auth`/`registry`/`config`/`gateway`) не должен применять plugin id, не начинающийся с `com.example.*`, и не должен объявлять зависимость иначе как `implementation(projects....)`/`api(projects....)` (project-to-project). Все внешние зависимости, плагины, версии и настройка сборки — только через `build-logic/convention/`. Проверка: `grep -rn 'id("' --include="build.gradle.kts" note user user-note auth registry config gateway | grep -v 'id("com\.example\.'` и аналогично по `dependencies{}`-блокам — оба должны быть пустыми. Прогонять эту проверку при добавлении новой зависимости/плагина в любой листовой модуль
- **Плагины-расширители (`java-library` и подобные) — только там, где реально нужна расширенная конфигурация, не каскадом «на всякий случай»** (введено 2026-07-15): прецедент — `com.example.jspecify` подключал `id("java-library")` только ради `api("org.jspecify")`, и через каскад `base → codequality → jspecify` эта конфигурация просачивалась во все 39 модулей, хотя видимость зависимости на потребителях от `api`/`implementation` не зависела (jspecify достаётся каждому модулю напрямую через собственный каскад `id(...)`, а не через project-to-project зависимость, где `api` реально что-то протаскивает дальше). Исправлено на `id("java")` + `implementation(...)`; `java-library` остался только в `com.example.library`, применяемом 6 модулями `contract`/`contract-reactive`, где `api(projects.*.domain)` — настоящая публичная сигнатура. При добавлении/правке convention-плагина, если он подключает id, расширяющий базовый (`java-library` поверх `java` и аналоги) — проверять: (1) нужна ли расширенная конфигурация именно этому плагину для собственной декларации зависимостей, или сводится к базовой; (2) не полагаться на то, что зависимость «протекает» api-путём к потребителям, если фактическая топология — каскадное применение `id(...)` внутри одного проекта, а не project-to-project. Проверка: `grep -rln 'id("java-library")'` по `build-logic/convention/` — список плагинов должен совпадать со списком, где реально объявлен `api(...)`
- **Build** — после каждого логического шага: `./gradlew clean check` (быстрее `build`: без `assemble`/`bootJar`) → обновить CLAUDE.md → коммит → пуш; перед коммитом дополнительно `./gradlew clean build` (проверяет и паковку — `bootJar`/`resolveMainClassName`, что `check` не покрывает)
- **Подзадачи** — крупную задачу разбивать на подзадачи; коммитить и пушить после каждой завершённой подзадачи, чтобы не терять прогресс при обрыве сессии
- **⚠️ Лимит размера файла — при превышении сокращать самостоятельно, без напоминания** (2026-07-14, актуализировано 2026-07-14 по прямому запросу пользователя — три ограничения ниже закреплены здесь именно затем, чтобы не повторять их в каждом запросе «сократи файл»): триггер — сообщение `⚠ CLAUDE.md is over the 150.0k-char limit`. Мерить `wc -m CLAUDE.md`, **не** `wc -c` — тот считает байты UTF-8, а из-за кириллицы (2 байта/символ) байты почти вдвое больше реального числа символов, на котором основан лимит (проверено эмпирически 2026-07-14). Три вещи трогать нельзя, только переформулировать:
  1. Раздел «Правила» — ни одна инструкция не теряется по смыслу; формулировку каждого пункта можно сжимать (кроме отдельно помеченных исключений, см. правило про Maven Central выше)
  2. `spring-boot-starters-reference.md` (вынесен из CLAUDE.md 2026-07-14, см. «Справочные файлы репозитория» в начале файла) — сам список зависимостей (и сам gradle-код) не сокращать и не удалять из него строки; допустима только проверка на дубликаты. Пояснительный текст вокруг списка и в CLAUDE.md — редактировать можно
  3. `file-catalog.md` (вынесен из CLAUDE.md 2026-07-14) — список файлов и их статусы ([DONE]/[REVIEW]/[ADD]/[REMOVED]) не менять и не удалять по инициативе агента (можно добавлять пропущенные файлы `[REVIEW]`, можно убирать строки реально удалённых файлов, `[DONE]` — только по явному указанию пользователя); комментарий к каждому файлу — можно свободно сокращать вплоть до полного удаления
  Всё остальное в CLAUDE.md (в первую очередь «Задачи»/«Открытые решения» — исторические подробности) сокращать без ограничений, не теряя решения по существу. Та же логика (сокращать свободно, не теряя решения по существу) применяется к `decisions-log.md`/`tech-glossary.md`, если один из них сам разрастётся — независимо мерить `wc -m` по каждому файлу

---

## ⚡ Задачи

**Текущая работа** — построчный пересмотр «Каталог файлов проекта»: ровно один файл за раз — разбор → утверждение пользователем → `[DONE]` → следующая строка `[REVIEW]` сверху вниз. Не пакетами, не забегая вперёд.

**Завершено 2026-07-24, позже в тот же день** (детали — см. `decisions-log.md` → «Архитектура» → «Порядок вендор-технология в имени composition-root»): порядок частей имени 27 реляционных composition-root модулей (3 сервиса × 9 комбинаций jpa/jdbc/r2dbc × h2/mysql/postgresql) перевёрнут с `{tech}-{vendor}` на `{vendor}-{tech}` — `application-jdbc-h2` → `application-h2-jdbc`, класс `NoteJdbcH2Application` → `NoteH2JdbcApplication`, аналогично `spring.application.name`. Директории, main/test-классы, `settings.gradle.kts`, `file-catalog.md` (пути) обновлены; `build.gradle.kts` не менялся. `application-mongodb`/`-mongodb-reactive` не затронуты (нет вендорной оси). `./gradlew clean check` пройден по всем 33 composition-root модулям (`git status` подтвердил чистое обнаружение переименований, без потерянных файлов)

**Завершено 2026-07-24** (детали — см. `decisions-log.md` → «Архитектура» → «Вендор БД — граница Gradle-модуля, не рантайм-профиль»): решена проблема driver bloat — один `bootJar` тащил H2+PostgreSQL+MySQL драйверы одновременно. `application-jpa`/`application-jdbc`/`application-r2dbc` (по одному на сервис) разбиты на 9 вендорных модулей каждый (`application-{tech}-{h2,mysql,postgresql}`) — 15 → 33 composition-root модуля по трём сервисам. `application-mongodb`/`-mongodb-reactive` не изменены (нет вендорной оси). Заодно переименованы 3 r2dbc-вендорных convention-плагина (`spring-boot-database-r2dbc-{vendor}` → `spring-boot-database-{vendor}-r2dbc`) для sort-by-name-группировки с `database-{vendor}`; аудит остальных convention-плагинов и всех модулей/пакетов проекта на тот же принцип — отклонений не найдено. Верифицировано напрямую (`dependencies --configuration runtimeClasspath` на 3 репрезентативных модулях — ровно один вендорный драйвер), не только `clean check`/`clean build` (оба зелёные по всем 33 модулям)

**Завершено 2026-07-23**: сверка версий стека с GitHub releases API — актуально всё, кроме NullAway `0.13.7`→`0.13.8` (вышел 2026-07-19; заметное изменение — добавлена поддержка Reactor, актуально для проекта с активным `reactor-core`). `./gradlew clean check` пройден, новых warnings не появилось

**Завершено 2026-07-23, ещё позже в тот же день** (детали — см. `decisions-log.md` → «Архитектура» → «Именование convention-плагинов для sort-by-name-группировки»): 16 из 48 convention-плагинов переименованы для авто-группировки при сортировке по имени. Слово-маркер перенесено из суффикса в префикс — `{vendor}-database` → `spring-boot-database-{vendor}` (6 плагинов, устраняет врезание `oauth2-*` между `mysql-database` и `postgresql-database`). Приватные фрагменты `codequality` получили общий префикс — `checkstyle`/`jacoco`/`jacoco-report-aggregation`/`jspecify`/`nullaway` → `codequality-*` (5 плагинов, 0 применений в листовых модулях, blast radius только внутри `codequality.gradle.kts`). Неточные имена технологий исправлены на литеральные — `spring-boot-oauth2-*` → `spring-boot-security-oauth2-*` (соответствует реальному имени стартера Boot 4); `spring-boot-client-web`/`-client-rest` → `spring-boot-client-webclient`/`-client-restclient` (пересматривает решение 2026-07-14 избегать «webclient» из-за схожести с «webflux» — префикс `client-` сохранён, разногласия не возникает). 7 плагинов 1:1 с папкой модуля не тронуты. `./gradlew projects` + `clean check` по всем 9 задетым composition-root модулям — зелёные

**Завершено 2026-07-23, позже в тот же день** (детали — см. `decisions-log.md` → «Архитектура» → «Testcontainers/Docker Compose возвращены симметрично по всей вендорной оси»): вместо снятия Testcontainers/Compose с MongoDB (см. следующий пункт) — обратное решение, довести PostgreSQL/MySQL до того же покрытия. `application-mongodb`/`application-mongodb-reactive` — Testcontainers/Compose подключены обратно; `application-jpa`/`application-jdbc`/`application-r2dbc` — по 2 новых Testcontainers-плагина и `*{Mysql,Postgresql}ApplicationTests`-класса на модуль. Найден и исправлен баг Testcontainers (не проекта): `MySQLContainer` требует JDBC-драйвер на classpath для wait-strategy, `PostgreSQLContainer` — нет (log-based) — добавлен `testRuntimeOnly(mysql-connector-j)` в `spring-boot-testcontainers-mysql`. `./gradlew clean check` пройден по всем 15 composition-root модулям во всех трёх сервисах

**Завершено 2026-07-23** (детали — см. `decisions-log.md` → «Архитектура» → «Технологическая и вендорная ось адаптеров»): полная матрица composition-root модулей — 5 на сервис (`application-jpa`/`application-jdbc`/`application-mongodb`/`application-r2dbc`/`application-mongodb-reactive`, было 2) × 3 сервиса = 15; вендорная ось H2/PostgreSQL/MySQL для JPA/JDBC/R2DBC через 4 новых convention-плагина + рантайм-профили, без новых Gradle-модулей. `application/`→`application-jpa/`, `application-reactive/`→`application-r2dbc/` (классы и `spring.application.name` тоже переименованы). Все узлы верифицированы вживую: H2-дефолт (в т. ч. R2DBC — embedded-detection есть и там, не только у JDBC), PostgreSQL-профиль (`HikariPool` реально подключился). Testcontainers/Docker Compose для MongoDB построены и верифицированы, но в тот же день сняты с применения по замечанию о непоследовательности, затем возвращены симметрично (см. предыдущий пункт) — convention-плагины больше не «про запас», применяются. `./gradlew clean check` пройден на каждом шаге

**Завершено 2026-07-15** (детали — см. `decisions-log.md` → «Convention plugins», «Правила»):
- Диамант у 4 standalone `spring-cloud-*`-плагинов (`config-server`/`eureka-server`/`gateway-webflux`/`gateway-webmvc`) сначала устранён новым линейным плагином `com.example.spring-cloud-application` (единственный родитель `spring-boot-application`, `dependencyManagement`-блок продублирован из `spring-cloud`), затем в тот же день пользователь пересмотрел это решение: `spring-cloud-application` переведён на 2 прямых родителя — `spring-cloud`+`spring-boot-application` — убирает дублирование кода ценой воссоздания того же диаманта. Диамант был доказанно безопасен (идемпотентность Gradle `PluginManager`, баг gradle/gradle#13252 исключён грепом); агент явно указал на воссоздание диаманта и рекомендовал линейный вариант — пользователь всё равно выбрал композицию. Правило «Кольцевые/ромбовидные зависимости» в «Правила» ужесточено: отслеживать и сообщать всегда, решение — за пользователем. Без shared-функций (явный отказ пользователя) — только композиция `id(...)`
- Разграничены два вида схождения путей: вид 1 (диамант внутри самого convention-плагина) — устранять; вид 2 (листовой модуль напрямую применяет 2+ convention-плагина, например `note/application-jpa` — `spring-boot-application`+`spring-boot-database-h2`) — решено пользователем: НЕ диамант, осознанная композиция, ради которой и построена система convention-плагинов, не трогать
- `jspecify` вынесен из `com.example.nullaway` в отдельный `com.example.jspecify`, применяется через `com.example.codequality` (стало 5 плагинов). `com.example.nullaway` переведён на `id("java")` вместо `id("java-library")` — `api(jspecify)` была единственной причиной `java-library` там, после выноса стала не нужна. **Пересмотрено в тот же день**: `com.example.jspecify` изначально был `id("java-library")` + `api(...)`, из-за чего `java-library` каскадом через `base → codequality → jspecify` протекал во все 39 модулей — не обязанность jspecify решать это за всех потребителей. Исправлено на `id("java")` + `implementation(...)` (видимость на потребителях не зависит от `api`/`implementation` здесь — jspecify достаётся каждому модулю напрямую через собственный каскад `id(...)`, а не через project-to-project зависимость). `java-library` теперь только там, где реально нужен `api()` — явно в `com.example.library`, применяемом 6 модулями `contract`/`contract-reactive` (`api(projects.*.domain)`); проверено грепом по всем convention-плагинам и 39 build.gradle.kts
- Проверено и закреплено правилом: все 39 листовых модулей применяют только `id("com.example.*")` и только `project()`-зависимости — ни одного самостоятельного plugin id/внешней зависимости в обход convention-плагинов не найдено
- Сверка версий всего стека (`gradle/libs.versions.toml`, Java, Gradle) с официальными источниками (GitHub releases API — надёжнее агрегированного веб-поиска, который на Checkstyle дал ложный результат «уже латест» при первой проверке): актуальны все, кроме Checkstyle — `13.7.0` → `13.8.0` (вышел 2026-07-12, добавляет поддержку JEP 512 compact source files и OpenJDK style guide checks). `./gradlew clean check` пройден

**Завершено 2026-07-14** (детали решений — см. `decisions-log.md`/«Именование», здесь только пойнтеры, чтобы не дублировать):
- Аудит кольцевых/ромбовидных зависимостей — по convention-плагинам, Gradle project-зависимостям и Java-иерархии портов: циклов не найдено; единственное на тот момент схождение путей (bootable-ось у 4 standalone `spring-cloud-*`) позже устранено 2026-07-15, см. выше
- `spring-boot-starter-validation`(`-test`) → `com.example.spring-boot`; `spring-boot-starter-actuator`(`-test`) → `com.example.spring-boot-application` (bootable-ось, не BOM-цепочка) — `com.example.spring-boot-actuator` удалён
- `jakarta.validation-api:3.1.1` → `com.example.base` (`implementation`) — делает `jakarta.validation.constraints.*` видимыми в `domain/`; сами аннотации `@NotNull`/`@NotBlank` на полях по-прежнему не расставлены (см. «Открытые решения»)
- `{Entity}Persistable` — общий маркер-интерфейс в `domain/`, реализован всеми 15 model-классами; `user-note.role` унифицирован до `UserNoteRole` enum везде; `schema.sql` создан для `data-jdbc`/`data-r2dbc` во всех трёх сервисах — закрывает «Управление схемой для R2DBC/JDBC»
- `data-jdbc/` переведён с сырого `NamedParameterJdbcTemplate` на Spring Data JDBC (`model/`+`repository/`+`mapper/`, `ListCrudRepository`) по всем трём сервисам — прежнее «без repository» решение признано ошибочным
- Референсы Spring Initializr объединены в единый блок в конце файла

**Завершено 2026-07-13**: убран `io.spring.javaformat` (не даёт переопределить `lineSplit`/`join_wrapped_lines`) — единственный гейт стиля теперь Checkstyle (`SpringChecks` + 5 модулей под 10 личных правил, см. `decisions-log.md` → «Стиль кода»), `spring-javaformat` запинен на `0.0.48-SNAPSHOT` (нужные чеки есть только в snapshot, риск мутации принят осознанно). Версии подняты до последних (Java 25, Gradle 9.6.1, Spring Boot 4.1.0, JUnit 6.1.2, Checkstyle 13.8.0, reactor-core 3.8.6, jacoco 0.8.15 — см. `decisions-log.md` → «Синхронизация версий»). Переименования: трёхуровневая иерархия портов (`{Entity}Interface`/`ServiceInterface`/`ControllerInterface`), `data-contract`→`contract`, добавлен `application-reactive/` (см. «Именование»). Добавлены `spring-boot-starter-r2dbc`/`-jdbc` (plain API) рядом с `data-r2dbc`/`data-jdbc` по референсу Initializr.

**Пересмотр CRUD-сервисов** (2026-07-07) — статус ГОТОВО подтверждён по note/user/user-note; оставшиеся расхождения см. «Открытые решения».

Статус по сервисам и модулям:

```
ГОТОВО   note/ · user/ · user-note/ — domain · contract · contract-reactive · webmvc · webflux
                  data-jpa · data-mongodb · data-jdbc · data-r2dbc · data-mongodb-reactive
                  application-{h2,mysql,postgresql}-{jpa,jdbc,r2dbc} · application-mongodb ·
                  application-mongodb-reactive (11 composition-root на сервис — вендор БД
                  для jpa/jdbc/r2dbc теперь граница модуля, не рантайм-профиль, 2026-07-24)
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
application-{h2,mysql,postgresql}-jpa/     composition root (sync)      → webmvc + data-jpa
application-{h2,mysql,postgresql}-jdbc/    composition root (sync)      → webmvc + data-jdbc
application-{h2,mysql,postgresql}-r2dbc/   composition root (reactive)  → webflux + data-r2dbc
application-mongodb/                       composition root (sync)      → webmvc + data-mongodb
application-mongodb-reactive/              composition root (reactive)  → webflux + data-mongodb-reactive
```

Ровно один driven-адаптер и ровно один вендорный БД-драйвер на classpath каждого composition root — обе оси (технология адаптера, вендор БД для jpa/jdbc/r2dbc) выражены границей Gradle-модуля, не рантайм-профилем (пересмотр 2026-07-24 — было `spring.profiles.active`, см. `decisions-log.md` → «Архитектура»: профиль выбирает bean в рантайме, но не убирает лишние драйверы из паковки). 11 composition-root модулей на сервис (9 реляционных + 2 Mongo, у Mongo вендорной оси нет) × 3 сервиса = 33.

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
- **Общий маркер-интерфейс модели сущности** (новое, 2026-07-14) — `{Entity}Persistable`, в `domain/` — `NotePersistable`, `UserPersistable`, `UserNotePersistable`; реализуют все 5 технологических model-классов сущности сразу (единственный общий предок sync+reactive). Осознанно выбрано имя, пересекающееся с реальным `org.springframework.data.domain.Persistable<ID>` — не конфликтует (префикс сущности делает simple name другим), но напоминает: сам маркер в `domain/` остаётся чистым Java без зависимости на `spring-data-commons`, а НАСТОЯЩИЙ `Persistable<ID>` (если понадобится `isNew()`) реализуется отдельно в каждом адаптере — см. `decisions-log.md` → «Архитектура»
- **Composition-root главный класс** (2026-07-23, расширено вендорным префиксом 2026-07-24, порядок частей имени/модуля перевёрнут на «вендор-технология» в тот же день) — `{Service}{Tech}Application` в `application-{tech}/` для Mongo (`NoteMongoApplication`, `NoteMongoReactiveApplication`); `{Service}{Vendor}{Tech}Application` в `application-{vendor}-{tech}/` для jpa/jdbc/r2dbc (`NoteH2JpaApplication`, `NoteMysqlJpaApplication`, `NotePostgresqlJpaApplication`, ...) — вендор всегда явный префикс перед технологией, без неявного «без префикса = H2» (см. `decisions-log.md` → «Вендор БД — граница Gradle-модуля»). Тестовый класс — `...ApplicationTests` по тому же паттерну. До 2026-07-23 было 2 модуля на сервис (`application`/`application-reactive`) без Tech-суффикса

**Tech**: `Jpa` · `Mongo` · `Jdbc` · `R2dbc` · `MongoReactive` (в имени класса — только для `{Tech}MapperContract`/`{Tech}Mapper`/`{Tech}Repository`/`{Tech}Entity`; адаптеры и порт-интерфейсы суффикс технологии в имени не несут — технология различается пакетом/модулем)

**Vendor** (2026-07-24, только для composition-root класса/модуля jpa/jdbc/r2dbc, см. выше): `H2` · `Mysql` · `Postgresql` — всегда префикс перед `{Tech}`, порядок `{Vendor}{Tech}` (`H2Jpa`, не `JpaH2`) — пересмотрено в тот же день: изначально был выбран порядок `{Tech}{Vendor}` («технология доступа архитектурно более фундаментальна»), затем перевёрнут по решению пользователя без изменения обоснования самой оси (вендор/технология остаются раздельными осями именования, поменялся только порядок отображения)

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

Глоссарий технологий за пределами Spring-стека (в основном ещё не используются) вынесен в [tech-glossary.md](tech-glossary.md) — не нужен на каждую сессию, читать при работе с конкретной технологией из списка (Kubernetes/Terraform/Jenkins/SonarQube/AWS/Redis/Kafka/Elastic/OAuth2/jMolecules/Axon).

---

## Принятые решения

Полная хронология архитектурных решений (Architecture/HTTP/Ошибки/Маппинг/Reactive-семантика/Convention plugins/Синхронизация версий/Стиль кода) вынесена в [decisions-log.md](decisions-log.md) — исторический лог, нужен редко (обычно только при пересмотре конкретного решения, grep по дате/теме). Статус «принято» не закрывает вопрос навсегда — см. «Правила» → «Пересмотр решений».

---

## Открытые решения

- **Симметрия ArchUnit + стратегия покрытия матрицы адаптер×драйвер тестами** (2026-07-23, поднято пользователем в приоритет параллельно с построчным пересмотром `file-catalog.md`; **не реализовывать до отдельного запроса**) — два связанных вопроса без решения:
  1. ArchUnit сейчас нигде не подключён, упомянут только в `tech-glossary.md` как компаньон jMolecules. Кандидат на автоматизацию правил, которые сегодня проверяются вручную грепом (см. «Правила» → «Только convention-плагины — листовые модули не применяют ничего напрямую», «Кольцевые/ромбовидные зависимости») — расслоение hexagonal (`domain` не зависит от инфраструктуры, `contract`(-`reactive`)→`domain`, адаптеры→`contract`), запрет на plugin id/зависимости в обход `com.example.*` в 39 листовых модулях. Технический риск, требующий исследования до решения: ArchUnit сканирует скомпилированные классы через reflection — в multi-module Gradle-проекте это означает либо один агрегирующий тестовый модуль с зависимостью на все 39 модулей (сам по себе широкое схождение путей, качественно отличное от диамантов, которые до сих пор разбирались в проекте), либо отдельный Gradle-таск, компонующий classpath всех модулей вручную
  2. Матрица тестового покрытия — **частично закрыта 2026-07-24**: вариант (A) «отдельный класс на комбинацию» реализован ещё сильнее, чем обсуждалось — не просто явный тестовый класс на вендор внутри общего модуля, а отдельный composition-root модуль на комбинацию (`application-{h2,mysql,postgresql}-{jpa,jdbc,r2dbc}` + `application-mongodb`/`-mongodb-reactive` = 11 модулей/сервис × 3 = 33, см. `decisions-log.md` → «Вендор БД — граница Gradle-модуля»), с ровно одним `*ApplicationTests`-классом на модуль. Вариант (B) (JUnit 5 `@ParameterizedTest`) снят с рассмотрения — противоречил бы уже принятому решению «вендор — граница модуля»; вариант (C) — мета-тест по типу ArchUnit, проверяющий «на каждый вендорный convention-плагин должен существовать application-модуль с соответствующим суффиксом» — остаётся актуальным, зависит от решения по пункту 1
- **Область подключения `spring-boot-starter-validation`** (2026-07-15) — сейчас в `com.example.spring-boot` (общий родитель всех 16 технологических плагинов), попадает во все Boot-модули, включая `data-mongodb`/`data-mongodb-reactive`/`data-r2dbc`/`data-jdbc`/`data-elasticsearch`/`database-h2`/`database-r2dbc-h2`/`client-restclient`/`client-webclient`/`security-oauth2-*`/`spring-cloud`. Автоматический триггер bean-validation есть только у `data-jpa` (Hibernate валидирует entity на pre-persist/pre-update) и `webmvc`/`webflux` (`@Valid` на входящих данных); `graphql` — отдельно, Spring for GraphQL поддерживает `@Valid` на `@Argument`. Для MongoDB автотриггера нет, но поддержка есть — `ValidatingMongoEventListener` (`org.springframework.data.mongodb.core.mapping.event`), требует явной регистрации bean (`LocalValidatorFactoryBean`), Spring Boot её не автоконфигурирует; альтернатива — `MongoJsonSchemaCreator` (генерация MongoDB-нативного `$jsonSchema` из JSR-303 аннотаций, тоже нужно применить вручную). У R2DBC/JDBC (Spring Data Relational) — ни автотриггера, ни штатного listener-механизма нет вообще. Сама аннотация ограничения (`jakarta.validation.constraints.NotNull` и т. д.) технологически нейтральна — одна и та же что для JPA, что для Mongo, что для R2DBC/JDBC; разница только в том, кто её проверяет. Варианты по месту объявления `starter-validation`: (A) оставить как есть — одна точка объявления, работает сразу для любой технологии, включая будущий `ValidatingMongoEventListener` для Mongo, цена — `hibernate-validator`+`jakarta.el`+`classmate` в модулях без реального сценария на сегодня (`database-h2`/`client-restclient`/`security-oauth2-*` и т. п., где validation в принципе не нужна); (B) сузить до `spring-boot-webmvc`/`spring-boot-webflux`/`spring-boot-data-jpa`(+`graphql`) — classpath точно соответствует сегодняшнему использованию, цена — дублирование объявления и необходимость руками добавлять зависимость в `data-mongodb`/`data-mongodb-reactive`, когда/если появится `ValidatingMongoEventListener`. Сами `@NotNull`/`@NotBlank` на 15 model-классах по-прежнему не расставлены (см. выше). Не реализовывать до отдельного запроса
- **Порядок методов реализации = порядок объявления в интерфейсе** (п. 8 из 10 личных правил форматирования, остальные 9 реализованы, см. `decisions-log.md` → «Стиль кода») — нужна семантическая привязка к реализуемому интерфейсу, синтаксическому Checkstyle не хватает резолвинга типов. Кандидаты: (1) кастомный Error Prone `BugChecker` (в проекте уже есть `net.ltgt.errorprone`); (2) JUnit-тест на `com.github.javaparser:javaparser-core` сравнивающий порядок методов интерфейс/реализация. Не реализовывать до отдельного запроса
- **Регистрация auth/ ↔ user/** — Lazy / Sync / Events (Kafka)
- **Каталог `data/` на уровне сервиса** — группировать `data-jpa`/`data-jdbc`/`data-r2dbc`/`data-mongodb`/`data-mongodb-reactive` в подкаталог `data/` (только каталог, не Gradle-модуль). Рекомендация — оставить как есть: нет функциональной пользы, а цена реорганизации реальна (`settings.gradle.kts`, typesafe-accessors меняют форму, сбивает пути в идущем пересмотре каталога). Пересмотреть при появлении конкретного драйвера
- **Возврат мутирующего use case** — DTO vs `void`
- **`@Transactional` на методах адаптера** — решение было зафиксировано, но не реализовано ни в одном адаптере ни одной технологии (обнаружено при пересмотре 2026-07-07). Решить: реализовать по всем адаптерам (~150 файлов) или снять как устаревшее (Spring Data репозитории и так транзакционны на уровне метода)
- **`user/`: `findByEmail`/`findByUsername` без HTTP-входа** — доведены до всех driven-адаптеров, не выведены в `webmvc`/`webflux`. Варианты: задел под будущий `auth/`, добавить контроллеры сейчас, или убрать как неиспользуемое
- **Javadoc в `package-info.java`** — ни один пакет не содержит package-level Javadoc, только `@NullMarked`; `checkstyle.xml` сейчас исключает javadoc-проверки — включение потребует такого комментария в каждом пакете
- **Composite build на границе сервисов** — каждый сервис мог бы подключаться через `includeBuild(...)` вместо `include(...)`. Ни один сервис не ссылается на модули другого, так что accessors не пострадали бы, но цена (7 новых `settings.gradle.kts`, ручная агрегация `build`/`check`/`clean`) больше выигрыша без текущего драйвера (независимое CI/версионирование, разъезд по репозиториям)
- **Инструмент миграции БД по адаптерам** (2026-07-15) — справочный обзор кандидатов (Flyway/Liquibase для data-jpa·data-jdbc, обход через blocking JDBC или Flamingock SQL target system для data-r2dbc, Mongock/Flamingock/liquibase-mongodb/Flyway Native Connectors для data-mongodb·data-mongodb-reactive, elasticsearch-evolution для data-elasticsearch) вынесен в [db-migration-tools-reference.md](db-migration-tools-reference.md). Ключевая находка: **Mongock в maintenance mode, официальный EOL — конец 2026 года**, преемник — Flamingock (те же авторы, v1.4.4). Выбор конкретного инструмента не сделан — не реализовывать до отдельного запроса
- **Полная модель Google Docs (совместное редактирование)** (2026-07-23) — набросок требований (доменная модель Document/Revision/Permission/Comment, real-time слой поверх WebSocket/STOMP, OT/CRDT как нерешённое ядро сложности без готового решения в Spring-экосистеме, права доступа поверх Spring Security+OAuth2) вынесен в [google-docs-full-model.md](google-docs-full-model.md). Рекомендация — не проектировать целиком: начать с обычного CRUD-документа по паттерну `note/`, real-time collaborative editing выделить в отдельную задачу. Не реализовывать до отдельного запроса

---

Правила ведения `file-catalog.md`/`spring-boot-starters-reference.md`/`decisions-log.md`/`tech-glossary.md` (что можно/нельзя менять по инициативе агента) — см. «Правила» → «Лимит размера файла» выше. Сами файлы и их назначение — см. «Справочные файлы репозитория» в начале этого файла.
