# spring-boot-testing-reference.md

## Full-context (`@SpringBootTest`)

- `@SpringBootTest`
- `@AutoConfigureMockMvc` / `MockMvc` / `MockMvcTester`
- `@AutoConfigureWebTestClient` / `WebTestClient`
- `@AutoConfigureTestRestTemplate` / `TestRestTemplate`
- `@AutoConfigureRestTestClient` / `RestTestClient`
- `@AutoConfigureTestDatabase`
- `@AutoConfigureWebServer`
- `@AutoConfigureJson`

## Мокирование бинов

- `@MockitoBean`
- `@MockitoSpyBean`
- `@MockBean`/`@SpyBean` — удалены в Boot 4.0
- `MockitoExtension`

## Слайс-тесты по слою

### Web

- `@WebMvcTest`
- `@WebFluxTest`

### Persistence

- `@DataJpaTest`
- `@JdbcTest`
- `@DataJdbcTest`
- `@DataR2dbcTest`
- `@DataMongoTest`
- `@DataCassandraTest`
- `@DataCouchbaseTest`
- `@DataElasticsearchTest`
- `@DataLdapTest`
- `@DataNeo4jTest`
- `@DataRedisTest`
- `@JooqTest`

### Client / внешние сервисы

- `@RestClientTest`
- `@WebClientTest`
- `@WebServiceClientTest`
- `@WebServiceServerTest`

### Прочее

- `@JsonTest`
- `@GraphQlTest`

## Testcontainers

- `@Testcontainers`
- `@Container`
- `@ServiceConnection`
- `JdbcConnectionDetails`
- `R2dbcConnectionDetails`
- `MongoConnectionDetails`
- `@DynamicPropertySource`
- `@ImportTestcontainers`
