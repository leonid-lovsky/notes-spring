# 01 · Monolith

Level: junior. Status: in progress.

## Change

The baseline: a classic layered Spring Boot application with server-rendered pages and one PostgreSQL database, the kind of application a junior writes first. A person creates, reads, edits and deletes notes in a browser. Everything else arrives one step per stage.

## Why

Every later stage is measured against a working, tested, conventional Spring application, so this stage contains only what such an application cannot work without.

## Tools

- Application: Gradle (Groovy DSL), Spring Boot starters with auto-configuration, Spring MVC with Thymeleaf. A note has an auto-increment primary key used inside the database, a unique UUID that identifies it everywhere outside the storage layer and is created with `UUID.randomUUID()`, and its content as text, which 03 renders as Markdown. Forms follow Post/Redirect/Get, and the message about the result survives the redirect as a flash attribute in the `HttpSession`.
- Data: Spring Data JPA on Hibernate with PostgreSQL started in a Docker container; the schema is written by hand in `schema-postgresql.sql`, named for its database so that a script for another database can stand next to it, which Spring Boot SQL initialization runs at every start (`spring.sql.init.mode=always`, `spring.sql.init.platform=postgresql`), and Hibernate only validates the entities against it (`spring.jpa.hibernate.ddl-auto=validate`); the SQL is valid on PostgreSQL and on H2. Until the first release the database holds only local test data, so the script always describes the current schema: a change is made in the script and the entity, and the table is dropped by hand to be created again at the next start; migrations are needed only once a released database holds data that must be kept; services are `@Transactional`.
- Tests: JUnit, Mockito, slice tests and page tests with MockMvc. H2 is a test dependency only, which Spring Boot puts in place of the configured database in JPA slice tests. `NotesApplicationTests` keeps the form Spring Initializr generates: it starts the whole application, so it runs against PostgreSQL in the container, which must be running for the build.
- Run: PostgreSQL is started once by hand with Docker, without Docker Compose, which comes in 22, and is restarted later with `docker start notes-postgres`; then `./gradlew bootRun` connects to it with the URL and credentials set in `application.properties`. These settings serve local development only: the connection leaves `application.properties` for Docker Compose support in 22, and the configuration and secrets of the test and production environments come from the environment in 25.

```
docker run -d --name notes-postgres -e POSTGRES_DB=notes -e POSTGRES_USER=notes -e POSTGRES_PASSWORD=notes -p 5432:5432 -v notes-postgres:/var/lib/postgresql postgres:18
```

## Open questions

A question moves into the sections above once it is decided.

- Editing and deleting from a form, which sends only `GET` and `POST`: separate `POST` endpoints such as `POST /notes/{id}/delete`, or the hidden method filter of Spring MVC (`spring.mvc.hiddenmethod.filter.enabled`, off by default).
- Files of Spring Initializr: the stage keeps the `.gitignore` and `.gitattributes` that Spring Initializr generates, next to those at the root of the repository. Whether the stages stay as close as possible to the output of Spring Initializr is decided for all stages together.

## Known warnings

The warnings are ordered by how critical they are and resolved in that order; a resolved warning is removed.

1. Gradle declares no toolchain repository. The local Java 25 toolchain was installed by auto-provisioning, which Gradle has deprecated and which fails from Gradle 10; a clean machine does not find the toolchain. Resolved in 27, the first stage that builds on a clean machine.
2. Spring Boot warns at startup that `spring.jpa.open-in-view` keeps its default, `true`. With `true` the `EntityManager` stays open for the whole request, so lazy associations load in the controller and the view, and the database connection is held until the response is written. With `false` it lives only inside a transaction, and a lazy association accessed outside it throws `LazyInitializationException`. The value is chosen once the services of the stage are written.
3. Mockito attaches itself to the test JVM as an agent, and the JVM reports the dynamic agent loading and limits class data sharing. The warnings come from the default behaviour of the tools and are accepted, as recorded in [ADR 0001](../docs/adr/0001-evolution-in-stages.md).

## Proof

- The page tests pass.
- `bootRun` starts the application against PostgreSQL in the container.
