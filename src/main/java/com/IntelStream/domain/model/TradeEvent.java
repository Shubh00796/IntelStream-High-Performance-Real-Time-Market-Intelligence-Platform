package com.IntelStream.domain.model;


import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a real-time trade event for a given instrument.
 */
@Value
@Builder
public class TradeEvent {
    Long tradeId;
    Long instrumentId;
    String symbol;
    String exchange;
    LocalDateTime timestamp;
    BigDecimal price;
    BigDecimal quantity;
    TradeType tradeType; // BUY or SELL

    public enum TradeType {
        BUY, SELL
    }

    public BigDecimal getTradeValue() {
        return price.multiply(quantity);
    }

    public boolean isBuy() {
        return TradeType.BUY.equals(tradeType);
    }

    public boolean isSell() {
        return TradeType.SELL.equals(tradeType);
    }
}
