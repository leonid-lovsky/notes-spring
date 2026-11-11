# 06 · Hexagonal

Level: senior. Status: not started.

## Change

A storage port is introduced in one or two services — only where a second implementation appears — with two adapters: Spring Data JDBC (PostgreSQL, MySQL, H2 selected by profile) and Spring Data MongoDB.

## Why

Delivery to infrastructure owned by someone else requires a different database.

## Tools

Spring Data JDBC, Spring Data MongoDB, one contract test of the port run against every adapter, a CI matrix over databases.

## Proof

The same contract test passes on every adapter and database.
