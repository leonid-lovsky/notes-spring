# HTTP API `notes-spring` — стандарты, семантика, объявление методов контроллера

> **Назначение:** справочник HTTP API трёх сервисов (`note`/`user`/`user-note`): нормативные стандарты с цитатами, принятая семантика и сигнатуры, названные отступления, известные расхождения по дереву вариантов, варианты объявления метода, `Location`, gateway, аргументы метода MVC/WebFlux
> **Когда читать:** при объявлении/изменении любого метода `controller-webmvc`/`controller-webflux`, при вопросах о кодах ответа, заголовках, gateway и путях
> **Статус:** актуализирован 2026-09-29 (оптимизация документации) — семантика «максимально простой вариант» решена пользователем; тела методов вызывают порты, реализации в `data-*` — заглушки; открытые вопросы — CLAUDE.md → «Задачи» → «Открытые вопросы реализации»
> **Разделы:** «Стандарты», «Принятая семантика и сигнатуры», «Названные отступления от стандартов», «Известные расхождения по дереву», «Отступления от принципов», «Варианты объявления метода по осям», «`Location` и `UriComponentsBuilder`», «Gateway и пути», «Аргументы метода контроллера»
> **Связано:** CLAUDE.md → «Правила» → «Придерживаемся стандартов…», «Принципы — прагматично…»; CLAUDE.md → «Задачи» → «Принятые решения 2026-09-29»; `docs/spring-boot-testing-reference.md` (тесты контроллеров); `docs/microservices-reference.md` (Gateway)

## Стандарты

Цитаты сверены 2026-09-29 по текстам `rfc-editor.org/rfc/rfc9110.txt`, `rfc5789.txt`, `rfc7396.txt`.

- REST — архитектурный стиль (диссертация Филдинга), не стандарт; строгий REST требует гипермедиа (HATEOAS, `spring-boot-starter-hateoas` есть в Initializr), решение по HATEOAS не принято
- нормативная часть — RFC 9110 (семантика HTTP), RFC 9457 (Problem Details, в Spring — `ProblemDetail`), RFC 5789 (`PATCH`), RFC 7396 (JSON Merge Patch); «коллекция во множественном числе» — отраслевое соглашение (Microsoft, Google AIP, Zalando), не RFC
- RFC 2119: MUST не нарушается; SHOULD — «следует, если нет веской причины иначе», отступление только с названной причиной
- §9.3.3 POST: «If one or more resources has been created on the origin server as a result of successfully processing a POST request, the origin server SHOULD send a 201 (Created) response containing a Location header field that provides an identifier for the primary resource created (Section 10.2.2) and a representation that describes the status of the request while referring to the new resource(s).»
- §15.3.2 201: «The primary resource created by the request is identified by either a Location header field in the response or, if no Location header field is received, by the target URI.»
- §10.2.2 Location: «The field value consists of a single URI-reference» — относительный адрес допустим, клиент разрешает его относительно адреса своего запроса
- §9.3.4 PUT: создал — «MUST inform the user agent by sending a 201 (Created) response»; изменил существующий — «MUST send either a 200 (OK) or a 204 (No Content)»; создавать `PUT` не обязан. Различие методов: «The target resource in a POST request is intended to handle the enclosed representation according to the resource's own semantics, whereas the enclosed representation in a PUT request is defined as replacing the state of the target resource» — для `id`, выбранного клиентом, стандарт описывает `PUT`
- §9.3.5 DELETE: SHOULD 202 (ещё не выполнено), 204 (выполнено, без содержимого) или 200 (с представлением результата)
- §15.5.5 404: «the origin server did not find a current representation for the target resource»; §15.5.10 409 — конфликт с текущим состоянием ресурса
- §8.8.3 ETag: «An origin server SHOULD send an ETag for any selected representation for which detection of changes can be reasonably and consistently determined»; условные запросы — `If-Match`, `If-None-Match: *` («только создать»), при нарушении условия — 412
- RFC 5789 §2: «The set of changes is represented in a format called a "patch document" identified by a media type»; «If the entire patch document cannot be successfully applied, then the server MUST NOT apply any of the changes»; клиенты форматов, зависящих от текущего состояния, «SHOULD use a conditional request»; ошибки (§2.2): некорректный документ — 400, неподдерживаемый формат — 415 с `Accept-Patch`, неприменимый — 422
- RFC 7396: «Null values in the merge patch are given special meaning to indicate the removal of existing values in the target»; формат подходит для JSON, который «do not make use of explicit null values»
- Spring (исходники 7.0.9): `Accept-Patch` выставляют сами `HttpMediaTypeNotSupportedException`/`UnsupportedMediaTypeStatusException` для `PATCH` и встроенный обработчик `OPTIONS` на обоих стеках; константы `application/merge-patch+json` в `MediaType` нет; Jackson читает `application/*+json`; `ResponseEntity` с ETag на `GET` сам отвечает 304 на `If-None-Match` (`HttpEntityMethodProcessor`/`ResponseEntityResultHandler`)

