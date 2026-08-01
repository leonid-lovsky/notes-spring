# Каталог файлов проекта

> Вынесено из CLAUDE.md 2026-07-14 (см. [../CLAUDE.md](../CLAUDE.md) → «Правила» → лимит размера файла). Полный git-отслеживаемый список файлов; статусы: [DONE] — утверждено, [REVIEW] — требует пересмотра (по умолчанию), [ADD] — файла нет, предложен, [REMOVED] — удалён (строка сохранена для истории). Список путей/статусов не трогать без прямого запроса (см. CLAUDE.md → «Правила» → лимит размера файла).

### Корень репозитория (8 файлов)
- `settings.gradle.kts` — [DONE]
- `gradlew.bat` — [DONE]
- `gradlew` — [DONE]
- `gradle.properties` — [DONE]
- `CLAUDE.md` — [DONE] — единственный документационный файл в корне, обязательное условие автозагрузки Claude Code; остальные документационные файлы — `docs/`, см. ниже
- `.java-version` — [DONE]
- `.springjavaformatconfig` — [REMOVED]
- `.gitignore` — [DONE]
- `.gitattributes` — [DONE]

### docs/ (6 файлов, перенесены из корня 2026-07-24 — реструктуризация документации под 100%-доступность для ИИ-агента, см. CLAUDE.md → «Правила» → «Документация — только для ИИ-агента, не для человека»)
- `docs/db-migration-tools-reference.md` — [REVIEW]
- `docs/decisions-log.md` — [REVIEW] — новый 2026-07-23, вынесен из CLAUDE.md → «Принятые решения» (снятие объёма, AI-readability)
- `docs/tech-glossary.md` — [REVIEW] — новый 2026-07-23, вынесен из CLAUDE.md → «Технологии» (снятие объёма, AI-readability)
- `docs/file-catalog.md` — [REVIEW] — пропущен в собственном каталоге, добавлен 2026-07-23 при попутной правке
- `docs/spring-boot-starters-reference.md` — [REVIEW] — пропущен в каталоге, добавлен 2026-07-23 при попутной правке
- `docs/google-docs-full-model.md` — [REVIEW] — новый 2026-07-23, набросок требований к совместному редактированию (см. CLAUDE.md → «Открытые решения»), не решение

### .claude/ (0 файлов, добавлено 2026-07-24, весь каталог удалён из репозитория и с диска в тот же день)
- `.claude/settings.json` — [REMOVED] — регистрировал SessionStart hook; удалён 2026-07-24 вместе со всем `.claude/` (`.gitignore` уже содержал `.claude/` — по факту был случайно закоммичен ранее, теперь untracked навсегда)
- `.claude/hooks/session-start.sh` — [REMOVED] — автоустановка JDK 25 в облачных сессиях; удалён 2026-07-24 из-за CRLF-порчи файла на диске (`bad interpreter` при старте сессии), не восстановлен

### user-note/ (213 файлов, было 153 — +60: 9 вендорных application-*-{h2,mysql,postgresql}/ модулей заменили 3 профильных 2026-07-24)

#### user-note/presentation/webmvc/ (5 файлов)
- `user-note/presentation/webmvc/build.gradle.kts` — [DONE]
- `user-note/presentation/webmvc/src/main/java/com/example/usernote/webmvc/package-info.java` — [DONE]
- `user-note/presentation/webmvc/src/main/java/com/example/usernote/webmvc/UserNoteExceptionHandler.java` — [REVIEW]
- `user-note/presentation/webmvc/src/main/java/com/example/usernote/webmvc/UserNoteControllerInterface.java` — [REVIEW]
- `user-note/presentation/webmvc/src/main/java/com/example/usernote/webmvc/UserNoteController.java` — [REVIEW]

#### user-note/presentation/webflux/ (5 файлов)
- `user-note/presentation/webflux/build.gradle.kts` — [DONE]
- `user-note/presentation/webflux/src/main/java/com/example/usernote/webflux/package-info.java` — [DONE]
- `user-note/presentation/webflux/src/main/java/com/example/usernote/webflux/UserNoteExceptionHandler.java` — [REVIEW]
- `user-note/presentation/webflux/src/main/java/com/example/usernote/webflux/UserNoteControllerReactiveInterface.java` — [REVIEW]
- `user-note/presentation/webflux/src/main/java/com/example/usernote/webflux/UserNoteController.java` — [REVIEW]

