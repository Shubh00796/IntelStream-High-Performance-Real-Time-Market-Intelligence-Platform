package com.IntelStream.domain.service.analytics.calculator;


import com.IntelStream.domain.model.MarketData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

@Component
@Slf4j
public class VolatilityCalculator {

    private static final MathContext MATH_CONTEXT = new MathContext(6, RoundingMode.HALF_UP);

    public BigDecimal calculate(List<MarketData> data) {
        if (data == null || data.size() < 2) {
            log.warn("Insufficient data for volatility calculation. At least 2 entries are required.");
            return BigDecimal.ZERO;
        }

        BigDecimal mean = data.stream()
                .map(MarketData::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(data.size()), MATH_CONTEXT);

        BigDecimal varianceSum = data.stream()
                .map(MarketData::getPrice)
                .map(price -> price.subtract(mean).pow(2, MATH_CONTEXT))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal variance = varianceSum.divide(BigDecimal.valueOf(data.size()), MATH_CONTEXT);
        BigDecimal stdDev = sqrt(variance, 6); // Volatility = Standard deviation

        return stdDev;
    }

    private BigDecimal sqrt(BigDecimal value, int scale) {
        BigDecimal x0 = BigDecimal.ZERO;
        BigDecimal x1 = new BigDecimal(Math.sqrt(value.doubleValue()));

        while (!x0.equals(x1)) {
            x0 = x1;
            x1 = value.divide(x0, scale, RoundingMode.HALF_UP)
                    .add(x0)
                    .divide(BigDecimal.valueOf(2), scale, RoundingMode.HALF_UP);
        }

        return x1;
    }
}
