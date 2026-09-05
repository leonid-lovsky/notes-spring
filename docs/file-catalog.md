# Каталог файлов проекта

> Вынесено из `CLAUDE.md` 2026-07-14 (см. `CLAUDE.md` → «Правила» → «Лимит размера файла»). Полный git-отслеживаемый список файлов; статусы: [DONE] — утверждено, [REVIEW] — требует пересмотра (по умолчанию), [ADD] — файла нет, предложен, [REMOVED] — удалён (строка сохранена для истории). Список путей/статусов не трогать без прямого запроса (см. `CLAUDE.md` → «Правила» → «Лимит размера файла»).

Схема каждой строки: путь (относительно ближайшего заголовка `### сервис/`/`#### модуль/` выше по файлу) — [СТАТУС] — комментарий (опционален).

### Корень репозитория (8 файлов)
- `settings.gradle.kts` — [DONE]
- `gradlew.bat` — [DONE]
- `gradlew` — [DONE]
- `gradle.properties` — [DONE]
- `CLAUDE.md` — [DONE] — единственный документационный файл в корне, обязательное условие автозагрузки Claude Code; остальные документационные файлы — в каталоге `docs/` (см. разделы «Корень репозитория» и `docs/` ниже в этом же файле)
- `.java-version` — [DONE]
- `.springjavaformatconfig` — [REMOVED]
- `.gitignore` — [DONE]
- `.gitattributes` — [DONE]

### docs/ (8 файлов, перенесены из корня 2026-07-24 — реструктуризация документации под 100%-доступность для ИИ-агента, см. CLAUDE.md → «Правила» → «Документация — только для ИИ-агента, не для человека»)
- `docs/db-migration-tools-reference.md` — [REVIEW]
- `docs/decisions-log.md` — [REVIEW] — новый 2026-07-23, вынесен из CLAUDE.md → «Принятые решения» (снятие объёма, AI-readability)
- `docs/tech-glossary.md` — [REVIEW] — новый 2026-07-23, вынесен из CLAUDE.md → «Технологии» (снятие объёма, AI-readability)
- `docs/file-catalog.md` — [REVIEW] — пропущен в собственном каталоге, добавлен 2026-07-23 при попутной правке
- `docs/spring-boot-starters-reference.md` — [REVIEW] — пропущен в каталоге, добавлен 2026-07-23 при попутной правке
- `docs/google-docs-full-model.md` — [REVIEW] — новый 2026-07-23, набросок требований к совместному редактированию (см. CLAUDE.md → «Открытые решения»), не решение
- `docs/convention-plugins-graph.md` — [REVIEW] — новый 2026-08-01, снимок текущего графа родитель→потомок convention-плагинов (не история — та в decisions-log.md), запрошено пользователем для облегчения навигации по build-logic/
- `docs/spring-boot-starters-full-matrix.md` — [REVIEW] — новый 2026-08-02, сырой снимок ВСЕХ 205 зависимостей Initializr (не куратированный список, в отличие от `spring-boot-starters-reference.md`) по 4 комбинациям Boot 4.0.7/4.1.0 × Java 21/25, получен напрямую с `start.spring.io/build.gradle` по запросу пользователя
- `docs/spring-boot-testing-reference.md` — [REVIEW] — новый 2026-08-18, справочник тестовых уровней/аннотаций/классов Boot 4.1 с датами появления, собран для TDD-переписывания (запрошено пользователем)

### .claude/ (0 файлов, добавлено 2026-07-24, весь каталог удалён из репозитория и с диска в тот же день)
- `.claude/settings.json` — [REMOVED] — регистрировал SessionStart hook; удалён 2026-07-24 вместе со всем `.claude/` (`.gitignore` уже содержал `.claude/` — по факту был случайно закоммичен ранее, теперь untracked навсегда)
- `.claude/hooks/session-start.sh` — [REMOVED] — автоустановка JDK 25 в облачных сессиях; удалён 2026-07-24 из-за CRLF-порчи файла на диске (`bad interpreter` при старте сессии), не восстановлен

### user-note/ (48 файлов, было 43 — +5: новые `data-{jdbc,jpa,mongodb,mongodb-reactive,r2dbc}/build.gradle.kts` 2026-09-05) — весь гексагональный слой (`domain/`·`persistence/`·`presentation/`·`application/application-{vendor}-{tech}/`) + сервисы `note/`·`user/` удалены целиком 2026-08-29 (`git rm`, см. CLAUDE.md → «Активная нить» → «2026-08-29 (вечер)»); 5 composition-root модулей теперь подключают соответствующий `data-*`-модуль через `dependencies {}`

#### user-note/ — удалённый гексагональный слой — [REMOVED] 2026-08-29
- `user-note/domain/{domain,contract,contract-reactive}/**` — [REMOVED] — records/enums/exceptions + порт-интерфейсы sync+reactive
- `user-note/persistence/data-{jdbc,jpa,mongodb,mongodb-reactive,r2dbc}/**` — [REMOVED] — driven-адаптеры/мапперы/entity/repository + `schema.sql`
- `user-note/presentation/{webmvc,webflux}/**` — [REMOVED] — driving-адаптеры (контроллеры, exception handler, порт-интерфейсы)
- `user-note/application/**` — [REMOVED] — старые composition-root модули (11 вложенных `application-{vendor}-{tech}/` + `application-mongodb[-reactive]/`)

#### user-note/application-jdbc/ (9 файлов, было 7) — sync реляционный скелет: webmvc + data-jdbc + database-{h2,mysql,postgresql} + testcontainers-{mysql,postgresql}
- `user-note/application-jdbc/build.gradle.kts` — [REVIEW]
- `user-note/application-jdbc/src/main/java/com/example/usernote/UserNoteApplication.java` — [REVIEW] — голый `@SpringBootApplication`
- `user-note/application-jdbc/src/main/java/com/example/usernote/package-info.java` — [REVIEW]
- `user-note/application-jdbc/src/main/resources/application.properties` — [REVIEW] — только `spring.mvc.problemdetails.enabled=true`
- `user-note/application-jdbc/src/main/resources/application-h2.properties` — [REMOVED] 2026-08-31 (было `spring.h2.console.enabled=true`)
- `user-note/application-jdbc/src/main/resources/application-mysql.properties` — [REMOVED] 2026-08-31 (connection-строки, в тестах перекрывались `@ServiceConnection`)
- `user-note/application-jdbc/src/main/resources/application-postgresql.properties` — [REMOVED] 2026-08-31
- `user-note/application-jdbc/src/test/java/com/example/usernote/UserNoteApplicationTests.java` — [REMOVED] 2026-09-04 (было `@Nested` h2/mysql/postgresql через `abstract DataSourceTests`)
- `user-note/application-jdbc/src/test/java/com/example/usernote/UserNoteApplicationDefaultTest.java` — [REVIEW] — новый 2026-09-04, голый `contextLoads(){}` без активного профиля (embedded H2)
- `user-note/application-jdbc/src/test/java/com/example/usernote/UserNoteApplicationMySQLTest.java` — [REVIEW] — новый 2026-09-04, `@ActiveProfiles("MySQL")`, голый `contextLoads(){}` (ассерт на `DataSource`/product-name снят)
- `user-note/application-jdbc/src/test/java/com/example/usernote/UserNoteApplicationPostgreSQLTest.java` — [REVIEW] — новый 2026-09-04, `@ActiveProfiles("PostgreSQL")`
- `user-note/application-jdbc/src/test/java/com/example/usernote/UserNoteTestApplication.java` — [REVIEW]
- `user-note/application-jdbc/src/test/java/com/example/usernote/UserNoteTestConfiguration.java` — [REVIEW] — `@Bean @Profile @ServiceConnection` MySQLContainer/PostgreSQLContainer

#### user-note/application-jpa/ (9 файлов, было 7) — sync реляционный скелет: то же, что application-jdbc/, но data-jpa вместо data-jdbc (единственное различие)
- `user-note/application-jpa/build.gradle.kts` — [REVIEW]
- `user-note/application-jpa/src/main/java/com/example/usernote/UserNoteApplication.java` — [REVIEW] — голый `@SpringBootApplication`
- `user-note/application-jpa/src/main/java/com/example/usernote/package-info.java` — [REVIEW]
- `user-note/application-jpa/src/main/resources/application.properties` — [REVIEW] — только `spring.mvc.problemdetails.enabled=true`
- `user-note/application-jpa/src/main/resources/application-h2.properties` — [REMOVED] 2026-08-31 (было `spring.h2.console.enabled=true`)
- `user-note/application-jpa/src/main/resources/application-mysql.properties` — [REMOVED] 2026-08-31 (connection-строки, в тестах перекрывались `@ServiceConnection`)
- `user-note/application-jpa/src/main/resources/application-postgresql.properties` — [REMOVED] 2026-08-31
- `user-note/application-jpa/src/test/java/com/example/usernote/UserNoteApplicationTests.java` — [REMOVED] 2026-09-04 (было `@Nested` h2/mysql/postgresql через `abstract DataSourceTests`)
- `user-note/application-jpa/src/test/java/com/example/usernote/UserNoteApplicationDefaultTest.java` — [REVIEW] — новый 2026-09-04, голый `contextLoads(){}` без активного профиля (embedded H2)
- `user-note/application-jpa/src/test/java/com/example/usernote/UserNoteApplicationMySQLTest.java` — [REVIEW] — новый 2026-09-04, `@ActiveProfiles("MySQL")`
- `user-note/application-jpa/src/test/java/com/example/usernote/UserNoteApplicationPostgreSQLTest.java` — [REVIEW] — новый 2026-09-04, `@ActiveProfiles("PostgreSQL")`
- `user-note/application-jpa/src/test/java/com/example/usernote/UserNoteTestApplication.java` — [REVIEW]
- `user-note/application-jpa/src/test/java/com/example/usernote/UserNoteTestConfiguration.java` — [REVIEW] — `@Bean @Profile @ServiceConnection` MySQLContainer/PostgreSQLContainer

