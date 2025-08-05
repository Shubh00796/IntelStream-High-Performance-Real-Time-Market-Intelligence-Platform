package com.IntelStream.domain.service.stockalert;


import com.IntelStream.domain.model.StockAlert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface StockAlertService {

    List<StockAlert> filterSevereAlerts(List<StockAlert> alerts);

    List<StockAlert> unacknowledgedAlerts(List<StockAlert> alerts);

    Map<StockAlert.AlertType, Long> countByAlertType(List<StockAlert> alerts);

    List<StockAlert> sortBySeverity(List<StockAlert> alerts);

    Optional<StockAlert> latestAlertForSymbol(List<StockAlert> alerts, String symbol);

    Map<String, List<StockAlert>> groupBySymbol(List<StockAlert> alerts);

    long countAcknowledged(List<StockAlert> alerts);

    boolean allSevereAlertsAcknowledged(List<StockAlert> alerts);

    List<String> distinctSymbolsInAlerts(List<StockAlert> alerts);

    List<StockAlert> filterByTimeRange(List<StockAlert> alerts, LocalDateTime from, LocalDateTime to);
}
