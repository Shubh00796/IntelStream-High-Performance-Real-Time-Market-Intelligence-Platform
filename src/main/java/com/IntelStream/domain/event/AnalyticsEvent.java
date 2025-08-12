package com.IntelStream.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsEvent {
    private Long instrumentId;
    private BigDecimal rsi;
    private BigDecimal movingAverage20;
    private BigDecimal movingAverage50;
    private BigDecimal volatility;
    private BigDecimal supportLevel;
    private BigDecimal resistanceLevel;
    private LocalDateTime timestamp;
    private String signal; // BUY, SELL, HOLD
}
