# 24 · NGINX

Level: junior. Status: not started.

## Change

NGINX terminates TLS in front of the application.

## Why

In 23 the application is reached over plain HTTP on its own port.

## Tools

NGINX in the Compose file, Spring Boot support for forwarded headers.

## Proof

- The acceptance tests pass.
- The suite reaches the system over HTTPS, and links use the external host.
