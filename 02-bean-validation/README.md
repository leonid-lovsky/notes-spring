# 02 · Bean Validation

Level: junior. Status: not started.

## Change

Note forms are validated: an empty title or a body above the size limit is rejected, and the form is shown again with a message next to the field.

## Why

In 01 any input reaches the database; an invalid note is either stored or fails with a database error and an error page.

## Tools

- Bean Validation constraints on the form objects, `@Valid` with `BindingResult` in the controllers, messages rendered by Thymeleaf.

## Proof

- The page tests pass.
- An invalid form is shown again with a message for every invalid field, and nothing is stored.
