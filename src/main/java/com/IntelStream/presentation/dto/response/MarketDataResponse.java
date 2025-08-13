package com.IntelStream.presentation.dto.response;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Jacksonized
public class MarketDataResponse {
    Long id;
    Long instrumentId;
    String instrumentSymbol;
    Long exchangeId;
    String exchangeCode;
    BigDecimal price;
    BigDecimal volume;
    BigDecimal bidPrice;
    BigDecimal askPrice;
    LocalDateTime timestamp;
    String source;



    @Builder.Default
    BigDecimal priceChangePercent = BigDecimal.ZERO;

    @Builder.Default
    BigDecimal volumeChangePercent = BigDecimal.ZERO;
}
