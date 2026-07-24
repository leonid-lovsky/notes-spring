#!/bin/bash
set -euo pipefail

# Only relevant on Claude Code on the web / remote environments: the base
# image ships JDK 21, but the project pins Java 25 (.java-version) and
# Gradle's toolchain auto-provisioning (foojay, api.foojay.io) is blocked by
# this environment's network policy. archive.ubuntu.com is reachable, and
# Ubuntu noble-updates already carries a openjdk-25-jdk-headless package, so
# installing it directly makes Gradle's own toolchain detection pick it up
# from /usr/lib/jvm with no further configuration.
if [ "${CLAUDE_CODE_REMOTE:-}" != "true" ]; then
  exit 0
fi

if [ ! -d /usr/lib/jvm/java-25-openjdk-amd64 ]; then
  apt-get update -qq
  apt-get install -y -qq openjdk-25-jdk-headless
fi
