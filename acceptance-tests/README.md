# Acceptance tests

One black-box test suite for every stage from 16, which introduces it after the REST API of 14 and its error format of 15. From then on the API grows only by addition, and only the authentication method changes once, in 40; this suite proves that behaviour defined in an earlier stage does not change, by talking to a running system over HTTP only, without knowing how the system is built.

## Boundary

The suite knows the base URL of the system, the credentials of a test user, the capabilities to run and, when the system accepts tokens, the issuer URL of the authorization server — all passed as parameters — and the public API of the application: HTTP, JSON, Server-Sent Events and OAuth 2.0 / OpenID Connect. Without an issuer URL it uses HTTP Basic; with one it obtains a token through standard OIDC discovery; the password grant (RFC 6749, section 4.3) is used for the test user, a named deviation from RFC 9700 accepted for a test environment.

The tests are grouped by capability, one JUnit tag each: notes, sharing, notifications and live feed from 16, search from 38. A stage passes the capabilities its API contains; a capability, once added, is run by every following stage. The export to Google Docs of 45 is not covered, because it needs a real Google account; the stage tests it against a stub.

The suite covers the API only; the browser interface is checked by page tests inside the stages. The suite does not know the framework of the application, its databases, message broker or authorization server product, whether the system is a monolith or a set of services, how it is run, or which stage it is testing. There is no stage-specific code in it.

## Rules

- Test data is created and verified only through the public API; the suite never reads or writes a database directly.
- Asynchronous behaviour — notifications and search — is verified by polling the API until the expected result appears or a timeout expires (Awaitility); the suite never subscribes to the broker or queries the search engine.
- Starting the system is not the suite's job: a stage's pipeline starts the stage (Docker Compose or Kubernetes, later also on Amazon Web Services) and passes its URLs to the suite.

## Tools

Gradle (Kotlin DSL), JUnit 5, REST Assured, Awaitility — nothing from the frameworks the application is built with.

```
./gradlew test -DbaseUrl=https://… -DissuerUrl=https://… -Dusername=… -Dpassword=… -Dcapabilities=notes,sharing,…
```

Status: not started.
