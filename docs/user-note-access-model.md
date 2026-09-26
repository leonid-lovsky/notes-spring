# Модель доступа `UserNote` — права на заметку (обсуждение 2026-09-25)

> **Назначение:** полный ход обсуждения модели доступа `UserNote` 2026-09-25→2026-09-26 — требования, выводы, код-эскизы по модулям, отклонённые варианты, аналоги, инварианты по слоям
> **Когда читать:** при возврате к правам/ролям/хранению прав, когда нужен «почему»; краткое текущее состояние — раздел «Кратко — текущее состояние» ниже, итоги по микросервисам — `docs/microservices-reference.md`
> **Статус:** консультации, решения не приняты, в коде ничего нет; `Permissions` временно отложены пользователем 2026-09-26
> **Разделы:** «Кратко — текущее состояние», «Требования пользователя», «Ключевые выводы 2026-09-25», «Точка остановки», «Итоговая модель целиком — код по модулям», «Направление 2026-09-26…», «Продолжение 2026-09-26…», «Нерешённое на 2026-09-25», «Рассмотренные и отклонённые варианты», «Аналоги из индустрии», «Паттерны…», «Связь с деревом вариантов», «Инварианты по слоям»
> **Связано:** `docs/microservices-reference.md`, CLAUDE.md → «Открытые решения» → «Модель доступа `UserNote`», «⚠️ Основной вопрос микросервисной архитектуры»

Статус: **консультация, решение не принято, в коде ничего не реализовано** — пользователь пишет код сам (CLAUDE.md → «Правила» → «Роль ассистента»). Файл фиксирует ход рассуждения и точку, на которой обсуждение остановилось 2026-09-25, чтобы не проходить его заново; продолжение 2026-09-26 — раздел «Направление 2026-09-26 — строка на право, use case как функция, модели входа/выхода» (актуальнее «Точки остановки» в названных там пунктах). Комментарии вынесены за рамки обсуждения (вернуться позже); свой комментарий правит/удаляет автор — право из авторства, не выдаваемое.

## Кратко — текущее состояние (2026-09-26)

- `Permissions` временно отложены пользователем 2026-09-26; рассматривается голая связь `UserNote (id, user_id, note_id)` — `record UserNote(UUID userId, UUID noteId)`, пять use case с именами по HTTP (`GetUserNote.get`, `PutUserNote.put` — идемпотентно, `DeleteUserNote.delete` — 204 и для отсутствующей, `ListUserNotes.list`, `ListNoteUsers.list`), хранение `UserNoteRow(@Id id, userId, noteId, @Version version)` одинаково в 4 `data-*` — раздел «Модель связи без прав»
- ⚠️ основной вопрос микросервисной архитектуры — списки «заметки пользователя с правами» и «пользователи заметки с правами»; рекомендация ассистента — хранение (В) (связь + отдельная таблица прав), курсорная пагинация, обогащение у вызывающего (BFF) — раздел «Списки с перечнем прав доступа — три варианта хранения»
- модель прав (на случай возврата): последняя рекомендация — одна строка на право + замыкание вместо проверки (раздел «Направление 2026-09-26…» → «Хранение прав…»); до неё — «Точка остановки» 2026-09-25 (строка CSV, отказ недопустимого набора)
- стиль use case (направление пользователя): один интерфейс = один поток = один метод, stateless; свои модели входа/выхода, идентичные переиспользуются; `UserNote` — `record`, не интерфейс; контроллер тонкий, один `@RestControllerAdvice` на оба стека
- Security: JWT несёт только `sub`; resource server в `controller-*`; правило доступа — в прикладном слое, не в `@PreAuthorize`; 404 вместо 403 при отсутствии доступа
- открыто: хранение прав (А)/(Б)/(В); кто видит списки; ⚠️ «как не допустить, что доступ к заметке потеряли все» (рекомендация — владелец отдельной записью); где живут реализация use case и общие классы `controller-*`; имена/пакеты reactive-интерфейсов — «Нерешённое на 2026-09-25» и `docs/microservices-reference.md` → «Открыто»

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
- **Дрейф рекомендаций ассистента в ходе обсуждения** (чтобы не принять старую рекомендацию за актуальную): флаги с «запись = READ» → множество строкой → флаги (по критерию переносимости) → роль-enum по модели Google (по аналогам) → множество + роли только в UI (по требованиям пользователя: без флагов, таблица флажков, плоские enum). 2026-09-26 → строка на право + замыкание, `UserNote` — `record`, use case «один интерфейс — один поток — один метод», модели входа/выхода (раздел «Направление 2026-09-26…»). Каждый поворот — от нового требования или критерия, актуальны «Точка остановки» и её уточнения в «Направлении 2026-09-26…»

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

## Направление 2026-09-26 — строка на право, use case как функция, модели входа/выхода

