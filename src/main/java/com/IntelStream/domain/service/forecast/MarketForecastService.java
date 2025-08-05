package com.IntelStream.domain.service.forecast;


import com.IntelStream.domain.model.MarketForecast;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MarketForecastService {

    List<MarketForecast> filterHighConfidence(List<MarketForecast> forecasts);

    List<MarketForecast> getAIModelForecasts(List<MarketForecast> forecasts);

    List<String> getSymbolsWithUptrend(List<MarketForecast> forecasts);

    Map<MarketForecast.ForecastSource, Long> countBySource(List<MarketForecast> forecasts);

    Optional<MarketForecast> getMostConfidentForecast(List<MarketForecast> forecasts);

    Map<String, Double> averageConfidenceBySymbol(List<MarketForecast> forecasts);

    Map<MarketForecast.TrendDirection, List<MarketForecast>> groupByTrend(List<MarketForecast> forecasts);

    List<MarketForecast> sortByConfidence(List<MarketForecast> forecasts);

    BigDecimal averageExpectedPriceForSymbol(List<MarketForecast> forecasts, String symbol);

    boolean allForecastsAboveThreshold(List<MarketForecast> forecasts, BigDecimal threshold);

    List<MarketForecast> forecastsInTimeRange(List<MarketForecast> forecasts, LocalDateTime from, LocalDateTime to);
}
