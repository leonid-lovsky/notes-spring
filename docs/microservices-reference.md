# Микросервисная архитектура `notes-spring` — справочный снимок (сессия 2026-09-26)

Снимок ТЕКУЩЕГО состояния консультаций, не история (история и полный ход рассуждения — `docs/user-note-access-model.md` → «Направление 2026-09-26…» и «Продолжение 2026-09-26…»). Статус всего файла: **консультация ассистента, решения пользователем не приняты, в коде ничего не реализовано** — пользователь пишет код сам (CLAUDE.md → «Правила» → «Роль ассистента»). `note/`·`user/`·`auth/`·`gateway/`·`registry/`·`config/` удалены из репозитория (2026-08-14, 2026-08-29) — ниже целевая картина, не план немедленной работы. Имена классов/интерфейсов/модулей — иллюстративные, не утверждены.

## Предпочтения пользователя, заданные в сессии 2026-09-26

- прагматичный подход, максимум стандартных возможностей Spring (Data, Security, WebMVC/WebFlux, Cloud и др.), автоконфигурация и настройки по умолчанию прежде своего кода
- максимально использовать все доступные технологии; дублирующие друг друга — с выбором одной при запуске приложения (конфликт с «вендор — граница Gradle-модуля» — решает пользователь, раздел «Выбор технологии при запуске»)
- use case — один интерфейс = один поток данных = один метод, stateless; свои модели входа/выхода «как request/response body», идентичные по смыслу переиспользуются, классы без необходимости не множить; контроллер только собирает вход, оборачивает результат, обрабатывает ошибки
- имена методов — ближе к HTTP (`get`/`put`/`delete`, коллекции — `list`)
- обсуждать только сущность, о которой идёт речь (поправка «мы сейчас говорим только про UserNote!») — CRUD заметки вне рамок без запроса
- `Permissions` временно отложены — рассматривается голая связь `UserNote (id, user_id, note_id)`; права вернулись только в вопросе про списки

## ⚠️ Основной вопрос микросервисной архитектуры (пользователь, 2026-09-26)

- как получать список всех заметок пользователя с перечнем прав и список всех пользователей заметки с перечнем прав, когда `name` и `content` живут в других сервисах
- рекомендация: хранение (В) — `user_note(id, user_id, note_id)` + `user_note_permission(id, user_note_id, permission)`; страница связей + права страницы вторым запросом (не N+1); курсор (keyset, непрозрачный `nextCursor`); обогащение у вызывающего (BFF) пакетными запросами `GET /notes?ids=…`, `GET /users?ids=…`
- варианты (А)/(Б)/(В), код выполнения, индексы, непроверенное (порядок UUID по вендорам, `Limit`/`…In` в 4.1.1) — `docs/user-note-access-model.md` → «Списки с перечнем прав доступа — три варианта хранения»

## Сервисы и ответственность

- `user` — профиль `(id, name)`; одиночное чтение + пакет по id; о заметках/правах не знает
- `note` — `(id, content)`; одиночное чтение + пакет по id; прав не хранит, перед выдачей проверяет доступ у `user-note`
- `user-note` — связи и права; сервис авторизации для заметок (Zanzibar): «может ли X сделать Y с заметкой Z» + два постраничных списка id с правами; внутри 8 листьев по вендорам, снаружи один сервис
- `auth` — Spring Authorization Server: вход, JWT, JWKS
- `gateway` — Spring Cloud Gateway + `oauth2-client`: единая точка входа и BFF (сессия браузера, `TokenRelay`, компоновка списков — единственный свой код)
- опционально: `registry` (Eureka — без Kubernetes), `config` (Config Server — при многих настройках)
- инфраструктура: база на сервис, брокер (Kafka/RabbitMQ), приёмник трассировки (OpenTelemetry Collector/Zipkin/Grafana)
- межсервисные потоки: чтение заметки — `note` синхронно спрашивает `user-note` (позже — локальная копия прав по событиям); создание заметки — две базы без общей транзакции: синхронный вызов с компенсацией или событие `NoteCreated` + transactional outbox; удаление заметки/пользователя — события `NoteDeleted`/`UserDeleted` → `user-note` удаляет связи, до событий сирот отсекает BFF
- стартеры по сервисам и непроверенное — `docs/user-note-access-model.md` → «Прагматичный набор сервисов на стандартных возможностях Spring»

