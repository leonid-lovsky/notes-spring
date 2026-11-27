# 04 · Pagination

Level: junior. Status: not started.

## Change

The list of notes is shown page by page in a stable order.

## Why

In 03 the list shows every note at once.

## Tools

`Pageable` and `Page` of Spring Data. The order comes from the time a note was created, kept in a field of its own, never from the identifier, whose order differs between storages.

## Proof

- The page tests pass.
- The list shows one page at a time with navigation between pages.
