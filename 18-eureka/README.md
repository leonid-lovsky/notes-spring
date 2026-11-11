# 18 · Eureka

Level: senior. Status: not started.

## Change

Services find each other through a registry instead of fixed addresses.

## Why

In 17 every service knows the addresses of the others; adding an instance means changing the configuration of its callers.

## Tools

- Eureka server and clients, Spring Cloud LoadBalancer in the HTTP service clients and the gateway.

## Proof

- The acceptance tests pass.
- A second instance of a service is used without changing any other service.
