# Каталог файлов проекта

> Вынесено из CLAUDE.md 2026-07-14 (см. [CLAUDE.md](CLAUDE.md) → «Правила» → лимит размера файла). Полный git-отслеживаемый список файлов; статусы: [DONE] — утверждено, [REVIEW] — требует пересмотра (по умолчанию), [ADD] — файла нет, предложен, [REMOVED] — удалён (строка сохранена для истории). Список путей/статусов не трогать без прямого запроса (см. CLAUDE.md → «Правила» → лимит размера файла).

### Корень репозитория (8 файлов)
- `settings.gradle.kts` — [DONE]
- `gradlew.bat` — [DONE]
- `gradlew` — [DONE]
- `gradle.properties` — [DONE]
- `CLAUDE.md` — [DONE]
- `.java-version` — [DONE]
- `.springjavaformatconfig` — [REMOVED]
- `.gitignore` — [DONE]
- `.gitattributes` — [DONE]
- `db-migration-tools-reference.md` — [REVIEW]

### user-note/ (115 файлов)

#### user-note/webmvc/ (5 файлов)
- `user-note/webmvc/build.gradle.kts` — [DONE]
- `user-note/webmvc/src/main/java/com/example/usernote/webmvc/package-info.java` — [DONE]
- `user-note/webmvc/src/main/java/com/example/usernote/webmvc/UserNoteExceptionHandler.java` — [REVIEW]
- `user-note/webmvc/src/main/java/com/example/usernote/webmvc/UserNoteControllerInterface.java` — [REVIEW]
- `user-note/webmvc/src/main/java/com/example/usernote/webmvc/UserNoteController.java` — [REVIEW]

#### user-note/webflux/ (5 файлов)
- `user-note/webflux/build.gradle.kts` — [DONE]
- `user-note/webflux/src/main/java/com/example/usernote/webflux/package-info.java` — [DONE]
- `user-note/webflux/src/main/java/com/example/usernote/webflux/UserNoteExceptionHandler.java` — [REVIEW]
- `user-note/webflux/src/main/java/com/example/usernote/webflux/UserNoteControllerReactiveInterface.java` — [REVIEW]
- `user-note/webflux/src/main/java/com/example/usernote/webflux/UserNoteController.java` — [REVIEW]

#### user-note/domain/ (9 файлов)
- `user-note/domain/build.gradle.kts` — [REVIEW]
- `user-note/domain/src/main/java/com/example/usernote/domain/package-info.java` — [DONE]
- `user-note/domain/src/main/java/com/example/usernote/domain/UserNoteRole.java` — [REVIEW]
- `user-note/domain/src/main/java/com/example/usernote/domain/UserNoteResponse.java` — [REVIEW]
- `user-note/domain/src/main/java/com/example/usernote/domain/UserNoteRequest.java` — [REVIEW]
- `user-note/domain/src/main/java/com/example/usernote/domain/UserNoteNotFoundException.java` — [REVIEW]
- `user-note/domain/src/main/java/com/example/usernote/domain/UserNotFoundException.java` — [REVIEW]
- `user-note/domain/src/main/java/com/example/usernote/domain/NoteNotFoundException.java` — [REVIEW]
- `user-note/domain/src/main/java/com/example/usernote/domain/UserNotePersistable.java` — [REVIEW]

#### user-note/data-r2dbc/ (11 файлов)
- `user-note/data-r2dbc/build.gradle.kts` — [DONE]
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/repository/package-info.java` — [DONE]
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/repository/UserNoteR2dbcRepository.java` — [REVIEW]
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/model/package-info.java` — [DONE]
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/model/UserNoteR2dbcEntity.java` — [REVIEW]
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/mapper/package-info.java` — [DONE]
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/mapper/UserNoteR2dbcMapperContract.java` — [REVIEW]
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/mapper/UserNoteR2dbcMapper.java` — [REVIEW]
- `user-note/data-r2dbc/src/main/resources/schema.sql` — [REVIEW]
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/adapter/package-info.java` — [DONE]
- `user-note/data-r2dbc/src/main/java/com/example/usernote/data/r2dbc/adapter/UserNoteService.java` — [REVIEW]

#### user-note/data-mongodb-reactive/ (10 файлов)
- `user-note/data-mongodb-reactive/build.gradle.kts` — [DONE]
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/repository/package-info.java` — [DONE]
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/repository/UserNoteMongoReactiveRepository.java` — [REVIEW]
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/model/package-info.java` — [DONE]
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/model/UserNoteReactiveDocument.java` — [REVIEW]
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/mapper/package-info.java` — [DONE]
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/mapper/UserNoteMongoReactiveMapperContract.java` — [REVIEW]
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/mapper/UserNoteMongoReactiveMapper.java` — [REVIEW]
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/adapter/package-info.java` — [DONE]
- `user-note/data-mongodb-reactive/src/main/java/com/example/usernote/data/mongodb/reactive/adapter/UserNoteService.java` — [REVIEW]

#### user-note/data-mongodb/ (10 файлов)
- `user-note/data-mongodb/build.gradle.kts` — [DONE]
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/repository/package-info.java` — [DONE]
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/repository/UserNoteMongoRepository.java` — [REVIEW]
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/model/package-info.java` — [DONE]
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/model/UserNoteDocument.java` — [REVIEW]
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/mapper/package-info.java` — [DONE]
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/mapper/UserNoteMongoMapperContract.java` — [REVIEW]
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/mapper/UserNoteMongoMapper.java` — [REVIEW]
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/adapter/package-info.java` — [DONE]
- `user-note/data-mongodb/src/main/java/com/example/usernote/data/mongodb/adapter/UserNoteService.java` — [REVIEW]

