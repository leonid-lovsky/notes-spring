# Spring Initializr — полная матрица всех совместимых зависимостей (Boot 4.0.7/4.1.0 × Java 21/25)

Получено напрямую с `start.spring.io` 2026-08-02: `metadata/client` дал список всех 205 id зависимостей Initializr вместе с `versionRange` на каждую (совместимость с конкретной версией Boot); список отфильтрован по `versionRange` под каждую целевую версию Boot (197 id совместимо с 4.0.7, 176 с 4.1.0 — часть технологий выведена между версиями), затем весь отфильтрованный список одним запросом отправлен на `/build.gradle` (`type=gradle-project`, `language=java`, `dependencies=<все id через запятую>`) — сервер сам собрал реальный `build.gradle` с плагинами/BOM/версиями для каждой комбинации. Инструмент разного назначения с `docs/spring-boot-starters-reference.md` (тот — куратированный список ровно тех технологий, что actuallly отслеживаются convention-плагинами проекта, эталон полноты для них); этот файл — полный сырой снимок ВСЕГО, что вообще предлагает Initializr на выбранную версию Boot, включая технологии, которых в проекте нет и не планируется (Vaadin/Kafka Streams/Cassandra/gRPC/Spring AI и т. д.) — справочник для общего кругозора, не источник для convention-плагинов.

**Практическая находка при генерации**: параметр `bootVersion` в query-строке `/build.gradle` должен передаваться БЕЗ суффикса `.RELEASE` (`bootVersion=4.1.0`, не `bootVersion=4.1.0.RELEASE`) — с суффиксом сервер Initializr падал с `500 Internal Server Error` / `Bom '...' could not be resolved` на любом запросе, включая однословный (`dependencies=web`), тогда как без суффикса тот же самый номер версии резолвился нормально. Не баг проекта — особенность конкретно `/build.gradle`-эндпоинта Initializr.

**Различия внутри пар (Java 21 vs Java 25 при одном Boot)** — только `languageVersion = JavaLanguageVersion.of(...)`, дословно совпадает во всём остальном.

**Различия между Boot 4.0.7 и 4.1.0** — состав стартеров реально разный, не только версии: 4.0.7 несёт ряд технологий, выведенных из Initializr к 4.1.0 (Azure/`spring-cloud-azure-starter-*`, GCP/`spring-cloud-gcp-starter-*`, Timefold Solver, Solace, Camel, gRPC (`io.grpc:grpc-services`+`spring-grpc-*`), MyBatis, Spring Shell, springdoc-openapi, jte, htmx, Tanzu Spring SDK, Sentry, datasource-micrometer, session-hazelcast/session-mongodb — большинство отмечены в metadata `versionRange` верхней границей `[...,4.1.0.M1)`), а 4.1.0 взамен добавляет то, чего не было в 4.0.7 (`spring-boot-starter-batch-data-mongodb`, `spring-boot-starter-grpc-client`/`-grpc-server` — другая реализация gRPC, чем в 4.0.7).

**Известная особенность сырого вывода Initializr, не ошибка копирования** (совпадает с уже задокументированной в `docs/spring-boot-starters-reference.md`, найденной 2026-07-24): во всех 4 сгенерированных файлах — 2 одинаковые дублирующиеся строки, `implementation 'org.springframework.security:spring-security-messaging'` дважды и `testImplementation 'org.springframework.boot:spring-boot-starter-ldap'` вместе с корректным `-ldap-test`-компаньоном (первая, без `-test`, лишняя — Initializr сам генерирует такое, не человеческий copy-paste).

## Boot 4.1.0 / Java 25

