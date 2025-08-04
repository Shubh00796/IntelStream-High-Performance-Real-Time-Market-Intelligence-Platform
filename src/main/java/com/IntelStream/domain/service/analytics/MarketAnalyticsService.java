package com.IntelStream.domain.service.analytics;

import com.IntelStream.application.common.exception.ResourceNotFoundException;
import com.IntelStream.application.strategy.SignalStrategy;
import com.IntelStream.domain.model.AnalyticsSnapshot;
import com.IntelStream.domain.model.MarketData;
import com.IntelStream.domain.service.analytics.calculator.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
@Slf4j
public class MarketAnalyticsService {

    private final RSIIndicatorCalculator rsiCalculator;
    private final VolatilityCalculator volatilityCalculator;
    private final MovingAverageCalculator movingAverageCalculator;
    private final PriceChangeCalculator priceChangeCalculator;
    private final PriceChangePercentCalculator priceChangePercentCalculator;
    private final Volume24hCalculator volume24hCalculator;
    private final Map<String, SignalStrategy> strategies;

    public AnalyticsSnapshot calculateAnalytics(
            Long instrumentId,
            List<MarketData> historicalData,
            LocalDateTime calculatedAt
    ) {
        validateHistoricalData(historicalData);

        BigDecimal rsi = rsiCalculator.calculate(historicalData);
        BigDecimal volatility = volatilityCalculator.calculate(historicalData);
        BigDecimal ma20 = movingAverageCalculator.calculate(historicalData, 20);
        BigDecimal ma50 = movingAverageCalculator.calculate(historicalData, 50);
        BigDecimal priceChange24h = priceChangeCalculator.calculate(historicalData, calculatedAt);
        BigDecimal priceChangePercent24h = priceChangePercentCalculator.calculate(historicalData, calculatedAt);
        BigDecimal volume24h = volume24hCalculator.calculate(historicalData, calculatedAt);

        boolean isOverbought = rsi.compareTo(BigDecimal.valueOf(70)) > 0;
        boolean isOversold = rsi.compareTo(BigDecimal.valueOf(30)) < 0;
        String trend = determineTrend(ma20, ma50);
        String signal = determineSignal(rsi, trend);

        log.debug("Analytics: RSI={}, Volatility={}, MA20={}, MA50={}, ΔPrice={}, Δ%, Volume={}, Trend={}, Signal={}",
                rsi, volatility, ma20, ma50, priceChange24h, priceChangePercent24h, volume24h, trend, signal);

        return AnalyticsSnapshot.builder()
                .instrumentId(instrumentId)
                .rsi(rsi)
                .volatility(volatility)
                .movingAverage20(ma20)
                .movingAverage50(ma50)
                .priceChange24h(priceChange24h)
                .priceChangePercent24h(priceChangePercent24h)
                .volume24h(volume24h)
                .calculatedAt(calculatedAt)
                .build();
    }

    // --- Helper logic ---

    // Predicates for validation
    private static final Predicate<List<MarketData>> isEmptyData =
            data -> data == null || data.isEmpty();

    // Trend determination logic
    private static final BiPredicate<BigDecimal, BigDecimal> isBullish =
            (ma20, ma50) -> ma20.compareTo(ma50) > 0;

    private static final BiPredicate<BigDecimal, BigDecimal> isBearish =
            (ma20, ma50) -> ma20.compareTo(ma50) < 0;

    // RSI thresholds
    private static final Predicate<BigDecimal> isRsiAbove30 =
            rsi -> rsi.compareTo(BigDecimal.valueOf(30)) > 0;

    private static final Predicate<BigDecimal> isRsiBelow70 =
            rsi -> rsi.compareTo(BigDecimal.valueOf(70)) < 0;

    public void validateHistoricalData(List<MarketData> historicalData) {
        if (historicalData == null || historicalData.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Cannot calculate analytics with empty market data.");
        }
    }


    public String determineTrend(BigDecimal ma20, BigDecimal ma50) {
        if (isBullish.test(ma20, ma50)) return "BULLISH";
        if (isBearish.test(ma20, ma50)) return "BEARISH";
        return "NEUTRAL";
    }

    public String determineSignal(BigDecimal rsi, String trend) {
        SignalStrategy strategy = strategies.getOrDefault(trend.toLowerCase(), strategies.get("neutral"));
        return strategy.generateSignal(rsi);
    }
}