#### user-note/domain/domain/ (9 файлов)
- `user-note/domain/domain/build.gradle.kts` — [REVIEW]
- `user-note/domain/domain/src/main/java/com/example/usernote/domain/package-info.java` — [DONE]
- `user-note/domain/domain/src/main/java/com/example/usernote/domain/UserNoteRole.java` — [REVIEW]
- `user-note/domain/domain/src/main/java/com/example/usernote/domain/UserNoteResponse.java` — [REVIEW]
- `user-note/domain/domain/src/main/java/com/example/usernote/domain/UserNoteRequest.java` — [REVIEW]
- `user-note/domain/domain/src/main/java/com/example/usernote/domain/UserNoteNotFoundException.java` — [REVIEW]
- `user-note/domain/domain/src/main/java/com/example/usernote/domain/UserNotFoundException.java` — [REVIEW]
- `user-note/domain/domain/src/main/java/com/example/usernote/domain/NoteNotFoundException.java` — [REVIEW]
- `user-note/domain/domain/src/main/java/com/example/usernote/domain/UserNotePersistable.java` — [REVIEW]

#### user-note/persistence/data-r2dbc/ (11 файлов)
- `user-note/persistence/data-r2dbc/build.gradle.kts` — [DONE]
- `user-note/persistence/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/repository/package-info.java` — [DONE]
- `user-note/persistence/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/repository/UserNoteR2dbcRepository.java` — [REVIEW]
- `user-note/persistence/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/model/package-info.java` — [DONE]
- `user-note/persistence/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/model/UserNoteR2dbcEntity.java` — [REVIEW]
- `user-note/persistence/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/mapper/package-info.java` — [DONE]
- `user-note/persistence/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/mapper/UserNoteR2dbcMapperContract.java` — [REVIEW]
- `user-note/persistence/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/mapper/UserNoteR2dbcMapper.java` — [REVIEW]
- `user-note/persistence/data-r2dbc/src/main/resources/schema.sql` — [REVIEW]
- `user-note/persistence/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/adapter/package-info.java` — [DONE]
- `user-note/persistence/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/adapter/UserNoteService.java` — [REVIEW]

#### user-note/persistence/data-mongodb-reactive/ (10 файлов)
- `user-note/persistence/data-mongodb-reactive/build.gradle.kts` — [DONE]
- `user-note/persistence/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/repository/package-info.java` — [DONE]
- `user-note/persistence/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/repository/UserNoteMongoReactiveRepository.java` — [REVIEW]
- `user-note/persistence/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/model/package-info.java` — [DONE]
- `user-note/persistence/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/model/UserNoteReactiveDocument.java` — [REVIEW]
- `user-note/persistence/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/mapper/package-info.java` — [DONE]
- `user-note/persistence/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/mapper/UserNoteMongoReactiveMapperContract.java` — [REVIEW]
- `user-note/persistence/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/mapper/UserNoteMongoReactiveMapper.java` — [REVIEW]
- `user-note/persistence/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/adapter/package-info.java` — [DONE]
- `user-note/persistence/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/adapter/UserNoteService.java` — [REVIEW]

#### user-note/persistence/data-mongodb/ (10 файлов)
- `user-note/persistence/data-mongodb/build.gradle.kts` — [DONE]
- `user-note/persistence/data-mongodb/src/main/java/com/example/usernote/data/mongodb/repository/package-info.java` — [DONE]
- `user-note/persistence/data-mongodb/src/main/java/com/example/usernote/data/mongodb/repository/UserNoteMongoRepository.java` — [REVIEW]
- `user-note/persistence/data-mongodb/src/main/java/com/example/usernote/data/mongodb/model/package-info.java` — [DONE]
- `user-note/persistence/data-mongodb/src/main/java/com/example/usernote/data/mongodb/model/UserNoteDocument.java` — [REVIEW]
- `user-note/persistence/data-mongodb/src/main/java/com/example/usernote/data/mongodb/mapper/package-info.java` — [DONE]
- `user-note/persistence/data-mongodb/src/main/java/com/example/usernote/data/mongodb/mapper/UserNoteMongoMapperContract.java` — [REVIEW]
- `user-note/persistence/data-mongodb/src/main/java/com/example/usernote/data/mongodb/mapper/UserNoteMongoMapper.java` — [REVIEW]
- `user-note/persistence/data-mongodb/src/main/java/com/example/usernote/data/mongodb/adapter/package-info.java` — [DONE]
- `user-note/persistence/data-mongodb/src/main/java/com/example/usernote/data/mongodb/adapter/UserNoteService.java` — [REVIEW]

