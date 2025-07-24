package com.IntelStream.presentation.inbound.controllers.query;


import com.IntelStream.application.query.handler.exchnage.*;
import com.IntelStream.presentation.dto.response.ExchangeResponse;
import com.IntelStream.shared.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Query controller for all exchange read operations.
 * Follows SRP and SoC. Error handling is managed globally.
 */
@RestController
@RequestMapping("/api/exchanges")
@RequiredArgsConstructor
public class ExchangeQueryController {

    private final GetExchangeByIdQueryHandler getExchangeByIdQueryHandler;
    private final GetExchangeByCodeQueryHandler getExchangeByCodeQueryHandler;
    private final GetAllActiveExchangesQueryHandler getAllActiveExchangesQueryHandler;
    private final GetExchangesByCurrencyQueryHandler getExchangesByCurrencyQueryHandler;
    private final GetExchangesByTimezoneQueryHandler getExchangesByTimezoneQueryHandler;
    private final GetOpenMarketsAtQueryHandler getOpenMarketsAtQueryHandler;
    private final IsMarketOpenQueryHandler isMarketOpenQueryHandler;

    // 1. Get exchange by ID
    @GetMapping("/{id}")
    public ApiResponse<ExchangeResponse> getById(@PathVariable Long id) {
        return ApiResponse.ok(getExchangeByIdQueryHandler.handle(id));
    }

    // 2. Get exchange by code
    @GetMapping("/code/{code}")
    public ApiResponse<ExchangeResponse> getByCode(@PathVariable String code) {
        return ApiResponse.ok(getExchangeByCodeQueryHandler.handle(code));
    }

    // 3. Get all active exchanges
    @GetMapping("/active")
    public ApiResponse<List<ExchangeResponse>> getAllActive() {
        return ApiResponse.ok(getAllActiveExchangesQueryHandler.handle());
    }

    // 4. Get exchanges by currency
    @GetMapping("/currency/{currency}")
    public ApiResponse<List<ExchangeResponse>> getByCurrency(@PathVariable String currency) {
        return ApiResponse.ok(getExchangesByCurrencyQueryHandler.handle(currency));
    }

    // 5. Get exchanges by timezone
    @GetMapping("/timezone/{timezone}")
    public ApiResponse<List<ExchangeResponse>> getByTimezone(@PathVariable String timezone) {
        return ApiResponse.ok(getExchangesByTimezoneQueryHandler.handle(timezone));
    }

    // 6. Get open markets at a given time
    @GetMapping("/open-at")
    public ApiResponse<List<ExchangeResponse>> getOpenMarketsAt(
            @RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime currentTime) {
        return ApiResponse.ok(getOpenMarketsAtQueryHandler.handle(currentTime));
    }

    // 7. Is a market open at a given time for an exchange
    @GetMapping("/{exchangeId}/is-open")
    public ApiResponse<Boolean> isMarketOpen(
            @PathVariable Long exchangeId,
            @RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime currentTime) {
        return ApiResponse.ok(isMarketOpenQueryHandler.handle(exchangeId, currentTime));
    }
}