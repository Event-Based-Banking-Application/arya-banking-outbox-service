package org.arya.banking.outbox.kafka;

import lombok.extern.slf4j.Slf4j;
import org.arya.banking.common.avro.OutboxKafkaEvent;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
public class OutboxEventProducer {

    private final KafkaTemplate<String, OutboxKafkaEvent> outboxEventTemplate;

    public OutboxEventProducer(KafkaTemplate<String, OutboxKafkaEvent> outboxEventTemplate) {
        this.outboxEventTemplate = outboxEventTemplate;
    }

    public void sendOutboxEvent(String topic, OutboxKafkaEvent outboxEvent) {
        outboxEventTemplate.send(topic, outboxEvent.getAggregateId().toString(), outboxEvent);
        log.info("Outbox Event for aggregate Id: {} sent", outboxEvent.getAggregateId());
    }

}