## Классический набор Spring Cloud (таблица, присланная пользователем 2026-09-26) — сверка с рекомендациями

- API Gateway (единая точка входа, маршрутизация, аутентификация, ограничение частоты) — Spring Cloud Gateway — совпадает: `gateway` (+ BFF, `TokenRelay`, `RequestRateLimiter` на Redis)
- Service Registry & Discovery (без жёстко заданных адресов) — Netflix Eureka Server — совпадает как `registry`; ассистент пометил опциональным (в Kubernetes то же даёт DNS сервисов); при желании пользователя «максимум технологий» — включать, с Spring Cloud LoadBalancer (`lb://…`); плагины `spring-cloud-loadbalancer` есть, Eureka-плагинов нет
- Inter-Service Communication (синхронные декларативные REST-клиенты) — Spring Cloud OpenFeign — плагин `spring-cloud-openfeign` есть; ассистент рекомендовал HTTP interfaces (`@HttpExchange`, Spring Framework 7), т. к. OpenFeign в режиме поддержки; по правилу «дублирующие технологии — выбор при запуске» это пара без штатного переключателя — выбор профилем (`@Profile` на двух реализациях одного исходящего порта) или один из двух на сборке — не решено
- Resilience / Fault Tolerance (circuit breaker) — Resilience4j — совпадает: `spring-cloud-starter-circuitbreaker-reactor-resilience4j` на вызове `note → user-note`, плагин `spring-cloud-circuit-breaker` есть
- Centralized Configuration — Spring Cloud Config Server — совпадает как `config`; ассистент пометил опциональным (прагматично — свой `application.properties` + переменные окружения); при «максимуме технологий» — включать, с `spring-cloud-starter-config` в сервисах и `spring-cloud-bus` для рассылки обновлений
- итог: таблица — классический набор Spring Cloud Netflix-эпохи; при предпочтении пользователя «максимум технологий» все пять входят в целевую картину, т. е. 7 Spring-приложений (`auth`, `gateway`, `registry`, `config`, `user`, `note`, `user-note`); открытое — OpenFeign против HTTP interfaces

## Компоненты Spring Cloud — подробности (консультация 2026-09-26)

Имена свойств с пометкой «по памяти» не сверены с метаданными jar (модулей нет в кэше Gradle — проект их не подключает); сверять при подключении.

