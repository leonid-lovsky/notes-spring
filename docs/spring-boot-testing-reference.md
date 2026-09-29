# Тестирование Spring Boot 4.1 — аннотации и классы

> **Назначение:** тестовые уровни, аннотации и классы Spring Boot 4.1 с датами появления (проверено по docs.spring.io/GitHub spring-projects)
> **Когда читать:** при выборе тестовой аннотации или клиента — полный контекст, слайсы (web, persistence, client), мокирование, Testcontainers
> **Статус:** справочник, собран для TDD-переписывания сервисов `note`/`user`/`user-note`; выбор клиента ограничен правилом CLAUDE.md → «Полная изоляция стеков» (в sync-ветке без `spring-webflux`)
> **Разделы:** «Аннотации тестовых классов верхнего уровня — перечень (сверено 2026-09-29)», «Аннотации» (Full-context, Мокирование бинов, Слайс-тесты — Web/Persistence/Client/прочее, Testcontainers), «Классы» (HTTP-клиенты, Мокирование, Testcontainers)
> **Связано:** CLAUDE.md → «Архитектура и структура проекта» → «Модули по уровням и контексты тестов», `docs/spring-boot-starters-reference.md` (`-test`-стартеры)

## Аннотации тестовых классов верхнего уровня — перечень (сверено 2026-09-29)

Составные аннотации вида `@…Test`, которые ставятся на тестовый класс и сами определяют контекст теста; вспомогательные (`@Import`, `@MockitoBean`, `@AutoConfigure*`, `@TestConfiguration` и т. п.) сюда не входят. Сверено с официальной документацией Spring Boot 4.1.1: приложение «Test Slices» (`docs.spring.io/spring-boot/appendix/test-auto-configuration/slices.html`) — 19 слайсов; раздел «Testing Spring Boot Applications» (`docs.spring.io/spring-boot/reference/testing/spring-boot-applications.html`) — `@SpringBootTest` (полный контекст, не слайс) и `@JsonTest` (в приложении «Test Slices» отсутствует, но описан в разделе о тестировании: «you can use the `@JsonTest` annotation from the `spring-boot-test-autoconfigure` module»). Итого 21 аннотация Spring Boot; дополнено 2026-09-29 до полного доступного перечня (по запросу пользователя): составные аннотации Spring Framework — `@SpringJUnitConfig`, `@SpringJUnitWebConfig` (`docs.spring.io/spring-framework/reference/testing/annotations/integration-junit-jupiter.html`), и других проектов Spring — `@ApplicationModuleTest` (`docs.spring.io/spring-modulith/reference/testing.html`), `@SpringBatchTest` (`docs.spring.io/spring-batch/reference/testing.html`), `@ShellTest` (`docs.spring.io/spring-shell/reference/testing.html`); проверены именно эти проекты, перечень по другим проектам экосистемы (Spring Integration, Spring Kafka и т. п.) не собирался — их тестовые аннотации (`@SpringIntegrationTest`, `@EmbeddedKafka`), по памяти, вспомогательные и контекст не определяют — не проверялось. Всего 26; в проекте применяются 6 (помечены «У нас»). Список ниже — дословно в форме, утверждённой пользователем 2026-09-29, не переформатировать.

Полный контекст:
- @SpringBootTest — весь ApplicationContext. У нас — в 24 листьях application-*.

Слайс — веб:
- @WebMvcTest — контроллеры Spring MVC. У нас — controller-webmvc.
- @WebFluxTest — контроллеры Spring WebFlux. У нас — controller-webflux.
- @GraphQlTest — GraphQL-контроллеры.

Слайс — хранение:
- @DataJdbcTest — репозитории Spring Data JDBC. У нас — data-jdbc.
- @DataR2dbcTest — репозитории Spring Data R2DBC. У нас — data-r2dbc.
- @DataMongoTest — Spring Data MongoDB, одна аннотация для blocking и reactive. У нас — data-mongodb, data-mongodb-reactive.
- @DataJpaTest — репозитории Spring Data JPA.
- @JdbcTest — голый DataSource + JdbcTemplate, без Spring Data.
- @JooqTest — запросы jOOQ.
- @DataCassandraTest — репозитории Spring Data Cassandra.
- @DataCouchbaseTest — репозитории Spring Data Couchbase.
- @DataElasticsearchTest — репозитории Spring Data Elasticsearch.
- @DataLdapTest — репозитории Spring Data LDAP.
- @DataNeo4jTest — репозитории Spring Data Neo4j.
- @DataRedisTest — репозитории Spring Data Redis.

Слайс — клиенты внешних сервисов:
- @RestClientTest — клиенты RestClient/RestTemplate.
- @WebClientTest — реактивный клиент WebClient.
- @WebServiceClientTest — SOAP-клиенты.
- @WebServiceServerTest — SOAP-эндпоинты.

Слайс — прочее:
- @JsonTest — JSON-сериализация (Jackson/Gson/JSON-B).

Spring Framework (без Spring Boot):
- @SpringJUnitConfig — SpringExtension + @ContextConfiguration: контекст из явно указанной конфигурации, без автоконфигурации Boot.
- @SpringJUnitWebConfig — то же + @WebAppConfiguration: веб-контекст.

Другие проекты Spring:
- @ApplicationModuleTest — Spring Modulith: замена @SpringBootTest, контекст ограничен модулем приложения.
- @SpringBatchTest — Spring Batch: добавляет в контекст тестовые утилиты Batch, ставится вместе с @SpringBootTest или @SpringJUnitConfig.
- @ShellTest — Spring Shell: слайс для команд оболочки.

