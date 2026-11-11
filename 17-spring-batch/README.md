# 17 · Spring Batch

Level: middle. Status: not started.

## Change

The search index can be rebuilt from the database by a batch job that processes notes in chunks and restarts from the last committed chunk after a failure.

## Why

In 16 the index is fed only by events; after a change of the index mapping or a lost index there is no way to rebuild it from the data.

## Tools

- Spring Batch with its job repository in PostgreSQL; a chunk-oriented step reading notes through Spring Data JPA and writing them to Elasticsearch; the job is started on demand and observed through Actuator.

## Proof

- The acceptance tests pass.
- The job rebuilds an empty index from the database.
- A job interrupted in the middle continues from the last committed chunk.
