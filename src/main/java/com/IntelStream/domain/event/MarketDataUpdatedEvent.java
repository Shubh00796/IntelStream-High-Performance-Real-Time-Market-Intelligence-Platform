package com.IntelStream.domain.event;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class MarketDataUpdatedEvent {
    Long marketDataId;
    Long instrumentId;
    BigDecimal price;
    BigDecimal volume;
    LocalDateTime timestamp;
}
