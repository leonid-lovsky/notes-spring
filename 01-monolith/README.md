# 01 · Monolith

Level: junior. Status: in progress.

## Change

The baseline: a classic layered Spring Boot application with server-rendered pages and one PostgreSQL database, the kind of application a junior writes first. A person creates, reads, edits and deletes notes in a browser. Everything else arrives one step per stage.

## Why

Every later stage is measured against a working, tested, conventional Spring application, so this stage contains only what such an application cannot work without.

## Tools

- Application: Gradle (Groovy DSL), Spring Boot starters with auto-configuration, Spring MVC with Thymeleaf; identifiers are UUIDs. Forms follow Post/Redirect/Get, and the message about the result survives the redirect as a flash attribute in the `HttpSession`.
- Data: Spring Data JPA on Hibernate with PostgreSQL installed on the developer machine; Hibernate generates the schema from the entities, so the stage contains no SQL; services are `@Transactional`.
- Tests: JUnit, Mockito, slice tests and page tests with MockMvc on the embedded H2. H2 is a test dependency only: Spring Boot configures it when no database URL is set and puts it in place of the configured database in JPA slice tests.
- Run: `./gradlew bootRun` against the local PostgreSQL, whose URL and credentials are set in `application.properties`.

## Open questions

A question moves into the sections above once it is decided.

- Database connection: without the URL of the local PostgreSQL, `bootRun` fails at startup with "Failed to determine a suitable driver class", because H2 is on the test classpath only. The URL and the credentials are the first step of the code: in `application.properties`, the simplest way, which keeps the local password in the repository until 25, or in environment variables such as `SPRING_DATASOURCE_URL`, which must be set before every run.
- Editing and deleting from a form, which sends only `GET` and `POST`: separate `POST` endpoints such as `POST /notes/{id}/delete`, or the hidden method filter of Spring MVC (`spring.mvc.hiddenmethod.filter.enabled`, off by default).
- UUID generation: `@GeneratedValue(strategy = GenerationType.UUID)`, where Hibernate creates the value on save, or `UUID.randomUUID()` in the constructor, where the value exists before saving. Version 7 UUIDs are ordered by time and suit an index better than random version 4 UUIDs.
- Schema settings, to be confirmed against the Spring Boot reference: `spring.jpa.hibernate.ddl-auto` defaults to `create-drop` for an embedded database and to `none` otherwise, so on PostgreSQL Hibernate creates nothing until the property is set, for example to `update`.
- Files of Spring Initializr: the stage keeps the `.gitignore` and `.gitattributes` that Spring Initializr generates, next to those at the root of the repository. Whether the stages stay as close as possible to the output of Spring Initializr is decided for all stages together.

## Known warnings

The warnings are ordered by how critical they are and resolved in that order; a resolved warning is removed.

1. Gradle declares no toolchain repository. The local Java 25 toolchain was installed by auto-provisioning, which Gradle has deprecated and which fails from Gradle 10; a clean machine does not find the toolchain. Resolved in 27, the first stage that builds on a clean machine.
2. Spring Boot warns at startup that `spring.jpa.open-in-view` keeps its default, `true`. With `true` the `EntityManager` stays open for the whole request, so lazy associations load in the controller and the view, and the database connection is held until the response is written. With `false` it lives only inside a transaction, and a lazy association accessed outside it throws `LazyInitializationException`. The value is chosen once the services of the stage are written.
3. Mockito attaches itself to the test JVM as an agent, and the JVM reports the dynamic agent loading and limits class data sharing. The warnings come from the default behaviour of the tools and are accepted, as recorded in [ADR 0001](../docs/adr/0001-evolution-in-stages.md).

## Proof

- The page tests pass.
- `bootRun` starts the application against the local database.