#### user-note/persistence/data-jpa/ (10 файлов)
- `user-note/persistence/data-jpa/build.gradle.kts` — [DONE]
- `user-note/persistence/data-jpa/src/main/java/com/example/usernote/data/jpa/repository/package-info.java` — [DONE]
- `user-note/persistence/data-jpa/src/main/java/com/example/usernote/data/jpa/repository/UserNoteJpaRepository.java` — [REVIEW]
- `user-note/persistence/data-jpa/src/main/java/com/example/usernote/data/jpa/model/package-info.java` — [DONE]
- `user-note/persistence/data-jpa/src/main/java/com/example/usernote/data/jpa/model/UserNoteEntity.java` — [REVIEW]
- `user-note/persistence/data-jpa/src/main/java/com/example/usernote/data/jpa/mapper/package-info.java` — [DONE]
- `user-note/persistence/data-jpa/src/main/java/com/example/usernote/data/jpa/mapper/UserNoteJpaMapperContract.java` — [REVIEW]
- `user-note/persistence/data-jpa/src/main/java/com/example/usernote/data/jpa/mapper/UserNoteJpaMapper.java` — [REVIEW]
- `user-note/persistence/data-jpa/src/main/java/com/example/usernote/data/jpa/adapter/package-info.java` — [DONE]
- `user-note/persistence/data-jpa/src/main/java/com/example/usernote/data/jpa/adapter/UserNoteService.java` — [REVIEW]

#### user-note/persistence/data-jdbc/ (11 файлов)
- `user-note/persistence/data-jdbc/build.gradle.kts` — [DONE]
- `user-note/persistence/data-jdbc/src/main/java/com/example/usernote/data/jdbc/repository/package-info.java` — [REVIEW]
- `user-note/persistence/data-jdbc/src/main/java/com/example/usernote/data/jdbc/repository/UserNoteJdbcRepository.java` — [REVIEW]
- `user-note/persistence/data-jdbc/src/main/java/com/example/usernote/data/jdbc/model/package-info.java` — [REVIEW]
- `user-note/persistence/data-jdbc/src/main/java/com/example/usernote/data/jdbc/model/UserNoteJdbcEntity.java` — [REVIEW]
- `user-note/persistence/data-jdbc/src/main/java/com/example/usernote/data/jdbc/mapper/package-info.java` — [DONE]
- `user-note/persistence/data-jdbc/src/main/java/com/example/usernote/data/jdbc/mapper/UserNoteJdbcMapperContract.java` — [REVIEW]
- `user-note/persistence/data-jdbc/src/main/java/com/example/usernote/data/jdbc/mapper/UserNoteJdbcMapper.java` — [REVIEW]
- `user-note/persistence/data-jdbc/src/main/resources/schema.sql` — [REVIEW]
- `user-note/persistence/data-jdbc/src/main/java/com/example/usernote/data/jdbc/adapter/package-info.java` — [DONE]
- `user-note/persistence/data-jdbc/src/main/java/com/example/usernote/data/jdbc/adapter/UserNoteService.java` — [REVIEW]

#### user-note/domain/contract-reactive/ (4 файлов)
- `user-note/domain/contract-reactive/build.gradle.kts` — [REVIEW]
- `user-note/domain/contract-reactive/src/main/java/com/example/usernote/contract/reactive/package-info.java` — [DONE]
- `user-note/domain/contract-reactive/src/main/java/com/example/usernote/contract/reactive/UserNoteReactiveInterface.java` — [REVIEW]
- `user-note/domain/contract-reactive/src/main/java/com/example/usernote/contract/reactive/UserNoteServiceReactiveInterface.java` — [REVIEW]

