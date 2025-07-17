package com.IntelStream.domain.service.analytics.calculator;

import com.IntelStream.domain.model.MarketData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
@Slf4j
public class RSIIndicatorCalculator {

    private static final int DEFAULT_PERIOD = 14;

    public BigDecimal calculate(List<MarketData> data) {
        if (isInsufficientData(data)) return BigDecimal.ZERO;

        List<BigDecimal> priceDifferences = calculatePriceDifferences(data);
        BigDecimal gain = totalGains(priceDifferences);
        BigDecimal loss = totalLosses(priceDifferences);

        return calculateRSI(gain, loss);
    }

    private boolean isInsufficientData(List<MarketData> data) {
        boolean insufficient = data == null || data.size() <= DEFAULT_PERIOD;
        if (insufficient) {
            log.warn("Insufficient data for RSI. Required: {}, Provided: {}", DEFAULT_PERIOD, data == null ? 0 : data.size());
        }
        return insufficient;
    }

    private List<BigDecimal> calculatePriceDifferences(List<MarketData> data) {
        return data.subList(data.size() - DEFAULT_PERIOD - 1, data.size()).stream()
                .map(MarketData::getPrice)
                .reduce(new java.util.ArrayList<BigDecimal>(), (acc, price) -> {
                    if (!acc.isEmpty()) acc.add(price.subtract(acc.get(acc.size() - 1)));
                    else acc.add(price);
                    return acc;
                }, (a, b) -> { a.addAll(b); return a; })
                .subList(1, DEFAULT_PERIOD + 1); // remove first dummy element
    }

    private BigDecimal totalGains(List<BigDecimal> differences) {
        return differences.stream()
                .filter(diff -> diff.compareTo(BigDecimal.ZERO) > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal totalLosses(List<BigDecimal> differences) {
        return differences.stream()
                .filter(diff -> diff.compareTo(BigDecimal.ZERO) < 0)
                .map(BigDecimal::abs)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateRSI(BigDecimal gain, BigDecimal loss) {
        if (gain.add(loss).compareTo(BigDecimal.ZERO) == 0) return BigDecimal.valueOf(50);
        if (loss.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.valueOf(100);

        BigDecimal avgGain = gain.divide(BigDecimal.valueOf(DEFAULT_PERIOD), 6, RoundingMode.HALF_UP);
        BigDecimal avgLoss = loss.divide(BigDecimal.valueOf(DEFAULT_PERIOD), 6, RoundingMode.HALF_UP);
        BigDecimal rs = avgGain.divide(avgLoss, 6, RoundingMode.HALF_UP);

        return BigDecimal.valueOf(100).subtract(
                BigDecimal.valueOf(100).divide(BigDecimal.ONE.add(rs), 2, RoundingMode.HALF_UP)
        );
    }
}
