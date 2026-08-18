# spring-boot-testing-reference.md

> Справочник тестовых уровней/аннотаций/классов Spring Boot, актуальный на Spring Boot 4.1 (проверено 2026-08-18). Источники — только docs.spring.io, GitHub spring-projects (release notes/wiki migration guides/javadoc), spring.io блог — не Maven Central, не общий веб-поиск (см. CLAUDE.md → «Правила» → «Источник истины для Spring-фактов»). Собран для решения о структуре тестов при TDD-переписывании проекта (см. `docs/decisions-log.md` → «Архитектура» → «Тесты — на уровне application/bootable-модулей»). Формат — плоский список, не таблица (см. CLAUDE.md → «Правила» → «Документация — только для ИИ-агента»). Пункты, помеченные «не подтверждено независимо», нуждаются в отдельной проверке перед тем, как на них полагаться.

## Уровни тестирования

- **Unit** — голая JUnit 5 + AssertJ, без `ApplicationContext`, Spring не участвует
- **Slice-тесты** (`@…Test`) — поднимают только часть контекста под одну технологию; нельзя комбинировать несколько `@…Test` на одном классе — вместо этого `@AutoConfigure…`-аннотации поверх одного slice (источник — docs.spring.io, `testing/spring-boot-applications.html`)
- **Full-context** (`@SpringBootTest`) — весь `ApplicationContext` через реальный `SpringApplication`, см. «`@SpringBootTest` — детали» ниже
- **Testcontainers-интеграционные** — full-context + `@Testcontainers`/`@Container`/`@ServiceConnection`, реальный вендор в Docker, см. «Testcontainers-слой» ниже
- **Architecture-тесты** (ArchUnit) — не Spring-специфично, сканирует байткод через reflection, отдельная библиотека; уже отдельный открытый вопрос проекта (CLAUDE.md → «Открытые решения» → «Симметрия ArchUnit»), здесь не разбирается подробно

## `@SpringBootTest` — детали

- `webEnvironment` — `MOCK` (дефолт, без сервера), `RANDOM_PORT`, `DEFINED_PORT`, `NONE`
- `useMainMethod` — `ALWAYS`/`WHEN_AVAILABLE`, управляет вызовом реального `main()`
- **С Spring Boot 4.0 `@SpringBootTest` больше не поднимает MockMvc/WebClient/TestRestTemplate автоматически** — обязателен явный `@AutoConfigureMockMvc`/`@AutoConfigureWebTestClient`/`@AutoConfigureTestRestTemplate` (источник — GitHub wiki `Spring-Boot-4.0-Migration-Guide`; уже задокументировано в CLAUDE.md → «Стек» → «Spring Boot 4 — критичные отличия от Boot 3»)
- `@Transactional`-тесты откатываются по умолчанию, КРОМЕ `RANDOM_PORT`/`DEFINED_PORT` — HTTP-клиент и сервер в разных потоках/транзакциях, отката не происходит (источник — docs.spring.io, `spring-boot-applications.html`)

## HTTP-тестовые клиенты

- **`MockMvc`** — классический, без AssertJ, требует `@AutoConfigureMockMvc`
- **`MockMvcTester`** — AssertJ-обёртка над MockMvc (`assertThat(mvc.get()...)`), появился в Spring Framework 6.2.0 (ноябрь 2024) → доступен с Spring Boot 3.4.0 (источник — spring.io блог «Spring Framework 6.2.0-M1», javadoc `MockMvcTester` в 6.2.x API)
- **`RestTestClient`** — новый класс, появился именно в Spring Framework 7 / Spring Boot 4.0 (не было в 3.x), рекомендуемая замена `TestRestTemplate`; 4 режима биндинга — к контроллеру напрямую без контекста, к MockMvc (slice), к application context без сервера, к реальному серверу; требует явный `@AutoConfigureRestTestClient` (источник — docs.spring.io, package summary `org.springframework.boot.resttestclient`, Boot 4.0.5 API)
- **`TestRestTemplate`** — переехал в пакет `org.springframework.boot.resttestclient`, требует зависимость `spring-boot-restclient` в рантайме и явный `@AutoConfigureTestRestTemplate` — breaking change Boot 4.0 (раньше был в `org.springframework.boot.test.web.client`, поднимался неявно). В Boot 4.1 добавлен метод `withCookieHandling(...)` для согласования с `RestTemplate` (источники — docs.spring.io `test-utilities.html`, GitHub wiki `Spring-Boot-4.1-Release-Notes`)
- **`WebTestClient`** — для WebFlux, требует `@AutoConfigureWebTestClient`, структурно не изменился