#### user-note/domain/contract/ (4 файлов)
- `user-note/domain/contract/build.gradle.kts` — [REVIEW]
- `user-note/domain/contract/src/main/java/com/example/usernote/contract/package-info.java` — [DONE]
- `user-note/domain/contract/src/main/java/com/example/usernote/contract/UserNoteInterface.java` — [REVIEW]
- `user-note/domain/contract/src/main/java/com/example/usernote/contract/UserNoteServiceInterface.java` — [REVIEW]

#### user-note/application-r2dbc/ (12 файлов) — [REMOVED] 2026-07-24, разбит на 3 вендорных модуля application-r2dbc-{h2,mysql,postgresql}/ — устраняет driver bloat
- `user-note/application-r2dbc/build.gradle.kts` — [REMOVED]
- `user-note/application-r2dbc/src/test/resources/application.properties` — [REMOVED]
- `user-note/application-r2dbc/src/test/java/com/example/usernote/UserNoteR2dbcApplicationTests.java` — [REMOVED]
- `user-note/application-r2dbc/src/main/resources/application.properties` — [REMOVED]
- `user-note/application-r2dbc/src/main/java/com/example/usernote/package-info.java` — [REMOVED]
- `user-note/application-r2dbc/src/main/java/com/example/usernote/UserNoteR2dbcApplication.java` — [REMOVED]
- `user-note/application-r2dbc/src/main/resources/application-postgresql.properties` — [REMOVED]
- `user-note/application-r2dbc/src/main/resources/application-mysql.properties` — [REMOVED]
- `user-note/application-r2dbc/compose-postgresql.yaml` — [REMOVED]
- `user-note/application-r2dbc/compose-mysql.yaml` — [REMOVED]
- `user-note/application-r2dbc/src/test/java/com/example/usernote/UserNotePostgresqlR2dbcApplicationTests.java` — [REMOVED]
- `user-note/application-r2dbc/src/test/java/com/example/usernote/UserNoteMysqlR2dbcApplicationTests.java` — [REMOVED]

#### user-note/application/application-h2-r2dbc/ (6 файлов)
- `user-note/application/application-h2-r2dbc/build.gradle.kts` — [REVIEW] — новый 2026-07-24, вендор БД — граница модуля вместо профиля (устраняет driver bloat: один драйвер на classpath вместо трёх сразу, см. decisions-log.md)
- `user-note/application/application-h2-r2dbc/src/main/java/com/example/usernote/package-info.java` — [DONE]
- `user-note/application/application-h2-r2dbc/src/main/java/com/example/usernote/UserNoteH2R2dbcApplication.java` — [REVIEW]
- `user-note/application/application-h2-r2dbc/src/main/resources/application.properties` — [REVIEW]
- `user-note/application/application-h2-r2dbc/src/test/resources/application.properties` — [REVIEW]
- `user-note/application/application-h2-r2dbc/src/test/java/com/example/usernote/UserNoteH2R2dbcApplicationTests.java` — [REVIEW]

#### user-note/application/application-mysql-r2dbc/ (7 файлов)
- `user-note/application/application-mysql-r2dbc/build.gradle.kts` — [REVIEW] — новый 2026-07-24, вендор БД — граница модуля вместо профиля (устраняет driver bloat: один драйвер на classpath вместо трёх сразу, см. decisions-log.md)
- `user-note/application/application-mysql-r2dbc/src/main/java/com/example/usernote/package-info.java` — [DONE]
- `user-note/application/application-mysql-r2dbc/src/main/java/com/example/usernote/UserNoteMysqlR2dbcApplication.java` — [REVIEW]
- `user-note/application/application-mysql-r2dbc/src/main/resources/application.properties` — [REVIEW]
- `user-note/application/application-mysql-r2dbc/compose.yaml` — [REVIEW]
- `user-note/application/application-mysql-r2dbc/src/test/resources/application.properties` — [REVIEW]
- `user-note/application/application-mysql-r2dbc/src/test/java/com/example/usernote/UserNoteMysqlR2dbcApplicationTests.java` — [REVIEW]

