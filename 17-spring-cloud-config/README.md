# 17 · Spring Cloud Config

Level: senior. Status: not started.

## Change

Settings of all services come from a central configuration server backed by a Git repository.

## Why

In 16 every service carries its own settings; changing one means rebuilding or redeploying the service.

## Tools

- Spring Cloud Config Server and Client, refresh without restart.

## Proof

- The acceptance tests pass.
- A changed setting reaches a running service without a restart.
