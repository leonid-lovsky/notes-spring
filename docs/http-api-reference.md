# HTTP API `notes-spring` — справочник по объявлению методов контроллера (сессия 2026-09-29)

> **Назначение:** итог обсуждения 2026-09-29 объявления `createNote` в `NoteController` — стандарты HTTP, итоговая сигнатура, семантика `replace`/`update`, все рассмотренные варианты по осям, `Location` и `UriComponentsBuilder`, gateway, аргументы методов контроллера
> **Когда читать:** при объявлении/изменении любого метода `controller-webmvc`/`controller-webflux` (`note`/`user`/`user-note`), при вопросах о кодах ответа, `Location`, gateway и путях
> **Статус:** решено пользователем 2026-09-29 — следовать стандартам (RFC 9110, RFC 9457), `Location` относительный, gateway без переписывания путей, семантика `replace` (замена целиком) и `update` (слияние, `null` игнорируется); открыто — место и состав `NoteRequest`/`NoteResponse` (CLAUDE.md → «Задачи» → «Решения, которые нужно принять» пп.1, 5), префикс `/api`, `@Valid` (п.7); код пишет пользователь
> **Разделы:** «Стандарты», «Итог для `createNote`», «Семантика `replace` и `update`», «Отступления семантики `replace`/`update` от принципов», «Все варианты объявления по осям», «`Location`», «`UriComponentsBuilder`», «Gateway и пути», «Аргументы метода контроллера»
> **Связано:** CLAUDE.md → «Правила» → «Придерживаемся стандартов, паттернов проектирования и лучших практик»; CLAUDE.md → «Задачи» → «Решения, которые нужно принять» п.4; CLAUDE.md → «Открытые решения» → «⚠️ ОСНОВНОЙ ВОПРОС МИКРОСЕРВИСНОЙ АРХИТЕКТУРЫ»; `docs/microservices-reference.md` → «Компоненты Spring Cloud — подробности» (Gateway)

## Стандарты

- REST — архитектурный стиль (диссертация Филдинга), не стандарт; строгий REST требует гипермедиа (HATEOAS, `spring-boot-starter-hateoas` есть в Initializr — `docs/spring-boot-starters-reference.md`), решение по HATEOAS не принято
- нормативная часть HTTP API — RFC 9110 (семантика HTTP: методы, коды, заголовки), RFC 9457 (Problem Details, в Spring — `ProblemDetail`), RFC 5789 (`PATCH`), RFC 7396 (JSON Merge Patch); «коллекция во множественном числе, `POST /notes`» — отраслевое соглашение (руководства Microsoft, Google AIP, Zalando), не RFC
- RFC 9110 §9.3.3 POST (сверено по `rfc-editor.org/rfc/rfc9110.txt`): «If one or more resources has been created on the origin server as a result of successfully processing a POST request, the origin server SHOULD send a 201 (Created) response containing a Location header field that provides an identifier for the primary resource created (Section 10.2.2) and a representation that describes the status of the request while referring to the new resource(s).»
- RFC 9110 §15.3.2 201: «The primary resource created by the request is identified by either a Location header field in the response or, if no Location header field is received, by the target URI.» — без `Location` у `POST /notes` созданным формально считается `/notes`, то есть коллекция
- RFC 9110 §10.2.2 Location: «The field value consists of a single URI-reference» — относительный адрес допустим, клиент разрешает его относительно адреса своего запроса
- RFC 9110 §9.3.4 PUT: если `PUT` создал ресурс, сервер «MUST inform the user agent by sending a 201 (Created) response»; при изменении существующего — «MUST send either a 200 (OK) or a 204 (No Content)» — создание через `PUT` с `id` от клиента тоже по стандарту
- RFC 9110 §9.3.5 DELETE: успех — SHOULD 202 (не выполнено ещё), 204 (выполнено, без содержимого) или 200 (с описанием результата)
- SHOULD (RFC 2119) — «следует, если нет веской причины иначе», отступление только с названной причиной; MUST не нарушается

