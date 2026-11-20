# 41 · React

Level: senior. Status: not started.

## Change

A single-page application in React works with the same REST API behind the gateway, next to the server-rendered pages.

## Why

In 40 the browser interface reloads a page for every action.

## Tools

- React built by its own toolchain inside the stage; the gateway signs the browser in and relays the token, so the application never holds a token.

## Proof

- The acceptance tests pass.
- Every function of the pages is available in the single-page application.
