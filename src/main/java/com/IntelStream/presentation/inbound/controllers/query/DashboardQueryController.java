package com.IntelStream.presentation.inbound.controllers.query;

import com.IntelStream.application.query.dto.DashboardQuery;
import com.IntelStream.application.query.dto.DashboardResponse;
import com.IntelStream.application.query.handler.dashboard.DashboardQueryHandler;
import com.IntelStream.shared.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardQueryController {

    private final DashboardQueryHandler dashboardQueryHandler;

    @GetMapping
    public ApiResponse<DashboardResponse> getDashboardData(
            @RequestParam List<Long> instrumentIds,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime asOf,
            @RequestParam(defaultValue = "true") boolean includeLatestPrice,
            @RequestParam(defaultValue = "true") boolean includeAnalytics,
            @RequestParam(defaultValue = "false") boolean includePriceHistory,
            @RequestParam(defaultValue = "24") int priceHistoryHours
    ) {
        DashboardQuery query = buildDashboardQuery(
                instrumentIds, asOf, includeLatestPrice, includeAnalytics, includePriceHistory, priceHistoryHours
        );

        DashboardResponse response = dashboardQueryHandler.handle(query);
        return ApiResponse.ok(response);
    }

    private DashboardQuery buildDashboardQuery(
            List<Long> instrumentIds,
            LocalDateTime asOf,
            boolean includeLatestPrice,
            boolean includeAnalytics,
            boolean includePriceHistory,
            int priceHistoryHours
    ) {
        return DashboardQuery.builder()
                .instrumentIds(instrumentIds)
                .asOf(asOf)
                .includeLatestPrice(includeLatestPrice)
                .includeAnalytics(includeAnalytics)
                .includePriceHistory(includePriceHistory)
                .priceHistoryHours(priceHistoryHours)
                .build();
    }
}
