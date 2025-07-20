package com.IntelStream.domain.service.analytics;

import com.IntelStream.application.common.exception.ResourceNotFoundException;
import com.IntelStream.domain.model.AnalyticsSnapshot;
import com.IntelStream.domain.model.MarketData;
import com.IntelStream.domain.service.analytics.calculator.RSIIndicatorCalculator;
import com.IntelStream.domain.service.analytics.calculator.VolatilityCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MarketAnalyticsService {

    private final RSIIndicatorCalculator rsiCalculator;
    private final VolatilityCalculator volatilityCalculator;

    public AnalyticsSnapshot calculateAnalytics(
            Long instrumentId,
            List<MarketData> historicalData,
            LocalDateTime calculatedAt
    ) {
        validateHistroricalData(historicalData);

        BigDecimal rsi = rsiCalculator.calculate(historicalData);
        BigDecimal volatility = volatilityCalculator.calculate(historicalData);

        log.debug("Calculated RSI: {}, Volatility: {}", rsi, volatility);

        return getAnalyticsSnapshot
                (instrumentId, calculatedAt, rsi, volatility);
    }

    private static void validateHistroricalData(List<MarketData> historicalData) {
        validateHistoricalData(historicalData);
    }

    private static void validateHistoricalData(List<MarketData> historicalData) {
        if (historicalData == null || historicalData.isEmpty()) {
            throw new ResourceNotFoundException("Cannot calculate analytics with empty market data.");
        }
    }

    private static AnalyticsSnapshot getAnalyticsSnapshot(Long instrumentId, LocalDateTime calculatedAt, BigDecimal rsi, BigDecimal volatility) {
        return AnalyticsSnapshot.builder()
                .instrumentId(instrumentId)
                .rsi(rsi)
                .volatility(volatility)
                .calculatedAt(calculatedAt)
                .build();
    }
}
