# 33 · Spring AI

Level: architect. Status: not started.

## Change

Search gains semantic matching: notes are turned into embeddings and found by meaning next to the full-text search, behind the same search API.

## Why

In 32 search finds words, not meaning: a note about a meeting is not found by a query for an appointment. The stage is about weighing the quality of results against the cost and latency of a model.

## Tools

- Spring AI with an embedding model run locally through Ollama and the Elasticsearch vector store; embeddings are computed by the search module from note events.

## Proof

- The acceptance tests pass.
- A query finds notes by meaning, and a test with fixed embeddings checks the ranking.