#### user-note/application-mongodb/ (8 файлов, было 7) — sync Mongo скелет: webmvc + data-mongodb + testcontainers-mongodb
- `user-note/application-mongodb/build.gradle.kts` — [REVIEW]
- `user-note/application-mongodb/src/main/java/com/example/usernote/UserNoteApplication.java` — [REVIEW] — голый `@SpringBootApplication`
- `user-note/application-mongodb/src/main/java/com/example/usernote/package-info.java` — [REVIEW]
- `user-note/application-mongodb/src/main/resources/application.properties` — [REVIEW] — только `spring.mvc.problemdetails.enabled=true`
- `user-note/application-mongodb/src/main/resources/application-mongodb.properties` — [REMOVED] 2026-08-31 (`spring.mongodb.*` connection-строки, в тестах перекрывались `@ServiceConnection`)
- `user-note/application-mongodb/src/test/java/com/example/usernote/UserNoteApplicationTests.java` — [REMOVED] 2026-09-04 (было `@Nested @ActiveProfiles("mongodb")` через `abstract MongoTemplateTests`)
- `user-note/application-mongodb/src/test/java/com/example/usernote/UserNoteApplicationDefaultTest.java` — [REVIEW] — новый 2026-09-04, голый `contextLoads(){}` без активного профиля
- `user-note/application-mongodb/src/test/java/com/example/usernote/UserNoteApplicationMongoDBTest.java` — [REVIEW] — новый 2026-09-04, `@ActiveProfiles("MongoDB")`
- `user-note/application-mongodb/src/test/java/com/example/usernote/UserNoteTestApplication.java` — [REVIEW]
- `user-note/application-mongodb/src/test/java/com/example/usernote/UserNoteTestConfiguration.java` — [REVIEW] — `@Bean @Profile("mongodb") @ServiceConnection MongoDBContainer`

#### user-note/application-mongodb-reactive/ (8 файлов, было 7) — reactive Mongo скелет: webflux + data-mongodb-reactive + testcontainers-mongodb
- `user-note/application-mongodb-reactive/build.gradle.kts` — [REVIEW]
- `user-note/application-mongodb-reactive/src/main/java/com/example/usernote/UserNoteApplication.java` — [REVIEW] — голый `@SpringBootApplication`
- `user-note/application-mongodb-reactive/src/main/java/com/example/usernote/package-info.java` — [REVIEW]
- `user-note/application-mongodb-reactive/src/main/resources/application.properties` — [REVIEW] — только `spring.webflux.problemdetails.enabled=true`
- `user-note/application-mongodb-reactive/src/main/resources/application-mongodb.properties` — [REMOVED] 2026-08-31 (`spring.mongodb.*` connection-строки, в тестах перекрывались `@ServiceConnection`)
- `user-note/application-mongodb-reactive/src/test/java/com/example/usernote/UserNoteApplicationTests.java` — [REMOVED] 2026-09-04 (было `@Nested @ActiveProfiles("mongodb")` через `abstract ReactiveMongoTemplateTests`)
- `user-note/application-mongodb-reactive/src/test/java/com/example/usernote/UserNoteApplicationDefaultTest.java` — [REVIEW] — новый 2026-09-04, голый `contextLoads(){}` без активного профиля
- `user-note/application-mongodb-reactive/src/test/java/com/example/usernote/UserNoteApplicationMongoDBTest.java` — [REVIEW] — новый 2026-09-04, `@ActiveProfiles("MongoDB")`
- `user-note/application-mongodb-reactive/src/test/java/com/example/usernote/UserNoteTestApplication.java` — [REVIEW]
- `user-note/application-mongodb-reactive/src/test/java/com/example/usernote/UserNoteTestConfiguration.java` — [REVIEW] — `@Bean @Profile("mongodb") @ServiceConnection MongoDBContainer`

#### user-note/application-r2dbc/ (9 файлов, было 7) — reactive реляционный скелет: webflux + data-r2dbc + database-r2dbc-{h2,mysql,postgresql} + testcontainers(+-r2dbc)-{mysql,postgresql}
- `user-note/application-r2dbc/build.gradle.kts` — [REVIEW]
- `user-note/application-r2dbc/src/main/java/com/example/usernote/UserNoteApplication.java` — [REVIEW] — голый `@SpringBootApplication`
- `user-note/application-r2dbc/src/main/java/com/example/usernote/package-info.java` — [REVIEW]
- `user-note/application-r2dbc/src/main/resources/application.properties` — [REVIEW] — только `spring.webflux.problemdetails.enabled=true`
- `user-note/application-r2dbc/src/main/resources/application-mysql.properties` — [REMOVED] 2026-08-31 (`spring.r2dbc.*` connection-строки, в тестах перекрывались `@ServiceConnection`)
- `user-note/application-r2dbc/src/main/resources/application-postgresql.properties` — [REMOVED] 2026-08-31
- `user-note/application-r2dbc/src/test/java/com/example/usernote/UserNoteApplicationTests.java` — [REMOVED] 2026-09-04 (было `@Nested` h2/mysql/postgresql через `abstract DatabaseClientTests`, ассерт `.contains`); переименован (git rename) в `UserNoteApplicationDefaultTest.java`
- `user-note/application-r2dbc/src/test/java/com/example/usernote/UserNoteApplicationDefaultTest.java` — [REVIEW] — новый 2026-09-04, голый `contextLoads(){}` без активного профиля (embedded H2)
- `user-note/application-r2dbc/src/test/java/com/example/usernote/UserNoteApplicationMySQLTest.java` — [REVIEW] — новый 2026-09-04, `@ActiveProfiles("MySQL")`
- `user-note/application-r2dbc/src/test/java/com/example/usernote/UserNoteApplicationPostgreSQLTest.java` — [REVIEW] — новый 2026-09-04, `@ActiveProfiles("PostgreSQL")`
- `user-note/application-r2dbc/src/test/java/com/example/usernote/UserNoteTestApplication.java` — [REVIEW]
- `user-note/application-r2dbc/src/test/java/com/example/usernote/UserNoteTestConfiguration.java` — [REVIEW] — `@Bean @Profile @ServiceConnection` MySQLContainer/PostgreSQLContainer (в R2DBC-модуле дают `R2dbcConnectionDetails`)

#### user-note/data-{jdbc,jpa,mongodb,mongodb-reactive,r2dbc}/ (5 файлов) — новые 2026-09-05, по одному на driven-технологию, каждый только `id("com.example.spring-boot-data-{tech}")`, кода нет; включены в `settings.gradle.kts` и подключены к соответствующему `application-*` через `implementation(project(":user-note:data-{tech}"))` (не typesafe-accessor — см. открытый вопрос в «Правила» про расхождение с leaf-purity-грепом)
- `user-note/data-jdbc/build.gradle.kts` — [REVIEW]
- `user-note/data-jpa/build.gradle.kts` — [REVIEW]
- `user-note/data-mongodb/build.gradle.kts` — [REVIEW]
- `user-note/data-mongodb-reactive/build.gradle.kts` — [REVIEW]
- `user-note/data-r2dbc/build.gradle.kts` — [REVIEW]

### user/ (210 файлов, было 150 — +60: 9 вендорных application-*-{h2,mysql,postgresql}/ модулей заменили 3 профильных 2026-07-24)

#### user/presentation/webmvc/ (5 файлов)
- `user/presentation/webmvc/build.gradle.kts` — [DONE]
- `user/presentation/webmvc/src/main/java/com/example/user/webmvc/package-info.java` — [DONE]
- `user/presentation/webmvc/src/main/java/com/example/user/webmvc/UserExceptionHandler.java` — [REVIEW]
- `user/presentation/webmvc/src/main/java/com/example/user/webmvc/UserControllerInterface.java` — [REVIEW]
- `user/presentation/webmvc/src/main/java/com/example/user/webmvc/UserController.java` — [REVIEW]

#### user/presentation/webflux/ (5 файлов)
- `user/presentation/webflux/build.gradle.kts` — [DONE]
- `user/presentation/webflux/src/main/java/com/example/user/webflux/package-info.java` — [DONE]
- `user/presentation/webflux/src/main/java/com/example/user/webflux/UserExceptionHandler.java` — [REVIEW]
- `user/presentation/webflux/src/main/java/com/example/user/webflux/UserControllerReactiveInterface.java` — [REVIEW]
- `user/presentation/webflux/src/main/java/com/example/user/webflux/UserController.java` — [REVIEW]

#### user/domain/domain/ (6 файлов)
- `user/domain/domain/build.gradle.kts` — [REVIEW]
- `user/domain/domain/src/main/java/com/example/user/domain/package-info.java` — [DONE]
- `user/domain/domain/src/main/java/com/example/user/domain/UserResponse.java` — [REVIEW]
- `user/domain/domain/src/main/java/com/example/user/domain/UserRequest.java` — [REVIEW]
- `user/domain/domain/src/main/java/com/example/user/domain/UserNotFoundException.java` — [REVIEW]
- `user/domain/domain/src/main/java/com/example/user/domain/UserPersistable.java` — [REVIEW]

#### user/persistence/data-r2dbc/ (11 файлов)
- `user/persistence/data-r2dbc/build.gradle.kts` — [DONE]
- `user/persistence/data-r2dbc/src/main/java/com/example/user/data/r2dbc/repository/package-info.java` — [DONE]
- `user/persistence/data-r2dbc/src/main/java/com/example/user/data/r2dbc/repository/UserR2dbcRepository.java` — [REVIEW]
- `user/persistence/data-r2dbc/src/main/java/com/example/user/data/r2dbc/model/package-info.java` — [DONE]
- `user/persistence/data-r2dbc/src/main/java/com/example/user/data/r2dbc/model/UserR2dbcEntity.java` — [REVIEW]
- `user/persistence/data-r2dbc/src/main/resources/schema.sql` — [REVIEW]
- `user/persistence/data-r2dbc/src/main/java/com/example/user/data/r2dbc/mapper/package-info.java` — [DONE]
- `user/persistence/data-r2dbc/src/main/java/com/example/user/data/r2dbc/mapper/UserR2dbcMapperContract.java` — [REVIEW]
- `user/persistence/data-r2dbc/src/main/java/com/example/user/data/r2dbc/mapper/UserR2dbcMapper.java` — [REVIEW]
- `user/persistence/data-r2dbc/src/main/java/com/example/user/data/r2dbc/adapter/package-info.java` — [DONE]
- `user/persistence/data-r2dbc/src/main/java/com/example/user/data/r2dbc/adapter/UserService.java` — [REVIEW]

