# Снимок графа convention-плагинов

> Новый 2026-08-01, снимок ТЕКУЩЕГО состояния (не история решений — история и обоснования «почему» остаются в `docs/decisions-log.md` → «Convention plugins — принцип именования и структура»). Причина создания — граф разбросан по нескольким хронологическим правкам `docs/decisions-log.md`, реконструировать текущее состояние оттуда занимает больше времени, чем прочитать один плоский список. Обновлять при каждом добавлении/удалении/переименовании convention-плагина или изменении его `plugins{}`-блока — в той же правке, не откладывая. Уже пережил несколько раундов правок в день создания — см. `docs/decisions-log.md` → «Атомизация базовых плагинов»: `base`→`java`, `library`→`java-library`, `reactor`→`project-reactor`, `codequality`-агрегатор инлайнирован, добавлены `spring-boot-validation` и `spring-boot-actuator` (воссоздан).

Проверочная команда (перегенерировать граф родитель→потомок по факту из файлов, свериться со списком ниже): `for f in build-logic/convention/src/main/kotlin/*.gradle.kts; do echo "$f: $(grep -oE 'id\("com\.example\.[a-zA-Z0-9.-]+"\)' "$f" | tr '\n' ' ')"; done`

Проверочная команда (число применений плагина в листовых модулях): `grep -rl 'id("com.example.{id}")' --include="build.gradle.kts" note user user-note auth registry config gateway | wc -l`

Схема каждой строки: **id плагина** — родитель(и) — что добавляет — число применений в листовых модулях сегодня (2026-08-01).

---

## Уровень 0 — корень

- `com.example.java` — родителя нет (сам применяет `id("java")` + 5 фрагментов `codequality-*` напрямую, без промежуточного агрегатора — см. «`codequality-*`» ниже) — toolchain из `.java-version`, `jakarta.validation-api`(implementation, делает аннотации видимыми в `domain/`), `junit-jupiter`(test) — 3 прямых применения (`note`/`user`/`user-note` → `domain/domain/build.gradle.kts`, единственные модули без Boot/reactor/java-library) + родитель 3 плагинов уровня 1

## Уровень 1 — родитель `java`

- `com.example.java-library` — `java` — + `java-library` (ядро Gradle, не Spring) — 6 применений (`contract`/`contract-reactive` × 3 сервиса — единственные модули, где `api(projects.*.domain)` реально нужен потребителям)
- `com.example.project-reactor` — `java` — переименован 2026-08-01 из `reactor` (точное имя технологии «Project Reactor») — + `reactor-core`(implementation) + `reactor-tools`(implementation) + `reactor-test`(test) — 3 применения (`contract-reactive` × 3 сервиса)
- `com.example.spring-boot` — `java` — + `io.spring.dependency-management` + Spring Boot BOM + `spring-boot-starter`(+test) — 0 прямых применений (только как родитель 33 плагинов уровня 2, из них 3 новых — `spring-boot-batch`/`-batch-jdbc`/`-batch-data-mongodb`, 2026-08-15)

## Уровень 2 — родитель `spring-boot` (технологические плагины, каждый = 1 Spring Boot стартер/концерн)