Статус: консультация, в коде ничего не реализовано; актуальнее «Точки остановки» и «Итоговой модели целиком» (2026-09-25) в пунктах ниже, остальное из них в силе. Повод — запрос пользователя «модель `UserNote`, одинаково нативно переводимая в SQL и NoSQL, легко интегрируемая с Spring Security и JWT» и просьба предложить свежие решения, даже не рассматривавшиеся ранее. Состояние кода на 2026-09-26: в `contract` пользователь уже написал `interface UserNote { UUID getUserId(); UUID getNoteId(); }` и `interface UserNoteService { List<UserNote> getUserNoteByUserId(UUID userId); }`.

### Хранение прав — одна строка на право + замыкание вместо проверки (предложение ассистента, не выбрано)

- замыкание: действующие права = замыкание хранимых по `PermissionRequirements` (`DELETE`→`EDIT`→`READ`, READ всегда); `Permissions` в компактном конструкторе нормализует набор (`closureOf`), а не отклоняет — недопустимого набора не существует по построению, `InvalidPermissionsException` и путь «недопустимый набор → 400» исчезают; `without(p)` убирает право и всё, что от него зависит; UI показывает замыкание, возвращённое сервером
- хранение: сущность-строка (имя не выбрано) `(@Id UUID id, UUID userId, UUID noteId, Permission permission, @Version Long version)`, одинаковая во всех 4 `data-*` (Mongo — `@Document` + `@CompoundIndex` на `userId`+`noteId`+`permission`, `unique = true`); таблица `user_note_permission`, `UNIQUE (user_id, note_id, permission)`, индекс для поиска по праву; репозиторий `findByUserIdAndNoteId` (права пары) и `findByUserIdAndPermission` («заметки, которые я могу править» — по индексу)
- почему нативно: enum Spring Data пишет строкой по имени в JDBC, R2DBC и Mongo сам — без конвертеров, `fromText`/`toText` и массивов (нет у MySQL и R2DBC) — по памяти, на байткоде 4.1.1 не проверено; новое право — константа enum + строка в `PermissionRequirements`, без миграции (Open/Closed сохранён); это кортеж Zanzibar (`note:{noteId}#EDIT@user:{userId}`) — переход на OpenFGA/SpiceDB — перенос данных один к одному
- снимает прежнее возражение против «одной записи на право» («нужны транзакции», «Рассмотренные и отклонённые варианты»): благодаря замыканию любой частично записанный набор допустим; запись набора — разницей (вставить недостающие, удалить лишние), не «удалить всё и вставить»; повторная вставка того же права (`DuplicateKeyException`) — идемпотентный успех, адаптер её проглатывает — гонка двух первых выдач («Нерешённое на 2026-09-25») исчезает
- цена: больше строк, запись набора — несколько операций; производный `deleteBy…` во всех трёх технологиях не проверен
- по дереву вариантов: модель, проверка, API одни на 8 листьев; различия — тип UUID в схеме MySQL (`CHAR(36)`/`BINARY(16)`, не проверено); Mongo — `spring.mongodb.representation.uuid=standard` (по метаданным `spring-boot-mongodb-4.1.1.jar` значение по умолчанию `unspecified`; что драйвер тогда отказывается писать UUID — по памяти, запуском не проверено) и уникальный индекс Boot сам не создаёт — `spring.data.mongodb.auto-index-creation=true` (есть в метаданных `spring-boot-data-mongodb-4.1.1.jar`) или индекс явно; атомарность записи набора — в SQL `@Transactional`, в Mongo без replica set нет (различие поведения, смягчено замыканием и записью разницей)
- запасной вариант: замыкание вместо проверки применимо и к прежней строке CSV в одной записи — уходит путь 400, хранение прежнее
- вопрос «все лишились доступа к заметке» модель не решает — рекомендация «владелец отдельной записью» («Нерешённое на 2026-09-25») в силе

### `UserNote` — `record`, не интерфейс (рекомендация ассистента)

- `public record UserNote(UUID userId, UUID noteId, Permissions permissions) {}` в `contract`
- главный довод — центральный вопрос: общий тест на порт сравнивает результаты 8 листьев по значению, `record` даёт `equals`/`hashCode` по полям одинаково на любой ветке; у интерфейса `equals` свой у каждой реализации (по умолчанию — по ссылке)
- интерфейс для данных, повторяющий геттеры одной структуры, — header interface (Фаулер); интерфейс уместен для поведения с несколькими реализациями (порты, use case)
- при хранении «строка на право» ни одна сущность хранилища не соответствует `UserNote` один к одному — реализовать интерфейс некому; интерфейс осмыслен только при строке CSV в одной записи, если сущности `data-*` должны сами быть `UserNote` (тогда тест сравнивает поля, не `equals`)
- прежнее возражение «инвариант проверять в каждой реализации» ослабло — инвариант в `Permissions`; `sealed` через модули не работает; будущие роли меняют способ получить `Permissions`, не `UserNote`
- следствие для кода пользователя: у `record` методы доступа `userId()`/`noteId()` вместо `getUserId()`/`getNoteId()` — Jackson, Spring Data, SpEL находят оба стиля

### Use case — один интерфейс, один поток данных, один метод, stateless (направление пользователя)

