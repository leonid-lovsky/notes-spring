# 06 · Thymeleaf

Level: junior. Status: not started.

## Change

The application gets a browser interface next to the REST API: server-rendered pages for every function of the API, form login with a session, and the live notification feed in the browser. The REST API does not change.

## Why

In 05 the application can be used only through the API and Swagger UI; a person cannot sign in and work with notes in a browser.

## Tools

- Thymeleaf with Spring MVC, server-side rendering without a frontend build.
- Spring Security form login with a session and CSRF protection for the pages; HTTP Basic stays for the API.
- The live feed through the browser `EventSource` API on the existing Server-Sent Events endpoint.
- Tests: page tests with MockMvc and HtmlUnit.

## Proof

- The acceptance tests pass.
- Every function of the API is reachable from the browser after signing in.
- The page tests pass.
