package com.IntelStream.presentation.inbound.controllers.query;

import com.IntelStream.application.query.dto.InstrumentSearchQuery;
import com.IntelStream.application.query.handler.instruments.GetInstrumentByIdQueryHandler;
import com.IntelStream.application.query.handler.instruments.GetInstrumentBySymbolQueryHandler;
import com.IntelStream.application.query.handler.instruments.GetInstrumentsByExchangeQueryHandler;
import com.IntelStream.application.query.handler.instruments.SearchInstrumentsQueryHandler;
import com.IntelStream.presentation.dto.response.InstrumentResponse;
import com.IntelStream.shared.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Query controller for all instrument read operations.
 * Follows SRP and SoC. Error handling is managed globally.
 */
@RestController
@RequestMapping("/api/instruments")
@RequiredArgsConstructor
public class InstrumentQueryController {

    private final GetInstrumentByIdQueryHandler getInstrumentByIdQueryHandler;
    private final GetInstrumentBySymbolQueryHandler getInstrumentBySymbolQueryHandler;
    private final GetInstrumentsByExchangeQueryHandler getInstrumentsByExchangeQueryHandler;
    private final SearchInstrumentsQueryHandler searchInstrumentsQueryHandler;

    // 1. Get instrument by ID
    @GetMapping("/{id}")
    public ApiResponse<InstrumentResponse> getById(@PathVariable Long id) {
        return ApiResponse.ok(getInstrumentByIdQueryHandler.handle(id));
    }

    // 2. Get instrument by symbol
    @GetMapping("/symbol/{symbol}")
    public ApiResponse<InstrumentResponse> getBySymbol(@PathVariable String symbol) {
        return ApiResponse.ok(getInstrumentBySymbolQueryHandler.handle(symbol));
    }

    // 3. Get all instruments for an exchange (paged)
    @GetMapping("/exchange/{exchangeId}")
    public ApiResponse<List<InstrumentResponse>> getByExchangePaged(
            @PathVariable Long exchangeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ApiResponse.ok(getInstrumentsByExchangeQueryHandler.handle(exchangeId, page, size));
    }

    // 4. Search instruments (by name, sector, etc.)
    @PostMapping("/search")
    public ApiResponse<List<InstrumentResponse>> search(@RequestBody InstrumentSearchQuery query) {
        return ApiResponse.ok(searchInstrumentsQueryHandler.handle(query));
    }
}