package com.IntelStream.application.command.handler;

import com.IntelStream.application.command.dto.BulkUpdateMarketDataCommand;
import com.IntelStream.application.command.dto.UpdateMarketDataCommand;
import com.IntelStream.application.command.service.MarketDataCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class MarketDataCommandHandler {

    private final MarketDataCommandService service;

    public Long handle(UpdateMarketDataCommand command) {
        return service.updateMarketData(command);
    }

    @Async
    public CompletableFuture<Integer> handleBulkAsync(BulkUpdateMarketDataCommand cmd) {
        return CompletableFuture.supplyAsync(() -> service.bulkUpdateMarketData(cmd));
    }
}