#### user-note/data-jpa/ (10 файлов)
- `user-note/data-jpa/build.gradle.kts` — [DONE]
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/repository/package-info.java` — [DONE]
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/repository/UserNoteJpaRepository.java` — [REVIEW]
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/model/package-info.java` — [DONE]
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/model/UserNoteEntity.java` — [REVIEW]
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/mapper/package-info.java` — [DONE]
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/mapper/UserNoteJpaMapperContract.java` — [REVIEW]
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/mapper/UserNoteJpaMapper.java` — [REVIEW]
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/adapter/package-info.java` — [DONE]
- `user-note/data-jpa/src/main/java/com/example/usernote/data/jpa/adapter/UserNoteService.java` — [REVIEW]

#### user-note/data-jdbc/ (11 файлов)
- `user-note/data-jdbc/build.gradle.kts` — [DONE]
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/repository/package-info.java` — [REVIEW]
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/repository/UserNoteJdbcRepository.java` — [REVIEW]
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/model/package-info.java` — [REVIEW]
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/model/UserNoteJdbcEntity.java` — [REVIEW]
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/mapper/package-info.java` — [DONE]
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/mapper/UserNoteJdbcMapperContract.java` — [REVIEW]
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/mapper/UserNoteJdbcMapper.java` — [REVIEW]
- `user-note/data-jdbc/src/main/resources/schema.sql` — [REVIEW]
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/adapter/package-info.java` — [DONE]
- `user-note/data-jdbc/src/main/java/com/example/usernote/data/jdbc/adapter/UserNoteService.java` — [REVIEW]

#### user-note/contract-reactive/ (4 файлов)
- `user-note/contract-reactive/build.gradle.kts` — [REVIEW]
- `user-note/contract-reactive/src/main/java/com/example/usernote/contract/reactive/package-info.java` — [DONE]
- `user-note/contract-reactive/src/main/java/com/example/usernote/contract/reactive/UserNoteReactiveInterface.java` — [REVIEW]
- `user-note/contract-reactive/src/main/java/com/example/usernote/contract/reactive/UserNoteServiceReactiveInterface.java` — [REVIEW]

#### user-note/contract/ (4 файлов)
- `user-note/contract/build.gradle.kts` — [REVIEW]
- `user-note/contract/src/main/java/com/example/usernote/contract/package-info.java` — [DONE]
- `user-note/contract/src/main/java/com/example/usernote/contract/UserNoteInterface.java` — [REVIEW]
- `user-note/contract/src/main/java/com/example/usernote/contract/UserNoteServiceInterface.java` — [REVIEW]

#### user-note/application-r2dbc/ (8 файлов)
- `user-note/application-r2dbc/build.gradle.kts` — [REVIEW]
- `user-note/application-r2dbc/src/test/resources/application.properties` — [REVIEW]
- `user-note/application-r2dbc/src/test/java/com/example/usernote/UserNoteR2dbcApplicationTests.java` — [REVIEW]
- `user-note/application-r2dbc/src/main/resources/application.properties` — [REVIEW]
- `user-note/application-r2dbc/src/main/java/com/example/usernote/package-info.java` — [REVIEW]
- `user-note/application-r2dbc/src/main/java/com/example/usernote/UserNoteR2dbcApplication.java` — [REVIEW]
- `user-note/application-r2dbc/src/main/resources/application-postgresql.properties` — [REVIEW]
- `user-note/application-r2dbc/src/main/resources/application-mysql.properties` — [REVIEW]

#### user-note/application-jpa/ (8 файлов)
- `user-note/application-jpa/build.gradle.kts` — [REVIEW]
- `user-note/application-jpa/src/test/resources/application.properties` — [REVIEW]
- `user-note/application-jpa/src/test/java/com/example/usernote/UserNoteJpaApplicationTests.java` — [REVIEW]
- `user-note/application-jpa/src/main/resources/application.properties` — [REVIEW]
- `user-note/application-jpa/src/main/java/com/example/usernote/package-info.java` — [DONE]
- `user-note/application-jpa/src/main/java/com/example/usernote/UserNoteJpaApplication.java` — [REVIEW]
- `user-note/application-jpa/src/main/resources/application-postgresql.properties` — [REVIEW]
- `user-note/application-jpa/src/main/resources/application-mysql.properties` — [REVIEW]

#### user-note/application-jdbc/ (8 файлов)
- `user-note/application-jdbc/build.gradle.kts` — [REVIEW]
- `user-note/application-jdbc/src/test/resources/application.properties` — [REVIEW]
- `user-note/application-jdbc/src/test/java/com/example/usernote/UserNoteJdbcApplicationTests.java` — [REVIEW]
- `user-note/application-jdbc/src/main/resources/application.properties` — [REVIEW]
- `user-note/application-jdbc/src/main/java/com/example/usernote/package-info.java` — [REVIEW]
- `user-note/application-jdbc/src/main/java/com/example/usernote/UserNoteJdbcApplication.java` — [REVIEW]
- `user-note/application-jdbc/src/main/resources/application-postgresql.properties` — [REVIEW]
- `user-note/application-jdbc/src/main/resources/application-mysql.properties` — [REVIEW]

