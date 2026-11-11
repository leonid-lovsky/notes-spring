# 07 · GitLab CI

Level: middle. Status: not started.

## Change

GitLab CI joins GitHub Actions and Jenkins as a third equal pipeline with the same steps. The application code does not change.

## Why

In 06 two CI systems are covered; GitLab CI is the third one commonly met in practice, and adding it shows that the build logic in Gradle makes a pipeline cheap to add.

## Tools

- `.gitlab-ci.yml` running the same Gradle tasks; the repository is mirrored to GitLab.

## Proof

- The acceptance tests pass.
- Three pipelines build, test and run the same application with the same steps.
