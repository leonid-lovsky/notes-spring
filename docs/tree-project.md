# Дерево проекта

> **Назначение:** «дерево проекта» — ASCII-снимок структуры каталогов репозитория
> **Когда читать:** для ориентации в раскладке, при запросе «дерево проекта» (выводить дословно)
> **Статус:** снимок, может расходиться с диском — актуализировать по следующему запросу; авторитетный список — `docs/file-catalog.md`
> **Разделы:** ASCII-дерево
> **Связано:** `docs/file-catalog.md`, `docs/tree-modules.md`

> Наглядный снимок физической структуры каталогов репозитория, без `.git`/`build`/`.gradle`/`.idea`/`node_modules`. Присылать в этом виде по запросу «дерево проекта», без изменения формата. Не источник истины — авторитетный построчный список файлов со статусами `[DONE]`/`[REVIEW]`/`[ADD]`/`[REMOVED]` — `docs/file-catalog.md`. Снимок может расходиться с диском при следующей правке структуры — актуализировать по факту следующего запроса «дерево проекта», не держать в синхроне превентивно.

```
.
├── .claude/settings.local.json
├── .github/workflows/gradle.yml
├── build-logic/
│   ├── convention/
│   │   ├── src/main/kotlin/com.example.*.gradle.kts   (49 convention-плагинов)
│   │   └── build.gradle.kts
│   └── settings.gradle.kts
├── docs/
│   ├── convention-plugins-graph.md
│   ├── db-migration-tools-reference.md
│   ├── decisions-log.md
│   ├── file-catalog.md
│   ├── google-docs-full-model.md
│   ├── spring-boot-starters-full-matrix.md
│   ├── spring-boot-starters-reference.md
│   ├── spring-boot-testing-reference.md
│   ├── spring-data-repository-reference.md
│   ├── tech-glossary.md
│   ├── tree-project.md
│   ├── tree-repositories.md
│   └── tree-variants.md
├── gradle/
│   ├── checkstyle/google_checks.xml
│   ├── wrapper/{gradle-wrapper.jar,gradle-wrapper.properties}
│   └── libs.versions.toml
├── user-note/
│   ├── application-h2/                    (sync SQL, embedded, без Testcontainers)
│   ├── application-h2-reactive/           (reactive SQL, embedded)
│   ├── application-mysql/                 (sync SQL, Testcontainers)
│   ├── application-mysql-reactive/        (reactive SQL, Testcontainers)
│   ├── application-postgresql/            (sync SQL, Testcontainers)
│   ├── application-postgresql-reactive/   (reactive SQL, Testcontainers)
│   ├── application-mongodb/               (sync, Testcontainers)
│   ├── application-mongodb-reactive/      (reactive, Testcontainers)
│   ├── controller-webmvc/                 (пустой UserNoteController, @WebMvcTest)
│   ├── controller-webflux/                (пустой UserNoteController, @WebFluxTest)
│   ├── data-jdbc/                         (UserNote + UserNoteRepository, @DataJdbcTest)
│   ├── data-r2dbc/                        (UserNote + UserNoteRepository, @DataR2dbcTest)
│   ├── data-mongodb/                      (UserNote + UserNoteRepository, @DataMongoTest)
│   ├── data-mongodb-reactive/             (UserNote + UserNoteRepository, @DataMongoTest)
│   └── .main-class
├── .editorconfig
├── .gitattributes
├── .gitignore
├── .java-version
├── CLAUDE.md
├── gradle.properties
├── gradlew / gradlew.bat
└── settings.gradle.kts
```
