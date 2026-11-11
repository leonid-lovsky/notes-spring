# 04 · OpenAPI

Level: junior. Status: not started.

## Change

The API also publishes an OpenAPI description with an interactive console.

## Why

In 03 the documentation is readable by people but not by tools: clients cannot generate code from it or try requests in a browser.

## Tools

- springdoc-openapi with Swagger UI.

## Proof

- The acceptance tests pass.
- The OpenAPI description validates, and every endpoint can be called from Swagger UI.
