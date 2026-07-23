# tech-glossary.md

> Вынесено из CLAUDE.md 2026-07-23 (снятие объёма для AI-readability — глоссарий технологий за пределами Spring-стека, большинство ещё не используется в проекте, не нужен на каждую сессию). Технологии за пределами Spring-стека (уже используемые или отложенные) — от инструментов разработки к production-инфраструктуре и архитектурным паттернам.

- **Gradle** — система сборки для JVM: граф задач с инкрементальным up-to-date tracking и build cache. Уже используется (см. CLAUDE.md → «Стек»): Kotlin DSL, convention-плагины в `build-logic/convention/`
- **testcontainers** — интеграционные тесты через реальные зависимости (БД, брокеры) в Docker-контейнерах вместо моков. Convention-плагины построены (`spring-boot-testcontainers`+`-testcontainers-mongodb`), но пока не применяются ни в одном модуле — см. `decisions-log.md` → «Технологическая и вендорная ось адаптеров»
- **Docker Compose** — один YAML описывает и запускает несколько контейнеров как одно окружение; локальный оркестратор, однохостовый предшественник Kubernetes. Convention-плагин `spring-boot-docker-compose` построен, не применяется — тот же статус, что и testcontainers
- **Kubernetes** — production-оркестратор контейнеров: реплики, self-healing, rolling update, сервис-дискавери (Pod/Deployment/Service/ConfigMap)
- **Terraform** — infrastructure as code (HCL): декларативное состояние инфраструктуры, `plan`/`apply` через state-файл. В проекте — provisioning + branch protection через GitHub-провайдер
- **Jenkins** — CI/CD-сервер: пайплайн build → test → deploy из `Jenkinsfile`
- **SonarQube** — статический анализ (баги, уязвимости, code smells, покрытие через JaCoCo) с quality gate поверх PR; дополняет Checkstyle/NullAway/JaCoCo
- **Amazon Web Services** — целевая cloud-платформа (EC2, S3, RDS, EKS, Lambda), обычно через Terraform
- **Redis** — in-memory key-value: кэш, сессии, rate-limiting, pub/sub; кандидат под Spring Cache (см. CLAUDE.md → «Задачи», ОТЛОЖЕНО)
- **Kafka / RabbitMQ** — message broker: Kafka — топики/consumer groups/event-streaming; RabbitMQ — классическая очередь (см. CLAUDE.md → «Открытые решения» → «Регистрация auth/ ↔ user/»)
- **Elastic Stack** — Elasticsearch (поиск/хранение) + Logstash/Beats (логи) + Kibana (визуализация)
- **OAuth2** — RFC 6749, делегирует выдачу access-токена внешнему Authorization Server. В Spring Boot 4 — три отдельных стартера (`security-oauth2-resource-server`/`-client`/`-authorization-server`); convention-плагины готовы, не применены. **Кто держит роль Authorization Server** — открытый вопрос: (1) self-hosted (`spring-boot-starter-security-oauth2-authorization-server`, Spring Security 7) — полный контроль, своя эксплуатация; (2) managed IdP — Okta, Auth0, AWS Cognito, Azure AD/Entra ID, Keycloak (self-hosted компромисс). Пересекается с «Регистрация auth/ ↔ user/» — определяет, останется ли `auth/` тонким прокси или полноценным Authorization Server
- **jMolecules** — DDD/hexagonal-аннотации (`@Entity`, `@ValueObject`, `@AggregateRoot`); ценность — в связке с ArchUnit-правилами, проверяющими соответствие кода архитектуре
- **Axon Framework** — CQRS + Event Sourcing: команды → события (append-only Event Store), read-модели строятся отдельно. Следующий уровень после текущей hexagonal-архитектуры

Spring Cache, Spring OpenFeign, Spring Cloud LoadBalancer, Spring Cloud Circuit Breaker — часть Spring-стека, справки не здесь; статус — см. CLAUDE.md → «Задачи».
