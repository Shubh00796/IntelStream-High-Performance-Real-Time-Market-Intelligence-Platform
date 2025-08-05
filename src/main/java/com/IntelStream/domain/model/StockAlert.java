package com.IntelStream.domain.model;


import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * Represents an alert triggered by a predefined stock condition.
 */
@Value
@Builder
public class StockAlert {
    Long alertId;
    String symbol;
    AlertType type; // PRICE_SPIKE, VOLUME_SURGE, RSI_OVERBOUGHT, RSI_OVERSOLD
    String message;
    LocalDateTime triggeredAt;
    double severityScore; // 0.0 to 1.0
    boolean acknowledged;

    public enum AlertType {
        PRICE_SPIKE, VOLUME_SURGE, RSI_OVERBOUGHT, RSI_OVERSOLD
    }

    public boolean isSevere() {
        return severityScore >= 0.75;
    }

    public boolean isUnacknowledged() {
        return !acknowledged;
    }
}
