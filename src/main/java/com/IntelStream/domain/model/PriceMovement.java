package com.IntelStream.domain.model;


import lombok.Value;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class PriceMovement {
    Long instrumentId;
    BigDecimal previousPrice;
    BigDecimal currentPrice;
    BigDecimal changePercent;
    boolean upward;
    LocalDateTime timestamp;

    public boolean isSignificant(BigDecimal threshold) {
        return changePercent.abs().compareTo(threshold) > 0;
    }
}
