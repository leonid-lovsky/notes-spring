# spring-boot-testing-reference.md

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
- `TestRestTemplate` — HTTP-клиент против реального порта (`RANDOM_PORT`/`DEFINED_PORT`)

### Мокирование

- `MockitoExtension` — JUnit 5-расширение Mockito для `@Mock`/`@Captor`-полей (не Spring)

### Testcontainers

- `JdbcConnectionDetails` — готовый `ConnectionDetails`-биндинг для JDBC-вендоров
- `R2dbcConnectionDetails` — готовый `ConnectionDetails`-биндинг для R2DBC-вендоров
- `MongoConnectionDetails` — готовый `ConnectionDetails`-биндинг для MongoDB
