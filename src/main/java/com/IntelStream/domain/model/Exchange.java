package com.IntelStream.domain.model;


import lombok.Value;
import lombok.Builder;
import java.time.LocalDateTime;

@Value
@Builder
public class Exchange {
    Long id;
    String code;
    String name;
    String timezone;
    String currency;
    LocalDateTime marketOpen;
    LocalDateTime marketClose;
    boolean active;

    public boolean isMarketOpen(LocalDateTime currentTime) {
        return currentTime.isAfter(marketOpen) && currentTime.isBefore(marketClose);
    }
}