## Mock/Spy — переломный момент Boot 3.4 → 4.0

- **`@MockitoBean`/`@MockitoSpyBean`** — аннотации Spring Framework (`org.springframework.test.context.bean.override.mockito`), появились в Spring Framework 6.2.0 (14 ноября 2024) → доступны с Spring Boot 3.4.0, заменили Boot-специфичные `@MockBean`/`@SpyBean` (deprecated с 3.4) — источник: spring.io блог «Spring Framework 6.2.0 Available Now», GitHub wiki
- **`@MockBean`/`@SpyBean` полностью удалены в Spring Boot 4.0** — не deprecated, а removed (источник — GitHub wiki `Spring-Boot-4.0-Migration-Guide`; уже задокументировано в CLAUDE.md → «Стек»)
- **Практическое отличие, не только переименование**: `@MockitoBean`/`@MockitoSpyBean` работают как поля тестового класса, но не внутри `@Configuration`-классов — общие моки через `@Bean` в `@Configuration` в Boot 4 так не работают
- **`MockitoTestExecutionListener` удалён** в Boot 4.0 (был deprecated в 3.4) — для `@Mock`/`@Captor`-полей нужен явный `MockitoExtension` из самого Mockito, не автоматика от Boot
- **Spring Framework 7.0**: Bean Overrides (`@MockitoBean`, `@MockitoSpyBean`, `@TestBean`) теперь применимы и к non-singleton бинам (prototype/custom scope), расширение относительно 6.2 (источник — GitHub wiki `Spring-Framework-7.0-Release-Notes`)
- **Релевантно для проекта**: раз даже подмена бина в Boot 4 требует Spring-специфичной bean-override инфраструктуры, а не голого Mockito — уже принятое в проекте решение «без Mockito, реальный стек» (см. `docs/decisions-log.md` → «Тесты — на уровне application/bootable-модулей») архитектурно последовательно, не идёт наперекор текущей платформе

## Slice-тесты по технологиям (Boot 4.1, отдельные `*-test`-артефакты)

- `spring-boot-webmvc-test` → `@WebMvcTest` — сканирует `@Controller`/`@ControllerAdvice`/`Filter`/`HandlerInterceptor`/`WebMvcConfigurer`/`Converter`, не сканирует обычные `@Component`/`@ConfigurationProperties`
- `spring-boot-webflux-test` → `@WebFluxTest`
- `spring-boot-data-jpa-test` → `@DataJpaTest` — сканирует `@Entity`, настраивает Spring Data JPA repositories + embedded БД, если есть на classpath
- `spring-boot-jdbc-test` → `@JdbcTest` — только `DataSource`+`JdbcTemplate`, без Spring Data JDBC repositories
- `spring-boot-data-jdbc-test` → `@DataJdbcTest` — то же плюс настоящие Spring Data JDBC repositories (это и есть разница `@JdbcTest`/`@DataJdbcTest`)
- `spring-boot-data-r2dbc-test` → `@DataR2dbcTest` — `R2dbcEntityTemplate` + Spring Data R2DBC repositories + embedded БД
- `spring-boot-data-mongodb-test` → `@DataMongoTest` — одна аннотация и для blocking, и для reactive (не два разных слайса), тип шаблона (`MongoTemplate`/`ReactiveMongoTemplate`) определяется по classpath автоматически
- `spring-boot-restclient-test` → `@RestClientTest`
- `spring-boot-webclient-test` → `@WebClientTest`
- **JSON-тестирование** (`@JsonTest`) — `JacksonTester`/`GsonTester`/`JsonbTester`/`BasicJsonTester`; в Boot 4/Jackson 3 поддерживается новый `JsonMapper` (через `@JacksonComponent`/`JacksonModule`) наряду со старым Jackson 2 `ObjectMapper` (deprecated-путь, не основной)