- входные порты (use case, Clean Architecture Мартина — input port/interactor; гексагональная — driving port) — интерфейсы в `contract` (sync) и `contract-reactive`; реализации (interactor) — в отдельном модуле (имя не выбрано); выходной порт хранилища — `contract`, реализация — `data-*`; `controller-*` зависит только от `contract`, реализацию сервиса не знает — закрывает вопрос «где живёт прикладной сервис»
- формула пользователя: один интерфейс = один поток данных = один метод, stateless — use case как функция (command handler CQRS); interactor держит только `final`-ссылки на порты, всё о вызове приходит в аргументе
- SRP у Мартина — «одна причина изменения», не «один метод»; разделение по методу — ближе к Interface Segregation; ассистент сначала предлагал интерфейсы по клиентам и реализации по причинам изменения (проверка / управление) — снято в пользу формулы пользователя
- правило «не найдено → 404» не расходится по классам: живёт в одном use case проверки доступа, остальные вызывают его композицией (конструктор), не слиянием классов
- четыре потока (имена иллюстративные): `CheckAccess.check`, `GetPermissions.get`, `GrantAccess.grant`, `RevokeAccess.revoke`; `/access-model` — не use case (нет потока данных, только константы таблиц) — чистая функция в `contract`
- интерфейс с именем потока, не общий `UseCase<C, R>` — в точке внедрения видно имя потока
- кто вызывает `grant`/`revoke`: во входе `actorId` (`sub` из JWT, контроллер достаёт из токена), interactor проверяет право вызывающего через `CheckAccess` (право вроде `MANAGE` — пример, его ещё нет) — закрывает вопрос «кто может вызывать `grant`/`revoke`» по механизму, конкретное право не выбрано
- stateless сервер + JWT + stateless interactor — одна идея на трёх уровнях: личность не хранится, приходит в каждой команде; interactor не знает HTTP и Spring Security — одна реализация за webmvc, тестируется общим тестом без Security-контекста
- цена: 4 потока × sync/reactive = 8 интерфейсов + 8 реализаций; код не дублируется — копии различаются только `S` против `Mono<S>`

### Модели входа и выхода use case (направление пользователя)

- у interactor свои модели входа и выхода (Input Data / Output Data Мартина), отдельно от доменных объектов и от HTTP, по образцу request body / response body
- переиспользовать модель, когда совпадает смысл и причина изменения, не только форма полей (иначе правка одного use case тянет другой)
- набор (имена иллюстративные): `AccessKey(UUID userId, UUID noteId)` — вход `get`, часть других входов; `AccessCheck(AccessKey key, Permission permission)` — вход `check`; `AccessChange(UUID actorId, AccessKey target, Set<Permission> permissions)` — вход `grant`; `AccessRevoke(UUID actorId, AccessKey target)` — вход `revoke` (не `AccessChange` с пустым набором: при замыкании пустой набор = READ, не «отозвать»); `AccessView(UUID userId, UUID noteId, List<Permission> permissions)` — единственный выход `check`/`get`/`grant`; `revoke` — `void`/`Mono<Void>`
- `grant` возвращает `AccessView`, не `void` — действующие права после замыкания шире запрошенных, клиенту нужны для флажков
- внутри входов/выходов — `Set<Permission>`/`List<Permission>`, не `Permissions`: доменный объект строит interactor; `UserNote` и `Permissions` — внутренние типы `contract` и порта хранилища
- по дереву вариантов: модели входа/выхода не зависят ни от sync/reactive, ни от вендора — один экземпляр в `contract`, `contract-reactive` переиспользует (`Mono<AccessView> grant(AccessChange input)`); удваиваются только интерфейсы потоков; JSON ответа одинаков на 8 листьях; общий тест сравнивает `AccessView` через `equals` `record`

### Контроллер — собрать вход, вызвать, отдать; ошибки — один обработчик

- контроллер: вход из пути (`@PathVariable`), токена (`actorId`) и тела → use case → выход; `ResponseEntity` не нужен — `AccessView` отдаётся как `@ResponseBody` со статусом 200, `revoke` — `@ResponseStatus(HttpStatus.NO_CONTENT)`; `controller-webflux` зеркален, отличаются только типы `Mono<AccessView>`/`Mono<Void>`
- выход use case отдаётся прямо как тело ответа (нет ничего HTTP-специфичного) — отдельный DTO ответа не нужен; вход как `@RequestBody` не годится (собирается из трёх источников) — в `controller-*` остаётся одно тело `PermissionsBody(Set<Permission> permissions)` для `PUT`; у `GET`/`DELETE` тела нет
- личность: мета-аннотация `@AuthenticationPrincipal(expression = "T(java.util.UUID).fromString(subject)")` (имя вроде `@CurrentUserId`) — резолвер есть в webmvc и webflux, в контроллер сразу приходит `UUID`; на Spring Security 7 не проверено
- ошибки: исключения use case в `contract` — `NoteNotFoundException` (404), `PermissionDeniedException` (403), `AccessConflictException` (409); `@RestControllerAdvice` с тремя `@ExceptionHandler` → `ProblemDetail` — выбор метода по типу исключения делает Spring, без `if`/`switch`; работает в webmvc и webflux (`Mono.error` попадает в тот же обработчик), импорты общие — файл побайтово одинаков в обоих `controller-*` (к вопросу «где живут общие классы двух `controller-*`»)
- остальные ошибки обработчику не нужны: разбор запроса (неизвестное имя права — Jackson, нечитаемый UUID в пути, нет тела) — 400 от Spring, как `ProblemDetail` (`problemdetails` включён в `application-controller-<stack>.properties`); недопустимого набора прав нет (замыкание); исключения хранилища переводит адаптер `data-*` — `DuplicateKeyException` при строке на право — идемпотентный успех, `OptimisticLockingFailureException` → `AccessConflictException`; контроллер видит одни исключения на 8 листьях, детали хранилища не протекают
- где что живёт: контракт исключений — `contract` (один на обе парадигмы); перевод исключений хранилища — каждый `data-*`; перевод в HTTP — один класс на оба стека; новое исключение use case — один метод-обработчик

