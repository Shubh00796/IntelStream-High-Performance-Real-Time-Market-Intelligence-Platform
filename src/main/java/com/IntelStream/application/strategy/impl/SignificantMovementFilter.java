package com.IntelStream.application.strategy.impl;

import com.IntelStream.application.strategy.MarketDataFilterStrategy;
import com.IntelStream.domain.model.MarketData;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component("significantMovement")
public class SignificantMovementFilter implements MarketDataFilterStrategy {

    private static final BigDecimal THRESHOLD = BigDecimal.valueOf(2.0); // 2%

    @Override
    public List<MarketData> filter(List<MarketData> data) {
        return data
                .stream()
                .filter(marketData -> marketData.hasSignificantMovement(BigDecimal.valueOf(100), THRESHOLD))
                .collect(Collectors.toList());

    }
}