#### user-note/application/application-postgresql-r2dbc/ (7 файлов)
- `user-note/application/application-postgresql-r2dbc/build.gradle.kts` — [REVIEW] — новый 2026-07-24, вендор БД — граница модуля вместо профиля (устраняет driver bloat: один драйвер на classpath вместо трёх сразу, см. decisions-log.md)
- `user-note/application/application-postgresql-r2dbc/src/main/java/com/example/usernote/package-info.java` — [DONE]
- `user-note/application/application-postgresql-r2dbc/src/main/java/com/example/usernote/UserNotePostgresqlR2dbcApplication.java` — [REVIEW]
- `user-note/application/application-postgresql-r2dbc/src/main/resources/application.properties` — [REVIEW]
- `user-note/application/application-postgresql-r2dbc/compose.yaml` — [REVIEW]
- `user-note/application/application-postgresql-r2dbc/src/test/resources/application.properties` — [REVIEW]
- `user-note/application/application-postgresql-r2dbc/src/test/java/com/example/usernote/UserNotePostgresqlR2dbcApplicationTests.java` — [REVIEW]

#### user-note/application-jpa/ (12 файлов) — [REMOVED] 2026-07-24, разбит на 3 вендорных модуля application-jpa-{h2,mysql,postgresql}/ — устраняет driver bloat
- `user-note/application-jpa/build.gradle.kts` — [REMOVED]
- `user-note/application-jpa/src/test/resources/application.properties` — [REMOVED]
- `user-note/application-jpa/src/test/java/com/example/usernote/UserNoteJpaApplicationTests.java` — [REMOVED]
- `user-note/application-jpa/src/main/resources/application.properties` — [REMOVED]
- `user-note/application-jpa/src/main/java/com/example/usernote/package-info.java` — [REMOVED]
- `user-note/application-jpa/src/main/java/com/example/usernote/UserNoteJpaApplication.java` — [REMOVED]
- `user-note/application-jpa/src/main/resources/application-postgresql.properties` — [REMOVED]
- `user-note/application-jpa/src/main/resources/application-mysql.properties` — [REMOVED]
- `user-note/application-jpa/compose-postgresql.yaml` — [REMOVED]
- `user-note/application-jpa/compose-mysql.yaml` — [REMOVED]
- `user-note/application-jpa/src/test/java/com/example/usernote/UserNotePostgresqlJpaApplicationTests.java` — [REMOVED]
- `user-note/application-jpa/src/test/java/com/example/usernote/UserNoteMysqlJpaApplicationTests.java` — [REMOVED]

#### user-note/application/application-h2-jpa/ (6 файлов)
- `user-note/application/application-h2-jpa/build.gradle.kts` — [REVIEW] — новый 2026-07-24, вендор БД — граница модуля вместо профиля (устраняет driver bloat: один драйвер на classpath вместо трёх сразу, см. decisions-log.md)
- `user-note/application/application-h2-jpa/src/main/java/com/example/usernote/package-info.java` — [DONE]
- `user-note/application/application-h2-jpa/src/main/java/com/example/usernote/UserNoteH2JpaApplication.java` — [REVIEW]
- `user-note/application/application-h2-jpa/src/main/resources/application.properties` — [REVIEW]
- `user-note/application/application-h2-jpa/src/test/resources/application.properties` — [REVIEW]
- `user-note/application/application-h2-jpa/src/test/java/com/example/usernote/UserNoteH2JpaApplicationTests.java` — [REVIEW]

#### user-note/application/application-mysql-jpa/ (7 файлов)
- `user-note/application/application-mysql-jpa/build.gradle.kts` — [REVIEW] — новый 2026-07-24, вендор БД — граница модуля вместо профиля (устраняет driver bloat: один драйвер на classpath вместо трёх сразу, см. decisions-log.md)
- `user-note/application/application-mysql-jpa/src/main/java/com/example/usernote/package-info.java` — [DONE]
- `user-note/application/application-mysql-jpa/src/main/java/com/example/usernote/UserNoteMysqlJpaApplication.java` — [REVIEW]
- `user-note/application/application-mysql-jpa/src/main/resources/application.properties` — [REVIEW]
- `user-note/application/application-mysql-jpa/compose.yaml` — [REVIEW]
- `user-note/application/application-mysql-jpa/src/test/resources/application.properties` — [REVIEW]
- `user-note/application/application-mysql-jpa/src/test/java/com/example/usernote/UserNoteMysqlJpaApplicationTests.java` — [REVIEW]

