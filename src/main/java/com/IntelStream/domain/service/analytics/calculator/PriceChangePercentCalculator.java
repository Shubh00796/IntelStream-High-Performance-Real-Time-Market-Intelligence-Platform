package com.IntelStream.domain.service.analytics.calculator;


import com.IntelStream.domain.model.MarketData;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class PriceChangePercentCalculator {

    public BigDecimal calculate(List<MarketData> data, LocalDateTime now) {
        Optional<MarketData> latest = data.stream()
                .filter(d -> d.getTimestamp().isBefore(now))
                .max(Comparator.comparing(MarketData::getTimestamp));

        Optional<MarketData> past = data.stream()
                .filter(d -> d.getTimestamp().isBefore(now.minusHours(24)))
                .max(Comparator.comparing(MarketData::getTimestamp));

        if (latest.isEmpty() || past.isEmpty()) return BigDecimal.ZERO;

        BigDecimal current = latest.get().getPrice();
        BigDecimal previous = past.get().getPrice();

        return current.subtract(previous)
                .divide(previous, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
}
