# 21 · Docker Compose

Level: junior. Status: not started.

## Change

One Compose file describes the stage: `bootRun` starts the server of the selected DBMS from it, and a Compose profile starts the whole stage; the connection leaves the profile files.

## Why

In 20 the DBMS server container is started and wired by hand.

## Tools

Spring Boot Docker Compose support.

## Proof

- The acceptance tests pass.
- `bootRun` starts the server of the selected DBMS by itself, and the acceptance tests pass against the stage started by Compose.
