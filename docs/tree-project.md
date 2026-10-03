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
├── note/                                  (с 2026-09-29 — группы модулей в подкаталогах, путь проекта `:note:<группа>:<модуль>`)
│   ├── contract/     contract-commons/ (модели), contract-synchronous/, contract-reactive/ (порты)
│   ├── controller/   controller-webmvc/, controller-webflux/ (контроллер, слайс-тест)
│   ├── service/      service-commons/ (запись), service-synchronous/, service-reactive/ (репозиторий, реализации портов; цель — чистая Java; технологию даёт лист)
│   └── application/  application-{h2,mysql,postgresql,mongodb}[-reactive]/ (8 листьев, composition root)
├── user/                                  (с 2026-09-29 — группы модулей в подкаталогах, путь проекта `:user:<группа>:<модуль>`)
│   ├── contract/     contract-commons/ (модели), contract-synchronous/, contract-reactive/ (порты)
│   ├── controller/   controller-webmvc/, controller-webflux/ (контроллер, слайс-тест)
│   ├── service/      service-commons/ (запись), service-synchronous/, service-reactive/ (репозиторий, реализации портов; цель — чистая Java; технологию даёт лист)
│   └── application/  application-{h2,mysql,postgresql,mongodb}[-reactive]/ (8 листьев, composition root)
├── user-note/                                  (с 2026-09-29 — группы модулей в подкаталогах, путь проекта `:user-note:<группа>:<модуль>`)
│   ├── contract/     contract-commons/ (модели), contract-synchronous/, contract-reactive/ (порты)
│   ├── controller/   controller-webmvc/, controller-webflux/ (контроллер, слайс-тест)
│   ├── service/      service-commons/ (запись), service-synchronous/, service-reactive/ (репозиторий, реализации портов; цель — чистая Java; технологию даёт лист)
│   └── application/  application-{h2,mysql,postgresql,mongodb}[-reactive]/ (8 листьев, composition root)
├── .editorconfig
├── .gitattributes
├── .gitignore
├── .java-version
├── CLAUDE.md
├── gradle.properties
├── gradlew / gradlew.bat
└── settings.gradle.kts
```
