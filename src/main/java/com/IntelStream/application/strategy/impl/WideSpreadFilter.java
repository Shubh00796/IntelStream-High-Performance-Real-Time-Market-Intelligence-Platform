package com.IntelStream.application.strategy.impl;

import com.IntelStream.application.strategy.MarketDataFilterStrategy;
import com.IntelStream.domain.model.MarketData;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component("wideSpread")
public class WideSpreadFilter implements MarketDataFilterStrategy {
    // This filter is designed to exclude market data with a spread greater than a specified threshold.
    private static final BigDecimal SPREAD_THRESHOLD = BigDecimal.valueOf(5);

    @Override
    public List<MarketData> filter(List<MarketData> data) {
        return data
                .stream()
                .filter(marketData -> marketData.getSpread().compareTo(SPREAD_THRESHOLD) < 0)
                .collect(Collectors.toList());
    }
}