#### user/persistence/data-mongodb-reactive/ (10 файлов)
- `user/persistence/data-mongodb-reactive/build.gradle.kts` — [DONE]
- `user/persistence/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/repository/package-info.java` — [DONE]
- `user/persistence/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/repository/UserMongoReactiveRepository.java` — [REVIEW]
- `user/persistence/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/model/package-info.java` — [DONE]
- `user/persistence/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/model/UserReactiveDocument.java` — [REVIEW]
- `user/persistence/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/mapper/package-info.java` — [DONE]
- `user/persistence/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/mapper/UserMongoReactiveMapperContract.java` — [REVIEW]
- `user/persistence/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/mapper/UserMongoReactiveMapper.java` — [REVIEW]
- `user/persistence/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/adapter/package-info.java` — [DONE]
- `user/persistence/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/adapter/UserService.java` — [REVIEW]

#### user/persistence/data-mongodb/ (10 файлов)
- `user/persistence/data-mongodb/build.gradle.kts` — [DONE]
- `user/persistence/data-mongodb/src/main/java/com/example/user/data/mongodb/repository/package-info.java` — [DONE]
- `user/persistence/data-mongodb/src/main/java/com/example/user/data/mongodb/repository/UserMongoRepository.java` — [REVIEW]
- `user/persistence/data-mongodb/src/main/java/com/example/user/data/mongodb/model/package-info.java` — [DONE]
- `user/persistence/data-mongodb/src/main/java/com/example/user/data/mongodb/model/UserDocument.java` — [REVIEW]
- `user/persistence/data-mongodb/src/main/java/com/example/user/data/mongodb/mapper/package-info.java` — [DONE]
- `user/persistence/data-mongodb/src/main/java/com/example/user/data/mongodb/mapper/UserMongoMapperContract.java` — [REVIEW]
- `user/persistence/data-mongodb/src/main/java/com/example/user/data/mongodb/mapper/UserMongoMapper.java` — [REVIEW]
- `user/persistence/data-mongodb/src/main/java/com/example/user/data/mongodb/adapter/package-info.java` — [DONE]
- `user/persistence/data-mongodb/src/main/java/com/example/user/data/mongodb/adapter/UserService.java` — [REVIEW]

#### user/persistence/data-jpa/ (10 файлов)
- `user/persistence/data-jpa/build.gradle.kts` — [DONE]
- `user/persistence/data-jpa/src/main/java/com/example/user/data/jpa/repository/package-info.java` — [DONE]
- `user/persistence/data-jpa/src/main/java/com/example/user/data/jpa/repository/UserJpaRepository.java` — [REVIEW]
- `user/persistence/data-jpa/src/main/java/com/example/user/data/jpa/model/package-info.java` — [DONE]
- `user/persistence/data-jpa/src/main/java/com/example/user/data/jpa/model/UserEntity.java` — [REVIEW]
- `user/persistence/data-jpa/src/main/java/com/example/user/data/jpa/mapper/package-info.java` — [DONE]
- `user/persistence/data-jpa/src/main/java/com/example/user/data/jpa/mapper/UserJpaMapperContract.java` — [REVIEW]
- `user/persistence/data-jpa/src/main/java/com/example/user/data/jpa/mapper/UserJpaMapper.java` — [REVIEW]
- `user/persistence/data-jpa/src/main/java/com/example/user/data/jpa/adapter/package-info.java` — [DONE]
- `user/persistence/data-jpa/src/main/java/com/example/user/data/jpa/adapter/UserService.java` — [REVIEW]

#### user/persistence/data-jdbc/ (11 файлов)
- `user/persistence/data-jdbc/build.gradle.kts` — [DONE]
- `user/persistence/data-jdbc/src/main/java/com/example/user/data/jdbc/repository/package-info.java` — [REVIEW]
- `user/persistence/data-jdbc/src/main/java/com/example/user/data/jdbc/repository/UserJdbcRepository.java` — [REVIEW]
- `user/persistence/data-jdbc/src/main/java/com/example/user/data/jdbc/model/package-info.java` — [REVIEW]
- `user/persistence/data-jdbc/src/main/java/com/example/user/data/jdbc/model/UserJdbcEntity.java` — [REVIEW]
- `user/persistence/data-jdbc/src/main/resources/schema.sql` — [REVIEW]
- `user/persistence/data-jdbc/src/main/java/com/example/user/data/jdbc/mapper/package-info.java` — [DONE]
- `user/persistence/data-jdbc/src/main/java/com/example/user/data/jdbc/mapper/UserJdbcMapperContract.java` — [REVIEW]
- `user/persistence/data-jdbc/src/main/java/com/example/user/data/jdbc/mapper/UserJdbcMapper.java` — [REVIEW]
- `user/persistence/data-jdbc/src/main/java/com/example/user/data/jdbc/adapter/package-info.java` — [DONE]
- `user/persistence/data-jdbc/src/main/java/com/example/user/data/jdbc/adapter/UserService.java` — [REVIEW]

#### user/domain/contract-reactive/ (4 файлов)
- `user/domain/contract-reactive/build.gradle.kts` — [REVIEW]
- `user/domain/contract-reactive/src/main/java/com/example/user/contract/reactive/package-info.java` — [DONE]
- `user/domain/contract-reactive/src/main/java/com/example/user/contract/reactive/UserReactiveInterface.java` — [REVIEW]
- `user/domain/contract-reactive/src/main/java/com/example/user/contract/reactive/UserServiceReactiveInterface.java` — [REVIEW]

#### user/domain/contract/ (4 файлов)
- `user/domain/contract/build.gradle.kts` — [REVIEW]
- `user/domain/contract/src/main/java/com/example/user/contract/package-info.java` — [DONE]
- `user/domain/contract/src/main/java/com/example/user/contract/UserInterface.java` — [REVIEW]
- `user/domain/contract/src/main/java/com/example/user/contract/UserServiceInterface.java` — [REVIEW]

#### user/application-r2dbc/ (12 файлов) — [REMOVED] 2026-07-24, разбит на 3 вендорных модуля application-r2dbc-{h2,mysql,postgresql}/ — устраняет driver bloat
- `user/application-r2dbc/build.gradle.kts` — [REMOVED]
- `user/application-r2dbc/src/test/resources/application.properties` — [REMOVED]
- `user/application-r2dbc/src/test/java/com/example/user/UserR2dbcApplicationTests.java` — [REMOVED]
- `user/application-r2dbc/src/main/resources/application.properties` — [REMOVED]
- `user/application-r2dbc/src/main/java/com/example/user/package-info.java` — [REMOVED]
- `user/application-r2dbc/src/main/java/com/example/user/UserR2dbcApplication.java` — [REMOVED]
- `user/application-r2dbc/src/main/resources/application-postgresql.properties` — [REMOVED]
- `user/application-r2dbc/src/main/resources/application-mysql.properties` — [REMOVED]
- `user/application-r2dbc/compose-postgresql.yaml` — [REMOVED]
- `user/application-r2dbc/compose-mysql.yaml` — [REMOVED]
- `user/application-r2dbc/src/test/java/com/example/user/UserPostgresqlR2dbcApplicationTests.java` — [REMOVED]
- `user/application-r2dbc/src/test/java/com/example/user/UserMysqlR2dbcApplicationTests.java` — [REMOVED]

#### user/application/application-h2-r2dbc/ (6 файлов)
- `user/application/application-h2-r2dbc/build.gradle.kts` — [REVIEW] — новый 2026-07-24, вендор БД — граница модуля вместо профиля (устраняет driver bloat: один драйвер на classpath вместо трёх сразу, см. decisions-log.md)
- `user/application/application-h2-r2dbc/src/main/java/com/example/user/package-info.java` — [DONE]
- `user/application/application-h2-r2dbc/src/main/java/com/example/user/UserH2R2dbcApplication.java` — [REVIEW]
- `user/application/application-h2-r2dbc/src/main/resources/application.properties` — [REVIEW]
- `user/application/application-h2-r2dbc/src/test/resources/application.properties` — [REVIEW]
- `user/application/application-h2-r2dbc/src/test/java/com/example/user/UserH2R2dbcApplicationTests.java` — [REVIEW]

#### user/application/application-mysql-r2dbc/ (7 файлов)
- `user/application/application-mysql-r2dbc/build.gradle.kts` — [REVIEW] — новый 2026-07-24, вендор БД — граница модуля вместо профиля (устраняет driver bloat: один драйвер на classpath вместо трёх сразу, см. decisions-log.md)
- `user/application/application-mysql-r2dbc/src/main/java/com/example/user/package-info.java` — [DONE]
- `user/application/application-mysql-r2dbc/src/main/java/com/example/user/UserMysqlR2dbcApplication.java` — [REVIEW]
- `user/application/application-mysql-r2dbc/src/main/resources/application.properties` — [REVIEW]
- `user/application/application-mysql-r2dbc/compose.yaml` — [REVIEW]
- `user/application/application-mysql-r2dbc/src/test/resources/application.properties` — [REVIEW]
- `user/application/application-mysql-r2dbc/src/test/java/com/example/user/UserMysqlR2dbcApplicationTests.java` — [REVIEW]

#### user/application/application-postgresql-r2dbc/ (7 файлов)
- `user/application/application-postgresql-r2dbc/build.gradle.kts` — [REVIEW] — новый 2026-07-24, вендор БД — граница модуля вместо профиля (устраняет driver bloat: один драйвер на classpath вместо трёх сразу, см. decisions-log.md)
- `user/application/application-postgresql-r2dbc/src/main/java/com/example/user/package-info.java` — [DONE]
- `user/application/application-postgresql-r2dbc/src/main/java/com/example/user/UserPostgresqlR2dbcApplication.java` — [REVIEW]
- `user/application/application-postgresql-r2dbc/src/main/resources/application.properties` — [REVIEW]
- `user/application/application-postgresql-r2dbc/compose.yaml` — [REVIEW]
- `user/application/application-postgresql-r2dbc/src/test/resources/application.properties` — [REVIEW]
- `user/application/application-postgresql-r2dbc/src/test/java/com/example/user/UserPostgresqlR2dbcApplicationTests.java` — [REVIEW]

