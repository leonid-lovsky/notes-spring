# Модель доступа `UserNote` — права на заметку (обсуждение 2026-09-25)

Статус: **консультация, решение не принято, в коде ничего не реализовано** — пользователь пишет код сам (CLAUDE.md → «Правила» → «Роль ассистента»). Файл фиксирует ход рассуждения и точку, на которой обсуждение остановилось 2026-09-25, чтобы не проходить его заново. Комментарии вынесены за рамки обсуждения (вернуться позже); свой комментарий правит/удаляет автор — право из авторства, не выдаваемое.

## Требования пользователя (в порядке появления в диалоге 2026-09-25)

- `UserNote` = `ID`, `UserID`, `NoteID` + доступ: can read, can edit, can delete (позже — comment, delete comments)
- проверка при обращении к документу — по праву (can read/edit/delete), не по роли
- возможность в будущем создавать роли, содержащие права; сейчас роли на сервере не нужны
- недопустимы противоречивые наборы — пример пользователя: `CAN EDIT = TRUE`, `CAN READ = FALSE` (инвариант)
- без флагов-колонок (`can_edit`/`can_delete`) — пользователь считает это нарушением Open/Closed: новое право меняет модель
- стремление к отсутствию `if`/`switch` (пользователь считает их запахом кода); позже смягчено — допустим `if`, если так проще и понятнее
- код понятен с первого взгляда даже новичку
- удобный компромисс сразу для UI / JWT / Spring Security / Spring Boot / Spring Data / SQL / NoSQL / Gradle-модулей (каждый модуль — слой)
- UI: таблица прав с флажками + выбор роли, выбор роли сразу проставляет её права
- enum плоские — только список имён, без конструкторов и полей

## Ключевые выводы обсуждения 2026-09-25 (не повторять разбор заново)

- **Права — код, роли — данные (или шаблоны)**: код проверяет права по именам, поэтому список прав — enum в коде; новое право без проверяющего кода бессмысленно. Роль — именованный набор прав; код никогда не спрашивает «является ли он редактором», только «может ли он править»
- **Три смысла роли — решает вопрос «что происходит с уже выданным доступом при правке роли»**: роль по ссылке (`roleId`, правка роли сразу меняет доступ всем — GitHub custom roles, AWS managed policies); роль-шаблон (права копируются при выдаче, правка касается только будущих — SonarQube permission templates); роль только в UI (сервер ролей не знает — NTFS «базовые разрешения»). Выбран третий, путь к первым двум открыт
- **Вся сложность хранения прав — из одной возможности: индивидуальные наборы для отдельного пользователя**. Продукты совместной работы с документами (Google Drive, Dropbox) от неё отказываются и хранят одну роль; ОС (Unix, NTFS, macOS) её дают и платят сложностью. Пользователь выбрал индивидуальные наборы (таблица флажков с ручной правкой)
- **Критерии сравнения представлений прав в хранилище**: естественный ли тип в каждом хранилище (или конвертер); можно ли искать по праву индексом («заметки, которые я могу править»); держится ли инвариант в одной записи (атомарно без транзакции); сколько стоит новое право. По ним: флаги лучше всех переносятся на SQL/NoSQL, но новое право = миграция (отклонено по Open/Closed); строка — одинакова везде, новое право бесплатно, поиска по праву нет (выбрано); запись на право (Zanzibar) — расширяется лучше всех, но нужны транзакции
- **Open/Closed здесь — на уровне модулей, не enum**: сам enum при новом праве правится неизбежно, цель — чтобы правка была в одном месте, а домен, сервис, API, хранилище, UI не менялись
- **Инвариант принадлежит понятию «набор прав», не сущности `UserNote`** — отсюда объект-значение `Permissions`; `UserNote` может стать хоть интерфейсом, инвариант не теряется
- **Объединение двух допустимых наборов допустимо** (оба содержат READ и замкнуты по зависимостям) — будущий путь «права роли ∪ явные права» не требует новой проверки
- **Дисциплина уже сейчас ради будущих ролей**: права пользователя читаются только через доменный метод (`has(p)`), никто не читает поле хранилища как «права пользователя» — тогда подключение ролей меняет только реализацию проверки; путь «вместо явных прав только роль» ломает данные — избегать
- **JWT несёт только личность** (`sub`, глобальные scope); права на конкретные ресурсы — на сервере по ACL (так Google и GitHub): прав много, выданный JWT не отозвать
- **404 вместо 403 при отсутствии READ** — существование заметки не раскрывается (как закрытые репозитории GitHub); 403 — только если READ есть, а нужного права нет
- **Правило в прикладном слое, Spring Security только на входе** — одна проверка на webmvc и webflux, домен без фреймворка, тестируется общим тестом на порт
- **`if`/`switch`**: запах — повторяющийся `switch` по одному признаку, `instanceof`-цепочки, аргументы-флаги (Фаулер); охранный `if` — не запах; одну развилку «факт → поведение» убрать нельзя, только спрятать — принцип «ветвиться один раз на границе, дальше полиморфизм»; для кода, понятного новичку, охранный `if` понятнее stream с `throw` в лямбде; `switch` по `sealed`-типу с проверкой полноты компилятором — другая школа (data-oriented programming), не запах — запрещать ли его, не решено
- **Откладывание решения**: не выбирать форму заранее, а сделать смену дешёвой — границы `Permissions` (представление), тонкий порт (хранение), при необходимости `AccessPolicy` (модель доступа) + контрактные тесты; Zanzibar/Event Sourcing/внешний PDP подключаются потом как новая реализация (Парнас 1972, «последний ответственный момент»)
- **Дрейф рекомендаций ассистента в ходе обсуждения** (чтобы не принять старую рекомендацию за актуальную): флаги с «запись = READ» → множество строкой → флаги (по критерию переносимости) → роль-enum по модели Google (по аналогам) → множество + роли только в UI (по требованиям пользователя: без флагов, таблица флажков, плоские enum). Каждый поворот — от нового требования или критерия, актуальна только «Точка остановки»

