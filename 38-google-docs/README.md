# 38 · Google Docs

Level: middle. Status: not started.

## Change

A user can export a note to their Google Docs.

## Why

In 37 a note cannot leave the application.

## Tools

- OAuth2 Client with Google as a second provider, used only to obtain access to the Google Docs API of the user; the HTTP client of the API is chosen when the stage starts.
- Tests: the Google Docs API is replaced by a stub; the acceptance tests do not cover the export, because it needs a real Google account.

## Proof

- The acceptance tests pass.
- An exported note appears in Google Docs with its formatting.
