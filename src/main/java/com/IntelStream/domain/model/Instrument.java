package com.IntelStream.domain.model;


import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class Instrument {
    Long id;
    String symbol;
    String name;
    String instrumentType; // STOCK, CRYPTO, etc.
    String sector;
    Long exchangeId;
    String currency;
    boolean active;
    LocalDateTime createdAt;

    public boolean isType(String type) {
        return instrumentType != null && instrumentType.equalsIgnoreCase(type);
    }

    public boolean isEquity() {
        return isType("STOCK");
    }

    public boolean isCryptocurrency() {
        return isType("CRYPTO");
    }
}
