package com.IntelStream.presentation.inbound.controllers.query;

import com.IntelStream.application.query.dto.MarketDataQuery;
import com.IntelStream.application.query.dto.TopMoversQuery;
import com.IntelStream.application.query.handler.marketdata.*;
import com.IntelStream.presentation.dto.response.MarketDataResponse;
import com.IntelStream.shared.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Query controller for all market data read operations.
 * Follows SRP and SoC. Error handling is managed globally.
 */
@RestController
@RequestMapping("/api/market-data")
@RequiredArgsConstructor
public class MarketDataQueryController {

    private final GetMarketDataByIdQueryHandler getMarketDataByIdQueryHandler;
    private final GetLatestTickQueryHandler getLatestTickQueryHandler;
    private final GetLatestMarketDataQueryHandler getLatestMarketDataQueryHandler;
    private final GetMarketDataQueryHandler getMarketDataQueryHandler;
    private final GetExchangeMarketDataQueryHandler getExchangeMarketDataQueryHandler;
    private final GetPagedMarketDataQueryHandler getPagedMarketDataQueryHandler;
    private final GetTopMoversQueryHandler getTopMoversQueryHandler;

    // 1. Get Market Data by ID
    @GetMapping("/{id}")
    public ApiResponse<MarketDataResponse> getById(@PathVariable Long id) {
        return ApiResponse.ok(getMarketDataByIdQueryHandler.handle(id));
    }

    // 2. Get Latest Tick for a single instrument
    @GetMapping("/latest/{instrumentId}")
    public ApiResponse<MarketDataResponse> getLatestTick(@PathVariable Long instrumentId) {
        return ApiResponse.ok(getLatestTickQueryHandler.handle(instrumentId));
    }

    // 3. Get Latest Tick for multiple instruments
    @PostMapping("/latest")
    public ApiResponse<List<MarketDataResponse>> getLatestTicks(@RequestBody List<Long> instrumentIds) {
        return ApiResponse.ok(getLatestMarketDataQueryHandler.handle(instrumentIds));
    }

    // 4. Get Market Data range for an instrument (with sorting)
    @PostMapping("/range")
    public ApiResponse<List<MarketDataResponse>> getMarketDataInRange(@RequestBody MarketDataQuery query) {
        return ApiResponse.ok(getMarketDataQueryHandler.handle(query));
    }

    // 5. Get Market Data by exchange since a given time
    @GetMapping("/exchange/{exchangeId}/since")
    public ApiResponse<List<MarketDataResponse>> getExchangeMarketData(
            @PathVariable Long exchangeId,
            @RequestParam("since") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since) {
        return ApiResponse.ok(getExchangeMarketDataQueryHandler.handle(exchangeId, since));
    }

    // 6. Get paged Market Data for an instrument
    @GetMapping("/paged/{instrumentId}")
    public ApiResponse<List<MarketDataResponse>> getPagedMarketData(
            @PathVariable Long instrumentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ApiResponse.ok(getPagedMarketDataQueryHandler.handle(instrumentId, page, size));
    }

    // 7. Get top movers
    @PostMapping("/top-movers")
    public ApiResponse<List<MarketDataResponse>> getTopMovers(@RequestBody TopMoversQuery query) {
        return ApiResponse.ok(getTopMoversQueryHandler.handle(query));
    }
}