# 38 · Elasticsearch

Level: middle. Status: not started.

## Change

A search module finds notes by full text, ranked by relevance and tolerant to typos. Its index is a separate read model, kept in a search engine and fed by note events.

## Why

In 37 notes can only be listed. A full-text query inside the database would have to be written twice, once for PostgreSQL and once for MySQL; a search engine works the same on both.

## Tools

- Elasticsearch through Spring Data Elasticsearch. The search module indexes notes from note events, so the event publication registry of 37 guarantees that the index catches up after an outage.
- Tests: the acceptance tests of the capability search, polling the API.

## Proof

- The acceptance tests pass.
- Search ranks results by relevance and tolerates typos.
- After Elasticsearch was down, the index catches up with all changes.