## Итог для `createNote`

Решение 2026-09-29: 201 + относительный `Location` + созданный ресурс в теле. Имена `NoteRequest`/`NoteResponse` — рабочие, не утверждены; без них объявление не скомпилируется.

```java
// controller-webmvc
@PostMapping
public ResponseEntity<NoteResponse> createNote(@RequestBody NoteRequest request) {
    throw notImplemented();
}

// controller-webflux
@PostMapping
public Mono<ResponseEntity<NoteResponse>> createNote(@RequestBody NoteRequest request) {
    return Mono.error(notImplemented());
}
```

Возврат, когда метод заработает:

```java
URI location = UriComponentsBuilder.fromPath("/notes/{id}").buildAndExpand(id).toUri();
return ResponseEntity.created(location).body(response);
```

- `POST /notes` — создание в коллекции, `id` генерирует сервер (где именно — п.3)
- `@RequestBody NoteRequest` — клиент передаёт только `content` (вероятно `record NoteRequest(String content)`), свой `id` подставить не может
- `ResponseEntity<NoteResponse>` — 201 + тело (SHOULD RFC 9110 §9.3.3); `ResponseEntity` нужен ради `Location`; ответом может стать сама модель, если переедет в `contract` (п.1)
- `Location` — относительный; статический `UriComponentsBuilder.fromPath(...)` (не параметр метода) вместо `URI.create("/notes/" + id)` — шаблон в той же форме, что `@GetMapping("/{id}")`, значения кодируются, без ручной склейки строк; оба варианта по стандарту, рекомендация ассистента — шаблон
- ошибки не в сигнатуре — `ProblemDetail` (RFC 9457) из общего `@RestControllerAdvice`
- сиблинги различаются только обёрткой `Mono` — зеркальность сохранена

## Семантика `replace` и `update`

Задана пользователем 2026-09-29 — буквальное понимание: `replace` — замена целиком, `update` — слияние; обе совпадают с общепринятым смыслом. Где живёт логика (interactor или адаптер) — зависит от варианта чистой архитектуры (CLAUDE.md → «Открытые решения» → «Чистая архитектура»); ответ 200 + `{Entity}Response` у обеих — рекомендация ассистента (204 без тела тоже по стандарту, выбор за пользователем).

`replace` — `PUT /notes/{id}`, `replaceNoteByID`:
- берёт `id` из пути; ищет сущность по `id`; не найдена — выходим (404 `ProblemDetail`); найдена — полностью заменяем сущность на переданную в запросе
- поле, пришедшее `null` (или отсутствующее — для record это одно и то же), очищает поле в сущности/документе; обязательное поле с `null` — ошибка валидации 400 ещё до поиска сущности
- стандарт: RFC 9110 §9.3.4 — новое состояние равно присланному представлению; при замене существующего ресурса — MUST 200 или 204; `PUT` не создаёт (рекомендация п.4) — RFC это допускает
- модель запроса — та же, что у создания (`NoteRequest`, без `id`): расхождение «`id` в пути ≠ `id` в теле» невозможно; обязательные поля — `@NotNull`/`@NotBlank`, будущие необязательные — `@Nullable` (замена их очищает)
- старое состояние не нужно — `existsById`, не `findById`

```java
@PutMapping("/{id}")
public ResponseEntity<NoteResponse> replaceNoteByID(
        @PathVariable("id") UUID id, @RequestBody NoteRequest request)
// webflux — Mono<ResponseEntity<NoteResponse>>

// logic sketch (sync)
if (!repository.existsById(id)) {
    throw new NoteNotFoundException(id);
}
Note saved = repository.save(new Note(id, request.content()));
return new NoteResponse(saved.id(), saved.content());

// reactive: repository.existsById(id).flatMap(exists -> exists ? repository.save(...) : Mono.error(new NoteNotFoundException(id)))
```

