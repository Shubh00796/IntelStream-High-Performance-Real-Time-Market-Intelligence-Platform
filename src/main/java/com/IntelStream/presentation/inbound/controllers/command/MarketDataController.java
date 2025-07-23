package com.IntelStream.presentation.inbound.controllers.command;

import com.IntelStream.application.command.dto.BulkUpdateMarketDataCommand;
import com.IntelStream.application.command.dto.UpdateMarketDataCommand;
import com.IntelStream.application.command.handler.MarketDataCommandHandler;
import com.IntelStream.shared.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/market-data")
@RequiredArgsConstructor
public class MarketDataController {

    private final MarketDataCommandHandler marketDataCommandHandler;

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<Long>> updateMarketData(@RequestBody UpdateMarketDataCommand command) {
        Long id = marketDataCommandHandler.handle(command);
        return ResponseEntity.ok(ApiResponse.ok(id));
    }

    @PostMapping("/bulk")
    public CompletableFuture<ResponseEntity<ApiResponse<Integer>>> bulkUpdate(@RequestBody BulkUpdateMarketDataCommand command) {
        return marketDataCommandHandler.handleBulkAsync(command)
                .thenApply(count -> ResponseEntity.ok(ApiResponse.ok(count)));
    }
}
