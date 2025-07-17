package com.IntelStream.presentation.dto.response;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class AnalyticsResponse {
    Long id;
    Long instrumentId;
    String instrumentSymbol;
    BigDecimal movingAverage20;
    BigDecimal movingAverage50;
    BigDecimal volatility;
    BigDecimal rsi;
    BigDecimal volume24h;
    BigDecimal priceChange24h;
    BigDecimal priceChangePercent24h;
    LocalDateTime calculatedAt;

    // Signal indicators
    Boolean isOverbought;
    Boolean isOversold;
    String trend; // BULLISH, BEARISH, NEUTRAL
    String signal; // BUY, SELL, HOLD
}