- **Spring Cloud Gateway** (`gateway`, обязательно): стартер `spring-cloud-starter-gateway-server-webflux` (есть `-server-webmvc`); маршруты свойствами — предикат → фильтры → `lb://<сервис>` (пример: `/api/notes/*/users/**` и `/api/users/me/notes` → `lb://user-note`, `/api/notes/**` → `lb://note`, `/api/users/**` → `lb://user`); автосоздание маршрутов по реестру (по памяти `spring.cloud.gateway.discovery.locator.enabled`) — прагматичнее явные маршруты (видно, что открыто наружу); аутентификация — `oauth2-client` (authorization code, токены в серверной сессии, браузеру cookie), `TokenRelay`, только «вошёл ли» — права решает `user-note`, сервисы всё равно проверяют токен сами; ограничение частоты — `RequestRateLimiter` на reactive Redis, ключ — пользователь/IP, 429; из коробки — фильтр `CircuitBreaker` (Resilience4j), тайм-ауты, повторы только идемпотентных методов, CORS, трассировка OpenTelemetry; сессии BFF — Spring Session в Redis (несколько экземпляров `gateway`); свой код — только эндпоинты компоновки списков (обычные контроллеры в том же приложении); плагинов Gateway в `build-logic/` нет
- **Eureka** (`registry`): сервисы при старте регистрируются и подтверждают, что живы; вызов по имени `lb://user-note` вместо адреса, экземпляры одного сервиса — под одним именем, распределяет Spring Cloud LoadBalancer; сервер — `spring-cloud-starter-netflix-eureka-server` + `@EnableEurekaServer`, одиночному серверу — `eureka.client.register-with-eureka=false`, `fetch-registry=false` (по памяти); клиенты (`gateway`, `user`, `note`, `user-note`, `auth`) — `spring-cloud-starter-netflix-eureka-client` + `eureka.client.service-url.defaultZone`, имя — `spring.application.name`; вызовы — `spring-cloud-starter-loadbalancer` (маршруты gateway, HTTP-клиенты); место — лист (сквозная технология, как Actuator), у `user-note` на всех 8 листьях под одним именем `user-note`; в Kubernetes выключается `eureka.client.enabled=false` (по памяти) — штатный переключатель при запуске; плагин `spring-cloud-loadbalancer` есть, Eureka server/client — нет
- **OpenFeign** (вызовы между сервисами): интерфейс `@FeignClient(name = "user-note")` с аннотациями Spring MVC, реализацию генерирует автоконфигурация (`@EnableFeignClients`), с Eureka+LoadBalancer имя → адрес; место — исходящий адаптер потребителя (в `note` — модуль-клиент, реализующий порт «проверить доступ»), интерфейс описывает потребитель; токен пользователя — `RequestInterceptor`, сервисный — OAuth2 client credentials (по памяти `spring.cloud.openfeign.oauth2.enabled`); ⚠️ блокирующий, reactive-версии нет — на reactive-ветке только `@HttpExchange` поверх `WebClient`; варианты (не выбрано): только HTTP interfaces на обеих ветках (`RestClient`/`WebClient`) или OpenFeign на sync + HTTP interfaces на reactive (две реализации одного порта без штатного переключателя — профиль или сборка); плагин `spring-cloud-openfeign` есть
- **Resilience4j** (устойчивость): абстракция Spring Cloud Circuit Breaker (`CircuitBreakerFactory`/`ReactiveCircuitBreakerFactory`), реализация Resilience4j; стартер `spring-cloud-starter-circuitbreaker-reactor-resilience4j` (покрывает ли sync — не проверено); настройки свойствами на именованный вызов (по памяти `resilience4j.circuitbreaker.instances.<имя>.*`); кроме circuit breaker — повторы, тайм-ауты, bulkhead, ограничение частоты; место — модуль-клиент вокруг `note → user-note` (у OpenFeign связка по памяти `spring.cloud.openfeign.circuitbreaker.enabled=true` + fallback) и фильтр `CircuitBreaker` в `gateway`; ⚠️ fallback проверки доступа — только «запретить» (503, заметку не выдавать; «разрешить» — дыра в безопасности); повторять только идемпотентные `GET`/`PUT`/`DELETE` (идемпотентный `PUT` связи — поэтому безопасен), `POST` без ключа идемпотентности — нет; плагин `spring-cloud-circuit-breaker` есть
- **Config Server** (`config`): `spring-cloud-config-server` + `@EnableConfigServer`; хранилище по умолчанию — отдельный git-репозиторий (также файловая система, Vault, JDBC), файлы `{application}-{profile}.properties`; клиенты — `spring-cloud-starter-config` + `spring.config.import=optional:configserver:http://config:8888`; сочетается с профильными файлами модулей (`application-data-jdbc.properties` и т. п. — значения по умолчанию, Config Server переопределяет по окружениям, удалённое по умолчанию приоритетнее); `optional:` — штатный переключатель при запуске (без сервера — локальные настройки); обновление — `@RefreshScope` + `/actuator/refresh` или Spring Cloud Bus (`/busrefresh`) через Kafka/RabbitMQ; секреты — `{cipher}…` или Vault, пароли баз в открытом git не держать; порядок старта с Eureka — «discovery first» (по памяти `spring.cloud.config.discovery.enabled=true`) или «config first» (прямой адрес) — прагматично прямой адрес; место — лист, ядро и адаптеры не знают источника свойств; репозиторий конфигураций — отдельно от сборки сервисов; плагинов Config server/client нет
- сводка плагинов: есть — `spring-cloud-openfeign`, `-loadbalancer`, `-circuit-breaker`, `spring-boot-security-oauth2-client`; добавить атомарно (по одному на стартер, с `-test`-парой, если есть в `docs/spring-boot-starters-reference.md`) — Gateway, Eureka server/client, Config server/client