### Альтернативы для Spring Security (не выбраны)

- проверка через `@PreAuthorize` с шаблонами аннотаций (`AnnotationTemplateExpressionDefaults`) — `@RequiresNotePermission(EDIT)`, внутри вызов бина прикладного слоя; правило остаётся в приложении; reactive не проверен — альтернатива, рекомендация «проверка в interactor» не отменена
- токены-возможности (Biscuit, macaroons) для ссылок «поделиться» — права в самом токене, только сужаются; дополняют хранение в базе, не заменяют (выданный токен не отозвать)

## Продолжение 2026-09-26 — поиск, именование по HTTP, связь без прав, списки с правами

Статус: консультация, в коде ничего не реализовано; все имена классов/интерфейсов — иллюстративные, не утверждены. Идёт после раздела «Направление 2026-09-26 — строка на право, use case как функция, модели входа/выхода» (зафиксирован коммитом `4f3e337`).

### ⚠️ Основной вопрос микросервисной архитектуры (поставлен пользователем 2026-09-26)

- **как получать список всех заметок пользователя с перечнем прав доступа и список всех пользователей заметки с перечнем прав доступа** — пользователь определил это как основной вопрос в микросервисной архитектуре (зеркало — CLAUDE.md → «Открытые решения»); решение не принято
- суть: `user-note` хранит только идентификаторы и права; `name` пользователя и `content` заметки — в других сервисах со своими базами (database-per-service, без FK/JOIN между сервисами) — список нужно (1) постранично выбрать в `user-note` одинаково на 8 листьях и (2) обогатить данными чужих сервисов без чтения их баз
- состояние на 2026-09-26: рекомендация ассистента — хранение (В) ниже + курсорная пагинация + обогащение на стороне вызывающего (клиент/BFF) по идентификаторам страницы одним пакетным запросом; будущие варианты — ниже «Задел на другие транспорты»

### Модель связи без прав (решение пользователя 2026-09-26 — `Permissions` временно отложены)

- пользователь сузил рассмотрение до связи `User (id, name)`, `Note (id, content)`, `UserNote (id, user_id, note_id)`; права вернулись в вопросе про списки (раздел ниже)
- связь неизменяема (создать/прочитать/удалить) → один `record UserNote(UUID userId, UUID noteId)` — ключ, вход, выход и доменное значение (по правилу пользователя «переиспользовать идентичное»); `id` — суррогатный ключ хранилища, личность — пара, её держит `UNIQUE (user_id, note_id)`
- use case (один интерфейс — один поток — один метод, имена по HTTP): `GetUserNote.get(UserNote)` — `GET /notes/{noteId}/users/{userId}` 200/404 («существует ли связь»); `PutUserNote.put(UserNote)` — `PUT` того же пути, идемпотентное создание (ресурс определён парой из пути — `PUT`, не `POST`); `DeleteUserNote.delete(UserNote)` — `DELETE`, 204 и для отсутствующей связи (идемпотентно; закрывает старый вопрос формы `remove`: sync `void`, reactive `Mono<Void>`); `ListUserNotes.list(UserNotesQuery)` — `GET /users/{userId}/notes` (текущий `UserNoteService.getUserNoteByUserId` пользователя — это он, со страницей); `ListNoteUsers.list(NoteUsersQuery)` — `GET /notes/{noteId}/users`; модели `PageQuery(String cursor, int limit)`, `UserNotePage(List<…> items, String nextCursor)`; исключение `UserNoteNotFoundException` → 404; в reactive «не найдено» — сигнал ошибки, не пустой `Mono` (иначе WebFlux отдаст 200 с пустым телом)
- хранение: `UserNoteRow(@Id UUID id, UUID userId, UUID noteId, @Version Long version)` одинаково в 4 `data-*` (`@Version` при неизменяемой связи — только признак новой записи); `UNIQUE (user_id, note_id)` + индекс `(note_id, user_id)`; гонка двух `PUT` → `DuplicateKeyException` → адаптер считает успехом; Mongo — `spring.mongodb.representation.uuid=standard`, `spring.data.mongodb.auto-index-creation=true` или индекс явно
- JWT только аутентифицирует; кто вправе создавать/удалять чужие связи — вне этой части (когда появится правило — во входы добавится `actorId` из `sub`)
- межсервисный задел: удаление заметки/пользователя в чужом сервисе оставляет строки-сироты — будущие use case «удалить все связи заметки»/«удалить все связи пользователя», вызываемые слушателем событий `NoteDeleted`/`UserDeleted` (новый driving-адаптер рядом с `controller-*`, ядро не меняется)
- аналогия пользователя: `UserNote` — таблица, которую Hibernate создал бы для `@ManyToMany` (`User.getNotes()`/`Note.getUsers()`); вывод — по структуре да, но явная сущность-связь обязательна: Hibernate сам рекомендует заменять `@ManyToMany` явной сущностью, как только у связи есть свои колонки (права); сервисы разные — ассоциации нет, только id (DDD: агрегаты ссылаются по id, Вернон «Effective Aggregate Design»); JPA в проекте нет, у Spring Data JDBC — `AggregateReference`, у R2DBC связей нет, Mongo — массив/`DBRef` разошёлся бы с SQL; `getNotes()`/`getUsers()` = два списка, но постраничные (без неограниченных ленивых коллекций, N+1, `LazyInitializationException`)
- имя `UserNote` против `NoteUser`: единого стандарта нет (Rails — `notes_users`, Laravel — `note_user`, по алфавиту; JPA по умолчанию — владеющая сторона первой); по смыслу оба неточны (сущность — связь, не заметка и не пользователь); **рекомендация ассистента — оставить `UserNote`** (укоренено: сервис, пакет, 14 модулей, документация; направления поиска симметричны); доменное имя (`Collaborator`/`Membership`/`Share`) — когда у связи снова появятся свои данные и поведение

