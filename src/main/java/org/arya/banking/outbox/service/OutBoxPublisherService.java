package org.arya.banking.outbox.service;

import lombok.extern.slf4j.Slf4j;
import org.arya.banking.common.avro.OutboxKafkaEvent;
import org.arya.banking.common.model.OutboxEvent;
import org.arya.banking.outbox.autoconfigure.OutboxProperties;
import org.arya.banking.outbox.kafka.OutboxEventProducer;
import org.arya.banking.outbox.repository.OutboxEventRepository;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

import static org.arya.banking.common.model.OutboxStatus.*;

@Slf4j
public class OutBoxPublisherService<T extends OutboxEvent> {

    private final OutboxEventRepository<T> outboxEventRepository;
    private final OutboxEventProducer outboxEventProducer;
    private final OutboxProperties outboxProperties;

    public OutBoxPublisherService(OutboxEventRepository<T> outboxEventRepository,
                                  OutboxEventProducer outboxEventProducer,
                                  OutboxProperties outboxProperties) {
        this.outboxEventRepository = outboxEventRepository;
        this.outboxEventProducer = outboxEventProducer;
        this.outboxProperties = outboxProperties;
    }

    @Scheduled(fixedDelayString = "${arya.outbox.publish-interval-ms:5000}")
    public void publishPendingAndRetry() {

        List<T> pendingEvent = outboxEventRepository.findByOutboxStatusIn(List.of(PENDING, RETRY_PENDING));
        pendingEvent.forEach(event -> {
            try {
                outboxEventProducer.sendOutboxEvent(event.getTopic(), OutboxKafkaEvent.newBuilder()
                        .setAggregateId(event.getAggregateId())
                        .setEventType(event.getEventType())
                        .setPayload(event.getPayload()).build());
                event.setOutboxStatus(COMPLETED);
            } catch (Exception e) {
                log.error("Error occurred while sending to kafka:", e);
                event.setOutboxStatus(RETRY_PENDING);
            } finally {
                handleEventUpdate(event);
            }
        });

    }

    private void handleEventUpdate(T event) {
        if (event.getRetryCount() < outboxProperties.getMaxRetries()) {
            event.setRetryCount(event.getRetryCount() + 1);
        } else {
            event.setOutboxStatus(FAILED);
        }
        outboxEventRepository.save(event);
    }
}