**Важная находка для стратегии проекта «embedded H2/MongoDB без контейнеров»**: у `@DataMongoTest` embedded MongoDB — не встроенная в Spring Boot возможность. Автоконфигурация embedded-Mongo (Flapdoodle) убрана из Spring Boot ещё с версии 2.7.0 — для «embedded Mongo без Docker» нужна отдельная сторонняя библиотека `de.flapdoodle.embed.mongo.spring{3x,4x}` как явная test-зависимость, не то, что появляется само при наличии на classpath. Совместимость этой библиотеки с Boot 4.0+ на момент проверки под вопросом — открытый GitHub issue `flapdoodle-oss/de.flapdoodle.embed.mongo.spring#77` (декабрь 2025) о поломке автоконфигурации именно на Boot 4.0 + Java 25, итоговый статус фикса не подтверждён независимо (комментарии issue не дочитаны до конца). Вероятная причина, по которой в проекте `application-mongodb` изначально выбрал Testcontainers, а не embedded (см. CLAUDE.md → «Задачи» → «Завершено 2026-07-23») — не просто «дороже», а «встроенного варианта у Spring Boot для Mongo нет вообще, только H2 умеет так из коробки». Источники: GitHub `flapdoodle-oss/de.flapdoodle.embed.mongo` README + issue #77, docs.spring.io `test-modules.html`

## `@AutoConfigureTestDatabase`

- Пакет — `org.springframework.boot.jdbc.test.autoconfigure` (Boot 4 паттерн реструктуризации под модульные `*-test`-артефакты)
- `replace` (какой `DataSource` подменять embedded-версией) — в старых версиях дефолт `ANY` заменял любой `DataSource`, включая `@ServiceConnection`-Testcontainers, требовал ручного `replace=NONE`
- **В Spring Boot 4.0.6 дефолт изменён на `NON_TEST`** — снижает необходимость ручного `replace=NONE`, когда БД уже задана тестом (например через `@ServiceConnection`). Точная патч-версия введения этого дефолта внутри линии 4.0.x — подтверждена только по актуальному javadoc URL 4.0.6, не проверена построчно по CHANGELOG — не подтверждено независимо
- Источники: docs.spring.io javadoc `AutoConfigureTestDatabase` (4.0.6 API), GitHub issue `spring-boot#35253`

## Testcontainers-слой

- `@Testcontainers`/`@Container` — из библиотеки Testcontainers (JUnit 5 extension), не Spring
- `@ServiceConnection` — появился в Spring Boot 3.1 (июнь 2023), в отдельном модуле `spring-boot-testcontainers`; заменил service-specific аннотации единой универсальной (источник — spring.io блог «Improved Testcontainers Support in Spring Boot 3.1», 23.06.2023)
- Готовые `ConnectionDetails`-биндинги под стек проекта: `JdbcConnectionDetails` (MySQL/PostgreSQL/MariaDB и др. через `JdbcDatabaseContainer`), `R2dbcConnectionDetails` (отдельно перечисленные `MySQLContainer`/`PostgreSQLContainer`/...), `MongoConnectionDetails` (`MongoDBContainer`, `MongoDBAtlasLocalContainer`)
- `@DynamicPropertySource` — низкоуровневая альтернатива `@ServiceConnection`, ручная регистрация свойств из контейнера — не нужна почти нигде в стеке проекта, раз `@ServiceConnection` покрывает MySQL/PostgreSQL/Mongo/R2DBC полностью
- `@ImportTestcontainers` — переиспользование объявления контейнеров через интерфейс между несколькими тестовыми классами
- Контейнеры как `@Bean` в `@TestConfiguration` — стартуют раньше остальных бинов, останавливаются позже; рекомендуемый способ, когда контекст кэшируется между тестами — релевантно плану shared-модуля с абстрактным JUnit-классом (см. `docs/decisions-log.md`)
- Источник: docs.spring.io, `spring-boot/reference/testing/testcontainers.html`