## Принятая семантика и сигнатуры

Решение пользователя 2026-09-29 — «максимально простой вариант»; `{E}` — `Note`/`User`/`UserNoteEntity`, путь — `/notes`|`/users`|`/user-notes`; `id` задаёт клиент, автогенерации нет.

- `GET /{id}` — `find{E}ById(UUID id)` — есть → 200 + `{E}Response`; нет → 404
- `POST /{id}` — `create{E}ById(UUID id, {E}Request request)` — создал → сейчас 200 (201 отложен); уже есть → 409
- `PUT /{id}` — `replace{E}ById(UUID id, {E}Request request)` — только полная замена: новое состояние = присланное, `null` (или отсутствующее поле — для record одно и то же) очищает поле, обязательное поле с `null` → 400 валидации; нет → 404 (upsert рассмотрен и отменён в тот же день)
- `PATCH /{id}` — `update{E}ById(UUID id, {E}Request request)` — меняются только переданные поля, `null` не трогает; нет → 404
- `DELETE /{id}` — `delete{E}ById(UUID id)` — возвращает удалённую сущность, 200 + `{E}Response` (RFC 9110 §9.3.5 допускает); нет → 404
- ошибки — исключения сценариев → один `@RestControllerAdvice` на стек → `ProblemDetail` (RFC 9457); ещё не реализовано
- вид контроллера (задан пользователем): порядок `GET`, `POST`, `PUT`, `PATCH`, `DELETE`; одна строка на метод — `return ResponseEntity.ok(порт.метод(...))` (webflux — `порт.метод(...).map(ResponseEntity::ok)`); без `if` и приватных методов; объявления в одну строку; импорты явные

```java
@GetMapping("/{id}")
public ResponseEntity<NoteResponse> findNoteById(@PathVariable("id") UUID id) {
    return ResponseEntity.ok(findNoteById.findNoteById(id));
}
```

## Названные отступления от стандартов

Отложены решением пользователя; при возврате — по правилу «Стандарт важнее предложения пользователя».

- `POST` отвечает 200 вместо 201 + `Location` (SHOULD §9.3.3) — «о семантике потом»
- `PATCH` принимает `{E}Request` в `application/json`, «`null` — не трогать» — это свой формат изменений, не RFC 7396 (`application/merge-patch+json`, где `null` удаляет значение); сегодня необязательных полей нет, форматы различаются только явным `null` в обязательном поле; строго по стандарту — тело как дерево JSON (`JsonNode` или Jakarta JSON-P `JsonMergePatch`), `update` = наложить изменения на текущее представление → провалидировать и сохранить как `replace`, недопустимый результат → 422
- нет ETag и условных запросов (SHOULD §8.8.3; RFC 5789 рекомендует условный `PATCH`) — источник ETag — `@Version` сущности
- `POST /{id}` на адрес ещё не существующего ресурса — стандарт не запрещает, но для клиентского `id` описывает `PUT`

## Известные расхождения по дереву

Проверено 2026-09-29 по исходникам Spring Data JDBC/R2DBC 4.1.1 и MongoDB 5.1.1.