### Списки с перечнем прав — три варианта хранения (не выбрано)

- выход один на оба списка: `UserNoteView(UUID userId, UUID noteId, List<Permission> permissions)` + `UserNotePage(List<UserNoteView> items, String nextCursor)`; `permissions` в выходе всегда отсортированы в порядке enum (база не гарантирует порядок — иначе `equals` общего теста разойдётся по вендорам)
- (А) права строкой в самой связи `user_note(id, user_id, note_id, permissions)` — список одним запросом; фильтра по праву средствами базы нет (`LIKE` у вендоров различается), фильтр в памяти ломает размер страницы
- (Б) строка на право `user_note(id, user_id, note_id, permission)` — страница по строкам `READ` (одна на пару при хранении полного замыкания), права страницы вторым запросом; фильтр по праву нативный; членство и права слиты
- (В) связь отдельно, права отдельно: `user_note(id, user_id, note_id)` без изменений + `user_note_permission(id, user_note_id, permission)`; страница по связям (одна строка на пару — естественная пагинация), права страницы вторым запросом `findByUserNoteIdIn(ids)`; фильтр по праву — запрос к таблице прав, затем связи; в Mongo — две коллекции, не вложенный массив (вложенный массив и `@MappedCollection` JDBC отпадают — у R2DBC коллекций нет, ветки разошлись бы) — **рекомендация ассистента**: прямое продолжение шага пользователя «сначала связь, права потом», `UserNote` не меняется, одинаково на 8 листьях; цена — удаление связи = два удаления (права, затем связь): SQL — `@Transactional`, Mongo без replica set — не атомарно (различие поведения, `.withReplicaSet()` — CLAUDE.md «Точка возврата»)
- выполнение при (В), sync: `findByUserIdAndNoteIdGreaterThanOrderByNoteId(userId, after, Limit.of(limit + 1))` (+1 — есть ли следующая страница) → `findByUserNoteIdIn(ids)` → `groupingBy` по `userNoteId` → `UserNoteView` с отсортированными правами; `nextCursor` = закодированный последний `noteId` (для списка пользователей заметки — зеркально, `userId`); два запроса на страницу, не N+1; reactive — `collectList()` → `flatMap` → `findByUserNoteIdIn(ids).collectMultimap(...)` → `Mono<UserNotePage>`
- индексы: `UNIQUE (user_id, note_id)` (заметки пользователя), `(note_id, user_id)` (пользователи заметки), `(user_note_id)` в таблице прав; в Mongo — те же составные
- не проверено: параметр `Limit` и `…In(List<UUID>)` в производных запросах JDBC/R2DBC/Mongo на байткоде 4.1.1; `In` с UUID у MySQL зависит от типа колонки; ⚠️ порядок сортировки UUID может различаться по вендорам (PostgreSQL `uuid` — побайтово, MySQL `CHAR(36)` — как строка, Mongo — порядок бинарных значений, H2 — не проверено) — одна и та же страница вернёт разные строки; кандидат во «враждебные данные» варианта Б центрального вопроса
- открыто: кто видит списки (свой список — видимо, только сам пользователь: `/users/me/notes`, `userId` из JWT; список пользователей заметки — какое право нужно, не решено)

### Пагинация — курсор (рекомендация ассистента)

