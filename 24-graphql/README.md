# 24 · GraphQL

Level: senior. Status: not started.

## Change

A GraphQL API is added next to the unchanged REST API for reads that combine data of several services, such as the notes of a user with their access rights and the names of their owners.

## Why

In 23 a client needs several REST calls to several services to build one screen.

## Tools

- Spring for GraphQL in an aggregation service behind the gateway, fetching from the services through their HTTP clients with batching.
- Tests: `GraphQlTester`.

## Proof

- The acceptance tests pass.
- One GraphQL query returns what took several REST calls before.
