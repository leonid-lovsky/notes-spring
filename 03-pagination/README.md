# 03 · Pagination

Level: junior. Status: not started.

## Change

The list of notes is shown page by page, sorted by title, with navigation between pages.

## Why

In 02 the list shows every note at once, and the page grows without limit with the number of notes.

## Tools

- `Pageable` and `Page` of Spring Data in the repository and the service, page navigation in Thymeleaf.

## Proof

- The page tests pass.
- The list shows one page of notes at a time in a stable order.
