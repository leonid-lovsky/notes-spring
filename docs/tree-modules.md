# Дерево модулей

> «Дерево модулей» — снимок всех листовых Gradle-модулей `user-note/` (единственный сервис с исходным кодом сейчас) с их пакетом и полным списком напрямую применённых convention-плагинов, в порядке `id(...)` из `build.gradle.kts`. Корень дерева — не каталог, а пакет: `com.example.usernote` побайтово одинаков во всех 16 модулей (проверено по `package-info.java`, см. `CLAUDE.md` → «Открытые решения»/обсуждение плоской структуры `user-note`) — различие между модулями живёт на границе Gradle-модуля/jar, не на границе Java-пакета. Присылать этот блок дословно по запросу «дерево модулей», без переформатирования. Источник истины при расхождении — сами `build.gradle.kts`; сверка: `for f in user-note/*/build.gradle.kts; do echo "$(dirname "$f" | xargs basename):"; grep -oP 'id\("com\.example\.[^"]+"\)' "$f"; done`.

```
com.example.usernote
│
├── contract                          — id("com.example.contract")
├── contract-reactive                 — id("com.example.contract-reactive")
│
├── controller-webmvc                 — id("com.example.spring-boot-webmvc")
├── controller-webflux                — id("com.example.spring-boot-webflux")
│
├── data-jdbc                         — id("com.example.spring-boot-data-jdbc"), id("com.example.spring-boot-test-h2")
├── data-r2dbc                        — id("com.example.spring-boot-data-r2dbc"), id("com.example.spring-boot-test-r2dbc-h2")
├── data-mongodb                      — id("com.example.spring-boot-data-mongodb")
├── data-mongodb-reactive             — id("com.example.spring-boot-data-mongodb-reactive")
│
├── application-h2                    — id("com.example.spring-boot"), id("com.example.spring-boot-bootable"), id("com.example.spring-boot-actuator"), id("com.example.spring-boot-webmvc"), id("com.example.spring-boot-data-jdbc"), id("com.example.spring-boot-database-h2")
├── application-h2-reactive           — id("com.example.spring-boot"), id("com.example.spring-boot-bootable"), id("com.example.spring-boot-actuator"), id("com.example.spring-boot-webflux"), id("com.example.spring-boot-data-r2dbc"), id("com.example.spring-boot-database-r2dbc-h2")
├── application-mysql                 — id("com.example.spring-boot"), id("com.example.spring-boot-bootable"), id("com.example.spring-boot-actuator"), id("com.example.spring-boot-webmvc"), id("com.example.spring-boot-data-jdbc"), id("com.example.spring-boot-database-mysql"), id("com.example.spring-boot-testcontainers"), id("com.example.spring-boot-testcontainers-mysql")
├── application-mysql-reactive        — id("com.example.spring-boot"), id("com.example.spring-boot-bootable"), id("com.example.spring-boot-actuator"), id("com.example.spring-boot-webflux"), id("com.example.spring-boot-data-r2dbc"), id("com.example.spring-boot-database-r2dbc-mysql"), id("com.example.spring-boot-testcontainers"), id("com.example.spring-boot-testcontainers-r2dbc"), id("com.example.spring-boot-testcontainers-mysql")
├── application-postgresql            — id("com.example.spring-boot"), id("com.example.spring-boot-bootable"), id("com.example.spring-boot-actuator"), id("com.example.spring-boot-webmvc"), id("com.example.spring-boot-data-jdbc"), id("com.example.spring-boot-database-postgresql"), id("com.example.spring-boot-testcontainers"), id("com.example.spring-boot-testcontainers-postgresql")
├── application-postgresql-reactive   — id("com.example.spring-boot"), id("com.example.spring-boot-bootable"), id("com.example.spring-boot-actuator"), id("com.example.spring-boot-webflux"), id("com.example.spring-boot-data-r2dbc"), id("com.example.spring-boot-database-r2dbc-postgresql"), id("com.example.spring-boot-testcontainers"), id("com.example.spring-boot-testcontainers-r2dbc"), id("com.example.spring-boot-testcontainers-postgresql")
├── application-mongodb               — id("com.example.spring-boot"), id("com.example.spring-boot-bootable"), id("com.example.spring-boot-actuator"), id("com.example.spring-boot-webmvc"), id("com.example.spring-boot-data-mongodb"), id("com.example.spring-boot-testcontainers"), id("com.example.spring-boot-testcontainers-mongodb")
└── application-mongodb-reactive      — id("com.example.spring-boot"), id("com.example.spring-boot-bootable"), id("com.example.spring-boot-actuator"), id("com.example.spring-boot-webflux"), id("com.example.spring-boot-data-mongodb-reactive"), id("com.example.spring-boot-testcontainers"), id("com.example.spring-boot-testcontainers-mongodb")
```

Каждый лист дополнительно наследует всё поддерево `com.example.base`→`com.example.codequality`→8 `codequality-*` (Checkstyle/JaCoCo(+report-aggregation)/jspecify/NullAway/PMD/SpotBugs/Spotless) транзитивно через свой прямой родитель — не показано отдельной веткой здесь, чтобы не дублировать `docs/convention-plugins-graph.md` (полный граф родитель→потомок всех convention-плагинов, источник истины по составу каждого плагина). 5 исключений из правила «имя плагина = официальное имя технологии» (`java`/`contract`/`contract-reactive`/`codequality`/`spring-boot-bootable` — названы придуманным проектом словом для роли, не заимствованным у технологии; у Spring Boot официальный термин — «executable jar/archive», не «bootable») — `CLAUDE.md` → «Правила».
