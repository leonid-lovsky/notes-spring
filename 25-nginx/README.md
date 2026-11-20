# 25 · NGINX

Level: junior. Status: not started.

## Change

NGINX terminates TLS in front of the application and forwards requests to it; the application learns the original scheme and host from forwarded headers.

## Why

In 24 the application is reached over plain HTTP on its own port, which cannot face the internet, and nothing can spread requests over several instances.

## Tools

- NGINX in the Compose file with a self-signed certificate locally and a certificate of a public authority on Amazon EC2; Spring Boot support for forwarded headers.

## Proof

- The acceptance tests pass. The suite reaches the system over HTTPS.
- Links built by the application use the external scheme and host.
