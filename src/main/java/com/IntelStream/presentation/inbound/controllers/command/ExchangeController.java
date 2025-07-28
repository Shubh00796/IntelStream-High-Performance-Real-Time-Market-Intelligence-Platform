package com.IntelStream.presentation.inbound.controllers.command;

import com.IntelStream.application.command.handler.ExchangeCommandHandler;
import com.IntelStream.domain.model.Exchange;
import com.IntelStream.shared.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/exchanges")
@RequiredArgsConstructor
public class ExchangeController {

    private final ExchangeCommandHandler exchangeCommandHandler;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createExchange(@RequestBody Exchange exchange) {
        Long id = exchangeCommandHandler.handleCreate(exchange);
        return ResponseEntity.ok(ApiResponse.ok(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivateExchange(@PathVariable Long id) {
        exchangeCommandHandler.handleDeactivate(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PostMapping("/can-trade")
    public ResponseEntity<ApiResponse<Boolean>> canTrade(@RequestBody Exchange exchange) {
        boolean tradable = exchangeCommandHandler.canTrade(exchange, LocalDateTime.now());
        return ResponseEntity.ok(ApiResponse.ok(tradable));
    }


}
