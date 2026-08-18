# spring-boot-testing-reference.md

## `@SpringBootTest` и его конфигурация

- `@SpringBootTest` — поднимает весь контекст; `webEnvironment` = `MOCK` (дефолт, без сервера) / `RANDOM_PORT` / `DEFINED_PORT` / `NONE`
- `@AutoConfigureMockMvc` — обязателен для `MockMvc`/`MockMvcTester` внутри `@SpringBootTest`
- `@AutoConfigureWebTestClient` — обязателен для `WebTestClient` внутри `@SpringBootTest`
- `@AutoConfigureTestRestTemplate` — обязателен для `TestRestTemplate` внутри `@SpringBootTest`
- `@AutoConfigureRestTestClient` — обязателен для `RestTestClient` внутри `@SpringBootTest`
- `@AutoConfigureTestDatabase` — подменяет `DataSource` на embedded-БД
- `@AutoConfigureWebServer` — поднимает embedded web server factory bean
- `@AutoConfigureJson` — настраивает JSON-тестеры без полного `@JsonTest`

## HTTP-тестовые клиенты

- `MockMvc`
- `MockMvcTester`
- `WebTestClient`
- `RestTestClient`
- `TestRestTemplate`

## Мокирование

- `@MockitoBean`
- `@MockitoSpyBean`
- `@MockBean`/`@SpyBean` — удалены в Boot 4.0
- `MockitoExtension`

## Слайс-тесты по технологии

- `@WebMvcTest`
- `@WebFluxTest`
- `@DataJpaTest`
- `@JdbcTest`
- `@DataJdbcTest`
- `@DataR2dbcTest`
- `@DataMongoTest`
- `@JsonTest`
- `@RestClientTest`
- `@WebClientTest`
- `@DataCassandraTest`
- `@DataCouchbaseTest`
- `@DataElasticsearchTest`
- `@DataLdapTest`
- `@DataNeo4jTest`
- `@DataRedisTest`
- `@GraphQlTest`
- `@JooqTest`
- `@WebServiceClientTest`
- `@WebServiceServerTest`

## Testcontainers-слой

- `@Testcontainers`
- `@Container`
- `@ServiceConnection`
- `JdbcConnectionDetails`
- `R2dbcConnectionDetails`
- `MongoConnectionDetails`
- `@DynamicPropertySource`
- `@ImportTestcontainers`
