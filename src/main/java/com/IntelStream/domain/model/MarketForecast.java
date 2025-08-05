package com.IntelStream.domain.model;


import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a future market forecast for a financial instrument.
 */
@Value
@Builder
public class MarketForecast {
    Long forecastId;
    String symbol;
    LocalDateTime generatedAt;
    LocalDateTime forecastFor;
    ForecastSource source; // AI_MODEL, ANALYST, HISTORICAL_TREND, etc.
    BigDecimal expectedPrice;
    double confidenceScore; // 0.0 to 1.0
    TrendDirection direction; // UP, DOWN, STABLE

    public enum ForecastSource {
        AI_MODEL, ANALYST, HISTORICAL_TREND
    }

    public enum TrendDirection {
        UP, DOWN, STABLE
    }

    public boolean isHighConfidence() {
        return confidenceScore >= 0.8;
    }

    public boolean isFromAI() {
        return source == ForecastSource.AI_MODEL;
    }

    public boolean isUptrend() {
        return direction == TrendDirection.UP;
    }

    public boolean isDowntrend() {
        return direction == TrendDirection.DOWN;
    }
}
