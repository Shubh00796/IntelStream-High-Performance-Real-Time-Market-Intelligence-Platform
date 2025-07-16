package com.IntelStream.domain.model;


import lombok.Value;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
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

    public boolean isOverbought() {
        return rsi != null && rsi.compareTo(BigDecimal.valueOf(70)) > 0;
    }

    public boolean isOversold() {
        return rsi != null && rsi.compareTo(BigDecimal.valueOf(30)) < 0;
    }
}