## Модель `UserNote` без прав (связь)

- `record UserNote(UUID userId, UUID noteId)` — ключ, вход, выход, доменное значение; `id` — суррогат хранилища; `UNIQUE (user_id, note_id)`; неизменяема
- use case: `GetUserNote.get` (`GET /notes/{noteId}/users/{userId}`, 200/404), `PutUserNote.put` (`PUT`, идемпотентно), `DeleteUserNote.delete` (`DELETE`, 204 и для отсутствующей), `ListUserNotes.list` (`GET /users/{userId}/notes`), `ListNoteUsers.list` (`GET /notes/{noteId}/users`); reactive — те же с `Mono<…>`, модели из `contract`; «не найдено» в reactive — сигнал ошибки, не пустой `Mono`
- хранение: `UserNoteRow(@Id id, userId, noteId, @Version version)` одинаково в 4 `data-*`; `DuplicateKeyException` при `PUT` — успех
- имя: оставить `UserNote` (не `NoteUser`); аналогия с `@ManyToMany` — по структуре да, но явная сущность обязательна

## Где реализуется Security

- `auth` — аутентификация (кто ты), выдача JWT
- `gateway` — сессия браузера (токены на сервере, браузеру cookie), `TokenRelay`, грубое `authenticated()`; не последний рубеж
- каждый сервис — resource server: `spring-boot-starter-security-oauth2-resource-server` + `issuer-uri`, проверка подписи локально по JWKS; грубая авторизация по scope при необходимости; прав на заметки в токене нет
- `user-note` — детальная авторизация в прикладном слое (use case проверки доступа), не в Spring Security
- `note` — применяет решение `user-note` (XACML: `user-note` — PDP, `note` — PEP)
- межсервисные вызовы — JWT пользователя или сервисный токен (client credentials)
- внутри `user-note`: `controller-*` — `SecurityFilterChain`/`SecurityWebFilterChain`, `@CurrentUserId` (`sub` → `UUID`), advice 404/403; `contract` и реализация use case — без Spring Security, `actorId` обычным значением; `data-*` — ничего; `application-*` — `issuer-uri`; плагин `com.example.spring-boot-security-oauth2-resource-server` уже есть в `build-logic/` (не применён)
- тесты: `jwt()` (MockMvc), `mockJwt()` (WebTestClient); тест на порт — без Security-контекста
- не делать: права только в `gateway`; правило в `@PreAuthorize`/`PermissionEvaluator`; права на заметки в JWT

## Где реализуется валидация

- `controller-*` — формат: разбор Spring (UUID, enum, JSON, тело) → 400 без кода; Bean Validation на параметрах/телах (`spring-boot-starter-validation`, плагин `spring-boot-validation` существует) → 400 `ProblemDetail` в webmvc и webflux
- `contract` — модели входа use case проверяют себя в компактном конструкторе `record` (чистая Java): правило одно для всех транспортов (HTTP, gRPC, события); рекомендация — контроллер не дублирует, исключение входа → 400
- реализация use case — правила, зависящие от состояния (существует ли связь, есть ли доступ, не последний ли управляющий)
- схема/`data-*` — только структурные `NOT NULL`/`UNIQUE`, одинаковые на 8 листьях; смысловые `CHECK` не ставить (расходятся по вендорам)
- между сервисами ссылочная целостность синхронно не проверяется — события + склейка в BFF (осознанно)

## Где реализуется обработка ошибок

