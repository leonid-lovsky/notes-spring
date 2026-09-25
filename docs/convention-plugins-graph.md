# Снимок графа convention-плагинов

> Новый 2026-08-01, снимок ТЕКУЩЕГО состояния (не история решений — история и обоснования «почему» остаются в `docs/decisions-log.md` → «Convention plugins — принцип именования и структура», сам этот лог не пополнялся с 2026-08-16, см. его собственную пометку). Причина создания — граф разбросан по нескольким хронологическим правкам, реконструировать текущее состояние оттуда занимает больше времени, чем прочитать один плоский список. Обновлять при каждом добавлении/удалении/переименовании convention-плагина или изменении его `plugins{}`-блока — в той же правке, не откладывая.
>
> **Полностью пересобран 2026-09-24** — предыдущая версия не обновлялась с 2026-08-01 и описывала `note`/`user`/`user-note`×3, `spring-boot-application`, `data-jpa`, `application-h2-jpa` и другие модули, удалённые/переструктурированные при переписывании. Область применения сузилась: сегодня единственный сервис с исходным кодом — `user-note/` (16 листьев по модели application-vendor, см. `CLAUDE.md` → «Архитектура и структура проекта» → «Корневое дерево вариантов»), `note`/`user`/`auth`/`registry`/`config`/`gateway` удалены из репозитория целиком — отсюда 0 применений у всех `spring-cloud-*`/batch/security-oauth2/graphql/elasticsearch/client-*-плагинов ниже (были рассчитаны на сервисы, которых больше нет, не единое отставание).

Проверочная команда (перегенерировать граф родитель→потомок по факту из файлов, свериться со списком ниже): `for f in build-logic/convention/src/main/kotlin/com.example.*.gradle.kts; do id=$(basename "$f" .gradle.kts); echo "$id :: $(grep -oE 'id\("[^"]+"\)' "$f" | tr '\n' ' ')"; done`

Проверочная команда (число применений плагина, только `user-note` — единственный сервис с кодом): `grep -rl 'id("com.example.{id}")' --include="build.gradle.kts" user-note | wc -l`

Схема каждой строки: **id плагина** — родитель(и) — что добавляет — число применений в `user-note/` сегодня (2026-09-24).

---

## Уровень 0 — корень

- `com.example.base` — родителя нет (сам применяет `id("java")` + `id("com.example.codequality")`) — toolchain из `.java-version`, `jakarta.validation-api`(implementation), `junit-jupiter`(test) + JUnit Platform Launcher(testRuntimeOnly) — 0 прямых применений (переименован из `com.example.java` 2026-09-24 по прямому запросу пользователя — откат решения 2026-08-01 «id = имя технологии»; ни один лист `user-note` не применяет его напрямую — только каскадом через `contract`/`project-reactor`/`spring-boot`, см. уровень 1)

## Уровень 1 — родитель `base`

- `com.example.codequality` — `base` — агрегатор 8 `codequality-*` (см. ниже) — 0 прямых применений (воссоздан 2026-09-24 по прямому запросу — тот же паттерн, что был убран 2026-08-01 как «обёртка без переиспользования, один потребитель»; сегодня потребитель по-прежнему один — `base` — но фрагментов 8, не 5)
- `com.example.contract` — `base` — не добавляет зависимостей, чистый алиас — 1 применение (`contract`) — введён 2026-09-24: `user-note/contract` не собирался (`plugins{}` был пуст, без единого `id(...)`), это единственный способ дать листу свою роль вместо голого `base` напрямую (см. `CLAUDE.md` → «Правила» → исключения из правила именования плагинов)
- `com.example.project-reactor` — `base` — + `reactor-core`(implementation) + `reactor-tools`(implementation) + `reactor-test`(test) — 0 прямых применений (родитель `contract-reactive`)
- `com.example.spring-boot` — `base` — + `io.spring.dependency-management` + Spring Boot BOM + `spring-boot-starter`(+test) — 0 прямых применений (родитель 24 технологических плагинов уровня 2 + `spring-cloud`)

## Уровень 2 — родитель `project-reactor`

- `com.example.contract-reactive` — `project-reactor` — не добавляет зависимостей, чистый алиас — 1 применение (`contract-reactive`) — симметрично `contract` выше, введён в той же сессии

## Уровень 2 — родитель `codequality` (8 фрагментов; каждый физически применяет бare Gradle/внешний id в своём файле — не `com.example.*` — см. «Как читать этот граф»)

