# 01 · Monolith

Level: junior. Status: in progress.

## Change

The baseline: an elementary, classic layered Spring Boot application with server-rendered pages, one PostgreSQL database and one transaction per request — the kind of application a junior writes first. A person creates, reads, edits and deletes notes in a browser. Everything else — validation, Markdown, users, sharing, the REST API, containers and the infrastructure around them — arrives one step per stage in the following stages.

## Why

Every later stage is measured against a working, tested, conventional Spring application. The stage contains only what such an application cannot work without.

## Tools

- Application: Gradle (Groovy DSL), Spring Boot starters with auto-configuration, Spring MVC with Thymeleaf; identifiers are UUIDs. Command–query separation comes in 05.
- Session: the server keeps state in the `HttpSession` of the servlet container. A form is submitted with `POST` and answered with a redirect (Post/Redirect/Get), so reloading a page does not submit the form again; the message about the result survives the redirect as a flash attribute, which Spring MVC keeps in the session. Sign-in comes in 06.
- Data: Spring Data JPA on Hibernate, PostgreSQL installed on the developer machine; Hibernate generates the schema from the entities (`spring.jpa.hibernate.ddl-auto`), so the stage contains no SQL; services are `@Transactional`. A schema written in SQL, which the entities are validated against, comes in 08.
- Open Session in View: `spring.jpa.open-in-view` is set explicitly, because Spring Boot logs a warning at startup while the property keeps its default. With `true`, the default, the JPA `EntityManager` stays bound to the thread for the whole request: lazy associations are loaded outside the service transaction — in the controller and while the page is rendered — and a database connection, once taken, is held until the response is written. With `false`, the `EntityManager` lives only inside a transaction, and a lazy association accessed outside it throws `LazyInitializationException`. The stage uses `false`: services load everything a page needs inside their transactions, so no query runs outside the service layer.
- Tests: JUnit, Mockito, slice tests, page tests with MockMvc. The tests run on the embedded H2, which Spring Boot puts in place of the configured database by default. Testcontainers replaces H2 in 23.
- Run: `./gradlew bootRun` with a local PostgreSQL whose URL and credentials are set in `application.properties`. Docker Compose starts the database in 22.

## Open questions

The questions below are decided while the stage is developed; a decided question moves into the sections above.

- Database connection: Spring Initializr adds H2 with runtime scope, so without a URL of the local PostgreSQL in `application.properties` `bootRun` silently starts the application on the embedded H2. The URL and the credentials are the first step of the code. They can be set in `application.properties` (`spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password`), the simplest way, which puts the password of the local database into the repository until `25-twelve-factor-app`, or in environment variables such as `SPRING_DATASOURCE_URL`, which keep it out of the repository but must be set before every run; the database, its user and its password may all be named `notes` after the application.
- Editing and deleting notes from a form, which can send only `GET` and `POST`: separate `POST` endpoints, such as `POST /notes/{id}/delete`, or the hidden method filter of Spring MVC (`spring.mvc.hiddenmethod.filter.enabled`, off by default), which turns a hidden `_method` field into `PUT` or `DELETE`.
- Generation of the UUID identifiers: the standard strategy of Jakarta Persistence, `@GeneratedValue(strategy = GenerationType.UUID)`, where Hibernate creates the value on save; a value created by the application, such as `UUID.randomUUID()` in the constructor of the entity, which exists before the entity is saved; or a default of the database, which needs a schema written in SQL and valid on both H2 and PostgreSQL. Random UUIDs of version 4 land at random places of an index, while UUIDs of version 7 are ordered by time.
- Schema settings and their combinations, to be confirmed against the Spring Boot reference: `spring.jpa.hibernate.ddl-auto` defaults to `create-drop` for an embedded database such as H2 when no schema manager such as Flyway is present, and to `none` otherwise, so on the local PostgreSQL Hibernate creates nothing until the property is set, for example to `update` (which never removes or renames anything) or `create`; `spring.sql.init.mode` defaults to `embedded`, so `schema.sql` of 08 runs on H2 but not on PostgreSQL until it is set to `always`; when `schema.sql` and the schema generation of Hibernate run together, `spring.jpa.defer-datasource-initialization` decides which runs first; from 08 the schema comes from SQL and `ddl-auto` is `validate`.

## Known warnings

The build, the tests and the running application emit the warnings below, ordered by how critical they are. They are resolved in that order as the application is developed; a resolved warning is removed from the list.

1. Gradle: the build declares no toolchain repository. The Java 25 toolchain on the developer machine was installed earlier by auto-provisioning, which Gradle has deprecated and which fails with an error from Gradle 10; on a clean machine, such as a CI runner, the toolchain is not found.
2. Mockito and the JVM, in the tests: Mockito attaches itself as an agent to the running JVM, the JVM reports the dynamic agent loading, and class data sharing is limited to boot loader classes. These warnings come from the default behaviour of the tools and are accepted; the reason and the configuration that would remove them are recorded in [ADR 0001](../docs/adr/0001-evolution-in-stages.md).

## Proof

- The page tests pass.
- `bootRun` starts the application against the local database.
