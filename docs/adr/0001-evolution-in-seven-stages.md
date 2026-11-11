# 0001. Evolution in seven stages

Status: accepted (2026-11-11)

## Context

The goal is to learn and to demonstrate Spring technologies on one application, from a classic monolith to a distributed, configurable, resilient system. The previous attempt built every variant at once (web stack × data access × database vendor as Gradle modules) and never reached a running application.

## Decision

Seven independent projects in one repository, built strictly one after another: monolith, modulith, event-driven, microservices, kubernetes, hexagonal, reactive. Each project starts as a copy of the previous one and adds exactly one architectural change, motivated by a problem of the previous stage. Functionality and HTTP API are fixed in the first project. Standard Spring Boot starters with auto-configuration are preferred over custom configuration.

## Consequences

+ every stage is a finished, deployable application and a portfolio item
+ the difference between two neighbouring directories shows one architectural step
− code is duplicated between stages on purpose: each directory is a snapshot
− frozen stages still need dependency updates
