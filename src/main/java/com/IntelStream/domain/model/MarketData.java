package com.IntelStream.domain.model;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MarketData {
    Long id;
    Long instrumentId;
    String instrumentSymbol;
    Long exchangeId;
    BigDecimal price;
    BigDecimal volume;
    BigDecimal bidPrice;
    BigDecimal askPrice;
    LocalDateTime timestamp;
    String source;

    public boolean hasSignificantMovement(BigDecimal previousPrice, BigDecimal threshold) {
        if (previousPrice == null || threshold == null) return false;
        BigDecimal changePercent = price.subtract(previousPrice)
                .divide(previousPrice, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        return changePercent.abs().compareTo(threshold) > 0;
    }

    public BigDecimal getSpread() {
        return askPrice.subtract(bidPrice);
    }
}