- `com.example.spring-boot-application` — `spring-boot` + `id("org.springframework.boot")` (bootable-ось, только это — actuator вынесен, см. `spring-boot-actuator` ниже) — 34 применения (все composition-root модули note/user/user-note/auth)
- `com.example.spring-boot-actuator` — `spring-boot` — новый 2026-08-01 (воссоздан — существовал до 2026-07-14, был слит в `spring-boot-application`) — + `spring-boot-starter-actuator`(+test) — 37 применений (34 листа с `spring-boot-application` + `registry/application`/`config/application`/`gateway/application`, где раньше приходил транзитивно через `spring-cloud-application`). В отличие от `spring-boot-validation` — расхождения в потребности нет (100% bootable-модулей хотят actuator), поэтому выбрана явная композиция на каждом листе (вид 2), а не встраивание в `spring-boot-application` вторым родителем (вид 1, тоже обсуждался — отклонён в пользу максимальной атомарности без диаманта, несмотря на больший объём правок)
- `com.example.spring-boot-batch` — `spring-boot` — новый 2026-08-15 — + `spring-boot-starter-batch`(+test) — 0 применений (ОТЛОЖЕНО, впервые появившаяся в проекте технология — см. `docs/decisions-log.md` → «Синхронизация версий»)
- `com.example.spring-boot-batch-data-mongodb` — `spring-boot` — новый 2026-08-15 — + `spring-boot-starter-batch-data-mongodb`(+test) — 0 применений (ОТЛОЖЕНО, применяется в композиции с `spring-boot-batch` на листе, не как родитель — тот же паттерн вид 2, что `spring-boot-application`+`spring-boot-database-h2`)
- `com.example.spring-boot-batch-jdbc` — `spring-boot` — новый 2026-08-15 — + `spring-boot-starter-batch-jdbc`(+test) — 0 применений (ОТЛОЖЕНО, композиция с `spring-boot-batch` на листе, см. выше)
- `com.example.spring-boot-client-restclient` — `spring-boot` — + `spring-boot-starter-restclient`(+test) — 0 применений (ОТЛОЖЕНО)
- `com.example.spring-boot-client-webclient` — `spring-boot` — + `spring-boot-starter-webclient`(+test) — 0 применений (ОТЛОЖЕНО)
- `com.example.spring-boot-data-elasticsearch` — `spring-boot` — + `spring-boot-starter-data-elasticsearch`(+test) + `spring-boot-starter-elasticsearch`(+test) — 0 применений (ОТЛОЖЕНО)
- `com.example.spring-boot-data-jdbc` — `spring-boot` — + `spring-boot-starter-data-jdbc`(+test) + `spring-boot-starter-jdbc`(+test) — 3 применения (`data-jdbc` × 3 сервиса)
- `com.example.spring-boot-data-jpa` — `spring-boot` — + `spring-boot-starter-data-jpa`(+test) — 3 применения (`data-jpa` × 3 сервиса, каждый композирует ещё `spring-boot-validation` — см. запись ниже)
- `com.example.spring-boot-data-mongodb` — `spring-boot` — + `spring-boot-starter-data-mongodb`(+test) + `spring-boot-starter-mongodb`(+test) — 3 применения (`data-mongodb` × 3 сервиса)
- `com.example.spring-boot-data-mongodb-reactive` — `spring-boot` — + `spring-boot-starter-data-mongodb-reactive`(+test) — 3 применения (`data-mongodb-reactive` × 3 сервиса)
- `com.example.spring-boot-data-r2dbc` — `spring-boot` — + `spring-boot-starter-data-r2dbc`(+test) + `spring-boot-starter-r2dbc`(+test) — 3 применения (`data-r2dbc` × 3 сервиса)
- `com.example.spring-boot-database-h2` — `spring-boot` — + `spring-boot-h2console`(implementation) + `h2database`(runtimeOnly) — 6 применений (`application-h2-jpa`/`-jdbc` × 3 сервиса)
- `com.example.spring-boot-database-h2-r2dbc` — `spring-boot` — + `r2dbc-h2`(runtimeOnly) — 3 применения (`application-h2-r2dbc` × 3 сервиса)
- `com.example.spring-boot-database-mysql` — `spring-boot` — + `mysql-connector-j`(runtimeOnly) — 6 применений (`application-mysql-jpa`/`-jdbc` × 3 сервиса)
- `com.example.spring-boot-database-mysql-r2dbc` — `spring-boot` — + `r2dbc-mysql`(runtimeOnly, io.asyncer) — 3 применения (`application-mysql-r2dbc` × 3 сервиса)
- `com.example.spring-boot-database-postgresql` — `spring-boot` — + `postgresql`(runtimeOnly) — 6 применений (`application-postgresql-jpa`/`-jdbc` × 3 сервиса)
- `com.example.spring-boot-database-postgresql-r2dbc` — `spring-boot` — + `r2dbc-postgresql`(runtimeOnly) — 3 применения (`application-postgresql-r2dbc` × 3 сервиса)
- `com.example.spring-boot-docker-compose` — `spring-boot` + `id("org.springframework.boot")` (bootable-ось, прямой id — не через `spring-boot-application` как родителя) — + `spring-boot-docker-compose`(developmentOnly) — 24 применения (все MySQL/PostgreSQL composition-root модули + `application-mongodb`/`-mongodb-reactive`, не H2 — embedded, не нуждается)
- `com.example.spring-boot-graphql` — `spring-boot` — + `spring-boot-starter-graphql`(+test) — 0 применений (ОТЛОЖЕНО)
- `com.example.spring-boot-security-oauth2-authorization-server` — `spring-boot` — + `spring-boot-starter-security-oauth2-authorization-server`(+test) — 0 применений (ОТЛОЖЕНО)
- `com.example.spring-boot-security-oauth2-client` — `spring-boot` — + `spring-boot-starter-security-oauth2-client`(+test) — 0 применений (ОТЛОЖЕНО)
- `com.example.spring-boot-security-oauth2-resource-server` — `spring-boot` — + `spring-boot-starter-security-oauth2-resource-server`(+test) — 0 применений (ОТЛОЖЕНО)
- `com.example.spring-boot-testcontainers` — `spring-boot` — нейтральная обвязка: + `spring-boot-testcontainers`(test) + `testcontainers-junit-jupiter`(test) — 24 применения (все MySQL/PostgreSQL/Mongo composition-root модули, симметрично `docker-compose`)
- `com.example.spring-boot-testcontainers-mongodb` — `spring-boot` — + `testcontainers-mongodb`(test) — 6 применений (`application-mongodb`/`-mongodb-reactive` × 3 сервиса)
- `com.example.spring-boot-testcontainers-mysql` — `spring-boot` — + `testcontainers-mysql`(test) + `mysql-connector-j`(testRuntimeOnly, нужен `MySQLContainer` для JDBC-based wait-strategy) — 9 применений (`application-mysql-{jpa,jdbc,r2dbc}` × 3 сервиса)
- `com.example.spring-boot-testcontainers-postgresql` — `spring-boot` — + `testcontainers-postgresql`(test) — 9 применений (`application-postgresql-{jpa,jdbc,r2dbc}` × 3 сервиса)
- `com.example.spring-boot-testcontainers-r2dbc` — `spring-boot` — + `testcontainers-r2dbc`(test) — 6 применений (`application-{mysql,postgresql}-r2dbc` × 3 сервиса)
- `com.example.spring-boot-validation` — `spring-boot` — новый 2026-08-01, + `spring-boot-starter-validation`(+test) — 9 применений (`webmvc`/`webflux`/`data-jpa` × 3 сервиса — единственные технологии с реальным автотриггером Bean Validation: `@Valid` на входящих данных и Hibernate pre-persist/pre-update соответственно; закрывает CLAUDE.md → «Открытые решения» → «Область подключения spring-boot-starter-validation», см. decisions-log.md → «Пересмотрено 2026-08-01»). Композиция с `spring-boot-webmvc`/`-webflux`/`-data-jpa` на этих 9 листовых модулях — вид 2 (осознанная композиция, не диамант, см. «Как читать этот граф» ниже)
- `com.example.spring-boot-webflux` — `spring-boot` — + `spring-boot-starter-webflux`(+test) — 3 применения (`webflux` × 3 сервиса, каждый композирует ещё `spring-boot-validation`)
- `com.example.spring-boot-webmvc` — `spring-boot` — + `spring-boot-starter-webmvc`(+test) — 3 применения (`webmvc` × 3 сервиса, каждый композирует ещё `spring-boot-validation`)
- `com.example.spring-cloud` — `spring-boot` — + Spring Cloud BOM (`spring-cloud-dependencies`) — 0 прямых применений (родитель 9 плагинов уровня 3 + `spring-cloud-application` ниже)

