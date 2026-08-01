# Arya Banking Outbox Service

Transactional Outbox Pattern starter library — guarantees at-least-once delivery of domain events to Apache Kafka via MongoDB.

## Quick Start

Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>org.arya.banking</groupId>
    <artifactId>arya-banking-outbox-service</artifactId>
    <version>1.0.0</version>
</dependency>
```

Enable the starter in `application.yaml`:

```yaml
arya:
  outbox:
    enabled: true
```

## Links

- [Local Development Setup](https://event-based-banking-application.github.io/arya-banking/docs/local-development/)
- [Outbox Service Docs](https://event-based-banking-application.github.io/arya-banking/docs/outbox-service/)