- `data-*` — перевод ошибок хранилища: `DuplicateKeyException` при идемпотентном `PUT` — успех; `OptimisticLockingFailureException` → исключение `contract` (409); прочее → 500; типы Spring Data наружу не выходят
- реализация use case — только исключения `contract` (sync `throw`, reactive `Mono.error`)
- `controller-*` — один `@RestControllerAdvice` (по методу на исключение `contract`) → `ProblemDetail` (RFC 9457), побайтово одинаков в webmvc/webflux; ошибки Spring — встроенный `ProblemDetail` (`spring.mvc/webflux.problemdetails.enabled` уже включены); неожиданные — 500 без стека, лог один раз на границе с trace id
- Spring Security — 401/403 из цепочки фильтров до контроллера (по умолчанию `WWW-Authenticate`, не `ProblemDetail`; единый формат — свои `AuthenticationEntryPoint`/`AccessDeniedHandler`)
- между сервисами — клиент переводит чужие 404/403 в свои исключения (чужой `ProblemDetail` наружу не проброс); недоступность — circuit breaker → 503; `gateway` — 504/503
- события — повторы + DLQ binder-а (имена свойств не сверены); обработчики идемпотентны

## Где реализуются технологии (Redis, Kafka, OpenFeign и др.)

- правило: технология = адаптер на границе гексагона = отдельный Gradle-модуль + атомарный convention-плагин; ядро (`contract` + use case) технологий не знает; лист `application-*` решает, что попадает в сборку
- входящие (driving) адаптеры — вызывают use case: HTTP-контроллеры, слушатели брокера, gRPC, GraphQL, Spring Batch
- исходящие (driven) — реализуют выходной порт из `contract`: хранилище, кэш, публикация событий, HTTP-клиенты к другим сервисам
- Kafka/RabbitMQ: приём — входящий модуль (`Consumer<NoteDeleted>`, Spring Cloud Stream); публикация — исходящий (`StreamBridge` или вынос событий Spring Modulith/outbox — реестра для R2DBC нет); смена брокера — смена binder-а
- OpenFeign/HTTP interfaces: исходящий адаптер вызывающего сервиса; интерфейс клиента описывает потребитель, не библиотека поставщика; рекомендация — `@HttpExchange` (OpenFeign в режиме поддержки); рядом LoadBalancer и circuit breaker
- Redis: кэш — декоратор порта хранилища или `@Cacheable` на адаптере (права не кэшировать без замеров — отозванный доступ живёт до истечения); сессии BFF — Spring Session в `gateway`; ограничение частоты — `RequestRateLimiter` в `gateway`; основное хранилище — новая ветка дерева, сейчас не нужна
- Elasticsearch — проекция поиска по событиям (CQRS); gRPC/GraphQL — входящие соседи `controller-*`; сквозное (Security, Actuator, OpenTelemetry, Docker Compose) — `controller-*`/лист/convention-плагины
- `build-logic/` на 2026-09-26: есть (не применены) — `spring-cloud-openfeign`, `-loadbalancer`, `-circuit-breaker`, `spring-boot-security-oauth2-{resource-server,client,authorization-server}`, `spring-boot-graphql`, `spring-boot-data-elasticsearch`, `spring-boot-batch*`; нет — Redis, Kafka, RabbitMQ, Spring Cloud Stream, Spring Cache, Spring Session

## Выбор технологии при запуске (желание пользователя 2026-09-26)

