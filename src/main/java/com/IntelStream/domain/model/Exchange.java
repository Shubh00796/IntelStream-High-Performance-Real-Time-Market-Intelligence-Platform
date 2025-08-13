package com.IntelStream.domain.model;


import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