- keyset: «следующие N после последнего ключа», в ответе непрозрачная строка `nextCursor`; одинакова для REST, gRPC (`page_token`/`next_page_token`, Google AIP-158), GraphQL (Relay connections) — новый транспорт не меняет use case; не сдвигается при вставках между страницами, не замедляется на дальних страницах (в отличие от `OFFSET`); формат курсора — внутреннее дело адаптера
- обогащение (`name`, `content`) — не в `user-note`: вызовы чужих сервисов изнутри `user-note` дают связанность во время выполнения и дубль логики компоновки; `note`/`user` сейчас удалены — честно реализуемо только отдавать идентификаторы; строки удалённых заметок до появления события `NoteDeleted` отбрасывает тот, кто обогащает

### Задел на другие транспорты (пользователь: «пока REST, в дальнейшем рассмотри и другие варианты»)

- use case и модели не зависят от транспорта — новый вариант = новый driving-модуль рядом с `controller-*`, ядро не меняется
- gRPC — RPC с `page_token` или server streaming (для reactive `Flux` ложится естественно)
- GraphQL — Relay connection с тем же курсором; обогащение через federation (`user-note` расширяет тип `Note` полем пользователей, шлюз склеивает сервисы); N+1 при обогащении — пакетная загрузка (DataLoader)
- события — `user-note` публикует изменения связей/прав, другие сервисы держат свою проекцию «кто что видит» (CQRS): чтение переезжает к потребителю, ценой брокера и окна рассинхронизации
- BFF — место компоновки для REST-клиентов (в CLAUDE.md `bff/` — «НЕ СОЗДАНО»)

### Именование методов по HTTP (решение пользователя 2026-09-26, запрос «ближе к CRUD или даже к HTTP»)

- рекомендация ассистента, принятая в дальнейших эскизах: интерфейс «глагол + ресурс», метод — глаголом HTTP (`get`/`put`/`delete`), коллекции — `list`; ориентир — стандартные методы Google AIP-131–135 (Get/List/Create/Update/Delete), одинаково ложатся на REST и gRPC
- HTTP точнее CRUD: выдача/создание связи по паре из пути = `PUT` (создать или заменить, идемпотентно), в CRUD пришлось бы делить на create/update; `list`, а не `get`, для коллекций — в AIP и gRPC это разные стандартные методы
- `CheckAccess.check` — не операция над ресурсом, а предусловие других use case, наружу не выходит — имя доменное (в AIP — «пользовательский метод», AIP-136)
- цена: из кода уходят доменные слова «выдать»/«отозвать» — сохранять их в документации; порт хранилища (driven) сохраняет словарь Spring Data (`find`/`save`/`delete`) — входные порты говорят языком клиента, выходные — языком хранилища

### Эскизы интерфейсов и контроллеров с правами (до решения «отложить `Permissions`»)

- sync-набор для контроллера (`contract`): `GetAccess.get(AccessTarget)`, `PutAccess.put(AccessChange)`, `DeleteAccess.delete(AccessTarget)`, `ListNoteAccesses.list(NoteAccessesQuery)`, `ListUserAccesses.list(UserAccessesQuery)`; модели `AccessKey(userId, noteId)`, `AccessTarget(actorId, AccessKey key)` (вход `get` и `delete` — совпали по форме и смыслу, заменил `AccessRevoke`), `AccessChange(actorId, AccessKey key, Set<Permission> permissions)`, `PageQuery`, `UserAccessesQuery(actorId, Permission permission, PageQuery page)` (`permission` по умолчанию `READ`), `NoteAccessesQuery(actorId, noteId, PageQuery page)`, `AccessView`, `AccessPage(List<AccessView> items, String nextCursor)`; исключения `NoteNotFoundException` 404, `PermissionDeniedException` 403, `AccessConflictException` 409; `CheckAccess` — внутренний, контроллер не видит; `/access-model` — чистая функция, не use case
- reactive-набор (`contract-reactive`, другой пакет — иначе столкновение полных имён в reactive-листе, какой — не решено): те же интерфейсы с `Mono<…>`, модели и исключения из `contract`; вход — обычное значение, не `Mono<Input>` (WebFlux сам разбирает `@RequestBody`; интерфейсы sync/reactive отличаются только возвращаемым типом); списки — `Mono<AccessPage>`, не `Flux` (в `Flux` некуда положить `nextCursor`; потоковая выдача NDJSON/SSE — отдельный use case позже); правило контракта: `get`/`put`/`check` никогда не завершаются пусто, пустое завершение — только `Mono<Void>` у `delete`; проверка — общий тест через `StepVerifier`; BlockHound (агент Reactor против блокирующих вызовов) — отдельное решение (ещё один Java-агент при цели «ноль предупреждений»)
- контроллер: существующий `UserNoteController` в обоих `controller-*` (без переименования); собирает вход из `@CurrentUserId` (мета-аннотация `@AuthenticationPrincipal(expression = "T(java.util.UUID).fromString(subject)")`), `@PathVariable`, `PermissionsBody(Set<Permission> permissions)`, `@RequestParam(required = false) String cursor`, `@RequestParam(defaultValue = "50") int limit`, `@RequestParam(defaultValue = "READ") Permission permission`; `ResponseEntity` не нужен (200 — возвращаемое значение, 204 — `@ResponseStatus(HttpStatus.NO_CONTENT)`); `/users/me/notes` — `userId` = `actorId`, не из пути; модули различаются только `Mono<…>`, пакетом интерфейсов и конфигурацией безопасности; `@CurrentUserId`, `PermissionsBody` и `@RestControllerAdvice` побайтово одинаковы в обоих модулях (вопрос «где живут общие классы двух `controller-*`»)