## Аннотации

### Full-context (`@SpringBootTest`)

- `@SpringBootTest` — поднимает весь `ApplicationContext`
- `@AutoConfigureMockMvc` — включает `MockMvc`/`MockMvcTester` внутри `@SpringBootTest`
- `@AutoConfigureWebTestClient` — включает `WebTestClient` внутри `@SpringBootTest`
- `@AutoConfigureTestRestTemplate` — включает `TestRestTemplate` внутри `@SpringBootTest`
- `@AutoConfigureRestTestClient` — включает `RestTestClient` внутри `@SpringBootTest`
- `@AutoConfigureTestDatabase` — подменяет `DataSource` на embedded-БД
- `@AutoConfigureWebServer` — поднимает embedded web server factory bean
- `@AutoConfigureJson` — настраивает JSON-тестеры без полного `@JsonTest`

### Мокирование бинов

- `@MockitoBean` — подменяет бин Mockito-моком
- `@MockitoSpyBean` — оборачивает бин Mockito-спаем
- `@MockBean` — удалена в Boot 4.0, заменена на `@MockitoBean`
- `@SpyBean` — удалена в Boot 4.0, заменена на `@MockitoSpyBean`

### Слайс-тесты — Web

- `@WebMvcTest` — слайс-тест Spring MVC контроллеров
- `@WebFluxTest` — слайс-тест Spring WebFlux контроллеров

### Слайс-тесты — Persistence

- `@DataJpaTest` — слайс-тест Spring Data JPA repositories + embedded БД
- `@JdbcTest` — слайс-тест голого `DataSource`+`JdbcTemplate`, без Spring Data JDBC
- `@DataJdbcTest` — слайс-тест Spring Data JDBC repositories
- `@DataR2dbcTest` — слайс-тест Spring Data R2DBC repositories
- `@DataMongoTest` — слайс-тест Spring Data MongoDB, одна аннотация для blocking и reactive
- `@DataCassandraTest` — слайс-тест Spring Data Cassandra repositories
- `@DataCouchbaseTest` — слайс-тест Spring Data Couchbase repositories
- `@DataElasticsearchTest` — слайс-тест Spring Data Elasticsearch repositories
- `@DataLdapTest` — слайс-тест Spring Data LDAP repositories
- `@DataNeo4jTest` — слайс-тест Spring Data Neo4j repositories
- `@DataRedisTest` — слайс-тест Spring Data Redis repositories
- `@JooqTest` — слайс-тест jOOQ-запросов

### Слайс-тесты — Client / внешние сервисы

- `@RestClientTest` — слайс-тест `RestClient`/`RestTemplate`-клиентов к внешним сервисам
- `@WebClientTest` — слайс-тест реактивного `WebClient`-клиента к внешним сервисам
- `@WebServiceClientTest` — слайс-тест SOAP-клиентов
- `@WebServiceServerTest` — слайс-тест SOAP-серверных эндпоинтов

### Слайс-тесты — прочее

- `@JsonTest` — слайс-тест JSON-сериализации (Jackson/Gson/JSON-B)
- `@GraphQlTest` — слайс-тест GraphQL-контроллеров

### Testcontainers

- `@Testcontainers` — JUnit 5-расширение Testcontainers, управляет жизненным циклом контейнеров
- `@Container` — помечает поле-контейнер, управляемое `@Testcontainers`
- `@ServiceConnection` — автосвязывание контейнера со Spring-конфигурацией
- `@DynamicPropertySource` — ручная регистрация свойств контейнера в `Environment`
- `@ImportTestcontainers` — переиспользование объявления контейнеров через интерфейс

## Классы

### Full-context — HTTP-клиенты

- `MockMvc` — вызов контроллеров без реального сервера, без AssertJ
- `MockMvcTester` — AssertJ-обёртка над `MockMvc`
- `WebTestClient` — HTTP-тестирование для WebFlux
- `RestTestClient` — универсальный HTTP-клиент, 4 режима биндинга (контроллер/MockMvc/context/сервер)
- привязка `WebTestClient` в Boot 4.1.1 (исходник `WebTestClientAutoConfiguration`, проверено 2026-09-29): при запущенном сервере — `bindToServer` (любой стек); в WebFlux-контексте — к контексту; в MVC-контексте — к `MockMvc` (`MockMvcWebTestClient`); условие — `WebClient`/`WebTestClient` на classpath, то есть `spring-webflux` + `spring-boot-webtestclient`; `@WebMvcTest` сам его не подключает (его `MockMvcWebClientAutoConfiguration` — HtmlUnit `WebClient`) — в проекте для sync-ветки недопустим по правилу изоляции стеков
- `TestRestTemplate` — HTTP-клиент против реального порта (`RANDOM_PORT`/`DEFINED_PORT`)

### Мокирование

- `MockitoExtension` — JUnit 5-расширение Mockito для `@Mock`/`@Captor`-полей (не Spring)

### Testcontainers

- `JdbcConnectionDetails` — готовый `ConnectionDetails`-биндинг для JDBC-вендоров
- `R2dbcConnectionDetails` — готовый `ConnectionDetails`-биндинг для R2DBC-вендоров
- `MongoConnectionDetails` — готовый `ConnectionDetails`-биндинг для MongoDB
