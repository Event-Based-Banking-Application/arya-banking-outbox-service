# Arya Banking Outbox Service

[![Maven Package](https://img.shields.io/github/v/tag/Event-Based-Banking-Application/arya-banking-outbox-service?label=package&color=blue&logo=apache-maven)](https://github.com/Event-Based-Banking-Application/arya-banking-outbox-service/packages)
[![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.4-6DB33F?logo=springboot)](https://spring.io/projects/spring-boot)

## Overview

**Arya Banking Outbox Service** is a Spring Boot starter library that implements the **Transactional Outbox Pattern** for the Arya Banking ecosystem. It guarantees **at-least-once delivery** of domain events to Apache Kafka by persisting outbox records to MongoDB in the same transaction as the business state change, then asynchronously relaying them to Kafka on a scheduled poll.

Intended to be used as a dependency in any Arya Banking microservice that publishes domain events.

---

## Features

- Spring Boot auto-configuration — drop-in starter, zero wiring
- MongoDB-backed generic `OutboxEventRepository<T>`
- Scheduled relay with bounded retry (`PENDING → COMPLETED` / `RETRY_PENDING → FAILED`)
- Avro integration via `OutboxKafkaEvent` (Confluent Schema Registry)
- Property-driven — `arya.outbox.{enabled, publish-interval-ms, max-retries}`

---

## Usage

Add the dependency in your service's `pom.xml`:

```xml
<dependency>
    <groupId>org.arya.banking</groupId>
    <artifactId>arya-banking-outbox-service</artifactId>
    <version>1.0.0</version>
</dependency>
```

Configure `application.yaml`:

```yaml
arya:
  outbox:
    enabled: true
    publish-interval-ms: 5000
    max-retries: 3
```

Write the business change and the outbox record in the same MongoDB transaction:

```java
@Transactional
public void registerUser(User user) {
    userRepository.save(user);
    outboxRepository.save(UserOutboxEvent.builder()
        .aggregateId(user.getId())
        .eventType("USER_REGISTERED")
        .topic("user.events")
        .payload(/* Avro payload */)
        .outboxStatus(OutboxStatus.PENDING)
        .retryCount(0)
        .build());
}
```

The scheduled publisher picks it up on the next tick and forwards it to Kafka.

---

## Documentation

Full reference, configuration matrix, and lifecycle diagrams:

➡️ [Outbox Service Docs](https://event-based-banking-application.github.io/arya-banking/docs/outbox-service/overview/)

---

## License

[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)

---

## Links

- [Source](https://github.com/Event-Based-Banking-Application/arya-banking-outbox-service)
- [Maven Package](https://github.com/Event-Based-Banking-Application/arya-banking-outbox-service/packages)
- [Arya Banking Common](https://github.com/Event-Based-Banking-Application/arya-banking-common) — provides `OutboxEvent`, `OutboxKafkaEvent`, `OutboxStatus`
- [Arya Banking BOM](https://github.com/Event-Based-Banking-Application/arya-banking-bom)
