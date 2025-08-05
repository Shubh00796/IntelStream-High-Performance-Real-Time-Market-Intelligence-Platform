package com.IntelStream.domain.service.tradeanomaly;

import com.IntelStream.domain.model.TradeAnomaly;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TradeAnomalyServiceImpl implements TradeAnomalyService {
    @Override
    public List<TradeAnomaly> filterSevereAnomalies(List<TradeAnomaly> anomalies) {
        return anomalies
                .stream()
                .filter(TradeAnomaly::isSevere)
                .collect(Collectors.toList());
    }

    @Override
    public List<TradeAnomaly> unresolvedAnomalies(List<TradeAnomaly> anomalies) {
        return anomalies
                .stream()
                .filter(TradeAnomaly::isUnresolved)
                .collect(Collectors.toList());
    }

    @Override
    public Map<TradeAnomaly.AnomalyType, Long> countByAnomalyType(List<TradeAnomaly> anomalies) {
        return anomalies.stream()
                .collect(Collectors.groupingBy(TradeAnomaly::getType,
                        Collectors.counting()));
    }

    @Override
    public Optional<TradeAnomaly> latestAnomaly(List<TradeAnomaly> anomalies) {
        return anomalies
                .stream()
                .max(Comparator.comparing(TradeAnomaly::getDetectedAt));

    }

    @Override
    public List<TradeAnomaly> sortBySeverity(List<TradeAnomaly> anomalies) {
        return anomalies.stream()
                .filter(TradeAnomaly::isSevere)
                .sorted(Comparator.comparing(TradeAnomaly::getSeverityScore).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getDistinctSymbols(List<TradeAnomaly> anomalies) {
        return anomalies.stream()
                .map(TradeAnomaly::getSymbol)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<TradeAnomaly>> groupBySymbol(List<TradeAnomaly> anomalies) {
        return anomalies.stream()
                .collect(Collectors.groupingBy(
                        TradeAnomaly::getSymbol
                ));
    }

    @Override
    public Map<String, Double> averageSeverityBySymbol(List<TradeAnomaly> anomalies) {
        return anomalies.stream()
                .collect(Collectors.groupingBy(
                        TradeAnomaly::getSymbol,
                        Collectors.averagingDouble(TradeAnomaly::getSeverityScore)
                ));
    }

    @Override
    public List<TradeAnomaly> anomaliesInTimeRange(List<TradeAnomaly> anomalies, LocalDateTime from, LocalDateTime to) {
        return anomalies
                .stream()
                .filter(tradeAnomaly -> !tradeAnomaly.getDetectedAt().isBefore(from) && !tradeAnomaly.getDetectedAt().isAfter(to))
                .collect(Collectors.toList());
    }

    @Override
    public boolean allResolved(List<TradeAnomaly> anomalies) {
        return anomalies
                .stream()
                .allMatch(TradeAnomaly::isResolved);
    }
}
