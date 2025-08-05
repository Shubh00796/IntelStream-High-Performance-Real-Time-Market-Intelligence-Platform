package com.IntelStream.domain.service.tradeanomaly;


import com.IntelStream.domain.model.TradeAnomaly;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TradeAnomalyService {

    List<TradeAnomaly> filterSevereAnomalies(List<TradeAnomaly> anomalies);

    List<TradeAnomaly> unresolvedAnomalies(List<TradeAnomaly> anomalies);

    Map<TradeAnomaly.AnomalyType, Long> countByAnomalyType(List<TradeAnomaly> anomalies);

    Optional<TradeAnomaly> latestAnomaly(List<TradeAnomaly> anomalies);

    List<TradeAnomaly> sortBySeverity(List<TradeAnomaly> anomalies);

    List<String> getDistinctSymbols(List<TradeAnomaly> anomalies);

    Map<String, List<TradeAnomaly>> groupBySymbol(List<TradeAnomaly> anomalies);

    Map<String, Double> averageSeverityBySymbol(List<TradeAnomaly> anomalies);

    List<TradeAnomaly> anomaliesInTimeRange(List<TradeAnomaly> anomalies, LocalDateTime from, LocalDateTime to);

    boolean allResolved(List<TradeAnomaly> anomalies);
}
