# 25 · Twelve-Factor App

Level: junior. Status: not started.

## Change

The stage follows the Twelve-Factor App: configuration and credentials come only from the environment, backing services are attached by URL, processes are stateless and shut down gracefully, and the same image runs in every environment.

## Why

In 24 credentials are written in the configuration files of the public repository, and the image carries settings of one environment.

## Tools

- Spring Boot externalized configuration from environment variables, an `.env` file outside the repository, graceful shutdown, the image of 21 unchanged between environments.
- The URL and the credentials of the database, written in `application.properties` since 01, leave the repository; this closes the compromise recorded in ADR 0001.

## Proof

- The acceptance tests pass.
- No credential is stored in the repository, and the same image runs with two different configurations.
