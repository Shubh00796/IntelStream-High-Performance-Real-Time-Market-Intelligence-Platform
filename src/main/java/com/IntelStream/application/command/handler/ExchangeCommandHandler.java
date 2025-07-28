package com.IntelStream.application.command.handler;

import com.IntelStream.application.command.service.ExchangeCommandService;
import com.IntelStream.application.service.ExchangeStrategyService;
import com.IntelStream.domain.model.Exchange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ExchangeCommandHandler {

    private final ExchangeCommandService service;
    private final ExchangeStrategyService exchangeStrategyService;

    public Long handleCreate(Exchange exchange) {
        return service.createExchange(exchange);
    }



    public void handleDeactivate(Long exchangeId) {
        service.deactivateExchange(exchangeId);
    }

    public boolean canTrade(Exchange exchange, LocalDateTime now) {
        // Trading eligibility logic directly embedded
        return exchange.isActive() && exchangeStrategyService.isOpen(exchange, now);
    }
}