#### user-note/application-mongodb/ (6 файлов)
- `user-note/application-mongodb/build.gradle.kts` — [REVIEW]
- `user-note/application-mongodb/src/test/resources/application.properties` — [REVIEW]
- `user-note/application-mongodb/src/test/java/com/example/usernote/UserNoteMongoApplicationTests.java` — [REVIEW]
- `user-note/application-mongodb/src/main/resources/application.properties` — [REVIEW]
- `user-note/application-mongodb/src/main/java/com/example/usernote/package-info.java` — [REVIEW]
- `user-note/application-mongodb/src/main/java/com/example/usernote/UserNoteMongoApplication.java` — [REVIEW]

#### user-note/application-mongodb-reactive/ (6 файлов)
- `user-note/application-mongodb-reactive/build.gradle.kts` — [REVIEW]
- `user-note/application-mongodb-reactive/src/test/resources/application.properties` — [REVIEW]
- `user-note/application-mongodb-reactive/src/test/java/com/example/usernote/UserNoteMongoReactiveApplicationTests.java` — [REVIEW]
- `user-note/application-mongodb-reactive/src/main/resources/application.properties` — [REVIEW]
- `user-note/application-mongodb-reactive/src/main/java/com/example/usernote/package-info.java` — [REVIEW]
- `user-note/application-mongodb-reactive/src/main/java/com/example/usernote/UserNoteMongoReactiveApplication.java` — [REVIEW]

#### user-note/ — предлагаемые отсутствующие файлы (`[ADD]`, 18)
- `user-note/webmvc/src/test/java/com/example/usernote/webmvc/UserNoteExceptionHandlerTest.java` — [ADD]
- `user-note/webmvc/src/test/java/com/example/usernote/webmvc/UserNoteControllerTest.java` — [ADD]
- `user-note/webflux/src/test/java/com/example/usernote/webflux/UserNoteExceptionHandlerTest.java` — [ADD]
- `user-note/webflux/src/test/java/com/example/usernote/webflux/UserNoteControllerTest.java` — [ADD]
- `user-note/domain/src/test/java/com/example/usernote/domain/UserNoteNotFoundExceptionTest.java` — [ADD]
- `user-note/data-r2dbc/src/test/java/com/example/usernote/data/r2dbc/mapper/UserNoteR2dbcMapperTest.java` — [ADD]
- `user-note/data-r2dbc/src/test/java/com/example/usernote/data/r2dbc/adapter/UserNoteR2dbcAdapterIT.java` — [ADD]
- `user-note/data-mongodb/src/test/java/com/example/usernote/data/mongodb/mapper/UserNoteMongoMapperTest.java` — [ADD]
- `user-note/data-mongodb/src/test/java/com/example/usernote/data/mongodb/adapter/UserNoteMongoAdapterIT.java` — [ADD]
- `user-note/data-mongodb-reactive/src/test/java/com/example/usernote/data/mongodb/reactive/mapper/UserNoteMongoReactiveMapperTest.java` — [ADD]
- `user-note/data-mongodb-reactive/src/test/java/com/example/usernote/data/mongodb/reactive/adapter/UserNoteMongoReactiveAdapterIT.java` — [ADD]
- `user-note/data-jpa/src/test/java/com/example/usernote/data/jpa/mapper/UserNoteJpaMapperTest.java` — [ADD]
- `user-note/data-jpa/src/test/java/com/example/usernote/data/jpa/adapter/UserNoteJpaAdapterIT.java` — [ADD]
- `user-note/data-jdbc/src/test/java/com/example/usernote/data/jdbc/mapper/UserNoteJdbcMapperTest.java` — [ADD]
- `user-note/data-jdbc/src/test/java/com/example/usernote/data/jdbc/adapter/UserNoteJdbcAdapterIT.java` — [ADD]
- `user-note/application-jpa/src/test/java/com/example/usernote/UserNoteEndToEndIT.java` — [ADD]

### user/ (112 файлов)

#### user/webmvc/ (5 файлов)
- `user/webmvc/build.gradle.kts` — [DONE]
- `user/webmvc/src/main/java/com/example/user/webmvc/package-info.java` — [DONE]
- `user/webmvc/src/main/java/com/example/user/webmvc/UserExceptionHandler.java` — [REVIEW]
- `user/webmvc/src/main/java/com/example/user/webmvc/UserControllerInterface.java` — [REVIEW]
- `user/webmvc/src/main/java/com/example/user/webmvc/UserController.java` — [REVIEW]

#### user/webflux/ (5 файлов)
- `user/webflux/build.gradle.kts` — [DONE]
- `user/webflux/src/main/java/com/example/user/webflux/package-info.java` — [DONE]
- `user/webflux/src/main/java/com/example/user/webflux/UserExceptionHandler.java` — [REVIEW]
- `user/webflux/src/main/java/com/example/user/webflux/UserControllerReactiveInterface.java` — [REVIEW]
- `user/webflux/src/main/java/com/example/user/webflux/UserController.java` — [REVIEW]

#### user/domain/ (6 файлов)
- `user/domain/build.gradle.kts` — [REVIEW]
- `user/domain/src/main/java/com/example/user/domain/package-info.java` — [DONE]
- `user/domain/src/main/java/com/example/user/domain/UserResponse.java` — [REVIEW]
- `user/domain/src/main/java/com/example/user/domain/UserRequest.java` — [REVIEW]
- `user/domain/src/main/java/com/example/user/domain/UserNotFoundException.java` — [REVIEW]
- `user/domain/src/main/java/com/example/user/domain/UserPersistable.java` — [REVIEW]

