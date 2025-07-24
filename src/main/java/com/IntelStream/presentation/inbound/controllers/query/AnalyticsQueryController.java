package com.IntelStream.presentation.inbound.controllers.query;

import com.IntelStream.application.query.dto.AnalyticsQuery;
import com.IntelStream.application.query.handler.analytics.*;
import com.IntelStream.presentation.dto.response.AnalyticsResponse;
import com.IntelStream.shared.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Query controller for all analytics read operations.
 * Follows SRP and SoC. Error handling is managed globally.
 */
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsQueryController {

    private final GetAnalyticsByIdQueryHandler getAnalyticsByIdQueryHandler;
    private final GetLatestAnalyticsByInstrumentQueryHandler getLatestAnalyticsByInstrumentQueryHandler;
    private final CalculateRealTimeAnalyticsQueryHandler calculateRealTimeAnalyticsQueryHandler;
    private final GetOverboughtAnalyticsQueryHandler getOverboughtAnalyticsQueryHandler;
    private final GetOversoldAnalyticsQueryHandler getOversoldAnalyticsQueryHandler;
    private final GetTopVolatileAnalyticsQueryHandler getTopVolatileAnalyticsQueryHandler;
    private final SearchAnalyticsSnapshotsQueryHandler searchAnalyticsSnapshotsQueryHandler;

    // 1. Get analytics by snapshot ID
    @GetMapping("/{id}")
    public ApiResponse<AnalyticsResponse> getById(@PathVariable Long id) {
        return ApiResponse.ok(getAnalyticsByIdQueryHandler.handle(id));
    }

    // 2. Get latest analytics by instrument ID
    @GetMapping("/latest/{instrumentId}")
    public ApiResponse<AnalyticsResponse> getLatestByInstrument(@PathVariable Long instrumentId) {
        return ApiResponse.ok(getLatestAnalyticsByInstrumentQueryHandler.handle(instrumentId));
    }

    // 3. Calculate real-time analytics for an instrument
    @GetMapping("/realtime/{instrumentId}")
    public ApiResponse<AnalyticsResponse> calculateRealTime(@PathVariable Long instrumentId) {
        return ApiResponse.ok(calculateRealTimeAnalyticsQueryHandler.handle(instrumentId));
    }

    // 4. Get overbought analytics since a given time
    @GetMapping("/overbought")
    public ApiResponse<List<AnalyticsResponse>> getOverbought(
            @RequestParam("since") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since) {
        return ApiResponse.ok(getOverboughtAnalyticsQueryHandler.handle(since));
    }

    // 5. Get oversold analytics since a given time
    @GetMapping("/oversold")
    public ApiResponse<List<AnalyticsResponse>> getOversold(
            @RequestParam("since") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since) {
        return ApiResponse.ok(getOversoldAnalyticsQueryHandler.handle(since));
    }

    // 6. Get top volatile analytics since a given time (limit required)
    @GetMapping("/top-volatile")
    public ApiResponse<List<AnalyticsResponse>> getTopVolatile(
            @RequestParam("limit") int limit,
            @RequestParam("since") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since) {
        return ApiResponse.ok(getTopVolatileAnalyticsQueryHandler.handle(limit, since));
    }

    // 7. Search analytics snapshots (complex query)
    @PostMapping("/search")
    public ApiResponse<List<AnalyticsResponse>> search(@RequestBody AnalyticsQuery query) {
        return ApiResponse.ok(searchAnalyticsSnapshotsQueryHandler.handle(query));
    }
}