`update` (слияние) — `PATCH /notes/{id}`, `updateNoteByID`:
- берёт `id` из пути; ищет сущность по `id`; не найдена — выходим (404 `ProblemDetail`); найдена — заменяем только переданные в запросе поля, `null`-поля игнорируем (поле не меняется)
- сегодня необязательных полей нет — описание на будущее
- стандарт: RFC 5789 (`PATCH`) правила слияния не задаёт — их определяет тип содержимого; «`null` игнорируем» при `application/json` — **названное отступление** от JSON Merge Patch (RFC 7396, `application/merge-patch+json`, где `null` = удалить поле); цена — клиент не может очистить необязательное поле; при появлении таких полей — перейти на RFC 7396 или различать «не передано»/«передан `null`» (`JsonNullable`, `Optional`-поля) — record этого не различает
- отдельная модель запроса (рабочее имя `NotePatchRequest`, не утверждено): все поля `@Nullable` (пакеты `@NullMarked`, иначе NullAway не пропустит); `@Nullable` в общем `NoteRequest` ослабил бы создание и замену
- нужно старое состояние — `findById`; пустой запрос `{}` — ничего не меняется, возвращается текущее состояние
- имя метода — `update…`, не `merge…`: `EntityManager.merge` в JPA значит другое (присоединить отсоединённую сущность — «сохранить или обновить»)

```java
@PatchMapping("/{id}")
public ResponseEntity<NoteResponse> updateNoteByID(
        @PathVariable("id") UUID id, @RequestBody NotePatchRequest request)
// webflux — Mono<ResponseEntity<NoteResponse>>

// logic sketch (sync)
Note current = repository.findById(id)
        .orElseThrow(() -> new NoteNotFoundException(id));
Note merged = new Note(id,
        request.content() != null ? request.content() : current.content());
return toResponse(repository.save(merged));

// reactive: findById(id).switchIfEmpty(Mono.error(...)).map(current -> merge(current, request)).flatMap(repository::save)
```

Общее для обеих:
- «не найдено» — исключение (рабочее имя `NoteNotFoundException`) → один `@RestControllerAdvice` → 404 `ProblemDetail` (RFC 9457); одно правило для `find`/`replace`/`update`/`delete`, контроллер не ветвится (не `Optional.empty()`)
- валидация — Bean Validation на границе: `@Valid` на аргументе + ограничения на полях модели; Spring на обоих стеках отдаёт 400 (`MethodArgumentNotValidException` / `WebExchangeBindException`), при включённых `problemdetails` — `ProblemDetail`, своего кода не нужно (AutoConfiguration); нужен `spring-boot-validation` в `controller-*` (CLAUDE.md п.7, пока нигде не применён). Без неё: `@NullMarked` при выполнении ничего не гарантирует (JSpecify/NullAway — только компиляция), Jackson положит `null`; на SQL — 500 от `NOT NULL`, на Mongo — тихая запись `null`, ветки дерева разойдутся
- известные расхождения по дереву, приняты по правилу «Принципы — прагматично» (CLAUDE.md → «Правила»): (1) параллельный `DELETE` между проверкой и `save` — Spring Data JDBC/R2DBC с непустым `id` делают `UPDATE`, 0 строк → исключение (по памяти `IncorrectUpdateSemanticsDataAccessException` для JDBC, не проверено ни для JDBC, ни для R2DBC), Mongo `save` — upsert, тихо создаёт документ заново; (2) у `update` — потерянное обновление: чтение-изменение-запись, два параллельных `PATCH` перезаписывают друг друга (сегодня поле одно — теоретически). Закрывает оба `@Version` в модели хранения + условный запрос `If-Match`/ETag (RFC 9110); поддержка `@Version` во всех 4 технологиях Spring Data — по памяти, не проверено; вернуться при втором поле или реальной конкурентной нагрузке

## Отступления семантики `replace`/`update` от принципов