## Точка остановки — последний предложенный вариант

- `Permission` — плоский enum: `READ, COMMENT, EDIT, DELETE, DELETE_COMMENTS`
- `Role` — плоский enum: `VIEWER, COMMENTER, EDITOR, OWNER`; роль — только готовый набор прав для UI (шаблон), в `UserNote` не хранится, проверка доступа о ролях не знает
- `PermissionRequirements` — `Map<Permission, Set<Permission>>` «право → что ему нужно» (`COMMENT→READ`, `EDIT→READ`, `DELETE→EDIT`, `DELETE_COMMENTS→COMMENT`), `getOrDefault(p, Set.of())`
- `RolePermissions` — `Map<Role, Set<Permission>>` «роль → её права»
- `Permissions` — record-объект-значение над `Set<Permission>`; в компактном конструкторе два охранных `if`: READ обязателен; для каждого права `containsAll(PermissionRequirements.of(p))`; метод `has(p)`. Недопустимый набор не создаётся (always-valid, «parse, don't validate»)
- `UserNote(UUID userId, UUID noteId, Permissions permissions)` — сам ничего не проверяет; без `id` (уточнено 2026-09-25 в «Итоговая модель целиком»): личность — пара, суррогатный `id` только в сущностях хранилища
- проверка в прикладном сервисе: `findByUserIdAndNoteId(...).orElseThrow(NoteNotFoundException)` → 404 (существование заметки не раскрывается); `!has(p)` → `PermissionDeniedException` → 403. Имя `AccessDeniedException` не брать — совпадает с `org.springframework.security.access.AccessDeniedException`
- хранение: права строкой `"COMMENT,EDIT,READ"` (отсортировано) во всех 4 `data-*`, включая Mongo — одинаковые сущности и мапперы (зеркальность сиблингов); перевод обычной Java в маппере (`Permissions.fromText`/`toText`), без регистрации конвертеров Spring; уникальная пара (`user_id`, `note_id`) — индекс в SQL и Mongo; признак новой записи — `@Version Long version` в сущности (`null` = новая) одинаково у JDBC/R2DBC/Mongo, вместо `Persistable.isNew()` (иначе `save` с заполненным id делает UPDATE); заодно оптимистическая блокировка → 409; FK на роль нет (ролей в хранилище нет)
- API: `GET /access-model` (`permissions` с `requires` + `roles` с наборами — строки таблицы и выпадающий список, новое право/роль появляется в UI без правки клиента); `GET`/`PUT`/`DELETE /notes/{noteId}/users/{userId}` с телом `{"permissions": [...]}`; недопустимый набор → 400
- UI: выбор роли → флажки = набор роли; ручная правка → отображается «Особая» (как «Особые разрешения» NTFS), если набор не совпал ни с одной ролью; отметка права может автоматически отмечать его `requires` — удобство, гарантию даёт сервер
- JWT: в токене только `sub` = userId (+ возможно глобальные scope); права на заметки в токен не кладутся — их много, выданный JWT не отозвать
- Spring Security: только OAuth2 resource server (проверка подписи) в `controller-*`, `UUID.fromString(jwt.getSubject())`; правило доступа — в прикладном слое, не в `@PreAuthorize`/`PermissionEvaluator` (SpEL-строки, неблокирующая работа в reactive не проверялась)
- Gradle: `Permission`/`Role`/таблицы/`Permissions`/`UserNote`/исключения/порт/проверка — `contract` (без Spring); reactive-порт — `contract-reactive`, доменные типы из `contract`; сущность+репозиторий+маппер+адаптер — `data-*`; JWT→userId, DTO, `@ExceptionHandler`→`ProblemDetail` — `controller-*` (нужен новый атомарный convention-плагин OAuth2 resource server); `schema.sql` вендора и `issuer-uri` — `application-*`
- цена изменений: новое право — константа + строка в `PermissionRequirements`; новая роль — константа + строка в `RolePermissions`; изменение набора роли касается только будущих выдач; роли, создаваемые пользователями, — `Role` из enum в таблицу того же формата в `/access-model`, `UserNote` не меняется; смена формы хранения — только `data-*`
- что теряется с плоскими enum: компилятор больше не запрещает цикл зависимостей (раньше `A(B), B(A)` — `illegal forward reference`) и не ловит роль, забытую в `RolePermissions` (`get` вернёт `null`) — закрывается unit-тестами на `contract`: каждая роль есть в таблице и её набор проходит `new Permissions(...)`; обход зависимостей не возвращается к исходному праву

## Итоговая модель целиком — код по модулям (2026-09-25, консультация, в репозиторий не записан)

Уточнения относительно «Точки остановки» (внесены в неё же): (1) доменный `UserNote` без `id` — личность = пара (`userId`, `noteId`), суррогатный `id` нужен только хранилищу (в Cassandra/DynamoDB был бы лишним); (2) вместо `Persistable.isNew()` — `@Version Long version`: Spring Data JDBC, R2DBC и Mongo одинаково считают сущность новой при `version == null`, заодно оптимистическая блокировка. Константы в таблицах — через `import static ...Permission.*`/`Role.*`.

### `contract` — модель (чистая Java)

```java
public enum Permission {
    READ,
    COMMENT,
    EDIT,
    DELETE,
    DELETE_COMMENTS
}

public enum Role {
    VIEWER,
    COMMENTER,
    EDITOR,
    OWNER
}

public final class PermissionRequirements {

    private static final Map<Permission, Set<Permission>> REQUIRES = Map.of(
            COMMENT, Set.of(READ),
            EDIT, Set.of(READ),
            DELETE, Set.of(EDIT),
            DELETE_COMMENTS, Set.of(COMMENT));

    private PermissionRequirements() {}

    public static Set<Permission> of(Permission permission) {
        return REQUIRES.getOrDefault(permission, Set.of());
    }
}

public final class RolePermissions {

    private static final Map<Role, Set<Permission>> PERMISSIONS = Map.of(
            VIEWER, Set.of(READ),
            COMMENTER, Set.of(READ, COMMENT),
            EDITOR, Set.of(READ, COMMENT, EDIT, DELETE_COMMENTS),
            OWNER, Set.of(READ, COMMENT, EDIT, DELETE, DELETE_COMMENTS));

    private RolePermissions() {}

    public static Set<Permission> of(Role role) {
        return PERMISSIONS.get(role);
    }
}

public record Permissions(Set<Permission> values) {

    public Permissions {
        values = Set.copyOf(values);
        if (!values.contains(READ)) {
            throw new InvalidPermissionsException("READ is required");
        }
        for (Permission permission : values) {
            if (!values.containsAll(PermissionRequirements.of(permission))) {
                throw new InvalidPermissionsException(
                        permission + " requires " + PermissionRequirements.of(permission));
            }
        }
    }

    public boolean has(Permission permission) {
        return values.contains(permission);
    }

    public List<Permission> sorted() {
        return values.stream().sorted().toList();
    }

    public static Permissions fromText(String text) {
        return new Permissions(Arrays.stream(text.split(","))
                .map(Permission::valueOf)
                .collect(Collectors.toSet()));
    }

    public String toText() {
        return sorted().stream()
                .map(Permission::name)
                .collect(Collectors.joining(","));
    }
}

public record UserNote(UUID userId, UUID noteId, Permissions permissions) {}

public interface UserNoteStorage {
    Optional<UserNote> find(UUID userId, UUID noteId);
    void save(UserNote userNote);           // создать или заменить права
    void delete(UUID userId, UUID noteId);
}
```

Исключения — три `RuntimeException` с сообщением: `InvalidPermissionsException` (недопустимый набор), `NoteNotFoundException` (нет доступа — снаружи «заметка не найдена»), `PermissionDeniedException` (доступ есть, нужного права нет).

### `contract-reactive` — порт

```java
public interface UserNoteReactiveStorage {
    Mono<UserNote> find(UUID userId, UUID noteId);
    Mono<Void> save(UserNote userNote);
    Mono<Void> delete(UUID userId, UUID noteId);
}
```

### Прикладной сервис (модуль — не решён)

```java
public class UserNoteService {

    private final UserNoteStorage storage;

    public UserNoteService(UserNoteStorage storage) {
        this.storage = storage;
    }

    public UserNote requireAccess(UUID userId, UUID noteId, Permission permission) {
        UserNote userNote = storage.find(userId, noteId)
                .orElseThrow(() -> new NoteNotFoundException(noteId));
        if (!userNote.permissions().has(permission)) {
            throw new PermissionDeniedException(permission);
        }
        return userNote;
    }

    public Permissions permissionsOf(UUID userId, UUID noteId) {
        return storage.find(userId, noteId)
                .map(UserNote::permissions)
                .orElseThrow(() -> new NoteNotFoundException(noteId));
    }

    public void grant(UUID userId, UUID noteId, Permissions permissions) {
        storage.save(new UserNote(userId, noteId, permissions));
    }

    public void revoke(UUID userId, UUID noteId) {
        storage.delete(userId, noteId);
    }
}
```

Reactive-двойник — та же форма: `switchIfEmpty(Mono.error(...))`, затем `flatMap` с `Mono.error(new PermissionDeniedException(...))` при отсутствии права. `grant`/`revoke` не проверяют право вызывающего — «Нерешённое».

### `data-jdbc` / `data-r2dbc` — сущность одинаковая

```java
@Table("user_note")
public record UserNoteRow(
        @Id UUID id,
        UUID userId,
        UUID noteId,
        String permissions,
        @Version Long version) {

    public UserNoteRow withPermissions(String newPermissions) {
        return new UserNoteRow(id, userId, noteId, newPermissions, version);
    }

    public UserNote toDomain() {
        return new UserNote(userId, noteId, Permissions.fromText(permissions));
    }
}

// data-jdbc
public interface UserNoteRepository extends ListCrudRepository<UserNoteRow, UUID> {
    Optional<UserNoteRow> findByUserIdAndNoteId(UUID userId, UUID noteId);
}

@Component
public class UserNoteStorageAdapter implements UserNoteStorage {

    private final UserNoteRepository repository;

    public UserNoteStorageAdapter(UserNoteRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<UserNote> find(UUID userId, UUID noteId) {
        return repository.findByUserIdAndNoteId(userId, noteId).map(UserNoteRow::toDomain);
    }

    @Override
    public void save(UserNote userNote) {
        String text = userNote.permissions().toText();
        UserNoteRow row = repository.findByUserIdAndNoteId(userNote.userId(), userNote.noteId())
                .map(existing -> existing.withPermissions(text))
                .orElseGet(() -> new UserNoteRow(UUID.randomUUID(), userNote.userId(), userNote.noteId(), text, null));
        repository.save(row);
    }

    @Override
    public void delete(UUID userId, UUID noteId) {
        repository.findByUserIdAndNoteId(userId, noteId).ifPresent(repository::delete);
    }
}

// data-r2dbc
public interface UserNoteRepository extends ReactiveCrudRepository<UserNoteRow, UUID> {
    Mono<UserNoteRow> findByUserIdAndNoteId(UUID userId, UUID noteId);
}

// адаптер (методы)
public Mono<UserNote> find(UUID userId, UUID noteId) {
    return repository.findByUserIdAndNoteId(userId, noteId).map(UserNoteRow::toDomain);
}

public Mono<Void> save(UserNote userNote) {
    String text = userNote.permissions().toText();
    return repository.findByUserIdAndNoteId(userNote.userId(), userNote.noteId())
            .map(existing -> existing.withPermissions(text))
            .defaultIfEmpty(new UserNoteRow(UUID.randomUUID(), userNote.userId(), userNote.noteId(), text, null))
            .flatMap(repository::save)
            .then();
}

public Mono<Void> delete(UUID userId, UUID noteId) {
    return repository.findByUserIdAndNoteId(userId, noteId).flatMap(repository::delete);
}
```

### `data-mongodb` / `data-mongodb-reactive`

Отличается только сущность; репозитории и адаптеры — как у JDBC (sync) и R2DBC (reactive). Уникальный индекс Boot сам не создаёт — явно или включить автосоздание свойством (точное имя свойства в Boot 4.1 сверить по метаданным jar).

```java
@Document("userNote")
@CompoundIndex(def = "{'userId': 1, 'noteId': 1}", unique = true)
public record UserNoteDocument(
        @Id UUID id,
        UUID userId,
        UUID noteId,
        String permissions,
        @Version Long version) {

    // withPermissions и toDomain — как у UserNoteRow
}
```

### `application-<vendor>` — схемы

```sql
-- application-h2, application-postgresql (и -reactive)
CREATE TABLE user_note (
    id          UUID         PRIMARY KEY,
    user_id     UUID         NOT NULL,
    note_id     UUID         NOT NULL,
    permissions VARCHAR(200) NOT NULL,
    version     BIGINT       NOT NULL,
    UNIQUE (user_id, note_id)
);

-- application-mysql (и -reactive); отображение UUID на CHAR(36) драйверами не проверено
CREATE TABLE user_note (
    id          CHAR(36)     PRIMARY KEY,
    user_id     CHAR(36)     NOT NULL,
    note_id     CHAR(36)     NOT NULL,
    permissions VARCHAR(200) NOT NULL,
    version     BIGINT       NOT NULL,
    UNIQUE (user_id, note_id)
);
```

### `controller-webmvc`

```java
public record PermissionsRequest(List<Permission> permissions) {}

public record PermissionsResponse(List<Permission> permissions) {}

public record AccessModelResponse(List<PermissionView> permissions, List<RoleView> roles) {

    public record PermissionView(Permission name, List<Permission> requires) {}

    public record RoleView(Role name, List<Permission> permissions) {}

    public static AccessModelResponse create() {
        return new AccessModelResponse(
                Arrays.stream(Permission.values())
                        .map(p -> new PermissionView(p, PermissionRequirements.of(p).stream().sorted().toList()))
                        .toList(),
                Arrays.stream(Role.values())
                        .map(r -> new RoleView(r, RolePermissions.of(r).stream().sorted().toList()))
                        .toList());
    }
}

@RestController
public class UserNoteController {

    private final UserNoteService service;

    public UserNoteController(UserNoteService service) {
        this.service = service;
    }

    @GetMapping("/access-model")
    public AccessModelResponse accessModel() {
        return AccessModelResponse.create();
    }

    @GetMapping("/notes/{noteId}/my-permissions")
    public PermissionsResponse myPermissions(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID noteId) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return new PermissionsResponse(service.permissionsOf(userId, noteId).sorted());
    }

    @PutMapping("/notes/{noteId}/users/{userId}")
    public void grant(@PathVariable UUID noteId, @PathVariable UUID userId, @RequestBody PermissionsRequest request) {
        service.grant(userId, noteId, new Permissions(Set.copyOf(request.permissions())));
    }

    @DeleteMapping("/notes/{noteId}/users/{userId}")
    public void revoke(@PathVariable UUID noteId, @PathVariable UUID userId) {
        service.revoke(userId, noteId);
    }
}

@RestControllerAdvice
public class AccessExceptionHandler {

    @ExceptionHandler(NoteNotFoundException.class)
    public ProblemDetail notFound(NoteNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ProblemDetail denied(PermissionDeniedException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(InvalidPermissionsException.class)
    public ProblemDetail invalid(InvalidPermissionsException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ProblemDetail conflict(OptimisticLockingFailureException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    }
}

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain security(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(requests -> requests.anyRequest().authenticated())
                .oauth2ResourceServer(server -> server.jwt(Customizer.withDefaults()))
                .build();
    }
}
```

`application-controller-webmvc.properties`: `spring.security.oauth2.resourceserver.jwt.issuer-uri=<адрес сервера авторизации>`.

### `controller-webflux`

Зеркально webmvc: те же DTO и `AccessExceptionHandler` (не зависят от стека — побайтово одинаковы в двух `controller-*`, кандидат на общее место, решение пользователя), контроллер возвращает `Mono`. Отличается безопасность:

```java
@Bean
public SecurityWebFilterChain security(ServerHttpSecurity http) {
    return http
            .authorizeExchange(exchanges -> exchanges.anyExchange().authenticated())
            .oauth2ResourceServer(server -> server.jwt(Customizer.withDefaults()))
            .build();
}
```

### UI

```js
const model = await fetch("/access-model").then(r => r.json());
let checked = new Set();

function applyRole(role) {
  checked = new Set(role.permissions);
}

function check(name) {
  checked.add(name);
  model.permissions.find(p => p.name === name).requires.forEach(check);
}

function uncheck(name) {
  checked.delete(name);
  model.permissions
    .filter(p => p.requires.includes(name))
    .forEach(p => uncheck(p.name));
}

function currentRole() {
  const match = model.roles.find(role =>
    role.permissions.length === checked.size &&
    role.permissions.every(p => checked.has(p)));
  return match ? match.name : "CUSTOM";
}

function save(noteId, userId) {
  return fetch(`/notes/${noteId}/users/${userId}`, {
    method: "PUT",
    headers: {"Content-Type": "application/json"},
    body: JSON.stringify({permissions: [...checked]}),
  });
}
```

### Тесты на таблицы (`contract`, без Spring)

```java
class AccessTablesTest {

    @Test
    void everyRoleHasValidPermissions() {
        for (Role role : Role.values()) {
            assertNotNull(RolePermissions.of(role), role + " is missing in RolePermissions");
            assertDoesNotThrow(() -> new Permissions(RolePermissions.of(role)), role.name());
        }
    }

    @Test
    void requirementsHaveNoCycles() {
        for (Permission permission : Permission.values()) {
            assertFalse(reachable(permission).contains(permission), permission + " requires itself");
        }
    }

    private Set<Permission> reachable(Permission start) {
        Set<Permission> seen = new HashSet<>();
        Deque<Permission> queue = new ArrayDeque<>(PermissionRequirements.of(start));
        while (!queue.isEmpty()) {
            Permission next = queue.poll();
            if (seen.add(next)) {
                queue.addAll(PermissionRequirements.of(next));
            }
        }
        return seen;
    }
}
```

## Нерешённое на 2026-09-25

- где живёт прикладной сервис (в `contract` рядом с портом или отдельный модуль; модуля прикладного слоя сегодня нет, корень «общее» дерева вариантов пуст)
- project-зависимости лист→`controller-*`→`contract` и лист→`data-*`→`contract` сходятся в листе — по аналогии «вид 2» допустимо, но правило про диаманты сформулировано и для project-зависимостей, нужно явное решение пользователя
- состав `requires` и наборы ролей — пример ассистента, не утверждён (в т. ч. требует ли модерация комментариев права комментировать)
- поиск по праву средствами БД («все заметки, которые я могу править») строкой не индексируется — выборка по `userId` + фильтр в памяти; если понадобится — переход на одну запись на право (только `data-*`)
- вводить ли механическую проверку отсутствия `if`/`switch` (grep `'\b(if|switch)\s*\('` или своё XPath-правило PMD) — предложено, не решено; решено ли запрещать и `switch` по `sealed`-типам (проверка полноты компилятором) — не обсуждено
- версия с id от приложения против id от БД — расхождение механизмов генерации по веткам (CLAUDE.md → «Открытые решения» → «Поведенческая аналогичность» п.3)
- кто может вызывать `grant`/`revoke` — в коде итоговой модели право вызывающего не проверяется: нужно право вроде «управлять доступом» или правило «только владелец» (связано с вопросом «доступ потерян у всех» ниже)
- где живут общие для двух `controller-*` DTO и `AccessExceptionHandler` (побайтово одинаковы, не зависят от стека)
- гонка двух одновременных первых выдач одной паре: поиск и вставка раздельны, вторая вставка падает на уникальности (`user_id`, `note_id`) → `DuplicateKeyException`, обработчика нет — вернуть 409 или повторить операцию
- новый атомарный convention-плагин для OAuth2 resource server и конкретный сервер авторизации (`issuer-uri`)
- **⚠️ ВАЖНЫЙ ВОПРОС (поставлен пользователем 2026-09-25) — как избежать состояния, когда все пользователи лишились доступа к заметке** (последний, кто может управлять доступом, отозвал себя/понизил себе права, или его учётная запись удалена в другом сервисе — заметка становится «сиротой»: читать, править, выдавать доступ некому). Это инвариант на несколько записей («у заметки всегда есть хотя бы один управляющий») — в отличие от `Permissions`, одной записью не держится. Варианты, не выбрано: (а) владелец — отдельная запись (`noteId` уникальный, `ownerId`), создаётся вместе с заметкой, отозвать нельзя, только передать (обновление одной записи); инвариант держит обычный уникальный индекс одинаково на всех 8 листьях, гонок нет — **рекомендация ассистента**; это возврат к ранее снятому варианту «владение отдельной записью», снятому вместе с отказом от понятия владельца; (б) проверка в `revoke`/`save`: «не убирать последнего с правом управления» — гонка: два одновременных отзыва двух управляющих видят по 2 и оба проходят → 0; нужна либо блокировка строк (`SELECT … FOR UPDATE` — синтаксис и поддержка различаются по вендорам и R2DBC; в Mongo — транзакция на replica set), либо одна строка-«версия доступа заметки», которую каждое изменение доступа обновляет с `@Version` — одновременные изменения конфликтуют оптимистической блокировкой, работает одинаково везде; (в) агрегат «доступ заметки» со всеми выдачами внутри и одной версией — в JDBC дочерняя таблица, в Mongo массив, в R2DBC коллекций нет — ломает зеркальность; (г) путь восстановления — администратор (глобальный scope в JWT) возвращает доступ, как администратор Google Workspace передаёт файлы удалённого пользователя — дополняет, не предотвращает; (д) заметка без управляющих удаляется (Google Drive удаляет файлы удалённой учётной записи, если их не передали) — меняет смысл, данные теряются. Отдельная грань: удаление пользователя в другом сервисе оставляет его выдачи — межсервисное событие (`UserDeleted`), см. CLAUDE.md → «Открытые решения» → «Межсервисная консистентность»

## Рассмотренные и отклонённые варианты (с причиной)

- булевы флаги `canRead`/`canEdit`/`canDelete` в `UserNote` — допускают `EDIT` без `READ`; вариант «запись = READ + флаги прочих прав» делает это невыразимым и лучше всего переносится на SQL/NoSQL (естественный логический тип везде, индекс по праву, инвариант в одной записи), но новое право = колонка во всех вендорных схемах + поле в 4 сущностях + маппер — отклонён пользователем по Open/Closed
- `Set<Permission>` в сущности как встроенная коллекция — Spring Data R2DBC коллекций не поддерживает, у MySQL нет массивов; в JDBC — дочерняя таблица, в Mongo — массив: SQL-ветки расходятся
- битовая маска в `int` — непрозрачно, побитовые операции в SQL у вендоров разные, в Cassandra/DynamoDB их нет
- одна запись на право (кортеж Zanzibar `note:1#edit@user:7`) — модель не меняется никогда, но выдача набора — запись нескольких строк, нужны транзакции (Mongo — replica set); оставлено как путь на будущее
- роль по ссылке `roleId` + таблица ролей (RBAC как данные) — второй запрос/соединение на каждую проверку, правило удаления назначенной роли, коллекция прав в роли упирается в R2DBC; нужен, только если правка роли должна сразу менять доступ всем её обладателям
- роль-enum с правами в коде, хранится только роль (модель Google Drive/Dropbox) — проще всего и типично для совместного доступа к документам, но нет индивидуальных наборов, а пользователь хочет таблицу флажков с ручной правкой
- владение отдельной записью (`noteId` уникальный, `ownerId`) вместо роли OWNER — снимает частичный уникальный индекс «ровно один владелец» (его нет в MySQL и, по памяти, в H2); снято вместе с отказом от понятия владельца в пользу набора прав
- `UserNote` как Java-интерфейс, реализуемый сущностями `data-*` (прецедент `{Entity}Persistable` старого `note/`) — убирает маппер только при чтении; инвариант пришлось бы проверять в каждой реализации (4 модуля, без гарантии), создание требует доменного класса или фабрики в порте, `sealed` через модули невозможен; с `Permissions` как отдельным объектом-значением инвариант не зависит от этого выбора
- enum с зависимостями в конструкторе (`EDIT(READ)`) — компилятор запрещал циклы; отклонён пользователем в пользу плоского enum
- правила как отдельные объекты (Specification, `enum PermissionsRule implements Predicate`) и объект решения `AccessDecision` (GRANTED/DENIED/NOT_FOUND с `enforce()`, по модели XACML Permit/Deny) — без `if`, но сложнее для новичка; отложено
- stream с `throw` внутри `ifPresent` вместо `if` — формально без `if`, но хуже читается; `Optional.of(x).filter(...).orElseThrow(...)` ради ухода от `if` — злоупотребление `Optional`
- Spring Security ACL (`spring-security-acl`) — своя схема только под JDBC, права битовой маской прямо на объект, нет ролей-наборов; не подходит R2DBC/Mongo-веткам
- внешний PDP (OpenFGA, SpiceDB, OPA, Cerbos) и Event Sourcing выдач — избыточны сейчас; граница `AccessPolicy` (если появится) оставляет путь открытым

## Аналоги из индустрии (по публичным API/статьям, внутреннее устройство не публикуется)

- Unix — 9 битов `rwx` в `st_mode`, зависимостей нет (`-w-` допустимо намеренно), POSIX ACL в расширенных атрибутах
- Windows NTFS — DACL из ACE (allow/deny, SID, 32-битная маска доступа, наследование), дескрипторы в `$Secure`; «базовые разрешения» в UI — наборы поверх ~14 детальных, несовпадение — «Особые разрешения»
- macOS — POSIX-биты + ACL (`chmod +a`); Finder — «Чтение и запись»/«Только чтение»/«Только запись (почтовый ящик)»/«Нет доступа»
- Google Drive API v3 — `Permission` с одной ролью из `owner`/`organizer`/`fileOrganizer`/`writer`/`commenter`/`reader`; `files.capabilities` — вычисляемые флаги (`canEdit`, `canComment`, …) для UI; внутри — Zanzibar (статья Google 2019)
- Dropbox — уровень доступа участника `owner`/`editor`/`viewer`/`viewer_no_comment` и служебные; API отдаёт вычисленные разрешённые действия
- GitHub — базовые роли Read/Triage/Write/Maintain/Admin; custom repository roles (Enterprise) = базовая роль + дополнительные права
- SonarQube — permission templates: шаблон копирует права при создании проекта, изменение шаблона не трогает существующие

## Паттерны, обсуждённые для неопределённых требований

- принцип: Парнас 1972 («On the Criteria To Be Used in Decomposing Systems into Modules») — модуль вокруг решения, которое вероятно изменится
- модель доступа: Policy Decision/Enforcement Point (XACML), Strategy + Composite (роль ∪ явные права), внешний PDP
- представление: Value Object (`Permissions`), Data Mapper, тонкий порт хранилища
- хранение, устойчивое к смене модели: кортежи Zanzibar, Event Sourcing/журнал выдач, CQRS (модель чтения для «что я могу править»)
- эволюция: Branch by Abstraction, Parallel Change (expand/migrate/contract) + Flyway/Liquibase, `schemaVersion` в документах Mongo, Tolerant Reader
- страховка: контрактный тест на порт и на политику доступа (fitness function), прогон против всех листьев
- без ветвлений: таблица вместо условий, Replace Conditional with Polymorphism, «ветвиться один раз на границе, дальше полиморфизм»; `if` как охранное условие — не запах (запах — повторяющийся `switch` по одному признаку, `instanceof`-цепочки, аргументы-флаги, Фаулер «Рефакторинг»)

## Связь с деревом вариантов (CLAUDE.md → «Архитектура и структура проекта» → «Корневое дерево вариантов»)

- модель проверена на все 8 листьев: домен, проверка доступа, API одни; различия — тип UUID в схеме вендора (у MySQL нет `UUID`, `CHAR(36)` — отображение драйверами не проверено) (признак новой записи — `@Version`, одинаков у JDBC/R2DBC/Mongo)
- враждебные данные для варианта Б центрального вопроса: duplicate-key на паре (`userId`, `noteId`), представление UUID, строка прав
- каскадное удаление (заметка → её `UserNote`) — кодом в прикладном сервисе, не `ON DELETE CASCADE`: у Mongo FK нет, иначе поведение разойдётся; атомарность в Mongo требует replica set (`.withReplicaSet()` — точка возврата 2026-09-21)

## Инварианты по слоям — UI / Framework / Model / Storage (обсуждение 2026-09-25)

Принцип: инвариант **определяется** в одном месте (модель), **применяется** на нескольких уровнях; роли слоёв — UI помогает человеку не ошибиться, фреймворк отсекает неверный запрос на входе, модель гарантирует, хранилище страхует от записи в обход приложения.

- **UI — удобство, не гарантия**: зависимые флажки (отметили «Изменять» → отмечается «Читать»; сняли «Читать» → снимаются зависящие права; либо флажок недоступен без требуемого); шаблоны ролей дают заведомо допустимый набор; проверка формы до отправки (встроенная проверка HTML-форм, схемы валидации JS-библиотек); правила не копировать в клиент, брать с сервера (`requires` из `/access-model`) — иначе разъедутся. Гарантирует: ничего — API вызывает любой клиент/скрипт/сервис
- **Framework (Spring Boot) — отсечь неверное на входе**: Jackson — неизвестное имя enum → ошибка разбора → 400; разбор можно строить сразу через объект модели (`@JsonCreator`); Bean Validation (`spring-boot-starter-validation`) — `@NotNull`/`@NotEmpty` на DTO, межполевые правила — своя аннотация уровня класса с `ConstraintValidator`, `@Valid` на `@RequestBody` → 400 в webmvc и webflux; свой валидатор должен вызывать модель, не повторять правило; `@Validated` на сервисе — проверка аргументов на границе прикладного слоя; обратные вызовы Spring Data перед записью — `BeforeConvertCallback`/`BeforeSaveCallback` (JDBC, R2DBC-реактивные, Mongo); Bean Validation при сохранении автоматически только у JPA (Hibernate), у Mongo — `ValidatingMongoEventListener` (Boot сам не настраивает), у JDBC/R2DBC механизма нет; `@ExceptionHandler` → `ProblemDetail` 400; нарушения хранилища Spring переводит в `DataIntegrityViolationException`/`DuplicateKeyException`; `@Transactional` — для инвариантов на несколько записей; Spring Security — «кто может», не корректность данных. Гарантирует: только для этого входа (очереди, пакетные задачи, другой контроллер не защищены)
- **Model — единственное место определения**: всегда-корректный объект (проверка в компактном конструкторе record или в фабрике при закрытом конструкторе); неизменяемость (record, `Set.copyOf`); объект-значение (`Permissions` — правило принадлежит понятию, не содержащей его сущности); невыразимость недопустимого в типах (уровень-enum, «запись = READ», `sealed`) — сильнейший вариант, проверять нечего; агрегат DDD — межобъектный инвариант проверяет корень, граница агрегата = граница инварианта и транзакции; тесты — unit на правило и на основе свойств (jqwik: случайные наборы, допустимые создаются, недопустимые отклоняются). Гарантирует: недопустимого значения в программе нет, откуда бы оно ни пришло
- **Storage SQL**: `NOT NULL`/`UNIQUE`/`PRIMARY KEY` — одинаково у H2/MySQL/PostgreSQL; `CHECK` — везде, MySQL проверяет с 8.0.16; для флагов просто (`CHECK (NOT can_edit OR can_read)`), для набора строкой — хрупкое `LIKE`; внешние ключи; типы-перечисления (`ENUM` MySQL, `CREATE TYPE`/`CREATE DOMAIN` PostgreSQL) и триггеры — у каждого вендора свои
- **Storage Mongo**: уникальные индексы; валидатор коллекции `$jsonSchema` (`validationLevel`/`validationAction`); FK и триггеров нет; транзакции по нескольким документам — только replica set
- **Storage будущие NoSQL-ветки**: Cassandra — почти ничего, запись с существующим ключом молча перезаписывает, защита от повторной вставки — только лёгкие транзакции `IF NOT EXISTS`; DynamoDB — условные выражения (`attribute_not_exists`) и транзакционная запись; Redis — только Lua-скрипты. Гарантирует (хранилище в целом): защиту от любых писателей (ручной SQL, миграции, другие сервисы), но набор механизмов у вендоров разный

**По дереву вариантов**: чем ниже слой, тем сильнее расходятся механизмы — модель одна на 8 листьев; фреймворк почти одинаков (кроме обратных вызовов, свои у каждой технологии); в хранилище одинаковы только `NOT NULL` и уникальность. Смысловой инвариант в хранилище даст разное поведение по веткам — ровно расхождение, которое центральный вопрос требует исключить. Вывод: смысловые правила — в модели, в хранилище — только структурные, одинаковые везде. Сильные ограничения хранилища нужны, когда в базу пишет кто-то кроме приложения; при «своя база у каждого сервиса» писатель один.

**Применение к `Permissions`** (предложение, не решение): UI — флажки с автоматической отметкой зависимостей, правила из `/access-model`; Framework — DTO `List<Permission>` (неизвестное имя отсекает Jackson → 400), контроллер создаёт `new Permissions(...)`, `InvalidPermissionsException` → 400 через `@ExceptionHandler`, своя аннотация Bean Validation не нужна (лишь вызывала бы модель); Model — `Permissions` проверяет себя в конструкторе + тесты на таблицы (каждая роль допустима, в зависимостях нет циклов); Storage — `NOT NULL` на `permissions`, уникальная пара (`user_id`, `note_id`) индексом в SQL и Mongo, `CHECK` на строку прав не ставить; при чтении `Permissions.fromText(...)` проверяет повторно — испорченная в базе запись в модель не проходит.