#### user/application-jpa/ (12 файлов) — [REMOVED] 2026-07-24, разбит на 3 вендорных модуля application-jpa-{h2,mysql,postgresql}/ — устраняет driver bloat
- `user/application-jpa/build.gradle.kts` — [REMOVED]
- `user/application-jpa/src/test/resources/application.properties` — [REMOVED]
- `user/application-jpa/src/test/java/com/example/user/UserJpaApplicationTests.java` — [REMOVED]
- `user/application-jpa/src/main/resources/application.properties` — [REMOVED]
- `user/application-jpa/src/main/java/com/example/user/package-info.java` — [REMOVED]
- `user/application-jpa/src/main/java/com/example/user/UserJpaApplication.java` — [REMOVED]
- `user/application-jpa/src/main/resources/application-postgresql.properties` — [REMOVED]
- `user/application-jpa/src/main/resources/application-mysql.properties` — [REMOVED]
- `user/application-jpa/compose-postgresql.yaml` — [REMOVED]
- `user/application-jpa/compose-mysql.yaml` — [REMOVED]
- `user/application-jpa/src/test/java/com/example/user/UserPostgresqlJpaApplicationTests.java` — [REMOVED]
- `user/application-jpa/src/test/java/com/example/user/UserMysqlJpaApplicationTests.java` — [REMOVED]

#### user/application/application-h2-jpa/ (6 файлов)
- `user/application/application-h2-jpa/build.gradle.kts` — [REVIEW] — новый 2026-07-24, вендор БД — граница модуля вместо профиля (устраняет driver bloat: один драйвер на classpath вместо трёх сразу, см. decisions-log.md)
- `user/application/application-h2-jpa/src/main/java/com/example/user/package-info.java` — [DONE]
- `user/application/application-h2-jpa/src/main/java/com/example/user/UserH2JpaApplication.java` — [REVIEW]
- `user/application/application-h2-jpa/src/main/resources/application.properties` — [REVIEW]
- `user/application/application-h2-jpa/src/test/resources/application.properties` — [REVIEW]
- `user/application/application-h2-jpa/src/test/java/com/example/user/UserH2JpaApplicationTests.java` — [REVIEW]

#### user/application/application-mysql-jpa/ (7 файлов)
- `user/application/application-mysql-jpa/build.gradle.kts` — [REVIEW] — новый 2026-07-24, вендор БД — граница модуля вместо профиля (устраняет driver bloat: один драйвер на classpath вместо трёх сразу, см. decisions-log.md)
- `user/application/application-mysql-jpa/src/main/java/com/example/user/package-info.java` — [DONE]
- `user/application/application-mysql-jpa/src/main/java/com/example/user/UserMysqlJpaApplication.java` — [REVIEW]
- `user/application/application-mysql-jpa/src/main/resources/application.properties` — [REVIEW]
- `user/application/application-mysql-jpa/compose.yaml` — [REVIEW]
- `user/application/application-mysql-jpa/src/test/resources/application.properties` — [REVIEW]
- `user/application/application-mysql-jpa/src/test/java/com/example/user/UserMysqlJpaApplicationTests.java` — [REVIEW]

#### user/application/application-postgresql-jpa/ (7 файлов)
- `user/application/application-postgresql-jpa/build.gradle.kts` — [REVIEW] — новый 2026-07-24, вендор БД — граница модуля вместо профиля (устраняет driver bloat: один драйвер на classpath вместо трёх сразу, см. decisions-log.md)
- `user/application/application-postgresql-jpa/src/main/java/com/example/user/package-info.java` — [DONE]
- `user/application/application-postgresql-jpa/src/main/java/com/example/user/UserPostgresqlJpaApplication.java` — [REVIEW]
- `user/application/application-postgresql-jpa/src/main/resources/application.properties` — [REVIEW]
- `user/application/application-postgresql-jpa/compose.yaml` — [REVIEW]
- `user/application/application-postgresql-jpa/src/test/resources/application.properties` — [REVIEW]
- `user/application/application-postgresql-jpa/src/test/java/com/example/user/UserPostgresqlJpaApplicationTests.java` — [REVIEW]

#### user/application-jdbc/ (12 файлов) — [REMOVED] 2026-07-24, разбит на 3 вендорных модуля application-jdbc-{h2,mysql,postgresql}/ — устраняет driver bloat
- `user/application-jdbc/build.gradle.kts` — [REMOVED]
- `user/application-jdbc/src/test/resources/application.properties` — [REMOVED]
- `user/application-jdbc/src/test/java/com/example/user/UserJdbcApplicationTests.java` — [REMOVED]
- `user/application-jdbc/src/main/resources/application.properties` — [REMOVED]
- `user/application-jdbc/src/main/java/com/example/user/package-info.java` — [REMOVED]
- `user/application-jdbc/src/main/java/com/example/user/UserJdbcApplication.java` — [REMOVED]
- `user/application-jdbc/src/main/resources/application-postgresql.properties` — [REMOVED]
- `user/application-jdbc/src/main/resources/application-mysql.properties` — [REMOVED]
- `user/application-jdbc/compose-postgresql.yaml` — [REMOVED]
- `user/application-jdbc/compose-mysql.yaml` — [REMOVED]
- `user/application-jdbc/src/test/java/com/example/user/UserPostgresqlJdbcApplicationTests.java` — [REMOVED]
- `user/application-jdbc/src/test/java/com/example/user/UserMysqlJdbcApplicationTests.java` — [REMOVED]

#### user/application/application-h2-jdbc/ (6 файлов)
- `user/application/application-h2-jdbc/build.gradle.kts` — [REVIEW] — новый 2026-07-24, вендор БД — граница модуля вместо профиля (устраняет driver bloat: один драйвер на classpath вместо трёх сразу, см. decisions-log.md)
- `user/application/application-h2-jdbc/src/main/java/com/example/user/package-info.java` — [DONE]
- `user/application/application-h2-jdbc/src/main/java/com/example/user/UserH2JdbcApplication.java` — [REVIEW]
- `user/application/application-h2-jdbc/src/main/resources/application.properties` — [REVIEW]
- `user/application/application-h2-jdbc/src/test/resources/application.properties` — [REVIEW]
- `user/application/application-h2-jdbc/src/test/java/com/example/user/UserH2JdbcApplicationTests.java` — [REVIEW]

#### user/application/application-mysql-jdbc/ (7 файлов)
- `user/application/application-mysql-jdbc/build.gradle.kts` — [REVIEW] — новый 2026-07-24, вендор БД — граница модуля вместо профиля (устраняет driver bloat: один драйвер на classpath вместо трёх сразу, см. decisions-log.md)
- `user/application/application-mysql-jdbc/src/main/java/com/example/user/package-info.java` — [DONE]
- `user/application/application-mysql-jdbc/src/main/java/com/example/user/UserMysqlJdbcApplication.java` — [REVIEW]
- `user/application/application-mysql-jdbc/src/main/resources/application.properties` — [REVIEW]
- `user/application/application-mysql-jdbc/compose.yaml` — [REVIEW]
- `user/application/application-mysql-jdbc/src/test/resources/application.properties` — [REVIEW]
- `user/application/application-mysql-jdbc/src/test/java/com/example/user/UserMysqlJdbcApplicationTests.java` — [REVIEW]

#### user/application/application-postgresql-jdbc/ (7 файлов)
- `user/application/application-postgresql-jdbc/build.gradle.kts` — [REVIEW] — новый 2026-07-24, вендор БД — граница модуля вместо профиля (устраняет driver bloat: один драйвер на classpath вместо трёх сразу, см. decisions-log.md)
- `user/application/application-postgresql-jdbc/src/main/java/com/example/user/package-info.java` — [DONE]
- `user/application/application-postgresql-jdbc/src/main/java/com/example/user/UserPostgresqlJdbcApplication.java` — [REVIEW]
- `user/application/application-postgresql-jdbc/src/main/resources/application.properties` — [REVIEW]
- `user/application/application-postgresql-jdbc/compose.yaml` — [REVIEW]
- `user/application/application-postgresql-jdbc/src/test/resources/application.properties` — [REVIEW]
- `user/application/application-postgresql-jdbc/src/test/java/com/example/user/UserPostgresqlJdbcApplicationTests.java` — [REVIEW]

#### user/application/application-mongodb/ (7 файлов)
- `user/application/application-mongodb/build.gradle.kts` — [REVIEW]
- `user/application/application-mongodb/src/test/resources/application.properties` — [REVIEW]
- `user/application/application-mongodb/src/test/java/com/example/user/UserMongoApplicationTests.java` — [REVIEW]
- `user/application/application-mongodb/src/main/resources/application.properties` — [REVIEW]
- `user/application/application-mongodb/src/main/java/com/example/user/package-info.java` — [REVIEW]
- `user/application/application-mongodb/src/main/java/com/example/user/UserMongoApplication.java` — [REVIEW]
- `user/application/application-mongodb/compose.yaml` — [REVIEW] — восстановлен 2026-07-23 (Testcontainers/Compose для Mongo вернули, теперь симметрично с PostgreSQL/MySQL)

#### user/application/application-mongodb-reactive/ (7 файлов)
- `user/application/application-mongodb-reactive/build.gradle.kts` — [REVIEW]
- `user/application/application-mongodb-reactive/src/test/resources/application.properties` — [REVIEW]
- `user/application/application-mongodb-reactive/src/test/java/com/example/user/UserMongoReactiveApplicationTests.java` — [REVIEW]
- `user/application/application-mongodb-reactive/src/main/resources/application.properties` — [REVIEW]
- `user/application/application-mongodb-reactive/src/main/java/com/example/user/package-info.java` — [REVIEW]
- `user/application/application-mongodb-reactive/src/main/java/com/example/user/UserMongoReactiveApplication.java` — [REVIEW]
- `user/application/application-mongodb-reactive/compose.yaml` — [REVIEW] — восстановлен 2026-07-23 (Testcontainers/Compose для Mongo вернули, теперь симметрично с PostgreSQL/MySQL)