- признак новой записи (`PersistentEntityIsNewStrategy.isNew`) — свойство `@Version`, без него `id`; `null` — новая, непустой не-примитив — существующая: с `id` от клиента и без `@Version` `save` всегда считает запись существующей
- JDBC (`JdbcAggregateChangeExecutionContext.updateWithoutVersion`) — результат `UPDATE` игнорируется, 0 строк молча; R2DBC (`R2dbcEntityTemplate.doUpdate`) — ошибка при 0 строк только при наличии версии; Mongo (`SimpleMongoRepository.save`) — существующую запись сохраняет upsert-ом
- следствие без `@Version`: создание через `save` на SQL-ветках ничего не создаёт (клиент получит успех); при гонке «проверка → запись» SQL молча теряет запись, Mongo заново создаёт удалённый документ — для `PUT` это 200 вместо MUST 201
- с `@Version` все 4 технологии при 0 затронутых записей дают `OptimisticLockingFailureException` (JDBC `updateWithVersion`, R2DBC `OptimisticLockingUtils.updateFailed`, Mongo `doSaveVersioned` без upsert, sync и reactive) → 409; `version == null` → `INSERT`; параллельный `DELETE` тоже даст 409 (не 404) — отступление по смыслу «конфликт с текущим состоянием»
- альтернативы: `Persistable<ID>` с `isNew()` — только создание, гонку не закрывает; явные `insert`/`update` шаблонов — `update` без версии на JDBC/R2DBC тоже молчит, нужны свои запросы в 4 копиях — отклонено
- без валидации `null` в обязательном поле: SQL — 500 от `NOT NULL`, Mongo — тихая запись `null`

## Отступления от принципов

Консультация 2026-09-29, решений не принято.