#### user-note/application/application-postgresql-jpa/ (7 файлов)
- `user-note/application/application-postgresql-jpa/build.gradle.kts` — [REVIEW] — новый 2026-07-24, вендор БД — граница модуля вместо профиля (устраняет driver bloat: один драйвер на classpath вместо трёх сразу, см. decisions-log.md)
- `user-note/application/application-postgresql-jpa/src/main/java/com/example/usernote/package-info.java` — [DONE]
- `user-note/application/application-postgresql-jpa/src/main/java/com/example/usernote/UserNotePostgresqlJpaApplication.java` — [REVIEW]
- `user-note/application/application-postgresql-jpa/src/main/resources/application.properties` — [REVIEW]
- `user-note/application/application-postgresql-jpa/compose.yaml` — [REVIEW]
- `user-note/application/application-postgresql-jpa/src/test/resources/application.properties` — [REVIEW]
- `user-note/application/application-postgresql-jpa/src/test/java/com/example/usernote/UserNotePostgresqlJpaApplicationTests.java` — [REVIEW]

#### user-note/application-jdbc/ (12 файлов) — [REMOVED] 2026-07-24, разбит на 3 вендорных модуля application-jdbc-{h2,mysql,postgresql}/ — устраняет driver bloat
- `user-note/application-jdbc/build.gradle.kts` — [REMOVED]
- `user-note/application-jdbc/src/test/resources/application.properties` — [REMOVED]
- `user-note/application-jdbc/src/test/java/com/example/usernote/UserNoteJdbcApplicationTests.java` — [REMOVED]
- `user-note/application-jdbc/src/main/resources/application.properties` — [REMOVED]
- `user-note/application-jdbc/src/main/java/com/example/usernote/package-info.java` — [REMOVED]
- `user-note/application-jdbc/src/main/java/com/example/usernote/UserNoteJdbcApplication.java` — [REMOVED]
- `user-note/application-jdbc/src/main/resources/application-postgresql.properties` — [REMOVED]
- `user-note/application-jdbc/src/main/resources/application-mysql.properties` — [REMOVED]
- `user-note/application-jdbc/compose-postgresql.yaml` — [REMOVED]
- `user-note/application-jdbc/compose-mysql.yaml` — [REMOVED]
- `user-note/application-jdbc/src/test/java/com/example/usernote/UserNotePostgresqlJdbcApplicationTests.java` — [REMOVED]
- `user-note/application-jdbc/src/test/java/com/example/usernote/UserNoteMysqlJdbcApplicationTests.java` — [REMOVED]

#### user-note/application/application-h2-jdbc/ (6 файлов)
- `user-note/application/application-h2-jdbc/build.gradle.kts` — [REVIEW] — новый 2026-07-24, вендор БД — граница модуля вместо профиля (устраняет driver bloat: один драйвер на classpath вместо трёх сразу, см. decisions-log.md)
- `user-note/application/application-h2-jdbc/src/main/java/com/example/usernote/package-info.java` — [DONE]
- `user-note/application/application-h2-jdbc/src/main/java/com/example/usernote/UserNoteH2JdbcApplication.java` — [REVIEW]
- `user-note/application/application-h2-jdbc/src/main/resources/application.properties` — [REVIEW]
- `user-note/application/application-h2-jdbc/src/test/resources/application.properties` — [REVIEW]
- `user-note/application/application-h2-jdbc/src/test/java/com/example/usernote/UserNoteH2JdbcApplicationTests.java` — [REVIEW]

