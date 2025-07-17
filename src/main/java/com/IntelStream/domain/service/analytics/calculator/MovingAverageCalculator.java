package com.IntelStream.domain.service.analytics.calculator;


import com.IntelStream.domain.model.MarketData;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class MovingAverageCalculator {

    public BigDecimal calculate(List<MarketData> data, int period) {
        if (data.size() < period) return BigDecimal.ZERO;
        return data.subList(data.size() - period, data.size()).stream()
                .map(MarketData::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(period), BigDecimal.ROUND_HALF_UP);
    }
}
