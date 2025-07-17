package com.IntelStream.presentation.dto.response;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
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

    // Additional computed fields
    BigDecimal spread;
    BigDecimal priceChangePercent;
    BigDecimal volumeChangePercent;
}