#### user/ — предлагаемые отсутствующие файлы (`[ADD]`, 11)
- `user/presentation/webmvc/src/test/java/com/example/user/webmvc/UserControllerTest.java` — [ADD]
- `user/presentation/webflux/src/test/java/com/example/user/webflux/UserControllerTest.java` — [ADD]
- `user/domain/domain/src/test/java/com/example/user/domain/UserNotFoundExceptionTest.java` — [ADD] — исправлено 2026-08-02: убраны 2 устаревшие строки `UserController.java` (webmvc/webflux) — файлы реально уже существуют и учтены отдельно как `[REVIEW]`, счётчик раздела скорректирован 13→11
- `user/persistence/data-r2dbc/src/test/java/com/example/user/data/r2dbc/adapter/UserR2dbcAdapterTest.java` — [ADD]
- `user/persistence/data-mongodb/src/test/java/com/example/user/data/mongodb/adapter/UserMongoAdapterTest.java` — [ADD]
- `user/persistence/data-mongodb-reactive/src/test/java/com/example/user/data/mongodb/reactive/adapter/UserMongoReactiveAdapterTest.java` — [ADD]
- `user/persistence/data-jpa/src/test/java/com/example/user/data/jpa/adapter/UserJpaAdapterTest.java` — [ADD]
- `user/persistence/data-jdbc/src/test/java/com/example/user/data/jdbc/adapter/UserJdbcAdapterTest.java` — [ADD]

### registry/ (6 файлов)

#### registry/application/ (6 файлов)
- `registry/build.gradle.kts` — [REVIEW]
- `registry/src/test/resources/application.properties` — [REVIEW]
- `registry/src/test/java/com/example/registry/RegistryApplicationTests.java` — [REVIEW]
- `registry/src/main/resources/application.properties` — [REVIEW]
- `registry/application/src/main/java/com/example/registry/package-info.java` — [DONE]
- `registry/application/src/main/java/com/example/registry/RegistryApplication.java` — [REVIEW]

### note/ (210 файлов, было 150 — +60: 9 вендорных application-*-{h2,mysql,postgresql}/ модулей заменили 3 профильных 2026-07-24)

#### note/presentation/webmvc/ (5 файлов)
- `note/presentation/webmvc/build.gradle.kts` — [DONE]
- `note/presentation/webmvc/src/main/java/com/example/note/webmvc/package-info.java` — [DONE]
- `note/presentation/webmvc/src/main/java/com/example/note/webmvc/NoteExceptionHandler.java` — [REVIEW]
- `note/presentation/webmvc/src/main/java/com/example/note/webmvc/NoteControllerInterface.java` — [REVIEW]
- `note/presentation/webmvc/src/main/java/com/example/note/webmvc/NoteController.java` — [REVIEW]

#### note/presentation/webflux/ (5 файлов)
- `note/presentation/webflux/build.gradle.kts` — [DONE]
- `note/presentation/webflux/src/main/java/com/example/note/webflux/package-info.java` — [DONE]
- `note/presentation/webflux/src/main/java/com/example/note/webflux/NoteExceptionHandler.java` — [REVIEW]
- `note/presentation/webflux/src/main/java/com/example/note/webflux/NoteControllerReactiveInterface.java` — [REVIEW]
- `note/presentation/webflux/src/main/java/com/example/note/webflux/NoteController.java` — [REVIEW]

#### note/domain/domain/ (6 файлов)
- `note/domain/domain/build.gradle.kts` — [REVIEW]
- `note/domain/domain/src/main/java/com/example/note/domain/package-info.java` — [DONE]
- `note/domain/domain/src/main/java/com/example/note/domain/NoteResponse.java` — [REVIEW]
- `note/domain/domain/src/main/java/com/example/note/domain/NoteRequest.java` — [REVIEW]
- `note/domain/domain/src/main/java/com/example/note/domain/NoteNotFoundException.java` — [REVIEW]
- `note/domain/domain/src/main/java/com/example/note/domain/NotePersistable.java` — [REVIEW]

#### note/persistence/data-r2dbc/ (11 файлов)
- `note/persistence/data-r2dbc/build.gradle.kts` — [DONE]
- `note/persistence/data-r2dbc/src/main/java/com/example/note/data/r2dbc/repository/package-info.java` — [DONE]
- `note/persistence/data-r2dbc/src/main/java/com/example/note/data/r2dbc/repository/NoteR2dbcRepository.java` — [REVIEW]
- `note/persistence/data-r2dbc/src/main/java/com/example/note/data/r2dbc/model/package-info.java` — [DONE]
- `note/persistence/data-r2dbc/src/main/java/com/example/note/data/r2dbc/model/NoteR2dbcEntity.java` — [REVIEW]
- `note/persistence/data-r2dbc/src/main/resources/schema.sql` — [REVIEW]
- `note/persistence/data-r2dbc/src/main/java/com/example/note/data/r2dbc/mapper/package-info.java` — [DONE]
- `note/persistence/data-r2dbc/src/main/java/com/example/note/data/r2dbc/mapper/NoteR2dbcMapperContract.java` — [REVIEW]
- `note/persistence/data-r2dbc/src/main/java/com/example/note/data/r2dbc/mapper/NoteR2dbcMapper.java` — [REVIEW]
- `note/persistence/data-r2dbc/src/main/java/com/example/note/data/r2dbc/adapter/package-info.java` — [DONE]
- `note/persistence/data-r2dbc/src/main/java/com/example/note/data/r2dbc/adapter/NoteService.java` — [REVIEW]

#### note/persistence/data-mongodb-reactive/ (10 файлов)
- `note/persistence/data-mongodb-reactive/build.gradle.kts` — [DONE]
- `note/persistence/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/repository/package-info.java` — [DONE]
- `note/persistence/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/repository/NoteMongoReactiveRepository.java` — [REVIEW]
- `note/persistence/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/model/package-info.java` — [DONE]
- `note/persistence/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/model/NoteReactiveDocument.java` — [REVIEW]
- `note/persistence/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/mapper/package-info.java` — [DONE]
- `note/persistence/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/mapper/NoteMongoReactiveMapperContract.java` — [REVIEW]
- `note/persistence/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/mapper/NoteMongoReactiveMapper.java` — [REVIEW]
- `note/persistence/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/adapter/package-info.java` — [DONE]
- `note/persistence/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/adapter/NoteService.java` — [REVIEW]

#### note/persistence/data-mongodb/ (10 файлов)
- `note/persistence/data-mongodb/build.gradle.kts` — [DONE]
- `note/persistence/data-mongodb/src/main/java/com/example/note/data/mongodb/repository/package-info.java` — [DONE]
- `note/persistence/data-mongodb/src/main/java/com/example/note/data/mongodb/repository/NoteMongoRepository.java` — [REVIEW]
- `note/persistence/data-mongodb/src/main/java/com/example/note/data/mongodb/model/package-info.java` — [DONE]
- `note/persistence/data-mongodb/src/main/java/com/example/note/data/mongodb/model/NoteDocument.java` — [REVIEW]
- `note/persistence/data-mongodb/src/main/java/com/example/note/data/mongodb/mapper/package-info.java` — [DONE]
- `note/persistence/data-mongodb/src/main/java/com/example/note/data/mongodb/mapper/NoteMongoMapperContract.java` — [REVIEW]
- `note/persistence/data-mongodb/src/main/java/com/example/note/data/mongodb/mapper/NoteMongoMapper.java` — [REVIEW]
- `note/persistence/data-mongodb/src/main/java/com/example/note/data/mongodb/adapter/package-info.java` — [DONE]
- `note/persistence/data-mongodb/src/main/java/com/example/note/data/mongodb/adapter/NoteService.java` — [REVIEW]

#### note/persistence/data-jpa/ (10 файлов)
- `note/persistence/data-jpa/build.gradle.kts` — [DONE]
- `note/persistence/data-jpa/src/main/java/com/example/note/data/jpa/repository/package-info.java` — [DONE]
- `note/persistence/data-jpa/src/main/java/com/example/note/data/jpa/repository/NoteJpaRepository.java` — [REVIEW]
- `note/persistence/data-jpa/src/main/java/com/example/note/data/jpa/model/package-info.java` — [DONE]
- `note/persistence/data-jpa/src/main/java/com/example/note/data/jpa/model/NoteEntity.java` — [REVIEW]
- `note/persistence/data-jpa/src/main/java/com/example/note/data/jpa/mapper/package-info.java` — [DONE]
- `note/persistence/data-jpa/src/main/java/com/example/note/data/jpa/mapper/NoteJpaMapperContract.java` — [REVIEW]
- `note/persistence/data-jpa/src/main/java/com/example/note/data/jpa/mapper/NoteJpaMapper.java` — [REVIEW]
- `note/persistence/data-jpa/src/main/java/com/example/note/data/jpa/adapter/package-info.java` — [DONE]
- `note/persistence/data-jpa/src/main/java/com/example/note/data/jpa/adapter/NoteService.java` — [REVIEW]

#### note/persistence/data-jdbc/ (11 файлов)
- `note/persistence/data-jdbc/build.gradle.kts` — [DONE]
- `note/persistence/data-jdbc/src/main/java/com/example/note/data/jdbc/repository/package-info.java` — [REVIEW]
- `note/persistence/data-jdbc/src/main/java/com/example/note/data/jdbc/repository/NoteJdbcRepository.java` — [REVIEW]
- `note/persistence/data-jdbc/src/main/java/com/example/note/data/jdbc/model/package-info.java` — [REVIEW]
- `note/persistence/data-jdbc/src/main/java/com/example/note/data/jdbc/model/NoteJdbcEntity.java` — [REVIEW]
- `note/persistence/data-jdbc/src/main/resources/schema.sql` — [REVIEW]
- `note/persistence/data-jdbc/src/main/java/com/example/note/data/jdbc/mapper/package-info.java` — [DONE]
- `note/persistence/data-jdbc/src/main/java/com/example/note/data/jdbc/mapper/NoteJdbcMapperContract.java` — [REVIEW]
- `note/persistence/data-jdbc/src/main/java/com/example/note/data/jdbc/mapper/NoteJdbcMapper.java` — [REVIEW]
- `note/persistence/data-jdbc/src/main/java/com/example/note/data/jdbc/adapter/package-info.java` — [DONE]
- `note/persistence/data-jdbc/src/main/java/com/example/note/data/jdbc/adapter/NoteService.java` — [REVIEW]

