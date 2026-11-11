# 11 · Elasticsearch

Level: middle. Status: not started.

## Change

The search module moves from the PostgreSQL full-text query to Elasticsearch.

## Why

In 10 search in PostgreSQL has neither relevance ranking nor typo tolerance.

## Tools

- Elasticsearch through Spring Data Elasticsearch. The search module indexes notes from note events, so the event publication registry of 04 guarantees that the index catches up after an outage.

## Proof

- The acceptance tests pass.
- Search ranks results by relevance and tolerates typos.
- After Elasticsearch was down, the index catches up with all changes.