## Уровень 3 — родитель `spring-cloud` (9 плагинов, все — «чистая технология», без bootable)

Пересмотрено 2026-08-01: раньше 4 из этих 9 (`config-server`/`eureka-server`/`gateway-server-webflux`/`gateway-server-webmvc`) брали `spring-cloud-application` в родители — единственное место в графе, где технологический плагин сам нёс bootable-ось. Приведено к общему для всего проекта паттерну: технология — отдельно (родитель `spring-cloud`, только BOM), bootable — явной композицией на листе (см. `spring-cloud-application` ниже), см. decisions-log.md → «`spring-cloud-*`-server-плагины приведены к общему паттерну композиции»:

- `com.example.spring-cloud-circuit-breaker` — `spring-cloud` — + `spring-cloud-starter-circuitbreaker-reactor-resilience4j` — 0 применений (ОТЛОЖЕНО)
- `com.example.spring-cloud-config-client` — `spring-cloud` — + `spring-cloud-starter-config` — 1 применение (`gateway/application`)
- `com.example.spring-cloud-config-server` — `spring-cloud` — переехал с родителя `spring-cloud-application` 2026-08-01 — + `spring-cloud-config-server` — 1 применение (`config/application`, вместе с явным `spring-cloud-application`)
- `com.example.spring-cloud-eureka-client` — `spring-cloud` — + `spring-cloud-starter-netflix-eureka-client` — 1 применение (`gateway/application`)
- `com.example.spring-cloud-eureka-server` — `spring-cloud` — переехал с родителя `spring-cloud-application` 2026-08-01 — + `spring-cloud-starter-netflix-eureka-server` — 1 применение (`registry/application`, вместе с явным `spring-cloud-application`)
- `com.example.spring-cloud-gateway-server-webflux` — `spring-cloud` — переименован из `spring-cloud-gateway-webflux` (точное имя артефакта) и переехал с родителя `spring-cloud-application`, оба — 2026-08-01 — + `spring-cloud-starter-gateway-server-webflux` — 1 применение (`gateway/application`, вместе с явным `spring-cloud-application`)
- `com.example.spring-cloud-gateway-server-webmvc` — `spring-cloud` — переименован из `spring-cloud-gateway-webmvc` и переехал с родителя `spring-cloud-application`, оба — 2026-08-01 — + `spring-cloud-starter-gateway-server-webmvc` — 0 применений (альтернатива webflux-варианту в gateway, не выбрана)
- `com.example.spring-cloud-loadbalancer` — `spring-cloud` — + `spring-cloud-starter-loadbalancer` — 0 применений (ОТЛОЖЕНО)
- `com.example.spring-cloud-openfeign` — `spring-cloud` — + `spring-cloud-starter-openfeign` — 0 применений (ОТЛОЖЕНО)