#### note/domain/contract-reactive/ (4 файлов)
- `note/domain/contract-reactive/build.gradle.kts` — [REVIEW]
- `note/domain/contract-reactive/src/main/java/com/example/note/contract/reactive/package-info.java` — [DONE]
- `note/domain/contract-reactive/src/main/java/com/example/note/contract/reactive/NoteReactiveInterface.java` — [REVIEW]
- `note/domain/contract-reactive/src/main/java/com/example/note/contract/reactive/NoteServiceReactiveInterface.java` — [REVIEW]

#### note/domain/contract/ (4 файлов)
- `note/domain/contract/build.gradle.kts` — [REVIEW]
- `note/domain/contract/src/main/java/com/example/note/contract/package-info.java` — [DONE]
- `note/domain/contract/src/main/java/com/example/note/contract/NoteInterface.java` — [REVIEW]
- `note/domain/contract/src/main/java/com/example/note/contract/NoteServiceInterface.java` — [REVIEW]

#### note/application-r2dbc/ (12 файлов) — [REMOVED] 2026-07-24, разбит на 3 вендорных модуля application-r2dbc-{h2,mysql,postgresql}/ — устраняет driver bloat
- `note/application-r2dbc/build.gradle.kts` — [REMOVED]
- `note/application-r2dbc/src/test/resources/application.properties` — [REMOVED]
- `note/application-r2dbc/src/test/java/com/example/note/NoteR2dbcApplicationTests.java` — [REMOVED]
- `note/application-r2dbc/src/main/resources/application.properties` — [REMOVED]
- `note/application-r2dbc/src/main/java/com/example/note/package-info.java` — [REMOVED]
- `note/application-r2dbc/src/main/java/com/example/note/NoteR2dbcApplication.java` — [REMOVED]
- `note/application-r2dbc/src/main/resources/application-postgresql.properties` — [REMOVED]
- `note/application-r2dbc/src/main/resources/application-mysql.properties` — [REMOVED]
- `note/application-r2dbc/compose-postgresql.yaml` — [REMOVED]
- `note/application-r2dbc/compose-mysql.yaml` — [REMOVED]
- `note/application-r2dbc/src/test/java/com/example/note/NotePostgresqlR2dbcApplicationTests.java` — [REMOVED]
- `note/application-r2dbc/src/test/java/com/example/note/NoteMysqlR2dbcApplicationTests.java` — [REMOVED]

#### note/application/application-h2-r2dbc/ (6 файлов)
- `note/application/application-h2-r2dbc/build.gradle.kts` — [REVIEW] — новый 2026-07-24, вендор БД — граница модуля вместо профиля (устраняет driver bloat: один драйвер на classpath вместо трёх сразу, см. decisions-log.md)
- `note/application/application-h2-r2dbc/src/main/java/com/example/note/package-info.java` — [DONE]
- `note/application/application-h2-r2dbc/src/main/java/com/example/note/NoteH2R2dbcApplication.java` — [REVIEW]
- `note/application/application-h2-r2dbc/src/main/resources/application.properties` — [REVIEW]
- `note/application/application-h2-r2dbc/src/test/resources/application.properties` — [REVIEW]
- `note/application/application-h2-r2dbc/src/test/java/com/example/note/NoteH2R2dbcApplicationTests.java` — [REVIEW]

#### note/application/application-mysql-r2dbc/ (7 файлов)
- `note/application/application-mysql-r2dbc/build.gradle.kts` — [REVIEW] — новый 2026-07-24, вендор БД — граница модуля вместо профиля (устраняет driver bloat: один драйвер на classpath вместо трёх сразу, см. decisions-log.md)
- `note/application/application-mysql-r2dbc/src/main/java/com/example/note/package-info.java` — [DONE]
- `note/application/application-mysql-r2dbc/src/main/java/com/example/note/NoteMysqlR2dbcApplication.java` — [REVIEW]
- `note/application/application-mysql-r2dbc/src/main/resources/application.properties` — [REVIEW]
- `note/application/application-mysql-r2dbc/compose.yaml` — [REVIEW]
- `note/application/application-mysql-r2dbc/src/test/resources/application.properties` — [REVIEW]
- `note/application/application-mysql-r2dbc/src/test/java/com/example/note/NoteMysqlR2dbcApplicationTests.java` — [REVIEW]

#### note/application/application-postgresql-r2dbc/ (7 файлов)
- `note/application/application-postgresql-r2dbc/build.gradle.kts` — [REVIEW] — новый 2026-07-24, вендор БД — граница модуля вместо профиля (устраняет driver bloat: один драйвер на classpath вместо трёх сразу, см. decisions-log.md)
- `note/application/application-postgresql-r2dbc/src/main/java/com/example/note/package-info.java` — [DONE]
- `note/application/application-postgresql-r2dbc/src/main/java/com/example/note/NotePostgresqlR2dbcApplication.java` — [REVIEW]
- `note/application/application-postgresql-r2dbc/src/main/resources/application.properties` — [REVIEW]
- `note/application/application-postgresql-r2dbc/compose.yaml` — [REVIEW]
- `note/application/application-postgresql-r2dbc/src/test/resources/application.properties` — [REVIEW]
- `note/application/application-postgresql-r2dbc/src/test/java/com/example/note/NotePostgresqlR2dbcApplicationTests.java` — [REVIEW]

#### note/application-jpa/ (12 файлов) — [REMOVED] 2026-07-24, разбит на 3 вендорных модуля application-jpa-{h2,mysql,postgresql}/ — устраняет driver bloat
- `note/application-jpa/build.gradle.kts` — [REMOVED]
- `note/application-jpa/src/test/resources/application.properties` — [REMOVED]
- `note/application-jpa/src/test/java/com/example/note/NoteJpaApplicationTests.java` — [REMOVED]
- `note/application-jpa/src/main/resources/application.properties` — [REMOVED]
- `note/application-jpa/src/main/java/com/example/note/package-info.java` — [REMOVED]
- `note/application-jpa/src/main/java/com/example/note/NoteJpaApplication.java` — [REMOVED]
- `note/application-jpa/src/main/resources/application-postgresql.properties` — [REMOVED]
- `note/application-jpa/src/main/resources/application-mysql.properties` — [REMOVED]
- `note/application-jpa/compose-postgresql.yaml` — [REMOVED]
- `note/application-jpa/compose-mysql.yaml` — [REMOVED]
- `note/application-jpa/src/test/java/com/example/note/NotePostgresqlJpaApplicationTests.java` — [REMOVED]
- `note/application-jpa/src/test/java/com/example/note/NoteMysqlJpaApplicationTests.java` — [REMOVED]

#### note/application/application-h2-jpa/ (6 файлов)
- `note/application/application-h2-jpa/build.gradle.kts` — [REVIEW] — новый 2026-07-24, вендор БД — граница модуля вместо профиля (устраняет driver bloat: один драйвер на classpath вместо трёх сразу, см. decisions-log.md)
- `note/application/application-h2-jpa/src/main/java/com/example/note/package-info.java` — [DONE]
- `note/application/application-h2-jpa/src/main/java/com/example/note/NoteH2JpaApplication.java` — [REVIEW]
- `note/application/application-h2-jpa/src/main/resources/application.properties` — [REVIEW]
- `note/application/application-h2-jpa/src/test/resources/application.properties` — [REVIEW]
- `note/application/application-h2-jpa/src/test/java/com/example/note/NoteH2JpaApplicationTests.java` — [REVIEW]

#### note/application/application-mysql-jpa/ (7 файлов)
- `note/application/application-mysql-jpa/build.gradle.kts` — [REVIEW] — новый 2026-07-24, вендор БД — граница модуля вместо профиля (устраняет driver bloat: один драйвер на classpath вместо трёх сразу, см. decisions-log.md)
- `note/application/application-mysql-jpa/src/main/java/com/example/note/package-info.java` — [DONE]
- `note/application/application-mysql-jpa/src/main/java/com/example/note/NoteMysqlJpaApplication.java` — [REVIEW]
- `note/application/application-mysql-jpa/src/main/resources/application.properties` — [REVIEW]
- `note/application/application-mysql-jpa/compose.yaml` — [REVIEW]
- `note/application/application-mysql-jpa/src/test/resources/application.properties` — [REVIEW]
- `note/application/application-mysql-jpa/src/test/java/com/example/note/NoteMysqlJpaApplicationTests.java` — [REVIEW]

#### note/application/application-postgresql-jpa/ (7 файлов)
- `note/application/application-postgresql-jpa/build.gradle.kts` — [REVIEW] — новый 2026-07-24, вендор БД — граница модуля вместо профиля (устраняет driver bloat: один драйвер на classpath вместо трёх сразу, см. decisions-log.md)
- `note/application/application-postgresql-jpa/src/main/java/com/example/note/package-info.java` — [DONE]
- `note/application/application-postgresql-jpa/src/main/java/com/example/note/NotePostgresqlJpaApplication.java` — [REVIEW]
- `note/application/application-postgresql-jpa/src/main/resources/application.properties` — [REVIEW]
- `note/application/application-postgresql-jpa/compose.yaml` — [REVIEW]
- `note/application/application-postgresql-jpa/src/test/resources/application.properties` — [REVIEW]
- `note/application/application-postgresql-jpa/src/test/java/com/example/note/NotePostgresqlJpaApplicationTests.java` — [REVIEW]

