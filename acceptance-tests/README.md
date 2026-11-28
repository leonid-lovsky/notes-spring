# Acceptance tests

Status: not started.

One black-box test suite runs against every stage from 18 and proves that behaviour defined in an earlier stage does not change. It talks to a running system over HTTP only and does not know how the system is built.

## Boundary

The suite receives as parameters the base URL of the system, the credentials of a test user, the capabilities to run and, for a system that accepts tokens, the issuer URL. It knows only the public API: HTTP, JSON, Server-Sent Events and OAuth 2.0 / OpenID Connect. Without an issuer URL it uses HTTP Basic; with one it discovers the provider through OpenID Connect and obtains a token with the password grant (RFC 6749, section 4.3), a deviation from RFC 9700 accepted for a test environment.

The tests are grouped by capability, one JUnit tag each: notes, sharing, notifications and live feed from 18, search from 38. A capability, once added, runs in every following stage. The export to Google Docs of 45 needs a real Google account and is tested inside its stage against a stub; the browser interface is checked by page tests inside the stages.

The suite does not know the framework, the databases, the message broker or the authorization server product, whether the system is a monolith or a set of services, how it is run or which stage it tests.

## Rules

- Test data is created and verified only through the API.
- Asynchronous behaviour — notifications and search — is verified by polling the API until a timeout.
- The suite does not start the system: the stage is started by its own command, from 27 by its pipeline, and its URLs are passed to the suite.

## Tools

Gradle (Kotlin DSL), JUnit, REST Assured and Awaitility — nothing from the frameworks of the application.

```
./gradlew test -DbaseUrl=https://… -DissuerUrl=https://… -Dusername=… -Dpassword=… -Dcapabilities=notes,sharing,…
```
