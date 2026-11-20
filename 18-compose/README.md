# 18 · Compose

Level: junior. Status: not started.

## Change

The whole stage — the application image and its database — runs from one Compose file, with a TLS reverse proxy in front of the application.

## Why

In 17 the application container and its database are started and wired together by hand.

## Tools

- Compose file of the whole stage with start order driven by the Actuator health checks of 16; a TLS reverse proxy for Amazon EC2.

## Proof

- The acceptance tests pass against the stage started from the Compose file.
- One command starts the whole stage.
