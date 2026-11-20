# 49 · Progressive Web App

Level: senior. Status: not started.

## Change

The single-page application becomes a Progressive Web App: it can be installed and shows the notes read before when the network is gone.

## Why

In 48 the application is a browser tab and shows nothing without a network.

## Tools

- A web app manifest and a service worker caching the application and the last read notes.

## Proof

- The acceptance tests pass.
- The installed application opens and shows cached notes offline.
