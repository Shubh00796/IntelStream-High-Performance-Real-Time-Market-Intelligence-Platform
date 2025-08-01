package com.IntelStream.application.processor;

import com.IntelStream.domain.model.AnalyticsSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class AnalyticsSnapshotProcessor {

    private final List<AnalyticsSnapshot> snapshots;

    //1. Filter Overbrought Snapshots
    public List<AnalyticsSnapshot> filterOverBroughtSnapshots() {
        return snapshots
                .stream()
                .filter(AnalyticsSnapshot::isOverbought)
                .toList();
    }

    //2. Filter Oversold Snapshots
    public List<AnalyticsSnapshot> filterOversoldSnapshots() {
        return snapshots
                .stream()
                .filter(AnalyticsSnapshot::isOversold)
                .toList();
    }

    //3. Filter by Volatility Above Threshold
    public List<AnalyticsSnapshot> filterByVolatilityAboveThreshold(BigDecimal threshold) {
        return snapshots
                .stream()
                .filter(analyticsSnapshot -> analyticsSnapshot.getVolatility() != null && analyticsSnapshot.getVolatility().compareTo(threshold) > 0)
                .toList();
    }

    //4. Filter By RSI in between
    public List<AnalyticsSnapshot> filterRSIInBetween(BigDecimal max, BigDecimal Min) {
        return snapshots
                .stream()
                .filter(analyticsSnapshot -> analyticsSnapshot.getRsi() != null && analyticsSnapshot.getRsi().compareTo(max) < 0 && analyticsSnapshot.getRsi().compareTo(Min) > 0)
                .toList();
    }

    //5. Filter by Price change percent above
    public List<AnalyticsSnapshot> filterByPriceChangePercentAbove(BigDecimal threshold) {
        return snapshots
                .stream()
                .filter(analyticsSnapshot -> analyticsSnapshot.getPriceChangePercent24h().compareTo(threshold) > 0)
                .toList();
    }

    //7. Filter by Volume above
    public List<AnalyticsSnapshot> filterByVolumeAbove(BigDecimal threshold) {
        return snapshots
                .stream()
                .filter(analyticsSnapshot -> analyticsSnapshot.getVolume24h() != null && analyticsSnapshot.getVolume24h().compareTo(threshold) > 0)
                .toList();
    }

    //8. Filter by Instrument Id and RsI
    public List<AnalyticsSnapshot> filterByInstrumentIdAndRsi(Long instrumentId, BigDecimal rsi) {
        return snapshots
                .stream()
                .filter(analyticsSnapshot -> analyticsSnapshot.getInstrumentId().equals(instrumentId) && analyticsSnapshot.getRsi().equals(rsi))
                .toList();
    }


}
