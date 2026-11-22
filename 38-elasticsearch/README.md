# 38 · Elasticsearch

Level: middle. Status: not started.

## Change

Notes are found by full text in a separate read model fed by note events.

## Why

In 37 notes can only be listed, and a search inside the database would differ per vendor.

## Tools

Elasticsearch through Spring Data Elasticsearch.

## Proof

- The acceptance tests pass.
- Search ranks by relevance, and the index catches up after an outage.
