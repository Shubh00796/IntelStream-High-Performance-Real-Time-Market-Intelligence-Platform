package com.IntelStream.application.processor;

import com.IntelStream.domain.model.AnalyticsSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;
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

    //8. Filter by InstrumentId and RsI
    public List<AnalyticsSnapshot> filterByInstrumentIdAndRsi(Long instrumentId, BigDecimal rsi) {
        return snapshots
                .stream()
                .filter(analyticsSnapshot -> analyticsSnapshot.getInstrumentId().equals(instrumentId) && analyticsSnapshot.getRsi().equals(rsi))
                .toList();
    }

    //9. Group by InstrumentId
    public Map<Long, List<AnalyticsSnapshot>> getSnapshotByInstrumentId() {
        return snapshots
                .stream()
                .collect(Collectors.groupingBy(AnalyticsSnapshot::getInstrumentId));
    }

    //10. Group by RSI Status
    public Map<String, List<AnalyticsSnapshot>> getSnapshotsRsiStatus() {
        Map<Predicate<BigDecimal>, String> rsiCategories = new LinkedHashMap<>();
        rsiCategories.put(rsi -> rsi.compareTo(BigDecimal.valueOf(70)) > 0, "Overbought");

        rsiCategories.put(rsi -> rsi.compareTo(BigDecimal.valueOf(30)) < 0, "Oversold");
        rsiCategories.put(rsi -> true, "Neutral"); // default case

        return snapshots.stream()
                .collect(Collectors.groupingBy(snapshot ->
                        Optional.ofNullable(snapshot.getRsi())
                                .map(rsi -> rsiCategories.entrySet().stream()
                                        .filter(entry -> entry.getKey().test(rsi))
                                        .map(Map.Entry::getValue)
                                        .findFirst()
                                        .orElse("Neutral"))
                                .orElse("Unknown")
                ));
    }

    //11. Group by Volatility Levels
    public Map<String, List<AnalyticsSnapshot>> getSnapshotsByVolatilityLevels() {
        Map<Predicate<BigDecimal>, String> volatilityCategories = new LinkedHashMap<>();

        volatilityCategories.put(volatility -> volatility.compareTo(BigDecimal.valueOf(0.5)) < 0, "Low Volatility");
        volatilityCategories.put(volatility -> volatility.compareTo(BigDecimal.valueOf(1.0)) < 0, "Medium Volatility");
        volatilityCategories.put(volatility -> true, "High Volatility"); // default case


        return snapshots
                .stream()
                .collect(Collectors.groupingBy(snapshot ->
                        Optional.ofNullable(snapshot.getVolatility())
                                .map(volatility -> volatilityCategories.entrySet().stream()
                                        .filter(entry -> entry.getKey().test(volatility))
                                        .map(Map.Entry::getValue)
                                        .findFirst()
                                        .orElse("Unknown"))
                                .orElse("Unknown")
                ));
    }

    //12. Group By Price Change Percent
    public Map<String, List<AnalyticsSnapshot>> getSnapshotsByPriceChangePercent() {
        Map<Predicate<BigDecimal>, String> priceChangeCategories = new LinkedHashMap<>();
        priceChangeCategories.put(priceChange -> priceChange.compareTo(BigDecimal.valueOf(0)) > 0, "Positive Change");
        priceChangeCategories.put(priceChange -> priceChange.compareTo(BigDecimal.ZERO) < 0, "Negative Change");
        priceChangeCategories.put(priceChange -> true, "No Change"); // default case

        return snapshots
                .stream()
                .collect(Collectors.groupingBy(snapshot -> Optional.ofNullable(snapshot.getPriceChangePercent24h())
                        .map(priceChange -> priceChangeCategories.entrySet().stream()
                                .filter(entry -> entry.getKey().test(priceChange))
                                .map(Map.Entry::getValue)
                                .findFirst()
                                .orElse("Unknown"))
                        .orElse("Unknown")
                ));
    }

    //13. Group by Volume Levels
    public Map<String, List<AnalyticsSnapshot>> getSnapshotsByVolumeLevels() {
        Map<Predicate<BigDecimal>, String> volumeCategories = new LinkedHashMap<>();
        volumeCategories.put(volume -> volume.compareTo(BigDecimal.valueOf(1000000)) < 0, "Low Volume");
        volumeCategories.put(volume -> volume.compareTo(BigDecimal.valueOf(10000000)) < 0, "Medium Volume");
        volumeCategories.put(volume -> true, "High Volume"); // default case

        return snapshots
                .stream()
                .collect(Collectors.groupingBy(snapshot -> Optional.ofNullable(snapshot.getVolume24h())
                        .map(volume -> volumeCategories.entrySet().stream()
                                .filter(entry -> entry.getKey().test(volume))
                                .map(Map.Entry::getValue)
                                .findFirst()
                                .orElse("Unknown"))
                        .orElse("Unknown")
                ));
    }
    // Filter by InstrumentId and Volatility
    public List<AnalyticsSnapshot> filterByInstrumentIdAndVolatility(Long instrumentId, BigDecimal volatility) {
        return snapshots
                .stream()
                .filter(analyticsSnapshot -> analyticsSnapshot.getInstrumentId().equals(instrumentId) && analyticsSnapshot.getVolatility().equals(volatility))
                .toList();
    }

 


}