- главное — логика снаружи модели (OOP, DDD, Tell Don't Ask): «найти → поменять поля → сохранить» — Transaction Script (Фаулер), для CRUD уместно; перенос правила слияния в модель (`public Note withContent(@Nullable String newContent) { return new Note(id, newContent != null ? newContent : content); }`) снимает отступления OOP/DDD/DRY (правило одно на обе ветки); применимо после решения, где живёт сущность (сегодня — 4 копии в `data-*`)
- CRUD-глаголы вместо языка предметной области (DDD) — оправданное упрощение по KISS
- `null` в двух смыслах (`replace` — очистить, `update` — не трогать) — отдельные модели удерживают путаницу; названо в «Названные отступления»
- проверка-и-действие без атомарности — «Известные расхождения по дереву»
- соответствует: семантика `PUT`/`PATCH`, одно правило 404 `ProblemDetail`, неизменяемые records, Bean Validation на границе, тонкий контроллер (SRP)

## Варианты объявления метода по осям

Разбор 2026-09-29 на примере создания; «ломает зеркальность» — есть только на одном стеке.

- способ объявления — аннотации на методе класса (принято); интерфейс с `@PostMapping`, который реализует контроллер (два интерфейса — sync/reactive); HTTP interface `@PostExchange` (серверная поддержка в Spring Framework 7 не проверена); функциональные эндпоинты `RouterFunction`/`HandlerFunction` (есть на обоих стеках)
- маршрут создания — `POST /{id}` (принято); `POST /` с генерацией `id` на сервере (отменено — `id` задаёт клиент); `PUT /{id}` как upsert (канонично для клиентского `id`, отменено ради простой семантики)
- имя — по сценарию (`create{E}ById`, принято); по HTTP-методу (`post{E}`) — пробовалось и заменено пользователем
- вход — `@RequestBody {E}Request` (принято); `JsonNode`/`Map` — нетипизированно; `String` + `text/plain`; `@RequestParam`/`@ModelAttribute` — формы; `@RequestBody Mono<…>` — только WebFlux; добавки — `@Valid`, `@RequestHeader("Idempotency-Key")`
- возврат — `ResponseEntity<{E}Response>` / `Mono<ResponseEntity<{E}Response>>` (принято); `ResponseEntity<Void>` (против SHOULD о теле); `@ResponseStatus(CREATED)` без `Location`; `ResponseEntity<?>`/`Object` — отклонено; `ResponseEntity<Mono<T>>` — статус известен до результата

## `Location` и `UriComponentsBuilder`

- `Location` сейчас не отдаётся (`POST` → 200, отложено); при возврате к 201 — относительный `URI.create("/notes/" + id)` или шаблоном `UriComponentsBuilder.fromPath("/notes/{id}").buildAndExpand(id).toUri()` (без ручной склейки); `ResponseEntity.created(URI)` — 201 + `Location`
- в большинстве туториалов `Location` нет (`return repository.save(...)` с 200) — упрощение; финальная версия туториала spring.io «Building REST services with Spring» уже возвращает `ResponseEntity.created(...)`: «This type of response typically includes a **Location** response header»
- зачем: сервер владеет схемой URL; E2E-тест идёт по адресу из `Location` и заодно проверяет его
- `UriComponentsBuilder` как параметр метода (MVC и WebFlux) — заполнен схемой/хостом/портом/context path, учитывает `X-Forwarded-*` при `server.forward-headers-strategy=framework`; нужен только для абсолютного внешнего адреса — сейчас не нужен
- MVC-only (ломают зеркальность): `ServletUriComponentsBuilder.fromCurrentRequest()`, `MvcUriComponentsBuilder.fromMethodCall(...)`

## Gateway и пути

Решение пользователя 2026-09-29: gateway (в будущем обязателен) маршрутизирует без переписывания путей — внешний путь равен пути сервиса.

- почему: относительный `Location` верен без настроек и нестандартных заголовков; сломаться может только путь (схему/хост/порт клиент берёт из адреса gateway)
- с префиксом (клиент на `/api/notes`, gateway срезает `/api`) `/notes/{id}` разрешится без `/api` — неверно
- запасной путь при префиксе (без смены контракта): `X-Forwarded-Prefix` от gateway (фильтр включается `spring.cloud.gateway.server.webflux.trusted-proxies`, `…x-forwarded.prefix-enabled` по умолчанию `true`) + `server.forward-headers-strategy=framework` (`ForwardedHeaderFilter` / `ForwardedHeaderTransformer`) + параметр `UriComponentsBuilder`; оговорка — `X-Forwarded-*` нестандартные («non-standard headers» — документация Boot), в `Forwarded` (RFC 7239) параметра префикса нет
- `RewriteLocationResponseHeader` меняет `host:port` и срезает версию, добавление префикса не описано (не проверено)
- относительная ссылка без ведущего `/` (`notes/{id}`) — хрупко при запросе со слешем в конце, не рекомендуется
- не решено: общий префикс `/api` (пример в `docs/microservices-reference.md`) — без переписывания он должен быть и в путях сервисов
- источники: docs.spring.io — Spring Cloud Gateway «httpheadersfilters», «rewritelocationresponseheader-factory»; Spring Boot «Running Behind a Front-end Proxy Server»

## Аргументы метода контроллера

Источник — Spring Framework reference «Method Arguments» для MVC (`web/webmvc/mvc-controller/ann-methods/arguments.html`) и WebFlux (`web/webflux/controller/ann-methods/arguments.html`), 2026-09-29.

- используются: `@PathVariable("id") UUID id`, `@RequestBody {E}Request`
- могут понадобиться: `@Valid` на `@RequestBody` (валидация, 400 + `ProblemDetail`); `Principal` / `@AuthenticationPrincipal Jwt` (после аутентификации; `@AuthenticationPrincipal` есть в таблице аргументов MVC, в таблице WebFlux нет — работа на WebFlux не проверена); `@RequestHeader` — `If-Match`/`If-None-Match` (условные запросы), `Idempotency-Key` (черновик IETF, по памяти)
- годятся, но повода нет: `@RequestParam`, `HttpEntity<T>`, `Locale`, `TimeZone`/`ZoneId`, `HttpMethod`, `@CookieValue`, `@MatrixVariable`, `@RequestAttribute`
- ломают зеркальность: только MVC — `ServletRequest`/`ServletResponse`, `HttpSession`, `WebRequest`, потоки `InputStream`/`Reader`/`OutputStream`/`Writer`, `RedirectAttributes`, `PushBuilder`; только WebFlux — `ServerWebExchange`, `ServerHttpRequest`/`ServerHttpResponse`, `WebSession`, `@RequestBody Mono<T>`
- для HTML-шаблонов, не REST: `Model`/`ModelMap`, `@ModelAttribute`, `Errors`/`BindingResult` (в REST — `@RestControllerAdvice`), `SessionStatus`, `@SessionAttribute`; `@RequestPart` — только `multipart/form-data`
- аргумент без аннотации — неявный `@RequestParam`/`@ModelAttribute`, против «явной композиции»