Консультация ассистента 2026-09-29 по вопросу пользователя «где мой подход отходит от принципов» (CLAUDE.md → «Правила» → «Принципы — прагматично»), от главного к второстепенному; решений не принято:
- (1) логика снаружи модели — OOP, DDD, Tell Don't Ask: «найти → поменять поля → сохранить» — Transaction Script (Фаулер), `request.content() != null ? … : current.content()` решает за объект; для CRUD уместно (Фаулер — Transaction Script для простой логики, Эванс — DDD для сложных доменов), но перенос правила в модель почти бесплатен и снимает три отступления сразу: DRY (правило слияния иначе повторится в sync/reactive-сценарии и в `note`/`user`, копий больше с каждым полем; в модели — одно на обе ветки, первый код узла «общее» дерева), OOP/DDD, Clean (метод принимает значения полей, не `NotePatchRequest` — иначе внутренний круг зависит от внешнего); эскиз — `public Note withContent(@Nullable String newContent) { return new Note(id, newContent != null ? newContent : content); }` в `record Note`; применимо после решения п.1 (сегодня `Note` в 4 `data-*` с `@Id` — метод оказался бы в 4 копиях) — **главный практический вывод**
- (2) CRUD-глаголы вместо языка предметной области (DDD) — «изменить текст заметки», «переименовать пользователя» вместо `replace`/`update`; для CRUD — оправданное упрощение по KISS, только назвать
- (3) `null` в двух смыслах — «очистить» в `replace`, «не трогать» в `update`; названное отступление от RFC 7396, отдельные модели запроса удерживают путаницу в рамках
- (4) проверка-и-действие без атомарности (корректность; DDD — агрегат как граница согласованности) — `existsById`/`findById` → `save`; принято, см. «Семантика `replace` и `update`» → «известные расхождения по дереву»
- (5) Clean Architecture, уже названные — модель в `data-*` с аннотацией Spring Data (п.1), `NoteRequest` одновременно модель сценария и JSON (вариант (Б))
- (6) SOLID — риск, не отступление: логика в контроллере нарушила бы SRP и Clean; в interactor или модели — нет
- соответствует: RFC 9110/5789 (семантика `PUT`/`PATCH`), RFC 9457 + DRY (одно правило 404 `ProblemDetail`), ISP (отдельная модель слияния, общая — создания и замены), неизменяемые records, Bean Validation на границе (Spring по умолчанию), `existsById` в `replace` (KISS)

## Все варианты объявления по осям

Итог — один выбор на каждой оси. «Ломает зеркальность» — есть только на одном стеке; «меняет постановку» — пересматривает намеченные решения.

Способ объявления:
- аннотации на методе класса — принято
- интерфейс с `@PostMapping`, контроллер реализует (Spring наследует маршрутные аннотации от интерфейса) — два интерфейса (`contract`/`contract-reactive`) из-за разных типов возврата, прежняя схема `NoteControllerInterface`
- интерфейс `@PostExchange` (HTTP interface) — один контракт для контроллера и клиента; серверная поддержка в Spring Framework 7 не проверена, интерфейсов тоже два
- функциональные эндпоинты `RouterFunction` + `HandlerFunction` (`ServerRequest` → `ServerResponse`) — есть в MVC и WebFlux, зеркальность сохраняется

Маршрут:
- `POST /notes` — принято, `id` от сервера
- `PUT /notes/{id}`, `id` от клиента — идемпотентное создание, по RFC допустимо; меняет постановку (рекомендация п.4 — «`PUT` не создаёт»), снимает вопрос генерации `id` (п.3)
- `POST /notes` с `id` в теле — `id` от клиента, но без идемпотентности `PUT`

Имя метода: `createNote` — принято; `create`; `post`/`postNote` (именование по HTTP-методу, обсуждение 2026-09-26 — `docs/user-note-access-model.md`)