## `spring-cloud-application` — bootable-ось для spring-cloud-модулей (симметрично `spring-boot-application`)

- `com.example.spring-cloud-application` — `spring-cloud` + прямой `id("org.springframework.boot")` (диамант устранён 2026-08-01 — раньше был вторым родителем через `spring-boot-application`, см. decisions-log.md → «Диамант `spring-cloud-application` устранён окончательно») — сам не добавляет зависимостей, только bootable-композиция — 3 прямых применения (`config/application`, `registry/application`, `gateway/application` — везде явно рядом со своим `spring-cloud-*`-технологическим плагином, тот же паттерн, что `spring-boot-application`+`spring-boot-database-h2` у реляционных composition-root)

## `codequality-*` — 5 приватных фрагментов, применяются напрямую из `java` (не через отдельный агрегатор)

До 2026-08-01 между `base`(ныне `java`) и этими 5 плагинами стоял отдельный `com.example.codequality` — чистый список из 5 `id(...)`, без собственной конфигурации, с единственным потребителем (`base`). Убран как враппер без переиспользования — 5 id перенесены напрямую в `plugins{}` `com.example.java.gradle.kts` (см. decisions-log.md → «Пересмотрено 2026-08-01»). Ни один из 5 не применяется напрямую ни в одном листовом модуле — только каскадом через `java`:

- `com.example.codequality-checkstyle` — `id("checkstyle")`, без `com.example.*` родителя — `SpringChecks` + `checkstyle.xml` + `spring-javaformat-checkstyle` — 0 прямых
- `com.example.codequality-jacoco` — `id("jacoco")`, без родителя — `jacocoTestReport` привязан к `test` — 0 прямых
- `com.example.codequality-jacoco-report-aggregation` — `id("jacoco-report-aggregation")`, без родителя, без своей конфигурации (autoconfig) — 0 прямых
- `com.example.codequality-jspecify` — `id("java")`, без `com.example.*` родителя — `implementation(jspecify)` — 0 прямых
- `com.example.codequality-nullaway` — `id("java")` + `id("net.ltgt.errorprone")`, без `com.example.*` родителя — NullAway как error — 0 прямых

---

## Как читать этот граф

Одна ось — BOM-цепочка (`java → java-library/reactor/spring-boot → spring-cloud → tech-плагин`, всегда 1 родитель по построению, без единого исключения) и одна ортогональная — bootable (`org.springframework.boot`, второй `id(...)` там, где нужен `bootJar`: `spring-boot-application`, `spring-cloud-application`, `docker-compose` — все три прямо, ни один не через другой). **Диамантов вида 1 (2+ `com.example.*`-родителя внутри одного convention-плагина, сходящихся в общем предке) в графе не осталось ни одного** — последний (`spring-cloud-application`) устранён 2026-08-01, см. decisions-log.md; исторически он же был единственным. Композиция 2+ convention-плагинов НА ЛИСТОВОМ модуле (вид 2 — например `application-h2-jpa` — `spring-boot-application`+`spring-boot-actuator`+`spring-boot-database-h2`, `webmvc`/`webflux`/`data-jpa` — своя технология + `spring-boot-validation`, `gateway/application` — `spring-cloud-application`+3 `spring-cloud-*`+`spring-boot-actuator`, или `application-mongodb` — `spring-boot-application`+`spring-boot-actuator`+`spring-boot-testcontainers`+`spring-boot-testcontainers-mongodb`+`spring-boot-docker-compose`) — не диамант для устранения, а штатный способ использования системы, применяемый теперь единообразно везде, включая `spring-cloud-*`-server-плагины (см. CLAUDE.md → «Правила» → «Кольцевые/ромбовидные зависимости» → вид 2).
