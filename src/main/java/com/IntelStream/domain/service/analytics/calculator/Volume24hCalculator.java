package com.IntelStream.domain.service.analytics.calculator;


import com.IntelStream.domain.model.MarketData;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class Volume24hCalculator {

    public BigDecimal calculate(List<MarketData> data, LocalDateTime now) {
        return data.stream()
                .filter(d -> d.getTimestamp().isAfter(now.minusHours(24)))
                .map(MarketData::getVolume)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
