package com.IntelStream.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Immutable snapshot of analytics data for a financial instrument.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnalyticsSnapshot {
    Long id;
    Long instrumentId;
    BigDecimal movingAverage20;
    BigDecimal movingAverage50;
    BigDecimal volatility;
    BigDecimal rsi;
    BigDecimal volume24h;
    BigDecimal priceChange24h;
    BigDecimal priceChangePercent24h;
    LocalDateTime calculatedAt;

    /**
     * Determines if the instrument is overbought based on RSI.
     * @return true if RSI > 70
     */
    public boolean isOverbought() {
        return rsi != null && rsi.compareTo(BigDecimal.valueOf(70)) > 0;
    }

    /**
     * Determines if the instrument is oversold based on RSI.
     * @return true if RSI < 30
     */
    public boolean isOversold() {
        return rsi != null && rsi.compareTo(BigDecimal.valueOf(30)) < 0;
    }
}