#### user/data-r2dbc/ (11 файлов)
- `user/data-r2dbc/build.gradle.kts` — [DONE]
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/repository/package-info.java` — [DONE]
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/repository/UserR2dbcRepository.java` — [REVIEW]
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/model/package-info.java` — [DONE]
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/model/UserR2dbcEntity.java` — [REVIEW]
- `user/data-r2dbc/src/main/resources/schema.sql` — [REVIEW]
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/mapper/package-info.java` — [DONE]
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/mapper/UserR2dbcMapperContract.java` — [REVIEW]
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/mapper/UserR2dbcMapper.java` — [REVIEW]
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/adapter/package-info.java` — [DONE]
- `user/data-r2dbc/src/main/java/com/example/user/data/r2dbc/adapter/UserService.java` — [REVIEW]

#### user/data-mongodb-reactive/ (10 файлов)
- `user/data-mongodb-reactive/build.gradle.kts` — [DONE]
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/repository/package-info.java` — [DONE]
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/repository/UserMongoReactiveRepository.java` — [REVIEW]
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/model/package-info.java` — [DONE]
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/model/UserReactiveDocument.java` — [REVIEW]
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/mapper/package-info.java` — [DONE]
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/mapper/UserMongoReactiveMapperContract.java` — [REVIEW]
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/mapper/UserMongoReactiveMapper.java` — [REVIEW]
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/adapter/package-info.java` — [DONE]
- `user/data-mongodb-reactive/src/main/java/com/example/user/data/mongodb/reactive/adapter/UserService.java` — [REVIEW]

#### user/data-mongodb/ (10 файлов)
- `user/data-mongodb/build.gradle.kts` — [DONE]
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/repository/package-info.java` — [DONE]
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/repository/UserMongoRepository.java` — [REVIEW]
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/model/package-info.java` — [DONE]
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/model/UserDocument.java` — [REVIEW]
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/mapper/package-info.java` — [DONE]
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/mapper/UserMongoMapperContract.java` — [REVIEW]
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/mapper/UserMongoMapper.java` — [REVIEW]
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/adapter/package-info.java` — [DONE]
- `user/data-mongodb/src/main/java/com/example/user/data/mongodb/adapter/UserService.java` — [REVIEW]

#### user/data-jpa/ (10 файлов)
- `user/data-jpa/build.gradle.kts` — [DONE]
- `user/data-jpa/src/main/java/com/example/user/data/jpa/repository/package-info.java` — [DONE]
- `user/data-jpa/src/main/java/com/example/user/data/jpa/repository/UserJpaRepository.java` — [REVIEW]
- `user/data-jpa/src/main/java/com/example/user/data/jpa/model/package-info.java` — [DONE]
- `user/data-jpa/src/main/java/com/example/user/data/jpa/model/UserEntity.java` — [REVIEW]
- `user/data-jpa/src/main/java/com/example/user/data/jpa/mapper/package-info.java` — [DONE]
- `user/data-jpa/src/main/java/com/example/user/data/jpa/mapper/UserJpaMapperContract.java` — [REVIEW]
- `user/data-jpa/src/main/java/com/example/user/data/jpa/mapper/UserJpaMapper.java` — [REVIEW]
- `user/data-jpa/src/main/java/com/example/user/data/jpa/adapter/package-info.java` — [DONE]
- `user/data-jpa/src/main/java/com/example/user/data/jpa/adapter/UserService.java` — [REVIEW]

#### user/data-jdbc/ (11 файлов)
- `user/data-jdbc/build.gradle.kts` — [DONE]
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/repository/package-info.java` — [REVIEW]
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/repository/UserJdbcRepository.java` — [REVIEW]
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/model/package-info.java` — [REVIEW]
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/model/UserJdbcEntity.java` — [REVIEW]
- `user/data-jdbc/src/main/resources/schema.sql` — [REVIEW]
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/mapper/package-info.java` — [DONE]
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/mapper/UserJdbcMapperContract.java` — [REVIEW]
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/mapper/UserJdbcMapper.java` — [REVIEW]
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/adapter/package-info.java` — [DONE]
- `user/data-jdbc/src/main/java/com/example/user/data/jdbc/adapter/UserService.java` — [REVIEW]

#### user/contract-reactive/ (4 файлов)
- `user/contract-reactive/build.gradle.kts` — [REVIEW]
- `user/contract-reactive/src/main/java/com/example/user/contract/reactive/package-info.java` — [DONE]
- `user/contract-reactive/src/main/java/com/example/user/contract/reactive/UserReactiveInterface.java` — [REVIEW]
- `user/contract-reactive/src/main/java/com/example/user/contract/reactive/UserServiceReactiveInterface.java` — [REVIEW]

#### user/contract/ (4 файлов)
- `user/contract/build.gradle.kts` — [REVIEW]
- `user/contract/src/main/java/com/example/user/contract/package-info.java` — [DONE]
- `user/contract/src/main/java/com/example/user/contract/UserInterface.java` — [REVIEW]
- `user/contract/src/main/java/com/example/user/contract/UserServiceInterface.java` — [REVIEW]

