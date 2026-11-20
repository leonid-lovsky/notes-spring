# 16 · REST Assured

Level: junior. Status: not started.

## Change

A black-box acceptance test suite talks to the running system over HTTP only. From this stage it runs against every stage and fixes the contract of the API: later stages only add to it.

## Why

In 15 the API is tested only from inside the application; nothing proves that the next stages keep its behaviour.

## Tools

- The separate Gradle project `acceptance-tests/` with JUnit, REST Assured and Awaitility, the capabilities notes, sharing, notifications and live feed; asynchronous behaviour is verified by polling the API; the suite knows only the base URL and the credentials of a test user.

## Proof

- The acceptance tests pass.
- The suite runs against the application started with `bootRun`.
