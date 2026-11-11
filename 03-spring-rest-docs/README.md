# 03 · Spring REST Docs

Level: junior. Status: not started.

## Change

The HTTP API is documented from its tests: every documented request and response is a passing test.

## Why

In 02 the API is described nowhere except in the code, and a description written by hand would drift from the implementation.

## Tools

- Spring REST Docs with MockMvc; Asciidoctor renders the documentation during the build.

## Proof

- The acceptance tests pass.
- A change of the API that is not reflected in the documentation fails a test.
