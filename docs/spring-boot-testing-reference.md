# spring-boot-testing-reference.md

> Перечень тестовых аннотаций/классов Spring Boot 4.1, проверено 2026-08-18 по docs.spring.io и GitHub spring-projects (не Maven Central). Собран для TDD-переписывания проекта. Заметки/находки, требующие контекста — в конце файла, не перемешаны со списком.

## Уровни тестирования

- Unit — голая JUnit 5 + AssertJ, без `ApplicationContext`
- Slice (`@…Test`) — часть контекста под одну технологию; несколько `@…Test` на одном классе комбинировать нельзя
- Full-context (`@SpringBootTest`) — весь `ApplicationContext`
- Testcontainers-интеграционные — full-context + реальный вендор в Docker
- Architecture (ArchUnit) — не Spring-специфично, отдельная библиотека

## `@SpringBootTest` и его конфигурация

- `@SpringBootTest` — поднимает весь контекст; `webEnvironment` = `MOCK` (дефолт, без сервера) / `RANDOM_PORT` / `DEFINED_PORT` / `NONE`
- `@AutoConfigureMockMvc` — обязателен для `MockMvc`/`MockMvcTester` внутри `@SpringBootTest` (с Boot 4.0 не поднимается автоматически)
- `@AutoConfigureWebTestClient` — обязателен для `WebTestClient` внутри `@SpringBootTest`
- `@AutoConfigureTestRestTemplate` — обязателен для `TestRestTemplate` внутри `@SpringBootTest`
- `@AutoConfigureRestTestClient` — обязателен для `RestTestClient` внутри `@SpringBootTest`
- `@AutoConfigureTestDatabase` — подменяет `DataSource` на embedded-БД; `replace` = `ANY`/`NONE`/`NON_TEST` (дефолт `NON_TEST` с Boot 4.0.6)
- `@AutoConfigureWebServer` — поднимает embedded web server factory bean (новое в 4.1)
- `@AutoConfigureJson` — настраивает JSON-тестеры без полного `@JsonTest`

## HTTP-тестовые клиенты

- `MockMvc` — вызов контроллеров без реального сервера, без AssertJ
- `MockMvcTester` — AssertJ-обёртка над `MockMvc` (Spring Framework 6.2.0 / Boot 3.4.0+)
- `WebTestClient` — HTTP-тестирование для WebFlux (реактивный аналог MockMvc/TestRestTemplate)
- `RestTestClient` — новый универсальный клиент (Spring Framework 7 / Boot 4.0+), 4 режима биндинга: контроллер напрямую, MockMvc, application context без сервера, реальный сервер
- `TestRestTemplate` — HTTP-клиент против реального порта (`RANDOM_PORT`/`DEFINED_PORT`); в Boot 4 отдельный модуль `spring-boot-restclient`, метод `withCookieHandling(...)` добавлен в 4.1

## Мокирование

- `@MockitoBean` — подменяет бин Mockito-моком (Spring Framework 6.2.0 / Boot 3.4.0+)
- `@MockitoSpyBean` — оборачивает существующий бин Mockito-спаем (Spring Framework 6.2.0 / Boot 3.4.0+)
- `@MockBean`/`@SpyBean` — **удалены в Boot 4.0** (не deprecated — removed), заменены на `@MockitoBean`/`@MockitoSpyBean`
- `MockitoExtension` (сам Mockito, не Spring) — нужен явно для `@Mock`/`@Captor`-полей с Boot 4.0 (`MockitoTestExecutionListener` удалён)

## Слайс-тесты по технологии

- `@WebMvcTest` — Spring MVC контроллеры (`@Controller`/`@ControllerAdvice`/`Filter`/`HandlerInterceptor`/`WebMvcConfigurer`/`Converter`)
- `@WebFluxTest` — Spring WebFlux контроллеры
- `@DataJpaTest` — Spring Data JPA repositories + `@Entity` + embedded БД
- `@JdbcTest` — голый `DataSource`+`JdbcTemplate`, без Spring Data JDBC repositories
- `@DataJdbcTest` — Spring Data JDBC repositories (в отличие от `@JdbcTest`)
- `@DataR2dbcTest` — Spring Data R2DBC repositories + `R2dbcEntityTemplate`
- `@DataMongoTest` — Spring Data MongoDB, одна аннотация и для blocking, и для reactive
- `@JsonTest` — `JacksonTester`/`GsonTester`/`JsonbTester`/`BasicJsonTester`
- `@RestClientTest` — тестирование `RestClient`/`RestTemplate`-клиентов к внешним сервисам
- `@WebClientTest` — тестирование реактивного `WebClient`-клиента к внешним сервисам
- `@DataCassandraTest`/`@DataCouchbaseTest`/`@DataElasticsearchTest`/`@DataLdapTest`/`@DataNeo4jTest`/`@DataRedisTest`/`@GraphQlTest`/`@JooqTest`/`@WebServiceClientTest`/`@WebServiceServerTest` — не используются в текущем стеке проекта, для общего кругозора
- `spring-boot-grpc-test` — тестирование gRPC server/client, без отдельной аннотации-слайса, новое в 4.1

## Testcontainers-слой

- `@Testcontainers`/`@Container` — сама библиотека Testcontainers (JUnit 5 extension), не Spring
- `@ServiceConnection` — автосвязывание контейнера со Spring-конфигурацией (`spring-boot-testcontainers`, с Boot 3.1)
- `JdbcConnectionDetails`/`R2dbcConnectionDetails`/`MongoConnectionDetails` — готовые `ConnectionDetails`-биндинги под `@ServiceConnection`
- `@DynamicPropertySource` — ручная регистрация свойств из контейнера (низкоуровневая альтернатива `@ServiceConnection`)
- `@ImportTestcontainers` — переиспользование объявления контейнеров между тестовыми классами через интерфейс

## Заметки (контекст, не часть перечня)

- Slice-тесты и `@SpringBootTest` нельзя комбинировать на одном классе — источник docs.spring.io, `testing/spring-boot-applications.html`
- `@Transactional`-тест откатывается по умолчанию, кроме `RANDOM_PORT`/`DEFINED_PORT` (сервер и клиент в разных потоках/транзакциях)
- `@MockitoBean`/`@MockitoSpyBean` не работают внутри `@Configuration`-классов (в отличие от старых `@MockBean`/`@SpyBean`)
- У Spring Boot нет embedded MongoDB «из коробки» (Flapdoodle-автоконфигурация убрана в 2.7.0) — нужна сторонняя `de.flapdoodle.embed.mongo.spring{3x,4x}`, совместимость с Boot 4.0+/Java 25 под вопросом (открытый GitHub issue `flapdoodle-oss/de.flapdoodle.embed.mongo.spring#77`, декабрь 2025, resolution не подтверждён) — вероятная причина, почему `application-mongodb` в проекте изначально выбрал Testcontainers, а не embedded
- Не подтверждено независимо: точная патч-версия дефолта `NON_TEST` внутри линии Boot 4.0.x; итоговый статус issue про Flapdoodle; точная календарная дата релиза Boot 4.1.0
