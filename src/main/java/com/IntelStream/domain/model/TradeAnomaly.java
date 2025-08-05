package com.IntelStream.domain.model;


import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * Represents a suspicious or abnormal trade activity.
 */
@Value
@Builder
public class TradeAnomaly {
    Long id;
    String symbol;
    AnomalyType type;
    String reason;
    LocalDateTime detectedAt;
    double severityScore; // 0.0 to 1.0
    boolean resolved;

    public enum AnomalyType {
        PRICE_OUTLIER,
        VOLUME_SPIKE,
        REPEATED_TRADES,
        LATENCY_HICCUP,
        UNUSUAL_ORDER_BOOK_MOVEMENT
    }

    public boolean isSevere() {
        return severityScore > 0.8;
    }

    public boolean isUnresolved() {
        return !resolved;
    }
}
