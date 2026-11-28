# 15 · Email Notifications

Level: junior. Status: not started.

## Change

A user also receives notifications by email.

## Why

In 14 a notification reaches a user only while a page of the application is open.

## Tools

Spring Boot mail support with `JavaMailSender`, called by a listener of the application events of 12 after the commit; Mailpit as the mail server, in a Docker container for `bootRun` and started by Testcontainers in the tests.

## Proof

- The page tests pass.
- Sharing a note sends an email that arrives in Mailpit.
- A failed email does not roll back the sharing.