#### note/application-jdbc/ (12 файлов) — [REMOVED] 2026-07-24, разбит на 3 вендорных модуля application-jdbc-{h2,mysql,postgresql}/ — устраняет driver bloat
- `note/application-jdbc/build.gradle.kts` — [REMOVED]
- `note/application-jdbc/src/test/resources/application.properties` — [REMOVED]
- `note/application-jdbc/src/test/java/com/example/note/NoteJdbcApplicationTests.java` — [REMOVED]
- `note/application-jdbc/src/main/resources/application.properties` — [REMOVED]
- `note/application-jdbc/src/main/java/com/example/note/package-info.java` — [REMOVED]
- `note/application-jdbc/src/main/java/com/example/note/NoteJdbcApplication.java` — [REMOVED]
- `note/application-jdbc/src/main/resources/application-postgresql.properties` — [REMOVED]
- `note/application-jdbc/src/main/resources/application-mysql.properties` — [REMOVED]
- `note/application-jdbc/compose-postgresql.yaml` — [REMOVED]
- `note/application-jdbc/compose-mysql.yaml` — [REMOVED]
- `note/application-jdbc/src/test/java/com/example/note/NotePostgresqlJdbcApplicationTests.java` — [REMOVED]
- `note/application-jdbc/src/test/java/com/example/note/NoteMysqlJdbcApplicationTests.java` — [REMOVED]

#### note/application/application-h2-jdbc/ (6 файлов)
- `note/application/application-h2-jdbc/build.gradle.kts` — [REVIEW] — новый 2026-07-24, вендор БД — граница модуля вместо профиля (устраняет driver bloat: один драйвер на classpath вместо трёх сразу, см. decisions-log.md)
- `note/application/application-h2-jdbc/src/main/java/com/example/note/package-info.java` — [DONE]
- `note/application/application-h2-jdbc/src/main/java/com/example/note/NoteH2JdbcApplication.java` — [REVIEW]
- `note/application/application-h2-jdbc/src/main/resources/application.properties` — [REVIEW]
- `note/application/application-h2-jdbc/src/test/resources/application.properties` — [REVIEW]
- `note/application/application-h2-jdbc/src/test/java/com/example/note/NoteH2JdbcApplicationTests.java` — [REVIEW]

#### note/application/application-mysql-jdbc/ (7 файлов)
- `note/application/application-mysql-jdbc/build.gradle.kts` — [REVIEW] — новый 2026-07-24, вендор БД — граница модуля вместо профиля (устраняет driver bloat: один драйвер на classpath вместо трёх сразу, см. decisions-log.md)
- `note/application/application-mysql-jdbc/src/main/java/com/example/note/package-info.java` — [DONE]
- `note/application/application-mysql-jdbc/src/main/java/com/example/note/NoteMysqlJdbcApplication.java` — [REVIEW]
- `note/application/application-mysql-jdbc/src/main/resources/application.properties` — [REVIEW]
- `note/application/application-mysql-jdbc/compose.yaml` — [REVIEW]
- `note/application/application-mysql-jdbc/src/test/resources/application.properties` — [REVIEW]
- `note/application/application-mysql-jdbc/src/test/java/com/example/note/NoteMysqlJdbcApplicationTests.java` — [REVIEW]

#### note/application/application-postgresql-jdbc/ (7 файлов)
- `note/application/application-postgresql-jdbc/build.gradle.kts` — [REVIEW] — новый 2026-07-24, вендор БД — граница модуля вместо профиля (устраняет driver bloat: один драйвер на classpath вместо трёх сразу, см. decisions-log.md)
- `note/application/application-postgresql-jdbc/src/main/java/com/example/note/package-info.java` — [DONE]
- `note/application/application-postgresql-jdbc/src/main/java/com/example/note/NotePostgresqlJdbcApplication.java` — [REVIEW]
- `note/application/application-postgresql-jdbc/src/main/resources/application.properties` — [REVIEW]
- `note/application/application-postgresql-jdbc/compose.yaml` — [REVIEW]
- `note/application/application-postgresql-jdbc/src/test/resources/application.properties` — [REVIEW]
- `note/application/application-postgresql-jdbc/src/test/java/com/example/note/NotePostgresqlJdbcApplicationTests.java` — [REVIEW]

#### note/application/application-mongodb/ (7 файлов)
- `note/application/application-mongodb/build.gradle.kts` — [REVIEW]
- `note/application/application-mongodb/src/test/resources/application.properties` — [REVIEW]
- `note/application/application-mongodb/src/test/java/com/example/note/NoteMongoApplicationTests.java` — [REVIEW]
- `note/application/application-mongodb/src/main/resources/application.properties` — [REVIEW]
- `note/application/application-mongodb/src/main/java/com/example/note/package-info.java` — [REVIEW]
- `note/application/application-mongodb/src/main/java/com/example/note/NoteMongoApplication.java` — [REVIEW]
- `note/application/application-mongodb/compose.yaml` — [REVIEW] — восстановлен 2026-07-23 (Testcontainers/Compose для Mongo вернули, теперь симметрично с PostgreSQL/MySQL)

#### note/application/application-mongodb-reactive/ (7 файлов)
- `note/application/application-mongodb-reactive/build.gradle.kts` — [REVIEW]
- `note/application/application-mongodb-reactive/src/test/resources/application.properties` — [REVIEW]
- `note/application/application-mongodb-reactive/src/test/java/com/example/note/NoteMongoReactiveApplicationTests.java` — [REVIEW]
- `note/application/application-mongodb-reactive/src/main/resources/application.properties` — [REVIEW]
- `note/application/application-mongodb-reactive/src/main/java/com/example/note/package-info.java` — [REVIEW]
- `note/application/application-mongodb-reactive/src/main/java/com/example/note/NoteMongoReactiveApplication.java` — [REVIEW]
- `note/application/application-mongodb-reactive/compose.yaml` — [REVIEW] — восстановлен 2026-07-23 (Testcontainers/Compose для Mongo вернули, теперь симметрично с PostgreSQL/MySQL)

#### note/ — предлагаемые отсутствующие файлы (`[ADD]`, 19)
- `note/presentation/webmvc/src/test/java/com/example/note/webmvc/NoteExceptionHandlerTest.java` — [ADD]
- `note/presentation/webmvc/src/test/java/com/example/note/webmvc/NoteControllerTest.java` — [ADD]
- `note/presentation/webflux/src/test/java/com/example/note/webflux/NoteExceptionHandlerTest.java` — [ADD]
- `note/presentation/webflux/src/test/java/com/example/note/webflux/NoteControllerTest.java` — [ADD]
- `note/domain/domain/src/test/java/com/example/note/domain/NoteNotFoundExceptionTest.java` — [ADD]
- `note/persistence/data-r2dbc/src/test/java/com/example/note/data/r2dbc/mapper/NoteR2dbcMapperTest.java` — [ADD]
- `note/persistence/data-r2dbc/src/test/java/com/example/note/data/r2dbc/adapter/NoteR2dbcAdapterIntegrationTest.java` — [ADD]
- `note/persistence/data-mongodb/src/test/java/com/example/note/data/mongodb/mapper/NoteMongoMapperTest.java` — [ADD]
- `note/persistence/data-mongodb/src/test/java/com/example/note/data/mongodb/adapter/NoteMongoAdapterIntegrationTest.java` — [ADD]
- `note/persistence/data-mongodb-reactive/src/test/java/com/example/note/data/mongodb/reactive/mapper/NoteMongoReactiveMapperTest.java` — [ADD]
- `note/persistence/data-mongodb-reactive/src/test/java/com/example/note/data/mongodb/reactive/adapter/NoteMongoReactiveAdapterIntegrationTest.java` — [ADD]
- `note/persistence/data-jpa/src/test/java/com/example/note/data/jpa/mapper/NoteJpaMapperTest.java` — [ADD]
- `note/persistence/data-jpa/src/test/java/com/example/note/data/jpa/adapter/NoteJpaAdapterIntegrationTest.java` — [ADD]
- `note/persistence/data-jdbc/src/test/java/com/example/note/data/jdbc/mapper/NoteJdbcMapperTest.java` — [ADD]
- `note/persistence/data-jdbc/src/test/java/com/example/note/data/jdbc/adapter/NoteJdbcAdapterIntegrationTest.java` — [ADD]
- `note/application/application-h2-jpa/src/test/java/com/example/note/NoteCreateEndpointIntegrationTest.java` — [ADD]

### gradle/ (4 файлов)

#### gradle/ — отдельные файлы (1)
- `gradle/libs.versions.toml` — [REVIEW]

#### gradle/wrapper/ (2 файлов)
- `gradle/wrapper/gradle-wrapper.properties` — [REVIEW]
- `gradle/wrapper/gradle-wrapper.jar` — [REVIEW]

#### gradle/checkstyle/ (1 файл)
- `gradle/checkstyle/checkstyle.xml` — [REMOVED] 2026-09-04 (вечер) — заменён на `google_checks.xml` без переопределений (см. ниже); до этого днём был копией `google_checks.xml` с `LineLength.max=120`+`Indentation.basicOffset=4`
- `gradle/checkstyle/google_checks.xml` — [REVIEW] — новый 2026-09-04 (вечер), дословная нетронутая копия бандла `checkstyle-13.10.0.jar` (было `io.spring.javaformat.checkstyle.SpringChecks` до этого же дня днём) — 0 переопределений: `severity=warning` по умолчанию (Gradle Checkstyle-таск не валит `check` на warning, только репортит), отступ 2 пробела, `LineLength.max=100`

### gateway/ (6 файлов)

#### gateway/application/ (6 файлов)
- `gateway/build.gradle.kts` — [REVIEW]
- `gateway/src/test/resources/application.properties` — [REVIEW]
- `gateway/src/test/java/com/example/gateway/GatewayApplicationTests.java` — [REVIEW]
- `gateway/src/main/resources/application.properties` — [REVIEW]
- `gateway/application/src/main/java/com/example/gateway/package-info.java` — [DONE]
- `gateway/application/src/main/java/com/example/gateway/GatewayApplication.java` — [REVIEW]

### config/ (6 файлов)

#### config/application/ (6 файлов)
- `config/build.gradle.kts` — [REVIEW]
- `config/src/test/resources/application.properties` — [REVIEW]
- `config/src/test/java/com/example/config/ConfigApplicationTests.java` — [REVIEW]
- `config/src/main/resources/application.properties` — [REVIEW]
- `config/application/src/main/java/com/example/config/package-info.java` — [DONE]
- `config/application/src/main/java/com/example/config/ConfigApplication.java` — [REVIEW]