Вход:
- без параметров — нынешняя заглушка
- `@RequestBody NoteRequest` — принято
- `@RequestBody Note` — модель с `id` (клиент подставит свой), контроллер `Note` не видит (п.1)
- `@RequestBody String` + `consumes = TEXT_PLAIN_VALUE` — без JSON, второе поле меняет контракт
- `@RequestBody JsonNode`/`Map<String, Object>` — нетипизированно, от этого ушли (`Object` в портах)
- `@RequestParam String content` — форма `application/x-www-form-urlencoded`
- `@ModelAttribute NoteRequest` — привязка полей формы
- `@RequestBody Mono<NoteRequest>` — только WebFlux, ломает зеркальность
- добавки: `@Valid` (п.7), `@RequestHeader("Idempotency-Key")`, `consumes = APPLICATION_JSON_VALUE`

Возврат, sync:
- `ResponseEntity<NoteResponse>` — принято (201 + тело + `Location`)
- `ResponseEntity<Void>` — нынешняя заглушка; противоречит SHOULD о теле
- `ResponseEntity<Note>` — сама модель (п.1)
- `ResponseEntity<UUID>` — в теле только `id`
- `NoteResponse` + `@ResponseStatus(CREATED)` — без `Location` (или через `HttpServletResponse` — ломает зеркальность); противоречит SHOULD о `Location`
- `void` + `@ResponseStatus(CREATED)` — ни тела, ни `Location`
- `ResponseEntity<?>`/`Object` — нетипизированно, отклонено

Возврат, reactive: `Mono<ResponseEntity<T>>` — зеркальная пара, принято; `Mono<T>` + `@ResponseStatus`; `Mono<Void>`; `ResponseEntity<Mono<T>>` — WebFlux допускает, но статус известен до результата, для 201 после сохранения не годится

Статус: 201 — принято; 200 — теряется смысл «создано»; 202 — асинхронное создание, меняет постановку

## `Location`

- в большинстве туториалов его нет (`return repository.save(...)` с 200) — упрощение для обучения; так начинается и официальный туториал spring.io «Building REST services with Spring», в финальной версии которого уже `ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel)` с пояснением «This type of response typically includes a **Location** response header»
- зачем: сервер владеет схемой URL (клиент не зашивает `/notes/{id}`, смена маршрута не ломает клиентов); E2E-тест («создал → прочитал → изменил → удалил») идёт по адресу из `Location` и заодно проверяет его; стандартные клиенты читают его (`RestTemplate.postForLocation`)
- `ResponseEntity.created(URI)` — метод Spring ровно для этого: 201 + `Location`

## `UriComponentsBuilder`

- параметр метода (MVC и WebFlux) — заранее заполнен схемой/хостом/портом/context path текущего запроса, учитывает `X-Forwarded-*` при `server.forward-headers-strategy=framework`; нужен только для абсолютного внешнего адреса
- сейчас не нужен: `server.servlet.context-path` не задан, прокси нет, `Location` относительный — схему/хост/порт клиент берёт из адреса своего запроса
- статический `UriComponentsBuilder.fromPath("/notes/{id}").buildAndExpand(id).toUri()` — не параметр, а замена ручной склейки строки; сигнатуру не меняет
- MVC-only альтернативы, ломают зеркальность: `ServletUriComponentsBuilder.fromCurrentRequest()`, `MvcUriComponentsBuilder.fromMethodCall(...)` (аналог в WebFlux не проверен)

## Gateway и пути

Решение пользователя 2026-09-29: gateway (в будущем обязателен) маршрутизирует без переписывания путей — внешний путь равен пути сервиса.