- `com.example.codequality-checkstyle` — `id("checkstyle")` — `configFile` на `gradle/checkstyle/checkstyle.xml` (2026-09-25 переименован из `google_checks.xml`), `maxWarnings = 0` (2026-09-25), дословная копия Google Checks + 2 переопределения (`LineLength.max` 120, `Indentation.basicOffset` 4) — 0 прямых
- `com.example.codequality-jacoco` — `id("jacoco")` — `jacocoTestReport` привязан к `test` — 0 прямых
- `com.example.codequality-jacoco-report-aggregation` — `id("jacoco-report-aggregation")` — без своей конфигурации (autoconfig) — 0 прямых
- `com.example.codequality-jspecify` — `id("java")` — `implementation(jspecify)` — 0 прямых
- `com.example.codequality-nullaway` — `id("java")` + `id("net.ltgt.errorprone")` — NullAway как error — 0 прямых
- `com.example.codequality-pmd` — `id("pmd")` — только `toolVersion` из каталога, без кастомного ruleset — 0 прямых
- `com.example.codequality-spotbugs` — `id("com.github.spotbugs")` — дефолтная конфигурация — 0 прямых
- `com.example.codequality-spotless` — `id("com.diffplug.spotless")` — `importOrder()`+`removeUnusedImports()`+`googleJavaFormat().aosp()`+`leadingTabsToSpaces()`; `compileJava.dependsOn(spotlessApply)` — автофикс при любой сборке — 0 прямых

## Уровень 2 — родитель `spring-boot` (технологические плагины, каждый = 1 Spring Boot стартер/концерн; `spring-boot-application`/`spring-cloud-application` удалены 2026-09-04/06 — bootable-ось несёт отдельный атомарный `spring-boot-bootable`, композиция на листе)

