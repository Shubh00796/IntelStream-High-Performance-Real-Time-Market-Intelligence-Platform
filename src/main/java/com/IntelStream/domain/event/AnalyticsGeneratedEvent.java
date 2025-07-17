package com.IntelStream.domain.event;


import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class AnalyticsGeneratedEvent {
    Long analyticsId;
    Long instrumentId;
    BigDecimal rsi;
    BigDecimal volatility;
    LocalDateTime calculatedAt;
}
