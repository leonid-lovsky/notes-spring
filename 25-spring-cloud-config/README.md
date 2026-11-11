# 25 · Spring Cloud Config

Level: senior. Status: not started.

## Change

Settings of all services come from a central configuration server backed by a Git repository; passwords and keys are stored there encrypted and decrypted by the server.

## Why

In 24 every service carries its own settings; changing one means rebuilding or redeploying the service.

## Tools

- Spring Cloud Config Server and Client, refresh without restart, encrypted values (`{cipher}`) with a server-side key.

## Proof

- The acceptance tests pass.
- A changed setting reaches a running service without a restart.
- No secret is stored in plain text in the repository.