- `com.example.spring-boot-actuator` — `spring-boot` — + `spring-boot-starter-actuator`(+test) — 8 применений (все 8 листьев `application-*`)
- `com.example.spring-boot-batch` — `spring-boot` — + `spring-boot-starter-batch`(+test) — 0 применений (ОТЛОЖЕНО)
- `com.example.spring-boot-batch-data-mongodb` — `spring-boot` — + `spring-boot-starter-batch-data-mongodb`(+test) — 0 применений (ОТЛОЖЕНО)
- `com.example.spring-boot-batch-jdbc` — `spring-boot` — + `spring-boot-starter-batch-jdbc`(+test) — 0 применений (ОТЛОЖЕНО)
- `com.example.spring-boot-bootable` — `spring-boot` + `id("org.springframework.boot")` (bootable-ось, единственная причина второго id — typed-аксессор `bootJar`/`developmentOnly`) — читает `mainClass` из `user-note/.main-class` — 8 применений (все 8 листьев). Имя — не заимствовано у Spring Boot (официальный термин — «executable jar/archive», не «bootable», проверено `docs.spring.io/spring-boot/gradle-plugin/packaging.html` 2026-09-24) — одно из 5 исключений из правила именования, см. `CLAUDE.md` → «Правила»
- `com.example.spring-boot-client-restclient` — `spring-boot` — + `spring-boot-starter-restclient`(+test) — 0 применений (ОТЛОЖЕНО)
- `com.example.spring-boot-client-webclient` — `spring-boot` — + `spring-boot-starter-webclient`(+test) — 0 применений (ОТЛОЖЕНО)
- `com.example.spring-boot-data-elasticsearch` — `spring-boot` — + `spring-boot-starter-data-elasticsearch`(+test) — 0 применений (ОТЛОЖЕНО)
- `com.example.spring-boot-data-jdbc` — `spring-boot` — + `spring-boot-starter-data-jdbc`(+test) — 4 применения (`data-jdbc` + листья `application-{h2,mysql,postgresql}` — применяется и на самом `data-jdbc`, и напрямую на каждом потребляющем его листе)
- `com.example.spring-boot-data-jpa` — `spring-boot` — + `spring-boot-starter-data-jpa`(+test) — 0 применений (`data-jpa` удалён из сборки повторно 2026-09-13; JPA — возможная будущая ветка корневого дерева вариантов, сегодня её нет)
- `com.example.spring-boot-data-mongodb` — `spring-boot` — + `spring-boot-starter-data-mongodb`(+test) — 2 применения (`data-mongodb`, `application-mongodb`)
- `com.example.spring-boot-data-mongodb-reactive` — `spring-boot` — + `spring-boot-starter-data-mongodb-reactive`(+test) — 2 применения (`data-mongodb-reactive`, `application-mongodb-reactive`)
- `com.example.spring-boot-data-r2dbc` — `spring-boot` — + `spring-boot-starter-data-r2dbc`(+test) — 4 применения (`data-r2dbc` + листья `application-{h2,mysql,postgresql}-reactive`)
- `com.example.spring-boot-database-h2` — `spring-boot` — + `h2database`(runtimeOnly) — 1 применение (`application-h2`)
- `com.example.spring-boot-database-mysql` — `spring-boot` — + `mysql-connector-j`(runtimeOnly) — 1 применение (`application-mysql`)
- `com.example.spring-boot-database-postgresql` — `spring-boot` — + `postgresql`(runtimeOnly) — 1 применение (`application-postgresql`)
- `com.example.spring-boot-database-r2dbc-h2` — `spring-boot` — + `r2dbc-h2`(runtimeOnly) — 1 применение (`application-h2-reactive`)
- `com.example.spring-boot-database-r2dbc-mysql` — `spring-boot` — + `r2dbc-mysql`(runtimeOnly, io.asyncer) — 1 применение (`application-mysql-reactive`)
- `com.example.spring-boot-database-r2dbc-postgresql` — `spring-boot` — + `r2dbc-postgresql`(runtimeOnly) — 1 применение (`application-postgresql-reactive`)
- `com.example.spring-boot-docker-compose` — `spring-boot` + `id("org.springframework.boot")` (тот же приём, что `bootable`, для typed-аксессора `developmentOnly`) — + `spring-boot-docker-compose`(developmentOnly) — 0 применений (ОТЛОЖЕНО — `compose.yaml` ещё не создан, см. `CLAUDE.md` → «Задачи» → п.2 «Docker Compose»)
- `com.example.spring-boot-graphql` — `spring-boot` — + `spring-boot-starter-graphql`(+test) — 0 применений (ОТЛОЖЕНО)
- `com.example.spring-boot-security-oauth2-authorization-server` — `spring-boot` — + `spring-boot-starter-security-oauth2-authorization-server`(+test) — 0 применений (ОТЛОЖЕНО)
- `com.example.spring-boot-security-oauth2-client` — `spring-boot` — + `spring-boot-starter-security-oauth2-client`(+test) — 0 применений (ОТЛОЖЕНО)
- `com.example.spring-boot-security-oauth2-resource-server` — `spring-boot` — + `spring-boot-starter-security-oauth2-resource-server`(+test) — 0 применений (ОТЛОЖЕНО)
- `com.example.spring-boot-test-h2` — `spring-boot` — + `h2`(testRuntimeOnly) — 1 применение (`data-jdbc`) — встроенная БД по умолчанию для `@DataJdbcTest`, только test-scope, не утекает в потребителей `data-jdbc`
- `com.example.spring-boot-test-r2dbc-h2` — `spring-boot` — + `r2dbc-h2`(testRuntimeOnly) — 1 применение (`data-r2dbc`) — зеркальная пара к `spring-boot-test-h2`
- `com.example.spring-boot-testcontainers` — `spring-boot` — + `spring-boot-testcontainers`(test) + `testcontainers-junit-jupiter`(test) — 6 применений (все листья кроме H2: `application-{mysql,postgresql}[-reactive]`, `application-mongodb[-reactive]`)
- `com.example.spring-boot-testcontainers-mongodb` — `spring-boot` — + `testcontainers-mongodb`(test) — 2 применения (`application-mongodb[-reactive]`)
- `com.example.spring-boot-testcontainers-mysql` — `spring-boot` — + `testcontainers-mysql`(test) + `mysql-connector-j`(testRuntimeOnly, нужен `MySQLContainer` для JDBC-based wait-strategy) — 2 применения (`application-mysql[-reactive]`)
- `com.example.spring-boot-testcontainers-postgresql` — `spring-boot` — + `testcontainers-postgresql`(test) — 2 применения (`application-postgresql[-reactive]`)
- `com.example.spring-boot-testcontainers-r2dbc` — `spring-boot` — + `testcontainers-r2dbc`(test) — 2 применения (`application-{mysql,postgresql}-reactive` — законная асимметрия: `PostgreSQLContainer` log-based wait-strategy не требует JDBC-драйвера, но R2DBC-фабрики Boot 4.1 всё равно вызывают `XR2DBCDatabaseContainer.getOptions(...)`, документация Testcontainers требует модуль на classpath для обеих R2DBC-веток)
- `com.example.spring-boot-validation` — `spring-boot` — + `spring-boot-starter-validation`(+test) — 0 применений (закрывало `webmvc`/`webflux`/`data-jpa` в старой архитектуре `note`/`user`; `controller-webmvc`/`controller-webflux`/`data-jdbc` сегодня его не применяют — не перепроверялось, нужна ли явная валидация в текущей плоской структуре `user-note`, см. `CLAUDE.md` → «Открытые решения» → «Область подключения `spring-boot-starter-validation`»)
- `com.example.spring-boot-webflux` — `spring-boot` — + `spring-boot-starter-webflux`(+test) — 5 применений (`controller-webflux` + листья `application-{h2,mysql,postgresql}-reactive`, `application-mongodb-reactive` — применяется и на модуле-адаптере, и напрямую на каждом потребляющем его листе)
- `com.example.spring-boot-webmvc` — `spring-boot` — + `spring-boot-starter-webmvc`(+test) — 5 применений (`controller-webmvc` + листья `application-{h2,mysql,postgresql,mongodb}`)
- `com.example.spring-cloud` — `spring-boot` — + Spring Cloud BOM (`spring-cloud-dependencies`) — 0 применений (родитель 9 плагинов уровня 3; `gateway`/`config`/`registry` — единственные потребители — удалены из репозитория 2026-08-14)