#### user/application-r2dbc/ (8 файлов)
- `user/application-r2dbc/build.gradle.kts` — [REVIEW]
- `user/application-r2dbc/src/test/resources/application.properties` — [REVIEW]
- `user/application-r2dbc/src/test/java/com/example/user/UserR2dbcApplicationTests.java` — [REVIEW]
- `user/application-r2dbc/src/main/resources/application.properties` — [REVIEW]
- `user/application-r2dbc/src/main/java/com/example/user/package-info.java` — [REVIEW]
- `user/application-r2dbc/src/main/java/com/example/user/UserR2dbcApplication.java` — [REVIEW]
- `user/application-r2dbc/src/main/resources/application-postgresql.properties` — [REVIEW]
- `user/application-r2dbc/src/main/resources/application-mysql.properties` — [REVIEW]

#### user/application-jpa/ (8 файлов)
- `user/application-jpa/build.gradle.kts` — [REVIEW]
- `user/application-jpa/src/test/resources/application.properties` — [REVIEW]
- `user/application-jpa/src/test/java/com/example/user/UserJpaApplicationTests.java` — [REVIEW]
- `user/application-jpa/src/main/resources/application.properties` — [REVIEW]
- `user/application-jpa/src/main/java/com/example/user/package-info.java` — [DONE]
- `user/application-jpa/src/main/java/com/example/user/UserJpaApplication.java` — [REVIEW]
- `user/application-jpa/src/main/resources/application-postgresql.properties` — [REVIEW]
- `user/application-jpa/src/main/resources/application-mysql.properties` — [REVIEW]

#### user/application-jdbc/ (8 файлов)
- `user/application-jdbc/build.gradle.kts` — [REVIEW]
- `user/application-jdbc/src/test/resources/application.properties` — [REVIEW]
- `user/application-jdbc/src/test/java/com/example/user/UserJdbcApplicationTests.java` — [REVIEW]
- `user/application-jdbc/src/main/resources/application.properties` — [REVIEW]
- `user/application-jdbc/src/main/java/com/example/user/package-info.java` — [REVIEW]
- `user/application-jdbc/src/main/java/com/example/user/UserJdbcApplication.java` — [REVIEW]
- `user/application-jdbc/src/main/resources/application-postgresql.properties` — [REVIEW]
- `user/application-jdbc/src/main/resources/application-mysql.properties` — [REVIEW]

#### user/application-mongodb/ (6 файлов)
- `user/application-mongodb/build.gradle.kts` — [REVIEW]
- `user/application-mongodb/src/test/resources/application.properties` — [REVIEW]
- `user/application-mongodb/src/test/java/com/example/user/UserMongoApplicationTests.java` — [REVIEW]
- `user/application-mongodb/src/main/resources/application.properties` — [REVIEW]
- `user/application-mongodb/src/main/java/com/example/user/package-info.java` — [REVIEW]
- `user/application-mongodb/src/main/java/com/example/user/UserMongoApplication.java` — [REVIEW]

#### user/application-mongodb-reactive/ (6 файлов)
- `user/application-mongodb-reactive/build.gradle.kts` — [REVIEW]
- `user/application-mongodb-reactive/src/test/resources/application.properties` — [REVIEW]
- `user/application-mongodb-reactive/src/test/java/com/example/user/UserMongoReactiveApplicationTests.java` — [REVIEW]
- `user/application-mongodb-reactive/src/main/resources/application.properties` — [REVIEW]
- `user/application-mongodb-reactive/src/main/java/com/example/user/package-info.java` — [REVIEW]
- `user/application-mongodb-reactive/src/main/java/com/example/user/UserMongoReactiveApplication.java` — [REVIEW]

#### user/ — предлагаемые отсутствующие файлы (`[ADD]`, 13)
- `user/webmvc/src/test/java/com/example/user/webmvc/UserControllerTest.java` — [ADD]
- `user/webmvc/src/main/java/com/example/user/webmvc/UserController.java` — [ADD]
- `user/webflux/src/test/java/com/example/user/webflux/UserControllerTest.java` — [ADD]
- `user/webflux/src/main/java/com/example/user/webflux/UserController.java` — [ADD]
- `user/domain/src/test/java/com/example/user/domain/UserNotFoundExceptionTest.java` — [ADD]
- `user/data-r2dbc/src/test/java/com/example/user/data/r2dbc/adapter/UserR2dbcAdapterTest.java` — [ADD]
- `user/data-mongodb/src/test/java/com/example/user/data/mongodb/adapter/UserMongoAdapterTest.java` — [ADD]
- `user/data-mongodb-reactive/src/test/java/com/example/user/data/mongodb/reactive/adapter/UserMongoReactiveAdapterTest.java` — [ADD]
- `user/data-jpa/src/test/java/com/example/user/data/jpa/adapter/UserJpaAdapterTest.java` — [ADD]
- `user/data-jdbc/src/test/java/com/example/user/data/jdbc/adapter/UserJdbcAdapterTest.java` — [ADD]

### registry/ (6 файлов)

#### registry/application/ (6 файлов)
- `registry/build.gradle.kts` — [REVIEW]
- `registry/src/test/resources/application.properties` — [REVIEW]
- `registry/src/test/java/com/example/registry/RegistryApplicationTests.java` — [REVIEW]
- `registry/src/main/resources/application.properties` — [REVIEW]
- `registry/application/src/main/java/com/example/registry/package-info.java` — [DONE]
- `registry/application/src/main/java/com/example/registry/RegistryApplication.java` — [REVIEW]

### note/ (112 файлов)