### build-logic/ (50 файлов, было 40 — добавлены 10 плагинов оси вендора/Testcontainers/dev-режима 2026-07-23)

#### build-logic/ — корневые файлы (2 файлов)
- `build-logic/settings.gradle.kts` — [REVIEW]
- `build-logic/convention/build.gradle.kts` — [REVIEW]

#### build-logic/convention/src/main/kotlin/ — precompiled script plugins (52 файла на диске, было 49 — точная синхронизация подраздела не поддерживалась с 2026-08-15, не проведена полностью в этой правке, см. CLAUDE.md → «Задачи»; +1 строка ниже — `com.example.codequality-spotless`, 2026-08-16)
- `build-logic/com.example.spring-boot-database-postgresql.gradle.kts` — [REVIEW] — новый 2026-07-23, вендорная ось для JPA/JDBC (`runtimeOnly org.postgresql:postgresql`), активируется через Spring-профиль
- `build-logic/com.example.spring-boot-database-mysql.gradle.kts` — [REVIEW] — новый 2026-07-23, `runtimeOnly com.mysql:mysql-connector-j`
- `build-logic/com.example.spring-boot-database-postgresql-r2dbc.gradle.kts` — [REVIEW] — новый 2026-07-23, `runtimeOnly org.postgresql:r2dbc-postgresql`; переименован 2026-07-24 из `-r2dbc-postgresql` (вендор-впереди-технологии, группировка с `database-postgresql`)
- `build-logic/com.example.spring-boot-database-mysql-r2dbc.gradle.kts` — [REVIEW] — новый 2026-07-23, `runtimeOnly io.asyncer:r2dbc-mysql`; переименован 2026-07-24 из `-r2dbc-mysql` (та же логика)
- `build-logic/com.example.spring-boot-testcontainers.gradle.kts` — [REVIEW] — новый 2026-07-23, технологически нейтральная обвязка Testcontainers+JUnit5+`@ServiceConnection` (родитель `spring-boot`); выделен из `-testcontainers-mongodb` в тот же день — не должен быть привязан к одной технологии, пригоден для будущих Postgres/Kafka/Redis-контейнеров
- `build-logic/com.example.spring-boot-testcontainers-mongodb.gradle.kts` — [REVIEW] — новый 2026-07-23, только MongoDB-специфичный артефакт `testcontainers-mongodb` (родитель `spring-boot`); применяется вместе с `-testcontainers` в `application-mongodb*`
- `build-logic/com.example.spring-boot-testcontainers-postgresql.gradle.kts` — [REVIEW] — новый 2026-07-23, `testcontainers-postgresql` — один контейнер даёт и `JdbcConnectionDetails`, и `R2dbcConnectionDetails` (подтверждено docs.spring.io), применяется в `application-jpa`/`-jdbc`/`-r2dbc`
- `build-logic/com.example.spring-boot-testcontainers-mysql.gradle.kts` — [REVIEW] — новый 2026-07-23, `testcontainers-mysql` + `testRuntimeOnly(mysql-connector-j)` (добавлено в тот же день: `MySQLContainer`, в отличие от `PostgreSQLContainer`, использует JDBC-based wait-strategy по умолчанию — без JDBC-драйвера падает `NoDriverFoundException`, найдено live-прогоном `clean check`)
- `build-logic/com.example.spring-boot-testcontainers-r2dbc.gradle.kts` — [REVIEW] — новый 2026-07-23, мост `testcontainers-r2dbc` (`R2DBCDatabaseContainer`) — нужен только `application-r2dbc`, чтобы JDBC-семейство контейнеров (Postgres/MySQL) отдавало `R2dbcConnectionDetails`; без него — `ClassNotFoundException` в рантайме (найдено этой же сборкой)
- `build-logic/com.example.spring-boot-docker-compose.gradle.kts` — [REVIEW] — новый 2026-07-23, dev-запуск `application-mongodb*`; родитель `spring-boot` + прямой `id("org.springframework.boot")` (как у `spring-boot-application`, не через него — исправлено в тот же день по замечанию пользователя о композиции)
- `build-logic/com.example.codequality-jspecify.gradle.kts` — [REVIEW] — новый 2026-07-15, вынесен из `nullaway` (`id("java-library")` + `api("org.jspecify")`), применяется через `codequality`
- `build-logic/com.example.spring-cloud-application.gradle.kts` — [REVIEW] — новый 2026-07-15, устраняет диамант у 4 standalone `spring-cloud-*`-плагинов (родитель `spring-boot-application` вместо параллельных `spring-cloud`+`spring-boot-application`)
- `build-logic/com.example.spring-cloud.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-cloud-openfeign.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-cloud-loadbalancer.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-cloud-gateway-webmvc.gradle.kts` — [REMOVED] — переименован 2026-08-01 в `spring-cloud-gateway-server-webmvc.gradle.kts` (точное имя артефакта `spring-cloud-starter-gateway-server-webmvc`)
- `build-logic/com.example.spring-cloud-gateway-webflux.gradle.kts` — [REMOVED] — переименован 2026-08-01 в `spring-cloud-gateway-server-webflux.gradle.kts` (точное имя артефакта `spring-cloud-starter-gateway-server-webflux`)
- `build-logic/com.example.spring-cloud-gateway-server-webmvc.gradle.kts` — [REVIEW] — новый путь после переименования выше
- `build-logic/com.example.spring-cloud-gateway-server-webflux.gradle.kts` — [REVIEW] — новый путь после переименования выше
- `build-logic/com.example.spring-cloud-eureka-server.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-cloud-eureka-client.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-cloud-config-server.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-cloud-config-client.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-cloud-circuit-breaker.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-webmvc.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-webflux.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-security-oauth2-resource-server.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-security-oauth2-client.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-security-oauth2-authorization-server.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-database-h2.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-graphql.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-data-r2dbc.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-data-mongodb.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-data-mongodb-reactive.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-data-jpa.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-data-jdbc.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-data-elasticsearch.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-client-webclient.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-client-restclient.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-application.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-actuator.gradle.kts` — [REVIEW] — воссоздан 2026-08-01 (был удалён 2026-07-14, слит тогда в `spring-boot-application`) — атомизация: вынесен обратно в отдельный плагин, применяется явно вторым `id(...)` на всех 37 листовых модулях, где раньше был неявным через `spring-boot-application`/`spring-cloud-application`
- `build-logic/com.example.project-reactor.gradle.kts` — [REVIEW] — переименован 2026-08-01 из `reactor.gradle.kts` (точное официальное имя технологии «Project Reactor»)
- `build-logic/com.example.spring-boot-database-h2-r2dbc.gradle.kts` — [REVIEW] — переименован 2026-07-24 из `-r2dbc-h2` (группировка с `database-h2`)
- `build-logic/com.example.codequality-nullaway.gradle.kts` — [REVIEW]
- `build-logic/com.example.java-library.gradle.kts` — [REVIEW] — переименован 2026-08-01 из `library.gradle.kts` (точное имя обёрнутого ядрового Gradle-плагина `java-library`, было сокращено)
- `build-logic/com.example.javaformat.gradle.kts` — [REMOVED]
- `build-logic/com.example.codequality-jacoco.gradle.kts` — [REVIEW] — применяется напрямую из `java.gradle.kts` с 2026-08-01, был через `codequality`-агрегатор
- `build-logic/com.example.codequality-jacoco-report-aggregation.gradle.kts` — [REVIEW] — применяется напрямую из `java.gradle.kts` с 2026-08-01, был через `codequality`-агрегатор
- `build-logic/com.example.codequality.gradle.kts` — [REMOVED] — убран 2026-08-01, чистый список из 5 id без своей конфигурации и с единственным потребителем (`base`/`java`) — инлайнирован напрямую в `java.gradle.kts`, см. decisions-log.md
- `build-logic/com.example.codequality-checkstyle.gradle.kts` — [REVIEW] — применяется напрямую из `java.gradle.kts` с 2026-08-01, был через `codequality`-агрегатор; с 2026-09-04 без `spring-javaformat-checkstyle`-зависимости, `configFile` указывает на `gradle/checkstyle/google_checks.xml` (см. `gradle/checkstyle/`)
- `build-logic/com.example.codequality-spotless.gradle.kts` — [REVIEW] — новый 2026-08-16, до 2026-09-04 был кастомный `importOrder`+regex-автофикс на `compileJava`; с 2026-09-04 днём — штатный `java { googleJavaFormat().aosp() }` без ручной привязки; тем же вечером `compileJava.dependsOn(spotlessApply)` возвращён по явному запросу — автофикс снова срабатывает сам при каждой сборке; применяется напрямую из `java.gradle.kts`, шестой `codequality-*`-фрагмент
- `build-logic/com.example.base.gradle.kts` — [REMOVED] — переименован 2026-08-01 в `java.gradle.kts` (id совпадает с обёрнутым ядровым Gradle-плагином `java`, было именем роли, не технологии)
- `build-logic/com.example.java.gradle.kts` — [REVIEW] — новый 2026-08-01, переименование предыдущей строки `com.example.base.gradle.kts`; дополнительно инлайнирует 5 `codequality-*` id вместо снятого агрегатора
- `build-logic/com.example.spring-boot-validation.gradle.kts` — [REVIEW] — новый 2026-08-01, вынесен из `spring-boot.gradle.kts` в атомарный плагин (`spring-boot-starter-validation`+test), применяется явно в `webmvc`/`webflux`/`data-jpa` × 3 сервиса — закрывает CLAUDE.md → «Открытые решения» → «Область подключения spring-boot-starter-validation»

### auth/ (6 файлов)

#### auth/application/ (6 файлов)
- `auth/build.gradle.kts` — [REVIEW]
- `auth/src/test/resources/application.properties` — [REVIEW]
- `auth/src/test/java/com/example/auth/AuthApplicationTests.java` — [REVIEW]
- `auth/src/main/resources/application.properties` — [REVIEW]
- `auth/application/src/main/java/com/example/auth/package-info.java` — [DONE]
- `auth/application/src/main/java/com/example/auth/AuthApplication.java` — [REVIEW]

### .github/ (1 файл)
- `.github/workflows/gradle.yml` — [REVIEW]
