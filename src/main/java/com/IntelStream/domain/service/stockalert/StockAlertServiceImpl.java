package com.IntelStream.domain.service.stockalert;

import com.IntelStream.domain.model.StockAlert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StockAlertServiceImpl implements StockAlertService {
    @Override
    public List<StockAlert> filterSevereAlerts(List<StockAlert> alerts) {
        return alerts.stream()
                .filter(StockAlert::isSevere)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockAlert> unacknowledgedAlerts(List<StockAlert> alerts) {
        return alerts.stream()
                .filter(StockAlert::isUnacknowledged)
                .collect(Collectors.toList());
    }

    @Override
    public Map<StockAlert.AlertType, Long> countByAlertType(List<StockAlert> alerts) {
        return alerts
                .stream()
                .collect(Collectors.groupingBy(StockAlert::getType,
                        Collectors.counting()));
    }

    @Override
    public List<StockAlert> sortBySeverity(List<StockAlert> alerts) {
        return alerts
                .stream()
                .sorted(Comparator.comparingDouble(StockAlert::getSeverityScore).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<StockAlert> latestAlertForSymbol(List<StockAlert> alerts, String symbol) {
        return alerts
                .stream()
                .filter(stockAlert -> stockAlert.getSymbol().equalsIgnoreCase(symbol))
                .max(Comparator.comparing(StockAlert::getTriggeredAt));

    }

    @Override
    public Map<String, List<StockAlert>> groupBySymbol(List<StockAlert> alerts) {
        return alerts
                .stream()
                .collect(Collectors.groupingBy(StockAlert::getSymbol));
    }

    @Override
    public long countAcknowledged(List<StockAlert> alerts) {
        return alerts
                .stream()
                .filter(StockAlert::isAcknowledged)
                .count();
    }

    @Override
    public boolean allSevereAlertsAcknowledged(List<StockAlert> alerts) {
        return alerts
                .stream()
                .filter(StockAlert::isSevere)
                .allMatch(StockAlert::isAcknowledged);
    }

    @Override
    public List<String> distinctSymbolsInAlerts(List<StockAlert> alerts) {
        return alerts
                .stream()
                .map(stockAlert -> stockAlert.getSymbol())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<StockAlert> filterByTimeRange(List<StockAlert> alerts, LocalDateTime from, LocalDateTime to) {
        return alerts
                .stream()
                .filter(stockAlert -> !stockAlert.getTriggeredAt().isBefore(from) && !stockAlert.getTriggeredAt().isAfter(to))
                .collect(Collectors.toList());
    }
}