#### note/webmvc/ (5 файлов)
- `note/webmvc/build.gradle.kts` — [DONE]
- `note/webmvc/src/main/java/com/example/note/webmvc/package-info.java` — [DONE]
- `note/webmvc/src/main/java/com/example/note/webmvc/NoteExceptionHandler.java` — [REVIEW]
- `note/webmvc/src/main/java/com/example/note/webmvc/NoteControllerInterface.java` — [REVIEW]
- `note/webmvc/src/main/java/com/example/note/webmvc/NoteController.java` — [REVIEW]

#### note/webflux/ (5 файлов)
- `note/webflux/build.gradle.kts` — [DONE]
- `note/webflux/src/main/java/com/example/note/webflux/package-info.java` — [DONE]
- `note/webflux/src/main/java/com/example/note/webflux/NoteExceptionHandler.java` — [REVIEW]
- `note/webflux/src/main/java/com/example/note/webflux/NoteControllerReactiveInterface.java` — [REVIEW]
- `note/webflux/src/main/java/com/example/note/webflux/NoteController.java` — [REVIEW]

#### note/domain/ (6 файлов)
- `note/domain/build.gradle.kts` — [REVIEW]
- `note/domain/src/main/java/com/example/note/domain/package-info.java` — [DONE]
- `note/domain/src/main/java/com/example/note/domain/NoteResponse.java` — [REVIEW]
- `note/domain/src/main/java/com/example/note/domain/NoteRequest.java` — [REVIEW]
- `note/domain/src/main/java/com/example/note/domain/NoteNotFoundException.java` — [REVIEW]
- `note/domain/src/main/java/com/example/note/domain/NotePersistable.java` — [REVIEW]

#### note/data-r2dbc/ (11 файлов)
- `note/data-r2dbc/build.gradle.kts` — [DONE]
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/repository/package-info.java` — [DONE]
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/repository/NoteR2dbcRepository.java` — [REVIEW]
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/model/package-info.java` — [DONE]
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/model/NoteR2dbcEntity.java` — [REVIEW]
- `note/data-r2dbc/src/main/resources/schema.sql` — [REVIEW]
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/mapper/package-info.java` — [DONE]
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/mapper/NoteR2dbcMapperContract.java` — [REVIEW]
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/mapper/NoteR2dbcMapper.java` — [REVIEW]
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/adapter/package-info.java` — [DONE]
- `note/data-r2dbc/src/main/java/com/example/note/data/r2dbc/adapter/NoteService.java` — [REVIEW]

#### note/data-mongodb-reactive/ (10 файлов)
- `note/data-mongodb-reactive/build.gradle.kts` — [DONE]
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/repository/package-info.java` — [DONE]
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/repository/NoteMongoReactiveRepository.java` — [REVIEW]
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/model/package-info.java` — [DONE]
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/model/NoteReactiveDocument.java` — [REVIEW]
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/mapper/package-info.java` — [DONE]
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/mapper/NoteMongoReactiveMapperContract.java` — [REVIEW]
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/mapper/NoteMongoReactiveMapper.java` — [REVIEW]
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/adapter/package-info.java` — [DONE]
- `note/data-mongodb-reactive/src/main/java/com/example/note/data/mongodb/reactive/adapter/NoteService.java` — [REVIEW]

#### note/data-mongodb/ (10 файлов)
- `note/data-mongodb/build.gradle.kts` — [DONE]
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/repository/package-info.java` — [DONE]
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/repository/NoteMongoRepository.java` — [REVIEW]
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/model/package-info.java` — [DONE]
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/model/NoteDocument.java` — [REVIEW]
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/mapper/package-info.java` — [DONE]
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/mapper/NoteMongoMapperContract.java` — [REVIEW]
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/mapper/NoteMongoMapper.java` — [REVIEW]
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/adapter/package-info.java` — [DONE]
- `note/data-mongodb/src/main/java/com/example/note/data/mongodb/adapter/NoteService.java` — [REVIEW]

#### note/data-jpa/ (10 файлов)
- `note/data-jpa/build.gradle.kts` — [DONE]
- `note/data-jpa/src/main/java/com/example/note/data/jpa/repository/package-info.java` — [DONE]
- `note/data-jpa/src/main/java/com/example/note/data/jpa/repository/NoteJpaRepository.java` — [REVIEW]
- `note/data-jpa/src/main/java/com/example/note/data/jpa/model/package-info.java` — [DONE]
- `note/data-jpa/src/main/java/com/example/note/data/jpa/model/NoteEntity.java` — [REVIEW]
- `note/data-jpa/src/main/java/com/example/note/data/jpa/mapper/package-info.java` — [DONE]
- `note/data-jpa/src/main/java/com/example/note/data/jpa/mapper/NoteJpaMapperContract.java` — [REVIEW]
- `note/data-jpa/src/main/java/com/example/note/data/jpa/mapper/NoteJpaMapper.java` — [REVIEW]
- `note/data-jpa/src/main/java/com/example/note/data/jpa/adapter/package-info.java` — [DONE]
- `note/data-jpa/src/main/java/com/example/note/data/jpa/adapter/NoteService.java` — [REVIEW]

#### note/data-jdbc/ (11 файлов)
- `note/data-jdbc/build.gradle.kts` — [DONE]
- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/repository/package-info.java` — [REVIEW]
- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/repository/NoteJdbcRepository.java` — [REVIEW]
- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/model/package-info.java` — [REVIEW]
- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/model/NoteJdbcEntity.java` — [REVIEW]
- `note/data-jdbc/src/main/resources/schema.sql` — [REVIEW]
- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/mapper/package-info.java` — [DONE]
- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/mapper/NoteJdbcMapperContract.java` — [REVIEW]
- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/mapper/NoteJdbcMapper.java` — [REVIEW]
- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/adapter/package-info.java` — [DONE]
- `note/data-jdbc/src/main/java/com/example/note/data/jdbc/adapter/NoteService.java` — [REVIEW]

