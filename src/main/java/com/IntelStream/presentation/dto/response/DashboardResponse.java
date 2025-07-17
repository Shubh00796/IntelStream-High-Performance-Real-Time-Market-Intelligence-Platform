package com.IntelStream.presentation.dto.response;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
@Jacksonized
public class DashboardResponse {
    List<MarketDataResponse> latestPrices;
    List<AnalyticsResponse> analytics;
    List<MarketDataResponse> priceHistory;
    MarketSummary summary;
    LocalDateTime lastUpdated;

    @Value
    @Builder
    @Jacksonized
    public static class MarketSummary {
        Integer totalInstruments;
        BigDecimal totalVolume24h;
        BigDecimal avgVolatility;
        BigDecimal avgRsi;
        Integer overboughtCount;
        Integer oversoldCount;
        Integer activeExchanges;
    }
}
