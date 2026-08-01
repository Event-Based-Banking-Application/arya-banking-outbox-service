package org.arya.banking.outbox.autoconfigure;

import org.arya.banking.common.avro.OutboxKafkaEvent;
import org.arya.banking.outbox.kafka.OutboxEventProducer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;

@AutoConfiguration
@ConditionalOnClass(KafkaTemplate.class)
@EnableConfigurationProperties(OutboxProperties.class)
@EnableScheduling
@ConditionalOnProperty(prefix = "arya.outbox", name = "enabled", havingValue = "true", matchIfMissing = true)
public class OutboxAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public KafkaTemplate<String, OutboxKafkaEvent> outboxEventKafkaTemplate(
            ProducerFactory<String, OutboxKafkaEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public OutboxEventProducer outboxEventProducer(
            KafkaTemplate<String, OutboxKafkaEvent> outboxEventTemplate) {
        return new OutboxEventProducer(outboxEventTemplate);
    }
}