## Прочие `*-test`-модули Boot 4.1 (общий кругозор, не входят в стек проекта)

`spring-boot-cache-test` (без слайса) · `spring-boot-data-cassandra-test`→`@DataCassandraTest` · `spring-boot-data-couchbase-test`→`@DataCouchbaseTest` · `spring-boot-data-elasticsearch-test`→`@DataElasticsearchTest` · `spring-boot-data-ldap-test`→`@DataLdapTest` · `spring-boot-data-neo4j-test`→`@DataNeo4jTest` · `spring-boot-data-redis-test`→`@DataRedisTest` · `spring-boot-graphql-test`→`@GraphQlTest` · `spring-boot-grpc-test` (без слайса, новое в 4.1) · `spring-boot-jooq-test`→`@JooqTest` · `spring-boot-security-test` (без слайса) · `spring-boot-webserver-test`→`@AutoConfigureWebServer` (новое в 4.1) · `spring-boot-webservices-test`→`@WebServiceClientTest`+`@WebServiceServerTest`. Источник: docs.spring.io, `test-modules.html`

## Что нового в 4.1 относительно 4.0 (testing)

- `@AutoConfigureWebServer` — новая аннотация, добавляет embedded web server factory bean тестам (источник — GitHub wiki `Spring-Boot-4.1-Release-Notes`)
- Поддержка тестирования gRPC server/client-приложений (Netty/Servlet поверх HTTP/2) — новая область, не было в 4.0
- `TestRestTemplate.withCookieHandling(...)` — новый метод
- Kotlin-расширения для `TestEntityManager`
- `@ServiceConnection(type = RabbitStreamConnectionDetails.class)` — новый вариант для RabbitMQ Streams, не релевантно стеку проекта
- Дата релиза 4.1.0 — по независимому поиску встречается «11 июня 2026», GitHub wiki-страница релиз-нот последний раз редактировалась 26 июня 2026 без явной даты релиза в тексте самой страницы — не подтверждено независимо по `github.com/spring-projects/spring-boot/releases/tag/v4.1.0`

## Spring Framework 7 (несёт Boot 4) — изменения в `spring-test`

- JUnit 4-поддержка в TestContext Framework задепрекейчена — только `SpringExtension` для JUnit 5 вперёд
- `SpringExtension` теперь использует test-method-scoped `ExtensionContext` — DI работает в конструкторы/поля `@Nested`-классов через тот же `ApplicationContext`; потенциальный breaking change для кастомных `TestExecutionListener` — при поломке `@Nested`-тестов после апгрейда нужен `@SpringExtensionConfig(useTestClassScopedExtensionContext = true)` на верхнем классе
- Bean Overrides применимы к non-singleton бинам — см. «Mock/Spy» выше
- Источник: GitHub wiki `Spring-Framework-7.0-Release-Notes`

## Не подтверждено независимо (требует отдельной проверки перед тем, как полагаться)

- Точная патч-версия введения дефолта `NON_TEST` в `@AutoConfigureTestDatabase` внутри линии 4.0.x
- Текущий статус совместимости `de.flapdoodle.embed.mongo.spring` с Boot 4.0/4.1 (issue от декабря 2025 найден, итоговый resolution не подтверждён)
- Точная календарная дата релиза Boot 4.1.0 (расходится между источниками, первоисточник `releases/tag/v4.1.0` не сверен напрямую)