- почему: относительный `Location` сервиса верен без настроек и без нестандартных заголовков; сломаться может только путь, не схема/хост/порт
- с префиксом (клиент на `/api/notes`, gateway срезает `/api`) — `/notes/{id}` разрешится в `https://gateway/notes/{id}` без `/api`, ссылка неверна
- запасной путь при префиксе (добавляется без смены контракта с клиентом): `X-Forwarded-Prefix` от gateway (фильтр включается `spring.cloud.gateway.server.webflux.trusted-proxies`, `spring.cloud.gateway.server.webflux.x-forwarded.prefix-enabled` по умолчанию `true`) + `server.forward-headers-strategy=framework` (`ForwardedHeaderFilter` MVC / `ForwardedHeaderTransformer` WebFlux) + параметр `UriComponentsBuilder`; оговорка — `X-Forwarded-*` нестандартные (документация Boot: «non-standard headers»), в стандартном `Forwarded` (RFC 7239) параметра префикса нет
- `RewriteLocationResponseHeader` (Gateway) — по документации меняет `host:port` и срезает версию (`/v2`), добавление произвольного префикса не описано — для `/api`, вероятно, не подходит (не проверено)
- относительная ссылка без ведущего `/` (`notes/{id}`) — по RFC разрешится в `/api/notes/{id}`, но хрупко: при запросе на `/api/notes/` получится `/api/notes/notes/{id}` — не рекомендуется
- не решено: общий префикс `/api` (пример маршрутов в `docs/microservices-reference.md`) — без переписывания он должен быть и в путях сервисов, сейчас контроллеры на `/notes`/`/users`
- источники: docs.spring.io — Spring Cloud Gateway «httpheadersfilters», «rewritelocationresponseheader-factory»; Spring Boot «Running Behind a Front-end Proxy Server»

## Аргументы метода контроллера

Источник — Spring Framework reference, «Method Arguments» для MVC (`web/webmvc/mvc-controller/ann-methods/arguments.html`) и WebFlux (`web/webflux/controller/ann-methods/arguments.html`), 2026-09-29.

Нужны или могут понадобиться `createNote` (оба стека):
- `@RequestBody NoteRequest` — принято
- `@Valid`/`@Validated` на `@RequestBody` — после п.7 (`spring-boot-validation`), ошибка → 400 + `ProblemDetail`
- `Principal` / `@AuthenticationPrincipal Jwt` — после аутентификации, если создатель становится владельцем (модель доступа `user-note`); `@AuthenticationPrincipal` (Spring Security) есть в таблице MVC, в таблице WebFlux нет — работа на WebFlux не проверена
- `@RequestHeader("Idempotency-Key")` — защита от дублей при повторе `POST`, для CRUD необязательно; черновик IETF, не RFC (по памяти, не проверено)
- `UriComponentsBuilder` — только при префиксе в gateway («Gateway и пути»)

Годятся, но не для этого метода:
- `@PathVariable` — при создании через `PUT /notes/{id}`
- `@RequestParam` — параметры запроса/формы, для JSON API тело лучше
- `@RequestHeader` для прочих заголовков — например `If-None-Match: *` для условного создания через `PUT`
- `HttpEntity<NoteRequest>` — тело + заголовки, менее явно, чем `@RequestBody` + `@RequestHeader`
- `Locale`, `TimeZone`/`ZoneId`, `HttpMethod`
- `@CookieValue`, `@MatrixVariable`, `@RequestAttribute` — повода в API нет

Не подходят или ломают зеркальность:
- только MVC: `ServletRequest`/`ServletResponse`, `HttpSession`, `WebRequest`/`NativeWebRequest`, `InputStream`/`Reader`, `OutputStream`/`Writer`, `RedirectAttributes`, `PushBuilder` (deprecated в Servlet 6.1)
- только WebFlux: `ServerWebExchange`, `ServerHttpRequest`/`ServerHttpResponse`, `WebSession`; `@RequestBody Mono<NoteRequest>`
- для HTML-шаблонов, не REST: `Model`/`ModelMap`/`Map`, `@ModelAttribute`, `Errors`/`BindingResult` (в REST — `@RestControllerAdvice`), `SessionStatus`, `@SessionAttribute`
- `@RequestPart` — только `multipart/form-data` (файлы)
- аргумент без аннотации — Spring сам решает `@RequestParam`/`@ModelAttribute`, неявно (против «явной композиции» — CLAUDE.md → «Правила»)
