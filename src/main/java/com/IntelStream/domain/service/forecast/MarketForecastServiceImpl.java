package com.IntelStream.domain.service.forecast;

import com.IntelStream.domain.model.MarketForecast;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MarketForecastServiceImpl implements MarketForecastService {
    @Override
    public List<MarketForecast> filterHighConfidence(List<MarketForecast> forecasts) {
        return forecasts.stream()
                .filter(MarketForecast::isHighConfidence)
                .collect(Collectors.toList());
    }

    @Override
    public List<MarketForecast> getAIModelForecasts(List<MarketForecast> forecasts) {
        return forecasts
                .stream()
                .filter(MarketForecast::isFromAI)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getSymbolsWithUptrend(List<MarketForecast> forecasts) {
        return forecasts
                .stream()
                .filter(MarketForecast::isUptrend)
                .map(MarketForecast::getSymbol)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Map<MarketForecast.ForecastSource, Long> countBySource(List<MarketForecast> forecasts) {
        return forecasts
                .stream()
                .collect(Collectors.groupingBy(MarketForecast::getSource, Collectors.counting()));
    }

    @Override
    public Optional<MarketForecast> getMostConfidentForecast(List<MarketForecast> forecasts) {
        return forecasts
                .stream()
                .max(Comparator.comparing(MarketForecast::getConfidenceScore));
    }

    @Override
    public Map<String, Double> averageConfidenceBySymbol(List<MarketForecast> forecasts) {
        return forecasts
                .stream()
                .collect(Collectors.groupingBy(MarketForecast::getSymbol,
                        Collectors.averagingDouble(MarketForecast::getConfidenceScore)
                ));
    }

    @Override
    public Map<MarketForecast.TrendDirection, List<MarketForecast>> groupByTrend(List<MarketForecast> forecasts) {
        return forecasts
                .stream()
                .collect(Collectors.groupingBy(MarketForecast::getDirection));
    }

    @Override
    public List<MarketForecast> sortByConfidence(List<MarketForecast> forecasts) {
        return forecasts
                .stream()
                .sorted(Comparator.comparingDouble(MarketForecast::getConfidenceScore).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal averageExpectedPriceForSymbol(List<MarketForecast> forecasts, String symbol) {
        return forecasts.stream()
                .filter(f -> f.getSymbol().equalsIgnoreCase(symbol))
                .map(MarketForecast::getExpectedPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(
                        BigDecimal.valueOf(forecasts.stream()
                                .filter(f -> f.getSymbol().equalsIgnoreCase(symbol)).count()),
                        2,
                        BigDecimal.ROUND_HALF_UP
                );
    }

    @Override
    public boolean allForecastsAboveThreshold(List<MarketForecast> forecasts, BigDecimal threshold) {
        return forecasts
                .stream()
                .allMatch(marketForecast -> marketForecast.getExpectedPrice().compareTo(threshold) > 0);
    }

    @Override
    public List<MarketForecast> forecastsInTimeRange(List<MarketForecast> forecasts, LocalDateTime from, LocalDateTime to) {
        return forecasts
                .stream()
                .filter(marketForecast -> !marketForecast.getForecastFor().isBefore(from) && !marketForecast.getForecastFor().isAfter(to))
                .collect(Collectors.toList());
    }
}
