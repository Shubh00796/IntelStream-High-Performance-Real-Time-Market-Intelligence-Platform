package com.IntelStream.domain.model;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
