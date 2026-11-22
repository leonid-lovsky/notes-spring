# 02 · Jakarta Validation

Level: junior. Status: not started.

## Change

Note forms are validated, and an invalid form is shown again with a message next to each invalid field.

## Why

In 01 any input reaches the database and fails there, if at all.

## Tools

Jakarta Validation with `@Valid` and `BindingResult` in the controllers.

## Proof

- The page tests pass.
- An invalid form stores nothing and shows a message for every invalid field.