#### note/contract-reactive/ (4 файлов)
- `note/contract-reactive/build.gradle.kts` — [REVIEW]
- `note/contract-reactive/src/main/java/com/example/note/contract/reactive/package-info.java` — [DONE]
- `note/contract-reactive/src/main/java/com/example/note/contract/reactive/NoteReactiveInterface.java` — [REVIEW]
- `note/contract-reactive/src/main/java/com/example/note/contract/reactive/NoteServiceReactiveInterface.java` — [REVIEW]

#### note/contract/ (4 файлов)
- `note/contract/build.gradle.kts` — [REVIEW]
- `note/contract/src/main/java/com/example/note/contract/package-info.java` — [DONE]
- `note/contract/src/main/java/com/example/note/contract/NoteInterface.java` — [REVIEW]
- `note/contract/src/main/java/com/example/note/contract/NoteServiceInterface.java` — [REVIEW]

#### note/application-r2dbc/ (8 файлов)
- `note/application-r2dbc/build.gradle.kts` — [REVIEW]
- `note/application-r2dbc/src/test/resources/application.properties` — [REVIEW]
- `note/application-r2dbc/src/test/java/com/example/note/NoteR2dbcApplicationTests.java` — [REVIEW]
- `note/application-r2dbc/src/main/resources/application.properties` — [REVIEW]
- `note/application-r2dbc/src/main/java/com/example/note/package-info.java` — [REVIEW]
- `note/application-r2dbc/src/main/java/com/example/note/NoteR2dbcApplication.java` — [REVIEW]
- `note/application-r2dbc/src/main/resources/application-postgresql.properties` — [REVIEW]
- `note/application-r2dbc/src/main/resources/application-mysql.properties` — [REVIEW]

#### note/application-jpa/ (8 файлов)
- `note/application-jpa/build.gradle.kts` — [REVIEW]
- `note/application-jpa/src/test/resources/application.properties` — [REVIEW]
- `note/application-jpa/src/test/java/com/example/note/NoteJpaApplicationTests.java` — [REVIEW]
- `note/application-jpa/src/main/resources/application.properties` — [REVIEW]
- `note/application-jpa/src/main/java/com/example/note/package-info.java` — [DONE]
- `note/application-jpa/src/main/java/com/example/note/NoteJpaApplication.java` — [REVIEW]
- `note/application-jpa/src/main/resources/application-postgresql.properties` — [REVIEW]
- `note/application-jpa/src/main/resources/application-mysql.properties` — [REVIEW]

#### note/application-jdbc/ (8 файлов)
- `note/application-jdbc/build.gradle.kts` — [REVIEW]
- `note/application-jdbc/src/test/resources/application.properties` — [REVIEW]
- `note/application-jdbc/src/test/java/com/example/note/NoteJdbcApplicationTests.java` — [REVIEW]
- `note/application-jdbc/src/main/resources/application.properties` — [REVIEW]
- `note/application-jdbc/src/main/java/com/example/note/package-info.java` — [REVIEW]
- `note/application-jdbc/src/main/java/com/example/note/NoteJdbcApplication.java` — [REVIEW]
- `note/application-jdbc/src/main/resources/application-postgresql.properties` — [REVIEW]
- `note/application-jdbc/src/main/resources/application-mysql.properties` — [REVIEW]

#### note/application-mongodb/ (6 файлов)
- `note/application-mongodb/build.gradle.kts` — [REVIEW]
- `note/application-mongodb/src/test/resources/application.properties` — [REVIEW]
- `note/application-mongodb/src/test/java/com/example/note/NoteMongoApplicationTests.java` — [REVIEW]
- `note/application-mongodb/src/main/resources/application.properties` — [REVIEW]
- `note/application-mongodb/src/main/java/com/example/note/package-info.java` — [REVIEW]
- `note/application-mongodb/src/main/java/com/example/note/NoteMongoApplication.java` — [REVIEW]

#### note/application-mongodb-reactive/ (6 файлов)
- `note/application-mongodb-reactive/build.gradle.kts` — [REVIEW]
- `note/application-mongodb-reactive/src/test/resources/application.properties` — [REVIEW]
- `note/application-mongodb-reactive/src/test/java/com/example/note/NoteMongoReactiveApplicationTests.java` — [REVIEW]
- `note/application-mongodb-reactive/src/main/resources/application.properties` — [REVIEW]
- `note/application-mongodb-reactive/src/main/java/com/example/note/package-info.java` — [REVIEW]
- `note/application-mongodb-reactive/src/main/java/com/example/note/NoteMongoReactiveApplication.java` — [REVIEW]

