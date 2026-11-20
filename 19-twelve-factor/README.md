# 19 · Twelve-Factor

Level: junior. Status: not started.

## Change

The stage follows the Twelve-Factor App: configuration and credentials come only from the environment, backing services are attached by URL, processes are stateless and shut down gracefully, and the same image runs in every environment.

## Why

In 18 credentials are written in the Compose file of the public repository, and the image carries settings of one environment.

## Tools

- Spring Boot externalized configuration from environment variables, an `.env` file outside the repository, graceful shutdown, the image of 17 unchanged between environments.

## Proof

- The acceptance tests pass.
- No credential is stored in the repository, and the same image runs with two different configurations.