```gradle
plugins {
	id 'java'
	id 'org.springframework.boot' version '4.1.0'
	id 'io.spring.dependency-management' version '1.1.7'
	id 'com.netflix.dgs.codegen' version '8.3.0'
	id 'org.hibernate.orm' version '7.4.1.Final'
	id 'org.graalvm.buildtools.native' version '1.1.1'
	id 'org.cyclonedx.bom' version '3.3.0'
	id 'org.springframework.cloud.contract' version '5.0.3'
	id 'com.google.protobuf' version '0.9.6'
	id 'org.asciidoctor.jvm.convert' version '4.0.5'
	id 'com.vaadin' version '25.2.4'
}

group = 'com.example'
version = '0.0.1-SNAPSHOT'

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
	maven { url = 'https://build.shibboleth.net/maven/releases' }
}

ext {
	set('snippetsDir', file("build/generated-snippets"))
	set('springAiVersion', "2.0.0")
	set('springBootAdminVersion', "4.1.2")
	set('springCloudServicesVersion', "4.4.1")
	set('springCloudVersion', "2025.1.2")
	set('springModulithVersion', "2.1.0")
	set('vaadinVersion', "25.2.4")
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-h2console'
	implementation 'org.springframework.boot:spring-boot-starter-activemq'
	implementation 'org.springframework.boot:spring-boot-starter-actuator'
	implementation 'org.springframework.boot:spring-boot-starter-amqp'
	implementation 'org.springframework.boot:spring-boot-starter-artemis'
	implementation 'org.springframework.boot:spring-boot-starter-batch'
	implementation 'org.springframework.boot:spring-boot-starter-batch-data-mongodb'
	implementation 'org.springframework.boot:spring-boot-starter-batch-jdbc'
	implementation 'org.springframework.boot:spring-boot-starter-cache'
	implementation 'org.springframework.boot:spring-boot-starter-cassandra'
	implementation 'org.springframework.boot:spring-boot-starter-cloudfoundry'
	implementation 'org.springframework.boot:spring-boot-starter-couchbase'
	implementation 'org.springframework.boot:spring-boot-starter-data-cassandra'
	implementation 'org.springframework.boot:spring-boot-starter-data-cassandra-reactive'
	implementation 'org.springframework.boot:spring-boot-starter-data-couchbase'
	implementation 'org.springframework.boot:spring-boot-starter-data-couchbase-reactive'
	implementation 'org.springframework.boot:spring-boot-starter-data-elasticsearch'
	implementation 'org.springframework.boot:spring-boot-starter-data-jdbc'
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-data-ldap'
	implementation 'org.springframework.boot:spring-boot-starter-data-mongodb'
	implementation 'org.springframework.boot:spring-boot-starter-data-mongodb-reactive'
	implementation 'org.springframework.boot:spring-boot-starter-data-neo4j'
	implementation 'org.springframework.boot:spring-boot-starter-data-r2dbc'
	implementation 'org.springframework.boot:spring-boot-starter-data-redis'
	implementation 'org.springframework.boot:spring-boot-starter-data-redis-reactive'
	implementation 'org.springframework.boot:spring-boot-starter-data-rest'
	implementation 'org.springframework.boot:spring-boot-starter-elasticsearch'
	implementation 'org.springframework.boot:spring-boot-starter-flyway'
	implementation 'org.springframework.boot:spring-boot-starter-freemarker'
	implementation 'org.springframework.boot:spring-boot-starter-graphql'
	implementation 'org.springframework.boot:spring-boot-starter-groovy-templates'
	implementation 'org.springframework.boot:spring-boot-starter-grpc-client'
	implementation 'org.springframework.boot:spring-boot-starter-grpc-server'
	implementation 'org.springframework.boot:spring-boot-starter-hateoas'
	implementation 'org.springframework.boot:spring-boot-starter-hazelcast'
	implementation 'org.springframework.boot:spring-boot-starter-integration'
	implementation 'org.springframework.boot:spring-boot-starter-jdbc'
	implementation 'org.springframework.boot:spring-boot-starter-jersey'
	implementation 'org.springframework.boot:spring-boot-starter-jooq'
	implementation 'org.springframework.boot:spring-boot-starter-kafka'
	implementation 'org.springframework.boot:spring-boot-starter-ldap'
	implementation 'org.springframework.boot:spring-boot-starter-liquibase'
	implementation 'org.springframework.boot:spring-boot-starter-mail'
	implementation 'org.springframework.boot:spring-boot-starter-mongodb'
	implementation 'org.springframework.boot:spring-boot-starter-mustache'
	implementation 'org.springframework.boot:spring-boot-starter-neo4j'
	implementation 'org.springframework.boot:spring-boot-starter-opentelemetry'
	implementation 'org.springframework.boot:spring-boot-starter-pulsar'
	implementation 'org.springframework.boot:spring-boot-starter-quartz'
	implementation 'org.springframework.boot:spring-boot-starter-r2dbc'
	implementation 'org.springframework.boot:spring-boot-starter-restclient'
	implementation 'org.springframework.boot:spring-boot-starter-rsocket'
	implementation 'org.springframework.boot:spring-boot-starter-security'
	implementation 'org.springframework.boot:spring-boot-starter-security-oauth2-authorization-server'
	implementation 'org.springframework.boot:spring-boot-starter-security-oauth2-client'
	implementation 'org.springframework.boot:spring-boot-starter-security-oauth2-resource-server'
	implementation 'org.springframework.boot:spring-boot-starter-security-saml2'
	implementation 'org.springframework.boot:spring-boot-starter-session-data-redis'
	implementation 'org.springframework.boot:spring-boot-starter-session-jdbc'
	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
	implementation 'org.springframework.boot:spring-boot-starter-validation'
	implementation 'org.springframework.boot:spring-boot-starter-webclient'
	implementation 'org.springframework.boot:spring-boot-starter-webflux'
	implementation 'org.springframework.boot:spring-boot-starter-webmvc'
	implementation 'org.springframework.boot:spring-boot-starter-webservices'
	implementation 'org.springframework.boot:spring-boot-starter-websocket'
	implementation 'org.springframework.boot:spring-boot-starter-zipkin'
	implementation 'com.okta.spring:okta-spring-boot-starter:3.1.0'
	developmentOnly 'com.vaadin:vaadin-dev'
	implementation 'com.vaadin:vaadin-spring-boot-starter'
	implementation 'de.codecentric:spring-boot-admin-starter-client'
	implementation 'de.codecentric:spring-boot-admin-starter-server'
	implementation 'io.pivotal.spring.cloud:spring-cloud-services-starter-config-client'
	implementation 'io.pivotal.spring.cloud:spring-cloud-services-starter-service-registry'
	implementation 'org.apache.kafka:kafka-streams'
	implementation 'org.flywaydb:flyway-database-db2'
	implementation 'org.flywaydb:flyway-database-derby'
	implementation 'org.flywaydb:flyway-database-hsqldb'
	implementation 'org.flywaydb:flyway-database-oracle'
	implementation 'org.flywaydb:flyway-database-postgresql'
	implementation 'org.flywaydb:flyway-mysql'
	implementation 'org.flywaydb:flyway-sqlserver'
	implementation 'org.jobrunr:jobrunr-spring-boot-4-starter:8.7.0'
	implementation 'org.springaicommunity:mcp-authorization-server-spring-boot:0.1.13'
	implementation 'org.springaicommunity:mcp-client-security-spring-boot:0.1.13'
	implementation 'org.springaicommunity:mcp-server-security-spring-boot:0.1.13'
	implementation 'org.springframework.ai:spring-ai-jsoup-document-reader'
	implementation 'org.springframework.ai:spring-ai-markdown-document-reader'
	implementation 'org.springframework.ai:spring-ai-pdf-document-reader'
	implementation 'org.springframework.ai:spring-ai-starter-mcp-client-webflux'
	implementation 'org.springframework.ai:spring-ai-starter-mcp-server-webflux'
	implementation 'org.springframework.ai:spring-ai-starter-model-anthropic'
	implementation 'org.springframework.ai:spring-ai-starter-model-bedrock'
	implementation 'org.springframework.ai:spring-ai-starter-model-bedrock-converse'
	implementation 'org.springframework.ai:spring-ai-starter-model-chat-memory'
	implementation 'org.springframework.ai:spring-ai-starter-model-chat-memory-repository-cassandra'
	implementation 'org.springframework.ai:spring-ai-starter-model-chat-memory-repository-jdbc'
	implementation 'org.springframework.ai:spring-ai-starter-model-chat-memory-repository-mongodb'
	implementation 'org.springframework.ai:spring-ai-starter-model-chat-memory-repository-neo4j'
	implementation 'org.springframework.ai:spring-ai-starter-model-chat-memory-repository-redis'
	implementation 'org.springframework.ai:spring-ai-starter-model-deepseek'
	implementation 'org.springframework.ai:spring-ai-starter-model-elevenlabs'
	implementation 'org.springframework.ai:spring-ai-starter-model-google-genai'
	implementation 'org.springframework.ai:spring-ai-starter-model-google-genai-embedding'
	implementation 'org.springframework.ai:spring-ai-starter-model-mistral-ai'
	implementation 'org.springframework.ai:spring-ai-starter-model-ollama'
	implementation 'org.springframework.ai:spring-ai-starter-model-openai'
	implementation 'org.springframework.ai:spring-ai-starter-model-postgresml-embedding'
	implementation 'org.springframework.ai:spring-ai-starter-model-stability-ai'
	implementation 'org.springframework.ai:spring-ai-starter-model-transformers'
	implementation 'org.springframework.ai:spring-ai-starter-model-vertex-ai-embedding'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-aws-opensearch'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-azure'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-bedrock-knowledgebase'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-cassandra'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-chroma'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-couchbase'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-elasticsearch'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-gemfire'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-mariadb'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-milvus'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-mongodb-atlas'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-neo4j'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-opensearch'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-oracle'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-pgvector'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-pinecone'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-qdrant'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-redis'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-s3'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-typesense'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-weaviate'
	implementation 'org.springframework.ai:spring-ai-tika-document-reader'
	implementation 'org.springframework.ai:spring-ai-vector-store-advisor'
	implementation 'org.springframework.amqp:spring-rabbit-stream'
	implementation 'org.springframework.cloud:spring-cloud-bus'
	implementation 'org.springframework.cloud:spring-cloud-config-server'
	implementation 'org.springframework.cloud:spring-cloud-function-web'
	implementation 'org.springframework.cloud:spring-cloud-starter'
	implementation 'org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j'
	implementation 'org.springframework.cloud:spring-cloud-starter-config'
	implementation 'org.springframework.cloud:spring-cloud-starter-consul-config'
	implementation 'org.springframework.cloud:spring-cloud-starter-consul-discovery'
	implementation 'org.springframework.cloud:spring-cloud-starter-gateway-server-webflux'
	implementation 'org.springframework.cloud:spring-cloud-starter-gateway-server-webmvc'
	implementation 'org.springframework.cloud:spring-cloud-starter-loadbalancer'
	implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
	implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-server'
	implementation 'org.springframework.cloud:spring-cloud-starter-openfeign'
	implementation 'org.springframework.cloud:spring-cloud-starter-task'
	implementation 'org.springframework.cloud:spring-cloud-starter-vault-config'
	implementation 'org.springframework.cloud:spring-cloud-starter-zookeeper-config'
	implementation 'org.springframework.cloud:spring-cloud-starter-zookeeper-discovery'
	implementation 'org.springframework.cloud:spring-cloud-stream'
	implementation 'org.springframework.cloud:spring-cloud-stream-binder-kafka'
	implementation 'org.springframework.cloud:spring-cloud-stream-binder-kafka-streams'
	implementation 'org.springframework.cloud:spring-cloud-stream-binder-pulsar'
	implementation 'org.springframework.cloud:spring-cloud-stream-binder-rabbit'
	implementation 'org.springframework.data:spring-data-rest-hal-explorer'
	implementation 'org.springframework.integration:spring-integration-amqp'
	implementation 'org.springframework.integration:spring-integration-grpc'
	implementation 'org.springframework.integration:spring-integration-http'
	implementation 'org.springframework.integration:spring-integration-jdbc'
	implementation 'org.springframework.integration:spring-integration-jms'
	implementation 'org.springframework.integration:spring-integration-jpa'
	implementation 'org.springframework.integration:spring-integration-kafka'
	implementation 'org.springframework.integration:spring-integration-mail'
	implementation 'org.springframework.integration:spring-integration-mongodb'
	implementation 'org.springframework.integration:spring-integration-r2dbc'
	implementation 'org.springframework.integration:spring-integration-redis'
	implementation 'org.springframework.integration:spring-integration-rsocket'
	implementation 'org.springframework.integration:spring-integration-stomp'
	implementation 'org.springframework.integration:spring-integration-webflux'
	implementation 'org.springframework.integration:spring-integration-websocket'
	implementation 'org.springframework.integration:spring-integration-ws'
	implementation 'org.springframework.modulith:spring-modulith-events-api'
	implementation 'org.springframework.modulith:spring-modulith-starter-core'
	implementation 'org.springframework.modulith:spring-modulith-starter-insight'
	implementation 'org.springframework.modulith:spring-modulith-starter-jdbc'
	implementation 'org.springframework.modulith:spring-modulith-starter-jpa'
	implementation 'org.springframework.modulith:spring-modulith-starter-mongodb'
	implementation 'org.springframework.modulith:spring-modulith-starter-neo4j'
	implementation 'org.springframework.security:spring-security-messaging'
	implementation 'org.springframework.security:spring-security-messaging'
	implementation 'org.springframework.security:spring-security-rsocket'
	implementation 'org.springframework.security:spring-security-webauthn'
	implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity6'
	compileOnly 'org.projectlombok:lombok'
	developmentOnly 'org.springframework.boot:spring-boot-devtools'
	developmentOnly 'org.springframework.boot:spring-boot-docker-compose'
	runtimeOnly 'com.h2database:h2'
	runtimeOnly 'com.ibm.db2:jcc'
	runtimeOnly 'com.microsoft.sqlserver:mssql-jdbc'
	runtimeOnly 'com.mysql:mysql-connector-j'
	runtimeOnly 'com.oracle.database.jdbc:ojdbc11'
	runtimeOnly 'com.oracle.database.r2dbc:oracle-r2dbc'
	runtimeOnly 'io.asyncer:r2dbc-mysql'
	runtimeOnly 'io.micrometer:micrometer-registry-datadog'
	runtimeOnly 'io.micrometer:micrometer-registry-dynatrace'
	runtimeOnly 'io.micrometer:micrometer-registry-graphite'
	runtimeOnly 'io.micrometer:micrometer-registry-influx'
	runtimeOnly 'io.micrometer:micrometer-registry-new-relic'
	runtimeOnly 'io.micrometer:micrometer-registry-otlp'
	runtimeOnly 'io.micrometer:micrometer-registry-prometheus'
	runtimeOnly 'io.r2dbc:r2dbc-h2'
	runtimeOnly 'io.r2dbc:r2dbc-mssql:1.0.0.RELEASE'
	runtimeOnly 'org.apache.derby:derby'
	runtimeOnly 'org.apache.derby:derbytools'
	runtimeOnly 'org.hsqldb:hsqldb'
	runtimeOnly 'org.mariadb:r2dbc-mariadb:1.1.3'
	runtimeOnly 'org.mariadb.jdbc:mariadb-java-client'
	runtimeOnly 'org.postgresql:postgresql'
	runtimeOnly 'org.postgresql:r2dbc-postgresql'
	developmentOnly 'org.springframework.ai:spring-ai-spring-boot-docker-compose'
	runtimeOnly 'org.springframework.modulith:spring-modulith-events-jms'
	runtimeOnly 'org.springframework.modulith:spring-modulith-runtime'
	runtimeOnly 'org.xerial:sqlite-jdbc'
	annotationProcessor 'org.projectlombok:lombok'
	annotationProcessor 'org.springframework.boot:spring-boot-configuration-processor'
	testImplementation 'org.springframework.boot:spring-boot-starter-activemq-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-actuator-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-amqp-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-artemis-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-batch-data-mongodb-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-batch-jdbc-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-batch-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-cache-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-cassandra-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-cloudfoundry-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-couchbase-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-cassandra-reactive-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-cassandra-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-couchbase-reactive-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-couchbase-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-elasticsearch-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-jdbc-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-jpa-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-ldap-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-mongodb-reactive-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-mongodb-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-neo4j-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-r2dbc-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-redis-reactive-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-redis-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-rest-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-elasticsearch-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-flyway-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-freemarker-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-graphql-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-groovy-templates-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-grpc-client-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-grpc-server-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-hateoas-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-hazelcast-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-jdbc-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-jersey-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-jooq-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-kafka-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-ldap'
	testImplementation 'org.springframework.boot:spring-boot-starter-ldap-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-liquibase-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-mail-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-mongodb-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-mustache-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-neo4j-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-opentelemetry-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-pulsar-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-quartz-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-r2dbc-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-restclient-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-restdocs'
	testImplementation 'org.springframework.boot:spring-boot-starter-rsocket-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-security-oauth2-authorization-server-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-security-oauth2-client-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-security-oauth2-resource-server-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-security-saml2-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-security-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-session-data-redis-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-session-jdbc-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-thymeleaf-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-validation-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-webclient-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-webflux-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-webmvc-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-webservices-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-websocket-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-zipkin-test'
	testImplementation 'org.springframework.boot:spring-boot-testcontainers'
	testImplementation 'com.unboundid:unboundid-ldapsdk'
	testImplementation 'io.projectreactor:reactor-test'
	testImplementation 'io.rest-assured:spring-web-test-client'
	testImplementation 'org.springframework.ai:spring-ai-spring-boot-testcontainers'
	testImplementation 'org.springframework.cloud:spring-cloud-starter-contract-stub-runner'
	testImplementation 'org.springframework.cloud:spring-cloud-starter-contract-verifier'
	testImplementation 'org.springframework.cloud:spring-cloud-stream-test-binder'
	testImplementation 'org.springframework.integration:spring-integration-test'
	testImplementation 'org.springframework.modulith:spring-modulith-starter-test'
	testImplementation 'org.springframework.restdocs:spring-restdocs-mockmvc'
	testImplementation 'org.testcontainers:testcontainers-activemq'
	testImplementation 'org.testcontainers:testcontainers-cassandra'
	testImplementation 'org.testcontainers:testcontainers-chromadb'
	testImplementation 'org.testcontainers:testcontainers-consul'
	testImplementation 'org.testcontainers:testcontainers-couchbase'
	testImplementation 'org.testcontainers:testcontainers-db2'
	testImplementation 'org.testcontainers:testcontainers-elasticsearch'
	testImplementation 'org.testcontainers:testcontainers-grafana'
	testImplementation 'org.testcontainers:testcontainers-junit-jupiter'
	testImplementation 'org.testcontainers:testcontainers-kafka'
	testImplementation 'org.testcontainers:testcontainers-mariadb'
	testImplementation 'org.testcontainers:testcontainers-milvus'
	testImplementation 'org.testcontainers:testcontainers-mongodb'
	testImplementation 'org.testcontainers:testcontainers-mssqlserver'
	testImplementation 'org.testcontainers:testcontainers-mysql'
	testImplementation 'org.testcontainers:testcontainers-neo4j'
	testImplementation 'org.testcontainers:testcontainers-ollama'
	testImplementation 'org.testcontainers:testcontainers-oracle-free'
	testImplementation 'org.testcontainers:testcontainers-postgresql'
	testImplementation 'org.testcontainers:testcontainers-pulsar'
	testImplementation 'org.testcontainers:testcontainers-qdrant'
	testImplementation 'org.testcontainers:testcontainers-r2dbc'
	testImplementation 'org.testcontainers:testcontainers-rabbitmq'
	testImplementation 'org.testcontainers:testcontainers-typesense'
	testImplementation 'org.testcontainers:testcontainers-vault'
	testImplementation 'org.testcontainers:testcontainers-weaviate'
	testCompileOnly 'org.projectlombok:lombok'
	testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
	testAnnotationProcessor 'org.projectlombok:lombok'
}

dependencyManagement {
	imports {
		mavenBom "org.springframework.modulith:spring-modulith-bom:${springModulithVersion}"
		mavenBom "com.vaadin:vaadin-bom:${vaadinVersion}"
		mavenBom "de.codecentric:spring-boot-admin-dependencies:${springBootAdminVersion}"
		mavenBom "io.pivotal.spring.cloud:spring-cloud-services-dependencies:${springCloudServicesVersion}"
		mavenBom "org.springframework.ai:spring-ai-bom:${springAiVersion}"
		mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
	}
}

generateJava {
	schemaPaths = ["${projectDir}/src/main/resources/graphql-client"]
	packageName = 'com.example.demo.codegen'
	generateClient = true
}

hibernate {
	enhancement {
	}
}

contracts {
	testMode = 'WebTestClient'
}

tasks.named('contractTest') {
	useJUnitPlatform()
}

tasks.named('test') {
	outputs.dir snippetsDir
	useJUnitPlatform()
}

tasks.named('asciidoctor') {
	inputs.dir snippetsDir
	dependsOn test
}
```

