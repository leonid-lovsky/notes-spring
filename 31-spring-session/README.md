# 31 · Spring Session

Level: middle. Status: not started.

## Change

The application runs in two instances behind NGINX, and the browser session is kept in Redis instead of the memory of one instance.

## Why

In 30 the session lives in one instance: a second instance does not know a signed-in user, and a restart signs everyone out.

## Tools

- Spring Session with Redis through Spring Boot auto-configuration; Redis of 30; NGINX of 25 spreads the requests.

## Proof

- The acceptance tests pass.
- A user stays signed in when requests alternate between the instances and after a restart of one of them.