- механизм Spring: автоконфигурация по `@ConditionalOnClass`/`@ConditionalOnProperty`/`@ConditionalOnMissingBean`
- есть штатный переключатель — выбор при запуске бесплатный: `spring.main.web-application-type` (сверено, jar 4.1.1); вендор по `spring.datasource.url`/`spring.r2dbc.url`; `spring.cache.type`; `spring.cloud.stream.default-binder`; фабрика HTTP-клиента; включение экспортёров трассировки/метрик — имена кроме первого по памяти, сверить по метаданным при подключении; Spring Session — по памяти `store-type` убран в Boot 3, хранилище по classpath (не проверено)
- переключателя нет (JDBC+Mongo — строгий режим репозиториев Spring Data; два контроллера; два адаптера одного порта) — профили: `spring.autoconfigure.exclude` в профильном файле + `@Profile` на наших адаптерах (уже работающий механизм — профильные файлы модулей + `spring.profiles.include`)
- цена «всё на classpath»: все драйверы в артефакте (размер, уязвимости — причина решения 2026-09-05); выключенные технологии пытаются подключиться, Actuator health `DOWN` — `management.health.<x>.enabled=false` или исключение автоконфигурации; комбинаций больше, чем листьев (центральный вопрос умножается); sync+reactive в одном приложении — два набора бинов
- рекомендация — правило по осям: при запуске — интеграционные технологии со штатным переключателем (кэш, брокер, HTTP-клиент, экспортёры), каждый адаптер — модуль с профильным файлом `application-<tech>.properties` (`--spring.profiles.active=kafka,redis-cache`); при сборке — хранилище и web-стек (как сейчас); **вендор базы — решение пользователя**: оставить листья (рекомендация — дерево вариантов и центральный вопрос на них построены) или свернуть до выбора по URL (разворот решения 2026-09-05)

## Границы модулей и сервисов

- граница сервиса — физическая: своя база (без общих таблиц/FK/JOIN, чужую базу не читать); отдельный `bootJar`/образ/жизненный цикл; обмен только HTTP/событиями; свои модели API у каждого (одинаковые по форме DTO — разные классы); клиент описывает потребитель
- сборка на границе сервиса: каталог верхнего уровня на сервис; сильнее — отдельная сборка на сервис через `includeBuild(...)` (чужой `project(...)` не резолвится; CLAUDE.md «Открытые решения» → «Composite build на границе сервисов»); оговорка — composite build подменяет зависимости по координатам `group:name`, поэтому дополнительно проверять grep-ом отсутствие зависимостей на `com.example`-артефакты других сервисов; общий `build-logic/` через `pluginManagement { includeBuild(...) }` — общая логика сборки, не связанность во время выполнения
- проверка контракта между сервисами средствами Spring — Spring Cloud Contract (в справочнике Initializr: плагин `org.springframework.cloud.contract`, `spring-cloud-starter-contract-verifier` у поставщика, `-stub-runner` у потребителя) — потребитель проверяет своего клиента на заглушках, сгенерированных из контрактов поставщика; не выбрано
- граница модуля внутри сервиса — Gradle-модуль: компилятор не даёт импортировать класс модуля, от которого нет зависимости — сильнейшая граница; направление зависимостей: адаптеры (`controller-*`, `data-*`, будущие) → `contract`; реализация use case → `contract`; лист → всё; адаптер → адаптер — никогда; `contract` без Spring (jspecify, Reactor у reactive)
- проверки: leaf-purity-grep (CLAUDE.md «Правила» → «Только convention-плагины»); grep направления `project(...)`-зависимостей по `build.gradle.kts` (например, `data-*` не зависит от `controller-*`); механическая проверка сборкой (Gradle-таск/ArchUnit) — эскалация, только по «делай» пользователя
- ⚠️ находка: все модули `user-note` сегодня в одном пакете `com.example.usernote` (split package между jar-ами) — package-private не скрывает члены между модулями на classpath, а sync/reactive-интерфейсы с одинаковыми именами столкнутся; вариант — пакет на роль модуля (`com.example.usernote` — `contract`, `.reactive`, `.controller`, `.data`), `@SpringBootApplication` в корневом пакете сканирует подпакеты; расходится с нынешней зеркальностью «один пакет везде» — решение пользователя
- выбор модулей при запуске — профильные файлы модулей (раздел «Выбор технологии при запуске»)

## Открыто (решает пользователь)

- вендор базы: листья или выбор по URL при запуске
- хранение прав (А)/(Б)/(В) для списков; кто видит списки (право на список пользователей заметки)
- пакеты модулей (split package) и имена reactive-интерфейсов
- где живут реализация use case и общие классы двух `controller-*`
- сервер авторизации (Spring Authorization Server или внешний), BFF в `gateway` или клиент, outbox для R2DBC
- `includeBuild` на границе сервисов, Spring Cloud Contract