#### user-note/application/application-mysql-jdbc/ (7 файлов)
- `user-note/application/application-mysql-jdbc/build.gradle.kts` — [REVIEW] — новый 2026-07-24, вендор БД — граница модуля вместо профиля (устраняет driver bloat: один драйвер на classpath вместо трёх сразу, см. decisions-log.md)
- `user-note/application/application-mysql-jdbc/src/main/java/com/example/usernote/package-info.java` — [DONE]
- `user-note/application/application-mysql-jdbc/src/main/java/com/example/usernote/UserNoteMysqlJdbcApplication.java` — [REVIEW]
- `user-note/application/application-mysql-jdbc/src/main/resources/application.properties` — [REVIEW]
- `user-note/application/application-mysql-jdbc/compose.yaml` — [REVIEW]
- `user-note/application/application-mysql-jdbc/src/test/resources/application.properties` — [REVIEW]
- `user-note/application/application-mysql-jdbc/src/test/java/com/example/usernote/UserNoteMysqlJdbcApplicationTests.java` — [REVIEW]

#### user-note/application/application-postgresql-jdbc/ (7 файлов)
- `user-note/application/application-postgresql-jdbc/build.gradle.kts` — [REVIEW] — новый 2026-07-24, вендор БД — граница модуля вместо профиля (устраняет driver bloat: один драйвер на classpath вместо трёх сразу, см. decisions-log.md)
- `user-note/application/application-postgresql-jdbc/src/main/java/com/example/usernote/package-info.java` — [DONE]
- `user-note/application/application-postgresql-jdbc/src/main/java/com/example/usernote/UserNotePostgresqlJdbcApplication.java` — [REVIEW]
- `user-note/application/application-postgresql-jdbc/src/main/resources/application.properties` — [REVIEW]
- `user-note/application/application-postgresql-jdbc/compose.yaml` — [REVIEW]
- `user-note/application/application-postgresql-jdbc/src/test/resources/application.properties` — [REVIEW]
- `user-note/application/application-postgresql-jdbc/src/test/java/com/example/usernote/UserNotePostgresqlJdbcApplicationTests.java` — [REVIEW]

#### user-note/application/application-mongodb/ (7 файлов)
- `user-note/application/application-mongodb/build.gradle.kts` — [REVIEW]
- `user-note/application/application-mongodb/src/test/resources/application.properties` — [REVIEW]
- `user-note/application/application-mongodb/src/test/java/com/example/usernote/UserNoteMongoApplicationTests.java` — [REVIEW]
- `user-note/application/application-mongodb/src/main/resources/application.properties` — [REVIEW]
- `user-note/application/application-mongodb/src/main/java/com/example/usernote/package-info.java` — [REVIEW]
- `user-note/application/application-mongodb/src/main/java/com/example/usernote/UserNoteMongoApplication.java` — [REVIEW]
- `user-note/application/application-mongodb/compose.yaml` — [REVIEW] — восстановлен 2026-07-23 (Testcontainers/Compose для Mongo вернули, теперь симметрично с PostgreSQL/MySQL)

#### user-note/application/application-mongodb-reactive/ (7 файлов)
- `user-note/application/application-mongodb-reactive/build.gradle.kts` — [REVIEW]
- `user-note/application/application-mongodb-reactive/src/test/resources/application.properties` — [REVIEW]
- `user-note/application/application-mongodb-reactive/src/test/java/com/example/usernote/UserNoteMongoReactiveApplicationTests.java` — [REVIEW]
- `user-note/application/application-mongodb-reactive/src/main/resources/application.properties` — [REVIEW]
- `user-note/application/application-mongodb-reactive/src/main/java/com/example/usernote/package-info.java` — [REVIEW]
- `user-note/application/application-mongodb-reactive/src/main/java/com/example/usernote/UserNoteMongoReactiveApplication.java` — [REVIEW]
- `user-note/application/application-mongodb-reactive/compose.yaml` — [REVIEW] — восстановлен 2026-07-23 (Testcontainers/Compose для Mongo вернули, теперь симметрично с PostgreSQL/MySQL)

