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
public class MarketDataEvent {
    private Long instrumentId;
    private Long exchangeId;
    private BigDecimal price;
    private BigDecimal volume;
    private BigDecimal bidPrice;
    private BigDecimal askPrice;
    private LocalDateTime timestamp;
    private String source;
    private String eventType; // PRICE_UPDATE, VOLUME_SPIKE, etc.
}