### Вне рамок — CRUD заметки (поправка пользователя 2026-09-26: «мы сейчас говорим только про UserNote!»)

- ассистент предложил `PostNote`/`GetNote`/`PutNote`/`PatchNote`/`DeleteNote`, `NoteController` и вопрос «(а) отдельный сервис заметок + `user-note` как центральный сервис доступа (Zanzibar) или (б) заметка и её доступ в одном сервисе (как Google Drive API, права — подресурс файла)» с рекомендацией (б) — пользователь остановил: рассматривается только `UserNote`; не поднимать без запроса пользователя (заметки — сервис `note/`, удалён 2026-08-29)

### Unix — уровни и права (справка по запросу пользователя 2026-09-26)

- уровни user/group/other, проверка по первому совпадению, не объединение (владелец с `---` не читает свой файл даже при `o=r`); права `r`(4)/`w`(2)/`x`(1), для каталога — список имён / создание-удаление записей (только вместе с `x`) / проход; права независимы (`-w-` допустимо); спецбиты setuid (4000), setgid (2000, у каталога — наследование группы), sticky (1000, `/tmp`); `umask` (022 → 644/755); `root` без проверок `r`/`w`, в Linux разделён на capabilities; POSIX ACL (`setfacl`/`getfacl`, `mask`, `+` в `ls -l`, ACL по умолчанию для каталогов); macOS ACL (`chmod +a`, `ls -le`); SELinux/AppArmor — мандатный слой поверх — дополняет раздел «Аналоги из индустрии»

### Сервисы и их ответственность (вопросы пользователя 2026-09-26: какой сервис за что отвечает, сколько сервисов нужно, `note` и `user` физически разделены архитектурными границами)

Консультация, решение не принято; `note/`·`user/` удалены из репозитория 2026-08-29, возврат — по отдельному решению — это целевая картина, не план немедленной работы.

- доменные сервисы — минимум 3, у каждого своя база и своё развёртывание: `user` — профиль `(id, name)`, одиночное чтение и пакет по id (`GET /users?ids=…`), о заметках и правах не знает; `note` — `(id, content)`, одиночное чтение и пакет по id (`GET /notes?ids=…`), прав не хранит, но перед выдачей содержимого проверяет доступ через `user-note`; `user-note` — связи и права, сервис авторизации для заметок (модель Zanzibar): «может ли X сделать Y с заметкой Z» + два постраничных списка идентификаторов с правами, без имён и содержимого
- сервер авторизации (JWT) — внешний (Keycloak) или свой на Spring Authorization Server (место бывшего `auth/`); сервисы только проверяют подпись (OAuth2 resource server), `sub` = userId
- BFF — компоновка экранов из нескольких сервисов; на старте может заменить клиент
- список «заметки пользователя с правами»: клиент → BFF `GET /me/notes` → `user-note` `GET /users/me/notes?cursor=…` (страница `noteId`+права) → `note` `GET /notes?ids=…` одним пакетом (`content`) → склейка по `noteId`, отсутствующие в ответе `note` id (удалены, событие не дошло) отбрасываются
- список «пользователи заметки с правами»: BFF → `user-note` `GET /notes/{noteId}/users` (сам проверяет доступ вызывающего) → `user` `GET /users?ids=…` (`name`) → склейка по `userId`; два сетевых вызова на страницу, не N+1; порядок и курсор задаёт `user-note`
- альтернатива без BFF: `user-note` держит локальную копию нужных полей (`name`, начало `content`) по событиям `UserUpdated`/`NoteUpdated` — один вызов, без связанности во время выполнения, ценой брокера, окна рассинхронизации и копии чужих данных (полный `content` копировать тяжело) — следующий шаг, не старт
- чтение заметки: `note` синхронно спрашивает `user-note` о `READ` для `sub` (цена — недоступен `user-note` → заметки не читаются); позже — локальная копия прав в `note` по событиям `AccessGranted`/`AccessRevoked`
- создание заметки: две базы, общей транзакции нет — синхронный вызов `user-note` с компенсацией (удалить заметку при сбое) или событие `NoteCreated` → `user-note` создаёт доступ владельца (окно, когда у создателя ещё нет доступа); надёжная публикация — transactional outbox
- удаление заметки/пользователя: `NoteDeleted`/`UserDeleted` → `user-note` удаляет связи; до событий сирот отсекает BFF при склейке
- физическая граница: своя база у каждого сервиса (без общих таблиц/FK/JOIN, чужую базу не читать); отдельный `bootJar`/образ/жизненный цикл; ни одной зависимости на код другого сервиса — только HTTP/события, свои модели API у каждого (одинаковые по форме DTO — разные классы, общая библиотека DTO связала бы сборки и версии); механическая проверка — grep по `build.gradle.kts` на `project(":user:…")` из `note/*` и наоборот (образец — leaf-purity-grep CLAUDE.md), сильнее — отдельная сборка на сервис через `includeBuild(...)` (чужой модуль не резолвится) — CLAUDE.md → «Открытые решения» → «Composite build на границе сервисов», три взаимодействующих сервиса — недостающий там повод
- рекомендация ассистента: `user`, `note`, `user-note` + сервер авторизации; компоновка списков в BFF; проверка доступа в `note` — синхронный вызов `user-note` на старте; события и outbox — следующий шаг; граница — `includeBuild`

