# 22 · Docker Compose

Level: junior. Status: not started.

## Change

One Compose file describes the stage. PostgreSQL in it is started by Spring Boot Docker Compose support during `bootRun`, so the database no longer has to be installed on the developer machine; the application image runs from the same file under a Compose profile, so `docker compose up` with that profile starts the whole stage.

## Why

In 21 the database is installed by hand on every machine, and the application container and its database are started and wired together by hand.

## Tools

- Spring Boot Docker Compose support (`spring-boot-docker-compose`, development only); Compose profiles for the services that `bootRun` must not start, checked against the Spring Boot reference when the stage starts; start order driven by the Actuator health checks of 19.

## Proof

- The acceptance tests pass against the stage started from the Compose file.
- `bootRun` starts the application with its database on a machine without a local PostgreSQL.