## Уровень 3 — родитель `spring-cloud` (9 плагинов, 0 применений — все потребители удалены 2026-08-14)

- `com.example.spring-cloud-circuit-breaker` — `spring-cloud` — + `spring-cloud-starter-circuitbreaker-reactor-resilience4j` — 0
- `com.example.spring-cloud-config-client` — `spring-cloud` — + `spring-cloud-starter-config` — 0 (был в `gateway/application`)
- `com.example.spring-cloud-config-server` — `spring-cloud` — + `spring-cloud-config-server` — 0 (был в `config/application`)
- `com.example.spring-cloud-eureka-client` — `spring-cloud` — + `spring-cloud-starter-netflix-eureka-client` — 0 (был в `gateway/application`)
- `com.example.spring-cloud-eureka-server` — `spring-cloud` — + `spring-cloud-starter-netflix-eureka-server` — 0 (был в `registry/application`)
- `com.example.spring-cloud-gateway-server-webflux` — `spring-cloud` — + `spring-cloud-starter-gateway-server-webflux` — 0 (был в `gateway/application`)
- `com.example.spring-cloud-gateway-server-webmvc` — `spring-cloud` — + `spring-cloud-starter-gateway-server-webmvc` — 0 (альтернатива webflux-варианту, не выбиралась)
- `com.example.spring-cloud-loadbalancer` — `spring-cloud` — + `spring-cloud-starter-loadbalancer` — 0 (ОТЛОЖЕНО)
- `com.example.spring-cloud-openfeign` — `spring-cloud` — + `spring-cloud-starter-openfeign` — 0 (ОТЛОЖЕНО)

---

## Как читать этот граф

**Одна ось** — BOM/toolchain-цепочка (`base → codequality/project-reactor/spring-boot/contract → …`, всегда 1 `com.example.*`-родитель по построению, без единого исключения — проверено сегодняшним прогоном проверочной команды выше) и **одна ортогональная** — bootable (`org.springframework.boot`, второй `id(...)` только там, где нужен typed-аксессор: `spring-boot-bootable`, `spring-boot-docker-compose`). **Диамантов вида 1 (2+ `com.example.*`-родителя внутри одного convention-плагина) в графе нет ни одного** — не было с 2026-08-01.

**Композиция 2+ convention-плагинов НА ЛИСТОВОМ модуле** (вид 2 — не диамант, штатный способ использования системы) — каждый из 8 `application-*` листьев компонует минимум 6 плагинов: `spring-boot`+`spring-boot-bootable`+`spring-boot-actuator`+свой веб-стартер (`webmvc`/`webflux`)+свой data-стартер+свой database/testcontainers-набор. Лист дополнительно применяет ТОТ ЖЕ `spring-boot-webmvc`/`-webflux`, что уже применён его модулем-зависимостью `controller-webmvc`/`controller-webflux` — не диамант (у каждого модуля свой собственный `plugins{}`, схождения путей внутри одного плагина нет), а следствие того, что `implementation(project(...))` не транслирует зависимости транзитивно на compile classpath потребителя.

**5 исключений из правила «имя плагина = официальное имя технологии»** (`base`/`contract`/`contract-reactive`/`codequality`/`spring-boot-bootable`) — полный разбор в `CLAUDE.md` → «Правила».

**Пустые (0 применений) плагины делятся на два класса**: (1) технология, для которой в проекте пока нет сервиса/модуля (`spring-cloud-*`, `spring-boot-batch*`, `spring-boot-security-oauth2-*`, `spring-boot-graphql`, `spring-boot-data-elasticsearch`, `spring-boot-client-*`, `spring-boot-docker-compose`) — готовы в `build-logic/`, ждут явного запроса; (2) технология, чей единственный потребитель в `user-note` удалён/переструктурирован (`spring-boot-data-jpa`, `spring-boot-validation`) — код плагина не удалён, т.к. может понадобиться снова (JPA — явная будущая ветка дерева вариантов).