### Прагматичный набор сервисов на стандартных возможностях Spring (запрос пользователя 2026-09-26: максимум автоконфигурации и настроек по умолчанию — Spring Data, Security, WebMVC, WebFlux, Cloud и др.)

Имена стартеров сверены с `docs/spring-boot-starters-reference.md` 2026-09-26; непроверенное помечено.

- обязательные Spring-приложения — 5:
  - `auth` — `spring-boot-starter-security-oauth2-authorization-server`: вход, JWT, JWKS; клиенты — свойствами `spring.security.oauth2.authorizationserver.*`; учётные данные, не профиль (объединить с `user` — прагматично, но это открытый пункт CLAUDE.md «Регистрация `auth/`↔`user/`»)
  - `gateway` — `spring-cloud-starter-gateway-server-webflux` (или `-server-webmvc`) + `spring-boot-starter-security-oauth2-client`: маршруты свойствами, вход authorization code, фильтр `TokenRelay` передаёт JWT в сервисы, браузер держит сессионную cookie без токенов (паттерн BFF); компоновка списков — единственный свой код, обычные контроллеры в том же приложении (маршрутами gateway не склеивает)
  - `user` — веб-стек + `spring-boot-starter-data-*` + `spring-boot-starter-security-oauth2-resource-server`; защита — одно свойство `spring.security.oauth2.resourceserver.jwt.issuer-uri`
  - `note` — то же; вызов `user-note` — HTTP interface (`@HttpExchange`, Spring Framework 7), автоконфигурация групп клиентов в Boot 4 есть, имена свойств по метаданным не сверены; OpenFeign (`spring-cloud-starter-openfeign`) — в режиме поддержки, для нового кода Spring рекомендует HTTP interfaces; токен — тот же bearer пользователя или сервисный через `oauth2-client` (client credentials)
  - `user-note` — то же + слушатель событий; внутри — 8 листьев по вендорам, снаружи — один сервис с одним API
- опциональные — 2: `registry` — `spring-cloud-starter-netflix-eureka-server` (+ в сервисах `-eureka-client` и `spring-cloud-starter-loadbalancer`, адреса `lb://user-note`) — нужен без Kubernetes (там DNS сервисов делает то же); `config` — `spring-cloud-config-server` (+ `spring-cloud-starter-config`) — прагматично не ставить (свой `application.properties` + переменные окружения), имеет смысл при многих сервисах и смене настроек без пересборки, тогда рядом `spring-cloud-bus`
- события: Spring Cloud Stream (`spring-cloud-stream` + `spring-cloud-stream-binder-kafka` или `-rabbit`), слушатель — бин `Consumer<NoteDeleted>`, привязка свойствами; outbox — Spring Modulith event publication registry (событие в той же транзакции, что данные): в справочнике Initializr реестры только `spring-modulith-starter-{jdbc,jpa,mongodb,neo4j}` — **для R2DBC нет**, reactive SQL-листьям `user-note` outbox решать иначе (новое расхождение по дереву вариантов); вынос из Modulith в Kafka/AMQP в локальном справочнике не значится (есть `spring-modulith-events-jms`) — не проверено
- устойчивость: `spring-cloud-starter-circuitbreaker-reactor-resilience4j` на вызове `note → user-note` (опционально; без него недоступный `user-note` ломает чтение заметок)
- наблюдаемость: `spring-boot-starter-actuator` (уже на каждом листе), `spring-boot-starter-opentelemetry` (сквозная трассировка через gateway и сервисы, заголовки автоматически), `micrometer-registry-otlp`; разработка — `spring-boot-docker-compose` (`bootRun` поднимает базы и брокер из `compose.yaml`, п. 2 плана CLAUDE.md)
- инфраструктура (не Spring): база на каждый доменный сервис и `auth`, брокер Kafka или RabbitMQ, приёмник трассировки (OpenTelemetry Collector/Zipkin/Grafana)
- итог: прагматичный минимум — 5 приложений (`auth`, `gateway`, `user`, `note`, `user-note`) + брокер + базы; `registry`/`config` — только без Kubernetes или при многих настройках; свой инфраструктурный код — компоновка списков в `gateway` и outbox для R2DBC-листьев

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
