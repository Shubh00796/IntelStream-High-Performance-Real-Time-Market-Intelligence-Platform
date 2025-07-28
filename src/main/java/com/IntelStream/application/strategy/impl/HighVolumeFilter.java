package com.IntelStream.application.strategy.impl;

import com.IntelStream.application.strategy.MarketDataFilterStrategy;
import com.IntelStream.domain.model.MarketData;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component("highVolume")
public class HighVolumeFilter implements MarketDataFilterStrategy {

    private static final BigDecimal VOLUME_THRESHOLD = BigDecimal.valueOf(10000);

    @Override
    public List<MarketData> filter(List<MarketData> data) {
        return data.stream()
                .filter(marketData -> marketData.getVolume().compareTo(VOLUME_THRESHOLD) > 0)
                .collect(Collectors.toList());
    }
}
