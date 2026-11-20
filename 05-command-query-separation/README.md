# 05 · Command–Query Separation

Level: junior. Status: not started.

## Change

Service methods follow command–query separation: a command changes state and returns at most the identifier of what it created, a query returns data and changes nothing. Controllers render a page by a query after the command.

## Why

In 04 service methods both change notes and return them, so a reader cannot tell from a signature whether a call has side effects, and reads cannot be optimised separately from writes.

## Tools

- Plain Spring services split into command and query methods; read-only transactions (`@Transactional(readOnly = true)`) for queries.

## Proof

- The page tests pass.
- No query method changes state, checked by tests on read-only transactions.
