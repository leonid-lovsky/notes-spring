# 04 · Microservices

Level: senior. Status: not started.

## Change

Every module becomes a service. The Thymeleaf UI becomes a `web` service behind the gateway. The gateway takes over login from the application, keeps the session in Redis and relays the token issued by `auth-server` to the services.

## Why

Modules need independent releases, calls between them now go over the network, and several gateway instances must share the user session.

## Tools

- Infrastructure: Spring Cloud Gateway, Spring Cloud Config, Eureka, secrets in AWS Secrets Manager through Spring Cloud AWS.
- Communication: HTTP service clients, Resilience4j through Spring Cloud Circuit Breaker, a choreographed saga over Kafka events.
- State: Redis for sessions (Spring Session) and caching (Spring Cache).
- Tests: Spring Cloud Contract for every client–provider pair.
- Run and delivery: all services run locally and on Amazon EC2 with Docker Compose; the GitHub Actions and Jenkins pipelines grow to many services.

## Proof

- The acceptance tests pass.
- A failed service does not take the system down.
- A failure in the middle of the saga is compensated.
- Contract tests protect every client–provider pair.