#### note/ — предлагаемые отсутствующие файлы (`[ADD]`, 19)
- `note/webmvc/src/test/java/com/example/note/webmvc/NoteExceptionHandlerTest.java` — [ADD]
- `note/webmvc/src/test/java/com/example/note/webmvc/NoteControllerTest.java` — [ADD]
- `note/webflux/src/test/java/com/example/note/webflux/NoteExceptionHandlerTest.java` — [ADD]
- `note/webflux/src/test/java/com/example/note/webflux/NoteControllerTest.java` — [ADD]
- `note/domain/src/test/java/com/example/note/domain/NoteNotFoundExceptionTest.java` — [ADD]
- `note/data-r2dbc/src/test/java/com/example/note/data/r2dbc/mapper/NoteR2dbcMapperTest.java` — [ADD]
- `note/data-r2dbc/src/test/java/com/example/note/data/r2dbc/adapter/NoteR2dbcAdapterIntegrationTest.java` — [ADD]
- `note/data-mongodb/src/test/java/com/example/note/data/mongodb/mapper/NoteMongoMapperTest.java` — [ADD]
- `note/data-mongodb/src/test/java/com/example/note/data/mongodb/adapter/NoteMongoAdapterIntegrationTest.java` — [ADD]
- `note/data-mongodb-reactive/src/test/java/com/example/note/data/mongodb/reactive/mapper/NoteMongoReactiveMapperTest.java` — [ADD]
- `note/data-mongodb-reactive/src/test/java/com/example/note/data/mongodb/reactive/adapter/NoteMongoReactiveAdapterIntegrationTest.java` — [ADD]
- `note/data-jpa/src/test/java/com/example/note/data/jpa/mapper/NoteJpaMapperTest.java` — [ADD]
- `note/data-jpa/src/test/java/com/example/note/data/jpa/adapter/NoteJpaAdapterIntegrationTest.java` — [ADD]
- `note/data-jdbc/src/test/java/com/example/note/data/jdbc/mapper/NoteJdbcMapperTest.java` — [ADD]
- `note/data-jdbc/src/test/java/com/example/note/data/jdbc/adapter/NoteJdbcAdapterIntegrationTest.java` — [ADD]
- `note/application-jpa/src/test/java/com/example/note/NoteCreateEndpointIntegrationTest.java` — [ADD]

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

### build-logic/ (47 файлов, было 40 — добавлены 7 плагинов оси вендора/Testcontainers/dev-режима 2026-07-23)

#### build-logic/ — корневые файлы (2 файлов)
- `build-logic/settings.gradle.kts` — [REVIEW]
- `build-logic/convention/build.gradle.kts` — [REVIEW]

#### build-logic/convention/src/main/kotlin/ — precompiled script plugins (45 файлов, было 38)
- `build-logic/com.example.spring-boot-postgresql-database.gradle.kts` — [REVIEW] — новый 2026-07-23, вендорная ось для JPA/JDBC (`runtimeOnly org.postgresql:postgresql`), активируется через Spring-профиль
- `build-logic/com.example.spring-boot-mysql-database.gradle.kts` — [REVIEW] — новый 2026-07-23, `runtimeOnly com.mysql:mysql-connector-j`
- `build-logic/com.example.spring-boot-r2dbc-postgresql-database.gradle.kts` — [REVIEW] — новый 2026-07-23, `runtimeOnly org.postgresql:r2dbc-postgresql`
- `build-logic/com.example.spring-boot-r2dbc-mysql-database.gradle.kts` — [REVIEW] — новый 2026-07-23, `runtimeOnly io.asyncer:r2dbc-mysql`
- `build-logic/com.example.spring-boot-testcontainers.gradle.kts` — [REVIEW] — новый 2026-07-23, технологически нейтральная обвязка Testcontainers+JUnit5+`@ServiceConnection` (родитель `spring-boot`); выделен из `-testcontainers-mongodb` в тот же день — не должен быть привязан к одной технологии, пригоден для будущих Postgres/Kafka/Redis-контейнеров
- `build-logic/com.example.spring-boot-testcontainers-mongodb.gradle.kts` — [REVIEW] — новый 2026-07-23, только MongoDB-специфичный артефакт `testcontainers-mongodb` (родитель `spring-boot`); применяется вместе с `-testcontainers` в `application-mongodb*`
- `build-logic/com.example.spring-boot-docker-compose.gradle.kts` — [REVIEW] — новый 2026-07-23, dev-запуск `application-mongodb*`; родитель `spring-boot` + прямой `id("org.springframework.boot")` (как у `spring-boot-application`, не через него — исправлено в тот же день по замечанию пользователя о композиции)
- `build-logic/com.example.jspecify.gradle.kts` — [REVIEW] — новый 2026-07-15, вынесен из `nullaway` (`id("java-library")` + `api("org.jspecify")`), применяется через `codequality`
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
- `build-logic/com.example.spring-boot-oauth2-resource-server.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-oauth2-client.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-oauth2-authorization-server.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-h2-database.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-graphql.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-data-r2dbc.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-data-mongodb.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-data-mongodb-reactive.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-data-jpa.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-data-jdbc.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-data-elasticsearch.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-client-web.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-client-rest.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-application.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-actuator.gradle.kts` — [REMOVED]
- `build-logic/com.example.reactor.gradle.kts` — [REVIEW]
- `build-logic/com.example.spring-boot-r2dbc-h2-database.gradle.kts` — [REVIEW]
- `build-logic/com.example.nullaway.gradle.kts` — [REVIEW]
- `build-logic/com.example.library.gradle.kts` — [REVIEW]
- `build-logic/com.example.javaformat.gradle.kts` — [REMOVED]
- `build-logic/com.example.jacoco.gradle.kts` — [REVIEW]
- `build-logic/com.example.jacoco-report-aggregation.gradle.kts` — [REVIEW]
- `build-logic/com.example.codequality.gradle.kts` — [REVIEW]
- `build-logic/com.example.checkstyle.gradle.kts` — [REVIEW]
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
