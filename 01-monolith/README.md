# 01 · Monolith

Level: junior. Status: in progress.

## Change

The baseline: a classic layered Spring Boot application with server-rendered pages and one PostgreSQL database, the kind of application a junior writes first. A person creates, reads, edits and deletes notes in a browser. Everything else arrives one step per stage.

## Why

Every later stage is measured against a working, tested, conventional Spring application, so this stage contains only what such an application cannot work without.

## Tools

- Application: Gradle (Groovy DSL), Spring Boot starters with auto-configuration, Spring MVC with Thymeleaf. A note has an auto-increment primary key used inside the database, a unique UUID that identifies it everywhere outside the storage layer and is created with `UUID.randomUUID()`, and its content as text, which 03 renders as Markdown. Forms follow Post/Redirect/Get, and the message about the result survives the redirect as a flash attribute in the `HttpSession`. The stage keeps the `.gitignore` and `.gitattributes` that Spring Initializr generates, next to those at the root of the repository: they stay as generated and do not conflict with the root files.
- Data: Spring Data JPA on Hibernate with PostgreSQL started in a Docker container; the schema is written by hand in `schema-postgresql.sql`, named for its database so that a script for another database can stand next to it, which Spring Boot SQL initialization runs at every start (`spring.sql.init.mode=always`, `spring.sql.init.platform=postgresql`), and Hibernate only validates the entities against it (`spring.jpa.hibernate.ddl-auto=validate`). Until the first release the database holds only local test data, so the script always describes the current schema: a change is made in the script and the entity, and the table is dropped by hand to be created again at the next start; migrations are needed only once a released database holds data that must be kept; services are `@Transactional`. `spring.jpa.open-in-view` is set to `false`, against the default `true` that Spring Boot keeps for compatibility and warns about at startup: the `EntityManager` lives only inside a transaction of a service, so every query is made by a service and none during the rendering of a page, and a service loads the associations a page needs with `@EntityGraph` or `JOIN FETCH` instead of hidden lazy queries from the template, one per row.
- Tests: JUnit, Mockito, slice tests and page tests with MockMvc. The tests run on PostgreSQL in a container that Testcontainers starts for them, the same database the application runs on rather than an imitation of it, so the build needs Docker running but not the local database. `TestcontainersConfiguration` is the one Spring Initializr generates with Testcontainers selected: it declares the container as a bean with `@ServiceConnection`, from which Spring Boot takes the URL and the credentials. Its image is pinned to `postgres:18`, the same as in the `docker run` command below, instead of the generated `postgres:latest`, which changes without a change of the code. The tests use Testcontainers only: the application itself always runs against its real database, so the `TestNotesApplication` that Spring Initializr generates for `bootTestRun` is not kept. The image is declared in two places and kept equal by hand, as recorded in [ADR 0001](../docs/adr/0001-evolution-in-stages.md).
- Run: PostgreSQL is started once by hand with Docker, without Docker Compose, which comes in 22, and is restarted later with `docker start notes-postgres`; then `./gradlew bootRun` connects to it with the URL and credentials set in `application.properties`. These settings serve local development only: the connection leaves `application.properties` for Docker Compose support in 22, and the configuration and secrets of the test and production environments come from the environment in 24.

```
docker run -d --name notes-postgres -e POSTGRES_DB=notes -e POSTGRES_USER=notes -e POSTGRES_PASSWORD=notes -p 5432:5432 -v notes-postgres:/var/lib/postgresql postgres:18
```

## Open questions

A question moves into the sections above once it is decided.

- Editing and deleting from a form, which sends only `GET` and `POST`: separate `POST` endpoints such as `POST /notes/{id}/delete`, or the hidden method filter of Spring MVC (`spring.mvc.hiddenmethod.filter.enabled`, off by default).

## Known warnings

The warnings are ordered by how critical they are and resolved in that order; a resolved warning is removed.

1. Gradle declares no toolchain repository. The local Java 25 toolchain was installed by auto-provisioning, which Gradle has deprecated and which fails from Gradle 10; a clean machine does not find the toolchain. Resolved in 26, the first stage that builds on a clean machine.
2. Mockito attaches itself to the test JVM as an agent, and the JVM reports the dynamic agent loading and limits class data sharing. The warnings come from the default behaviour of the tools and are accepted, as recorded in [ADR 0001](../docs/adr/0001-evolution-in-stages.md).

## Proof

- The tests pass on PostgreSQL that Testcontainers starts, with the local database stopped.
- `bootRun` starts the application against PostgreSQL in the container.
