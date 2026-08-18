# spring-boot-testing-reference.md

## Аннотации

- `@AutoConfigureJson` — настраивает JSON-тестеры без полного `@JsonTest`
- `@AutoConfigureMockMvc` — включает `MockMvc`/`MockMvcTester` внутри `@SpringBootTest`
- `@AutoConfigureRestTestClient` — включает `RestTestClient` внутри `@SpringBootTest`
- `@AutoConfigureTestDatabase` — подменяет `DataSource` на embedded-БД
- `@AutoConfigureTestRestTemplate` — включает `TestRestTemplate` внутри `@SpringBootTest`
- `@AutoConfigureWebServer` — поднимает embedded web server factory bean
- `@AutoConfigureWebTestClient` — включает `WebTestClient` внутри `@SpringBootTest`
- `@Container` — Testcontainers: помечает поле-контейнер, управляемое JUnit-расширением
- `@DataCassandraTest` — слайс-тест Spring Data Cassandra repositories
- `@DataCouchbaseTest` — слайс-тест Spring Data Couchbase repositories
- `@DataElasticsearchTest` — слайс-тест Spring Data Elasticsearch repositories
- `@DataJdbcTest` — слайс-тест Spring Data JDBC repositories
- `@DataJpaTest` — слайс-тест Spring Data JPA repositories + embedded БД
- `@DataLdapTest` — слайс-тест Spring Data LDAP repositories
- `@DataMongoTest` — слайс-тест Spring Data MongoDB, одна аннотация для blocking и reactive
- `@DataNeo4jTest` — слайс-тест Spring Data Neo4j repositories
- `@DataR2dbcTest` — слайс-тест Spring Data R2DBC repositories
- `@DataRedisTest` — слайс-тест Spring Data Redis repositories
- `@DynamicPropertySource` — Testcontainers: ручная регистрация свойств контейнера в `Environment`
- `@GraphQlTest` — слайс-тест GraphQL-контроллеров
- `@ImportTestcontainers` — переиспользование объявления контейнеров через интерфейс
- `@JdbcTest` — слайс-тест голого `DataSource`+`JdbcTemplate`, без Spring Data JDBC
- `@JooqTest` — слайс-тест jOOQ-запросов
- `@JsonTest` — слайс-тест JSON-сериализации (Jackson/Gson/JSON-B)
- `@MockBean` — удалена в Boot 4.0, заменена на `@MockitoBean`
- `@MockitoBean` — подменяет бин Mockito-моком
- `@MockitoSpyBean` — оборачивает бин Mockito-спаем
- `@RestClientTest` — слайс-тест `RestClient`/`RestTemplate`-клиентов к внешним сервисам
- `@ServiceConnection` — Testcontainers: автосвязывание контейнера со Spring-конфигурацией
- `@SpringBootTest` — поднимает весь `ApplicationContext`
- `@SpyBean` — удалена в Boot 4.0, заменена на `@MockitoSpyBean`
- `@Testcontainers` — JUnit 5-расширение Testcontainers, управляет жизненным циклом контейнеров
- `@WebClientTest` — слайс-тест реактивного `WebClient`-клиента к внешним сервисам
- `@WebFluxTest` — слайс-тест Spring WebFlux контроллеров
- `@WebMvcTest` — слайс-тест Spring MVC контроллеров
- `@WebServiceClientTest` — слайс-тест SOAP-клиентов
- `@WebServiceServerTest` — слайс-тест SOAP-серверных эндпоинтов

## Классы

- `JdbcConnectionDetails` — Testcontainers: готовый `ConnectionDetails`-биндинг для JDBC-вендоров
- `MockitoExtension` — JUnit 5-расширение Mockito для `@Mock`/`@Captor`-полей (не Spring)
- `MockMvc` — вызов контроллеров без реального сервера, без AssertJ
- `MockMvcTester` — AssertJ-обёртка над `MockMvc`
- `MongoConnectionDetails` — Testcontainers: готовый `ConnectionDetails`-биндинг для MongoDB
- `R2dbcConnectionDetails` — Testcontainers: готовый `ConnectionDetails`-биндинг для R2DBC-вендоров
- `RestTestClient` — универсальный HTTP-клиент, 4 режима биндинга (контроллер/MockMvc/context/сервер)
- `TestRestTemplate` — HTTP-клиент против реального порта (`RANDOM_PORT`/`DEFINED_PORT`)
- `WebTestClient` — HTTP-тестирование для WebFlux