#### user-note/ — предлагаемые отсутствующие файлы (`[ADD]`, 18)
- `user-note/presentation/webmvc/src/test/java/com/example/usernote/webmvc/UserNoteExceptionHandlerTest.java` — [ADD]
- `user-note/presentation/webmvc/src/test/java/com/example/usernote/webmvc/UserNoteControllerTest.java` — [ADD]
- `user-note/presentation/webflux/src/test/java/com/example/usernote/webflux/UserNoteExceptionHandlerTest.java` — [ADD]
- `user-note/presentation/webflux/src/test/java/com/example/usernote/webflux/UserNoteControllerTest.java` — [ADD]
- `user-note/domain/domain/src/test/java/com/example/usernote/domain/UserNoteNotFoundExceptionTest.java` — [ADD]
- `user-note/persistence/data-r2dbc/src/test/java/com/example/usernote/data/r2dbc/mapper/UserNoteR2dbcMapperTest.java` — [ADD]
- `user-note/persistence/data-r2dbc/src/test/java/com/example/usernote/data/r2dbc/adapter/UserNoteR2dbcAdapterIT.java` — [ADD]
- `user-note/persistence/data-mongodb/src/test/java/com/example/usernote/data/mongodb/mapper/UserNoteMongoMapperTest.java` — [ADD]
- `user-note/persistence/data-mongodb/src/test/java/com/example/usernote/data/mongodb/adapter/UserNoteMongoAdapterIT.java` — [ADD]
- `user-note/persistence/data-mongodb-reactive/src/test/java/com/example/usernote/data/mongodb/reactive/mapper/UserNoteMongoReactiveMapperTest.java` — [ADD]
- `user-note/persistence/data-mongodb-reactive/src/test/java/com/example/usernote/data/mongodb/reactive/adapter/UserNoteMongoReactiveAdapterIT.java` — [ADD]
- `user-note/persistence/data-jpa/src/test/java/com/example/usernote/data/jpa/mapper/UserNoteJpaMapperTest.java` — [ADD]
- `user-note/persistence/data-jpa/src/test/java/com/example/usernote/data/jpa/adapter/UserNoteJpaAdapterIT.java` — [ADD]
- `user-note/persistence/data-jdbc/src/test/java/com/example/usernote/data/jdbc/mapper/UserNoteJdbcMapperTest.java` — [ADD]
- `user-note/persistence/data-jdbc/src/test/java/com/example/usernote/data/jdbc/adapter/UserNoteJdbcAdapterIT.java` — [ADD]
- `user-note/application/application-h2-jpa/src/test/java/com/example/usernote/UserNoteEndToEndIT.java` — [ADD]

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

#### user/ — предлагаемые отсутствующие файлы (`[ADD]`, 13)
- `user/presentation/webmvc/src/test/java/com/example/user/webmvc/UserControllerTest.java` — [ADD]
- `user/presentation/webmvc/src/main/java/com/example/user/webmvc/UserController.java` — [ADD]
- `user/presentation/webflux/src/test/java/com/example/user/webflux/UserControllerTest.java` — [ADD]
- `user/presentation/webflux/src/main/java/com/example/user/webflux/UserController.java` — [ADD]
- `user/domain/domain/src/test/java/com/example/user/domain/UserNotFoundExceptionTest.java` — [ADD]
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
- `gradle/checkstyle/checkstyle.xml` — [REVIEW]

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

#### build-logic/convention/src/main/kotlin/ — precompiled script plugins (48 файлов, было 38)
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
- `build-logic/com.example.spring-cloud-gateway-webmvc.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-cloud-gateway-webflux.gradle.kts` — [REVIEW]
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
- `build-logic/com.example.spring-boot-actuator.gradle.kts` — [REMOVED]
- `build-logic/com.example.reactor.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-database-h2-r2dbc.gradle.kts` — [REVIEW] — переименован 2026-07-24 из `-r2dbc-h2` (группировка с `database-h2`)
- `build-logic/com.example.codequality-nullaway.gradle.kts` — [REVIEW]
- `build-logic/com.example.library.gradle.kts` — [REVIEW]
- `build-logic/com.example.javaformat.gradle.kts` — [REMOVED]
- `build-logic/com.example.codequality-jacoco.gradle.kts` — [REVIEW]
- `build-logic/com.example.codequality-jacoco-report-aggregation.gradle.kts` — [REVIEW]
- `build-logic/com.example.codequality.gradle.kts` — [REVIEW]
- `build-logic/com.example.codequality-checkstyle.gradle.kts` — [REVIEW]
- `build-logic/com.example.base.gradle.kts` — [REVIEW]

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