## Boot 4.1.0 / Java 21

Идентично блоку «Boot 4.1.0 / Java 25» выше во всём, кроме одной строки:

```gradle
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
```

## Boot 4.0.7 / Java 25

```gradle
plugins {
	id 'java'
	id 'org.springframework.boot' version '4.0.7'
	id 'io.spring.dependency-management' version '1.1.7'
	id 'com.netflix.dgs.codegen' version '8.3.0'
	id 'org.hibernate.orm' version '7.2.19.Final'
	id 'org.graalvm.buildtools.native' version '0.11.5'
	id 'gg.jte.gradle' version '3.2.4'
	id 'org.cyclonedx.bom' version '3.3.0'
	id 'org.springframework.cloud.contract' version '5.0.3'
	id 'com.google.protobuf' version '0.9.5'
	id 'org.asciidoctor.jvm.convert' version '4.0.5'
	id 'com.vaadin' version '25.1.10'
}

group = 'com.example'
version = '0.0.1-SNAPSHOT'

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
	maven { url = 'https://build.shibboleth.net/maven/releases' }
}

ext {
	set('snippetsDir', file("build/generated-snippets"))
	set('datasourceMicrometerVersion', "2.2.1")
	set('netflixDgsVersion', "11.1.0")
	set('sentryVersion', "8.27.0")
	set('solaceSpringCloudVersion', "6.0.0")
	set('springAiVersion', "2.0.0")
	set('springBootAdminVersion', "4.0.4")
	set('springCloudAzureVersion', "7.3.0")
	set('springCloudGcpVersion', "8.0.5")
	set('springCloudServicesVersion', "4.4.1")
	set('springCloudVersion', "2025.1.2")
	set('springGrpcVersion', "1.0.3")
	set('springModulithVersion', "2.0.7")
	set('springShellVersion', "4.0.2")
	set('tanzuSpringSdkVersion', "1.0.0")
	set('timefoldSolverVersion', "2.3.0")
	set('vaadinVersion', "25.1.10")
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-h2console'
	implementation 'org.springframework.boot:spring-boot-starter-activemq'
	implementation 'org.springframework.boot:spring-boot-starter-actuator'
	implementation 'org.springframework.boot:spring-boot-starter-amqp'
	implementation 'org.springframework.boot:spring-boot-starter-artemis'
	implementation 'org.springframework.boot:spring-boot-starter-batch'
	implementation 'org.springframework.boot:spring-boot-starter-batch-jdbc'
	implementation 'org.springframework.boot:spring-boot-starter-cache'
	implementation 'org.springframework.boot:spring-boot-starter-cassandra'
	implementation 'org.springframework.boot:spring-boot-starter-cloudfoundry'
	implementation 'org.springframework.boot:spring-boot-starter-couchbase'
	implementation 'org.springframework.boot:spring-boot-starter-data-cassandra'
	implementation 'org.springframework.boot:spring-boot-starter-data-cassandra-reactive'
	implementation 'org.springframework.boot:spring-boot-starter-data-couchbase'
	implementation 'org.springframework.boot:spring-boot-starter-data-couchbase-reactive'
	implementation 'org.springframework.boot:spring-boot-starter-data-elasticsearch'
	implementation 'org.springframework.boot:spring-boot-starter-data-jdbc'
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-data-ldap'
	implementation 'org.springframework.boot:spring-boot-starter-data-mongodb'
	implementation 'org.springframework.boot:spring-boot-starter-data-mongodb-reactive'
	implementation 'org.springframework.boot:spring-boot-starter-data-neo4j'
	implementation 'org.springframework.boot:spring-boot-starter-data-r2dbc'
	implementation 'org.springframework.boot:spring-boot-starter-data-redis'
	implementation 'org.springframework.boot:spring-boot-starter-data-redis-reactive'
	implementation 'org.springframework.boot:spring-boot-starter-data-rest'
	implementation 'org.springframework.boot:spring-boot-starter-elasticsearch'
	implementation 'org.springframework.boot:spring-boot-starter-flyway'
	implementation 'org.springframework.boot:spring-boot-starter-freemarker'
	implementation 'org.springframework.boot:spring-boot-starter-graphql'
	implementation 'org.springframework.boot:spring-boot-starter-groovy-templates'
	implementation 'org.springframework.boot:spring-boot-starter-hateoas'
	implementation 'org.springframework.boot:spring-boot-starter-hazelcast'
	implementation 'org.springframework.boot:spring-boot-starter-integration'
	implementation 'org.springframework.boot:spring-boot-starter-jdbc'
	implementation 'org.springframework.boot:spring-boot-starter-jersey'
	implementation 'org.springframework.boot:spring-boot-starter-jooq'
	implementation 'org.springframework.boot:spring-boot-starter-kafka'
	implementation 'org.springframework.boot:spring-boot-starter-ldap'
	implementation 'org.springframework.boot:spring-boot-starter-liquibase'
	implementation 'org.springframework.boot:spring-boot-starter-mail'
	implementation 'org.springframework.boot:spring-boot-starter-mongodb'
	implementation 'org.springframework.boot:spring-boot-starter-mustache'
	implementation 'org.springframework.boot:spring-boot-starter-neo4j'
	implementation 'org.springframework.boot:spring-boot-starter-opentelemetry'
	implementation 'org.springframework.boot:spring-boot-starter-pulsar'
	implementation 'org.springframework.boot:spring-boot-starter-quartz'
	implementation 'org.springframework.boot:spring-boot-starter-r2dbc'
	implementation 'org.springframework.boot:spring-boot-starter-restclient'
	implementation 'org.springframework.boot:spring-boot-starter-rsocket'
	implementation 'org.springframework.boot:spring-boot-starter-security'
	implementation 'org.springframework.boot:spring-boot-starter-security-oauth2-authorization-server'
	implementation 'org.springframework.boot:spring-boot-starter-security-oauth2-client'
	implementation 'org.springframework.boot:spring-boot-starter-security-oauth2-resource-server'
	implementation 'org.springframework.boot:spring-boot-starter-security-saml2'
	implementation 'org.springframework.boot:spring-boot-starter-session-data-redis'
	implementation 'org.springframework.boot:spring-boot-starter-session-jdbc'
	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
	implementation 'org.springframework.boot:spring-boot-starter-validation'
	implementation 'org.springframework.boot:spring-boot-starter-webclient'
	implementation 'org.springframework.boot:spring-boot-starter-webflux'
	implementation 'org.springframework.boot:spring-boot-starter-webmvc'
	implementation 'org.springframework.boot:spring-boot-starter-webservices'
	implementation 'org.springframework.boot:spring-boot-starter-websocket'
	implementation 'org.springframework.boot:spring-boot-starter-zipkin'
	implementation 'ai.timefold.solver:timefold-solver-spring-boot-starter'
	implementation 'com.azure.spring:spring-cloud-azure-starter'
	implementation 'com.azure.spring:spring-cloud-azure-starter-active-directory'
	implementation 'com.azure.spring:spring-cloud-azure-starter-actuator'
	implementation 'com.azure.spring:spring-cloud-azure-starter-data-cosmos'
	implementation 'com.azure.spring:spring-cloud-azure-starter-integration-storage-queue'
	implementation 'com.azure.spring:spring-cloud-azure-starter-jdbc-mysql'
	implementation 'com.azure.spring:spring-cloud-azure-starter-jdbc-postgresql'
	implementation 'com.azure.spring:spring-cloud-azure-starter-keyvault'
	implementation 'com.azure.spring:spring-cloud-azure-starter-storage'
	implementation 'com.google.cloud:spring-cloud-gcp-starter'
	implementation 'com.google.cloud:spring-cloud-gcp-starter-pubsub'
	implementation 'com.google.cloud:spring-cloud-gcp-starter-storage'
	implementation 'com.hazelcast.spring:hazelcast-spring-session:4.0.0'
	implementation 'com.netflix.graphql.dgs:graphql-dgs-spring-graphql-starter'
	implementation 'com.okta.spring:okta-spring-boot-starter:3.1.0'
	implementation 'com.solace.spring.cloud:spring-cloud-starter-stream-solace'
	developmentOnly 'com.vaadin:vaadin-dev'
	implementation 'com.vaadin:vaadin-spring-boot-starter'
	implementation 'com.vmware.tanzu.spring:tanzu-spring-starter'
	implementation 'de.codecentric:spring-boot-admin-starter-client'
	implementation 'de.codecentric:spring-boot-admin-starter-server'
	implementation 'gg.jte:jte-spring-boot-starter-4:3.2.4'
	implementation 'io.github.wimdeblauwe:htmx-spring-boot-thymeleaf:5.1.0'
	implementation 'io.grpc:grpc-services'
	implementation 'io.pivotal.spring.cloud:spring-cloud-services-starter-config-client'
	implementation 'io.pivotal.spring.cloud:spring-cloud-services-starter-service-registry'
	implementation 'io.sentry:sentry-spring-boot-4-starter'
	implementation 'net.ttddyy.observation:datasource-micrometer-opentelemetry'
	implementation 'net.ttddyy.observation:datasource-micrometer-spring-boot'
	implementation 'org.apache.camel.springboot:camel-spring-boot-starter:4.20.0'
	implementation 'org.apache.kafka:kafka-streams'
	implementation 'org.flywaydb:flyway-database-db2'
	implementation 'org.flywaydb:flyway-database-derby'
	implementation 'org.flywaydb:flyway-database-hsqldb'
	implementation 'org.flywaydb:flyway-database-oracle'
	implementation 'org.flywaydb:flyway-database-postgresql'
	implementation 'org.flywaydb:flyway-mysql'
	implementation 'org.flywaydb:flyway-sqlserver'
	implementation 'org.jobrunr:jobrunr-spring-boot-4-starter:8.7.0'
	implementation 'org.mongodb:mongodb-spring-session:4.0.0'
	implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:4.0.1'
	implementation 'org.springaicommunity:mcp-authorization-server-spring-boot:0.1.13'
	implementation 'org.springaicommunity:mcp-client-security-spring-boot:0.1.13'
	implementation 'org.springaicommunity:mcp-server-security-spring-boot:0.1.13'
	implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.2'
	implementation 'org.springframework.ai:spring-ai-jsoup-document-reader'
	implementation 'org.springframework.ai:spring-ai-markdown-document-reader'
	implementation 'org.springframework.ai:spring-ai-pdf-document-reader'
	implementation 'org.springframework.ai:spring-ai-starter-mcp-client-webflux'
	implementation 'org.springframework.ai:spring-ai-starter-mcp-server-webflux'
	implementation 'org.springframework.ai:spring-ai-starter-model-anthropic'
	implementation 'org.springframework.ai:spring-ai-starter-model-bedrock'
	implementation 'org.springframework.ai:spring-ai-starter-model-bedrock-converse'
	implementation 'org.springframework.ai:spring-ai-starter-model-chat-memory'
	implementation 'org.springframework.ai:spring-ai-starter-model-chat-memory-repository-cassandra'
	implementation 'org.springframework.ai:spring-ai-starter-model-chat-memory-repository-jdbc'
	implementation 'org.springframework.ai:spring-ai-starter-model-chat-memory-repository-mongodb'
	implementation 'org.springframework.ai:spring-ai-starter-model-chat-memory-repository-neo4j'
	implementation 'org.springframework.ai:spring-ai-starter-model-chat-memory-repository-redis'
	implementation 'org.springframework.ai:spring-ai-starter-model-deepseek'
	implementation 'org.springframework.ai:spring-ai-starter-model-elevenlabs'
	implementation 'org.springframework.ai:spring-ai-starter-model-google-genai'
	implementation 'org.springframework.ai:spring-ai-starter-model-google-genai-embedding'
	implementation 'org.springframework.ai:spring-ai-starter-model-mistral-ai'
	implementation 'org.springframework.ai:spring-ai-starter-model-ollama'
	implementation 'org.springframework.ai:spring-ai-starter-model-openai'
	implementation 'org.springframework.ai:spring-ai-starter-model-postgresml-embedding'
	implementation 'org.springframework.ai:spring-ai-starter-model-stability-ai'
	implementation 'org.springframework.ai:spring-ai-starter-model-transformers'
	implementation 'org.springframework.ai:spring-ai-starter-model-vertex-ai-embedding'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-aws-opensearch'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-azure'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-bedrock-knowledgebase'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-cassandra'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-chroma'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-couchbase'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-elasticsearch'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-gemfire'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-mariadb'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-milvus'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-mongodb-atlas'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-neo4j'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-opensearch'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-oracle'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-pgvector'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-pinecone'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-qdrant'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-redis'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-s3'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-typesense'
	implementation 'org.springframework.ai:spring-ai-starter-vector-store-weaviate'
	implementation 'org.springframework.ai:spring-ai-tika-document-reader'
	implementation 'org.springframework.ai:spring-ai-vector-store-advisor'
	implementation 'org.springframework.amqp:spring-rabbit-stream'
	implementation 'org.springframework.cloud:spring-cloud-bus'
	implementation 'org.springframework.cloud:spring-cloud-config-server'
	implementation 'org.springframework.cloud:spring-cloud-function-web'
	implementation 'org.springframework.cloud:spring-cloud-starter'
	implementation 'org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j'
	implementation 'org.springframework.cloud:spring-cloud-starter-config'
	implementation 'org.springframework.cloud:spring-cloud-starter-consul-config'
	implementation 'org.springframework.cloud:spring-cloud-starter-consul-discovery'
	implementation 'org.springframework.cloud:spring-cloud-starter-gateway-server-webflux'
	implementation 'org.springframework.cloud:spring-cloud-starter-gateway-server-webmvc'
	implementation 'org.springframework.cloud:spring-cloud-starter-loadbalancer'
	implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
	implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-server'
	implementation 'org.springframework.cloud:spring-cloud-starter-openfeign'
	implementation 'org.springframework.cloud:spring-cloud-starter-task'
	implementation 'org.springframework.cloud:spring-cloud-starter-vault-config'
	implementation 'org.springframework.cloud:spring-cloud-starter-zookeeper-config'
	implementation 'org.springframework.cloud:spring-cloud-starter-zookeeper-discovery'
	implementation 'org.springframework.cloud:spring-cloud-stream'
	implementation 'org.springframework.cloud:spring-cloud-stream-binder-kafka'
	implementation 'org.springframework.cloud:spring-cloud-stream-binder-kafka-streams'
	implementation 'org.springframework.cloud:spring-cloud-stream-binder-pulsar'
	implementation 'org.springframework.cloud:spring-cloud-stream-binder-rabbit'
	implementation 'org.springframework.data:spring-data-rest-hal-explorer'
	implementation 'org.springframework.grpc:spring-grpc-client-spring-boot-starter'
	implementation 'org.springframework.grpc:spring-grpc-server-web-spring-boot-starter'
	implementation 'org.springframework.integration:spring-integration-amqp'
	implementation 'org.springframework.integration:spring-integration-http'
	implementation 'org.springframework.integration:spring-integration-jdbc'
	implementation 'org.springframework.integration:spring-integration-jms'
	implementation 'org.springframework.integration:spring-integration-jpa'
	implementation 'org.springframework.integration:spring-integration-kafka'
	implementation 'org.springframework.integration:spring-integration-mail'
	implementation 'org.springframework.integration:spring-integration-mongodb'
	implementation 'org.springframework.integration:spring-integration-r2dbc'
	implementation 'org.springframework.integration:spring-integration-redis'
	implementation 'org.springframework.integration:spring-integration-rsocket'
	implementation 'org.springframework.integration:spring-integration-stomp'
	implementation 'org.springframework.integration:spring-integration-webflux'
	implementation 'org.springframework.integration:spring-integration-websocket'
	implementation 'org.springframework.integration:spring-integration-ws'
	implementation 'org.springframework.modulith:spring-modulith-events-api'
	implementation 'org.springframework.modulith:spring-modulith-starter-core'
	implementation 'org.springframework.modulith:spring-modulith-starter-insight'
	implementation 'org.springframework.modulith:spring-modulith-starter-jdbc'
	implementation 'org.springframework.modulith:spring-modulith-starter-jpa'
	implementation 'org.springframework.modulith:spring-modulith-starter-mongodb'
	implementation 'org.springframework.modulith:spring-modulith-starter-neo4j'
	implementation 'org.springframework.security:spring-security-messaging'
	implementation 'org.springframework.security:spring-security-messaging'
	implementation 'org.springframework.security:spring-security-rsocket'
	implementation 'org.springframework.security:spring-security-webauthn'
	implementation 'org.springframework.shell:spring-shell-starter'
	implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity6'
	compileOnly 'org.projectlombok:lombok'
	developmentOnly 'org.springframework.boot:spring-boot-devtools'
	developmentOnly 'org.springframework.boot:spring-boot-docker-compose'
	developmentOnly 'com.azure.spring:spring-cloud-azure-docker-compose'
	runtimeOnly 'com.h2database:h2'
	runtimeOnly 'com.ibm.db2:jcc'
	runtimeOnly 'com.microsoft.sqlserver:mssql-jdbc'
	runtimeOnly 'com.mysql:mysql-connector-j'
	runtimeOnly 'com.oracle.database.jdbc:ojdbc11'
	runtimeOnly 'com.oracle.database.r2dbc:oracle-r2dbc'
	runtimeOnly 'io.asyncer:r2dbc-mysql'
	runtimeOnly 'io.micrometer:micrometer-registry-datadog'
	runtimeOnly 'io.micrometer:micrometer-registry-dynatrace'
	runtimeOnly 'io.micrometer:micrometer-registry-graphite'
	runtimeOnly 'io.micrometer:micrometer-registry-influx'
	runtimeOnly 'io.micrometer:micrometer-registry-new-relic'
	runtimeOnly 'io.micrometer:micrometer-registry-otlp'
	runtimeOnly 'io.micrometer:micrometer-registry-prometheus'
	runtimeOnly 'io.r2dbc:r2dbc-h2'
	runtimeOnly 'io.r2dbc:r2dbc-mssql:1.0.0.RELEASE'
	runtimeOnly 'org.apache.derby:derby'
	runtimeOnly 'org.apache.derby:derbytools'
	runtimeOnly 'org.hsqldb:hsqldb'
	runtimeOnly 'org.mariadb:r2dbc-mariadb:1.1.3'
	runtimeOnly 'org.mariadb.jdbc:mariadb-java-client'
	runtimeOnly 'org.postgresql:postgresql'
	runtimeOnly 'org.postgresql:r2dbc-postgresql'
	developmentOnly 'org.springframework.ai:spring-ai-spring-boot-docker-compose'
	runtimeOnly 'org.springframework.modulith:spring-modulith-events-jms'
	runtimeOnly 'org.springframework.modulith:spring-modulith-runtime'
	runtimeOnly 'org.xerial:sqlite-jdbc'
	annotationProcessor 'org.projectlombok:lombok'
	annotationProcessor 'org.springframework.boot:spring-boot-configuration-processor'
	testImplementation 'org.springframework.boot:spring-boot-starter-activemq-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-actuator-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-amqp-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-artemis-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-batch-jdbc-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-batch-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-cache-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-cassandra-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-cloudfoundry-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-couchbase-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-cassandra-reactive-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-cassandra-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-couchbase-reactive-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-couchbase-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-elasticsearch-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-jdbc-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-jpa-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-ldap-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-mongodb-reactive-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-mongodb-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-neo4j-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-r2dbc-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-redis-reactive-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-redis-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-rest-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-elasticsearch-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-flyway-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-freemarker-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-graphql-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-groovy-templates-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-hateoas-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-hazelcast-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-jdbc-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-jersey-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-jooq-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-kafka-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-ldap'
	testImplementation 'org.springframework.boot:spring-boot-starter-ldap-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-liquibase-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-mail-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-mongodb-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-mustache-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-neo4j-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-opentelemetry-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-pulsar-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-quartz-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-r2dbc-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-restclient-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-restdocs'
	testImplementation 'org.springframework.boot:spring-boot-starter-rsocket-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-security-oauth2-authorization-server-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-security-oauth2-client-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-security-oauth2-resource-server-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-security-saml2-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-security-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-session-data-redis-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-session-jdbc-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-thymeleaf-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-validation-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-webclient-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-webflux-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-webmvc-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-webservices-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-websocket-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-zipkin-test'
	testImplementation 'org.springframework.boot:spring-boot-testcontainers'
	testImplementation 'com.azure.spring:spring-cloud-azure-testcontainers'
	testImplementation 'com.netflix.graphql.dgs:graphql-dgs-spring-graphql-starter-test'
	testImplementation 'com.unboundid:unboundid-ldapsdk'
	testImplementation 'io.projectreactor:reactor-test'
	testImplementation 'io.rest-assured:spring-web-test-client'
	testImplementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter-test:4.0.1'
	testImplementation 'org.springframework.ai:spring-ai-spring-boot-testcontainers'
	testImplementation 'org.springframework.cloud:spring-cloud-starter-contract-stub-runner'
	testImplementation 'org.springframework.cloud:spring-cloud-starter-contract-verifier'
	testImplementation 'org.springframework.cloud:spring-cloud-stream-test-binder'
	testImplementation 'org.springframework.grpc:spring-grpc-test'
	testImplementation 'org.springframework.integration:spring-integration-test'
	testImplementation 'org.springframework.modulith:spring-modulith-starter-test'
	testImplementation 'org.springframework.restdocs:spring-restdocs-mockmvc'
	testImplementation 'org.springframework.shell:spring-shell-starter-test'
	testImplementation 'org.testcontainers:testcontainers-activemq'
	testImplementation 'org.testcontainers:testcontainers-cassandra'
	testImplementation 'org.testcontainers:testcontainers-chromadb'
	testImplementation 'org.testcontainers:testcontainers-consul'
	testImplementation 'org.testcontainers:testcontainers-couchbase'
	testImplementation 'org.testcontainers:testcontainers-db2'
	testImplementation 'org.testcontainers:testcontainers-elasticsearch'
	testImplementation 'org.testcontainers:testcontainers-gcloud'
	testImplementation 'org.testcontainers:testcontainers-grafana'
	testImplementation 'org.testcontainers:testcontainers-junit-jupiter'
	testImplementation 'org.testcontainers:testcontainers-kafka'
	testImplementation 'org.testcontainers:testcontainers-mariadb'
	testImplementation 'org.testcontainers:testcontainers-milvus'
	testImplementation 'org.testcontainers:testcontainers-mongodb'
	testImplementation 'org.testcontainers:testcontainers-mssqlserver'
	testImplementation 'org.testcontainers:testcontainers-mysql'
	testImplementation 'org.testcontainers:testcontainers-neo4j'
	testImplementation 'org.testcontainers:testcontainers-ollama'
	testImplementation 'org.testcontainers:testcontainers-oracle-free'
	testImplementation 'org.testcontainers:testcontainers-postgresql'
	testImplementation 'org.testcontainers:testcontainers-pulsar'
	testImplementation 'org.testcontainers:testcontainers-qdrant'
	testImplementation 'org.testcontainers:testcontainers-r2dbc'
	testImplementation 'org.testcontainers:testcontainers-rabbitmq'
	testImplementation 'org.testcontainers:testcontainers-typesense'
	testImplementation 'org.testcontainers:testcontainers-vault'
	testImplementation 'org.testcontainers:testcontainers-weaviate'
	testCompileOnly 'org.projectlombok:lombok'
	testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
	testAnnotationProcessor 'org.projectlombok:lombok'
}

dependencyManagement {
	imports {
		mavenBom "com.solace.spring.cloud:solace-spring-cloud-bom:${solaceSpringCloudVersion}"
		mavenBom "org.springframework.modulith:spring-modulith-bom:${springModulithVersion}"
		mavenBom "com.vaadin:vaadin-bom:${vaadinVersion}"
		mavenBom "com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:${netflixDgsVersion}"
		mavenBom "org.springframework.shell:spring-shell-dependencies:${springShellVersion}"
		mavenBom "org.springframework.grpc:spring-grpc-dependencies:${springGrpcVersion}"
		mavenBom "de.codecentric:spring-boot-admin-dependencies:${springBootAdminVersion}"
		mavenBom "io.sentry:sentry-bom:${sentryVersion}"
		mavenBom "net.ttddyy.observation:datasource-micrometer-bom:${datasourceMicrometerVersion}"
		mavenBom "io.pivotal.spring.cloud:spring-cloud-services-dependencies:${springCloudServicesVersion}"
		mavenBom "com.vmware.tanzu.spring:tanzu-spring-sdk-dependencies:${tanzuSpringSdkVersion}"
		mavenBom "com.azure.spring:spring-cloud-azure-dependencies:${springCloudAzureVersion}"
		mavenBom "com.google.cloud:spring-cloud-gcp-dependencies:${springCloudGcpVersion}"
		mavenBom "org.springframework.ai:spring-ai-bom:${springAiVersion}"
		mavenBom "ai.timefold.solver:timefold-solver-bom:${timefoldSolverVersion}"
		mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
	}
}

generateJava {
	schemaPaths = ["${projectDir}/src/main/resources/graphql-client"]
	packageName = 'com.example.demo.codegen'
	generateClient = true
}

hibernate {
	enhancement {
	}
}

jte {
	generate()
	binaryStaticContent = true
}

contracts {
	testMode = 'WebTestClient'
}

protobuf {
	protoc {
		artifact = 'com.google.protobuf:protoc'
	}
	plugins {
		grpc {
			artifact = 'io.grpc:protoc-gen-grpc-java'
		}
	}
	generateProtoTasks {
		all()*.plugins {
			grpc {
				option '@generated=omit'
			}
		}
	}
}

tasks.named('contractTest') {
	useJUnitPlatform()
}

tasks.named('test') {
	outputs.dir snippetsDir
	useJUnitPlatform()
}

tasks.named('asciidoctor') {
	inputs.dir snippetsDir
	dependsOn test
}
```

## Boot 4.0.7 / Java 21

Идентично блоку «Boot 4.0.7 / Java 25» выше во всём, кроме одной строки:

```gradle
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
```
