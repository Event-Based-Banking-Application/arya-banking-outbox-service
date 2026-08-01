package org.arya.banking.outbox.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "arya.outbox")
public class OutboxProperties {

    /**
     * Whether the outbox library is enabled.
     */
    private boolean enabled = true;

    /**
     * Fixed delay in milliseconds between scheduled outbox publication attempts.
     */
    private long publishIntervalMs = 5000;

    /**
     * Maximum number of retry attempts for failed outbox events.
     */
    private int maxRetries = 3;
}
