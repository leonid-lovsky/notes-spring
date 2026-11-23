# 22 · Docker Compose

Level: junior. Status: not started.

## Change

One Compose file describes the stage: `bootRun` starts PostgreSQL from it, and a Compose profile starts the whole stage; the database connection leaves `application.properties`.

## Why

In 21 the database container is started and wired by hand.

## Tools

Spring Boot Docker Compose support.

## Proof

- The acceptance tests pass.
- `bootRun` starts PostgreSQL by itself, and the acceptance tests pass against the stage started by Compose.
