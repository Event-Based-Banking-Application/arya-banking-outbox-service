package org.arya.banking.outbox.repository;

import org.arya.banking.common.model.OutboxEvent;
import org.arya.banking.common.model.OutboxStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface OutboxEventRepository<T extends OutboxEvent> extends MongoRepository<T, String> {

    List<T> findByOutboxStatusIn(List<OutboxStatus> outboxStatuses);
}
